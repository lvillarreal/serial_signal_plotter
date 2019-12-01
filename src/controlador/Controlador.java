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
		public void actionPerformed(ActionEvent e)  {
			
			Runnable r_barOptions = new barOptions(vista,e.getActionCommand());
			Thread t_barOptions = new Thread(r_barOptions);
			
			Runnable r_serialComm = new SerialComm(vista,e.getActionCommand());
			Thread t_serialComm = new Thread(r_serialComm);
			
			if (e.getActionCommand().equals(InterfaceVista.ButtonConnectPushed)){
				t_serialComm.start();				
			}else if(e.getActionCommand().equals(InterfaceVista.ListSerialPorts)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.MenuButtonExitPushed)){
				t_barOptions.start();
			}
			
			
		}

		


}

class SerialComm implements Runnable{
	private InterfaceVista vista;
	private String option;
	
	public SerialComm(InterfaceVista vista,String option) {
		this.vista = vista;
		this.option = option;
	}
	
	public void run() {
		switch(option) {
		case InterfaceVista.ButtonConnectPushed:
			buttonConnect();
			break;
		}
	}
	
	private void buttonConnect() {
		/*for (int i=1; i<=3000; i++){
			System.out.println(i);
			try {
				Thread.sleep(4);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}
}


// Implementa lo relacionado a la barra de menu
class barOptions implements Runnable{
	
	
	private String option;
	private InterfaceVista vista;
	
	public barOptions(InterfaceVista vista,String option) {
		this.option = option;
		this.vista = vista;
	}
	
	public void run() {
		switch(option){
			case InterfaceVista.MenuButtonExitPushed:
				exitProgram();
				break;
				
			case InterfaceVista.ListSerialPorts:
				listSerialPorts();
				break;	
		}
	}
	
	private void exitProgram() {
		System.exit(0);
	}
	
	// Lista en consola los puertos conectados
    private void listSerialPorts() { 
	    
	 	Enumeration ports = CommPortIdentifier.getPortIdentifiers();  
        
	 	if(ports.hasMoreElements()) {
	 		vista.setListCommPorts("Puertos conectados");
            vista.setListCommPorts(((CommPortIdentifier)ports.nextElement()).getName().toString() );

	 	}else {
	 		vista.setListCommPorts("No se detectan puertos conectados");
	 	}
        while(ports.hasMoreElements())  {
            vista.setListCommPorts(((CommPortIdentifier)ports.nextElement()).getName().toString() );
        }
	 }
	
}




	


