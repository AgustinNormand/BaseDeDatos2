/*
 * mvcc2.c
 * 
 * Copyright 2017 grchere <grchere@debian2>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 * Implementacion mvcc con rts,wts
 * Idea de re-start: no hacer nada a nivel hilo, sino manejarlo con la
 * estructura en memoria MVCC2_TID agregar las operaciones que fue haciendo
 * la T entonces se puede hacer rollback y volver a generar un nuevo numero y
 * volver a aplicar las operaciones -> problema: loop infinito.
 */


#include "mvcc2.h"

// funciones privadas
MVCC2_RECORD *mvcc2_new_record(MVCC2_TID *tid);
MVCC2_RECORD_VERSION *mvcc2_new_record_version(MVCC2_TID *tid,MVCC2_RECORD *r);
MVCC2_RECORD_VERSION *mvcc2_get_record_version(MVCC2_TID *tid,MVCC2_RECORD *r);
short mvcc2_deactivate(MVCC2_TID *tid);

// hilos, transacciones de prueba
void *transaccion1(void *);
void *transaccion2(void *);
void *transaccion3(void *);

// TESTING
int main(int argc, char **argv) {
	pthread_t th1,th2,th3;
	printf("main(): inicio\n");
	pthread_create(&th1,NULL,transaccion1,NULL);
	pthread_create(&th2,NULL,transaccion2,NULL);
	sleep(2);
	pthread_create(&th3,NULL,transaccion3,NULL);
	printf("main(): hago join() esperando la finalizacion de los hilos\n");
	pthread_join(th1,NULL);
	pthread_join(th2,NULL);
	pthread_join(th3,NULL);
	mvcc2_print_visible_records(mvcc2_new_tid());
	mvcc2_print_records();
	printf("main(): fin\n");
	return 0;
}

// *********************************************************************
// TESTING TRANSACTIONS

void *transaccion1(void *p) {
	printf("transaccion1: inicio\n");
	MVCC2_TID *tr = mvcc2_new_tid();
	mvcc2_add_record(tr,"juan");
	mvcc2_add_record(tr,"pedro");
	mvcc2_print_active_tids();
	mvcc2_print_records();
	mvcc2_commit(tr);
	mvcc2_print_active_tids();
	mvcc2_print_records();
	printf("transaccion1: fin\n");
	pthread_exit(NULL);
}

void *transaccion2(void *p) {
	printf("transaccion2: inicio\n");
	MVCC2_TID *tr = mvcc2_new_tid();
	mvcc2_add_record(tr,"ana");
	mvcc2_add_record(tr,"estela");
	mvcc2_print_active_tids();
	mvcc2_print_records();
	mvcc2_commit(tr);
	mvcc2_print_active_tids();
	mvcc2_print_records();
	printf("transaccion2: fin\n");
	pthread_exit(NULL);
}
void *transaccion3(void *p) {
	printf("transaccion3: inicio\n");
	MVCC2_TID *tr = mvcc2_new_tid();
	short r = mvcc2_delete_record(tr,"pedro");
	if ( r ) printf("transaccion3: delete ok!\n");
	else     printf("transaccion3: delete error!\n");
	mvcc2_commit(tr);
	printf("transaccion3: fin\n");
	pthread_exit(NULL);
}

// END TESTING TRANSACTIONS
// *********************************************************************


