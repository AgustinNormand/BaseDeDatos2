package view;

import java.util.Scanner;

import com.agustin.prueba.ConnectionManager;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class VistaConsola {

	private Scanner scan = new Scanner(System.in);

	ConnectionManager cm = ConnectionManager.getInstance();

	private RedisCommands<String, String> conn = cm.getSyncConnection();

	public VistaConsola() {
		System.out.println("Consola de Esqueleto.REDIS");
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
				break;
			}
			case 2: {
				break;
			}
			case 0:{
				cm.close();
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + opcion);
			}
		}
	}
}
