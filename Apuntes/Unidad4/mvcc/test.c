/*
 * test.c
 * 
 * Copyright 2016 grchere <grchere@debian2>
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

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>

void *imprimo(void *threadid);


int main(int argc, char **argv)
{
	int t=3,status;
	pthread_t thr;
	int rc = pthread_create(&thr, NULL, imprimo,  &t);
	if (rc){ 
		printf("main(): Error, pthread_create() retorna %d\n", rc);
		exit(-1);
	}
	rc = pthread_join(thr,(void **) &status);
	if (rc){ 
		printf("main(): Error, pthread_join() retorna %d\n", rc);
		exit(-2);
	}
	printf("main(): fin!\n");
	pthread_exit(NULL);
	return 0;
}

//============================================================
//                FUNCIONES
//============================================================

void *imprimo(void *threadid) {
   int *tid;
   tid = (int *) threadid;
   sleep(3);
   printf("Mensaje de thread %d!\n", *tid);
   pthread_exit(NULL);
}