MVCC2_TID *mvcc2_new_tid() {
	if ( mvcc2_active_tids_count >= MVCC2_MAX_TIDS ) {
		fprintf(stderr,"mvcc2_new_tid():Pretende superar la cantidad maxima de transacciones activas permitidas [%d]\n",MVCC2_MAX_TIDS); 
		return NULL;
	}
	pthread_mutex_lock(&mvcc2_m1);
		int r=mvcc2_next_tid++;
		mvcc2_active_tids_count++;
	pthread_mutex_unlock(&mvcc2_m1);
	if ( mvcc2_active_tids == NULL ) {
		pthread_mutex_lock(&mvcc2_m1);
			mvcc2_active_tids = (MVCC2_TID *) malloc(sizeof(MVCC2_TID)*MVCC2_MAX_TIDS);
			memset(mvcc2_active_tids,0,sizeof(MVCC2_TID)*MVCC2_MAX_TIDS);
			mvcc2_active_tids->tid=r;
			memset(mvcc2_active_tids->rollback,0,sizeof(MVCC2_OP)*MVCC2_MAX_ROLLBACK_OPS);
			memset(mvcc2_active_tids->redo,0,sizeof(MVCC2_OP)*MVCC2_MAX_ROLLBACK_OPS);
			mvcc2_active_tids->lop=0;
		pthread_mutex_unlock(&mvcc2_m1);
		return mvcc2_active_tids;
	} else {
		// busco "hueco" dejado en vector de transacciones activas por commit para su reuso
		// en caso de no encontrar hueco, agrego al final
		MVCC2_TID *p = mvcc2_active_tids;
		int i;
		for(i=0;i<mvcc2_active_tids_count;p++) {
			if ( p->tid != 0 ) {
				i++;
			} else {
				// hueco o bien llegue al final
				p->tid=r;
				memset(p->rollback,0,sizeof(MVCC2_OP)*MVCC2_MAX_ROLLBACK_OPS);
				memset(p->redo,0,sizeof(MVCC2_OP)*MVCC2_MAX_ROLLBACK_OPS);
				p->lop=0;
				return p;
			}
		}
		pthread_mutex_lock(&mvcc2_m1);
			mvcc2_active_tids_count--;
		pthread_mutex_unlock(&mvcc2_m1);
		fprintf(stderr,"mvcc2_new_tid():Imposible crear nueva transaccion!, no se encontro lugar en arreglo de transacciones!\n"); 
		return NULL;
	}
}
void mvcc2_print_active_tids() {
	int i;
	MVCC2_TID *p=mvcc2_active_tids;
	printf("Active TIDs:");
	for(i=0;p != NULL && i<mvcc2_active_tids_count;p++) {
		if ( p->tid != 0 ) {
			printf(" %d",p->tid);
			i++;
		}
	}
	printf("\n");
}
short mvcc2_is_active_tid(MVCC2_TID *tid) {
	short r=0;
	int i;
	MVCC2_TID *p=mvcc2_active_tids;
	for(i=0;p != NULL && i<mvcc2_active_tids_count;p++) {
		if ( p->tid != 0 ) {
			if ( p->tid == tid->tid ) return (short) 1;
			i++;
		}
	}
	return r;
}
short mvcc2_is_active_ntid(int tid) {
	short r=0;
	int i;
	MVCC2_TID *p=mvcc2_active_tids;
	for(i=0;p != NULL && i<mvcc2_active_tids_count;p++) {
		if ( p->tid != 0 ) {
			if ( p->tid == tid ) return (short) 1;
			i++;
		}
	}
	return r;
}
/**
 * quita transaccion del arreglo de transacciones activas
 * 
 */
short mvcc2_deactivate(MVCC2_TID *tid) {
	int i;
	short ret=0;
	MVCC2_TID *p=mvcc2_active_tids;
	for(i=0;i<mvcc2_active_tids_count;p++,i++) {
		if ( p->tid != 0 ) {
			if ( tid->tid == p->tid ) {
				memset(p,0,sizeof(MVCC2_TID));
				ret=1;
				break;
			}
		}
	}
	return ret;
}
/**
 * get record version no implica lectura (no actualiza rts), simplemente devuelve la ultima
 * version disponible del registro para esta transaccion
 * devuelve NULL en caso de que no exista ninguna version para esta transaccion
 */
MVCC2_RECORD_VERSION *mvcc2_get_record_version(MVCC2_TID *tid,MVCC2_RECORD *r) {
	if ( r == NULL || tid == NULL || r->versions == NULL || r->count == 0 ) return NULL;
	// si esta borrado el registro por una T anterior o por ella misma -> no existe registro
	if ( r->expired_tid <= tid->tid ) return NULL;
	int i;
	MVCC2_RECORD_VERSION *rv = r->versions;
	MVCC2_RECORD_VERSION *p = NULL;
	int lastWts = 0;
	// recorre de menor a mayor (posiblemente sea mas rapido hacerlo en orden inverso)
	for(i=0;i < r->count;i++,rv++) {
		if ( rv->wts < tid->tid ) {
			if ( p == NULL ) {
				p = rv;
				lastWts=rv->wts;
			} else {
				if ( rv->wts > lastWts ) {
					lastWts = rv->wts;
					p = rv;
				}
			}
		}
	}
	return p;
}

// HASTA ACA LLEGUE!!

short mvcc2_add_record(MVCC2_TID *tid,char *data) {
	short ret=1;
	if ( !mvcc2_is_active_tid(tid) ) return (short) 0;
	MVCC2_RECORD *newr = mvcc2_new_record(tid);
	strcpy(newr->versions->data,data);
	tid->rollback[tid->lop].op = 'd'; 
	tid->rollback[tid->lop].record = newr - mvcc2_records; 
	tid->rollback[tid->lop].version = 0; 
	tid->lop++;
	return ret;
}
/**
 * Borra el primer registro que machee con el contenido de data
 */
short mvcc2_delete_record(MVCC2_TID *tid,char *data) {
	MVCC2_RECORD *p = mvcc2_records;
	int i;
	short ret=0;
	for(i=0;i<mvcc2_records_count;i++,p++) {
		MVCC2_RECORD_VERSION *rv = mvcc2_get_record_version(tid,p);
		if ( rv ) {
			if ( mvcc2_is_record_visible(tid,rv) && !strcmp(rv->data,data) ) {
				if ( mvcc2_is_record_locked(p) ) {
					fprintf(stderr,"Registro [%s] lockeado por otra transaccion\n",rv->data);
					break;
				} else {
					p->expired_tid = tid->tid;
					tid->rollback[tid->lop].op = 'a'; 
					tid->rollback[tid->lop].record = p - mvcc2_records; 
					tid->lop++;
					ret=1;
					break; 
				}
			}
		}
	}
	return ret;
}

