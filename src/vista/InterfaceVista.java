package vista;

import controlador.Controlador;

public interface InterfaceVista {
		
	void setControlador(Controlador c);
	void start();
	
	void setGrafica();
	void setListCommPorts(String list);
	
	
	static final String ButtonConnectPushed = "ButtonConnectedPushed";	// Para ActionCommand del boton connect
	static final String ListSerialPorts = "ListSerialPorts";			// Para listar los puertos seriales conectados
	static final String MenuButtonExitPushed = "MenuButtonExitPushed";	// Cuando se presiona el boton File/Exit

}
