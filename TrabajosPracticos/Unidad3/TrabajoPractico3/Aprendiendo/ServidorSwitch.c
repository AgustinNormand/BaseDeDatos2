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
        char query[1024];
        char databasename[1024];
        int nb1;
        printf("Conexion aceptada desde el cliente\n");
        nb1 = read(idSocketCliente, query, 1024);
        query[nb1] = '\0';
        if (query[0] == 'm') {
          ObtenerNombreBaseDatos(query, databasename);
          printf(".......recibido del cliente %d : %s %s\n", idSocketCliente, databasename, query);
          char respuesta[1024];
          memset(respuesta, 0, 1024);
          funcionMysql(databasename, query, respuesta);
          write(idSocketCliente, respuesta, 1024);
          Log(databasename, query);
        }
        if (query[0] == 'p') {
          ObtenerNombreBaseDatos(query, databasename);
          printf(".......recibido del cliente %d : %s %s\n", idSocketCliente, databasename, query);
          char respuesta[1024];
          memset(respuesta, 0, 1024);
          funcionPostgresql(databasename, query, respuesta);
          write(idSocketCliente, respuesta, 1024);
          Log(databasename, query);
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
void ObtenerNombreBaseDatos(char query[], char databasename[]) {
  char queryaux[1024];
  int i;
  for (i = 0; query[i] != ':'; i++);
  int j, x = 0;
  for (j = i + 1; i < strlen(query) && query[j] != ':'; j++) {
    databasename[x] = query[j];
    x++;
  }
  databasename[x] = '\0';
  j++;
  int p;
  x = 0;
  for (p = j; p < strlen(query) && query[p] != '\0'; p++) {
    queryaux[x] = query[p];
    x++;
  }
  queryaux[x] = '\0';
  strcpy(query, queryaux);
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
