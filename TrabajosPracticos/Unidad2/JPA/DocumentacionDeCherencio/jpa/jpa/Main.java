
/**
 * Main class inicia aplicacion java que utiliza JPA en standard edition
 * 
 * @author G.Cherencio 
 * @version 1.0
 */
import javax.persistence.*;
public class Main {
   private static EntityManagerFactory emf = null;
   private static EntityManager em = null;
   
   public static void main(String arg[]) {
       System.out.println("Main:inicio");
       emf = Persistence.createEntityManagerFactory("tapas");
       System.out.println("Main:emf creado");
       em = emf.createEntityManager();
       System.out.println("Main:em creado");
       EntityTransaction emt = em.getTransaction();
       System.out.println("Main:emt creada");
       emt.begin();
       System.out.println("Main:emt.begin() hecho");
       Cliente c = new Cliente();
       c.setCodigo(456);
       c.setNombre("alta desde JPA 2.0");
       c.setDirec("eclipseLink");
       c.setPostal(6700);
       String smsg = "persist()";
       try {
           em.persist(c);
           //Cliente f = em.find(Cliente.class,456); // acceso por PK
           //em.remove(f);
           System.out.println("Main:em.persist(c) hecho");
           smsg = "commit()";
           emt.commit();
           System.out.println("Main:emt.commit() hecho");
       } catch(IllegalArgumentException iae) {
           System.out.println("Main:Error en "+smsg+ " persistiendo cliente, posiblemente sea null");
       } catch(EntityExistsException eee) {
           System.out.println("Main:Error en "+smsg+" persistiendo cliente, esta entidad ya existe");
       } catch(TransactionRequiredException tre) {
           System.out.println("Main:Error en "+smsg+" persistiendo cliente, se requiere de una transaccion");
       } catch(Exception e) {
           System.out.println("Main:Error en "+smsg+ " persistiendo cliente, error: "+e.getMessage());
       }
       salir();
       System.out.println("Main:fin");
   }
   public static EntityManagerFactory getEntityManagerFactory() { return emf; }
   public static EntityManager getEntityManager() { return em; }
   public static void salir() {
       if ( em != null ) em.close();
       if ( emf != null ) emf.close();
   }
}
