
import java.io.*;
/**
 * Implementa interfase pero no indica anotacion Procesar
 * Ejecutar HagoProceso desde Consola!
 * Ojo que esta clase pedira 2 caracteres por consola!!
 * debido a que no implementa anotacion Procesar
 * 
 * @author G.Cherencio 
 * @version 1.0
 */
public class MiProceso3 implements Procesable
{
    /**
     * Implemento el proceso a realizar
     * Lee dos caracteres por stdin y los escribe en stdout
     * 
     */
    public void proceso()
    {
        // put your code here
        try {
            char c1 = (char) System.in.read();
            char c2 = (char) System.in.read();
            System.out.print(c1);
            System.out.print(c2);
        } catch(IOException ioe) {
        }
    }
}
