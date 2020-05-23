#include <netdb.h>

#include <sys/types.h>

#include <sys/socket.h>

#include <netinet/in.h>

#include <arpa/inet.h>

#include <string.h>

#include <stdlib.h>

#include <stdio.h>

#include <unistd.h>

#include <fcntl.h>

#include <time.h>

#include "ServidorSwitch.h"

#include "NetworkConfiguration.h"

#include <json-c/json.h>

#include <libxml/xmlmemory.h>

#include <libxml/parser.h>

#include <ctype.h>

int parseDatabases(xmlDocPtr doc, xmlNodePtr cur, char databaseName[64]);

int main(int argc, char * argv[]) {
  struct sockaddr_in socketServidor, socketCliente;
  int idSocketServidor, idSocketCliente;
  socklen_t lensock = sizeof(struct sockaddr_in);
  idSocketServidor = socket(AF_INET, SOCK_STREAM, 0);
  printf("idSocketServidor: %d\n", idSocketServidor);
  socketServidor.sin_family = AF_INET;
  socketServidor.sin_port = htons(PuertoServidorSwitch);
  socketServidor.sin_addr.s_addr = inet_addr(IPServidorSwitch);
  memset(socketServidor.sin_zero, 0, 8);
  
  printf("Bind: %d\n", bind(idSocketServidor, (struct sockaddr * ) & socketServidor, lensock));
  printf("Listen: %d\n", listen(idSocketServidor, 5));
  while (1) 
  {
    idSocketCliente = accept(idSocketServidor, (struct sockaddr * ) & socketCliente, & lensock);
    if (idSocketCliente != -1) 
    {
      if (!fork()) 
			{
	      printf("\nNueva conexión aceptada.\n");
	      
			  char engineName[64];
			  char databaseName[64];
			  char query[1024];        
			  readSocket(idSocketCliente,databaseName,query);
			  
			  if( determinarEngineName(databaseName,engineName) == 1)
			  {
		      if (strcmp(engineName,"MYSQL") == 0) 
		      {
		      	printf("Cliente de MySql con socketFileDescriptor %d a la db \"%s\".\n",idSocketCliente,databaseName);
		      	printf("------------------\n");
		      	while(strcmp(query,"0") != 0)
		      	{
							printf("Query: %s\n",query);
			        char bufferRespuesta[1024];
							memset(bufferRespuesta, 0, 1024);
			        queryAMysql(databaseName, query, bufferRespuesta);
			        printf("Send: %ld Bytes.\n",send(idSocketCliente, bufferRespuesta, 1024,0));
			        printf("------------------\n");
			        readSocket(idSocketCliente,databaseName,query);
		        }
		      }
					if (strcmp(engineName,"POSTGRESQL") == 0) 
					{
						printf("Cliente de PostgreSql con socketFileDescriptor %d a la db \"%s\".\n",idSocketCliente,databaseName);
						printf("------------------\n");
						while (strcmp(query,"0") != 0)
						{
							printf("Query: %s\n",query);
			        char bufferRespuesta[1024];
			        memset(bufferRespuesta, 0, 1024);
			        queryAPostgresql(databaseName, query, bufferRespuesta);
			        printf("Send: %ld Bytes.\n",send(idSocketCliente, bufferRespuesta, 1024,0));
			        printf("------------------\n");
			        readSocket(idSocketCliente,databaseName,query);
		        }
		      }
					if (strcmp(engineName,"FIREBIRD") == 0) 
					{
						printf("Cliente de Firebird con socketFileDescriptor %d a la db \"%s\".\n",idSocketCliente,databaseName);
						printf("------------------\n");
						while (strcmp(query,"0") != 0)
						{
							printf("Query: %s\n",query);
			        char bufferRespuesta[1024];
			        memset(bufferRespuesta, 0, 1024);
			        queryAFirebird(databaseName, query, bufferRespuesta);
			        printf("Send: %ld Bytes.\n",send(idSocketCliente, bufferRespuesta, 1024,0));
			        printf("------------------\n");
			        readSocket(idSocketCliente,databaseName,query);
        		}
					}
	      printf("Conexion finalizada.\n\n");
	      close(idSocketCliente);
	      exit(0);
				}
				else
				{
					char bufferRespuesta[1024] = "No se encontró servidor corriendo que contenga la base de datos solicitada";
					printf("%s\n",bufferRespuesta);
					writeSocketWithError(idSocketCliente,bufferRespuesta,1024);
				}
			}
    } 
    else 
    {
    	char bufferRespuesta[1024];
    	sprintf(bufferRespuesta,"Conexion rechazada %d", idSocketCliente);
    	writeSocketWithError(idSocketCliente,bufferRespuesta,1024);
      printf("%s\n",bufferRespuesta);
    }
  }	
  return 0;
}

