import java.util.*;
/**
 * Clase generica que sirve para apilar objetos de tipo T
 * 
 * @author G.Cherencio 
 * @version 1.0
 */
public class Pila<T>
{
    // instance variables - replace the example below with your own
    private Stack<T> stack;

    /**
     * Constructor for objects of class Pila
     */
    public Pila()
    {
        // initialise instance variables
        stack = new Stack<T>();
    }
    /**
     * Constructor for objects of class Pila
     */
    public Pila(T x)
    {
        // initialise instance variables
        this();
        poner(x);
    }

    /**
     * Poner en la pila
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public void poner(T x) {
        stack.push(x);
    }
    /**
     * Sacar de la pila
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public T sacar() {
        return stack.pop();
    }
}
