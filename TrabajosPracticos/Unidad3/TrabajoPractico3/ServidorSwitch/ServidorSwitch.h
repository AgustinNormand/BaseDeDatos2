
char * DBnombres[1024];
char DBservidor[1024];
void queryAPostgresql(char [],char [],char []);
void queryAMysql(char [],char [],char []);
void queryAFirebird(char [],char [],char []);
int verificarFirebirdDatabase(char []);
char buscarServidor(char * ,int);
int recorrerRespuesta(char bufferRespuesta[1024], char databaseName[64]);
int determinarEngineName(char databaseName[64],char engineName[64]);
void readSocket(int fdSocketCliente, char databaseName[64], char query[1024]);
void parseJson(char buffer[], char databaseNameString[], char queryString[]);

