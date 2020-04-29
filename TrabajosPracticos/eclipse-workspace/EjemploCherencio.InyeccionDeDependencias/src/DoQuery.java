import java.sql.*;
import javax.sql.*;
/**
 * Clase simple para hacer un query a b.d.
 * que sera inyectada con dependencia de conexion a b.d. firebird 2.5
 * No utiliza DataIO directamente para hacer query, solo para ejemplificar
 * uso de conexion inyectada
 * 
 * @author G. Cherencio
 * @version 1.0
 */
public class DoQuery {
    
    @MyConnection
    private Connection c;  //valor inyectado!
    private String sql;

    /**
     * Constructor for objects of class DoQuery
     */
    public DoQuery() {
        // initialise instance variables
        this("SELECT * FROM CLIENTE");
    }
    public DoQuery(final String sql) {
        this.sql = sql;
        System.out.println("Inyecto conexion");
        DoInjection.doInjection(this);
    }

    /**
     * Muestra el valor del primer campo en todas las filas del query
     */
    public void doQuery() {
        try {
            Statement ss = c.createStatement();
            ResultSet rrs = ss.executeQuery(sql); // select
            String fld0 = rrs.getMetaData().getColumnName(1);
            int i=0;
            while(rrs.next()) {
                System.out.println("row "+i+" field="+fld0+" value="+rrs.getString(1));
            }
            rrs.close();
            ss.close();
        } catch (SQLException e) {
            System.err.println("DoQuery.doQuery(): Error query ["+sql+"] "+e.getMessage());
        }
        
    }
}
