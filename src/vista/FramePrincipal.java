package vista;


import javax.swing.*;
import controlador.Controlador;
import java.awt.*;
import java.awt.event.*;

// MARCO o FRAME
public class FramePrincipal extends JFrame implements InterfaceVista{

	/* OBJETOS RELACIONADOS AL MENU */
	
	private JMenuBar barra;		// Objeto que almacena la barra de menu
	
	// Opciones del menu
	private JMenu menu_file;
	private JMenu menu_view;
	private JMenu menu_help;
	
	//Items de cada opcion
	//Items de file
	private JMenuItem menuItem_save;
	private JMenuItem menuItem_saveAs;
	private JMenuItem menuItem_import;
	private JMenuItem menuItem_export;
	private JMenuItem menuItem_exit;
	
	//Items de edit
	
	//Items de view
	private JMenuItem menuItem_graph;
	private JMenuItem menuItem_math;
		
	//Items de help
    private JMenuItem menuItem_helpContent;
	private JMenuItem menuItem_about;

	// ATRIBUTOS RELACIONADOS AL PANEL PRINCIPAL
	
	private JTextField text_port;	// campo de texto para introducir el puerto 
	private JButton button_connect;	// boton para conectar
	private JLabel label;			// CAMBIAR POR LA GRAFICA
	private JLabel console;

	
	// PANELES
	
	private JPanel panel_menu;
	private JPanel panel_principal;
	
	
	
	
	
	// CONSTRUCTOR
	public FramePrincipal() {
		

	}

	
	// METODOS
	
	
	public void start() {
		startPanelPrincipal();
		startMenu();
		
		startFrame("Signal Plotter");
		
	}
	
	//	startFrame inicializa el frame (marco) 
	private void startFrame(String title) {

		
		this.setExtendedState(MAXIMIZED_BOTH);	// el marco se abre con ventana completa
		this.setTitle(title);

		this.setVisible(true);
		
		
	}
	
	// METODOS RELACIONADOS AL MENU
	
	// Inicializa el menu
	private void startMenu() {
		panel_menu = new JPanel();
		setMenu();
		this.setJMenuBar(this.barra);
		
	}
	
	// Crea el menu
	private void setMenu() {
		// Inicializacion JMenuBar
		barra = new JMenuBar();
		
		// Inicializacion JMenu
		menu_file   = new JMenu("File");
		menu_view   = new JMenu("View");
		menu_help 	= new JMenu("Help");
		
		// Inicializacion JMenuItem
			//Items de file
		menuItem_save   = new JMenuItem("Save");
		menuItem_saveAs = new JMenuItem("Save As...");
		menuItem_import = new JMenuItem("Import...");
		menuItem_export = new JMenuItem("Export...");
		menuItem_exit   = new JMenuItem("Exit");
		
			//Items de view
		menuItem_graph = new JMenuItem("Graph");
		menuItem_math  = new JMenuItem("Math");
		
			//Items de help
		menuItem_helpContent = new JMenuItem("Help Contents");
		menuItem_about       = new JMenuItem("About Signal Plotter");

		// Agregado de imagenes
		//menuItem_save.setIcon(new ImageIcon(getClass().getResource("/resources/save.png")));
		//menuItem_saveAs.setIcon(new ImageIcon(getClass().getResource("/resources/save_as.png")));

		// AGREGAR OPCIONES PERTINENTES A LOS MENUS
		
		// Se agregan los items a las opciones

			// file
		menu_file.add(menuItem_save);
//		menu_file.addSeparator();
		menu_file.add(menuItem_saveAs);
		menu_file.addSeparator();
		menu_file.add(menuItem_import);
//		menu_file.addSeparator();
		menu_file.add(menuItem_export);
		menu_file.addSeparator();
		menu_file.add(menuItem_exit);
		menu_file.addSeparator();
		
			//view
		menu_view.add(menuItem_graph);
		menu_view.addSeparator();
		menu_view.add(menuItem_math);
		menu_view.addSeparator();
		
			//help
		menu_help.add(menuItem_helpContent);
		menu_help.addSeparator();
		menu_help.add(menuItem_about);
		menu_help.addSeparator();
		
			// Barra
		barra.add(menu_file);
		barra.add(menu_view);
		barra.add(menu_help);
		
	}
	
	// METODOS RELACIONADOS AL PANEL PRINCIPAL
	
	// Inicializa el panel principal
	private void startPanelPrincipal() {
		configPanel();
		setObjects();
		
		this.getContentPane().add(panel_principal);
	}

	
	// Configuracion del panel
	private void configPanel() {
		panel_principal = new JPanel();
		panel_principal.setLayout(new GridBagLayout());
		panel_principal.setBackground(new Color(56,68,54));
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
		
		panel_principal.add(text_port,c);		// se agrega al panel
		
		
		// BOTON CONNECT
		
		// Instanciacion del boton para la conexion serial
		
		button_connect = setButton(c,"CONNECT",3,0,1,1,0.0,10);


		panel_principal.add(button_connect,c);		// se agrega al panel
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

		panel_principal.add(label, c);

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
		panel_principal.add(console, c);
		

	}
		
	// Crea, configura y ubica el boton connect
	private JButton setButton(GridBagConstraints c,String name,int gridx, int gridy,int gridwidth, int gridheight,double weightx, int gap) {
		JButton button = new JButton(name);
		
		
		c.gridx = gridx; 		// El campo de texto empieza en la columna cero.
		c.gridy = gridy; 		// El campo de texto empieza en la fila cero
		c.gridwidth = gridwidth; 	// El campo de texto ocupa una columna.
		c.gridheight = gridheight;   // El campo de texto ocupa una fila.
		c.weightx = weightx;	    
		c.insets = new Insets(gap,gap,gap,gap);	
		button.setToolTipText("Presione para conectar con el puerto serie");
		button.setActionCommand(ButtonConnectPushed);
		
		return button;
		
	}
	
	
	@Override
	public void setControlador(Controlador c) {
		button_connect.addActionListener((ActionListener) c);
	}

	@Override
	public void setGrafica() {
		// TODO Auto-generated method stub
		
	}

}

