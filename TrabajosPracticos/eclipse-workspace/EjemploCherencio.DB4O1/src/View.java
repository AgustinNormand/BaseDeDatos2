import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Write a description of class View here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class View extends JFrame {
    public static final long serialVersionUID = 42L;    
    
    private JTextArea txt = new JTextArea();
    private JPanel pder = new JPanel();
    private JButton btnAlta = new JButton("Alta");
    private JButton btnBaja = new JButton("BajaC");
    private JButton btnBaj2 = new JButton("BajaN");
    private JButton btnBaj3 = new JButton("BajaAll");
    private JButton btnList = new JButton("Listar");
    private JButton btnLis2 = new JButton("ListarNN");
    private JButton btnLis3 = new JButton("ListarGT");
    private JButton btnLis4 = new JButton("ListarGTL");
    
    public View() {
        setLayout(new BorderLayout());
        pder.setLayout(new GridLayout(8,1));
        
        // TextArea no editable
        txt.setEditable(false);
        
        // tooltips
        btnAlta.setToolTipText("Alta de Cliente, no repite codigo");
        btnBaja.setToolTipText("Borra por codigo");
        btnBaj2.setToolTipText("Borra por nombre");
        btnBaj3.setToolTipText("Borra todo!");
        btnList.setToolTipText("Lista todos los clientes");
        btnLis2.setToolTipText("Lista todos los clientes que NO coincidan con el nombre");
        btnLis3.setToolTipText("Lista todos los clientes cuyo codigo sea mayor al ingresado");
        btnLis4.setToolTipText("Lista todos los clientes cuyo codigo sea mayor y like nombre");
        
        pder.add(btnAlta);
        pder.add(btnBaja);
        pder.add(btnBaj2);
        pder.add(btnBaj3);
        pder.add(btnLis2);
        pder.add(btnLis3);
        pder.add(btnLis4);
        pder.add(btnList);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.out.println("fin!");
                Util.close();
                Util.fin();
            }
        });
        
        btnAlta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.insert(new Cliente(getCodigo(),getNombre()));
            }
        });

        btnBaja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.delete(new Cliente(getCodigo()));
            }
        });
        
        btnBaj2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.delete(new Cliente(0,getNombre()));
            }
        });

        btnBaj3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( JOptionPane.showConfirmDialog(null,
"Borra todo?", "Destruye base de datos", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION )
                    Util.deleteAll();
            }
        });

        
        btnLis2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.listNotName(getNombre()).toString());
            }
        });
        
        btnLis3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.listGtCodigo(getCodigo()).toString());
            }
        });

        btnLis4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.listGtCodigoAndLike(getCodigo(),getNombre()).toString());
            }
        });
        
        btnList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.list(new Cliente()).toString());
            }
        });
        
        add(txt,"Center");
        add(pder,"East");
        add(new JLabel("db4o embebido"),"North");
        
        setTitle("Persistiendo Objetos en db4o");
        setSize(400,500);
        setVisible(true);
        
    }

    private int getCodigo() {
        String cod = getString("Codigo (-1):");
        if ( !cod.equals("-1") ) return new Integer(cod).intValue();
        else return 0;
    }
    private String getNombre() {
        String cod = getString("Nombre (null):");
        if ( !cod.equals("null") ) return cod;
        else return null;
    }
    private String getString(String prompt) {
        return JOptionPane.showInputDialog(null,prompt);
    }
}
