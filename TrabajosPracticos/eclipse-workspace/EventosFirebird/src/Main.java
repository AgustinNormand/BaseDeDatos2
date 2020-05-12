import java.io.*;
/**
 * Espera por evento NuevoPedido este codigo puede notificarse del evento
 * Firebird NuevoPedido y proceder en consecuencia
 * Probado con FB 2.5 y Jaybird 3.0.6 en Windows 10
 * 
 * Codigo dentro de la BD para probar este programa:
 
CREATE TABLE TBL_PEDIDO
(
  NRO integer NOT NULL,
  CONSTRAINT PK_TBL_PEDIDO PRIMARY KEY (NRO)
);


SET TERM ^ ;
CREATE TRIGGER TRG_AITBL_PEDIDO FOR TBL_PEDIDO ACTIVE
AFTER insert POSITION 0
AS
BEGIN
    POST_EVENT 'NuevoPedido';
END^
SET TERM ; ^

 * Mas info: https://github.com/FirebirdSQL/jaybird/wiki/Jaybird-Events-API
 * 
 * @author G. Cherencio
 * @version 1.0
 * @see https://github.com/FirebirdSQL/jaybird/wiki/Jaybird-Events-API
 */
public class Main {
    public static void main(String[] args) {
        FBCon f = new FBCon();
        f.setServer("localhost");
        f.setPuerto(3050);
        f.setBaseDeDatos("d:/grchere/PRUEBA.FDB");
        f.setUser("sysdba");
        f.setPwd("masterkey");
        f.setRole("sysdb");
        f.setDb(f.getServer()+"/"+f.getPuerto()+":"+f.getBaseDeDatos());
        if ( f.connect() ) {
            System.out.println("FB Conectado!");
            String opt = null;
            do {
                System.out.println("Firebird SQL DB Events con Jaybird\n"+
                    "1. Evento Sincronico (consola)\n"+
                    "2. Evento Asincronico (swing)\n"+
                    "9. Salir\n"+
                    "Elija Opcion:");
                Console cons = System.console();
                opt = cons.readLine();
                if ( opt.equals("1") ) eventoSincronico();
                if ( opt.equals("2") ) eventoAsincronico();
            } while(!opt.equals("9"));
            System.out.println("FB desconecto y termino aplicacion!");
            f.disconnect();
        } else {
            System.out.println("FB Error: " + f.getErrorMessages());
        }
    }
    
    /**
     * Implemento espera de evento sincronico
     */
    private static void eventoSincronico() {
        EventoSincronico ev = new EventoSincronico(FBCon.getFBCon(1));
        System.out.println("Proceso bloqueado esperando por evento NuevoPedido");    
        ev.run("NuevoPedido");
        System.out.println("Proceso evento NuevoPedido!");
    }

    /**
     * Implemento espera de evento asincronico
     */
    private static void eventoAsincronico() {
        new View(FBCon.getFBCon(1));
    }

}
