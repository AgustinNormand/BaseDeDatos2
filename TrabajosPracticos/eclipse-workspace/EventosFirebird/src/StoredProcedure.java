/**
 *
 * @author Santiago, Agustin, Rodrigo, G. Cherencio
 */
public class StoredProcedure {
        private String name;
        private Boolean selectable;
        private int nParamIn = 0;   // nro de parametros de entrada
        private int nParamOut = 0;  // nro de parametros de salida
        private String query = null;

   public StoredProcedure(String name){
       this(name,0,0,false);
   }
   public StoredProcedure(String name,Boolean selectable){
       this(name,0,0,selectable);
   }
   public StoredProcedure(String name,int nIn,Boolean selectable){
       this(name,nIn,0,selectable);
   }
   public StoredProcedure(String name,int nIn,int nOut){
       this(name,nIn,nOut,false);
   }
   public StoredProcedure(String name,int nIn,int nOut,Boolean selectable){
       setName(name);
       setParamIn(nIn);
       setParamOut(nOut);
       setSelectable(selectable);
       buildQuery();
   }
   
   public void setName(String n) { name = n; }
   public String getName(){
       return this.name;
   }
   
   
   public void setSelectable(boolean s) { selectable = s; }
   public Boolean isSelectable(){
       return this.selectable;
   }
   
   public int getParamIn() { return nParamIn; }
   public int getParamOut() { return nParamOut; }
   public void setParamIn(int n) { nParamIn = n; }
   public void setParamOut(int n) { nParamOut = n; }
   
   private void buildQuery() {
        boolean first = true;
        //Armamos la llamada al sp
        StringBuffer bcall = new StringBuffer("{call "+getName()+"(");
        for (int i=0;i<getParamIn();i++){
            if ( first ) { first=false;bcall.append("?"); } else bcall.append(",?"); 
        }
        for (int i=0;i<getParamOut();i++){
            bcall.append(",?");   
        }
        bcall.append(")}");
        query = bcall.toString();
   }
   
   public String getQuery() { return query; }
   
   
    @Override
    public String toString() { return getName(); }
    @Override
    public int hashCode() { return getName().hashCode(); }
    @Override
    public boolean equals(Object obj) {
        return ((StoredProcedure) obj).getName().equalsIgnoreCase(getName());
    }
   
}
