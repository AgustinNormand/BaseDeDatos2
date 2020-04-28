import java.util.ArrayList;

public class Gestor {
	private Database db = new Database();

	public int insertarCliente(int idCliente,String descr) {
		int errorCode = 0;
		if (!clientExists(idCliente)) {
			Cliente cliente = new Cliente(idCliente,descr);
			db.insertarCliente(cliente);
		} else
			errorCode = 1;
		return errorCode;
	}

	public Cliente selectCliente(int idCliente) {
		boolean found = false;
		int index = 0;
		Cliente cliente = null;
		ArrayList<Cliente> clientes = db.selectAllClientes();
		while (index < clientes.size() && !found) {
			cliente = clientes.get(index++);
			if (cliente.getId() == idCliente) 
				found = true;
		}
		if (!found) 
			cliente = null;
		return cliente;
	}


	public ArrayList<Cliente> selectAllClientes(){
		return db.selectAllClientes();
	}

	public int insertarFactura(int nro, int idCliente, double importe) {
		int errorCode = 0;
		if (clientExists(idCliente)) {
			if (!billExists(nro)){
				Factura factura = new Factura(nro,idCliente,importe);
				db.insertarFactura(factura);
			} else
				errorCode = 2;
		} else
			errorCode = 1;
		return errorCode;
	}

	private boolean clientExists(int idCliente) {
		int index = 0;
		boolean found = false;
		ArrayList<Cliente> clientes = db.selectAllClientes();
		while (index < clientes.size() && !found) {
			Cliente cliente = clientes.get(index++);
			if (cliente.getId() == idCliente) 
				found = true;
		}
		return found;
	}

	private boolean billExists(int nro) {
		int index = 0;
		boolean found = false;
		ArrayList<Factura> facturas = db.selectAllFacturas();
		while(index < facturas.size() && !found) {
			Factura factura = facturas.get(index++);
			if (factura.getNro() == nro)
				found = true;
		}
		return found;
	}

	public Factura selectFactura(int nroFactura) {
		/*
		boolean found = false;
		int index = 0;
		Factura factura = null;
		ArrayList<Factura> facturas = db.selectAllFacturas();
		while (index < facturas.size() && !found) {
			factura = facturas.get(index++);
			if (factura.getNro() == nroFactura) 
				found = true;
		}
		if (!found) 
			factura = null;
		return factura;
		*/
		return db.selectFromFacturaWhere(nroFactura);
	}

	public ArrayList<Factura> selectAllFacturas(){
		return db.selectAllFacturas();
	}

	public int deleteFactura(int nroFactura) {
		int errorCode = 0;
		Factura factura = this.selectFactura(nroFactura);
		if (factura != null)
			db.deleteFactura(factura);
		else
			errorCode = 1;
		return errorCode;
	}

	public int deleteCliente(int idCliente) {
		int errorCode = 0;
		Cliente cliente = this.selectCliente(idCliente);
		if (cliente != null) {
			ArrayList<Factura> facturas = db.selectAllFacturas();
			for (Factura factura : facturas) 
				if (factura.getId() == idCliente)
					this.deleteFactura(factura.getNro());
			db.deleteCliente(cliente);
		} else
			errorCode = 1;
		return errorCode;
	}

	public int updateFactura(int nroFacturaAModificar, int nuevoNroFactura, int nuevoIdCliente, double nuevoImporte) {
		int errorCode = 0;
		Factura factura = this.selectFactura(nroFacturaAModificar);
		if (factura != null) {
			if (clientExists(nuevoIdCliente)) {
				if (nroFacturaAModificar != nuevoNroFactura) {
					if (!billExists(nuevoNroFactura)) {
						db.updateFactura(factura, new Factura(nuevoNroFactura,nuevoIdCliente,nuevoImporte));
					} else
						errorCode = 3;
				} else
					db.updateFactura(factura, new Factura(nroFacturaAModificar,nuevoIdCliente,nuevoImporte));
			} else 
				errorCode = 2;
		} else
			errorCode = 1;
		return errorCode;
	}

	public int updateCliente(int idClienteAModificar, int nuevoIdCliente, String nuevaDescripcion) {
		int errorCode = 0;
		Cliente clienteAModificar = this.selectCliente(idClienteAModificar);
		if (clienteAModificar != null) {
			if (idClienteAModificar != nuevoIdCliente) {//Quiere cambiar el id
				if (!clientExists(nuevoIdCliente)) { //Verifico que no exista el nuevo id
					db.updateCliente(clienteAModificar, new Cliente(nuevoIdCliente,nuevaDescripcion)); //Ya modifico el cliente, antes de modificar las facturas
					ArrayList<Factura> facturas = this.selectAllFacturas();
					for (Factura factura : facturas) //Modifico en cascada todas las facturas
						if (factura.getId() == idClienteAModificar)
							this.updateFactura(factura.getNro(), factura.getNro(), nuevoIdCliente, factura.getImporte());
				} else errorCode = 2; //Ya existe el nuevo ID que se quiere ingresar
			} else { //Si no quiere cambiar el id
				db.updateCliente(clienteAModificar, new Cliente(idClienteAModificar,nuevaDescripcion)); //Reemplazo la old.descripcion con la nueva.
			}
		} else errorCode = 1; //No existe el cliente a modificar
		return errorCode;
	}
	
	public int dropDatabase() {
		return db.drop();
	}

	public ArrayList<Cliente> selectClientesInRange(int idDesde, int idHasta) {
		ArrayList<Cliente> clientesReturn = new ArrayList<>();
		ArrayList<Cliente> clientes = db.selectAllClientes();
		for (Cliente cliente : clientes) {
			int idCliente = cliente.getId();
			if (idCliente >= idDesde && idCliente <= idHasta) 
				clientesReturn.add(new Cliente(cliente.getId(),cliente.getDescr()));
		}
		return clientesReturn;
	}

	public ArrayList<Factura> selectFacturasInRange(int nroDesde, int nroHasta) {
		ArrayList<Factura> facturasReturn = new ArrayList<>();
		ArrayList<Factura> facturas = db.selectAllFacturas();
		for (Factura factura : facturas) {
			int nroFactura = factura.getNro();
			if (nroFactura >= nroDesde && nroFactura <= nroHasta) 
				facturasReturn.add(new Factura(factura.getNro(),factura.getId(),factura.getImporte()));
		}
		return facturasReturn;
	}

	public ArrayList<Factura> selectFacturasFromId(int idCliente) {
		ArrayList<Factura> facturasReturn = new ArrayList<>();
		ArrayList<Factura> facturas = db.selectAllFacturas();
		for (Factura factura : facturas) 
			if (factura.getId() == idCliente)
				facturasReturn.add(new Factura(factura.getNro(),factura.getId(),factura.getImporte()));
		return facturasReturn;
	}

	public ArrayList<Factura> selectFacturasFromImporte(int importe) {
		ArrayList<Factura> facturasReturn = new ArrayList<>();
		ArrayList<Factura> facturas = db.selectAllFacturas();
		for (Factura factura : facturas) 
			if (factura.getImporte() >= importe)
				facturasReturn.add(new Factura(factura.getNro(),factura.getId(),factura.getImporte()));
		return facturasReturn;
	}
	
	public ArrayList<Cliente> selectFromClienteWhere(String descripcion){
		return db.selectFromClienteWhere(descripcion);
	}
}
