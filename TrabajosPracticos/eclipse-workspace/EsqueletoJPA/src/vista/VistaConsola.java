package vista;

import java.util.Scanner;

import ModelController.Gestor;
import ModelController.Test;

public class VistaConsola {

	private Scanner scan = new Scanner(System.in);

	Gestor gdb = Gestor.getInstance();

	public VistaConsola() {
		System.out.println("Consola de Esqueleto.JPA");
		menuPrincipal();
	}

	public void menuPrincipal() {
		int opcion = -1;
		while (opcion != 0) {
			System.out.println("Ingrese una opcion");
			System.out.println("1)");
			System.out.println("2)");
			System.out.println("3)");
			System.out.println("4)");
			System.out.println("0) Salir");

			opcion = scan.nextInt();
			switch (opcion) {
			case 1: {
				Test test = new Test();
				test.setId(1239);
				int error = gdb.persist(test);
				if (error != 0)
					System.out.println(gdb.getErrorMessage());
				else
					System.out.println("Insertado correctamente");
				break;
			}
			case 2: {

			}
			case 0:{
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + opcion);
			}
		}
	}
}