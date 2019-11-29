package vista;


import javax.swing.*;

import controlador.Controlador;

import java.awt.*;

// MARCO o FRAME
public class Vista extends JFrame implements InterfaceVista{

	private PanelPrincipal panel_principal;
	private PanelMenu panel_menu;

	
	
	// Constructor
	public Vista() {
		start();

	}

	// Metodos
	
	private void start() {
		startMenu();
		startPanelPrincipal();
		startFrame("Signal Plotter");
		
	}
	
	// Inicializa el panel principal
	private void startPanelPrincipal() {
		panel_principal = new PanelPrincipal();
		this.add(panel_principal);
	}
	
	// Inicializa el menu
	private void startMenu() {
		panel_menu = new PanelMenu();
		this.add(panel_menu);
		this.setJMenuBar(panel_menu.get_barra());
		
	}
	
	//	startFrame inicializa el frame (marco) 
	private void startFrame(String title) {

		
		this.setExtendedState(MAXIMIZED_BOTH);	// el marco se abre con ventana completa
		this.setTitle(title);

		this.setVisible(true);
		
		
	}

	@Override
	public void setControlador(Controlador c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGrafica() {
		// TODO Auto-generated method stub
		
	}

}

