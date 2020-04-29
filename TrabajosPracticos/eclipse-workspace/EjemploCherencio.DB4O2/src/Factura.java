
import java.util.*;
/**
 * clase POJO que representa una factura
 * 
 * @author G.Cherencio
 * @version 1.0
 */
public class Factura
{
    // instance variables - replace the example below with your own
    private int nro;
    private double monto;
    private List<Detalle> det = new ArrayList<Detalle>();

    public Factura() {
        this(0,0.0);
    }
    public Factura(int n) {
        this(n,0.0);
    }
    public Factura(int n,double d) {
        setNro(n);setMonto(d);
    }

    public void   setNro(int n) { nro = n; }
    public void   setMonto(double d) { monto = d; }
    public int    getNro() { return nro; }
    public double getMonto() { return monto; }
    
    public void add(Detalle d)    { det.add(d);    }
    public void remove(Detalle d) { det.remove(d); }
    public List<Detalle> getDetalles() { return det; }
    
    @Override
    public String toString() { return ""+getNro()+" $ "+getMonto(); }
    
}
