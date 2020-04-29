
/**
 * Aplicacion de consola para probar inyeccion de dependencias
 * 
 * @author G. Cherencio
 * @version 1.0
 */
public class App {
    public static void main(String[] arg) {
        System.out.println("Incializo conexion a b.d.");
        if ( DataIO.init() ) {
            DoQuery dq = new DoQuery("SELECT DESCR FROM TBL_PLANE");
            System.out.println("Uso conexion");
            dq.doQuery();
            System.out.println("Fin!");
            DataIO.finish();
        }
    }
}
