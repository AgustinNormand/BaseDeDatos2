package ModelController;

import javax.persistence.EntityManagerFactory;
import javax.persistence.*;

public class Gestor {
	private static Gestor singleInstance = null;

	private EntityManagerFactory emf = null;

	private static String errorMessage = "";

	private Gestor() {
		emf = Persistence.createEntityManagerFactory("db");
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
		return errorCode;
	}
	
	public void close() {
		emf.close();
	}
}