short mvcc2_update_record(MVCC2_TID *tid,char *olddata,char *newdata) {
	if ( mvcc2_delete_record(tid,olddata) ) if ( mvcc2_add_record(tid,newdata) ) return (short) 1;
	return (short) 0;
}

/**
 * Pone en NULL a tid en el arreglo de transacciones activas,
 * disminuye la cantidad de transacciones activa
 * crea "hueco" dentro del arreglo de transacciones activas
 * No crea nuevo arreglo porque ello impactaria en los punteros de transacciones
 * actualmente en uso
 */
short mvcc2_commit(MVCC2_TID *tid) {
	if ( !mvcc2_active_tids_count ) return (short) 0;
	if ( !mvcc2_is_active_ntid(tid->tid) ) return (short) 0;
	return mvcc2_deactivate(tid);
}

short mvcc2_rollback(MVCC2_TID *tid) {
	if ( !tid->lop ) return (short) 0;
	if ( mvcc2_records == NULL ) return (short) 0;
	int lop = tid->lop;
	MVCC2_RECORD *prec=NULL;
	while(lop) {
		lop--;
		prec = mvcc2_records + tid->rollback[lop].record;
		switch(tid->rollback[lop].op) {
			case 'a':
				prec->expired_tid = 0;
				break;
			case 'd':
				prec->expired_tid = tid->tid;
				break;
		}
	}
	return mvcc2_deactivate(tid);
}

MVCC2_RECORD *mvcc2_new_record(MVCC2_TID *tid) {
	pthread_mutex_lock(&mvcc2_m1);
		mvcc2_records_count++;
		if ( mvcc2_records == NULL ) mvcc2_records = (MVCC2_RECORD *) malloc(sizeof(MVCC2_RECORD));
		else mvcc2_records = (MVCC2_RECORD *) realloc(mvcc2_records,sizeof(MVCC2_RECORD)*mvcc2_records_count);
	pthread_mutex_unlock(&mvcc2_m1);
	MVCC2_RECORD *p = mvcc2_records+mvcc2_records_count-1;
	memset(p,0,sizeof(MVCC2_RECORD));
	p->expired_tid=0;
	p->created_tid=tid->tid;
	mvcc2_new_record_version(tid,p);
	return p;
}
MVCC2_RECORD_VERSION *mvcc2_new_record_version(MVCC2_TID *tid,MVCC2_RECORD *r) {
	if ( r == NULL ) return NULL;
	MVCC2_RECORD_VERSION *p = NULL;
	// a un registro borrado por
	// debo re-arrancar T  si expired > tid->tid ??
	if ( r->expired_tid != 0 && r->expired_tid <= tid->tid ) return NULL;
	// problema: un mismo mutex para N registros
	pthread_mutex_lock(&mvcc2_m2);
		r->count++;
		if ( r->versions == NULL ) {
			r->versions = (MVCC2_RECORD_VERSION *) malloc(sizeof(MVCC2_RECORD_VERSION));
			p=r->versions;
		} else {
			r->versions = (MVCC2_RECORD_VERSION *) realloc(r->versions,sizeof(MVCC2_RECORD_VERSION)*r->count);
			p = r->versions+r->count-1;
		}
		p->rts = tid->tid;
		p->wts = tid->tid;
		memset(p->data,0,sizeof(MVCC2_MAX_RECORD_LENGTH));
	pthread_mutex_unlock(&mvcc2_m2);
	return p;
}


short mvcc2_is_record_visible(MVCC2_TID *tid,MVCC2_RECORD_VERSION *rv) {
	short ret=1;
	if ( tid == NULL || rv == NULL || mvcc2_active_tids == NULL ) return (short) 0;
	// si el registro fue creado por otra Ti activa -> no es un registro visible para Ti
	if ( mvcc2_is_active_ntid(r->created_tid) && ( r->created_tid != tid->tid ) ) return (short) 0;
	// el registro expiro y no es de nuestra transaccion
	if ( r->expired_tid != 0 && (!mvcc2_is_active_ntid(r->expired_tid) || r->expired_tid == tid->tid) ) return (short) 0;
	return ret;
}

short mvcc2_is_record_locked(MVCC2_RECORD *r) {
	return  (r->expired_tid != 0 && mvcc2_is_active_ntid(r->expired_tid)) ? 1 : 0;
}

void mvcc2_print_records() {
	if ( mvcc2_records == NULL ) return;
	MVCC2_RECORD *p = mvcc2_records;
	printf("Expired\tCreated\tDATA\n");
	int i;
	for(i=0;i<mvcc2_records_count;i++,p++) printf("%d\t%d\t%s\n",p->expired_tid,p->created_tid,p->data);
}

// imprime los registros visibles para tid 
void mvcc2_print_visible_records(MVCC2_TID *tid) {
	if ( mvcc2_records == NULL ) return;
	MVCC2_RECORD *p = mvcc2_records;
	printf("Expired\tCreated\tDATA\n");
	int i;
	for(i=0;i<mvcc2_records_count;i++,p++) 
		if ( mvcc2_is_record_visible(tid,p) ) 
			printf("%d\t%d\t%s\n",p->expired_tid,p->created_tid,p->data);
}


