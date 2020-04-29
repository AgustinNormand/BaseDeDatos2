
import java.io.*;
/**
 * MiProceso1 implementa proceso() en donde interviene entrada, salida, error
 * std. Requiere de constructor nulo
 * Ejercicio:
 *   1. Cambie de lugar la anotacion Procesar, pongala a nivel de atributo o metodo y compile
 *      observe lo que sucede ¿Por que?
 *   2. Pruebe el codigo no indicando ningun atributo, indicando solo archivo de entrada o salida o error o bien todos
 *   
 *   
 * Temas: entrada,salida,error,anotaciones propias,interfase, etc.
 * 
 * @author G.Cherencio
 * @version 1.0
 */
@Procesar(archivoEntrada="MIENTRADA.TXT",archivoSalida="MISALIDA.TXT",archivoError="MIERROR.TXT")
public class MiProceso1 implements Procesable
{
    // instance variables - replace the example below with your own

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
