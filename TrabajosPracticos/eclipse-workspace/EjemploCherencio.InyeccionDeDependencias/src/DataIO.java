 

import java.sql.*;
import java.util.*;
import javax.swing.*;

import org.firebirdsql.jdbc.*;

import java.text.*;
import java.io.*;

/**
 * Clase static encargada de realizar I/O sobre la base de datos y mantener conexion sobre la misma
 * Basada en codigo de ejemplo provista por driver JDBC Jaybird para utilizar con base de datos firebird sql
 * 
 * Falta agregar soporte para ejecucion de store procedures
 * 
 * <b>Clase static no preparada para la ejecucion en un entorno multi-thread (una posibilidad es agregar metodo
 * getInstance() y compartir -en forma sincronizada- referencia Connection y en donde s,rs,rsMetaData sean atributos
 * de instancia en vez de clase</b>
 * 
 * Parametros de conexion a b.d. hardcoded
 * 
 * @author G.Cherencio
 * @version 1.0
 */
public class DataIO {
    // instance variables - replace the example below with your own
    public static final String OS = System.getProperty("os.name").toLowerCase();    
    private static org.firebirdsql.pool.FBWrappingDataSource dataSource = null;
    private static Connection c = null;
    private static String databaseURL = null;
    private static String dbDescription = "An Example Database";
    private static String appName = null;
    private static String user = null;
    private static String password = null;
    private static String rol = null;
    private static int loginTimeOut = 10; // 10 segundos de time-out para login
    private static java.sql.DatabaseMetaData dbMetaData = null;
    //private static Statement s = null;
    //private static ResultSet rs = null;
    //private static ResultSetMetaData rsMetaData = null;
    private static boolean transactionSupported = false;
    // aqui guardo los ultimos mensajes de error significativos de sql
    private static StringBuffer msgErr = new StringBuffer();
    // formato de fechas por defecto
    private static String dateFmt = "dd/MM/yyyy";
    // bloque static para inicializar DataIO
    static {
        preinit();
    }
    public static void preinit() {
        String home = System.getProperty("user.home");
        String filesep = System.getProperty("file.separator");
        String appName = "sgans";
        File appdir = new File(home + filesep + appName);
        if ( !appdir.exists() ) {
            // crear directorio de configuracion de aplicacion
            appdir.mkdir();
        }
        File appconf = new File(home + filesep + appName + filesep + "sgans.properties");
        System.out.println("Archivo de Parametros:"+appconf);
        Properties p = new Properties();
        if ( !appconf.exists() ) {
            // crear archivo de propiedades con valores por defecto
            p.setProperty("description.short","SGANS");
            p.setProperty("description.long","Sistema de Gestion Academico del Nivel Superior");
            if (OS.indexOf("win") >= 0) {
                p.setProperty("database","localhost/3050:C:"+filesep+"SGANS"+filesep+"isft.fdb");
            } else {
                p.setProperty("database","localhost/3050:"+filesep+"var"+filesep+"lib"+filesep
                    +"firebird"+filesep+"2.5"+filesep+"data"+filesep+"isft.fdb");
            }
            p.setProperty("user","sysdba");
            p.setProperty("password",encripto("masterkey"));
            p.setProperty("role","sysdb");
            p.setProperty("login.time.out","10");
            p.setProperty("date.format","dd/MM/yyyy");
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(appconf);
                p.store(fos,"SGANS archivo configuracion");
                fos.close();
            } catch(IOException ioe) {
                System.err.println("DataIO: Error cargando "+appconf+" "+ioe.getMessage());
            }
        } else {
            // cargar archivo de propiedades
            try {
                p.load(new FileInputStream(appconf));
            } catch(IOException ioe) {
                System.err.println("DataIO: Error cargando "+appconf+" "+ioe.getMessage());
            }
        }
        // seteo propiedades DataIO
        if ( getAppName() == null ) setAppName(p.getProperty("description.short"));
        else p.setProperty("description.short",getAppName());
        setDescription(p.getProperty("description.long"));
        if ( getDatabase() == null ) setDatabase(p.getProperty("database"));
        else p.setProperty("database",getDatabase());
        if ( getUser() == null ) setUser(p.getProperty("user"));
        else p.setProperty("user",getUser());
        if ( getPassword() == null ) setPassword(desencripto(p.getProperty("password")));
        else p.setProperty("password",encripto(getPassword()));
        if ( getRole() == null ) setRole(p.getProperty("role"));
        else p.setProperty("role",getRole());
        setLoginTimeOut(new Integer(p.getProperty("login.time.out")));
        setDateFmt(p.getProperty("date.format"));
        // vuelvo a grabar parametros que pudieron haber sido actualizados en dialogo de login
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(appconf);
            p.store(fos,"SGANS archivo configuracion");
            fos.close();
        } catch(IOException ioe) {
            System.err.println("DataIO: Error grabando "+appconf+" "+ioe.getMessage());
        }
        System.out.println("Parametros Seteados");
    }

    /**
     * 
     * Metodo a ser llamado antes que init() permite cambiar el usuario, password, rol por defecto
     * 
     * @param u Nombre de Usuario dentro de la base de datos
     * @param p Contraseña del usuario de la base de datos
     * @param r Rol del Usuario dentro de la base de datos
     */
    public static void setUser(String u,String p,String r) {
        user = u;password = p;rol = r;
    }
    public static void setUser(String u,String p) {
        user = u;password = p;
    }
    public static void setUser(String u) {
        user = u;
    }
    public static void setPassword(String p) {
        password = p;
    }
    public static void setRole(String r) {
        rol = r;
    }
    public static void setDateFmt(String f) { dateFmt = f; }
    public static String getDatabase() { return databaseURL; }
    public static void setDatabase(String db) { databaseURL = db; }
    public static String getDescription() { return dbDescription; }
    public static String getAppName() { return appName; }
    public static void setDescription(String d) { dbDescription = d; }
    public static void setAppName(String a) { appName = a; }
    public static int getLoginTimeOut() { return loginTimeOut; }
    public static void setLoginTimeOut(int lt) {
        loginTimeOut = lt;
    }
    public static org.firebirdsql.pool.FBWrappingDataSource getDataSource() { return dataSource; }
    public static java.sql.DatabaseMetaData getDatabaseMetaData() { return dbMetaData; }
    public static Connection getConnection() { return c; }
    
    public static String getUser() { return user; }
    public static String getPassword() { return password; }
    public static String getRole() { return rol; }
    public static String getDateFmt() { return dateFmt; }
    /**
     * 
     * Establece la conexion a la b.d., pone el auto-commit en off (es decir, que luego de un conjunto 
     * de actualizaciones a la b.d. para que las mismas esten disponibles para los otros usuarios, se debera
     * invocar al metodo doCommit() de esta clase para que realice el commit correspondiente en b.d.)
     * Utiliza forma nueva de conexion a traves de pool de conexiones utilizando datasource (la version anterior
     * de esta clase no permitia esto)
     * @return true si pudo hacer todo el trabajo Ok, false en caso de error
     */
    public static boolean init() {
        preinit();
        boolean r = true;
        try {
            dataSource = new org.firebirdsql.pool.FBWrappingDataSource();
            // Set the standard properties
            dataSource.setDatabase (getDatabase());
            dataSource.setDescription (getDescription());
            dataSource.setRoleName(getRole());
            dataSource.setLoginTimeout(getLoginTimeOut());
            c = dataSource.getConnection (getUser(), getPassword());
            
            System.out.println ("Connection established.");
            c.setAutoCommit(false);
            System.out.println("Auto-commit is disabled.");
            dbMetaData = c.getMetaData();
            // Ok, let's query a driver/database capability
            if (dbMetaData.supportsTransactions()) {
                System.out.println ("Transactions are supported.");
                transactionSupported = true;
            } else {
                System.out.println ("Transactions are not supported.");
                transactionSupported = false;
            }
        } catch(java.sql.SQLException e) {
            doException(e,"Unable to establish a connection through the driver manager.");
            r=false;
        }
        return r;
    }
    
    // lo privado no se comenta, pero en este caso, el comentario es para quien lea este fuente
    /**
     * 
     * Metodo que muestra por stderr toda la informacion de un error ocasionado por una excepcion sql
     * dentro de estos mensajes deberian estar los correspondientes a violaciones de constraints declarados
     * en la b.d. y excepciones firebird lanzadas desde triggers y store procedures
     * 
     * @param e objeto SQLException que representa el error sql
     */
    private static void showSQLException (SQLException e) {
        // Notice that a SQLException is actually a chain of SQLExceptions,
        // let's not forget to print all of them...
        SQLException next = e;
        Calendar now = Calendar.getInstance();
        String msg0 = "" + now.getTime() + " ";
        while (next != null) {
            String msg1 = "SQL Message: " + next.getMessage();
            String msg2 = "Error Code: " + next.getErrorCode();
            String msg3 = "SQL State: " + next.getSQLState();
            System.err.println(msg1);
            System.err.println(msg2);
            System.err.println(msg3);
            msgErr.append(msg0+"\n"+msg1+"\n"+msg2+"\n"+msg3+"\n\t");
            next = next.getNextException ();
        }
    }

    /**
     * 
     * @return Devuelve los ultimos mensajes de error de sql acumulados en buffer de DataIO. Utilice metodo clearErrorMessage()
     * para borrar estos errores.
     */
    public static String getErrorMessages() {
        return msgErr.toString();
    }
    /**
     * 
     * Borrar todos los ultimos errores de SQL
     */
    public static void clearErrorMessages() {
        msgErr.delete(0,msgErr.length());
    }
    /**
     * 
     * Metodo que que permite saber si esta conexion soporta transacciones o no
     * 
     * @return true si soporta transacciones, false en caso de que no las soporte
     */
    public static boolean isTransactional() { return transactionSupported; }
    
    /**
     * 
     * Metodo que permite la ejecucion de un query de actualizacion (insert,update,delete) sobre la b.d.
     * <b>No realiza commit, esto debe hacerse cuando corresponda llamando al metodo doCommit()</b>
     * 
     * @param query query sql a ejecutar
     * @return true si se ejecuto ok, false en caso de error
     */
    public static boolean executeQuery(final String query) {
        boolean r=true;
        try {
            Statement ss = c.createStatement();
            ss.executeUpdate (query); // update, insert, delete, etc.
        } catch (SQLException e) {
            doException(e,"Unable to execute ["+query+"]");
            r=false;
        }        
        return r;
    }
    /**
     * 
     * Metodo que que realiza commit en b.d. para dar por aceptado los ultimos cambios realizados sobre la b.d.
     * 
     * @return true si se ejecuto ok, false en caso de error
     */
    public static synchronized boolean doCommit() {
        boolean r=true;
        try {
            c.commit();
        } catch (SQLException e) {
            doException(e,"Unable to commit!");
            r=false;
        }        
        return r;
    }
    /**
     * 
     * Metodo que que permite la ejecucion de un query de consulta (select) sobre la b.d.
     * el cual devuelve un ResultSet que puede ser recorrido para recuperar cada una de las tuplas devueltas
     * 
     * @param query query sql a ejecutar
     * @return ResultSet con tuplas a recuperar si todo Ok o null en caso de error
     */
    public static ResultSet getResultSet(final String query) {
        ResultSet rrs = null;
        try {
            //rs = null;
            //rsMetaData = null;
            Statement ss = c.createStatement();
            rrs = ss.executeQuery(query); // select
            //rsMetaData = rrs.getMetaData();
        } catch (SQLException e) {
            doException(e,"Unable to get ResultSet from ["+query+"]");
        }
        return rrs;
    }
    /**
     * 
     * Metodo que que devuelve -a partir de ResultSet- la cantidad de columnas que posee el mismo
     * No hacer sucesivas llamadas a este metodo, utilizar la version que recibe un ResulSetMetaData
     *      
     * @param rs result set sobre el cual se pretende esta informacion
     * @return cantidad de columnas o -1 en caso de error
     */
    public static int getColumnCount(ResultSet rs) {
        int cc = -1;
        try {
            cc = getColumnCount(rs.getMetaData());
        } catch (SQLException e) {
            doException(e,"Unable to retrieve resultset metadata!");
        }
        return cc;
    }
    /**
     * 
     * Metodo que que devuelve -a partir de ResultSetMetaData- la cantidad de columnas que posee el ResultSet
     * Utilizar este metodo (basado en ResultSetMetaData) siempre que sea posible para evitar sucesivas llamadas
     * al metodo getMetaData() de la clase ResultSet
     * 
     * @param rsm ResultSetMetaData del record set sobre el cual se pretende esta informacion
     * @return cantidad de columnas o -1 en caso de error
     */
    public static int getColumnCount(ResultSetMetaData rsm) {
        int cc = -1;
        try {
            cc = rsm.getColumnCount();
        } catch (SQLException e) {
            doException(e,"Unable to retrieve column count!");
        }
        return cc;
    }
    
    /**
     * 
     * Metodo que que devuelve -a partir de info de metadata- el nombre de la columna i-esima del
     * ultimo query ejecutado
     * No hacer sucesivas llamadas a este metodo, utilizar la version que recibe un ResulSetMetaData
     * 
     * @param rs record set sobre el cual se pretende esta informacion
     * @param i numero de la columna (0..getColumnCount()-1)
     * @return nombre de la columna en cuestion o null en caso de error
     */
    public static String getColumnName(ResultSet rs,final int i) {
        String name = null;
        try {
            name = getColumnName(rs.getMetaData(),i);
        } catch (SQLException e) {
            doException(e,"Unable to retrieve ResultSetMetaData for column name("+i+")!");
        }
        return name;
    }
    /**
     * 
     * Metodo que que devuelve -a partir de info de metadata- el nombre de la columna i-esima del
     * ultimo query ejecutado
     * 
     * @param rsm ResultSetMetaData del record set sobre el cual se pretende esta informacion
     * @param i numero de la columna (0..getColumnCount()-1)
     * @return nombre de la columna en cuestion o null en caso de error
     */
    public static String getColumnName(ResultSetMetaData rsm,final int i) {
        String name = null;
        try {
            name = rsm.getColumnName(i);
        } catch (SQLException e) {
            doException(e,"Unable to retrieve column name("+i+")!");
        }
        return name;
    }
    
    /**
     * 
     * Metodo que que devuelve -a partir de info de metadata- el tipo de dato de la columna i-esima del
     * ultimo query ejecutado
     * 
     * @param rsm ResultSetMetaData del record set sobre el cual se pretende esta informacion
     * @param i numero de la columna (0..getColumnCount()-1)
     * @return entero que representa el tipo de dato sql de la columna indicada o -1 en caso de error
     */
    public static int getColumnType(ResultSetMetaData rsm,final int i) {
        int type = -1;
        try {
            type = rsm.getColumnType(i);
        } catch (SQLException e) {
            doException(e,"Unable to retrieve column type("+i+")!");
        }
        return type;
    }
    /**
     * 
     * Metodo que que devuelve -a partir de info de metadata- el tipo de dato de la columna i-esima del
     * ultimo query ejecutado
     * Preferentemente utilizar la version que recibe como argumento un ResulSetMetaData para evitar sucesivas
     * llamadas a getMetaData()
     * 
     * @param rs result set sobre el cual se pretende esta informacion
     * @param i numero de la columna (0..getColumnCount()-1)
     * @return entero que representa el tipo de dato sql de la columna indicada o -1 en caso de error
     */
    public static int getColumnType(ResultSet rs,final int i) {
        int type = -1;
        try {
            type = getColumnType(rs.getMetaData(),i);
        } catch (SQLException e) {
            doException(e,"Unable to retrieve ResultSetMetaData for column type("+i+")!");
        }
        return type;
    }
    
    // close connection
    /**
     * 
     * Metodo que cierra la conexion con la base de datos
     * ultimo query ejecutado
     * 
     * @param i numero de la columna (0..getColumnCount()-1)
     * @return entero que representa el tipo de dato sql de la columna indicada o -1 en caso de error
     */
    public static void finish() {
        try {
          c.close();
          System.out.println ("Connection closed.");
        } catch (java.sql.SQLException e) {
          doException(e,"Unable to close connection through the driver manager.");
        }        
    }
    /**
     * 
     * Metodo que imprime stack de ejecucion + mensaje (msg) + mensajes de error sql en stderr
     * 
     * @param e excepcion sql que ha ocurrido
     * @param msg mensaje de error aplicativo que se pretende anexar a los errores sql
     */
    public static void doException(SQLException e,final String msg) {
        e.printStackTrace();
        System.err.println (msg);
        msgErr.append("<html><U>"+msg+"</U></html>\n\t");
        showSQLException(e);
    }
    public static CallableStatement prepareSelPro(StoredProcedure procedure) {
        CallableStatement cs = null;
        try {
            cs = c.prepareCall(procedure.getQuery());
            FirebirdCallableStatement fbCs = (FirebirdCallableStatement) cs;
            fbCs.setSelectableProcedure(true);
        } catch(SQLException e) {
            doException(e,"DataIO.prepareSelPro():Unable to prepare SProcedure "+procedure);
            cs = null;
        }
        return cs;
    }
    
    public static CallableStatement prepareExePro(StoredProcedure procedure) {
        CallableStatement cs = null;
        try {
            cs = c.prepareCall(procedure.getQuery());
        } catch(SQLException e) {
            doException(e,"DataIO.prepareExePro():Unable to prepare Procedure "+procedure);
        }
        return cs;
    }
    
    public static ResultSet executePro(StoredProcedure procedure,CallableStatement cs) {
        ResultSet rs = null;
        try {
            rs = cs.executeQuery();
        } catch(SQLException e) {
            doException(e,"DataIO.executePro():Unable to execute Procedure "+procedure);
        }
        return rs;
    }
    
    /**
     * Setea el valor del parametro dentro de un CallableStatement, en caso de ser null, utiliza setNull
     * Se utiliza para parametros de tipo varchar ( cs.setString() )
     */
    public static void setParamValue(CallableStatement cs,int paramNro,int paramType,String paramValue) {
        try {
            if ( paramValue == null ) cs.setNull(paramNro,paramType);
            else                      cs.setString(paramNro,paramValue);
        } catch(SQLException sqle) {
            System.err.println("DataIO.setParamValue(): Error seteando param: "+paramNro+" "+sqle.getMessage());
        }
    }
    /**
     * Setea el valor del parametro dentro de un CallableStatement, en caso de ser null, utiliza setNull
     * Se utiliza para parametros de tipo integer ( cs.setInt() )
     * OJO!! TODO VALOR ENTERO <= 0 SE LO ASUME COMO NULL por ejemplo, codigo postal,
     * en la grilla aparece como 0, pero dicho valor es invalido para la b.d., por lo tanto,
     * se envia NULL a la b.d. . Solo se enviara un codigo postal > 0.
     */
    public static void setParamValue(CallableStatement cs,int paramNro,int paramType,int paramValue) {
        try {
            if ( paramValue <= 0 ) cs.setNull(paramNro,paramType);
            else                      cs.setInt(paramNro,paramValue);
        } catch(SQLException sqle) {
            System.err.println("DataIO.setParamValue()2: Error seteando param: "+paramNro+" "+sqle.getMessage());
        }
    }
    /**
     * Setea el valor del parametro dentro de un CallableStatement, en caso de ser null, utiliza setNull
     * Se utiliza para parametros de tipo Date ( cs.setDate() )
     * OJO! ASUME QUE paramValue es un String que puede ser null que representa una fecha en formato
     * de fechas por defecto DataIO.getDateFmt()
     */
    public static void setParamValueDate(CallableStatement cs,int paramNro,String paramValue) {
        try {
            if ( paramValue == null || paramValue.equals("") ) {
                cs.setNull(paramNro,Types.DATE);
            } else {                      
                // Conversion de fecha String ingresada en formato de fecha por defecto
                // a fecha de tipo java.sql.Date
                SimpleDateFormat fmt = new SimpleDateFormat(getDateFmt());
                java.util.Date f = null;
                try {
                    f = fmt.parse(paramValue);
                    cs.setDate(paramNro,new java.sql.Date(f.getTime()));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "DataIO.setParamValueDate():["+paramValue+"] no es una fecha valida ("+getDateFmt()+")" , "Error en Ingreso de Fecha", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch(SQLException sqle) {
            System.err.println("DataIO.setParamValueDate(): Error seteando param: "+paramNro+" "+sqle.getMessage());
        }
    }

    /** Chequea si la fecha String es una valida acorde con el formato de fecha por defecto */
    public static boolean isDate(String strDate) {
        SimpleDateFormat fmt = new SimpleDateFormat(getDateFmt());
        java.util.Date f = null;
        try {
            f = fmt.parse(strDate);
            return true;
        } catch (ParseException ex) {
        }                
        return false;
    }
    
    /**
     * Encripto texto 
     * 
     */
    public static String encripto(final String msg) {
        StringBuffer sb = new StringBuffer("");
        for(int i = 0;i<msg.length();i++) {
            char encr = (char) (msg.charAt(i)+(char) 4); 
            sb.append(String.format("%02x",new Integer(encr)));
        }
        return sb.toString();
    }
    /**
     * Desencripto texto 
     * 
     */
    public static String desencripto(final String msg) {
        StringBuffer sb = new StringBuffer("");
        for(int i = 0;i<(msg.length()-1);i+=2) {
            int entero = Integer.parseInt(msg.substring(i,i+2),16);
            entero-=4;
            sb.append((char) entero);
        }
        return sb.toString();
    }
    
}