 

/**
 * Comando sql a ejecutar
 * 
 * @author G. Cherencio
 * @version 1.0
 */
public class SqlCommand {
    // instance variables - replace the example below with your own
    private String cmd = null;
    private boolean lrun = false;
    private String lerror = null;

    /**
     * Constructor for objects of class SqlCommand
     */
    public SqlCommand() {}
    public SqlCommand(String c) { setCommand(c); }
    // SETTERS
    public void setCommand(String s) { cmd = s; }
    public void setLastError(String s) { lerror = s; }
    public void setLastRun(boolean s) { lrun = s; }
    // GETTERS
    public String getCommand() { return cmd; }
    public String getLastError() { return lerror; }
    public boolean getLastRun() { return lrun; }
    // CUSTOM
    public boolean run(FBCon f) {
        return run(f,true);
    }
    public boolean run(FBCon f,boolean doCommit) {
        String c = getCommand().trim().toUpperCase();
        boolean bdml = c.startsWith("INSERT ") || c.startsWith("UPDATE ") || c.startsWith("DELETE ");
        boolean ret=false;
        if ( bdml ) {
            if ( doCommit ) {
                if ( f.executeQuery(getCommand()) ) if ( f.doCommit() ) ret=true;
            } else {
                if ( f.executeQuery(getCommand()) ) ret=true;
            }
        } else {
            if ( f.doDdl(getCommand()) ) ret=true;
        }
        setLastRun(ret);
        if ( ret ) {
            setLastError(null);
        } else {
            setLastError(f.getErrorMessages());
            f.clearErrorMessages();
        }
        return ret;
    }

    
    @Override
    public String toString() { return getCommand(); }
    @Override
    public int hashCode() { return getCommand().hashCode(); }
    @Override
    public boolean equals(Object obj) {
        return ((SqlCommand) obj).getCommand().equalsIgnoreCase(getCommand());
    }
}
