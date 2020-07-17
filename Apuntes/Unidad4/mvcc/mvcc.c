/*
 * mvcc.c
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
 */


/**
 * IDEA: implementar en C la version de Elliot Chance
 * https://elliot.land/post/implementing-your-own-transactions-with-mvcc
 * implementacion de mvcc en memoria
 * luego persistir arreglo de registros en disco
 * agregar mutex para proteger acceso concurrente a datos compartidos
 * antes probar un hilo por vez
 * 
 * todo list
 * -probar agregar registro, agregar funciones de trace
 * -continuar con los siguientes metodos
 * -agregar soporte multihilos
 * -probar con hilos
 */

#include "mvcc.h"

// funciones privadas
MVCC_RECORD *mvcc_new_record();
short mvcc_deactivate(MVCC_TID *tid);

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
	mvcc_print_visible_records(mvcc_new_tid());
	mvcc_print_records();
	printf("main(): fin\n");
	return 0;
}

// *********************************************************************
// TESTING TRANSACTIONS

void *transaccion1(void *p) {
	printf("transaccion1: inicio\n");
	MVCC_TID *tr = mvcc_new_tid();
	mvcc_add_record(tr,"juan");
	mvcc_add_record(tr,"pedro");
	mvcc_print_active_tids();
	mvcc_print_records();
	mvcc_commit(tr);
	mvcc_print_active_tids();
	mvcc_print_records();
	printf("transaccion1: fin\n");
	pthread_exit(NULL);
}

void *transaccion2(void *p) {
	printf("transaccion2: inicio\n");
	MVCC_TID *tr = mvcc_new_tid();
	mvcc_add_record(tr,"ana");
	mvcc_add_record(tr,"estela");
	mvcc_print_active_tids();
	mvcc_print_records();
	mvcc_commit(tr);
	mvcc_print_active_tids();
	mvcc_print_records();
	printf("transaccion2: fin\n");
	pthread_exit(NULL);
}
void *transaccion3(void *p) {
	printf("transaccion3: inicio\n");
	MVCC_TID *tr = mvcc_new_tid();
	short r = mvcc_delete_record(tr,"pedro");
	if ( r ) printf("transaccion3: delete ok!\n");
	else     printf("transaccion3: delete error!\n");
	mvcc_commit(tr);
	printf("transaccion3: fin\n");
	pthread_exit(NULL);
}

// END TESTING TRANSACTIONS
// *********************************************************************


