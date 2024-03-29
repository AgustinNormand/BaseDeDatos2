import java.io.File;
import java.util.ArrayList;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class Database {
	private ObjectContainer db = null;
	String finalPath = "";
	public Database() {
		String[] path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("/");
		path[path.length-1] = "database.yml";
		for (String string : path)
			if (!string.equals(""))
				finalPath = finalPath + "/" +string;
	}
	
	private void openDb() {
		db = Db4oEmbedded.openFile(finalPath);
	}
	
	private void closeDb() {
		db.close();
	}
	
	public void insertarCliente(Cliente cliente) {
		openDb();
		db.store(cliente);
		closeDb();
	}
	public ArrayList<Cliente> selectAllClientes() {
		openDb();
		Query query = db.query();
		query.constrain(Cliente.class);
		ObjectSet listClientes = query.execute();
		ArrayList<Cliente> lc = new ArrayList<>();
		for (Object cliente : listClientes) 
			lc.add((Cliente) cliente);
		closeDb();
		return lc;
	}
	
	public void insertarFactura(Factura factura) {
		openDb();
		db.store(factura);
		closeDb();
	}
	
	public ArrayList<Factura> selectAllFacturas() {
		openDb();
		ObjectSet listFacturas = db.queryByExample(Factura.class); 
		ArrayList<Factura> lf = new ArrayList<>();
		for (Object factura : listFacturas) 
			lf.add((Factura) factura);
		closeDb();
		return lf;
	}
	
	public void deleteCliente(Cliente cliente) {
		openDb();
		ObjectSet result = db.queryByExample(cliente);
		Cliente clienteAEliminar = (Cliente) result.next();
		db.delete(clienteAEliminar);
		closeDb();
	}
	
	public void deleteFactura(Factura factura) {
		openDb();
		ObjectSet result = db.queryByExample(factura);
		Factura facturaAEliminar = (Factura) result.next();
		db.delete(facturaAEliminar);
		closeDb();
	}
	
	public void updateFactura(Factura oldBill,Factura newBill) {
		openDb();
		ObjectSet result = db.queryByExample(oldBill);
		Factura facturaAModificar = (Factura) result.next();
		facturaAModificar.setId(newBill.getId());
		facturaAModificar.setImporte(newBill.getImporte());
		facturaAModificar.setNro(newBill.getNro());
		db.store(facturaAModificar);
		closeDb();
	}
	
	public void updateCliente(Cliente oldClient, Cliente newClient) {
		openDb();
		ObjectSet result = db.queryByExample(oldClient);
		Cliente clienteAModificar = (Cliente) result.next();
		clienteAModificar.setId(newClient.getId());
		clienteAModificar.setDescr(newClient.getDescr());
		db.store(clienteAModificar);
		closeDb();
	}

	public int drop() {
		int errorCode = 0;
		File file = new File(finalPath);
		if (!file.delete())
			errorCode = 1;
		return errorCode;
	}
	
	public ArrayList<Cliente> selectFromClienteWhere(String descripcion) {
		openDb();
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		Query query = db.query();
		query.constrain(Cliente.class);
		query.descend("descr").constrain("b");
		ObjectSet listClientes = query.execute();
		for (Object cliente : listClientes) 
			clientes.add((Cliente) cliente);
		closeDb();
		return clientes;
	}
	
	public Factura selectFromFacturaWhere(int idFactura) {
		Factura factura = null;
		openDb();/*
		Query query = db.query();
		query.constrain(Factura.class);
		query.descend("nro").constrain(idFactura);
		ObjectSet resultado = query.execute();
		factura = (Factura) resultado.get(0);
		*/
		Factura facturaModelo = new Factura();
		facturaModelo.setImporte(idFactura);
		ObjectSet resultado = db.queryByExample(facturaModelo);
		factura = (Factura) resultado.get(0);
		closeDb();
		return factura;
	}
}
