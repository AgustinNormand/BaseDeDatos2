import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
/**
 * Write a description of class View here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class View extends JFrame {
    public static final long serialVersionUID = 42L;
    
    private JList<Factura> lstf = new JList<Factura>(new DefaultListModel<Factura>());
    private JList<Detalle> lstd = new JList<Detalle>(new DefaultListModel<Detalle>());
    private JPanel pder = new JPanel();
    private JPanel pinf = new JPanel();
    private JPanel pinf1 = new JPanel();
    private JPanel pinf2 = new JPanel();
    private JPanel pcent1 = new JPanel();
    private JPanel pcent2 = new JPanel();
    private JPanel pcent = new JPanel();
    private JButton btnAlta = new JButton("+Factura");
    private JButton btnBaja = new JButton("-Factura");
    private JButton btnBaj2 = new JButton("+Detalle");
    private JButton btnBaj3 = new JButton("-Detalle");
    private JButton btnList = new JButton("Listar");
    private JButton btnLis2 = new JButton("ListarF");
    private JButton btnLis3 = new JButton("ListarP");
    private JButton btnLis4 = new JButton("ListarNP");
    
    private JButton btnAlCl = new JButton("+");
    private JButton btnBaCl = new JButton("-");
    private JButton btnAlPr = new JButton("+");
    private JButton btnBaPr = new JButton("-");
    private JTextArea txt = new JTextArea();
    
    private JComboBox<Cliente> cmbcli = new JComboBox<Cliente>();
    private JComboBox<Producto> cmbpro = new JComboBox<Producto>();
    
    private Cliente selectedCliente = null;
    private Factura selectedFactura = null;
    private Detalle selectedDetalle = null;
    private Producto selectedProducto = null;
    
    public View() {
        setLayout(new BorderLayout());
        pder.setLayout(new GridLayout(8,1));
        pcent1.setLayout(new GridLayout(1,2));
        pcent2.setLayout(new GridLayout(1,1));
        pcent.setLayout(new GridLayout(2,1));
        
        // tooltips
        btnAlta.setToolTipText("Nueva Factura del cliente seleccionado");
        btnBaja.setToolTipText("Borro Factura Seleccionada");
        btnBaj2.setToolTipText("Nueva linea de detalle de factura seleccionada");
        btnBaj3.setToolTipText("Borra linea de detalle seleccionada");
        btnList.setToolTipText("Lista todos los Clientes");
        btnLis2.setToolTipText("Lista por Numero de Factura");
        btnLis3.setToolTipText("Lista por Codigo de Producto");
        btnLis4.setToolTipText("Lista por Nombre de Producto");
        
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
                Cliente c = (Cliente) cmbcli.getSelectedItem();
                if ( c != null ) {
                    Factura f = new Factura(getInt("Nro:"),getDouble("Monto:"));
                    c.add(f);
                    Util.getDb().store(c);
                    Util.getDb().store(f);
                    Util.getDb().commit();
                    loadListFac();
                }
            }
        });

        btnBaja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( selectedCliente != null && selectedFactura != null ) {
                    // borro detalle de b.d. y de memoria
                    try {
                        for(Detalle d : selectedFactura.getDetalles()) {
                            Util.getDb().delete(d);
                            selectedFactura.remove(d);
                        }
                    } catch(Exception ex) {}
                    // borro detalle de memoria
                    
                    // borro factura de b.d.
                    Util.getDb().delete(selectedFactura);
                    // borro factura de memoria
                    selectedCliente.remove(selectedFactura);
                    // actualizo cliente en b.d.
                    Util.getDb().store(selectedCliente);
                    Util.getDb().commit();
                    loadListFac();loadListDet();
                }
            }
        });
        
        btnBaj2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cliente c = (Cliente) cmbcli.getSelectedItem();
                Factura f = lstf.getSelectedValue();   
                Producto p = (Producto) cmbpro.getSelectedItem();
                if ( c != null && f != null && p != null ){
                    Detalle d = new Detalle(p,getInt("Cantidad:"),getDouble("Precio:"));
                    f.add(d);
                    Util.getDb().store(c);
                    Util.getDb().store(f);
                    Util.getDb().store(d);
                    Util.getDb().commit();
                    loadListDet();
                }
            }
        });

        btnBaj3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( selectedFactura != null && selectedDetalle != null ) {
                    // borro detalle de la factura
                    selectedFactura.remove(selectedDetalle);
                    // borro detalle de b.d.
                    Util.getDb().delete(selectedDetalle);
                    // actualizo factura
                    Util.getDb().store(selectedFactura);
                    Util.getDb().commit();
                    loadListDet();                    
                }
            }
        });

        
        btnLis2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.listByFac(getInt("Factura Nro:")).toString());
            }
        });
        
        btnLis3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.listByProd(getInt("Producto Nro:")).toString());
            }
        });

        btnLis4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.listByProdName(getString("Nombre Producto:")).toString());
            }
        });
        
        btnList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txt.setText(Util.list(new Cliente()).toString());
            }
        });

        
        btnAlCl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.getCache().addCliente(
                    new Cliente(
                        getInt("Codigo:"),
                        getString("Nombre:")));
                Util.getCache().saveCache();
                loadCmbCli();
            }
        });
        
        btnBaCl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cliente c = (Cliente) cmbcli.getSelectedItem();
                Util.getCache().removeCliente(c);
                Util.getCache().saveCache();
                loadCmbCli();
            }
        });

        btnAlPr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Util.getCache().addProducto(
                    new Producto(
                        getInt("Codigo:"),
                        getString("Nombre:"),
                        getDouble("Precio:")));
                Util.getCache().saveCache();
                loadCmbProd();
            }
        });
        
        btnBaPr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Producto p = (Producto) cmbpro.getSelectedItem();
                Util.getCache().removeProducto(p);
                Util.getCache().saveCache();
                loadCmbProd();
            }
        });

        loadCmbCli();
        cmbcli.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cliente c = (Cliente) cmbcli.getSelectedItem();
                System.out.println("Selecciono cli="+c);
                selectedCliente = Util.loadCliente(c);
                loadListFac();
                loadListDet();
            }
        });
        cmbcli.setEditable(false);
        
        loadCmbProd();
        //cmbpro.addItem(Util.getCache().getProductos().get(1));
        cmbpro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Producto p = (Producto) cmbpro.getSelectedItem();
                System.out.println("Selecciono pro="+p);
            }
        });
        cmbpro.setEditable(false);
        
        lstf.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                selectedFactura = lstf.getSelectedValue();
                System.out.println("Selecciono factura="+selectedFactura);
                loadListDet();
            }
        });

        lstd.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                selectedDetalle = lstd.getSelectedValue();
                System.out.println("Selecciono detalle="+selectedDetalle);
            }
        });
        
        loadListFac();
        loadListDet();
        
        pinf1.add(new JLabel("Cliente:"));
        pinf1.add(cmbcli);
        pinf1.add(btnAlCl);
        pinf1.add(btnBaCl);
        
        pinf2.add(new JLabel("Prod:"));
        pinf2.add(cmbpro);
        pinf2.add(btnAlPr);
        pinf2.add(btnBaPr);
        
        pinf.setLayout(new GridLayout(2,1));
        pinf.add(pinf1);
        pinf.add(pinf2);
        
        pcent1.add(lstf);
        pcent1.add(lstd);
        pcent2.add(txt);
        pcent.add(pcent1);
        pcent.add(pcent2);
        
        add(pcent,"Center");
        add(pder,"East");
        add(new JLabel("db4o embebido"),"North");
        add(pinf,"South");
        
        setTitle("Persistiendo Objetos en db4o");
        setSize(400,500);
        setVisible(true);
        
    }

//======================================================================

    private void loadCmbProd() {
        selectedProducto = null;
        cmbpro.removeAllItems();
        for(Producto p : Util.getCache().getProductos()) cmbpro.addItem(p);
    }
    private void loadCmbCli() {
        selectedCliente = null;
        cmbcli.removeAllItems();
        for(Cliente c : Util.getCache().getClientes()) cmbcli.addItem(c);
    }
    private void loadListFac() {
        selectedFactura = null;
        // carga las facturas del cliente seleccionado
        ((DefaultListModel) lstf.getModel()).removeAllElements();
        if ( selectedCliente != null ) {
            for(Factura f : selectedCliente.getFacturas()) ((DefaultListModel<Factura>) lstf.getModel()).addElement(f);
        }
    }

    private void loadListDet() {
        selectedDetalle = null;
        // carga el detalle de la factura seleccionada
        ((DefaultListModel) lstd.getModel()).removeAllElements();
        if ( selectedFactura != null ) {
            for(Detalle d : selectedFactura.getDetalles()) ((DefaultListModel<Detalle>) lstd.getModel()).addElement(d);
        }
    }
    
    private double getDouble(String prompt) {
        String str = null;
        boolean continuo = true;
        double ret=0.0;
        Double i = null;
        while(continuo) {
            str = getString(prompt);
            try {
                i = new Double(str);
                ret = i;
                continuo=false;
            } catch(NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null,"["+str+"] no es un valor double!");
            }
        }
        return ret;
    }
    private int getInt(String prompt) {
        String str = null;
        boolean continuo = true;
        int ret=0;
        Integer i = null;
        while(continuo) {
            str = getString(prompt);
            try {
                i = new Integer(str);
                ret = i;
                continuo=false;
            } catch(NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null,"["+str+"] no es un valor entero!");
            }
        }
        return ret;
    }
    private String getString(String prompt) {
        return JOptionPane.showInputDialog(null,prompt);
    }
}
