
/**
 * clase POJO que representa un detalle de factura
 * 
 * @author G.Cherencio
 * @version 1.0
 */
public class Detalle {
    private Producto producto;
    private int cantidad;
    private double precio;

    /**
     * Constructor for objects of class Detalle
     */
    public Detalle() {
        this(null,0,0.0);
    }
    public Detalle(Producto p) {
        this(p,0,0.0);
    }
    public Detalle(Producto p,int n) {
        this(p,n,0.0);
    }
    public Detalle(Producto p,int n,double pr) {
        setProducto(p);setCantidad(n);setPrecio(pr);
    }

    public void     setCantidad(int n)     { cantidad = n; }
    public void     setPrecio(double d)    { precio = d; }
    public void     setProducto(Producto p){ producto = p; }
    public int      getCantidad()          { return cantidad; }
    public double   getPrecio()            { return precio; }
    public Producto getProducto()          { return producto; }

    @Override
    public String toString() { return ""+getCantidad()+" "+getProducto().getNombre()+" $ "+getPrecio(); }
    
}
