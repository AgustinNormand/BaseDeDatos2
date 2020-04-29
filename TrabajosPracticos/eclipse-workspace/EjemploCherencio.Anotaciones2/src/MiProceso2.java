import java.io.*;
/**
 * MiProceso2 no implementa proceso() 
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
@Procesar(archivoEntrada="README.TXT",archivoSalida="MISALIDA.TXT")
public class MiProceso2
{
	// instance variables - replace the example below with your own
	private int x;

	/**
	 * Constructor for objects of class MiProceso2
	 */
	public MiProceso2()
	{
		// initialise instance variables
		x = 0;
	}

	/**
	 * An example of a method - replace this comment with your own
	 * 
	 * @param  y   a sample parameter for a method
	 * @return     the sum of x and y 
	 */
	public int sampleMethod(int y)
	{
		// put your code here
		return x + y;
	}
}
