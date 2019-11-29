package vista;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import controlador.*;

/*
// Vista del model MVC
public class PanelPrincipal extends JPanel{
	//ATRIBUTOS DE CLASE
		
	// variables para la distribucion de los objetos
	
	private JTextField text_port;	// campo de texto para introducir el puerto 
	private JButton button_connect;	// boton para conectar
	private JLabel label;
	private JLabel console;

	static final String ButtonConnectPushed = "ButtonConnectedPushed";	// Para ActionCommand del boton connect
	
	
	public PanelPrincipal() {
		
		configPanel();
		setObjects();
	}
	
	// Configuracion del panel
	private void configPanel() {
		setLayout(new GridBagLayout());
		setBackground(new Color(56,68,54));
	}
	
	private void setObjects() {
	
		// GridBagConstraints es una clase que en sus atributos contiene la informacion de la ubicacion de 
		// los objetos dentro del panel
		
		GridBagConstraints c = new GridBagConstraints();	

		// SECCION CONEXION SERIAL
		
		// CAMPO DE TEXTO
		
		// Instanciacion del Campo de texto para la conexion serial
		text_port = new JTextField("COM 1");	// se instancia el TextField con el texto COM 1 por defecto
		text_port.setEnabled(true);				// se habilita el ingreso de texto por parte del usuario		
		text_port.setToolTipText("Ingrese el puerto COM al que se ha conectado el dispositivo");
		c.gridx = 2; 		// El campo de texto empieza en la columna cero.
		c.gridy = 0; 		// El campo de texto empieza en la fila cero
		c.gridwidth = 1; 	// El campo de texto ocupa una columna.
		c.gridheight = 1;   // El campo de texto ocupa una fila.
		c.weightx = 0.1;
		c.insets = new Insets(10,10,10,10);
		c.ipadx = 50;
		c.anchor = GridBagConstraints.EAST;
		
		this.add(text_port,c);		// se agrega al panel
		
		
		// BOTON CONNECT
		
		// Instanciacion del boton para la conexion serial
		
		button_connect = setButton(c,"CONNECT",3,0,1,1,0.0,10);
		button_connect.setToolTipText("Presione para conectar con el puerto serie");
		button_connect.setActionCommand(ButtonConnectPushed);

		this.add(button_connect,c);		// se agrega al panel
		c.weightx = 0.0;
		
		// SECCION DE LA GRAFICA
		label = new JLabel("RECORDAR CAMBIAR POR LA GRAFICA");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setOpaque(true);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1; 
		c.gridheight = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.insets = new Insets(10,10,10,10);
		c.fill = GridBagConstraints.BOTH;

		this.add(label, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		
		
		// SECCION DE CONSOLE
		console = new JLabel("CONSOLA");
		console.setOpaque(true);
		console.setBackground(Color.WHITE);
		console.setForeground(Color.BLACK);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4; 
		c.gridheight = 1;
		c.weighty = 0.3;
		c.weightx = 1.0;
		c.insets = new Insets(10,10,10,10);
		this.add(console, c);
		

	}
		
	private JButton setButton(GridBagConstraints c,String name,int gridx, int gridy,int gridwidth, int gridheight,double weightx, int gap) {
		JButton button = new JButton(name);
		
		
		c.gridx = gridx; 		// El campo de texto empieza en la columna cero.
		c.gridy = gridy; 		// El campo de texto empieza en la fila cero
		c.gridwidth = gridwidth; 	// El campo de texto ocupa una columna.
		c.gridheight = gridheight;   // El campo de texto ocupa una fila.
		c.weightx = weightx;	    
		c.insets = new Insets(gap,gap,gap,gap);	
		
		return button;
		
	}
	
	public JButton getButtonConnect() {
		return button_connect;
	}
}
*/


