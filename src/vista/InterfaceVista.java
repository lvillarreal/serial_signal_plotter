package vista;

import controlador.Controlador;

public interface InterfaceVista {
		
	void setControlador(Controlador c);

	void setGrafica();
	
	static final String ButtonConnectPushed = "ButtonConnectedPushed";	// Para ActionCommand del boton connect

	
}
