/*
 * mvcc2.h
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
 * Algoritmo multiversion implementado con atributos rts, wts
 * re-iniciando transacciones
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

#define MVCC2_MAX_RECORD_LENGTH 256  // largo maximo de registro
#define MVCC2_MAX_ROLLBACK_OPS 1024  // cantidad maxima de operaciones de rollback
#define MVCC2_MAX_TIDS 256           // cantidad maxima de transacciones activas

typedef struct MVCC2_RECORD_VERSION {
	int rts;
	int wts;
	char data[MVCC2_MAX_RECORD_LENGTH]; // null string terminated
} MVCC2_RECORD_VERSION;

typedef struct MVCC2_RECORD {
	int expired_tid;
	int created_tid;
	int count;  // version counter 1..N
	MVCC2_RECORD_VERSION *versions;
} MVCC2_RECORD;

typedef struct MVCC2_OP {
	unsigned char op; // 'a' for add, 'd' for delete, 'u' for update
	int record;       // record offset number 0..N-1
	int version;      // record offset version number 0..N-1
	char old_data[MVCC2_MAX_RECORD_LENGTH]; // null string terminated
	char new_data[MVCC2_MAX_RECORD_LENGTH]; // null string terminated
} MVCC2_OP;

typedef struct MVCC2_TID {
	int tid;
	MVCC2_OP rollback[MVCC2_MAX_ROLLBACK_OPS];
	MVCC2_OP redo[MVCC2_MAX_ROLLBACK_OPS];
	int lop; // last operation index 0..N-1
} MVCC2_TID;

// mutex to protect concurret access to items shared
pthread_mutex_t mvcc2_m1 = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mvcc2_m2 = PTHREAD_MUTEX_INITIALIZER; // utilizado para agregar version, reveer, repensar!

MVCC2_RECORD *mvcc2_records      = NULL;
int           mvcc2_records_count=0;
int           mvcc2_next_tid=1;
MVCC2_TID    *mvcc2_active_tids = NULL;
int           mvcc2_active_tids_count=0;

MVCC2_TID *mvcc2_new_tid();
void mvcc2_print_active_tids();
short mvcc2_is_active_tid(MVCC2_TID *);
short mvcc2_is_active_ntid(int tid);
short mvcc2_is_record_visible(MVCC2_TID *tid,MVCC2_RECORD_VERSION *r);
short mvcc2_is_record_locked(MVCC2_RECORD *r);
void mvcc2_print_records();  // imprime todos los registros
void mvcc2_print_visible_records(MVCC2_TID *tid); // imprime los registros visibles para tid 

// main functions
short mvcc2_add_record(MVCC2_TID *tid,char *data);
short mvcc2_delete_record(MVCC2_TID *tid,char *data);
short mvcc2_update_record(MVCC2_TID *tid,char *olddata,char *newdata);
short mvcc2_commit(MVCC2_TID *tid);
short mvcc2_rollback(MVCC2_TID *tid);

