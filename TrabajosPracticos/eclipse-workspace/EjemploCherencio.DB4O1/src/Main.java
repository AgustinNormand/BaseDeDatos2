
/**
 * Crea/conecta b.d. db4o para persistir objetos
 * Utiliza db4o en embebido en aplicacion
 * Un solo proceso por vez puede utilizar la b.d.
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
   $ java -cp ../db4o-8.0/lib/db4o-8.0.249.16098-all-java5.jar:. Main
 * 
 * @author G.Cherencio
 * @version 1.0
 */
public class Main {
    public static void main(String[] arg) {
        Util.initEmbebido();
        System.out.println("db="+Util.getDb());
        //db.backup("mibase.back.db");
        new View();
    }
}