void writeSocketWithError(int fdSocketCliente, char bufferRespuesta[1024],int cantidadBytes){
		char bufferRespuestaAux[1024];
		strcpy(bufferRespuestaAux,"<query>\n");
		strcat(bufferRespuestaAux,"\t<status>\n");
		strcat(bufferRespuestaAux,"\t\t<status1>");
		strcat(bufferRespuestaAux,bufferRespuesta);
		strcat(bufferRespuestaAux,"</status1>\n");
		strcat(bufferRespuestaAux,"\t</status>\n");
		strcat(bufferRespuestaAux,"</query>\n");
		printf("Send: %ld Bytes.\n",send(fdSocketCliente, bufferRespuestaAux, 1024,0));
		
}

void parseJson(char buffer[], char databaseNameString[], char queryString[])
{
	struct json_object *parsed_json;
	struct json_object *databaseName;
	struct json_object *query;
				
	parsed_json = json_tokener_parse(buffer);
				
	json_object_object_get_ex(parsed_json, "databaseName", &databaseName);
	json_object_object_get_ex(parsed_json, "query", &query);
	
	strcpy(databaseNameString, json_object_get_string(databaseName));
	strcpy(queryString, json_object_get_string(query));
}

void readSocket(int fdSocketCliente, char databaseName[64], char query[1024]){
	char buffer[1024];
  int cantidadBytesLeidos;
        
  cantidadBytesLeidos = read(fdSocketCliente, buffer, 1024);
  buffer[cantidadBytesLeidos] = '\0';
        
  printf("Recv: %d\n",cantidadBytesLeidos);
        	
  parseJson(buffer, databaseName, query);
}

void upperCase(char string[64]){
	int i = 0;
	while(string[i] != '\0'){
		if (string[i] >= 'a' && string[i] <= 'z')
			string[i] = toupper(string[i]);
		i++;
	}
}

int determinarEngineName(char databaseName[64],char engineName[64])
{
	int found = 0;
	char bufferRespuesta[1024];
	printf("Cliente de la base de datos %s\n",databaseName);
	queryAPostgresql("postgres","SELECT datname FROM pg_database WHERE datistemplate = false;",bufferRespuesta);
	if (recorrerRespuesta(bufferRespuesta, databaseName) == 1)
	{
		strcpy(engineName,"POSTGRESQL");
		found = 1;
	}
	else
	{
		queryAMysql("INFORMATION_SCHEMA","SELECT SCHEMA_NAME FROM SCHEMATA;",bufferRespuesta);
		if (recorrerRespuesta(bufferRespuesta, databaseName) == 1)
		{
			strcpy(engineName,"MYSQL");
			found = 1;
		}
		else
			if(verificarFirebirdDatabase(databaseName) == 1)
			{
				strcpy(engineName,"FIREBIRD");
				found = 1;
			}
			
	}
	return found;
}

int recorrerRespuesta(char bufferRespuesta[1024], char databaseName[64])
{
	upperCase(databaseName);
	int found = 0;
	xmlDocPtr doc;
	xmlNodePtr cur;
	doc = xmlReadMemory(bufferRespuesta,1024,"test.xml",NULL,0);
	if (doc == NULL)
	{
		printf("File not parsed\n");
	} 
	else
	{
		cur = xmlDocGetRootElement(doc);
		if (cur == NULL)
		{
			printf("Empty document\n");
			xmlFreeDoc(doc);
		} 
		else
		{
			if (xmlStrcmp(cur->name, (const xmlChar *) "query"))
			{
				printf("Wrong root type\n");
				xmlFreeDoc(doc);
			}
			else
			{
				cur = cur -> xmlChildrenNode;
				while ((cur != NULL) && (found == 0))
				{
					if ((!xmlStrcmp(cur->name, (const xmlChar *) "rows")))
					{
						found = parseDatabases(doc,cur,databaseName);
						break;
					}
					cur = cur -> next;
				}
			}
		}
	}
	return found;
}

int parseDatabases(xmlDocPtr doc, xmlNodePtr cur, char databaseName[64])
{
	int found = 0;
	xmlNodePtr curdos;
	xmlChar *rowValue;
	cur = cur->xmlChildrenNode;
	while ((cur != NULL) && (found == 0))
	{
		curdos = cur ->xmlChildrenNode;
		while ((curdos != NULL) && (found == 0))
		{
			rowValue = xmlNodeListGetString(doc, curdos->xmlChildrenNode, 1);
			if (rowValue != NULL)
			{
				upperCase(rowValue);
				if (strcmp(rowValue,databaseName) == 0)
					found = 1;
			}
			xmlFree(rowValue);
			curdos = curdos->next;
		}
			cur = cur->next;
	}		
return found;
}
