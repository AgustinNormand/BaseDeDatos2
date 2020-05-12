import java.sql.*;
import java.util.*;
import org.firebirdsql.jdbc.*;
import org.firebirdsql.ds.*;
import java.text.*;
import javax.swing.*;
import java.io.*;
import javax.naming.*;
/**
 * Representa los datos de conexion a una b.d. firebird
 * 
 * @author G. Cherencio 
 * @version 1.0
 */
public class FBCon implements Comparable<FBCon>
{
    // comienza con conexion 0, se incrementa por cada objeto que se crea
    private static int nConn = 0;
    private int n=0;
    private String user;
    private String pwd;
    private String db; // <server>/<port>:<database>
    private String role;
    private String name;
    private java.sql.DatabaseMetaData dbm = null;
    //private org.firebirdsql.pool.FBWrappingDataSource ds = null;
    private FBSimpleDataSource ds = new FBSimpleDataSource();
    private Connection c = null;
    private boolean trsup = false;
    private int timeOut = 10; // 10 segundos de time-out para login
    private static Set<FBCon> set = new TreeSet<FBCon>();
    private StringBuffer msgErr = new StringBuffer();
    // formato de fechas por defecto
    private String dateFmt = "dd/MM/yyyy";
    // Agrego nuevos campos para evitar parsing
    private String server = "";
    private int puerto = 3050;
    private String baseDeDatos = "";

    // ==============================================================
    // constructors
    // ==============================================================
    
    /**
     * Constructor for objects of class FBCon
     */
    public FBCon(){
        this("localhost/3050:/var/lib/firebird/3.0/data/prueba.fdb");
    }
    public FBCon(String database) {
        this(database,"sysdba");
    }
    public FBCon(String database,String userid) {
        this(database,userid,"masterkey");
    }
    public FBCon(String database,String userid,String password) {
        this(database,userid,password,"sysdb");
    }
    public FBCon(String database,String userid,String password,String role) {
        this(database,userid,password,role,"none");
    }
    public FBCon(String database,String userid,String password,String role,String strn) {
        synchronized(this) {  n=++nConn;set.add(this); }
        // http://www.firebirdsql.org/file/documentation/drivers_documentation/java/3.0.0-SNAPSHOT/release_notes.html
        //System.setProperty("org.firebirdsql.jdbc.defaultConnectionEncoding","utf-8");
        setUser(userid);
        setPwd(password);
        //if ( !database.startsWith("//") ) database = new String("//"+database);
        System.out.println("FBCon() database="+database);
        setDb(database);
        setRole(role);
        setName(strn);
        setTimeOut(10);
//System.out.println("FBCon: {"+database+"}{"+userid+"}{"+password+"}{"+role+"}");        
    }
    
