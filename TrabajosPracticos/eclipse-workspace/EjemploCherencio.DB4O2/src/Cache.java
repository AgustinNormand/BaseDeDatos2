import com.db4o.*;
import com.db4o.cs.*;
import com.db4o.query.*;
import java.util.*;
/**
 * Clase que representa los datos cache de la apliacion
 * en este caso, el listado de clientes y productos
 * Maldicion!, db4o no persiste datos static no final,
 * por ello, se opto por mantener en la aplicacion un
 * unico objeto de esta clase y persistirlo 
 * con asociaciones a listas de productos y
 * clientes
 * 
 * @author G.Cherencio 
 * @version 1.0
 */
public class Cache {
    private List<Cliente> cli = new ArrayList<Cliente>();
    private List<Producto> pro = new ArrayList<Producto>();

    public List<Producto> getProductos() { return pro; }
    public List<Cliente> getClientes() { return cli; }
    public void addCliente(Cliente c) { 
        // solo agrego si el codigo de cliente no existe previamente
        if ( c == null) return;
        for(Cliente cc : cli) if ( cc.getCodigo() == c.getCodigo() ) return;
        cli.add(c); 
    }
    public void removeCliente(Cliente c) { 
        if ( c == null) return;
        cli.remove(c); 
    }
    public void addProducto(Producto p) { 
        // solo agrego si el codigo de producto no existe previamente
        if ( p == null) return;
        for(Producto pp : pro) if ( pp.getCodigo() == p.getCodigo() ) return;
        pro.add(p); 
    }
    public void removeProducto(Producto p) { 
        if ( p == null) return;
        pro.remove(p); 
    }
    
    public void saveCache() {
        Util.getDb().store(this);
        //Util.getDb().store(Cache.pro);
        // por defecto, la profundidad de actualizacion es 1 ( es decir, solo
        // se graban los miembros del objeto que son primitivos o String);
        // para que grabe los miembros de cache, se debe configurar el contenedor (antes de ser abierto)
        // usar cascadeOnUpdate(), cascadeOnDelete()
        // ver initEmbebido() si no cambio profundidad de actualizacion x defecto, debere
        // actualizar manualmente los miembros de Cache:
        Util.getDb().commit();
System.out.println("saveCache():grabe cache! cli="+getClientes().size()+" pro="+getProductos().size());                
    }
    public static Cache loadCache() {
        Cache me = new Cache();
        Cache ome = null;
        try {
            ObjectSet<Cache> os = Util.getDb().queryByExample(me);
            if(os.hasNext()) {
                ome = os.next();
System.out.println("loadCache():recupere cache! hay "+ome.getClientes().size()+
                   " clientes y "+ome.getProductos().size()+" productos!");                
            }
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        return ome;
    }
    
}
