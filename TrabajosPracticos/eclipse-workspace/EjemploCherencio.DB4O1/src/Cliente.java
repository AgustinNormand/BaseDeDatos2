
/**
 * Clase POJO simple a persistir en db4o
 * 
 * @author G.Cherencio
 * @version 1.0
 */
public class Cliente
{
    // instance variables - replace the example below with your own
    private int codigo;
    private String nombre;

    public Cliente()
    {
        // initialise instance variables
        this(0,null);
    }
    public Cliente(int c) {
        this(c,null);
    }
    public Cliente(int c,String n) {
        setCodigo(c);setNombre(n);
    }

    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    
    public void setCodigo(int n) { codigo = n; }
    public void setNombre(String n) { nombre = n; }
    
}