    // ==============================================================
    // setters
    // ==============================================================
    public void setUser(String u) { user = u; }
    public void setPwd(String p) { pwd = p; }
    public void setDb(String d) { db = d; }
    public void setRole(String r) { role = r; }
    public void setTimeOut(int n) { timeOut = n; }
    public void setDateFmt(String f) { dateFmt = f; }
    public void setName(String n) { name = n; }
    public void setServer(String s){ server = s; }
    public void setPuerto(int p) { puerto = p; }
    public void setBaseDeDatos(String b) { baseDeDatos = b; }
    /**
     * Setea el valor del parametro dentro de un CallableStatement, en caso de ser null, utiliza setNull
     * Se utiliza para parametros de tipo varchar ( cs.setString() )
     */
    public void setParamValue(CallableStatement cs,int paramNro,int paramType,String paramValue) {
        try {
            if ( paramValue == null ) cs.setNull(paramNro,paramType);
            else                      cs.setString(paramNro,paramValue);
        } catch(SQLException sqle) {
            System.err.println("FBCon.setParamValue(): Error seteando param: "+paramNro+" "+sqle.getMessage());
        }
    }
    /**
     * Setea el valor del parametro dentro de un CallableStatement, en caso de ser null, utiliza setNull
     * Se utiliza para parametros de tipo integer ( cs.setInt() )
     * OJO!! TODO VALOR ENTERO <= 0 SE LO ASUME COMO NULL por ejemplo, codigo postal,
     * en la grilla aparece como 0, pero dicho valor es invalido para la b.d., por lo tanto,
     * se envia NULL a la b.d. . Solo se enviara un codigo postal > 0.
     */
    public void setParamValue(CallableStatement cs,int paramNro,int paramType,int paramValue) {
        try {
            if ( paramValue <= 0 ) cs.setNull(paramNro,paramType);
            else                      cs.setInt(paramNro,paramValue);
        } catch(SQLException sqle) {
            System.err.println("FBCon.setParamValue()2: Error seteando param: "+paramNro+" "+sqle.getMessage());
        }
    }
    /**
     * Setea el valor del parametro dentro de un CallableStatement, en caso de ser null, utiliza setNull
     * Se utiliza para parametros de tipo Date ( cs.setDate() )
     * OJO! ASUME QUE paramValue es un String que puede ser null que representa una fecha en formato
     * de fechas por defecto FBCon.getDateFmt()
     */
    public void setParamValueDate(CallableStatement cs,int paramNro,String paramValue) {
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
                    JOptionPane.showMessageDialog(null, "FBCon.setParamValueDate():["+paramValue+"] no es una fecha valida ("+getDateFmt()+")" , "Error en Ingreso de Fecha", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch(SQLException sqle) {
            System.err.println("FBCon.setParamValueDate(): Error seteando param: "+paramNro+" "+sqle.getMessage());
        }
    }
    
    // ==============================================================
    // getters
    // ==============================================================
    public String getUser() { return user; }
    public String getPwd() { return pwd; }
    public String getDb() { return db; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public int getId() { return n; }
    public int getTimeOut() { return timeOut; }
    public Connection getConnection() { return c; }
    public DatabaseMetaData getDatabaseMetaData() { return dbm; }
    public boolean isTransactionSupported() { return trsup; }
    public String getDateFmt() { return dateFmt; }
    public String getServer() { return server; }
    public int getPuerto() { return puerto; }
    public String getBaseDeDatos() { return baseDeDatos; }
    
    public static FBCon getFBCon(int id) {
        for(FBCon f : set) if ( f.hashCode() == id ) return f;
        return null;
    }
    public static FBCon getFBCon(String sn) {
        for(FBCon f : set) if ( f.getName().equalsIgnoreCase(sn) ) return f;
        return null;
    }
    /**
     * 
     * @return Devuelve los ultimos mensajes de error de sql acumulados en buffer de FBCon. Utilice metodo clearErrorMessage()
     * para borrar estos errores.
     */
    public String getErrorMessages() {
        return msgErr.toString();
    }
    /**
     * 
     * Metodo que que permite la ejecucion de un query de consulta (select) sobre la b.d.
     * el cual devuelve un ResultSet que puede ser recorrido para recuperar cada una de las tuplas devueltas
     * 
     * @param query query sql a ejecutar
     * @return ResultSet con tuplas a recuperar si todo Ok o null en caso de error
     */
    public ResultSet getResultSet(final String query) {
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
     * Devuelve el valor del campo indicado de la primer fila del resultset o bien null
     * en caso de error o de no existir el campo o fila a recuperar
     */
    public String getFirstRow(final String query,final String field) {
        ResultSet rs = getResultSet(query);
        String s = null;
        try {
            if ( rs.next() ) {
                s = rs.getString(field);
            }
            rs.close();
        } catch(SQLException e) {
            doException(e,"Unable to get first row/field from ["+query+","+field+"]");
        }
        return s;
    }
    /**
     * 
     * Metodo que que devuelve -a partir de ResultSet- la cantidad de columnas que posee el mismo
     * No hacer sucesivas llamadas a este metodo, utilizar la version que recibe un ResulSetMetaData
     *      
     * @param rs result set sobre el cual se pretende esta informacion
     * @return cantidad de columnas o -1 en caso de error
     */
    public int getColumnCount(ResultSet rs) {
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
    public int getColumnCount(ResultSetMetaData rsm) {
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
    public String getColumnName(ResultSet rs,final int i) {
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
    public String getColumnName(ResultSetMetaData rsm,final int i) {
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
    public int getColumnType(ResultSetMetaData rsm,final int i) {
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
    public int getColumnType(ResultSet rs,final int i) {
        int type = -1;
        try {
            type = getColumnType(rs.getMetaData(),i);
        } catch (SQLException e) {
            doException(e,"Unable to retrieve ResultSetMetaData for column type("+i+")!");
        }
        return type;
    }
    /**
     * Obtiene el texto de stdout de un comando previamente ejecutado
     */
    public String getStdout(final String shellOutput) {
        StringBuffer sb = new StringBuffer("");
        boolean inStdout = false;
        for(String s : shellOutput.split("\n")) {
            if ( s.equals("stdout") ) {
                inStdout=true;
            } else if ( s.equals("stderr") ) {
                inStdout=false;
            } else if ( inStdout ) {
                sb.append(s+"\n");
            }
        }
        return sb.toString();
    }

    /**
     * Obtiene el texto de stderr de un comando previamente ejecutado
     */
    public String getStderr(final String shellOutput) {
        StringBuffer sb = new StringBuffer("");
        boolean inStderr = false;
        for(String s : shellOutput.split("\n")) {
            if ( s.equals("stdout") ) {
                inStderr=false;
            } else if ( s.equals("stderr") ) {
                inStderr=true;
            } else if ( inStderr ) {
                sb.append(s+"\n");
            }
        }
        return sb.toString();
    }
    
    
    // ==============================================================
    // custom
    // ==============================================================
    /**
     * Crea nuevo usuario en base de datos de seguridad firebird
     * Utiliza gsec y linea de comando
     *      
     * @param adminUser usuario administrador en servidor remoto
     * @param adminRole role de usuario administrador en servidor remoto
     * @param adminPwd contraseña de usuario administrador en servidor remoto
     * @param server  servidor remoto en donde vamos a crear este nuevo usuario
     * @param securityDatabase pathname completo a la base de datos de seguridad dentro del servidor remoto
     * @param newUser userid nuevo a crear en servidor remoto
     * @param adminPwd contraseña de nuevo usuario a crear
     */
    public void addUser(final String adminUser, final String adminRole, final String adminPwd,
        final String server,final String securityDatabase,final String newUser, final String newUserPwd) {
        String comillas = "";
        if ( server.indexOf(" ") != -1 || securityDatabase.indexOf(" ") != -1 ) comillas = "\"";
        String command = "gsec -user "+adminUser+" -password "+adminPwd+" -role "+adminRole+" -database "+comillas+
        server+":"+securityDatabase+comillas+" -add "+newUser+" -pw "+newUserPwd;
//System.out.println(command);
//return;        
        String ret = doShellCommand(command);
//System.out.println("command ["+command+"] returns ["+ret+"]");
    }
    
    public static void clear() { 
//System.out.println("FBCon.clear() inicio");        
        synchronized(set) {
            for(FBCon f : set) f.disconnect();
            nConn = 0;
            set.clear();
        }
//System.out.println("FBCon.clear() fin!");        
    }
    /**
     * 
     * Borrar todos los ultimos errores de SQL
     */
    public void clearErrorMessages() {
        msgErr.delete(0,msgErr.length());
    }
    
    public boolean connect() {
        boolean r = true;
        try {
//System.out.println("connect(): ds");            
            ds = getDs();
//System.out.println("connect(): ds.getConnection()");            
            
            c = ds.getConnection();
            System.out.println("FBCon.connect(): conectado! a "+getDb());

            c.setAutoCommit(false);
//System.out.println("connect(): getMetaData()");            
            dbm = c.getMetaData();
            
            if ( dbm.supportsTransactions() ) trsup = true;
            else trsup = false;
//System.out.println("connect(): fin");            
            
        } catch(java.sql.SQLException e) {
            System.err.println("connect():"+e);
            r=false;
        }
        return r;
    }
    private FBSimpleDataSource getDs() {
        FBSimpleDataSource ds = null;
        try {
            ds = new org.firebirdsql.ds.FBSimpleDataSource();
            ds.setDatabase(getDb());
            ds.setUser(getUser());
            ds.setUserName(getUser());
            ds.setPassword(getPwd());
            ds.setRoleName(getRole());
            ds.setLoginTimeout(getTimeOut());
            ds.setType("PURE_JAVA");
            //ds.setCharSet("utf-8");
            ds.setEncoding("UTF8");
            
        } catch(SQLException sqle) {
            System.err.println("getDs():"+sqle);
            showSQLException(sqle);
        }
        return ds;
    }
    public boolean test() {
        // ver https://github.com/FirebirdSQL/jaybird/wiki/Jaybird-and-Firebird-3
        // idem connect pero sin registrar la conexion y desconectando
        FBSimpleDataSource dds = null;
        Connection cc = null;
        boolean r = true;
        try {
            dds = getDs();
            cc = dds.getConnection();
            cc.close();
        } catch(java.sql.SQLException e) {
            System.err.println("test():"+e);
            showSQLException(e);
            r=false;
        }
        return r;
    }
    
    /**
     * Desencripto texto 
     * 
     */
    public static String desencripto(final String msg) {
        StringBuffer sb = new StringBuffer("");
        int entero = 0;
        for(int i = 0;i<(msg.length()-1);i+=2) {
            try {
                entero = Integer.parseInt(msg.substring(i,i+2),16);
                entero-=4;
                sb.append((char) entero);
            } catch(NumberFormatException nfe) {
                // en caso de error, devuelvo una copia del mismo String
                sb = new StringBuffer(msg);
                break;
            }
        }
        return sb.toString();
    }
    
    public void disconnect() {
        try {
            if ( c != null ) {
                c.close();
                //ds.shutdown();
                dbm=null;
                ds=null;
                c=null;
            }
//System.out.println ("FBCon.disconnect(): Connection closed!");
        } catch (java.sql.SQLException e) {
            System.err.println("disconnect():"+e);
        }        
    }
    /**
     * Ejecuta el comando ddl indicado
     * @param ddlCommand comando ddl firebird a ejecutar
     * @return true si pudo ejecutar el comando ok, sino devuelve false
     */
    public boolean doDdl(final String ddlCommand) {
        boolean r = true;
        try {
            Statement st = c.createStatement();
            st.execute(ddlCommand);
            st.close();
            doCommit();
        } catch(SQLException e) {
            doException(e,"Unable to execute ddl command ["+ddlCommand+"]");
            r=false;
        }        
        return r;
    }
    
    /**
     * 
     * Metodo que imprime stack de ejecucion + mensaje (msg) + mensajes de error sql en stderr
     * 
     * @param e excepcion sql que ha ocurrido
     * @param msg mensaje de error aplicativo que se pretende anexar a los errores sql
     */
    public void doException(SQLException e,final String msg) {
        e.printStackTrace();
        System.err.println (msg);
        msgErr.append(msg+"\n");
        showSQLException(e);
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
    
    public ResultSet executePro(StoredProcedure procedure,CallableStatement cs) {
        ResultSet rs = null;
        try {
            rs = cs.executeQuery();
        } catch(SQLException e) {
            doException(e,"FBCon.executePro():Unable to execute Procedure "+procedure);
        }
        return rs;
    }
    /**
     * 
     * Metodo que permite la ejecucion de un query de actualizacion (insert,update,delete) sobre la b.d.
     * <b>No realiza commit, esto debe hacerse cuando corresponda llamando al metodo doCommit()</b>
     * 
     * @param query query sql a ejecutar
     * @return true si se ejecuto ok, false en caso de error
     */
    public boolean executeQuery(final String query) {
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
    public synchronized boolean doCommit() {
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
     * Ejecuta comando en Shell del sistema operativo
     * 
     * @param command comando shell a ejecutar
     * @return salida del comando o null en caso de error
     */
    public String doShellCommand(String command) {
        StringBuffer output = new StringBuffer();
        String ret = null;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            output.append("stdout\n");
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            output.append("stderr\n");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = br.readLine()) != null) {
                output.append(line + "\n");
            }
            ret = output.toString();
            int exitVal = p.waitFor();
            reader.close();
            br.close();
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    /** Chequea si la fecha String es una valida acorde con el formato de fecha por defecto */
    public boolean isDate(String strDate) {
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
     * Lista los usuarios en base de datos de seguridad firebird
     * Utiliza gsec y linea de comando
     *      
     * @param adminUser usuario administrador en servidor remoto
     * @param adminRole role de usuario administrador en servidor remoto
     * @param adminPwd contraseña de usuario administrador en servidor remoto
     * @param server  servidor remoto en donde vamos a crear este nuevo usuario
     * @param securityDatabase pathname completo a la base de datos de seguridad dentro del servidor remoto
     * @return lista de usuarios en forma de Strings
     */
    public List<String> listUser(final String adminUser, final String adminRole, final String adminPwd,
        final String server,final String securityDatabase) {
        List<String> lst = new ArrayList<String>();
        String comillas = "";
        if ( server.indexOf(" ") != -1 || securityDatabase.indexOf(" ") != -1 ) comillas = "\"";
        String command = "gsec -user "+adminUser+" -password "+adminPwd+" -role "+adminRole+" -database "+comillas+server+":"+securityDatabase+comillas+" -display";
        
//System.out.println(command);
//return;        
        String ret = doShellCommand(command);
//System.out.println(ret);
        String sout = getStdout(ret);
//System.out.println(sout);
        
        for(String s : sout.split("\n")) {
            if ( !(s.startsWith(" ") || s.startsWith("-") ) ) {
//System.out.println("["+s+"] espacio en posicion "+s.indexOf(" "));                
                String uname = s.substring(0,s.indexOf(" "));
                lst.add(uname);
            }
        }
//System.out.println("command ["+command+"] returns ["+ret+"]");
        return lst;
    }
    
    public CallableStatement prepareSelPro(StoredProcedure procedure) {
        CallableStatement cs = null;
        try {
            cs = getConnection().prepareCall(procedure.getQuery());
            FirebirdCallableStatement fbCs = (FirebirdCallableStatement) cs;
            fbCs.setSelectableProcedure(true);
        } catch(SQLException e) {
            doException(e,"FBCon.prepareSelPro():Unable to prepare SProcedure "+procedure);
            cs = null;
        }
        return cs;
    }
    
    public CallableStatement prepareExePro(StoredProcedure procedure) {
        CallableStatement cs = null;
        try {
            cs = getConnection().prepareCall(procedure.getQuery());
        } catch(SQLException e) {
            doException(e,"FBCon.prepareExePro():Unable to prepare Procedure "+procedure);
        }
        return cs;
    }

    /**
     * 
     * Metodo que muestra por stderr toda la informacion de un error ocasionado por una excepcion sql
     * dentro de estos mensajes deberian estar los correspondientes a violaciones de constraints declarados
     * en la b.d. y excepciones firebird lanzadas desde triggers y store procedures
     * 
     * @param e objeto SQLException que representa el error sql
     */
    private void showSQLException(SQLException e) {
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
            msgErr.append(msg0+"\n\t"+msg1+"\n\t"+msg2+"\n\t"+msg3+"\n");
            next = next.getNextException ();
        }
    }
    /**
     * Devuelve el nombre de la clave primaria o null en caso de que no exista nombre del primary key constraint
     */
    public String getPKName(final String table) {
        ResultSet rs = null;
        String pkname = null;
        try {
            rs = dbm.getPrimaryKeys(null,null,table);
            if(rs.next()) {
                pkname = rs.getString("PK_NAME");
            }
            rs.close();
        } catch(java.sql.SQLException e) {
            System.err.println("FBCon.getPkColumns():"+e);
        }
        return pkname;
    }
    public List<String> getPKColumns(final String table) {
/*
Each primary key column description has the following columns:

TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
COLUMN_NAME String => column name
KEY_SEQ short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key).
PK_NAME String => primary key name (may be null)
*/
        List<String> lst = new ArrayList<String>();
        Map<Integer,String> mpk = new HashMap<Integer,String>();
        ResultSet rs = null;
//System.out.println("**************PK Tabla "+table);
        try {
            rs = dbm.getPrimaryKeys(null,null,table);
            while(rs.next()) {
                mpk.put(rs.getInt("KEY_SEQ"),rs.getString("COLUMN_NAME"));
//System.out.println(rs.getString("TABLE_NAME")+","+rs.getString("COLUMN_NAME")+","+rs.getString("KEY_SEQ")+","+rs.getString("PK_NAME"));
            }
            rs.close();
            // RECORRO EL MAP EN ORDEN PARA GENERAR ARRAYLIST
            int i=1;
            String value=null;
            while((value=mpk.get(new Integer(i))) != null) {
                lst.add((i-1),value);
                i++;
            }
        } catch(java.sql.SQLException e) {
            System.err.println("FBCon.getPkColumns():"+e);
        }
//System.out.println("**************");
        return lst;
    }
    /**
     * Similar a getPKColumns(), pero en este caso devuelve la pk de la tabla indicada
     * con formato (col1,col2,..colN) en el orden indicado
     */
    public String getPK(final String table) {
        List<String> lst = getPKColumns(table);
        StringBuffer sb = new StringBuffer("(");
        int i=0;
        for(String s : lst) {
            if ( i > 0 ) sb.append(",");
            sb.append(s);
            i++;
        }
        sb.append(")");
        return sb.toString();
    }
    
    public Set<String> getFKNames(final String table) {
        Set<String> sfk = new HashSet<String>();
        ResultSet rs = null;
//System.out.println("**************FKs Tabla "+table);
        try {
            rs = dbm.getImportedKeys(null,null,table);
            while(rs.next()) {
                //mpk.put(rs.getInt("KEY_SEQ"),rs.getString("COLUMN_NAME"));
                //StringBuffer sb = new StringBuffer(rs.getString("COLUMN_NAME")+" "+rs.getString("TYPE_NAME"));
//System.out.println(rs.getString("FKTABLE_NAME")+","+rs.getString("FKCOLUMN_NAME")+","+rs.getString("KEY_SEQ")+","+rs.getString("FK_NAME")+","+rs.getString("PK_NAME"));
                sfk.add(rs.getString("FK_NAME"));
            }            
            rs.close();
        } catch(java.sql.SQLException e) {
            System.err.println("FBCon.getFKDef():"+e);
        }
//System.out.println("**************");
        return sfk;
    }
    public Map<Integer,String> getFKDefs(final String table) {
        int i=1;
        Map<Integer,String> fks = new HashMap<Integer,String>();
        for(String fk : getFKNames(table)) {
            fks.put(i,getFKDef(table,fk));
            i++;
        }
        return fks;
    }
    public String getFKDef(final String table,final String fk) {
/*
ResultSet getExportedKeys(String catalog,
                        String schema,
                        String table)
                          throws SQLException
Retrieves a description of the foreign key columns that reference the given table's primary key columns (the foreign keys exported by a table). They are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ.
Each foreign key column description has the following columns:

PKTABLE_CAT String => primary key table catalog (may be null)
PKTABLE_SCHEM String => primary key table schema (may be null)
PKTABLE_NAME String => primary key table name
PKCOLUMN_NAME String => primary key column name
FKTABLE_CAT String => foreign key table catalog (may be null) being exported (may be null)
FKTABLE_SCHEM String => foreign key table schema (may be null) being exported (may be null)
FKTABLE_NAME String => foreign key table name being exported
FKCOLUMN_NAME String => foreign key column name being exported
KEY_SEQ short => sequence number within foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key).
UPDATE_RULE short => What happens to foreign key when primary is updated:
importedNoAction - do not allow update of primary key if it has been imported
importedKeyCascade - change imported key to agree with primary key update
importedKeySetNull - change imported key to NULL if its primary key has been updated
importedKeySetDefault - change imported key to default values if its primary key has been updated
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
DELETE_RULE short => What happens to the foreign key when primary is deleted.
importedKeyNoAction - do not allow delete of primary key if it has been imported
importedKeyCascade - delete rows that import a deleted key
importedKeySetNull - change imported key to NULL if its primary key has been deleted
importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
importedKeySetDefault - change imported key to default if its primary key has been deleted
FK_NAME String => foreign key name (may be null)
PK_NAME String => primary key name (may be null)
DEFERRABILITY short => can the evaluation of foreign key constraints be deferred until commit
importedKeyInitiallyDeferred - see SQL92 for definition
importedKeyInitiallyImmediate - see SQL92 for definition
importedKeyNotDeferrable - see SQL92 for definition
 */        
        ResultSet rs = null;
        int i=0;
        StringBuffer sbfk = new StringBuffer("(");
        StringBuffer sbfk0 = new StringBuffer("CONSTRAINT ");
        StringBuffer sbfk1 = new StringBuffer();
//System.out.println("**************FK Tabla "+table);
        try {
            rs = dbm.getImportedKeys(null,null,table);
            while(rs.next()) {
                //mpk.put(rs.getInt("KEY_SEQ"),rs.getString("COLUMN_NAME"));
                //StringBuffer sb = new StringBuffer(rs.getString("COLUMN_NAME")+" "+rs.getString("TYPE_NAME"));
//System.out.println(rs.getString("FKTABLE_NAME")+","+rs.getString("FKCOLUMN_NAME")+","+rs.getString("KEY_SEQ")+","+rs.getString("FK_NAME")+","+rs.getString("PK_NAME"));
                if ( rs.getString("FK_NAME").equalsIgnoreCase(fk) ) {
                    if ( i == 0 ) {
                        sbfk0.append(rs.getString("FK_NAME")+" FOREIGN KEY ");
                        sbfk1.append(rs.getString("PKTABLE_NAME"));
                    }
                    if ( i > 0 ) sbfk.append(",");
                    sbfk.append(rs.getString("FKCOLUMN_NAME"));
                    i++;
                }
            }
            rs.close();
            sbfk.append(")");
            sbfk0.append(sbfk.toString()+" REFERENCES "+sbfk1.toString());
        } catch(java.sql.SQLException e) {
            System.err.println("FBCon.getFKDef():"+e);
        }
//System.out.println("**************");
        return sbfk0.toString();
    }
    /**
     * Obtiene la definicion de cada columna de la tabla
     * a partir de la columna 1 a la N
     */
    public Map<Integer,String> getColumnDef(final String table) {
/*
ResultSet getColumns(String catalog,
                   String schemaPattern,
                   String tableNamePattern,
                   String columnNamePattern)
                     throws SQLException

Retrieves a description of table columns available in the specified catalog.
Only column descriptions matching the catalog, schema, table and column name criteria are returned. They are ordered by TABLE_CAT,TABLE_SCHEM, TABLE_NAME, and ORDINAL_POSITION.

Each column description has the following columns:

TABLE_CAT String => table catalog (may be null)
TABLE_SCHEM String => table schema (may be null)
TABLE_NAME String => table name
COLUMN_NAME String => column name
DATA_TYPE int => SQL type from java.sql.Types
TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
COLUMN_SIZE int => column size.
BUFFER_LENGTH is not used.
DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
NUM_PREC_RADIX int => Radix (typically either 10 or 2)
NULLABLE int => is NULL allowed.
columnNoNulls - might not allow NULL values
columnNullable - definitely allows NULL values
columnNullableUnknown - nullability unknown
REMARKS String => comment describing column (may be null)
COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
SQL_DATA_TYPE int => unused
SQL_DATETIME_SUB int => unused
CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
ORDINAL_POSITION int => index of column in table (starting at 1)
IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
YES --- if the column can include NULLs
NO --- if the column cannot include NULLs
empty string --- if the nullability for the column is unknown
SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
YES --- if the column is auto incremented
NO --- if the column is not auto incremented
empty string --- if it cannot be determined whether the column is auto incremented
IS_GENERATEDCOLUMN String => Indicates whether this is a generated column
YES --- if this a generated column
NO --- if this not a generated column
empty string --- if it cannot be determined whether this is a generated column
The COLUMN_SIZE column specifies the column size for the given column. For numeric data, this is the maximum precision. For character data, this is the length in characters. For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes. For the ROWID datatype, this is the length in bytes. Null is returned for data types where the column size is not applicable.
 */        
        Map<Integer,String> mpk = new HashMap<Integer,String>();
        ResultSet rs = null;
//System.out.println("**************Columns Tabla "+table);
        try {
            rs = dbm.getColumns(null,null,table,null);
            while(rs.next()) {
                //mpk.put(rs.getInt("KEY_SEQ"),rs.getString("COLUMN_NAME"));
                StringBuffer sb = new StringBuffer(rs.getString("COLUMN_NAME")+" "+rs.getString("TYPE_NAME"));
//System.out.println("col nro="+rs.getString("ORDINAL_POSITION")+","+rs.getString("COLUMN_NAME")+","+rs.getString("TYPE_NAME")+","+
//    rs.getString("COLUMN_SIZE")+",decimal="+rs.getString("DECIMAL_DIGITS")+",null?="+rs.getString("IS_NULLABLE")+
//    ",default="+rs.getString("COLUMN_DEF"));
                if ( isMember(rs.getString("TYPE_NAME"),new String[] {"SMALLINT","INTEGER","FLOAT","DOUBLE PRECISION","DATE","TIME","TIMESTAMP"}) ) {
                    sb.append(" ");
                } else if (isMember(rs.getString("TYPE_NAME"),new String[] {"DECIMAL","NUMERIC"})) {
                    sb.append("("+rs.getString("COLUMN_SIZE")+","+rs.getString("DECIMAL_DIGITS")+") ");
                } else if (isMember(rs.getString("TYPE_NAME"),new String[] {"CHAR","CHARACTER","CHARACTER VARYING","VARCHAR","NCHAR","NATIONAL CHARACTER","NATIONAL CHAR"})) {
                    sb.append("("+rs.getString("COLUMN_SIZE")+") ");
                }
                if ( rs.getString("COLUMN_DEF") != null ) {
                    sb.append("DEFAULT "+rs.getString("COLUMN_DEF")+" ");
                }
                if ( rs.getString("IS_NULLABLE").equalsIgnoreCase("NO") ) {
                    sb.append("NOT NULL ");
                }
//System.out.println("guardo: "+rs.getInt("ORDINAL_POSITION")+","+sb.toString().trim());                
                mpk.put(rs.getInt("ORDINAL_POSITION"),sb.toString().trim());
            }
            rs.close();
        } catch(java.sql.SQLException e) {
            System.err.println("FBCon.getColumnDef():"+e);
        }
//System.out.println("**************");
        return mpk;
    }
    
    public String getDdl(final String table) {
        StringBuffer sb = new StringBuffer("CREATE TABLE "+table+" (\n");
        int i=1;
        Map<Integer,String> mcol = getColumnDef(table);
        Map<Integer,String> fks = getFKDefs(table);
        // agrego definicion de columnas
        String scol=null;
        while((scol=mcol.get(i)) != null) {
            if ( i > 1 ) sb.append(",\n");
            sb.append(scol);
            i++;
        }
        // agrego pk's
        String pkconstraint = getPKName(table);
        if ( pkconstraint != null ) {
            sb.append(",\n");
            sb.append("CONSTRAINT "+pkconstraint+" PRIMARY KEY ");
        } else {
            sb.append(",\nPRIMARY KEY ");
        }
        sb.append(getPK(table));
        // agrego fk's
        if ( fks.size() > 0 ) {
            sb.append(",\n");
            i=1;
            while((scol=fks.get(i)) != null) {
                if ( i > 1 ) sb.append(",\n");
                sb.append(scol);
                i++;
            }
        }
        sb.append("\n)");
//System.out.println("FBCon.getDdl("+table+"):"+sb.toString());        
        return sb.toString();
    }
    // override object class
    @Override
    public boolean equals(Object obj) {
        FBCon c = (FBCon) obj;
        return ( c.getDb().equals(getDb()) && 
            c.getPwd().equals(getPwd()) &&
            c.getUser().equals(getUser()) );
    }
    
    @Override
    public int hashCode() {
        return n;
    }

    @Override
    public String toString() {
        return "["+getDb()+","+getUser()+","+getPwd()+"]";
    }
    
    // Comparable interface
    public int compareTo(FBCon o) {
        return getId()-o.getId();
    }

    // verifica si un elemento se encuentra dentro del arreglo
    private boolean isMember(final String item,final String[] array) {
        for(String s : array) if ( s.equalsIgnoreCase(item) ) return true;
        return false;
    }
}
