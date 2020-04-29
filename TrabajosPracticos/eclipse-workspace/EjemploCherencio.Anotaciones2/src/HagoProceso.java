
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.io.*;
/**
 * Clase que permite la ejecucion (previa redireccion de la entrada, la salida y el
 * error standard acorde con lo indicado con la anotacion propia "Procesar") de toda
 * clase "procesable" (que implementa la interfaz Procesable)
 * 
 * Temas: anotaciones, anotaciones propias, reflection, redireccion stdin-stdout-stderr,
 * clase System, Interfase, java.io, etc.
 * 
 * @author G.Cherencio
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class HagoProceso
{
    /**
     * hagoProceso() permite la ejecucion -mediante reflection- de instancias de las clases
     * que se indiquen como argumento de entrada
     * La entrada standard sera redirigida tal como se indique en la anotacion Procesar de la clase indicada
     * Idem para la salida standard y el error standard
     * Si no se anota y/o los valores de la anotacion son aquellos por defecto, no se hace redireccionamiento
     */
    public static void hagoProceso(String[] aprocesar)
    {
        if ( aprocesar == null ) return;
        // para clase:
        for(String sclass : aprocesar) {
            try {
                Class c = Class.forName(sclass);
                boolean implementaProcesable = false;
                for(Class cint : c.getInterfaces()) {  // si se usa c.getInterfaces() no ubica interfases implementadas en superclases
                    if ( cint.getName().equals("Procesable") ) {
                        implementaProcesable = true;
                        break;
                    }
                }
                if ( !implementaProcesable ) { // es posible que alguna superclase implemente la interfase Procesable
                    try {
                        Object o = c.newInstance();
                        if ( o instanceof Procesable ) implementaProcesable=true;
                    }catch(Exception e) {
                    }
                }
                if ( implementaProcesable ) {
                    System.out.println("HagoProceso.hagoProceso():Intento Ejecucion de clase ["+sclass+"]");
                    Class<Procesable> cp = c;
                    try {
                        Procesable p = cp.newInstance();
                        Procesar pann = null;
                        for(Annotation a : cp.getAnnotations()) {
                            if ( a instanceof Procesar ) {
                                pann = (Procesar) a;
                                break;
                            }
                        }
                        boolean rentrada = false,rsalida = false,rerror = false;
                        // Debo Redirigir algo?
                        if ( pann != null ) {
                            if ( pann.archivoEntrada().length() > 0  )  rentrada=true; 
                            if ( pann.archivoSalida().length() > 0 ) rsalida=true;
                            if ( pann.archivoError().length() > 0 ) rerror=true;
                        }
                        InputStream sin = null,oin = System.in;
                        PrintStream sout = null, oout = System.out;
                        PrintStream serr = null, oerr = System.err;
                        boolean hiceIn=false,hiceOut=false,hiceErr=false;
                        // Hago Redirecciones
                        if ( rentrada ) {
                            try {
                                System.out.println("HagoProceso.hagoProceso() tomo entrada de ["+pann.archivoEntrada()+"]");
                                sin = new FileInputStream(pann.archivoEntrada());
                                System.setIn(sin); // redirijo entrada
                                hiceIn=true;
                            } catch(FileNotFoundException fnfe) {
                                sin = null;
                                System.err.println("Clase ["+sclass+"] archivo ["+pann.archivoEntrada()+"] no exite!");
                            } catch(SecurityException se) {
                                if ( sin != null ) try { sin.close();sin=null; } catch(Exception e) {}
                                System.err.println("Clase ["+sclass+"] archivo ["+pann.archivoEntrada()+"] problemas de seguridad!");
                            }
                        }
                        if ( rsalida ) {
                            try {
                                System.out.println("HagoProceso.hagoProceso() envio salida a ["+pann.archivoSalida()+"]");
                                sout = new PrintStream(pann.archivoSalida());
                                System.setOut(sout); // redirijo salida
                                hiceOut=true;
                            } catch(FileNotFoundException fnfe) {
                                sout = null;
                                System.err.println("Clase ["+sclass+"] archivo ["+pann.archivoSalida()+"] no accesible!");
                            } catch(SecurityException se) {
                                if ( sout != null ) try { sout.close();sout=null; } catch(Exception e) {}
                                System.err.println("Clase ["+sclass+"] archivo ["+pann.archivoSalida()+"] problemas de seguridad!");
                            }
                        }
                        if ( rerror ) {
                            try {
                                System.err.println("HagoProceso.hagoProceso() envio error a ["+pann.archivoError()+"]");                                
                                serr = new PrintStream(pann.archivoError());
                                System.setErr(serr); // redirijo error
                                hiceErr=true;
                            } catch(FileNotFoundException fnfe) {
                                serr = null;
                                System.err.println("Clase ["+sclass+"] archivo ["+pann.archivoError()+"] no accesible!");
                            } catch(SecurityException se) {
                                if ( serr != null ) try { serr.close();serr=null; } catch(Exception e) {}
                                System.err.println("Clase ["+sclass+"] archivo ["+pann.archivoError()+"] problemas de seguridad!");
                            }
                        }
                        // Ejecuto
                        p.proceso();
                        // Restablezco Redirecciones
                        try {
                            if ( hiceIn ) {
                                sin.close();
                                System.setIn(oin);
                            }
                            if ( hiceOut ) {
                                sout.close();
                                System.setOut(oout);
                            }
                            if ( hiceErr ) {
                                serr.close();
                                System.setErr(oerr);
                            }
                        }catch(Exception e) {
                        }
                    } catch(InstantiationException ie) {
                        System.err.println("Clase ["+sclass+"] no puede instanciarse!");                        
                    } catch(IllegalAccessException iae) {
                        System.err.println("Clase ["+sclass+"] no accesible!");                        
                    }
                } else {
                    System.err.println("Clase ["+sclass+"] no implementa interfaz Procesable!");
                }
            } catch(ClassNotFoundException cnfe) {
                System.err.println("Clase ["+sclass+"] no existe");
            }
        }
        
    }
    
}
