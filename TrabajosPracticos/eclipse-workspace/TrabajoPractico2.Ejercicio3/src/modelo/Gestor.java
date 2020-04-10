package modelo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Gestor {
	
	private static Gestor singleInstance = null;

	private EntityManagerFactory emf = null;
	
	private Gestor() {
		emf = Persistence.createEntityManagerFactory("database");
	}
	
	public static Gestor getInstance() {
		if (singleInstance == null)
				singleInstance = new Gestor();
		
		return singleInstance;
	}
	
	public int persist(Object entity){
		EntityManager em = emf.createEntityManager();
		int errorCode = 0;
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		return errorCode; //Missing to code this
	}
	
	/* OPERACIONES CON FACTURA */
	
	@SuppressWarnings("unchecked")
	public List<Factura> selectAllFromFactura(){
		EntityManager em = emf.createEntityManager();
		List<Factura> facturas = (List<Factura>)  em.createQuery("SELECT f FROM FACTURA f").getResultList();
		em.close();
		return facturas;
	}
	
	public Factura selectFacturaWhere(int nro) {
		EntityManager em = emf.createEntityManager();
		Factura factura = em.find(Factura.class, nro);
		return factura;
	}
	
	public void removeFactura(Factura factura) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		factura = em.merge(factura);
		em.remove(factura);
		em.getTransaction().commit();
		em.close();
	}
	
	/* FIN DE OPERACIONES CON FACTURA */
	
	/* OPERACIONES CON DETALLE */
	
	@SuppressWarnings("unchecked")
	public List<Detalle> selectAllFromDetalle(){
		EntityManager em = emf.createEntityManager();
		List<Detalle> detalles = (List<Detalle>)  em.createQuery("SELECT d FROM DETALLE d").getResultList();
		em.close();
		return detalles;
	}
	
	public Detalle selectDetalleWhere(int nro, int id) {
		EntityManager em = emf.createEntityManager();
		//Detalle detalle = em.find(Detalle.class,nro,id);
		Detalle detalle = (Detalle) em.createQuery("SELECT d FROM DETALLE d WHERE ID = "+id+" AND NRO = "+nro);
		return detalle;
	}
	
	public void removeDetalle(Detalle detalle) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		detalle = em.merge(detalle);
		em.remove(detalle);
		em.getTransaction().commit();
		em.close();
	}
	
	/* FIN DE OPERACIONES CON DETALLE */
	
	/* OPERACIONES CON CLIENTE */
	
	@SuppressWarnings("unchecked")
	public List<Cliente> selectAllFromCliente(){
		EntityManager em = emf.createEntityManager();
		List<Cliente> clientes = (List<Cliente>)  em.createQuery("SELECT c FROM CLIENTE c").getResultList();
		em.close();
		return clientes;
	}
	
	public Cliente selectClienteWhere(int idCliente) {
		EntityManager em = emf.createEntityManager();
		Cliente cliente = em.find(Cliente.class, idCliente);
		return cliente;
	}
	
	public void removeFactura(Cliente cliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		cliente = em.merge(cliente);
		em.remove(cliente);
		em.getTransaction().commit();
		em.close();
	}
	
	/* FIN DE OPERACIONES CON CLIENTE */
	
	/* OPERACIONES CON PRODUCTO */
	
	@SuppressWarnings("unchecked")
	public List<Producto> selectAllFromProducto(){
		EntityManager em = emf.createEntityManager();
		List<Producto> productos = (List<Producto>)  em.createQuery("SELECT p FROM PRODUCTO p").getResultList();
		em.close();
		return productos;
	}
	
	public Producto selectProductoWhere(int idProducto) {
		EntityManager em = emf.createEntityManager();
		Producto producto = em.find(Producto.class, idProducto);
		return producto;
	}
	
	public void removeProducto(Producto producto) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		producto = em.merge(producto);
		em.remove(producto);
		em.getTransaction().commit();
		em.close();
	}
	
	/* FIN DE OPERACIONES CON PRODUCTO */
	
	/* OPERACIONES CON PROVEEDOR */
	
	@SuppressWarnings("unchecked")
	public List<Proveedor> selectAllFromProveedor(){
		EntityManager em = emf.createEntityManager();
		List<Proveedor> proveedores = (List<Proveedor>)  em.createQuery("SELECT p FROM PROVEEDOR p").getResultList();
		em.close();
		return proveedores;
	}
	
	public Proveedor selectProveedorWhere(int idProveedor) {
		EntityManager em = emf.createEntityManager();
		Proveedor proveedor = em.find(Proveedor.class, idProveedor);
		return proveedor;
	}
	
	public void removeProveedor(Proveedor proveedor) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		proveedor = em.merge(proveedor);
		em.remove(proveedor);
		em.getTransaction().commit();
		em.close();
	}
	
	/* FIN DE OPERACIONES CON PROVEEDOR */
	
	
	

	
}
