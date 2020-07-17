#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <netdb.h>
#include <ctype.h>
#include <libxml/xmlmemory.h>
#include <libxml/parser.h>

#include "Cliente.h"
#include "NetworkConfiguration.h"


int main(int argcm, char * argv[]){
	struct sockaddr_in socketServidor;
	int fdSocketCliente;
	socklen_t longitudSocket = sizeof(struct sockaddr_in);
	fdSocketCliente = socket(AF_INET,SOCK_STREAM,0);
	printf("fdSocketCliente: %d\n", fdSocketCliente);
	socketServidor.sin_family = AF_INET;
	socketServidor.sin_port = htons(PuertoServidorSwitch);
	socketServidor.sin_addr.s_addr = inet_addr(IPServidorSwitch);
	memset(socketServidor.sin_zero,0,8);
	
	int connectResult;
	connectResult = connect(fdSocketCliente,(struct sockaddr *) & socketServidor, longitudSocket);
	printf("Connect: %d\n", connectResult);
	if (connectResult != -1){
		char databaseName[64];
		char query[896];
		char temp;
		printf("\nIngresar databaseName\n");
		scanf("%s",databaseName);
		
		printf("\nIngresar query's.\n");
		printf("Ingrese 0 para salir.\n");
		scanf("%c",&temp); //Clear the buffer otherwise doesnt work.
		scanf("%[^\n]",query);
		while (strcmp(query,"0") != 0){
			char respuesta[1024] = "";
			queryALaBase(fdSocketCliente,databaseName,query,respuesta);
			parseRespuesta(respuesta);
			printf("\nIngrese nueva query.\n");
			scanf("%c",&temp); //Clear the buffer otherwise doesnt work.
			scanf("%[^\n]",query);
		}
		cerrarConexionBase(fdSocketCliente,databaseName);
		close(fdSocketCliente);
	}
	
	return 0;
}

int validarEngineName(char engineName[64]){
	int found = 0;
	int i = 0;
	while((!found) && (i < maxEngines))
		if(strcmp(engineName,engineNames[i]) == 0)
			found = 1;
		else
			i++;
	return found;
}

void upperCase(char string[64]){
	int i = 0;
	while(string[i] != '\0'){
		if (string[i] >= 'a' && string[i] <= 'z')
			string[i] = toupper(string[i]);
		i++;
	}
}

void queryALaBase(int fdSocketCliente, char databaseName[64], char query[1024], char respuesta[1024]){
	char buffer[1024];
	memset(buffer,0,1024);
	strcat(buffer,"{\"databaseName\" : \"");
	strcat(buffer,databaseName);
	strcat(buffer,"\", \"query\" : \"");
	strcat(buffer,query);
	strcat(buffer,"\"}");
	printf("Send: %ld Bytes.\n", send(fdSocketCliente,&buffer,1024,0));
	printf("%s\n",buffer);			
	
	char bufferRecibo[1024];
	memset(bufferRecibo,0,1024);
	int cantidadBytesRecibidos;
	cantidadBytesRecibidos = recv(fdSocketCliente,bufferRecibo,1024,0);
	printf("Recv: %d Bytes.\n",cantidadBytesRecibidos);
	strcpy(respuesta,bufferRecibo);
}

void cerrarConexionBase(int fdSocketCliente, char databaseName[64]){
	char buffer[1024];
	memset(buffer,0,1024);
	strcat(buffer,"{\"databaseName\" : \"");
	strcat(buffer,databaseName);
	strcat(buffer,"\", \"query\" : \"0\"}");
	printf("Send: %ld Bytes. (%s)\n\n", send(fdSocketCliente,&buffer,1024,0),buffer);
		
}

void parseRespuesta(char respuesta[1024])
{
	xmlDocPtr doc;
	xmlNodePtr cur;
	doc = xmlReadMemory(respuesta,1024,"document.xml",NULL,0);
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
				while (cur != NULL)
				{
					if ((!xmlStrcmp(cur->name, (const xmlChar *) "status")))
						parseStatus(doc, cur);
					if ((!xmlStrcmp(cur->name, (const xmlChar *) "cols")))
						parseCols(doc, cur);
					if ((!xmlStrcmp(cur->name, (const xmlChar *) "rows")))
						parseRows(doc, cur);
					cur = cur -> next;
				}
			}
		}
	}
}

void parseStatus(xmlDocPtr doc, xmlNodePtr cur)
{
	xmlChar *status;
	cur = cur->xmlChildrenNode;
	while (cur != NULL) 
	{
	  status = xmlNodeListGetString(doc, cur->xmlChildrenNode, 1);
	  if (status != NULL)
	  	printf("%s\t", status);
	  xmlFree(status);
		cur = cur->next;
	}
	printf("\n");
}


void parseCols(xmlDocPtr doc, xmlNodePtr cur)
{
xmlChar *colname;
	cur = cur->xmlChildrenNode;
	while (cur != NULL) 
	{
	  colname = xmlNodeListGetString(doc, cur->xmlChildrenNode, 1);
	  if (colname != NULL)
	  	printf("%s\t", colname);
	  xmlFree(colname);
		cur = cur->next;
	}
	printf("\n");
}

void parseRows(xmlDocPtr doc, xmlNodePtr cur)
{
	xmlNodePtr curdos;
	xmlChar *rowValue;
	cur = cur->xmlChildrenNode;
	while (cur != NULL)
	{
		curdos = cur ->xmlChildrenNode;
		while (curdos != NULL){
	  rowValue = xmlNodeListGetString(doc, curdos->xmlChildrenNode, 1);
	  if (rowValue != NULL)
	  	printf("%s\t", rowValue);
	  xmlFree(rowValue);
	  	curdos = curdos->next;
	  }
	  printf("\n");
		cur = cur->next;
	}
}



