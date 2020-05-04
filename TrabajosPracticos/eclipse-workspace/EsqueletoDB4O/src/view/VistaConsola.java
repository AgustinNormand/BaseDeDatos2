package view;

import java.util.Scanner;

import model.Cliente;
import model.Database;
import model.Factura;

public class VistaConsola {

	private Scanner scan = new Scanner(System.in);

	Database database = new Database();

	public VistaConsola() {
		System.out.println("Consola de Esqueleto.DB4O");
		database.initEmbebidoSinConfiguracion();
		menuPrincipal();

		// Client server //Si es cliente servidor, descomento esto, y descomento lineas en Database.
		//database.initClient();

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
				break;
			}
			case 2: {
				break;

			}
			case 0:{
				database.close();
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + opcion);
			}
		}
	}

}
