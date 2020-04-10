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
		List<Factura> facturas = (List<Factura>)  em.createQuery("SELECT f FROM Factura f").getResultList();
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
		List<Detalle> detalles = (List<Detalle>)  em.createQuery("SELECT d FROM Detalle d").getResultList();
		em.close();
		return detalles;
	}
	
	public Factura selectDetalleWhere(int nro, int id) {
		EntityManager em = emf.createEntityManager();
		Factura detalle = em.find(Detalle.class,nro,id);
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
	
	/* OPERACIONES CON FACTURA */
	
	@SuppressWarnings("unchecked")
	public List<Factura> selectAllFromFactura(){
		EntityManager em = emf.createEntityManager();
		List<Factura> facturas = (List<Factura>)  em.createQuery("SELECT f FROM Factura f").getResultList();
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
	
	/* OPERACIONES CON FACTURA */
	
	@SuppressWarnings("unchecked")
	public List<Factura> selectAllFromFactura(){
		EntityManager em = emf.createEntityManager();
		List<Factura> facturas = (List<Factura>)  em.createQuery("SELECT f FROM Factura f").getResultList();
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
	
	/* OPERACIONES CON FACTURA */
	
	@SuppressWarnings("unchecked")
	public List<Factura> selectAllFromFactura(){
		EntityManager em = emf.createEntityManager();
		List<Factura> facturas = (List<Factura>)  em.createQuery("SELECT f FROM Factura f").getResultList();
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
	
	
	

	
}
