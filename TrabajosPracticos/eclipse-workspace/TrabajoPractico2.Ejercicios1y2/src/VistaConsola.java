import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class VistaConsola {
	Gestor gdb = new Gestor();
	private Scanner scan = new Scanner(System.in);
	
	public VistaConsola() {
	}
	
	public void Iniciar() {
		menuPrincipal();		
	}
	
	private void menuPrincipal() {
		int opcionMenuPrincipal = -1;
		while (opcionMenuPrincipal != 0) {
			System.out.println("1) Select");
			System.out.println("2) Insert");
			System.out.println("3) Update");
			System.out.println("4) Delete");
			System.out.println("9) Drop Database");
			System.out.println("0) Salir");
			opcionMenuPrincipal = scan.nextInt();
			clearScreen();
			switch (opcionMenuPrincipal){
			case 1:
				menuSelect();
				break;
			case 2:
				menuInsert();
				break;
			case 3:
				menuUpdate();
				break;
			case 4:
				menuDelete();
				break;
			case 9:
				dropDatabase();
			}
		}
	}
	
	// SELECT
	
	private void menuSelect() {
		int opcionMenuSelect = -1;
		while (opcionMenuSelect != 0) {
			System.out.println("1) Cliente");
			System.out.println("2) Factura");
			System.out.println("0) Volver");
			opcionMenuSelect = scan.nextInt();
			clearScreen();
			switch (opcionMenuSelect) {
			case 1:
				menuSelectCliente();
				break;
			case 2:
				menuSelectFactura();
				break;
			}
		}
	}
	private void menuSelectCliente() {
		int opcionMenuSelectCliente = -1;
		while (opcionMenuSelectCliente != 0) {
			System.out.println("1) Select * From Cliente");
			System.out.println("2) Select * From Cliente Where ...");
			System.out.println("0) Volver");
			opcionMenuSelectCliente = scan.nextInt();
			clearScreen();
			switch (opcionMenuSelectCliente) {
			case 1:
				selectAllClientes();
				break;
			case 2:
				selectCliente();
				break;
			}
		}
	}

	public void selectCliente() {
		System.out.println("Ingese el ID del cliente a buscar");
		int idCliente = scan.nextInt();
		Cliente cliente = gdb.selectCliente(idCliente);
		
		if (cliente == null) 
			System.out.println("Cliente no encontrado");
		else
			System.out.println(cliente);
		
		returnMenuAnterior();
	}
	
	public void selectAllClientes() {
		ArrayList<Cliente> clientes = gdb.selectAllClientes();
		if (clientes.isEmpty())
			System.out.println("No hay clientes en la base de datos");
		else
			for (Cliente cliente : clientes) 
				System.out.println(cliente);
		returnMenuAnterior();
	}
	
	private void menuSelectFactura() {
		int opcionMenuSelectFactura = -1;
		while (opcionMenuSelectFactura != 0) {
			System.out.println("1) Select * From Factura");
			System.out.println("2) Select * From Factura Where ...");
			System.out.println("0) Volver");
			opcionMenuSelectFactura = scan.nextInt();
			clearScreen();
			switch (opcionMenuSelectFactura) {
			case 1:
				selectAllFacturas();
				break;
			case 2:
				selectFactura();
				break;
			}
		}
	}
	
	public void selectFactura() {
		System.out.println("Ingese el NRO de la factura a buscar");
		int nroFactura = scan.nextInt();
		Factura factura = gdb.selectFactura(nroFactura);
		
		if (factura == null) 
			System.out.println("Factura no encontrada");
		else
			System.out.println(factura);
		
		returnMenuAnterior();
	}
	
	public void selectAllFacturas(){
		ArrayList<Factura> facturas = gdb.selectAllFacturas();
		if (facturas.isEmpty())
			System.out.println("No hay facturas en la base de datos");
		else
			for (Factura factura : facturas) 
				System.out.println(factura);
		returnMenuAnterior();
	}
	//
	
	// INSERT
	
	private void menuInsert() {
		int opcionMenuInsert = -1;
		while (opcionMenuInsert != 0) {
			System.out.println("1) Cliente");
			System.out.println("2) Factura");
			System.out.println("0) Volver");
			opcionMenuInsert = scan.nextInt();
			clearScreen();
			switch (opcionMenuInsert) {
			case 1:
				insertCliente();
				break;
			case 2:
				insertFactura();
				break;
			}
		}
	}
	
	private void insertCliente() {
		System.out.println("Ingrese el ID del cliente a insertar");
		int idCliente = scan.nextInt();
		System.out.println("Ingese la DESCR del cliente a insertar");
		String descr = scan.next();
		int errorCode = gdb.insertarCliente(idCliente,descr);
		switch(errorCode) {
		case 0:
			System.out.println("Cliente insertado correctamente");
			break;
		case 1:
			System.out.println("Ya existe un cliente con el ID ingresado");
			break;
		}
		returnMenuAnterior();
	}
	
	private void insertFactura() {
		System.out.println("Ingrese el NRO de la factura a insertar");
		int nroFactura = scan.nextInt();
		System.out.println("Ingese el ID del cliente de la factura.");
		int idCliente = scan.nextInt();
		System.out.println("Ingese el IMPORTE de la factura.");
		double importe = scan.nextDouble();
		int errorCode = gdb.insertarFactura(nroFactura,idCliente,importe);
		
		switch(errorCode) {
		case 0:
			System.out.println("Factura insertada correctamente");
			break;
		case 1:
			System.out.println("El ID ingresado no pertenece a un cliente en la base de datos");
			break;
		case 2:
			System.out.println("Ya existe una factura con el NRO ingresado");
			break;
		}
		
		returnMenuAnterior();
	}
	
	//
	
	// DELETE
	
	private void menuDelete() {
		int opcionmenuDelete = -1;
		while (opcionmenuDelete != 0) {
			System.out.println("1) Cliente");
			System.out.println("2) Factura");
			System.out.println("0) Volver");
			opcionmenuDelete = scan.nextInt();
			clearScreen();
			switch (opcionmenuDelete) {
			case 1:
				deleteCliente();
				break;
			case 2:
				deleteFactura();
				break;
			}
		}
	}
	
	private void deleteCliente(){
		System.out.println("Ingrese el ID del cliente a eliminar");
		int idCliente = scan.nextInt();
		int errorCode = gdb.deleteCliente(idCliente);
		switch(errorCode) {
		case 0:
			System.out.println("Cliente eliminado correctamente");
			break;
		case 1:
			System.out.println("El ID ingresado no pertenece a un cliente en la base de datos");
			break;
		}
		
		returnMenuAnterior();
	}
	
	private void deleteFactura(){
		System.out.println("Ingrese el NRO de la factura a eliminar");
		int nroFactura = scan.nextInt();
		int errorCode = gdb.deleteFactura(nroFactura);
		switch(errorCode) {
		case 0:
			System.out.println("Factura eliminada correctamente");
			break;
		case 1:
			System.out.println("El NRO ingresado no pertenece a una factura en la base de datos");
			break;
		}
		
		returnMenuAnterior();
	}
	
	// UPDATE
	
	private void menuUpdate() {
		int opcionmenuUpdate = -1;
		while (opcionmenuUpdate != 0) {
			System.out.println("1) Cliente");
			System.out.println("2) Factura");
			System.out.println("0) Volver");
			opcionmenuUpdate = scan.nextInt();
			clearScreen();
			switch(opcionmenuUpdate) {
			case 1:
				updateCliente();
				break;
			case 2:
				updateFactura();
				break;
			}
		}
	}
	
	private void updateCliente() {
		System.out.println("Ingrese el ID del cliente a modificar");
		int idClienteAModificar = scan.nextInt();
		System.out.println("Ingrese el nuevo ID");
		int idClienteNuevo = scan.nextInt();
		System.out.println("Ingrese la nueva DESCRIPCION");
		String descripcion = scan.next();
		int errorCode = gdb.updateCliente(idClienteAModificar, idClienteNuevo, descripcion);
		switch (errorCode) {
		case 0:
			System.out.println("Cliente modificado correctamente.");
			break;
		case 1:
			System.out.println("El ID ingresado no pertenece a un cliente en la base de datos");
			break;
		case 2:
			System.out.println("El nuevo ID ya existe en la base de datos");
			break;
		}
		returnMenuAnterior();
	}
	
	private void updateFactura() {
		System.out.println("Ingrese el NUMERO de la factura a modificar");
		int nroFacturaAModificar = scan.nextInt();
		System.out.println("Ingrese el nuevo NUMERO");
		int nroFacturaNuevo = scan.nextInt();
		System.out.println("Ingrese el nuevo ID");
		int idClienteNuevo = scan.nextInt();
		System.out.println("Ingrese el nuevo IMPORTE");
		double importeNuevo = scan.nextDouble();
		int errorCode = gdb.updateFactura(nroFacturaAModificar, nroFacturaNuevo, idClienteNuevo, importeNuevo);
		switch(errorCode) {
		case 0:
			System.out.println("Factura modificada correctamente");
			break;
		case 1:
			System.out.println("El NUMERO ingresado no pertenece a una factura en la base de datos");
			break;
		case 2:
			System.out.println("El nuevo ID no pertenece a un cliente en la base de datos");
			break;
		case 3:
			System.out.println("El nuevo NUMERO ya existe en la base de datos");
			break;
		}
		returnMenuAnterior();
	}
	
	//
	
	public void dropDatabase() {
		clearScreen();
		int errorCode = gdb.dropDatabase();
		switch(errorCode) {
		case 0:
			System.out.println("Base de datos eliminada correctamente");
			break;
		case 1:
			System.out.println("No existe una base de datos creada");
			break;
		}
		returnMenuAnterior();
	}
	public void returnMenuAnterior() {
		System.out.println("Persione una tecla para volver al menu anterior.");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clearScreen();
		
	}
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
       }
}
