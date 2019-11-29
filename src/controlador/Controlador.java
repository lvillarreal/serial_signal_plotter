package controlador;

import vista.*;
import java.awt.event.*;


public class Controlador{
	
	private PanelPrincipal vista;
	private ButtonConnect boton_connect_event;

	
	public ButtonConnect getBotonConectEvent() {
		return boton_connect_event;
	}
		
}

//Cuando se presiona el boton connect
class ButtonConnect implements ActionListener {

	@Override 
	public void actionPerformed(ActionEvent e) {
		System.out.println("BOTON PRESIONADO");
		
	}
}