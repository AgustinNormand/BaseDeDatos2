#include <string.h>

#include <libpq-fe.h>

#include <sys/types.h>

#include <string.h>

#include <stdlib.h>

#include <stdio.h>

#include <unistd.h>

#include <fcntl.h>

#include "NetworkConfiguration.h"

void queryAPostgresql(char databaseName[], char query[], char bufferRespuesta[]) {

	printf("Accediendo a PostgreSql\n");	
	
	strcpy(bufferRespuesta,"<query>\n");
	
	char bufferAuxiliar[1024];
	
  PGconn * conn;
  PGresult * result;
  int i, j;
  conn = PQsetdbLogin(IPPostgresql, PuertoPostgresql, NULL, NULL, databaseName, UsernamePostgresql, PasswordPostgresql);
  if (PQstatus(conn) != CONNECTION_BAD) 
  {
    result = PQexec(conn, query);
    if (PQresultStatus(result) == PGRES_COMMAND_OK) 
    {
    	strcat(bufferRespuesta,"\t<status>\n");
    	strcat(bufferRespuesta,"\t<status1>Ok query.</status1>\n");
    	strcat(bufferRespuesta,"\t</status>\n");
      PQclear(result);
    } 
    else
    if (result != NULL && PGRES_TUPLES_OK == PQresultStatus(result)) 
    {
    	int colNumber = 1;
    	strcat(bufferRespuesta,"\t<cols>\n");
    	for (i = 0; i < PQnfields(result); i++) 
      {
      	sprintf(bufferAuxiliar,"\t\t<colname%d>",colNumber);
	      strcat(bufferRespuesta,bufferAuxiliar);
	      strcat(bufferRespuesta,PQfname(result,i));
	      sprintf(bufferAuxiliar,"</colname%d>\n",colNumber);
	      strcat(bufferRespuesta,bufferAuxiliar);
	      colNumber++;
      }
      strcat(bufferRespuesta,"\t</cols>\n");
    	strcat(bufferRespuesta,"\t<rows>\n");
    	int rowNumber = 1;
      for (i = 0; i <= PQntuples(result) - 1; i++) 
      {
      	sprintf(bufferAuxiliar,"\t\t<row%d>\n",rowNumber);
	      strcat(bufferRespuesta,bufferAuxiliar);
        for (j = 0; j < PQnfields(result); j++) 
        {
        	colNumber = j + 1;
        	sprintf(bufferAuxiliar,"\t\t\t<col%d>",colNumber);
        	strcat(bufferRespuesta,bufferAuxiliar);
          strcat(bufferRespuesta, PQgetvalue(result, i, j));
          sprintf(bufferAuxiliar,"</col%d>\n",colNumber);
          strcat(bufferRespuesta,bufferAuxiliar);
        }
        sprintf(bufferAuxiliar,"\t\t</row%d>\n",rowNumber);
        strcat(bufferRespuesta,bufferAuxiliar);
        rowNumber++;
      }
      strcat(bufferRespuesta,"\t</rows>\n");
      PQclear(result);
    } 
    else
    {
    	printf("Falló la query al servidor PostgreSql.\n");
    	strcat(bufferRespuesta,"\t<status>\n");
    	strcat(bufferRespuesta,"\t<status1>Falló la query.</status1>\n");
    	strcat(bufferRespuesta,"\t</status>\n");
    }
  }
  else
  { 
  	strcat(bufferRespuesta,"\t<status>\n");
   	strcat(bufferRespuesta,"\t<status1>Falló la conexión.</status1>\n");
  	strcat(bufferRespuesta,"\t</status>\n");
		printf("Fallo la conexión al servidor PostgreSql.\n");
	}
  PQfinish(conn);
  
  strcat(bufferRespuesta,"</query>\n\0");
}
