package vista;


import javax.swing.*;
import java.awt.*;

// MARCO o FRAME
public class VentanaPrincipal extends JFrame {


	// Constructor
	public VentanaPrincipal() {
		startMenu();
		startSerial();
		startFrame("Signal Plotter");
		
	}

	// Metodos
	
	// Inicializa el bloque para conexion serial
	private void startSerial() {
		PanelPrincipal panel_principal = new PanelPrincipal();
		this.add(panel_principal);
	}
	
	// Inicializa el menu
	private void startMenu() {
		PanelMenu panel_menu = new PanelMenu();
		this.add(panel_menu);
		this.setJMenuBar(panel_menu.get_barra());
		
	}
	
	//	startFrame inicializa el frame (marco) 
	private void startFrame(String title) {

		
		this.setExtendedState(MAXIMIZED_BOTH);	// el marco se abre con ventana completa
		this.setTitle(title);

		this.setVisible(true);
		
		
	}
}

