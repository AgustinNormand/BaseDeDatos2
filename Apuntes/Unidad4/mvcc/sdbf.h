/*
 * sdbf.h
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


/**
 * libreria para manejo de archivo similar a dbf, pero con campos de un
 * mismo tipo (caracter)
 * y generando N versiones de cada registro, guardando su rts y wts
 * utilizado para probar algoritmo multi-version de control de 
 * concurrencia
 * Estructura del archivo:
 * SDBF_HEADER+n SDBF_FIELDS+RECORD_PAGES 
 */

#include <stdio.h>
#include <stdlib.h>

typedef struct SDBF_HEADER {
	int numberOffields; // cantidad de campos a continuacion del header
	int recordLength;   // largo de registro
	int recordCount;    // cantidad de registros
	int recordPage;     // puntero a pagina de registros
	char name[60];      // include \0
};

typedef struct SDBF_FIELDS {
	char fieldName[64]; // nombre de campo (max 63 caracteres mas \0 final)
	int fieldSize;      // cantidad de bytes que ocupa el campo
};

typedef struct SDBF_CATALOG {
	long lastTID;       // ultimo nro de transaccion
};
