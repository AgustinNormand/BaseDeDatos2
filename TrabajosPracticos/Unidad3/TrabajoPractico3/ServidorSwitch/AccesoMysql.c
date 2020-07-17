#include <mysql/mysql.h>

#include <sys/types.h>

#include <string.h>

#include <stdlib.h>

#include <stdio.h>

#include <unistd.h>

#include <fcntl.h>

#include "NetworkConfiguration.h"

void queryAMysql(char databaseName[], char query[], char bufferRespuesta[]) {

	printf("Accediendo a MySql\n");
	
	strcpy(bufferRespuesta,"<query>\n");
	
	char bufferAuxiliar[1024];

  MYSQL * conn;
  MYSQL_RES * result; 
  MYSQL_ROW row; 
  char * server = IPMySql;
  char * user = UsernameMysql; 
  char * password = PasswordMysql; 
  char * database = databaseName; 
  conn = mysql_init(NULL);
  
  if (mysql_real_connect(conn, server, user, password, database, 0, NULL, 0) == NULL) {
  	printf("Fallo la conexión al servidor MySql.\n");
  	strcat(bufferRespuesta,"\t<status>\n");
   	strcat(bufferRespuesta,"\t<status1>Falló la conexión.</status1>\n");
   	strcat(bufferRespuesta,"\t</status>\n");
  } else {
	if (mysql_query(conn, query) != 0) {
		printf("Falló la query al servidor MySql.\n");
		strcat(bufferRespuesta,"\t<status>\n");
   	strcat(bufferRespuesta,"\t<status1>Falló la query.</status1>\n");
   	strcat(bufferRespuesta,"\t</status>\n");
    } 
    else 
		{
			result = mysql_use_result(conn);
      if (result == NULL)
      {
	      strcat(bufferRespuesta,"\t<status>\n");
  	  	strcat(bufferRespuesta,"\t<status1>Ok query.</status1>\n");
	    	strcat(bufferRespuesta,"\t</status>\n");
	    }	
      else 
      {
      	strcat(bufferRespuesta,"\t<cols>\n");
      	
      	int i;
      	int num_attrib = mysql_num_fields(result);
      	MYSQL_FIELD *fields;
			  fields = mysql_fetch_fields(result);
			  int colNumber = 1;
      	for(i = 0; i < num_attrib; i++)
      	{
		    	sprintf(bufferAuxiliar,"\t\t<colname%d>",colNumber);
			    strcat(bufferRespuesta,bufferAuxiliar);
			    
			    strcat(bufferRespuesta,fields[i].name);
			    
			    sprintf(bufferAuxiliar,"</colname%d>\n",colNumber);
			    strcat(bufferRespuesta,bufferAuxiliar);
			    colNumber++;
		    }
      	
      	strcat(bufferRespuesta,"\t</cols>\n");
        
        strcat(bufferRespuesta,"\t<rows>\n");
        
        int rowNumber = 1;
        while ((row = mysql_fetch_row(result)) != NULL) 
        {
  	      sprintf(bufferAuxiliar,"\t\t<row%d>\n",rowNumber);
		      strcat(bufferRespuesta,bufferAuxiliar);
          colNumber = 1;
          for (i = 0; i < num_attrib; i++) 
          {
            sprintf(bufferAuxiliar,"\t\t\t<col%d>",colNumber);
	        	strcat(bufferRespuesta,bufferAuxiliar);
						strcat(bufferRespuesta, row[i]);	        	
	          sprintf(bufferAuxiliar,"</col%d>\n",colNumber);
	          strcat(bufferRespuesta,bufferAuxiliar);
	          colNumber++;
          }
	        sprintf(bufferAuxiliar,"\t\t</row%d>\n",rowNumber);
	        strcat(bufferRespuesta,bufferAuxiliar);
          rowNumber++;
        }
        strcat(bufferRespuesta,"\t</rows>\n");
        mysql_free_result(result);
      }
    }
  }
  mysql_close(conn);
  
  strcat(bufferRespuesta,"</query>\n\0");
	
}
