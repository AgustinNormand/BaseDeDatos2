
/**
 * Write a description of class Factura here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Factura
{
    // instance variables - replace the example below with your own
    private int nro;
    private Cliente id;
    private double importe;

    /**
     * Constructor for objects of class Factura
     */
    public Factura() {
        this(1,null,0.0);
    }
    public Factura(int n) {
        this(n,null,0.0);
    }
    public Factura(int n,Cliente c) {
        this(n,c,0.0);
    }
    public Factura(int n,Cliente c,double i) {
        setNro(n);setCliente(c);setImporte(i);
    }
    
    // SETTERS
    public void setNro(int n) { nro = n; }
    public void setCliente(Cliente c) { id = c; }
    public void setImporte(double i) { importe = i; }
    // GETTERS
    public int getNro() { return nro; }
    public Cliente getCliente() { return id; }
    public double getImporte() { return importe; }
    
    @Override
    public String toString() {
        return "[factura nro:"+getNro()+", cliente:"+getCliente().getDescr()+
            ",$:"+getImporte()+"]";
    }
    
}
