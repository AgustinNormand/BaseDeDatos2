import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import javax.persistence.*;
/**
 * Clase Model - Controller que implementa table model para realizar abm de objetos de tipo Entidad Cliente a persistidos 
 * utilizando JPA. Es una clase model especifica que solo sirve para hacer abm de objetos de tipo Cliente
 * 
 * @author G.Cherencio 
 * @version 1.0
 */
public class ClienteModel extends AbstractTableModel implements KeyListener
{
    // instance variables - replace the example below with your own
    private List<Cliente> lista;
    private List<Cliente> listaAlta = new ArrayList<Cliente>();
    private String columnas[] = {"Codigo","Nombre","Direccion","Postal","TE"};
    private Class clase_columnas[] = { Integer.class, String.class, String.class, Integer.class, String.class };
    private EntityTransaction emt;
    private EntityManager em;
    private boolean modoAlta = false;
    private JTable tabla = null;

    /**
     * Constructor for objects of class ClienteModel
     */
    public ClienteModel(List<Cliente> c,EntityManager e,JTable t)
    {
        // initialise instance variables
        lista = c;
        em = e;
        emt = em.getTransaction();
        tabla = t;
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
        List<Cliente> l = (!modoAlta) ? lista : listaAlta;
        switch(col) {
            case 0:
                 return l.get(row).getCodigo();
            case 1:
                 return l.get(row).getNombre();
            case 2:
                 return l.get(row).getDirec();
            case 3:
                 return l.get(row).getPostal();
            case 4:
                 return l.get(row).getTel();
        }
        return null;
    }
    
    public boolean isCellEditable(int row, int col){ 
        if ( !modoAlta && (col == 0 || row > lista.size()) ) return false;
        if ( modoAlta && row > 0 ) return false;
        return true;
    }
    
    public void setValueAt(Object value, int row, int col) {
        if ( row > lista.size() ) return;
        if ( !modoAlta && col == 0 ) return;
        List<Cliente> l = (!modoAlta) ? lista : listaAlta;
        if ( !modoAlta ) emt.begin();
        switch(col) {
            case 0:
                 l.get(row).setCodigo((Integer) value);
                 break;
            case 1:
                 l.get(row).setNombre((String) value);
                 break;
            case 2:
                 l.get(row).setDirec((String) value);
                 break;
            case 3:
                 l.get(row).setPostal((Integer) value);
                 break;
            case 4:
                 l.get(row).setTel((String) value);
                 break;
        }
        if ( !modoAlta ) emt.commit();
        fireTableCellUpdated(row, col);
        System.out.println("cambiar valor: " + value + " en fila=" + row + " col=" + col);
    }
    public Class getColumnClass(int c) { return clase_columnas[c]; }
    
///////////////////////////////////////////
// KeyListener interface
///////////////////////////////////////////
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        int rta,fila;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_INSERT: // pulso Ins, hago alta
               if ( !modoAlta ) {
                   rta = JOptionPane.showConfirmDialog(null,"¿Agrega Nuevo Registro?"); // yes=0,no=1,cancel=2            
                   if ( rta == 0 ) {
                        modoAlta=true;
                        if (listaAlta.size() > 0) listaAlta.remove(0);
                        listaAlta.add(new Cliente());
                        fireTableDataChanged();
                   }
                } else {
                   rta = JOptionPane.showConfirmDialog(null,"¿Confirma Nuevo Registro?");
                   switch(rta) {
                       case 0:
                           // persisto listaAlta, agrego listaAlta a lista
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
                    // remover jpa, remover lista
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
    public void keyReleased(KeyEvent e) {}
}