MVCC_TID *mvcc_new_tid() {
	if ( active_tids_count >= MVCC_MAX_TIDS ) {
		fprintf(stderr,"mvcc_new_tid():Pretende superar la cantidad maxima de transacciones activas permitidas [%d]\n",MVCC_MAX_TIDS); 
		return NULL;
	}
	pthread_mutex_lock(&mvcc_m1);
		int r=next_tid++;
		active_tids_count++;
	pthread_mutex_unlock(&mvcc_m1);
	if ( active_tids == NULL ) {
		pthread_mutex_lock(&mvcc_m1);
			active_tids = (MVCC_TID *) malloc(sizeof(MVCC_TID)*MVCC_MAX_TIDS);
			memset(active_tids,0,sizeof(MVCC_TID)*MVCC_MAX_TIDS);
			active_tids->tid=r;
			active_tids->lop=0;
		pthread_mutex_unlock(&mvcc_m1);
		return active_tids;
	} else {
		// busco "hueco" dejado en vector de transacciones activas por commit para su reuso
		// en caso de no encontrar hueco, agrego al final
		MVCC_TID *p = active_tids;
		int i;
		for(i=0;i<active_tids_count;p++) {
			if ( p->tid != 0 ) {
				i++;
			} else {
				// hueco o bien llegue al final
				p->tid=r;
				memset(p->rollback,0,sizeof(MVCC_OP)*MVCC_MAX_ROLLBACK_OPS);
				p->lop=0;
				return p;
			}
		}
		pthread_mutex_lock(&mvcc_m1);
			active_tids_count--;
		pthread_mutex_unlock(&mvcc_m1);
		fprintf(stderr,"mvcc_new_tid():Imposible crear nueva transaccion!, no se encontro lugar en arreglo de transacciones!\n"); 
		return NULL;
	}
}
void mvcc_print_active_tids() {
	int i;
	MVCC_TID *p=active_tids;
	printf("Active TIDs:");
	for(i=0;p != NULL && i<active_tids_count;p++) {
		if ( p->tid != 0 ) {
			printf(" %d",p->tid);
			i++;
		}
	}
	printf("\n");
}
short mvcc_is_active_tid(MVCC_TID *tid) {
	short r=0;
	int i;
	MVCC_TID *p=active_tids;
	for(i=0;p != NULL && i<active_tids_count;p++) {
		if ( p->tid != 0 ) {
			if ( p->tid == tid->tid ) return (short) 1;
			i++;
		}
	}
	return r;
}
short mvcc_is_active_ntid(int tid) {
	short r=0;
	int i;
	MVCC_TID *p=active_tids;
	for(i=0;p != NULL && i<active_tids_count;p++) {
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
short mvcc_deactivate(MVCC_TID *tid) {
	int i;
	short ret=0;
	MVCC_TID *p=active_tids;
	for(i=0;i<active_tids_count;p++) {
		if ( p->tid != 0 ) {
			if ( tid->tid == p->tid ) {
				memset(p,0,sizeof(MVCC_TID));
				pthread_mutex_lock(&mvcc_m1);
					active_tids_count--;
				pthread_mutex_unlock(&mvcc_m1);
				ret=1;
				break;
			}
			i++;
		}
	}
	return ret;
}


short mvcc_add_record(MVCC_TID *tid,char *data) {
	short ret=1;
	if ( !mvcc_is_active_tid(tid) ) return (short) 0;
	MVCC_RECORD *newr = mvcc_new_record();
	newr->expired_tid=0;
	newr->created_tid=tid->tid;
	memset(newr->data,0,MVCC_MAX_RECORD_LENGTH);
	strcpy(newr->data,data);
	tid->rollback[tid->lop].op = 'd'; 
	tid->rollback[tid->lop].record = newr - records; 
	tid->lop++;
	return ret;
}
/**
 * Borra el primer registro que machee con el contenido de data
 */
short mvcc_delete_record(MVCC_TID *tid,char *data) {
	MVCC_RECORD *p = records;
	int i;
	short ret=0;
	for(i=0;i<record_count;i++,p++) {
		if ( mvcc_is_record_visible(tid,p) && !strcmp(p->data,data) ) {
			if ( mvcc_is_record_locked(p) ) {
				fprintf(stderr,"Registro [%s] lockeado por otra transaccion\n",p->data);
				break;
			} else {
				p->expired_tid = tid->tid;
				tid->rollback[tid->lop].op = 'a'; 
				tid->rollback[tid->lop].record = p - records; 
				tid->lop++;
				ret=1;
				break; 
			}
		}
	}
	return ret;
}

short mvcc_update_record(MVCC_TID *tid,char *olddata,char *newdata) {
	if ( mvcc_delete_record(tid,olddata) ) if ( mvcc_add_record(tid,newdata) ) return (short) 1;
	return (short) 0;
}

/**
 * Pone en NULL a tid en el arreglo de transacciones activas,
 * disminuye la cantidad de transacciones activa
 * crea "hueco" dentro del arreglo de transacciones activas
 * No crea nuevo arreglo porque ello impactaria en los punteros de transacciones
 * actualmente en uso
 */
short mvcc_commit(MVCC_TID *tid) {
	if ( !active_tids_count ) return (short) 0;
	if ( !mvcc_is_active_ntid(tid->tid) ) return (short) 0;
	return mvcc_deactivate(tid);
}

short mvcc_rollback(MVCC_TID *tid) {
	if ( !tid->lop ) return (short) 0;
	if ( records == NULL ) return (short) 0;
	int lop = tid->lop;
	MVCC_RECORD *prec=NULL;
	while(lop) {
		lop--;
		prec = records + tid->rollback[lop].record;
		switch(tid->rollback[lop].op) {
			case 'a':
				prec->expired_tid = 0;
				break;
			case 'd':
				prec->expired_tid = tid->tid;
				break;
		}
	}
	return mvcc_deactivate(tid);
}

MVCC_RECORD *mvcc_new_record() {
	pthread_mutex_lock(&mvcc_m1);
		record_count++;
		if ( records == NULL ) records = (MVCC_RECORD *) malloc(sizeof(MVCC_RECORD));
		else records = (MVCC_RECORD *) realloc(records,sizeof(MVCC_RECORD)*record_count);
	pthread_mutex_unlock(&mvcc_m1);
	MVCC_RECORD *p = records+(record_count-1);
	p->expired_tid=-1;
	p->created_tid=-1;
	memset(p->data,0,MVCC_MAX_RECORD_LENGTH);
	return p;
}

short mvcc_is_record_visible(MVCC_TID *tid,MVCC_RECORD *r) {
	short ret=1;
	if ( tid == NULL || r == NULL || active_tids == NULL ) return (short) 0;
	// si el registro fue creado por otra Ti activa -> no es un registro visible para Ti
	if ( mvcc_is_active_ntid(r->created_tid) && ( r->created_tid != tid->tid ) ) return (short) 0;
	// el registro esta borrado por una Ti no activa
	if ( r->expired_tid != 0 && (!mvcc_is_active_ntid(r->expired_tid) || r->expired_tid == tid->tid) ) return (short) 0;
	return ret;
}

short mvcc_is_record_locked(MVCC_RECORD *r) {
	return  (r->expired_tid != 0 && mvcc_is_active_ntid(r->expired_tid)) ? 1 : 0;
}

void mvcc_print_records() {
	if ( records == NULL ) return;
	MVCC_RECORD *p = records;
	printf("Expired\tCreated\tDATA\n");
	int i;
	for(i=0;i<record_count;i++,p++) printf("%d\t%d\t%s\n",p->expired_tid,p->created_tid,p->data);
}

// imprime los registros visibles para tid 
void mvcc_print_visible_records(MVCC_TID *tid) {
	if ( records == NULL ) return;
	MVCC_RECORD *p = records;
	printf("Expired\tCreated\tDATA\n");
	int i;
	for(i=0;i<record_count;i++,p++) 
		if ( mvcc_is_record_visible(tid,p) ) 
			printf("%d\t%d\t%s\n",p->expired_tid,p->created_tid,p->data);
}



