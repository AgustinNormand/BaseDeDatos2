#define maxEngines 3
const char engineNames[maxEngines][64] = {"POSTGRESQL","MYSQL","FIREBIRD"};
void cerrarConexionBase(int fdSocketCliente, /*char engineName[64],*/ char databaseName[64]);
void upperCase(char string[64]);
int validarEngineName(char engineName[64]);
void queryALaBase(int fdSocketCliente, /*char engineName[64],*/ char databaseName[64], char query[1024], char respuesta[1024]);
void parseRespuesta(char query[1024]);

