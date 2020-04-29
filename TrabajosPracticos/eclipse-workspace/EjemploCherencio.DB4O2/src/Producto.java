
/**
 * clase POJO que representa un producto
 *
 * @author G.Cherencio
 * @version 1.0
 */
public class Producto
{
    // instance variables - replace the example below with your own
    private int codigo;
    private String nombre;
    private double precio;

    public Producto() {
        this(0,null,0.0);
    }
    public Producto(int c) {
        this(c,null,0.0);
    }
    public Producto(int c,String n) {
        this(c,n,0.0);
    }
    public Producto(int c,String n,double p) {
        setCodigo(c);setNombre(n);setPrecio(p);
    }

    
    public void   setCodigo(int n) { codigo = n; }
    public void   setNombre(String n) { nombre = n; }
    public void   setPrecio(double d) { precio = d; }
    public int    getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    
    @Override
    public String toString() { return getNombre(); }
}
