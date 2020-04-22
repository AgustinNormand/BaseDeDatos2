package com.bd2.unidad2;

import com.bd2.unidad2.repository.DaosFactory;
import com.bd2.unidad2.repository.impl.ConnectionManager;
import com.bd2.unidad2.repository.impl.DaosImplementationFactory;
import com.bd2.unidad2.view.Menu;

/**
 * Inicio de la aplicacion!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ConnectionManager connectionManager = ConnectionManager.getInstance("localhost", 6379, "masterkey");
    	DaosFactory factory = new DaosImplementationFactory(connectionManager.getSyncConnection());
    	Menu menu = new Menu(
    			factory.getInstanceOfLoginDao(),
                factory.getInstanceOfUsuarioDao(),
                factory.getInstanceOfTokenDao(),
                factory.getInstanceOfMailValidationDao()
    			);
        try{
            menu.menuPrincipal();
        }finally {
            //si o si se cierra la conexion
            connectionManager.close();
        }
    }
}
