#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <netdb.h>

int main(int argcm, char * argv[]){
	struct sockaddr_in socketServidor;
	int fdSocketCliente;
	socklen_t longitudSocket = sizeof(struct sockaddr_in);
	fdSocketCliente = socket(AF_INET,SOCK_STREAM,0);
	printf("fdSocketCliente: %d\n", fdSocketCliente);
	socketServidor.sin_family = AF_INET;
	socketServidor.sin_port = htons(6666);
	socketServidor.sin_addr.s_addr = inet_addr("192.168.0.79");
	memset(socketServidor.sin_zero,0,8);
	
	printf("Connect: %d\n", connect(fdSocketCliente,(struct sockaddr *) & socketServidor, longitudSocket));
	FILE *fdArchivo = fopen("archivoAEnviar.json","r");
	if (fdArchivo != NULL)
	{
		char buffer[1024];
		char ch;
		int i = 0;
		while((ch = fgetc(fdArchivo)) != EOF)
      buffer[i++] = ch;
		printf("Send: %ld\n", send(fdSocketCliente,& buffer,1024,0));
		fclose(fdArchivo);
	} else
		printf("Error en la apertura del archivo\n");
	return 0;
}
