#include <string.h>

#include <libpq-fe.h> //Hay que descargarla

#include <sys/types.h>

#include <string.h>

#include <stdlib.h>

#include <stdio.h>

#include <unistd.h>

#include <fcntl.h>
 //Ubicacion en la red del servidor Postgresql
#define IPPostgresql "192.168.1.30" // IP de la máquina virtual Linux Debian donde esta
// en ejecucion el motor postgresql
# define PuertoPostgresql "5432"
void funcionPostgresql(char bufnomb[], char bufquery[], char respuesta[]) {
  PGconn * conn;
  PGresult * res;
  int i, j;
  conn = PQsetdbLogin(IPPostgresql, PuertoPostgresql, NULL, NULL, bufnomb, "postgres", "clave");
  if (PQstatus(conn) != CONNECTION_BAD) {
    res = PQexec(conn, bufquery);
    if (res != NULL && PGRES_TUPLES_OK == PQresultStatus(res)) {
      for (i = 0; i <= PQntuples(res) - 1; i++) {
        for (j = 0; j < PQnfields(res); j++) {
          strcat(respuesta, PQgetvalue(res, i, j));
          strcat(respuesta, "\t");
        }
        strcat(respuesta, "\n");
      }
      strcat(respuesta, "\0");
      PQclear(res);
    }
  } else {
    strcat(respuesta, "Fallo conexion postgresql\n");
  }
  PQfinish(conn);
}
