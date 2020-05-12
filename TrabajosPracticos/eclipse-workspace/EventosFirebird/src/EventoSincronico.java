import org.firebirdsql.event.EventManager;
import org.firebirdsql.event.FBEventManager;
import org.firebirdsql.gds.impl.GDSType;
import java.sql.SQLException;

/**
 * Permite bloquear el proceso actual en metodo run()
 * quedando a la espera de un evento de BD Firebird SQL Server
 * sobre la conexion que se indique al constructor
 * Probado con FB 2.5 y Jaybird 3.0.6 en Windows 10
 *
 * @author G. Cherencio
 * @version 1.0
 * @see https://github.com/FirebirdSQL/jaybird/wiki/Jaybird-Events-API
 */
public class EventoSincronico {
    private EventManager eventManager;
    private int timeOut = 10000;
 
    public EventoSincronico(FBCon con){
        eventManager = new FBEventManager(GDSType.getType("PURE_JAVA"));
        eventManager.setDatabase(con.getBaseDeDatos());
        eventManager.setHost(con.getServer());
        eventManager.setPort(con.getPuerto());
        eventManager.setUser(con.getUser());
        eventManager.setPassword(con.getPwd());
    }
 
    public void run(String eventName){
        int eventCount = 0;
        try {
            eventManager.connect();
            System.out.println("Espero por evento "+eventName);
            int count = -1;
            while (count == -1){
                try {
                    count = eventManager.waitForEvent(eventName, getTimeOut());
                    if (count == -1){
                        System.out.println("Timed out esperando "+eventName);
                        //break;
                    }
                } catch (InterruptedException ie){
                    throw new RuntimeException(ie);
                }
            }
            System.out.println("Recibi " + count + " instancia(s) "
                    + "del evento '" + eventName + "'");
            eventManager.disconnect();
        } catch (SQLException se){
            throw new RuntimeException(se);
        }
    }
 
    // setters getters
    public void setTimeOut(int n) { timeOut = n; }
    public int  getTimeOut() { return timeOut; }
}    
