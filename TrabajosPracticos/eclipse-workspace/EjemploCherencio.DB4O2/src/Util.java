
import com.db4o.*;
import com.db4o.cs.*;
import com.db4o.query.*;
import com.db4o.config.*;
import java.util.*;
/**
 * Esta clase encapsula las operaciones para persistir a clientes 
 * dentro de b.d. db4o
 * Tambien permite ejecutar db4o y conectarse a su contenedor de objetos
 * en forma embebida o bien ejecutar servidor o bien ejecutar cliente
 * 
 * @author G. Cherencio
 * @version 1.0
 */
public class Util
{
    private static ObjectContainer db = null;
    private static ObjectServer server = null;
    private static Cache cache = null; // mantengo una unica instancia de Cache en toda la aplicacion
    public static void setCache(Cache c) { 
        cache = c; 
        if ( cache == null ) cache = new Cache();
    }
    public static Cache getCache() { return cache; }
    
    public static Cliente loadCliente(Cliente c) {
        Cliente ret = null;
        try {
            ObjectSet<Cliente> os = getDb().queryByExample(c);
            if(os.hasNext()) {
                ret = os.next();
//System.out.println("loadCliente(): cli="+ret);
            }
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return ret;
    }
    
    public static void initServer() {
        try{
            server = Db4oClientServer.openServer("mibase.db",8080);
            server.grantAccess("usuario","contrasenia");
            System.out.println("Server db4o running...");
            System.out.println("at localhost,port 8080,database mibase.db ...");
            System.out.println("Press any key to stop");
            System.in.read();
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        } finally {
            server.close();
        }
    }
    public static void initEmbebido() {
        EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
        // configuro profundidad de actualizacion de las clases del sistema
        //conf.common().objectClass(Cache.class).persistStaticFieldValues(); // solo valido para static final/enum/const
        conf.common().objectClass(Cache.class).cascadeOnUpdate(true);
        conf.common().objectClass(Cache.class).cascadeOnDelete(false);
        conf.common().objectClass(Cliente.class).cascadeOnUpdate(true);
        conf.common().objectClass(Cliente.class).cascadeOnDelete(true);
        conf.common().objectClass(Factura.class).cascadeOnUpdate(true);
        conf.common().objectClass(Factura.class).cascadeOnDelete(true);
        conf.common().objectClass(Detalle.class).cascadeOnUpdate(true);
        conf.common().objectClass(Detalle.class).cascadeOnDelete(false); // borrara el producto tambien
        // abro base de datos
        db = Db4oEmbedded.openFile(conf,"mibase.db");
    }
    public static void initClient() {
        try{
            db = Db4oClientServer.openClient("localhost", 8080, "usuario", "contrasenia");
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }        
    }
    public static ObjectContainer getDb() { return db; }
    public static void close() { 
System.out.println("close!");
        db.close(); 
    }
    public static void fin() {
        System.exit(0);
    }
    public static void insert(Cliente nuevo) {
        try {
            Cliente found = findFirst(new Cliente(nuevo.getCodigo()));
            if ( found != null && found.getCodigo() == nuevo.getCodigo() ) 
                found.setNombre(nuevo.getNombre());
            else found = nuevo;
            Util.getDb().store(found);
            Util.getDb().commit();
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    public static void delete(Cliente aborrar) {
        ObjectSet<Cliente> os = find(aborrar);
        if ( os != null ) {
            while(os.hasNext()) {
                try {
                    Cliente found = os.next();
                    getDb().delete(found);
                    getDb().commit();
                } catch(Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
    
    public static Cliente findFirst(Cliente c) {
        Cliente found = null;
        try {
            ObjectSet<Cliente> os = getDb().queryByExample(c);
            while(found == null && os.hasNext()) {
                found = os.next();
            }
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return found;
    }
    public static ObjectSet<Cliente> find(Cliente c) {
        ObjectSet<Cliente> os = null;
        try {
            os = getDb().queryByExample(c);
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return os;
    }
    public static StringBuilder list(Cliente c) {
        return list(find(c));
    }
    public static StringBuilder list(ObjectSet<Cliente> os) {
        StringBuilder sb = new StringBuilder();
        try {
            Cliente found = null;
            while(os.hasNext()) {
                found = os.next();
                sb.append("Codigo: "+found.getCodigo()+"\n"+
                          "Nombre: "+found.getNombre()+"\n"+
                          "Cantidad de Facturas: "+found.getFacturas().size()+"\n");
                for(Factura f : found.getFacturas()) {
                    sb.append("   Factura: "+f.getNro()+" $ "+f.getMonto()+"\n"+
                              "   Cantidad de Items: "+f.getDetalles().size()+"\n");
                    for(Detalle d : f.getDetalles()) {
                        sb.append("      "+d.getCantidad()+" "+
                                d.getProducto().getNombre()+"("+d.getProducto().getCodigo()+") $ "+d.getPrecio()+"\n");
                    }
                }
                sb.append("================\n");
            }
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return sb;
    }
    public static void deleteAll() {
        try {
            ObjectSet<Object> os = getDb().queryByExample(new Object());
            while(os.hasNext()) {
                getDb().delete(os.next());
            }        
            getDb().commit();
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    private static StringBuilder listByCli(Cliente c) {
        StringBuilder sb = new StringBuilder();
        try {
            ObjectSet<Cliente> os = getDb().queryByExample(c);
            sb=list(os);
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return sb;
    }
    public static StringBuilder listByProdName(String name) {
        Factura f = new Factura();
        Cliente c = new Cliente();
        Producto p = new Producto(0,name);
        Detalle d = new Detalle(p);
        f.add(d);
        c.add(f);
        return listByCli(c);
    }
    public static StringBuilder listByProd(int nro) {
        Factura f = new Factura();
        Cliente c = new Cliente();
        Producto p = new Producto(nro);
        Detalle d = new Detalle(p);
        f.add(d);
        c.add(f);
        return listByCli(c);
    }
    public static StringBuilder listByFac(int nro) {
        Factura f = new Factura(nro);
        Cliente c = new Cliente();
        c.add(f);
        return listByCli(c);
    }
    public static StringBuilder listNotName(String nombre) {
        StringBuilder sb = new StringBuilder();
        try {
            Query query = getDb().query();
            query.constrain(Cliente.class);
            query.descend("nombre").constrain("pepe").not();
            ObjectSet<Cliente> os = query.execute();
            sb=list(os);
System.out.println("listNotName="+sb.toString());
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return sb;
    }
}
