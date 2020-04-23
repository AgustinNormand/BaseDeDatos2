import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class Tests {
	Gestor gestor = new Gestor();
	
	@Test
	@Order(1)
	void dropearDatabase() {
		gestor.dropDatabase();
	}
	@Test
	@Order(2)
	void clientesVacios() {
		ArrayList<Cliente> clientes = gestor.selectAllClientes();
		Assert.assertTrue(clientes.isEmpty());
	}
	
	@Test
	@Order(3)
	void facturasVacias() {
		ArrayList<Factura> facturas = gestor.selectAllFacturas();
		Assert.assertTrue(facturas.isEmpty());
	}

	
	@Test
	@Order(4)
	void insercionCliente() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		ArrayList<Cliente> clientesEsperados = new ArrayList<Cliente>();
		clientesEsperados.add(new Cliente(1,"A"));
		Assert.assertEquals(clientesEsperados.toString(),gestor.selectAllClientes().toString());
	}
	
	@Test
	@Order(5)
	void insercionFactura() {
		assertEquals(0,gestor.insertarFactura(1,1,1));
		ArrayList<Factura> facturasEsperadas = new ArrayList<Factura>();
		facturasEsperadas.add(new Factura(1,1,1));
		Assert.assertEquals(facturasEsperadas.toString(),gestor.selectAllFacturas().toString());
	}
	
	@Test
	@Order(6)
	void insercionClienteClienteRepetido() {
		assertEquals(1,gestor.insertarCliente(1, "A"));
	}

	@Test
	@Order(7)
	void insercionFacturaClienteInexistente() {
		assertEquals(1,gestor.insertarFactura(1,2,1));
	}
	
	@Test
	@Order(8)
	void insercionFacturaFacturaRepetida() {
		assertEquals(2,gestor.insertarFactura(1,1,1));
	}
	
	@Test
	@Order(9)
	void eliminacionFacturaFacturaInexistente() {
		assertEquals(1,gestor.deleteFactura(2));
	}
	
	@Test
	@Order(10)
	void eliminacionClienteClienteInexistente() {
		assertEquals(1,gestor.deleteCliente(2));
	}

	@Test
	@Order(11)
	void eliminacionFactura() {
		assertEquals(0,gestor.deleteFactura(1));
		Assert.assertEquals(new ArrayList<Factura>().toString(),gestor.selectAllFacturas().toString());
	}
	
	@Test
	@Order(12)
	void eliminacionCliente() {
		assertEquals(0,gestor.deleteCliente(1));
		Assert.assertEquals(new ArrayList<Cliente>().toString(),gestor.selectAllClientes().toString());
	}
		
	@Test
	@Order(13)
	void eliminacionClienteEliminacionFacturas() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(0,gestor.insertarFactura(1, 1, 1));
		assertEquals(0,gestor.deleteCliente(1));
		Assert.assertEquals(new ArrayList<Cliente>().toString(),gestor.selectAllClientes().toString());
		Assert.assertEquals(new ArrayList<Factura>().toString(),gestor.selectAllFacturas().toString());
	}
	
	@Test
	@Order(14)
	void modificacionFactura() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(0,gestor.insertarFactura(1, 1, 1));
		assertEquals(0,gestor.updateFactura(1,2,1,1));
		ArrayList<Factura> facturasEsperadas = new ArrayList<>();
		facturasEsperadas.add(new Factura(2,1,1));
		Assert.assertEquals(facturasEsperadas.toString(),gestor.selectAllFacturas().toString());
		gestor.dropDatabase();
	}
	
	@Test
	@Order(15)
	void modificacionCliente() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(0,gestor.updateCliente(1, 2, "B"));
		ArrayList<Cliente> clientesEsperados = new ArrayList<>();
		clientesEsperados.add(new Cliente(2,"B"));
		Assert.assertEquals(clientesEsperados.toString(),gestor.selectAllClientes().toString());
		gestor.dropDatabase();
	}
	
	@Test
	@Order(16)
	void modificacionClienteCascadaFactura() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(0,gestor.insertarFactura(1, 1, 1));
		assertEquals(0,gestor.updateCliente(1, 2, "B"));
		ArrayList<Cliente> clientesEsperados = new ArrayList<>();
		clientesEsperados.add(new Cliente(2,"B"));
		ArrayList<Factura> facturasEsperadas = new ArrayList<>();
		facturasEsperadas.add(new Factura(1,2,1));
		Assert.assertEquals(clientesEsperados.toString(),gestor.selectAllClientes().toString());
		Assert.assertEquals(facturasEsperadas.toString(),gestor.selectAllFacturas().toString());
		gestor.dropDatabase();
	}
	
	@Test
	@Order(17)
	void modificacionClienteClienteInexistente() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(1,gestor.updateCliente(2, 3, "B"));
		gestor.dropDatabase();
	}
	
	@Test
	@Order(18)
	void modificacionClienteClienteRepetido() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(0,gestor.insertarCliente(2, "B"));
		assertEquals(2,gestor.updateCliente(1, 2, "C"));
		gestor.dropDatabase();
	}
	
	@Test
	@Order(19)
	void modificacionFacturaFacturaInexistente() {
		assertEquals(1,gestor.updateFactura(1, 2, 1, 1));
		gestor.dropDatabase();
	}
	
	@Test
	@Order(20)
	void modificacionFacturaClienteInexistente() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(0,gestor.insertarFactura(1, 1, 1));
		assertEquals(2,gestor.updateFactura(1, 1, 2, 1));
		gestor.dropDatabase();
	}
	
	@Test
	@Order(21)
	void modificacionFacturaFacturaRepetida() {
		assertEquals(0,gestor.insertarCliente(1, "A"));
		assertEquals(0,gestor.insertarFactura(1, 1, 1));
		assertEquals(0,gestor.insertarFactura(2, 1, 1));
		assertEquals(3,gestor.updateFactura(1, 2, 1, 1));
		gestor.dropDatabase();
	}
	
	
}
