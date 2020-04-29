import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Database {
	
	private ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),"parcial.db");
	
    public void close() { 
        db.close(); 
    }
    
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
	*/
    
    /*
    public ObjectSet<Object> find(Object obj){
    	ObjectSet<Object> os = null;
    	try {
			os = db.queryByExample(obj);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	return os;
    }
    */
    
    /*
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
}
