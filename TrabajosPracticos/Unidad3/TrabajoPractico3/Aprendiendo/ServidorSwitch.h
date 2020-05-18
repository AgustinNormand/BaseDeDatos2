#define IPSerBD "192.168.0.79"
#define PuertoSerBD 6666
#define IPPostgrsql "192.168.1.30"
#define PuertoPostgresql "5432"
#define IPMySql "192.168.1.40"
#define PuertoMySql "3306"
char * DBnombres[1024];
char DBservidor[1024];
void funcionPostgresql(char [],char [],char []);
void funcionMysql(char [],char [],char []);
char buscarServidor(char * ,int);
void DameFechaMaquina( char *);
void DameHoraMaquina( char *);
void Log(char * ,char *);
