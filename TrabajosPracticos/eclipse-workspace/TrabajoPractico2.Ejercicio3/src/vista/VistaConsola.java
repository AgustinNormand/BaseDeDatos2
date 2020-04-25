package vista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import controlador.Gestor;
import modelo.Cliente;
import modelo.Detalle;
import modelo.Factura;
import modelo.Proveedor;
import modelo.Producto;
import modelo.Direccion;

public class VistaConsola {
	Gestor gdb = Gestor.getInstance();
	private Scanner scan = new Scanner(System.in);

	public VistaConsola() {
	}

	public void iniciar() {
		menuPrincipal();		
	}

	private void menuPrincipal() {
		int opcionMenuPrincipal = -1;
		while (opcionMenuPrincipal != 0) {
			System.out.println("1) Select");
			System.out.println("2) Insert");
			System.out.println("3) Update");
			System.out.println("4) Delete");
			System.out.println("5) Consultas");
			System.out.println("0) Salir");
			opcionMenuPrincipal = scan.nextInt();
			clearScreen();
			switch (opcionMenuPrincipal){
			case 0:
				gdb.close();
				break;
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
			case 5:
				menuConsultas();
				break;
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
			System.out.println("6) Direccion");
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
			case 6:
				menuSelectDireccion();
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
		Cliente cliente = gdb.selectFromClienteWhere(idCliente);

		if (cliente == null) 
			System.out.println("Cliente no encontrado");
		else
			System.out.println(cliente);

		returnMenuAnterior();
	}

	private void selectAllFromCliente() {
		List<Cliente> clientes = gdb.selectFromCliente();
		if (clientes.isEmpty())
			System.out.println("No hay Clientes en la base de datos");
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
		Factura factura = gdb.selectFromFacturaWhere(nroFactura);

		if (factura == null) 
			System.out.println("Factura no encontrada");
		else
			System.out.println(factura);

		returnMenuAnterior();
	}

	private void selectAllFromFactura(){
		List<Factura> facturas = gdb.selectFromFactura();
		if (facturas.isEmpty())
			System.out.println("No hay Facturas en la base de datos");
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
		Detalle detalle = gdb.selectFromDetalleWhere(nroDetalle,idDetalle);

		if (detalle == null) 
			System.out.println("Detalle no encontrado");
		else
			System.out.println(detalle);

		returnMenuAnterior();
	}

	private void selectAllFromDetalle() {
		List<Detalle> detalles = gdb.selectFromDetalle();
		if (detalles.isEmpty())
			System.out.println("No hay Detalles en la base de datos");
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
		Proveedor proveedor = gdb.selectFromProveedorWhere(idProveedor);

		if (proveedor == null) 
			System.out.println("Proveedor no encontrado");
		else
			System.out.println(proveedor);

		returnMenuAnterior();
	}

	private void selectAllFromProveedor() {
		List<Proveedor> proveedores = gdb.selectFromProveedor();
		if (proveedores.isEmpty())
			System.out.println("No hay Proveedores en la base de datos");
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
		Producto producto = gdb.selectFromProductoWhere(idProducto);

		if (producto == null) 
			System.out.println("Producto no encontrado");
		else
			System.out.println(producto);

		returnMenuAnterior();
	}

	private void selectAllFromProducto() {
		List<Producto> productos = gdb.selectFromProducto();
		if (productos.isEmpty())
			System.out.println("No hay Productos en la base de datos");
		else
			for (Producto producto : productos) 
				System.out.println(producto);
		returnMenuAnterior();	
	}
	
	private void menuSelectDireccion() {
		int opcionMenuSelectDireccion = -1;
		while (opcionMenuSelectDireccion != 0) {
			System.out.println("1) Select * From Direccion");
			System.out.println("2) Select * From Direccion Where ...");
			System.out.println("0) Volver");
			opcionMenuSelectDireccion = scan.nextInt();
			clearScreen();
			switch (opcionMenuSelectDireccion) {
			case 1:
				selectAllFromDireccion();
				break;
			case 2:
				selectDireccionWhere();
				break;
			}
		}
	}

	private void selectDireccionWhere() {
		System.out.println("Ingese el ID de la DIRECCION a buscar");
		int idDireccion = scan.nextInt();
		Direccion direccion = gdb.selectFromDireccionWhere(idDireccion);

		if (direccion == null) 
			System.out.println("Direccion no encontrada");
		else
			System.out.println(direccion);

		returnMenuAnterior();
	}

	private void selectAllFromDireccion() {
		List<Direccion> direcciones = gdb.selectFromDireccion();
		if (direcciones.isEmpty())
			System.out.println("No hay Direcciones en la base de datos");
		else
			for (Direccion direccion : direcciones) 
				System.out.println(direccion);
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
			System.out.println("6) Direccion");
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
			case 6:
				insertDireccion();
				break;
			}
		}
	}

