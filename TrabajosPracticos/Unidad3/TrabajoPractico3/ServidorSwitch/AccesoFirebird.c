#include <stdio.h>
#include <string.h>
#include <libfb/libfb/libfb.h>
#include "NetworkConfiguration.h"

int queryAFirebird(char databaseName[], char bufferQuery[], char bufferRespuesta[]){
	printf("Accediendo a Firebird\n");
	
	char bufferAux[512];
	strcpy(bufferRespuesta,"<query>\n");
	
	
	extern int FB_SHOW_MESSAGES;
	FB_SHOW_MESSAGES = 0;
	fb_db_info dbinfo;

	char dbPath[1024];
	strcpy(dbPath,IPFirebird);
	strcat(dbPath,"/");
	strcat(dbPath,PuertoFirebird);
	strcat(dbPath,":");
	strcat(dbPath,DbPathFirebird);
	strcat(dbPath,databaseName);
	strcat(dbPath,".fdb");
	
	strcpy(dbinfo.user,UsernameFirebird);
	strcpy(dbinfo.passw,PasswordFirebird);
	strcpy(dbinfo.role,RoleFirebird);
	strcpy(dbinfo.dbname,dbPath);
	
	if(fb_do_connect(&dbinfo)) 
	{
  	query myQuery;
    fb_init(&myQuery); 
		if (fb_do_query(&dbinfo, 1, bufferQuery, onDoGenericQuery, &myQuery))
		{
			printf("Cantidad de filas %d\n",myQuery.rows);
			printf("Cantidad de columnas %d\n",myQuery.cols);
			printf("Rows Inserted %d\n",myQuery.rows_inserted);
			printf("Rows Updated %d\n",myQuery.rows_updated);
			printf("Rows Deleted %d\n",myQuery.rows_deleted);
			printf("Rows Selected %d\n",myQuery.rows_selected);			
			printf("Rows Fetched %d\n",myQuery.rows_fetched);
			if (myQuery.cols == 0) {
				printf("Ok query.\n");
				strcat(bufferRespuesta,"\t<status>\n");
  	 		strcat(bufferRespuesta,"\t<status1>Ok query.</status1>\n");
		   	strcat(bufferRespuesta,"\t</status>\n");
			}
			else
			{
			
				strcat(bufferRespuesta,"\t<cols>\n");
				
				int k;
				char **colnames = (char **) myQuery.colname;
				for (k = 0; k < myQuery.cols; k++)
				{
					sprintf(bufferAux,"\t\t<colname%d>",k+1);
					strcat(bufferRespuesta,bufferAux);
					
					strcat(bufferRespuesta,*(colnames+k));
					
					sprintf(bufferAux,"</colname%d>\n",k+1);
					strcat(bufferRespuesta,bufferAux);
					
				}
				
				strcat(bufferRespuesta,"\t</cols>\n");
				
				strcat(bufferRespuesta,"\t<rows>\n");
				int i, j;
				rquery *q = myQuery.top;
				char **rowvalues = (char **) q->col;
				for (i = 0; i < myQuery.rows; i++)
				{
					sprintf(bufferAux,"\t\t<row%d>\n",i+1);
					strcat(bufferRespuesta,bufferAux);
					for(j = 0; j < myQuery.cols; j++)
					{
					sprintf(bufferAux,"\t\t\t<col%d>",j+1);
					strcat(bufferRespuesta,bufferAux);	
					
					strcat(bufferRespuesta,*(rowvalues+j));
					
					sprintf(bufferAux,"</col%d>\n",j+1);
					strcat(bufferRespuesta,bufferAux);	
					}
					sprintf(bufferAux,"\t\t</row%d>\n",i+1);
					strcat(bufferRespuesta,bufferAux);
				}
				strcat(bufferRespuesta,"\t</rows>\n");
				fb_free(&myQuery);
			}
		}
		else
		{
			printf("Falló la query.\n");
			strcat(bufferRespuesta,"\t<status>\n");
  	 	strcat(bufferRespuesta,"\t<status1>Falló la query.</status1>\n");
	   	strcat(bufferRespuesta,"\t</status>\n");
		}
		fb_do_disconnect(&dbinfo);
	}
	else
	{
		printf("Falló la conexión.\n");
		strcat(bufferRespuesta,"\t<status>\n");
   	strcat(bufferRespuesta,"\t<status1>Falló la conexión.</status1>\n");
   	strcat(bufferRespuesta,"\t</status>\n");
	}
	
	strcat(bufferRespuesta,"</query>\n\0");
	printf("Buffer respuesta resultante\n");
	printf("%s\n",bufferRespuesta);
	printf("Going out\n");
}

int verificarFirebirdDatabase(char databaseName[]) {
	printf("Accediendo a Firebird\n");
	
	int found = 0;
	
	extern int FB_SHOW_MESSAGES;
	FB_SHOW_MESSAGES = 0;
	fb_db_info dbinfo;

	char dbPath[1024];
	strcpy(dbPath,IPFirebird);
	strcat(dbPath,"/");
	strcat(dbPath,PuertoFirebird);
	strcat(dbPath,":");
	strcat(dbPath,DbPathFirebird);
	strcat(dbPath,databaseName);
	strcat(dbPath,".fdb");
	
	strcpy(dbinfo.user,UsernameFirebird);
	strcpy(dbinfo.passw,PasswordFirebird);
	strcpy(dbinfo.role,RoleFirebird);
	strcpy(dbinfo.dbname,dbPath);
	
	if(fb_do_connect(&dbinfo)) 
	{
		found = 1;
		fb_do_disconnect(&dbinfo);
	}
	else
		found = 0;
	return found;
}
