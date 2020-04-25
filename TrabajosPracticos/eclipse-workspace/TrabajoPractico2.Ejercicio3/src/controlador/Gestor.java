package controlador;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;


import modelo.Cliente;
import modelo.Detalle;
import modelo.Factura;
import modelo.Proveedor;
import modelo.Producto;
import modelo.Direccion;

public class Gestor {

	private static Gestor singleInstance = null;

	private EntityManagerFactory emf = null;
	
	private static String errorMessage = "";

	private Gestor() {
		emf = Persistence.createEntityManagerFactory("database");
	}

	public static String getErrorMessage() {
		return errorMessage;
	}
	
	public static Gestor getInstance() {
		if (singleInstance == null)
			singleInstance = new Gestor();

		return singleInstance;
	}

	public int persist(Object entity){
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			errorCode = 1;
			errorMessage = e.getMessage();
		}
		finally {
			em.close();
		}
		return errorCode; //Missing to code this
	}

	/* OPERACIONES CON FACTURA */
	@SuppressWarnings("unchecked")
	public List<Factura> selectFromFactura(){
		EntityManager em = emf.createEntityManager();
		List<Factura> facturas = (List<Factura>)  em.createQuery("SELECT f FROM Factura f").getResultList();
		em.close();
		return facturas;
	}

	public Factura selectFromFacturaWhere(int nro) {
		Factura factura = null;
		EntityManager em = emf.createEntityManager();
		try {
			factura = em.find(Factura.class, nro);	
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return factura;
	}

	public int deleteFromFacturaWhere(Factura factura) {
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			factura = em.merge(factura);
			em.remove(factura);
			em.getTransaction().commit();
		} catch (IllegalArgumentException iae) {
			errorCode = 1;
			errorMessage = "La factura a eliminar no existe en la base de datos";
		} catch (Exception e) {
			errorCode = 1;
			errorMessage = e.getMessage();
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	
	public int updateFacturaSet(int nroFacturaAModificar,int idClienteNuevo, double importeNuevo) {
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			Factura oldFactura = this.selectFromFacturaWhere(nroFacturaAModificar);
			oldFactura = em.merge(oldFactura);
			oldFactura.setImporte(importeNuevo);
			oldFactura.setCliente(this.selectFromClienteWhere(idClienteNuevo));
			em.getTransaction().commit();
		} catch (Exception e) {
			errorMessage = e.getMessage();
			errorCode = 1;
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	/* FIN DE OPERACIONES CON FACTURA */

	/* OPERACIONES CON DETALLE */
	@SuppressWarnings("unchecked")
	public List<Detalle> selectFromDetalle(){
		EntityManager em = emf.createEntityManager();
		List<Detalle> detalles = (List<Detalle>)  em.createQuery("SELECT d FROM Detalle d").getResultList();
		em.close();
		return detalles;
	}

	public Detalle selectFromDetalleWhere(int nro, int id) {
		EntityManager em = emf.createEntityManager();
		Detalle detalle = null;
		try {
			detalle = em.createQuery("SELECT d FROM Detalle d WHERE d.factura = :nro AND d.producto = :id",Detalle.class)
					.setParameter("nro", this.selectFromFacturaWhere(nro))
					.setParameter("id", this.selectFromProductoWhere(id))
					.getSingleResult();
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return detalle;
	}

	public int deleteFromDetalleWhere(Detalle detalle) {
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			detalle = em.merge(detalle);
			em.remove(detalle);
			em.getTransaction().commit();
		} catch (IllegalArgumentException iae) {
			errorCode = 1;
			errorMessage = "El detalle a eliminar no existe en la base de datos";
		} catch (Exception e) {
			errorCode = 1;
			errorMessage = e.getMessage();
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	
	public int updateDetalleSet(int nroDetalleAModificar, int idDetalleAModificar, int nuevaCantidad, double nuevoImporte) {
		int errorCode = 0;
		Detalle detalle = null;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			detalle = detalle = em.createQuery("SELECT d FROM Detalle d WHERE d.factura = :nro AND d.producto = :id",Detalle.class)
					.setParameter("nro", this.selectFromFacturaWhere(nroDetalleAModificar))
					.setParameter("id", this.selectFromProductoWhere(idDetalleAModificar))
					.getSingleResult();
			detalle.setPrecio(nuevoImporte);
			detalle.setCantidad(nuevaCantidad);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (detalle == null)
				errorMessage = "El NRO e ID no pertenecen a un detalle en la base de datos";
			else
				errorMessage = e.getMessage();
			errorCode = 1;
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	/* FIN DE OPERACIONES CON DETALLE */

	/* OPERACIONES CON CLIENTE */
	@SuppressWarnings("unchecked")
	public List<Cliente> selectFromCliente(){
		EntityManager em = emf.createEntityManager();
		List<Cliente> clientes = (List<Cliente>)  em.createQuery("SELECT c FROM Cliente c").getResultList();
		em.close();
		return clientes;
	}

	public Cliente selectFromClienteWhere(int idCliente) {
		Cliente cliente = null;
		EntityManager em = emf.createEntityManager();
		try {
			cliente = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.id = "+idCliente).getSingleResult();	
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return cliente;
	}

	public int deleteFromClienteWhere(Cliente cliente) {
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			cliente = em.merge(cliente);
			em.remove(cliente);
			em.getTransaction().commit();
		} catch (IllegalArgumentException iae) {
			errorCode = 1;
			errorMessage = "El cliente a eliminar no existe en la base de datos";
		} catch (Exception e) {
			errorCode = 1;
			errorMessage = e.getMessage();
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	/* FIN DE OPERACIONES CON CLIENTE */

	/* OPERACIONES CON PRODUCTO */
	@SuppressWarnings("unchecked")
	public List<Producto> selectFromProducto(){
		EntityManager em = emf.createEntityManager();
		List<Producto> productos = (List<Producto>)  em.createQuery("SELECT p FROM Producto p").getResultList();
		em.close();
		return productos;
	}

	public Producto selectFromProductoWhere(int idProducto) {
		Producto producto = null;
		EntityManager em = emf.createEntityManager();
		try {
			producto = (Producto) em.createQuery("SELECT p FROM Producto p WHERE p.id = "+idProducto).getSingleResult();
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return producto;
	}

	public int deleteFromProductoWhere(Producto producto) {
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			producto = em.merge(producto);
			em.remove(producto);
			em.getTransaction().commit();
		} catch (IllegalArgumentException iae) {
			errorCode = 1;
			errorMessage = "El producto a eliminar no existe en la base de datos";
		} catch (Exception e) {
			errorCode = 1;
			errorMessage = e.getMessage();
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	/* FIN DE OPERACIONES CON PRODUCTO */

	/* OPERACIONES CON PROVEEDOR */
	@SuppressWarnings("unchecked")
	public List<Proveedor> selectFromProveedor(){
		EntityManager em = emf.createEntityManager();
		List<Proveedor> proveedores = (List<Proveedor>)  em.createQuery("SELECT p FROM Proveedor p").getResultList();
		em.close();
		return proveedores;
	}

	public Proveedor selectFromProveedorWhere(int idProveedor) {
		EntityManager em = emf.createEntityManager();
		Proveedor proveedor = null;
		try {
			proveedor = em.find(Proveedor.class, idProveedor);
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return proveedor;
	}

	public int deleteFromProveedorWhere(Proveedor proveedor) {
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			proveedor = em.merge(proveedor);
			em.remove(proveedor);
			em.getTransaction().commit();
		} catch (IllegalArgumentException iae) {
			errorCode = 1;
			errorMessage = "El proveedor a eliminar no existe en la base de datos";
		} catch (Exception e) {
			errorCode = 1;
			errorMessage = e.getMessage();
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	/* FIN DE OPERACIONES CON PROVEEDOR */

	/* OPERACIONES CON DIRECCION */
	@SuppressWarnings("unchecked")
	public List<Direccion> selectFromDireccion(){
		EntityManager em = emf.createEntityManager();
		List<Direccion> direcciones = (List<Direccion>)  em.createQuery("SELECT d FROM Direccion d").getResultList();
		em.close();
		return direcciones;
	}

	public Direccion selectFromDireccionWhere(int idDireccion) {
		EntityManager em = emf.createEntityManager();
		Direccion direccion = null;
		try {
			direccion = em.find(Direccion.class, idDireccion);
		} catch (Exception e) {
			e.getMessage();
		}
		return direccion;
	}

	public int deleteFromDireccionWhere(Direccion direccion) {
		int errorCode = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			direccion = em.merge(direccion);
			em.remove(direccion);
			em.getTransaction().commit();
		} catch (IllegalArgumentException iae) {
			errorCode = 1;
			errorMessage = "La direccion a eliminar no existe en la base de datos";
		} catch (Exception e) {
			errorCode = 1;
			errorMessage = e.getMessage();
		}
		finally {
			em.close();
		}
		return errorCode;
	}
	/* FIN DE OPERACIONES CON DIRECCION */

	public void close() {
		emf.close();
	}
}