	private void insertCliente() {
		Cliente cliente = new Cliente();
		System.out.println("Ingresar el ID del cliente a insertar");
		cliente.setId(scan.nextInt());
		System.out.println("Ingesar el NOMBRE del cliente a insertar");
		cliente.setNombre(scan.next());
		System.out.println("Ingresar el ID de la DIRECCION del cliente a insertar");
		cliente.setDireccion(gdb.selectFromDireccionWhere(scan.nextInt()));
		
		int errorCode = cliente.persist();
		switch(errorCode) {
		case 0:
			System.out.println("Cliente insertado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		
		returnMenuAnterior();
	}

	private void insertFactura() {
		Factura factura = new Factura();
		System.out.println("Ingrese el NRO de la factura a insertar");
		factura.setNro(scan.nextInt());
		System.out.println("Ingese el ID del cliente de la factura.");
		factura.setCliente(gdb.selectFromClienteWhere(scan.nextInt()));
		factura.setFecha(new Date());
		
		int errorCode = factura.persist();
		switch(errorCode) {
		case 0:
			System.out.println("Factura insertada correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}

	private void insertDetalle() {
		Detalle detalle = new Detalle();
		System.out.println("Ingresar el NRO del DETALLE");
		detalle.setFactura(gdb.selectFromFacturaWhere(scan.nextInt()));
		System.out.println("Ingresar el ID del DETALLE");
		detalle.setProducto(gdb.selectFromProductoWhere(scan.nextInt()));
		System.out.println("Ingresar la CANTIDAD del DETALLE");
		detalle.setCantidad(scan.nextInt());
		System.out.println("Ingresar el PRECIO del DETALLE");
		detalle.setPrecio(scan.nextDouble());
		
		int errorCode = detalle.persist();
		switch(errorCode) {
		case 0:
			System.out.println("DETALLE insertado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	
	private void insertProveedor() {
		Proveedor proveedor = new Proveedor();
		System.out.println("Ingresar el ID del PROVEEDOR a insertar");
		proveedor.setId(scan.nextInt());
		System.out.println("Ingresar el NOMBRE del PROVEEDOR a insertar");
		proveedor.setNombre(scan.next());
		int idProducto = -1;
		List<Producto> productosQueProvee = null;
		System.out.println("Ingresar el ID 0 para dejar de ingresar PRODUCTOS");
		while (idProducto != 0) {
			System.out.println("Ingresar ID de PRODUCTO que provee el PROVEEDOR");
			idProducto = scan.nextInt();
			if (idProducto != 0)
				productosQueProvee.add(gdb.selectFromProductoWhere(idProducto));
		}
		proveedor.setProductosQueProvee(productosQueProvee);
		
		int errorCode = proveedor.persist();
		switch(errorCode) {
		case 0:
			System.out.println("PROVEEDOR insertado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	private void insertProducto() {
		Producto producto = new Producto();
		System.out.println("Ingresar el ID del PRODUCTO");
		producto.setId(scan.nextInt());
		System.out.println("Ingresar la DESCRIPCION del PRODUCTO");
		producto.setDescr(scan.next());
		System.out.println("Ingresar el STOCK del PRODUCTO");
		producto.setStock(scan.nextInt());
		System.out.println("Ingresar el PRECIO BASE del PRODUCTO");
		producto.setPrecioBase(scan.nextDouble());
		System.out.println("Ingresar el PRECIO COSTO del PRODUCTO");
		producto.setPrecioCosto(scan.nextDouble());
		int errorCode = producto.persist();
		switch (errorCode) {
		case 0:
			System.out.println("PRODUCTO insertado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	
	private void insertDireccion() {
		Direccion direccion = new Direccion();
		System.out.println("Ingresar el ID del DIRECCION");
		direccion.setId(scan.nextInt());
		System.out.println("Ingresar la CALLE de la DIRECCION");
		direccion.setCalle(scan.next());
		System.out.println("Ingresar el NRO de la DIRECCION");
		direccion.setNro(scan.nextInt());
		System.out.println("Ingresar la LOCALIDAD de la DIRECCION");
		direccion.setLocalidad(scan.next());
		
		int errorCode = direccion.persist();
		switch (errorCode) {
		case 0:
			System.out.println("DIRECCION insertada correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
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
			System.out.println("3) Detalle");
			System.out.println("4) Proveedor");
			System.out.println("5) Producto");
			System.out.println("6) Direccion");
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
			case 6:
				deleteDireccion();
				break;
					
			}
		}
	}

	private void deleteCliente(){
		System.out.println("Ingrese el ID del cliente a eliminar");
		int idCliente = scan.nextInt();
		int errorCode = gdb.deleteFromClienteWhere(gdb.selectFromClienteWhere(idCliente));
		switch(errorCode) {
		case 0:
			System.out.println("Cliente eliminado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}

		returnMenuAnterior();
	}

	private void deleteFactura(){
		System.out.println("Ingrese el NRO de la factura a eliminar");
		int nroFactura = scan.nextInt();
		int errorCode = gdb.deleteFromFacturaWhere(gdb.selectFromFacturaWhere(nroFactura));
		switch(errorCode) {
		case 0:
			System.out.println("Factura eliminada correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	
	private void deleteDetalle() {
		System.out.println("Ingrese el NRO del DETALLE a eliminar");
		int nroDetalle = scan.nextInt();
		System.out.println("Ingrese el ID del DETALLE a eliminar");
		int idDetalle = scan.nextInt();
		int errorCode = gdb.deleteFromDetalleWhere(gdb.selectFromDetalleWhere(nroDetalle,idDetalle));
		switch(errorCode) {
		case 0:
			System.out.println("DETALLE eliminado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	
	private void deleteProveedor() {
		System.out.println("Ingrese el ID del PROVEEDOR a eliminar");
		int idProveedor = scan.nextInt();
		int errorCode = gdb.deleteFromProveedorWhere(gdb.selectFromProveedorWhere(idProveedor));
		switch(errorCode) {
		case 0:
			System.out.println("Proveedor eliminado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	
	private void deleteProducto() {
		System.out.println("Ingrese el ID del PRODUCTO a eliminar");
		int idProducto = scan.nextInt();
		int errorCode = gdb.deleteFromProductoWhere(gdb.selectFromProductoWhere(idProducto));
		switch(errorCode) {
		case 0:
			System.out.println("Producto eliminado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();	
	}
	
	private void deleteDireccion() {
		System.out.println("Ingrese el ID de la DIRECCION a eliminar");
		int idDireccion = scan.nextInt();
		int errorCode = gdb.deleteFromDireccionWhere(gdb.selectFromDireccionWhere(idDireccion));
		switch(errorCode) {
		case 0:
			System.out.println("Direccion eliminada correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
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
			System.out.println("6) Direccion");
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
			case 6:
				updateDireccion();
				break;
			}
		}
	}

	private void updateCliente() {
		/*
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
		*/
		returnMenuAnterior();
	}

	private void updateFactura() {
		
		System.out.println("Ingrese el NUMERO de la factura a modificar");
		int nroFacturaAModificar = scan.nextInt();
		System.out.println("Ingrese el nuevo ID");
		int idClienteNuevo = scan.nextInt();
		System.out.println("Ingrese el nuevo IMPORTE");
		double importeNuevo = scan.nextDouble();
		int errorCode = gdb.updateFacturaSet(nroFacturaAModificar, idClienteNuevo, importeNuevo);
		switch(errorCode) {
		case 0:
			System.out.println("Factura modificada correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	
	private void updateDetalle() {
		System.out.println("Ingrese el NUMERO del detalle a modificar");
		int nroDetalleAModificar = scan.nextInt();
		System.out.println("Ingrese el ID del detalle a modificar");
		int idDetalleAModificar = scan.nextInt();
		System.out.println("Ingrese la nueva CANTIDAD");
		int nuevaCantidad = scan.nextInt();
		System.out.println("Ingrese el nuevo IMPORTE");
		double nuevoImporte = scan.nextDouble();
		int errorCode = gdb.updateDetalleSet(nroDetalleAModificar, idDetalleAModificar, nuevaCantidad, nuevoImporte);
		switch(errorCode) {
		case 0:
			System.out.println("Detalle modificado correctamente");
			break;
		case 1:
			System.out.println(Gestor.getErrorMessage());
			break;
		}
		returnMenuAnterior();
	}
	
	private void updateProveedor() {
		returnMenuAnterior();
	}
	
	private void updateProducto() {
		returnMenuAnterior();
	}
	private void updateDireccion() {
		returnMenuAnterior();
	}
	//
	
	private void menuConsultas() {
		int opcionMenuConsultas = -1;
		while (opcionMenuConsultas != 0) {
			System.out.println("1) Factura");
			System.out.println("2) Cliente");
			System.out.println("3) Producto");
			System.out.println("4) Proveedor");
			System.out.println("0) Volver");
			opcionMenuConsultas = scan.nextInt();
			clearScreen();
			switch(opcionMenuConsultas) {
			case 1:
				menuConsultasFactura();
				break;
			case 2:
				menuConsultasCliente();
				break;
			case 3:
				menuConsultasProducto();
				break;
			case 4:
				menuConsultasProveedor();
				break;
			}
		}
	}
	
	private void menuConsultasProveedor() {
		int opcionMenuConsultasProveedor = -1;
		while (opcionMenuConsultasProveedor != 0) {
			System.out.println("1) Obtener todos los productos que provee un proveedor");
			System.out.println("0) Volver");
			opcionMenuConsultasProveedor = scan.nextInt();
			clearScreen();
			switch(opcionMenuConsultasProveedor) {
			case 1:
				consultarProductosProveedor();
				break;
			}
		}
	}

	private void consultarProductosProveedor() {
		System.out.println("Ingrese el ID del proveedor");
		int idProveedor = scan.nextInt();
		Proveedor proveedor = gdb.selectFromProveedorWhere(idProveedor);
		if (proveedor != null) {
			List<Producto> productos = proveedor.getProductosQueProvee();
			if (productos.isEmpty())
				System.out.println("El proveedor no provee ningun producto");
			else
				for (Producto producto : productos) 
					System.out.println(producto.toString());
		} else
			System.out.println("No existe un proveedor con dicho ID");
		returnMenuAnterior();		
	}

	private void menuConsultasProducto() {
		int opcionMenuConsultasProducto = -1;
		while (opcionMenuConsultasProducto != 0) {
			System.out.println("1) Obtener todos los detalles en los que aparece un producto");
			System.out.println("2) Obtener proveedores de un producto");
			System.out.println("0) Volver");
			opcionMenuConsultasProducto = scan.nextInt();
			clearScreen();
			switch(opcionMenuConsultasProducto) {
			case 1:
				consultarDetallesProducto();
				break;
			case 2:
				consultarProveedoresProducto();
				break;
			}
		}
	}

	private void consultarProveedoresProducto() {
		System.out.println("Ingrese el ID del producto");
		int idProducto = scan.nextInt();
		Producto producto = gdb.selectFromProductoWhere(idProducto);
		if (producto != null) {
			List<Proveedor> proveedores = producto.getProveedores();
			if (proveedores.isEmpty())
				System.out.println("El producto no lo provee ningun proveedor");
			else
				for (Proveedor proveedor : proveedores) 
					System.out.println(proveedor.toString());
		} else
			System.out.println("No existe un producto con dicho ID");
		returnMenuAnterior();		
	}

	private void consultarDetallesProducto() {
		System.out.println("Ingrese el ID del producto");
		int idProducto = scan.nextInt();
		Producto producto = gdb.selectFromProductoWhere(idProducto);
		if (producto != null) {
			List<Detalle> detalles = producto.getDetalles();
			if (detalles.isEmpty())
				System.out.println("El producto no aparece en ningun detalle");
			else
				for (Detalle detalle : detalles) 
					System.out.println(detalle.toString());
		} else
			System.out.println("No existe un producto con dicho ID");
		returnMenuAnterior();
	}

	private void menuConsultasCliente() {
		int opcionMenuConsultasCliente = -1;
		while (opcionMenuConsultasCliente != 0) {
			System.out.println("1) Obtener facturas de un cliente");
			System.out.println("0) Volver");
			opcionMenuConsultasCliente = scan.nextInt();
			clearScreen();
			switch(opcionMenuConsultasCliente) {
			case 1:
				consultarFacturasCliente();
				break;
			}
		}		
	}

	private void consultarFacturasCliente() {
		System.out.println("Ingrese el ID del cliente");
		int idCliente = scan.nextInt();
		Cliente cliente = gdb.selectFromClienteWhere(idCliente);
		if (cliente != null) {
			List<Factura> facturas = cliente.getFacturas();
			if(facturas.isEmpty())
				System.out.println("El cliente no contiene facturas");
			else
				for (Factura factura : facturas) {
					System.out.println(factura.toString());
				}
		} else
			System.out.println("No existe un cliente con dicho ID");
		returnMenuAnterior();
	}

	private void menuConsultasFactura() {
		int opcionMenuConsultasFactura = -1;
		while (opcionMenuConsultasFactura != 0) {
			System.out.println("1) Obtener detalles de una factura");
			System.out.println("0) Volver");
			opcionMenuConsultasFactura = scan.nextInt();
			clearScreen();
			switch(opcionMenuConsultasFactura) {
			case 1:
				consultarDetallesFactura();
				break;
			}
		}
	}

	private void consultarDetallesFactura() {
		System.out.println("Ingrese el NRO de la factura");
		int nroFactura = scan.nextInt();
		Factura factura = gdb.selectFromFacturaWhere(nroFactura);
		if (factura != null) {
		List<Detalle> detalles = factura.getDetalles();
		if (detalles.isEmpty())
			System.out.println("La factura no contiene detalles");
		else
			for (Detalle detalle : detalles)
				System.out.println(detalle.toString());
		} else
			System.out.println("No existe una factura con dicho NRO");
		returnMenuAnterior();
	}

	public void returnMenuAnterior() {
		System.out.println("Persione una tecla para volver al menu anterior.");
		try {
			System.in.read();
		} catch (IOException e) {
		}
		clearScreen();

	}
	public static void clearScreen() {  
		System.out.print("\033[H\033[2J");  
		System.out.flush();  
	}
}
