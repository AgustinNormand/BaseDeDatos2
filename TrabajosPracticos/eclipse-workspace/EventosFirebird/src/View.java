import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Interfaz grafica swing para probar eventos asincronicos FB
 * Crea y lanza objeto event listener asincronico en BD Firebird
 *
 * @author G. Cherencio
 * @version 1.0
 * @see https://github.com/FirebirdSQL/jaybird/wiki/AsynchronousEventNotifier.java
 */
public class View {
    private JTextArea textArea;
    private EventoAsincronico ev;

    /**
     * Constructor for objects of class View
     */
    public View(FBCon f) {
        buildGui();
        ev = new EventoAsincronico(f,textArea);
        ev.run(new String[] { "NuevoPedido" } );
    }

    private void buildGui(){
        JFrame frame = new JFrame("Recibo Evento NuevoPedido");
        JPanel panel = new JPanel();
 
        JButton clearButton = new JButton("Borrar");
        frame.getContentPane().add(panel);
        panel.setLayout(new BorderLayout());
        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        panel.add(new JScrollPane(this.textArea));
 
        clearButton.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    textArea.setText("");
                }
            });
        panel.add(clearButton, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(400, 500));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.show();
    }

}
