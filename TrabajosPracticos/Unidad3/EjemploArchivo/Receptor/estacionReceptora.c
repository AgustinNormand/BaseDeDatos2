#include <arpa/inet.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <json-c/json.h>

int main(int arc, char * argv[])
{
	struct sockaddr_in socketServidor, socketCliente;
	int fdSocketServidor, fdSocketCliente;
	socklen_t longitudSocket = sizeof(struct sockaddr_in);
	fdSocketServidor = socket(AF_INET,SOCK_STREAM,0);
	printf("fdSocketServidor: %d\n", fdSocketServidor);
	socketServidor.sin_family = AF_INET;
	socketServidor.sin_port = htons(1234);
	socketServidor.sin_addr.s_addr = inet_addr("127.0.0.1");
	memset(socketServidor.sin_zero,0,8);
	
	printf("Bind: %d\n", bind(fdSocketServidor, (struct sockaddr * ) & socketServidor, longitudSocket));
	printf("Listen: %d\n", listen(fdSocketServidor, 1));
	while(1){
		printf("Esperando Conexion con un Emisor ...\n");
		fdSocketCliente = accept(fdSocketServidor, (struct sockaddr * ) & socketCliente, & longitudSocket);
		if (fdSocketCliente != -1) {
			if (!fork()) {
				char buffer[1024];
				int cantidadBytesLeidos;
				
				printf("Conexion aceptada\n");
				cantidadBytesLeidos = recv(fdSocketCliente,buffer,1024,0);
				buffer[cantidadBytesLeidos] = '\0';
				
				struct json_object *parsed_json;
				struct json_object *engineName;
				struct json_object *databaseName;
				struct json_object *query;
				
				parsed_json = json_tokener_parse(buffer);
				
				json_object_object_get_ex(parsed_json, "engineName", &engineName);
				json_object_object_get_ex(parsed_json, "databaseName", &databaseName);
				json_object_object_get_ex(parsed_json, "query", &query);
				
				printf("engineName: %s\n",json_object_get_string(engineName));
				printf("databaseName: %s\n",json_object_get_string(databaseName));
				printf("query: %s\n",json_object_get_string(query));
				
				
				FILE *fdArchivo = fopen("archivoRecibido.txt","w");
				fprintf(fdArchivo,"%s",buffer);
				fclose(fdArchivo);
			}
		} else {
      printf("Conexion Rechazada %d \n", fdSocketCliente);
			}
	}
	return 0;
}
