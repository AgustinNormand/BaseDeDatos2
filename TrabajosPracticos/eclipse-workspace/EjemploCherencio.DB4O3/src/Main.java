
import com.db4o.*;
import com.db4o.cs.*;
import com.db4o.query.*;
/**
 * Aplicacion de consola
 * TP II Ej 1
 * 11078 Base de Datos II
 * 
 * @author G. Cherencio
 * @version 1.0
 */
public class Main {
    public static void main(String[] arg) {
        // inicializo db4o embebida
        System.out.println("Abro/creo base de datos tp2ej1.db");
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),"tp2ej1.db");
        System.out.println("Creo objeto Factura");
        Factura f = new Factura(100,new Cliente(222,"Cliente 222"),123.45);
        System.out.println("Objeto Factura creado:");
        System.out.println(f);
        System.out.println("que esta asociado con Objeto Cliente:");
        System.out.println(f.getCliente());
        System.out.println("Busco Factura en bd db4o:");
        Factura f_en_bd = null;
        try {
            // busco este objeto Factura en la bd
            ObjectSet<Factura> os = db.queryByExample(f);
            if ( os.hasNext() ) {
                f_en_bd = os.next();
            }
            // evaluo resultado de la busqueda
            if ( f_en_bd != null && f_en_bd.getNro() == f.getNro() ) {
                // este objeto Factura ya estaba guardado en bd db4o tp2je1.db
                System.out.println("Encontre Factura en bd:\n"+f_en_bd);
                f_en_bd.setCliente(f.getCliente());
                f_en_bd.setImporte(f.getImporte());
            } else {
                // este objeto Factura no estaba en bd
                f_en_bd = f;
            }
            System.out.println("Guardo Factura en bd:\n"+f_en_bd);
            // guardo objeto Factura en bd
            db.store(f_en_bd);
            // commit
            System.out.println("commit!");
            db.commit();
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        // cierro bd embebida
        System.out.println("cierro!");
        db.close();
    }
}
