package modelo;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VistaConsola {
	Gestor gdb = Gestor.getInstance();
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
			System.out.println("3) Detalle");
			System.out.println("4) Proveedor");
			System.out.println("5) Producto");
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
			case 3:
				menuSelectDetalle();
				break;
			case 4:
				menuSelectProveedor();
				break;
			case 5:
				menuSelectProducto();
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
				selectAllFromCliente();
				break;
			case 2:
				selectClienteWhere();
				break;
			}
		}
	}

	private void selectClienteWhere() {
		System.out.println("Ingese el ID del cliente a buscar");
		int idCliente = scan.nextInt();
		Cliente cliente = gdb.selectClienteWhere(idCliente);

		if (cliente == null) 
			System.out.println("Cliente no encontrado");
		else
			System.out.println(cliente);

		returnMenuAnterior();
	}

	private void selectAllFromCliente() {
		List<Cliente> clientes = gdb.selectAllFromCliente();
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
				selectAllFromFactura();
				break;
			case 2:
				selectFacturaWhere();
				break;
			}
		}
	}

	private void selectFacturaWhere() {
		System.out.println("Ingese el NRO de la factura a buscar");
		int nroFactura = scan.nextInt();
		Factura factura = gdb.selectFacturaWhere(nroFactura);

		if (factura == null) 
			System.out.println("Factura no encontrada");
		else
			System.out.println(factura);

		returnMenuAnterior();
	}

	private void selectAllFromFactura(){
		List<Factura> facturas = gdb.selectAllFromFactura();
		if (facturas.isEmpty())
			System.out.println("No hay facturas en la base de datos");
		else
			for (Factura factura : facturas) 
				System.out.println(factura);
		returnMenuAnterior();
	}

	private void menuSelectDetalle() {
		int opcionMenuSelectDetalle = -1;
		while (opcionMenuSelectDetalle != 0) {
			System.out.println("1) Select * From Detalle");
			System.out.println("2) Select * From Detalle Where ...");
			System.out.println("0) Volver");
			opcionMenuSelectDetalle = scan.nextInt();
			clearScreen();
			switch (opcionMenuSelectDetalle) {
			case 1:
				selectAllFromDetalle();
				break;
			case 2:
				selectDetalleWhere();
				break;
			}
		}
	}

	private void selectDetalleWhere() {
		System.out.println("Ingese el NRO del DETALLE a buscar");
		int nroDetalle = scan.nextInt();
		System.out.println("Ingese el ID del DETALLE a buscar");
		int idDetalle = scan.nextInt();
		Detalle detalle = gdb.selectDetalleWhere(nroDetalle,idDetalle);

		if (detalle == null) 
			System.out.println("Detalle no encontrado");
		else
			System.out.println(detalle);

		returnMenuAnterior();
	}

	private void selectAllFromDetalle() {
		List<Detalle> detalles = gdb.selectAllFromDetalle();
		if (detalles.isEmpty())
			System.out.println("No hay detalles en la base de datos");
		else
			for (Detalle detalle : detalles) 
				System.out.println(detalle);
		returnMenuAnterior();	
	}

	private void menuSelectProveedor() {
		int opcionMenuSelectProveedor = -1;
		while (opcionMenuSelectProveedor != 0) {
			System.out.println("1) Select * From Proveedor");
			System.out.println("2) Select * From Proveedor Where ...");
			System.out.println("0) Volver");
			opcionMenuSelectProveedor = scan.nextInt();
			clearScreen();
			switch (opcionMenuSelectProveedor) {
			case 1:
				selectAllFromProveedor();
				break;
			case 2:
				selectProveedorWhere();
				break;
			}
		}		
	}

	private void selectProveedorWhere() {
		System.out.println("Ingese el ID del PROVEEDOR a buscar");
		int idProveedor = scan.nextInt();
		Proveedor proveedor = gdb.selectProveedorWhere(idProveedor);

		if (proveedor == null) 
			System.out.println("Proveedor no encontrado");
		else
			System.out.println(proveedor);

		returnMenuAnterior();
	}

	private void selectAllFromProveedor() {
		List<Proveedor> proveedores = gdb.selectAllFromProveedor();
		if (proveedores.isEmpty())
			System.out.println("No hay proveedores en la base de datos");
		else
			for (Proveedor proveedor : proveedores) 
				System.out.println(proveedor);
		returnMenuAnterior();	
	}

	private void menuSelectProducto() {
		int opcionMenuSelectProducto = -1;
		while (opcionMenuSelectProducto != 0) {
			System.out.println("1) Select * From Producto");
			System.out.println("2) Select * From Producto Where ...");
			System.out.println("0) Volver");
			opcionMenuSelectProducto = scan.nextInt();
			clearScreen();
			switch (opcionMenuSelectProducto) {
			case 1:
				selectAllFromProducto();
				break;
			case 2:
				selectProductoWhere();
				break;
			}
		}
	}

	private void selectProductoWhere() {
		System.out.println("Ingese el ID del PRODUCTO a buscar");
		int idProducto = scan.nextInt();
		Producto producto = gdb.selectProductoWhere(idProducto);

		if (producto == null) 
			System.out.println("Producto no encontrado");
		else
			System.out.println(producto);

		returnMenuAnterior();
	}

	private void selectAllFromProducto() {
		List<Producto> productos = gdb.selectAllFromProducto();
		if (productos.isEmpty())
			System.out.println("No hay productos en la base de datos");
		else
			for (Producto producto : productos) 
				System.out.println(producto);
		returnMenuAnterior();	
	}

	//

	// INSERT

	private void menuInsert() {
		int opcionMenuInsert = -1;
		while (opcionMenuInsert != 0) {
			System.out.println("1) Cliente");
			System.out.println("2) Factura");
			System.out.println("3) Detalle");
			System.out.println("4) Proveedor");
			System.out.println("5) Producto");
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
			case 3:
				insertDetalle();
				break;
			case 4:
				insertProveedor();
				break;
			case 5:
				insertProducto();
				break;
			}
		}
	}

	private void insertCliente() {
		System.out.println("Ingrese el ID del cliente a insertar");
		int idCliente = scan.nextInt();
		System.out.println("Ingese el NOMBRE del cliente a insertar");
		String nombre = scan.next();
		Cliente cliente = new Cliente(idCliente,nombre);
		int errorCode = cliente.persist();
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
		byte estado = scan.nextByte();
		System.out.println("Ingese el ESTADO de la factura.");
		double importe = scan.nextDouble();
		Gestor gestor = Gestor.getInstance();
		Cliente cliente = gestor.selectClienteWhere(idCliente);
		Factura factura = new Factura(nroFactura,importe,estado,new Date(),cliente);
		int errorCode = factura.persist();

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

	private void insertDetalle() {
		Gestor gestor = Gestor.getInstance();
		System.out.println("Ingresar el NRO del DETALLE");
		Factura factura = gestor.selectFacturaWhere(scan.nextInt());
		System.out.println("Ingresar el ID del DETALLE");
		Producto producto = gestor.selectProductoWhere(scan.nextInt());
		System.out.println("Ingresar la CANTIDAD del DETALLE");
		int cantidad = scan.nextInt();
		System.out.println("Ingresar el PRECIO del DETALLE");
		double precio = scan.nextDouble();
		Detalle detalle = new Detalle(factura,producto,cantidad,precio);
		int errorCode = detalle.persist();
		
		switch(errorCode) {
		case 0:
			System.out.println("DETALLE insertado correctamente");
			break;
		case 1:
			System.out.println("El ID o NRO ingresados no pertenecen a un PRODUCTO o FACTURA en la base de datos");
			break;
		case 2:
			System.out.println("Ya existe un DETALLE con el NRO y ID ingresados");
			break;
		}

		returnMenuAnterior();
	}
	private void insertProveedor() {
		Gestor gestor = Gestor.getInstance();
		System.out.println("Ingresar el ID del PROVEEDOR a insertar");
		int idProveedor = scan.nextInt();
		System.out.println("Ingresar el NOMBRE del PROVEEDOR a insertar");
		String nombre = scan.next();
		int idProducto = -1;
		List<Producto> productosQueProvee = null;
		System.out.println("Ingresar el ID 0 para dejar de ingresar PRODUCTOS");
		while (idProducto != 0) {
			System.out.println("Ingresar ID de PRODUCTO que provee el PROVEEDOR");
			idProducto = scan.nextInt();
			if (idProducto != 0)
				productosQueProvee.add(gestor.selectProductoWhere(idProducto));
		}
		Proveedor proveedor = new Proveedor(idProveedor,nombre,productosQueProvee);
		int errorCode = proveedor.persist();
		switch(errorCode) {
		case 0:
			System.out.println("PROVEEDOR insertado correctamente");
			break;
		case 1:
			System.out.println("Ya existe un PROVEEDOR con el ID ingresado");
			break;
		}
		returnMenuAnterior();
	}
	private void insertProducto() {

	}

	//

	// DELETE

	private void menuDelete() {
		int opcionmenuDelete = -1;
		while (opcionmenuDelete != 0) {
			System.out.println("1) Cliente");
			System.out.println("2) Factura");
			System.out.println("3) Detalle");
			System.out.println("4) Proveedor");
			System.out.println("5) Producto");
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
			case 3:
				deleteDetalle();
				break;
			case 4:
				deleteProveedor();
				break;
			case 5:
				deleteProducto();
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
			System.out.println("3) Detalle");
			System.out.println("4) Proveedor");
			System.out.println("5) Producto");
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
			case 3:
				updateDetalle();
				break;
			case 4:
				updateProveedor();
				break;
			case 5:
				updateProducto();
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
