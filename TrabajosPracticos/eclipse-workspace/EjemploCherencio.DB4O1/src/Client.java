
/**
 * Conecta a servidor db4o en localhost, puerto tcp 8080,
 * base de datos mibase.db , utilizando usuario "usuario" con clave "contrasenia"
 * N clientes podrian acceder a esta base de datos mientras el servidor
 * este corriendo
 * 
 * ATENCION:
 * BlueJ no funciona con db4o desde el entorno de desarrollo
 * provoca errores en la recuperacion de objetos y se debe
 * a la carga de las clases db4o desde su archivo jar
 * Desde linea de comando este problema no se reproduce
 * SOLUCION:
 * a) ejecutar proyecto desde linea de comandos
 * b) exportar proyecto en archivo jar y ejecutar desde linea de comandos
 * 
 * Ejecucion desde linea de comandos en ubicacion actual:
 * 
   $ java -cp ../db4o-8.0/lib/db4o-8.0.249.16098-all-java5.jar:. Client
 * 
 * @author G.Cherencio
 * @version 1.0 
 */
public class Client {
    public static void main(String[] arg) {
        Util.initClient();
        new View();
    }
}