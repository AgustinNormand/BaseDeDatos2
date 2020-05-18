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

#include <json-c/json.h>

void parseJson(char buffer[], char engineNameString[], char databaseNameString[], char queryString[]);
void ObtenerNombreBaseDatos(char query[], char databasename[]);
int main(int argc, char * argv[]) {
  struct sockaddr_in socketServidor, socketCliente;
  int idSocketServidor, idSocketCliente;
  socklen_t lensock = sizeof(struct sockaddr_in);
  idSocketServidor = socket(AF_INET, SOCK_STREAM, 0);
  printf("idSocketServidor: %d\n", idSocketServidor);
  socketServidor.sin_family = AF_INET;
  socketServidor.sin_port = htons(PuertoSerBD);
  socketServidor.sin_addr.s_addr = inet_addr(IPSerBD);
  memset(socketServidor.sin_zero, 0, 8);
  
  printf("Bind: %d\n", bind(idSocketServidor, (struct sockaddr * ) & socketServidor, lensock));
  printf("Listen: %d\n", listen(idSocketServidor, 5));
  
  while (1) {
    printf("Esperando Conexion ...\n");
    idSocketCliente = accept(idSocketServidor, (struct sockaddr * ) & socketCliente, & lensock);
    if (idSocketCliente != -1) {
      if (!fork()) {
	      printf("Conexion aceptada desde el cliente\n");
	      
        char buffer[1024];
        int cantidadBytesLeidos;
        
        cantidadBytesLeidos = read(idSocketCliente, buffer, 1024);
       //buffer[cantidadBytesLeidos] = '\0';
        
        printf("Cantidad de bytes leidos: %d\n",cantidadBytesLeidos);
        
        char engineName[64];
        char databaseName[64];
        char query[1024];
        
        parseJson(buffer, engineName, databaseName, query);
                
        printf("EngineName: %s\n",engineName);
        printf("DatabaseName: %s\n",databaseName);
        printf("Query: %s\n",query);
        
        if (strcmp(engineName,"MySql") == 0) {
        	printf("Query a MySql\n");
        //  ObtenerNombreBaseDatos(query, databaseName);
        //  printf(".......recibido del cliente %d : %s %s\n", idSocketCliente, databaseName, query);
        //  char respuesta[1024];
      //   memset(respuesta, 0, 1024);
      //    funcionMysql(databaseName, query, respuesta);
       //   write(idSocketCliente, respuesta, 1024);
       //   Log(databaseName, query);
        }
				if (strcmp(engineName,"Postgresql") == 0) {
					printf("Query a Postgresql\n");
//          ObtenerNombreBaseDatos(query, databaseName);
  //        printf("Peticion recibida\n");
    //      printf("idSocketCliente: %d, databaseName: %s, query: %s\n", idSocketCliente, databaseName, query);
      //    char respuesta[1024];
        //  memset(respuesta, 0, 1024);
//          funcionPostgresql(databaseName, query, respuesta);
  //        write(idSocketCliente, respuesta, 1024);
    //      Log(databaseName, query);
        }
        printf("conexion finalizada con el cliente\n");
        close(idSocketCliente);
        exit(0);
      }
    } else {
      printf("conexion rechazada %d \n", idSocketCliente);
    }
  }
  return 0;
}

void parseJson(char buffer[], char engineNameString[], char databaseNameString[], char queryString[])
{
	struct json_object *parsed_json;
	struct json_object *engineName;
	struct json_object *databaseName;
	struct json_object *query;
				
	parsed_json = json_tokener_parse(buffer);
				
	json_object_object_get_ex(parsed_json, "engineName", &engineName);
	json_object_object_get_ex(parsed_json, "databaseName", &databaseName);
	json_object_object_get_ex(parsed_json, "query", &query);
	
	strcpy(engineNameString, json_object_get_string(engineName));
	strcpy(databaseNameString, json_object_get_string(databaseName));
	strcpy(queryString, json_object_get_string(query));
}

void Log(char * db, char * sql) {
  char Fecha[128];
  char Hora[128];
  DameFechaMaquina(Fecha);
  DameHoraMaquina(Hora);
  int fd = open("log", O_CREAT | O_WRONLY, 0666);
  lseek(fd, 0, 2);
  write(fd, Fecha, strlen(Fecha));
  write(fd, " ", 1);
  write(fd, Hora, strlen(Hora));
  write(fd, " ", 1);
  write(fd, db, strlen(db));
  write(fd, " ", 1);
  write(fd, sql, strlen(sql));
  write(fd, "\n", 1);
  printf("%s %s %s %s\n", Fecha, Hora, db, sql);
  close(fd);
}
void DameFechaMaquina(char * Fecha) {
  time_t tiempo = time(0);
  struct tm * tlocal = localtime( & tiempo);
  strftime(Fecha, 128, "%d/%m/%y", tlocal);
}
void DameHoraMaquina(char * Hora) {
  time_t tiempo = time(0);
  struct tm * tlocal = localtime( & tiempo);
  strftime(Hora, 128, "%H:%M:%S", tlocal);
}
