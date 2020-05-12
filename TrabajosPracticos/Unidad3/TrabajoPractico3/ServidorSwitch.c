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
  struct sockaddr_in s_sock, c_sock;
  int idsocks, idsockc;
  socklen_t lensock = sizeof(struct sockaddr_in);
  idsocks = socket(AF_INET, SOCK_STREAM, 0);
  printf("idsocks %d\n", idsocks);
  s_sock.sin_family = AF_INET;
  s_sock.sin_port = htons(PuertoSerBD);
  s_sock.sin_addr.s_addr = inet_addr(IPSerBD);
  memset(s_sock.sin_zero, 0, 8);
  printf("bind %d\n", bind(idsocks, (struct sockaddr * ) & s_sock, lensock));
  printf("listen %d\n", listen(idsocks, 5));
  while (1) {
    printf("esperando conexion\n");
    idsockc = accept(idsocks, (struct sockaddr * ) & c_sock, & lensock);
    if (idsockc != -1) {
      if (!fork()) {
        char query[1024];
        char databasename[1024];
        int nb1;
        printf("conexion aceptada desde el cliente\n");
        nb1 = read(idsockc, query, 1024);
        query[nb1] = '\0';
        if (query[0] == 'm') {
          ObtenerNombreBaseDatos(query, databasename);
          printf(".......recibido del cliente %d : %s %s\n", idsockc, databasename, query);
          char respuesta[1024];
          memset(respuesta, 0, 1024);
          funcionMysql(databasename, query, respuesta);
          write(idsockc, respuesta, 1024);
          Log(databasename, query);
        }
        if (query[0] == 'p') {
          ObtenerNombreBaseDatos(query, databasename);
          printf(".......recibido del cliente %d : %s %s\n", idsockc, databasename, query);
          char respuesta[1024];
          memset(respuesta, 0, 1024);
          funcionPostgresql(databasename, query, respuesta);
          write(idsockc, respuesta, 1024);
          Log(databasename, query);
        }
        printf("conexion finalizada con el cliente\n");
        close(idsockc);
        exit(0);
      }
    } else {
      printf("conexion rechazada %d \n", idsockc);
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
