
import java.util.*;
/**
 * Clase sin importancia para usar anotacion de usuario Documentar
 * ver la salida de javadoc de esta clase
 * 
 * Ejercicio:
 *   1. Verificar que en Anotacion Documentar existe el import de java.lang.annotation.*; 
 *      y anotacion Documented antes de declarar anotacion Documentar
 *   2. Generar documentacion usando: javadoc *.java desde linea de comandos en el directorio
 *      de este proyecto, verificar el resultado. Se deberia ver la anotacion como parte de la
 *      documentacion de la clase
 *   3. Modificar Anotacion Documentar y comentar anotacion Documented
 *   4. Volver a generar documentacion: javadoc *.java (idem punto 2) verificar que ya no aparezca
 *      la anotacion Documentar como parte de la documentacion de la clase MiClase
 *      
 * Tema: Uso de anotaciones, anotaciones propias, javadoc, Interface, Calendar, StringBuffer
 * @author G.Cherencio
 * @version 1.0
 * 
 */
@Documentar(autor="Pepe")
public class MiClase
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor para objetos de MiClase
     */
    public MiClase()
    {
        // initialise instance variables
        x = 0;
    }

    /**
     * Metodo para incrementar contador interno de la clase
     * 
     * @param  y   cantidad a incrementar/decrementar
     * @return     nuevo valor de contador
     */
    public int incrementar(int y)
    {
        // put your code here
        return x+=y;
    }
    
    public static void main(String[] arg) {
        System.out.println(MiClase.today());
    }
    
    /**
     * Calcula y devuelve fecha actual
     *
     * @return devuelve fecha actual en formato dd/mm/yyyy
     */
    public static String today() {
        Calendar now = Calendar.getInstance();
        int dia = now.get(Calendar.DATE);
        int mes = now.get(Calendar.MONTH)+1; // mes 0 based
        int anio = now.get(Calendar.YEAR);
        StringBuffer sb = new StringBuffer(new String().format("%2d/%2d/%4d",dia,mes,anio));
        for(int z=0;z<sb.length();z++) if ( sb.charAt(z) == ' ' ) sb.replace(z,z+1,"0");
        String str = sb.toString();
        return str;
    }
}
