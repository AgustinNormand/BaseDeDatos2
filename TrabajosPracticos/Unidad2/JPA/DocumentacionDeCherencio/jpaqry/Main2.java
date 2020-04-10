
/**
 * Main class inicia aplicacion java que utiliza JPA en standard edition
 * Permite abm a traves de JTable de todo tipo de Entidades a persistir 
 * usando JPA
 * 
 * @author G.Cherencio 
 * @version 1.0
 */
import javax.persistence.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
@SuppressWarnings("unchecked")
public class Main2 {
   private static EntityManagerFactory emf = null;
   private static EntityManager em = null;
   
   public static void main(String arg[]) {
       System.out.println("Main2:inicio");
       emf = Persistence.createEntityManagerFactory("tapas");
       System.out.println("Main2:emf creado");
       em = emf.createEntityManager();
       System.out.println("Main2:em creado");
       pantalla();
   }
   public static EntityManagerFactory getEntityManagerFactory() { return emf; }
   public static EntityManager getEntityManager() { return em; }
   public static void salir() {
       if ( em != null )  { em.close();em=null; }
       if ( emf != null ) { emf.close();emf=null; }
       System.out.println("Main2:fin");
       System.exit(0);
   }
   public static void pantalla() {
       JFrame frame = new JFrame("J2SE + JPA 2.0");
       JTable tabla = new JTable();
       JScrollPane scroll = new JScrollPane(tabla);
       frame.getContentPane().setLayout(new BorderLayout());
       Query qry = em.createNamedQuery("cliente.porNombre");
       qry.setParameter("nombre", "A%");
       List<Cliente> clientes = qry.getResultList();
       Model<Cliente> cm = new Model<Cliente>("Cliente",clientes,Main2.em,tabla);
       tabla.setModel(cm);
       tabla.addKeyListener(cm);
       frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { Main2.salir(); } });
       frame.getContentPane().add(scroll,"Center");
       frame.setSize(400,400);
       frame.setVisible(true);
   }
}
