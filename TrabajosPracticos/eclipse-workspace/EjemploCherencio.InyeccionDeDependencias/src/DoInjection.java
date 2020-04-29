import java.lang.annotation.*;
import java.lang.reflect.*;
import java.sql.*;
/**
 * Write a description of class DoInjection here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DoInjection {
    public static void doInjection(Object obj) {
        if ( obj != null ) {
            for(Field f : obj.getClass().getDeclaredFields()) {
                if ( f.isAnnotationPresent(MyConnection.class) ) {
                    // el campo esta anotado como MyConnection
                    if ( f.getType().equals(Connection.class) ) {
                        // el campo es de tipo Connection
                        f.setAccessible(true);
                        try {
                            f.set(obj,DataIO.getConnection());
                        } catch(Exception e) {
                            System.err.println("DoInjection.doInjection(): Error in "+
                                obj.getClass().getName()+" "+e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
