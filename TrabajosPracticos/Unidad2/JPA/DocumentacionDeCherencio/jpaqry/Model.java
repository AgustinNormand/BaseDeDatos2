import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import javax.persistence.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
/**
 * Clase Model - Controller que implementa table model para realizar abm de objetos de tipo Entidad a persistidos 
 * utilizando JPA. Utiliza reflection para determinar en runtime los datos requeridos para informar a JTable acerca
 * de las acciones a realizar
 * 
 * @author G.Cherencio 
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class Model<T> extends AbstractTableModel implements KeyListener
{
    // instance variables - replace the example below with your own
    private List<T> lista;
    private List<T> listaAlta = new ArrayList<T>();
    private String columnas[];
    private Class clase_columnas[];
    private boolean pk_columnas[];
    private Method getters[];
    private Method setters[];
    private EntityTransaction emt;
    private EntityManager em;
    private boolean modoAlta = false;
    private JTable tabla = null;
    // identificacion de la clase generica actual
    private Class listaClass;
    private T miobj;
    private final String nombreGenerico;

    /**
     * Constructor for objects of class ClienteModel
     */
    public Model(final String nomGenerico,List<T> c,EntityManager e,JTable t)
    {
        // initialise instance variables
        lista = c;
        em = e;
        emt = em.getTransaction();
        tabla = t;
        miobj = null;
        nombreGenerico = nomGenerico;
        try {
            miobj = (T) Class.forName(nombreGenerico).newInstance();
            System.out.println("Model:cree objeto de tipo:"+miobj.getClass().getName());
        } catch(ClassNotFoundException ccnf) {
            System.err.println("Model:La clase ["+nombreGenerico+"] no existe!:\n"+ccnf.getMessage());
        } catch(Exception x) {
            System.err.println("Model:Exception creando objeto de tipo ["+nombreGenerico+"]:\n"+x.getMessage());
        }
        // si no pude crear una instancia generica y la lista esta vacia --> estoy en problemas, no puedo continuar!
        if ( miobj == null ) {
            if ( lista != null ) {
                if ( lista.size() > 0 ) {
                    miobj = lista.get(0);
                    if ( miobj == null ) {
                        System.err.println("Model:No puede crear instancia de ["+nombreGenerico+"] y la lista contiene null!");
                        System.exit(-3);
                    }
                } else {
                    System.err.println("Model:No puede crear instancia de ["+nombreGenerico+"] y la lista esta vacia!");
                    System.exit(-2);
                }
            } else {
                System.err.println("Model:No puede crear instancia de ["+nombreGenerico+"] y la lista no esta instanciada!");
                System.exit(-1);
            }
        }
        listaClass = miobj.getClass();
        int ncols = listaClass.getDeclaredFields().length;
        int i=0;
        columnas = new String[ncols];
        clase_columnas = new Class[ncols];
        pk_columnas = new boolean[ncols];
        getters = new Method[ncols];
        setters = new Method[ncols];
        for(Field f : listaClass.getDeclaredFields()) { // item 0
            System.out.println("Atributo: "+f.getName()+" clase:"+f.getType().getName());
            // obtengo nombre por defecto de la columna
            columnas[i] = f.getName();
            // obtengo clase de la columna i
            String tipo = f.getType().getName();
            if ( tipo.equals("byte") ) clase_columnas[i] = Byte.class;
            else if ( tipo.equals("short") ) clase_columnas[i] = Short.class;
            else if ( tipo.equals("boolean") ) clase_columnas[i] = Boolean.class;
            else if ( tipo.equals("int") ) clase_columnas[i] = Integer.class;
            else if ( tipo.equals("long") ) clase_columnas[i] = Long.class;
            else if ( tipo.equals("float") ) clase_columnas[i] = Float.class;
            else if ( tipo.equals("double") ) clase_columnas[i] = Double.class;
            else if ( tipo.equals("char") ) clase_columnas[i] = Character.class;
            else clase_columnas[i] = f.getType();
            // determino si la columna es clave primaria
            pk_columnas[i]=false;
            for(Annotation a:f.getAnnotations()) {
                System.out.println("Model: campo="+f.getName()+" anotacion="+a);
                if ( a.toString().startsWith("@javax.persistence.Id(") ) { pk_columnas[i]=true; }
                if ( a.toString().startsWith("@Columna(") ) { Columna aa = (Columna ) a;columnas[i]=aa.nombre(); }
            }
            // instancio metodos getters y setters de la columna i
            getters[i] = null;
            setters[i] = null;
            for(Method m:listaClass.getMethods()) {
                if ( m.getName().equalsIgnoreCase("get"+f.getName()) ) getters[i] = m;
                else if ( m.getName().equalsIgnoreCase("set"+f.getName()) ) setters[i] = m;
            }
            i++;
        }

    }

