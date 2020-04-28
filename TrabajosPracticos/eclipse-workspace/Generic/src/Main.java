import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

	public static void main(String[] args) {
		Elemento<String> elemento = new Elemento<String>();
		elemento.setDato("Hola");
		System.out.println(elemento.getDato());
		Method[] metodos = elemento.getClass().getDeclaredMethods();
		try {
			metodos[0].invoke(elemento, "Buen Día");           //setDato
			System.out.println(metodos[1].invoke(elemento));   //getDato
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
}
