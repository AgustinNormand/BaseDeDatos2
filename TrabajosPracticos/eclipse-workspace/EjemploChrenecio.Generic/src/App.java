import java.util.*;
/**
 * Aplicacion que usa la clase Pila para apilar objetos en forma generica
 * 
 * Discusion: ventajas y desventajas de clases genericas, relacion con upcasting/downcasting,
 *            polimorfismo, relacion con clase Object, por que se produce el error de compilacion de la linea 23
 *            
 * @author G.Cherencio 
 * @version 1.0
 */
public class App {
    public static void main(String arg[]) {
        Pila<String> ps = new Pila<String>();
        ps.poner("hola");
        ps.poner("que");
        ps.poner("tal");

        App.desapilo(ps);
        
        Pila<Integer> pi = new Pila<Integer>();
        pi.poner(1); // autoboxing
        pi.poner(2); // autoboxing
        //pi.poner("hola");  //        ERROR DE COMPILACION!!!!!!!!!
        pi.poner(3); // autoboxing
        pi.poner(new Integer(5));

        App.desapilo(pi);
    }
    
    public static void desapilo(Pila p) {
        try {
            while(true) System.out.println("Desapilo: "+p.sacar());
        } catch(EmptyStackException ese) {
            // sale por aqui cuando ya no queda mas nada en la pila
            System.out.println("Desapile todo");
        }
    }

}
