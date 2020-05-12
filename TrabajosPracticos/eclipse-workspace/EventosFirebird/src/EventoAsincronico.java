import org.firebirdsql.event.*;
import org.firebirdsql.gds.impl.GDSType;
import javax.swing.*;
import java.util.Date;
import java.sql.SQLException;
/**
 * Write a description of class EventoAsincronico here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class EventoAsincronico implements EventListener {
    private EventManager eventManager;
    private JTextArea txt;
    
    public EventoAsincronico(FBCon f,JTextArea t) {
        eventManager = new FBEventManager(GDSType.getType("PURE_JAVA"));
        eventManager.setDatabase(f.getBaseDeDatos());
        eventManager.setHost(f.getServer());
        eventManager.setPort(f.getPuerto());
        eventManager.setUser(f.getUser());
        eventManager.setPassword(f.getPwd());
        txt = t;
    }

    
    public void run(String [] eventNames){
        try {
            eventManager.connect();
            for (int i = 0; i < eventNames.length; i++){
                eventManager.addEventListener(eventNames[i], this);
            }
        } catch (SQLException se){
            throw new RuntimeException(se);
        }
    }
 
 
    public void eventOccurred(DatabaseEvent event){
        addText(new Date().toString()
                + event.getEventName() + " occurred "
                + event.getEventCount() + " time(s)\n");
    }
 
    private void addText(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    txt.setText(txt.getText() + text);
                }
            }
        );
    }
    
}
