package controlador;

import java.awt.event.*;
import java.util.Enumeration;
import gnu.io.CommPortIdentifier;

import modelo.*;
import vista.*;

public class Controlador implements ActionListener{
	
	
	//private Modelo modelo;
	private InterfaceVista vista;

	
	public Controlador(Modelo modelo, InterfaceVista vista) {
		//this.modelo = modelo;
		this.vista = vista;
	}

		@Override 
		public void actionPerformed(ActionEvent e) {
			
			if (e.getActionCommand().equals(InterfaceVista.ButtonConnectPushed)){
				System.out.println("BOTON PRESIONADO");
				
			}else if(e.getActionCommand().equals(InterfaceVista.ListSerialPorts)){
				listSerialPorts();
			}
		}
		
		 public void listSerialPorts() { 
			    //String list = new String();
			 	Enumeration ports = CommPortIdentifier.getPortIdentifiers();  
		        
			 	if(ports.hasMoreElements()) {
			 		vista.setListCommPorts("Puertos conectados");
		            vista.setListCommPorts(((CommPortIdentifier)ports.nextElement()).getName().toString() );

			 	}
		        while(ports.hasMoreElements())  {
		            vista.setListCommPorts(((CommPortIdentifier)ports.nextElement()).getName().toString() );
		        }
		
		
		 }
}

	


