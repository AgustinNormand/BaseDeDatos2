package model;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.query.Query;

public class Database {
	
	//Embebida
	private ObjectContainer db = null;
	
	//Servidor
	//private ObjectServer server = null;
	//private ObjectContainer db = null;
	
    public void close() { 
        db.close(); 
    }
    
    /*
     * SI ES CON CLIENTE / SERVIDOR
     * 
    public void initServer() {
    	try {
			server = Db4oClientServer.openServer("parcial.db", 8080);
			server.grantAccess("SYSDBA", "masterkey");
			System.out.println("Server db4o running...");
            System.out.println("at localhost,port 8080,database mibase.db ...");
            System.out.println("Press any key to stop");
            System.in.read();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			server.close();
		}
    }
    
    public void initClient() {
    	try {
			db = Db4oClientServer.openClient("localhost", 8080, "SYSDBA", "masterkey");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    
    /*
     * SI NO REQUIERE CONFIGURACION 
     *
    */
    public void initEmbebidoSinConfiguracion(){
    db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),"parcial.db");
    }
    
    
    /*
     * SI REQUIERE CONFIGURACION
     * 
     public void initEmbebidoConConfiguracion() {
        EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
        conf.common().objectClass(.class).cascadeOnUpdate(boolean);
        conf.common().objectClass(.class).cascadeOnDelete(boolean);
        db = Db4oEmbedded.openFile(conf,"parcial.db");
    }
     * */
    
    
  /*
    public void insert(Object obj) {
    	try {
    		if (selectFirst(obj) == null) {
        		db.store(obj);
        		db.commit();
        	}	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }

    public ObjectSet<Object> find(Object obj){
    	ObjectSet<Object> os = null;
    	try {
			os = db.queryByExample(obj);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return os;
    }
 
    

	public Object selectFirst(Object obj) {
		Object objReturn = null;
		try {
			ObjectSet<Object> os = find(obj);
			while (objReturn == null && os.hasNext()) 
				objReturn = os.next();			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return objReturn;
	}
*/
    
    /*
	public void delete (Object obj) {
		ObjectSet<Object> os = find(obj);
		if (os != null) {
			while(os.hasNext()) 
				try {
					Object found = os.next();
					db.delete(found);
					db.commit();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
		}
	}
	*/
 
    /*
     * CONVERTIR EL RESULTADO DE UN QUERY A STRING
     * 
    public StringBuilder list(ObjectSet<Object> os) {
    	StringBuilder sb = new StringBuilder();
    	try {
			Object found = null;
			while(os.hasNext()) {
				found = os.next();
				sb.append(found.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return sb;
    }
    */
    
    /*
     * LISTAR LOS OBJETOS QUE NO CONTENGAN UN VALOR DETERMINADO EN UN ATRIBUTO.
     * 
    public StringBuilder listNotAtribute(String valueAtribute) {
    	StringBuilder stringReturn = null;
    	try {
			Query query = db.query();
			query.constrain(Object.class);
			query.descend("atribute").constrain(valueAtribute).not();
			ObjectSet<Object> os = query.execute();
			stringReturn = list(os);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return stringReturn;
    }
    */
    
    /*
     * LISTAR LOS OBJETOS CON QUE CONTENGAN UN VALOR MAYOR AL INDICADO EN UN ATRIBUTO.
     * 
    public StringBuilder listGreaterAtribute (int valueAtribute) {
    	StringBuilder stringReturn = null;
    	try {
			Query query = db.query();
			query.constrain(Object.class);
			query.descend("atribute").constrain(valueAtribute).greater();
			ObjectSet<Object> os = query.execute();
			stringReturn = list(os);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return stringReturn;
    }
    */
    
    /*
     * LISTAR LOS OBJECTOS QUE CONTENGAN UN VALOR MAYOR AL INDICADO EN UN ATRIBUTO, Y UN VALOR IGUAL AL INDICADO EN OTRO ATRIBUTO
     * 
    public StringBuilder listGreaterAndLikeAtribute(int valueAtributeOne, String valueAtributeTwo) {
    	StringBuilder stringReturn = null;
    	try {
			Query query = db.query();
			query.constrain(Object.class);
			query.descend("atributeOne").constrain(valueAtributeOne).greater().and(query.descend("atributeTwo").constrain(valueAtributeTwo));
			ObjectSet<Object> os = query.execute();
			stringReturn = list(os);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return stringReturn;
    }
    */
}