///////////////////////////////////////////
// AbstractTableModel
///////////////////////////////////////////

    public String getColumnName(int col) { return columnas[col]; }
    public int getRowCount() {
        return (!modoAlta) ? lista.size() : 1;
    }
    public int getColumnCount() { return columnas.length; }
    public Object getValueAt(int row, int col) { 
        if ( !modoAlta && row > lista.size() ) return null;
        if ( modoAlta && row > 0 ) return null;
        List<T> l = (!modoAlta) ? lista : listaAlta;
        try {
            if ( getters[col] != null ) return getters[col].invoke(l.get(row));
        } catch(Exception e) {
            System.err.println("Model:Exception ejecutando getter de columna"+col+":\n"+e.getMessage());
        }
        return null;
    }
    
    public boolean isCellEditable(int row, int col){ 
        if ( !modoAlta && (pk_columnas[col] || row > lista.size()) ) return false;
        if ( modoAlta && row > 0 ) return false;
        return true;
    }
    
    public void setValueAt(Object value, int row, int col) {
        if ( row > lista.size() ) return;
        if ( !modoAlta && col == 0 ) return;
        List<T> l = (!modoAlta) ? lista : listaAlta;
        if ( !modoAlta ) emt.begin();
        try {
            if ( setters[col] != null ) setters[col].invoke(l.get(row),value);
        } catch(Exception e) {
            System.err.println("Model:Exception ejecutando setter de columna"+col+":\n"+e.getMessage());
        }
        if ( !modoAlta ) emt.commit();
        fireTableCellUpdated(row, col);
    }
    public Class getColumnClass(int c) { return clase_columnas[c]; }
    
///////////////////////////////////////////
// KeyListener interface
///////////////////////////////////////////
    public void keyTyped(KeyEvent e) {}  // no hago nada, este evento no me interesa
    public void keyPressed(KeyEvent e) {
        int rta,fila;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_INSERT: // pulso Ins, hago alta
               if ( !modoAlta ) { // si no estoy en modo alta...
                   rta = JOptionPane.showConfirmDialog(null,"¿Agrega Nuevo Registro?"); // yes=0,no=1,cancel=2            
                   if ( rta == 0 ) {
                        modoAlta=true;
                        if (listaAlta.size() > 0) listaAlta.remove(0);
                        try {
                            listaAlta.add((T) listaClass.newInstance());
                            fireTableDataChanged();
                        } catch(Exception ex) {
                            System.err.println("ClienteModel:Exception creando instancia generica:"+ex.getMessage());
                            JOptionPane.showMessageDialog(null,"Error(E) Creando Instancia Generica:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                            modoAlta=false;
                        }
                   }
                } else {  // ya estoy en modo alta, entonces pretende confirmar el registro...
                   rta = JOptionPane.showConfirmDialog(null,"¿Confirma Nuevo Registro?");
                   switch(rta) {
                       case 0:
                           // persisto listaAlta, agrego listaAlta a lista, notifico el cambio
                           try {
                               emt.begin();
                               em.persist(listaAlta.get(0));
                               emt.commit();
                               modoAlta=false;
                               lista.add(listaAlta.get(0));
                               fireTableDataChanged();
                           } catch(RuntimeException re) {
                               System.err.println("ClienteModel:RuntimeException persistiendo Cliente:"+re.getMessage());
                               JOptionPane.showMessageDialog(null,"Error(RT) Confirmando Alta Cliente:\n"+re.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                           } catch(Exception ex) {
                               System.err.println("ClienteModel:Exception persistiendo Cliente:"+ex.getMessage());
                               JOptionPane.showMessageDialog(null,"Error(E) Confirmando Alta Cliente:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                           }
                           break;
                       case 1:
                           break;
                       case 2:
                           modoAlta=false;
                           fireTableDataChanged();
                           break;
                   }
                }
                break;
            case KeyEvent.VK_DELETE: // pulso Del, hago baja
                fila=tabla.getSelectedRow();
                if ( modoAlta || fila == -1 ) break;
                rta = JOptionPane.showConfirmDialog(null,"¿Borra el Registro Seleccionado?");
                if ( rta == 0 ) {
                    // remover de jpa, remover de lista, notificar delete
                    try {
                        emt.begin();
                        em.remove(lista.get(fila));
                        emt.commit();
                        lista.remove(fila);
                        fireTableRowsDeleted(fila,fila);
                    } catch(RuntimeException re) {
                        System.err.println("ClienteModel:RuntimeException borrando Cliente:"+re.getMessage());
                        JOptionPane.showMessageDialog(null,"Error(RT) Borrando Cliente:\n"+re.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    } catch(Exception ex) {
                        System.err.println("ClienteModel:Exception borrando Cliente:"+ex.getMessage());
                        JOptionPane.showMessageDialog(null,"Error(E) Borrando Cliente:\n"+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
        }
    }
    public void keyReleased(KeyEvent e) {}  // no hago nada, este evento no me interesa
}
