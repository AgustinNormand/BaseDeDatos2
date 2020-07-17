/*
 * mvcc.h
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
 * Algoritmo multiversion implementado con atributos created, expired
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

#define MVCC_MAX_RECORD_LENGTH 256  // largo maximo de registro
#define MVCC_MAX_ROLLBACK_OPS 1024  // cantidad maxima de operaciones de rollback
#define MVCC_MAX_TIDS 256           // cantidad maxima de transacciones activas

typedef struct MVCC_RECORD {
	int expired_tid;
	int created_tid;
	char data[MVCC_MAX_RECORD_LENGTH]; // null string terminated
} MVCC_RECORD;

typedef struct MVCC_OP {
	unsigned char op; // 'a' for add, 'd' for delete
	int record; // record number 0..N-1
} MVCC_OP;

typedef struct MVCC_TID {
	int tid;
	MVCC_OP rollback[MVCC_MAX_ROLLBACK_OPS];
	int lop; // last operation index 0..N-1
} MVCC_TID;

// mutex to protect concurret access to items shared
pthread_mutex_t mvcc_m1 = PTHREAD_MUTEX_INITIALIZER;

MVCC_RECORD *records = NULL;
int record_count=0;
int next_tid=1;
MVCC_TID *active_tids = NULL;
int active_tids_count=0;

MVCC_TID *mvcc_new_tid();
void mvcc_print_active_tids();
short mvcc_is_active_tid(MVCC_TID *);
short mvcc_is_active_ntid(int tid);
short mvcc_is_record_visible(MVCC_TID *tid,MVCC_RECORD *r);
short mvcc_is_record_locked(MVCC_RECORD *r);
void mvcc_print_records();  // imprime todos los registros
void mvcc_print_visible_records(MVCC_TID *tid); // imprime los registros visibles para tid 

// main functions
short mvcc_add_record(MVCC_TID *tid,char *data);
short mvcc_delete_record(MVCC_TID *tid,char *data);
short mvcc_update_record(MVCC_TID *tid,char *olddata,char *newdata);
short mvcc_commit(MVCC_TID *tid);
short mvcc_rollback(MVCC_TID *tid);

