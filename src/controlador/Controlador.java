package controlador;

import vista.*;
import java.awt.event.*;
import modelo.*;

public class Controlador implements ActionListener{
	
	private Modelo modelo;
	private FramePrincipal vista;

	public Controlador(Modelo modelo, FramePrincipal vista) {
		this.modelo = modelo;
		this.vista = vista;
	}


		@Override 
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(InterfaceVista.ButtonConnectPushed)){
				System.out.println("BOTON PRESIONADO");
			}else {
				System.out.println("ERROR");
			}
			
			
		}
	}

	


