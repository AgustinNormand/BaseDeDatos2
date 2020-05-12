#include "/usr/include/mysql/mysql.h"

#include <sys/types.h>

#include <string.h>

#include <stdlib.h>

#include <stdio.h>

#include <unistd.h>

#include <fcntl.h>

# define IPMysql "192.168.1.40" //Ubicación del motor MySql
# define PuertoMysql "3306"
void funcionMysql(char dabasename[], char query[], char respuesta[]) {
  MYSQL * conn; // variable de conexión para MySQL
  MYSQL_RES * res; // variable que contendra el resultado de la consuta
  MYSQL_ROW row; // variable que contendra los campos por cada registro consultado
  char * server = IPMysql; //direccion del motor MySql
  char * user = "jromer"; //usuario para consultar la base de datos
  char * password = "xxxxxxxxx"; // contraseña para el usuario
  char * database = dabasename; //nombre de la base de datos a consultar
  conn = mysql_init(NULL); //inicializacion
  memset(respuesta, 0, 1024);
  /* conectar a la base de datos */
  if (!mysql_real_connect(conn, server, user, password, database, 0, NULL, 0)) {
    /* definir los parámetros de la conexión antes establecidos */
    sprintf(respuesta, "%s\n", mysql_error(conn)); /* si hay un error definir cual fue dicho error */ //
    strcat(respuesta, "\0");
  } else {
    /* enviar consulta SQL */
    if (mysql_query(conn, query)) {
      /* definicion de la consulta y el origen de la conexion */
      sprintf(respuesta, "%s\n", mysql_error(conn));
      strcat(respuesta, "\0");
    } else {
      res = mysql_use_result(conn);
      if (res == NULL) {
        strcpy(respuesta, "ok query\0");
      } else {
        int num_attrib = mysql_num_fields(res);
        while ((row = mysql_fetch_row(res)) != NULL) {
          /* recorrer la variable res con todos los registros obtenidos para su uso */
          // printf("%s\t%s\t%s \n", row[0],row[1]); /* la variable row se convierte en un arreglo por el numero de campos que hay en la tabla */
          int i;
          for (i = 0; i < num_attrib; i++) {
            strcat(respuesta, row[i]);
            strcat(respuesta, " ");
          }
          strcat(respuesta, "\n");
        }
        strcat(respuesta, "\0");
        mysql_free_result(res);
      }
    }
  }
  /* se libera la variable res y se cierra la conexión */
  mysql_close(conn);
}
