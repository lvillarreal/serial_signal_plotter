package controlador;

import java.util.Enumeration;
import gnu.io.CommPortIdentifier;
import vista.*;

public class SerialComm {
	
	private FramePrincipal vista;
	public SerialComm(FramePrincipal vista) {
		this.vista = vista;
	}
		
	// muestra en consola los puertos activos
    public void list() {  
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();  
         
        
        while(ports.hasMoreElements())  {
            System.out.println(((CommPortIdentifier)ports.nextElement()).getName());
            vista.setListCommPorts(((CommPortIdentifier)ports.nextElement()).getName());
        }

        
    }  
    

	
}
