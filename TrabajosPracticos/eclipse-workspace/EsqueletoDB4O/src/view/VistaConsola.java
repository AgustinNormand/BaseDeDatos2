package view;

import java.util.ArrayList;
import java.util.Scanner;

import model.Cliente;
import model.Database;

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
				Cliente cliuno = new Cliente(0,"PepeUno",10);
				Cliente cliunoYMedio = new Cliente(1,"PepeUnoYMedio",10);
				Cliente clidos = new Cliente(50,"PepeDos",10);
				Cliente clitres = new Cliente(100,"PepeTres",10);
				Cliente clicuatro = new Cliente(101,"PepeCuatro",10);
				database.insert(cliuno);
				database.insert(cliunoYMedio);
				database.insert(clidos);
				database.insert(clitres);
				database.insert(clicuatro);
				break;
			}
			case 2: {
				ArrayList<Cliente> clientes = database.listar();
				for (Cliente cliente : clientes)
					System.out.println(cliente.toString());
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
