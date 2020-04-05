package vista;


import javax.swing.*;
import controlador.Controlador;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.awt.event.*;

import org.jfree.chart.ChartPanel;


// MARCO o FRAME
public class FramePrincipal extends JFrame implements InterfaceVista{
	

	/*CONFIGURACION DE GRAFICA*/
	private JFrame frame_conf_graph;
	
	private JLabel jl_time_range;
	private JLabel jl_sample_rate;
	private JLabel jl_signal_name;
	
	private JComboBox<String> jcb_time_units;
	private JComboBox<String> jcb_sample_rate_units;
	
	private JTextField jtf_set_time_range;
	private JTextField jtf_set_sample_rate;
	private JTextField jtf_set_signal_name;
	
	private JButton jb_add_time;
	private JButton jb_add_sample_rate;
	private JButton jb_add_signal_name;

	private JButton jb_close;
	
	
	
	
	
	
	/*GRAFICA*/
	private XYchart grafica;
	
	
	
	
	/* OBJETOS RELACIONADOS AL MENU */
	
	private JMenuBar barra;		// Objeto que almacena la barra de menu
	
	// Opciones del menu
	private JMenu menu_file;
	private JMenu menu_view;
	private JMenu menu_math;
	private JMenu menu_config;
	private JMenu menu_help;
	
	//Items de cada opcion
	
	//Items de file
	private JMenuItem menuItem_file_save;
	private JMenuItem menuItem_file_saveAs;
	private JMenuItem menuItem_file_openFile;
	private JMenu menuItem_file_export;
	private JMenuItem menuItem_file_exit;
	
		// EXPORT
		private JMenuItem menuItem_file_export_txt;
		private JMenuItem menuItem_file_export_matlab;
	
	//Items de view
	private JMenu menuItem_view_graph;
	//private JMenuItem menuItem_view_math;
	private JMenuItem menuItem_view_SerialPorts;
	
		// Graph
		private JMenuItem menuItem_view_graph_getRangeTime;
		private JMenuItem menuItem_view_graph_Fs;	// frecuencia de muestreo
		private JMenuItem menuItem_view_graph_graphData;	// graficar datos obtenidos
		
	// Items de Math
		private JMenu menuItem_math_fft;
		
		// Items de FFT
			private JMenuItem menuItem_math_fft_calculate;
			private JMenu menuItem_math_fft_graph;
				
				// Items de graph
				private JMenuItem menuItem_math_fft_graph_module;
				private JMenuItem menuItem_math_fft_graph_angle;

			
	// Items de Config
		
	private JMenu menuItem_config_graph;
	private JMenu menuItem_config_serial;
			
			// Config graph
			private JMenuItem menuItem_config_graph_all;
			private JMenuItem menuItem_config_setRangeTime;
			private JMenuItem menuItem_config_setFs;
			
			// Config serial
			private JMenuItem menuItem_config_serial_baudRate;
//			private JComboBox<String> menuItem_config_serial_CommPort;
			
			
			
	//Items de help
    private JMenuItem menuItem_help_helpContent;
	private JMenuItem menuItem_help_about;

	
	
	// ATRIBUTOS RELACIONADOS AL PANEL PRINCIPAL
	
	private JTextField text_port;	    // campo de texto para introducir el puerto 
	private JButton button_connect;	    // boton para conectar
	private JButton button_disconnect;
	private JButton button_start;	    // Comineza a almacenar los datos recibidos por puerto serie
	private JLabel label;			    // CAMBIAR POR LA GRAFICA
	
	private JTextArea console;
	private JInternalFrame frame_console;
	private JInternalFrame graph_frame;
	
	// PANELES
	
	//private JPanel panel_menu;
	private JPanel panel_principal;
	
	
	
	
	
	// CONSTRUCTOR
	public FramePrincipal() {

	}

	
	// METODOS
	
	
	public void start() {
		startPanelPrincipal();
		startMenu();
		setConfigGraph();
		
		startFrame("Signal Plotter");
		
	}
	
	//	startFrame inicializa el frame (marco) 
	private void startFrame(String title) {

		
		this.setExtendedState(MAXIMIZED_BOTH);	// el marco se abre con ventana completa
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(500,500));
		this.setVisible(true);
		
		
	}
	
	// METODOS RELACIONADOS AL MENU
	
	// Inicializa el menu
	private void startMenu() {
		//panel_menu = new JPanel();
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
		menu_math   = new JMenu("Math");
		menu_config = new JMenu("Config.");
		menu_help 	= new JMenu("Help");
		
		// Inicializacion JMenuItem
			//Items de file
		menuItem_file_save   = new JMenuItem("Save");
		menuItem_file_saveAs = new JMenuItem("Save As...");
		menuItem_file_openFile = new JMenuItem("Open file...");
		//menuItem_file_export = new JMenu("Export...");
		menuItem_file_exit   = new JMenuItem("Exit");
		
		
			// Export
			this.menuItem_file_export_txt = new JMenuItem("Text file (.txt)");
			this.menuItem_file_export_txt.setToolTipText("Exporta los datos a un archivo de texto.");
			this.menuItem_file_export_matlab = new JMenuItem("Matlab file (.m)");
			this.menuItem_file_export_matlab.setToolTipText("Exporta los datos a un archivo .m que realiza la gráfica en Matlab.");
		
			//Items de view
		menuItem_view_graph = new JMenu("Graph");
		//menuItem_view_math  = new JMenuItem("Math");
		menuItem_view_SerialPorts = new JMenuItem("Serial Ports");
				 
				//Items de Graph
				menuItem_view_graph_getRangeTime = new JMenuItem("Show time range");
				this.menuItem_view_graph_getRangeTime.setToolTipText("Muestra en consola la duración actual de la medición. Valor por defecto: 1 ms.");
				menuItem_view_graph_Fs = new JMenuItem("Show sample rate");
				this.menuItem_view_graph_Fs.setToolTipText("Muestra en consola la frecuencia de muestreo actual. Valor por defecto: 1 kHz.");
			    this.menuItem_view_graph_graphData = new JMenuItem("Graph data");
			    this.menuItem_view_graph_graphData.setToolTipText("Graficar los datos capturados.");
			
			// Items de Math
				menuItem_math_fft = new JMenu("FFT");
				menuItem_math_fft.setToolTipText("Calcula la fft de la señal muestreada. Guarda el resultado en \"files/signal_fft.txt\". Grafica el resultado. ");
				
				// Items de FFT
				this.menuItem_math_fft_calculate = new JMenuItem("Calculate");
				this.menuItem_math_fft_graph = new JMenu("Graph");
				
					// Items de graph
					this.menuItem_math_fft_graph_angle = new JMenuItem("Angle");
					this.menuItem_math_fft_graph_module = new JMenuItem("Module");

					
				
				
				
			// Items de Config
				this.menuItem_config_graph = new JMenu("Graph");
				this.menuItem_config_serial = new JMenu("Serial Comm");
				
					// Config graph
					this.menuItem_config_setRangeTime = new JMenuItem("Set time range");
					this.menuItem_config_setRangeTime.setToolTipText("Setea el tiempo de duración de la gráfica");
					this.menuItem_config_setFs = new JMenuItem("Set sampling rate");
					this.menuItem_config_setFs.setToolTipText("Setea la frecuencia de muestreo, necesaria para graficar la señal");
					this.menuItem_config_graph_all = new JMenuItem("Set all features");
					this.menuItem_config_graph_all.setToolTipText("Setea todas las caracteristicas de la gráfica");


					// Config Serial
					this.menuItem_config_serial_baudRate = new JMenuItem("Set baud rate");
					this.menuItem_config_serial_baudRate.setToolTipText("Setea los baudios para la transmisión serial");
					
					
			//Items de help
		menuItem_help_helpContent = new JMenuItem("Help Contents");
		menuItem_help_about       = new JMenuItem("About Signal Plotter");
		
		// Agregado de imagenes
		//menuItem_save.setIcon(new ImageIcon(getClass().getResource("/resources/save.png")));
		//menuItem_saveAs.setIcon(new ImageIcon(getClass().getResource("/resources/save_as.png")));

		// AGREGAR OPCIONES PERTINENTES A LOS MENUS
		
		// Se agregan los items a las opciones

			// file
//		menu_file.add(menuItem_file_save);
//		menu_file.addSeparator();
		menu_file.add(menuItem_file_openFile);
		menu_file.addSeparator();
		menu_file.add(menuItem_file_saveAs);
		menu_file.addSeparator();
//		menu_file.add(menuItem_file_export);
		menu_file.addSeparator();
		menu_file.add(menuItem_file_exit);
//		menu_file.addSeparator();
			
			// Export
				//this.menuItem_file_export.add(this.menuItem_file_export_txt);
				//this.menuItem_file_export.add(this.menuItem_file_export_matlab);
			//view
		menu_view.add(menuItem_view_graph);
		menu_view.addSeparator();
		//menu_view.add(menuItem_view_math);
		menu_view.addSeparator();
		menu_view.add(menuItem_view_SerialPorts);
				
					// graph
					menuItem_view_graph.add(menuItem_view_graph_getRangeTime);
					menuItem_view_graph.add(menuItem_view_graph_Fs);	
					menuItem_view_graph.add(this.menuItem_view_graph_graphData);	

					
			
			// Math
			menu_math.add(menuItem_math_fft);
			menu_math.addSeparator();		
			
				menuItem_math_fft.add(this.menuItem_math_fft_calculate);
				menuItem_math_fft.add(this.menuItem_math_fft_graph);
			
				this.menuItem_math_fft_graph.add(this.menuItem_math_fft_graph_module);
				this.menuItem_math_fft_graph.add(this.menuItem_math_fft_graph_angle);
			
			// Config.
		menu_config.add(this.menuItem_config_graph);
		menu_config.addSeparator();
		menu_config.add(this.menuItem_config_serial);	
		
					// Config Graph
				
					//this.menuItem_config_graph.add(this.menuItem_config_setRangeTime);
					//menuItem_config_graph.addSeparator();
					//this.menuItem_config_graph.add(this.menuItem_config_setFs);
					//menuItem_config_graph.addSeparator();
					this.menuItem_config_graph.add(this.menuItem_config_graph_all);
					
					// Config Serial
					this.menuItem_config_serial.add(this.menuItem_config_serial_baudRate);
					this.menuItem_config_serial.addSeparator();
					
				
					
			//help
		menu_help.add(menuItem_help_helpContent);
		menu_help.addSeparator();
		menu_help.add(menuItem_help_about);
		menu_help.addSeparator();
		
			// Barra
		barra.add(menu_file);
		barra.add(menu_view);
		barra.add(menu_math);
		barra.add(menu_config);
		barra.add(menu_help);
		
		

		
		
		
		// CONFIGURACION DE OPCIONES
		// FILE
		
		menuItem_file_exit.setActionCommand(InterfaceVista.MenuButtonExitPushed);
		menuItem_file_openFile.setActionCommand(InterfaceVista.MenuButtonOpenFile);
		this.menuItem_file_saveAs.setActionCommand(InterfaceVista.MenuSaveAs);
		
		//VIEW
		menuItem_view_SerialPorts.setActionCommand(InterfaceVista.ListSerialPorts);
		this.menuItem_view_graph_Fs.setActionCommand(InterfaceVista.GetSamplingRate);
		this.menuItem_view_graph_getRangeTime.setActionCommand(InterfaceVista.GetTimeRange);
		this.menuItem_view_graph_graphData.setActionCommand(InterfaceVista.ViewGraphData);
			//EXPORT
			this.menuItem_file_export_matlab.setActionCommand(InterfaceVista.FileExportMatlab);
			this.menuItem_file_export_txt.setActionCommand(InterfaceVista.FileExportText);
		// MATH
		this.menuItem_math_fft_calculate.setActionCommand(InterfaceVista.CalculateFFT);
		this.menuItem_math_fft_graph_module.setActionCommand(InterfaceVista.GraphFFTmodule);
		this.menuItem_math_fft_graph_angle.setActionCommand(InterfaceVista.GraphFFTangle);

		// CONFIG
		    
			// GRAPH
			this.menuItem_config_graph_all.setActionCommand(InterfaceVista.ConfigGraph);
			this.menuItem_config_setRangeTime.setActionCommand(InterfaceVista.ConfigTimeRange);
			this.menuItem_config_setFs.setActionCommand(InterfaceVista.ConfigSamplingRate);
			// SERIAL
			this.menuItem_config_serial_baudRate.setActionCommand(InterfaceVista.ConfigSetBaudRate);
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
		panel_principal.setBackground(new Color(23,100,105));
	}
	
	// Instanciacion configuracion y distribucion de los objetos del panel
	private void setObjects() {
		
		
		// GridBagConstraints es una clase que en sus atributos contiene la informacion de la ubicacion de 
		// los objetos dentro del panel
		
		GridBagConstraints c = new GridBagConstraints();	

		// SECCION CONEXION SERIAL
		
		// CAMPO DE TEXTO
		
		// Instanciacion del Campo de texto para la conexion serial
		text_port = new JTextField("COM1");	// se instancia el TextField con el texto COM 1 por defecto
		text_port.setEnabled(true);				// se habilita el ingreso de texto por parte del usuario		
		text_port.setToolTipText("Ingrese el puerto COM al que se ha conectado el dispositivo. Ej, COM1.");
		text_port.setActionCommand(ButtonConnectPushed);	// Si presiona enter es como si presionara el boton connect
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
		button_connect.setToolTipText("Presione para conectar con el puerto serie");
		button_connect.setActionCommand(ButtonConnectPushed);
		button_connect.setVisible(true);
		panel_principal.add(button_connect,c);		// se agrega al panel
		c.weightx = 0.0;
		
		button_disconnect = setButton(c,"DISCONNECT",3,0,1,1,0.0,10);
		button_disconnect.setVisible(false);
		button_disconnect.setToolTipText("Presione para desconectar el puerto serie");
		button_disconnect.setActionCommand(ButtonDisconnectPushed);

		panel_principal.add(button_disconnect,c);		// se agrega al panel
		c.weightx = 0.0;
		
		// Instanciacon del boton start
		button_start = setButton(c,"START",2,1,2,1,0.0,20);
		c.anchor = GridBagConstraints.NORTH;
		button_start.setToolTipText("Presione para comenzar la captura de datos");
		button_start.setActionCommand(ButtonStartPushed);
		panel_principal.add(button_start,c);		// se agrega al panel
		c.weightx = 0.0;
		
		
		
		
		
		// SECCION DE LA GRAFICA
		
		
		setGraph(c,1,0,1,2,1.0,1.0,10);	// frame_graph es un JInternalFrame
		configChart(graph_frame,"Signal name");
		panel_principal.add(graph_frame,c);
		

		c.weightx = 0.0;
		c.weighty = 0.0;
		
		
		
		
		
		// SECCION CONSOLA
		
		frame_console = setConsole(c,"Console",0,2,4,1,1.0,0.3,10);
		c.ipady = 60;
		configConsole(frame_console);
		
		
		panel_principal.add(frame_console, c);
		

	}
		

	

	
	
	// Crea, configura y ubica un boton
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
	
	
	@Override 
	public void setButtonEnable(String button,boolean option) {
		switch (button) {
		case InterfaceVista.ButtonStartEnable:
			this.button_start.setEnabled(option);
			break;
		
		case InterfaceVista.ButtonConnectEnable:
			this.button_connect.setEnabled(option);
			break;
		}
	}
	
	

	
	
	// SECCION VENTANA EMERGENTE CONFIGURACION DE GRAFICA
	
		
	private void setConfigGraph() {
		    frame_conf_graph = new JFrame();
			JPanel panel_p = new JPanel();
			panel_p.setLayout(new GridBagLayout());
			
		    Toolkit mi_pantalla = Toolkit.getDefaultToolkit();	//en mi_pantalla (variable objeto) guardamos el sistema nativo de ventanas
		    Dimension dim = mi_pantalla.getScreenSize();
			
			
				
			frame_conf_graph.getContentPane().setLayout(new FlowLayout());
			
			setConfigGraphObjects(panel_p);
			frame_conf_graph.getContentPane().add(panel_p);
		
			frame_conf_graph.setSize(dim.width/4,dim.height/3);
			frame_conf_graph.setLocation(dim.width/3,dim.height/3);
			frame_conf_graph.setTitle("Chart configuration");
			frame_conf_graph.setResizable(false);
	}
	
private void setConfigGraphObjects(JPanel panel_principal) {
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5,5,5,5); // se aplica a todos
		c.anchor = GridBagConstraints.WEST;	// se aplica a todos
		
		// SIGNAL NAME
		
		jl_signal_name = new JLabel("Signal name");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jl_signal_name,c);
		
		jtf_set_signal_name = new JTextField("Signal name",10);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		panel_principal.add(jtf_set_signal_name,c);
		
		jb_add_signal_name = new JButton("SET");
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jb_add_signal_name,c);
		
		
		// TIME RANGE
		
		
		jl_time_range = new JLabel("Time range");
		c.gridx=0;
		c.gridy=1;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jl_time_range,c);
		
		jtf_set_time_range = new JTextField("1",10);
		c.gridx=1;
		c.gridy=1;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jtf_set_time_range,c);
		
		jcb_time_units = new JComboBox();
		c.gridx = 2;
		c.gridy=1;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jcb_time_units,c);
		
		jb_add_time = new JButton ("SET");
		c.gridx = 3;
		c.gridy=1;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jb_add_time,c);
		
		
		// SAMPLE RATE
		
		jl_sample_rate = new JLabel("Sample rate");
		c.gridx=0;
		c.gridy=2;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jl_sample_rate,c);
		
		jtf_set_sample_rate = new JTextField("1",10);
		c.gridx=1;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jtf_set_sample_rate,c);
		
		jcb_sample_rate_units = new JComboBox();
		c.gridx=2;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jcb_sample_rate_units,c);
		
		jb_add_sample_rate = new JButton("SET");
		c.gridx=3;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel_principal.add(jb_add_sample_rate,c);
		
		
			// CLOSE
		
		jb_close = new JButton("Close");
		c.gridx=0;
		c.gridy = 3;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(75,5,5,5);
		panel_principal.add(jb_close,c);
		
		
		
		jcb_sample_rate_units.addItem("Hz");
		jcb_sample_rate_units.addItem("kHz");
		jcb_sample_rate_units.addItem("MHz");
		jcb_sample_rate_units.addItem("GHz");
		
		jcb_time_units.addItem("nS");
		jcb_time_units.addItem("uS");
		jcb_time_units.addItem("mS");
		jcb_time_units.addItem("S");
		
		jcb_time_units.setSelectedIndex(2);
		jcb_sample_rate_units.setSelectedIndex(1);
		
		
		// Agregado de action commands a los botones
		this.jb_close.setActionCommand(InterfaceVista.ConfigGraphClose);
		this.jb_add_time.setActionCommand(InterfaceVista.ConfigGraphAddTime);
		this.jb_add_sample_rate.setActionCommand(InterfaceVista.ConfigGraphAddSampleRate);
		this.jb_add_signal_name.setActionCommand(InterfaceVista.ConfigGraphAddSignalName);
		
		this.jtf_set_time_range.setActionCommand(InterfaceVista.ConfigGraphAddTime);
		this.jtf_set_sample_rate.setActionCommand(InterfaceVista.ConfigGraphAddSampleRate);
		this.jtf_set_signal_name.setActionCommand(InterfaceVista.ConfigGraphAddSignalName);
		
	}
	
	
	
	// SECCION MANEJO DE CONSOLE
	
	@Override
	public void writeConsole(String line) {
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		console.setText(console.getText()+"["+hourFormat.format(date)+"] >> "+line+"\r\n");
	}
	
	@Override
	public void deleteConsole() {
		console.setText(" ");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// METODOS DE INTERFACE VISTA
	
	@Override
	public void setControlador(Controlador c) {
		
		// Serial communication section
		button_connect.addActionListener(c);
		button_disconnect.addActionListener(c);
		button_start.addActionListener(c);
		text_port.addActionListener(c);
		
		// file section
		this.menuItem_file_openFile.addActionListener(c);
		this.menuItem_file_saveAs.addActionListener(c);
		
		// view section
		menuItem_view_SerialPorts.addActionListener(c);
		this.menuItem_view_graph_Fs.addActionListener(c);
		this.menuItem_view_graph_getRangeTime.addActionListener(c);
		this.menuItem_view_graph_graphData.addActionListener(c);
		// Math section
		this.menuItem_math_fft_calculate.addActionListener(c);
		this.menuItem_math_fft_graph_module.addActionListener(c);
		this.menuItem_math_fft_graph_angle.addActionListener(c);
		// Config section
			
			//CONFIG GRAPH
			this.menuItem_config_setFs.addActionListener(c);
			this.menuItem_config_setRangeTime.addActionListener(c);
			this.menuItem_config_graph_all.addActionListener(c);
			
			//CONFIG SERIAL
			this.menuItem_config_serial_baudRate.addActionListener(c);
			
			
		// file section
		menuItem_file_exit.addActionListener(c);
		
		// Seccion Config Graph
		this.jb_close.addActionListener(c);
		this.jb_add_sample_rate.addActionListener(c);
		this.jb_add_signal_name.addActionListener(c);
		this.jb_add_time.addActionListener(c);
		this.jtf_set_signal_name.addActionListener(c);
		this.jtf_set_time_range.addActionListener(c);
		this.jtf_set_sample_rate.addActionListener(c);
	
	}
	
	// Crea el frame interno donde estará la consola
	private JInternalFrame setConsole(GridBagConstraints c,String name,int gridx, int gridy,int gridwidth, int gridheight,double weightx,double weighty, int gap) {
		
		JInternalFrame console = new JInternalFrame(name);
		console.setVisible(true);
		c.gridx = gridx; 		// El campo de texto empieza en la columna cero.
		c.gridy = gridy; 		// El campo de texto empieza en la fila cero
		c.gridwidth = gridwidth; 	// El campo de texto ocupa una columna.
		c.gridheight = gridheight;   // El campo de texto ocupa una fila.
		c.weightx = weightx;
		c.weighty = weighty;
		c.insets = new Insets(gap,gap,gap,gap);	
				
		return console;
		
	}
	
	private void configConsole(JInternalFrame frame_console) {
		
		 // set flow layout for the frame  
       frame_console.getContentPane().setLayout(new BorderLayout());  
       
       console = new JTextArea(JFrame.MAXIMIZED_HORIZ,JFrame.MAXIMIZED_VERT); 
       
       JScrollPane scrollableTextArea = new JScrollPane(console);  
       
 
       scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
       scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
       
       console.setEditable(false);
       
       frame_console.getContentPane().add(scrollableTextArea); 
		
	}
	
	
	// Se configura el grafico y se agrega en el layout
	private void configChart(JInternalFrame frame_graph,String chart_name) {
		
		frame_graph.getContentPane().setLayout(new BorderLayout());
		grafica = new XYchart();
		grafica.createDataset();
		grafica.startChart(chart_name);

		frame_graph.getContentPane().add(grafica.getChartPanel());

		//frame_graph.setVisible(true);
		
	}

	
	// Configura el JInternalFrame que contendrá la grafica 
	private void setGraph(GridBagConstraints c,int gridx, int gridy,int gridwidth, int gridheight,double weightx,double weighty, int gap) {
		graph_frame = new JInternalFrame();
	    graph_frame.setVisible(true);
		c.gridx = gridx; 		// El campo de texto empieza en la columna cero.
		c.gridy = gridy; 		// El campo de texto empieza en la fila cero
		c.gridwidth = gridwidth; 	// El campo de texto ocupa una columna.
		c.gridheight = gridheight;   // El campo de texto ocupa una fila.
		c.weightx = weightx;
		c.weighty = weighty;
		c.insets = new Insets(gap,gap,gap,gap);	
		c.fill = GridBagConstraints.BOTH;
		
		
		//return graph_frame;
	}
	
	// option = 0 Guardar archivo
	// option = 1 Abrir archivo
	@Override
	public String fileWindow(byte option) {
		 String status = null;
		 try {
			 JFileChooser file=new JFileChooser();
			 switch(option) {
			 	case InterfaceVista.optionSaveFile: 
			 		 if(file.showSaveDialog(this) == file.CANCEL_OPTION) return "_CANCEL_";
					 break;
					 
			 	case InterfaceVista.optionOpenFile:
			 		 if(file.showOpenDialog(this) == file.CANCEL_OPTION) return "_CANCEL_";
			 		 break;
			 		 
			 }
			 status = file.getSelectedFile().toString();
			 
		 }catch(Exception e) {
			 e.printStackTrace();
			 status = null;
		 }
		 return status;
	}
	
	
	/*public void setConfigGraph() {
		frame_conf_graph = new ConfigGraph();
		frame_conf_graph.setVisible(false);
	}*/
	
	public void showConfigGraph() {
		frame_conf_graph.setVisible(true);
	}
	
	@Override
	public void closeConfigGraph() {
		frame_conf_graph.setVisible(false);
	}

	@Override
	public String getConfigSampleRate() {
		return this.jtf_set_sample_rate.getText();
	}
	
	@Override
	public String getSampleRateUnits() {
		return (String)this.jcb_sample_rate_units.getSelectedItem();
	}


	@Override
	public String getConfigTimeRange() {
		return this.jtf_set_time_range.getText();
	}


	@Override
	public String getTimeRangeUnits() {
		return (String)this.jcb_time_units.getSelectedItem();
	}

	@Override
	public void setSignalName(String name) {
		grafica.actualiceTitle(name);
		
	}

	@Override
	public void actualiceChartData(String signal_name,double[][] data) {
		grafica.actualiceDataset(signal_name,data);
	}

	@Override 
	public void deleteChartData() {
		grafica.deleteDataset();
	}
	
	@Override
	public String getNewSignalName() {
		return this.jtf_set_signal_name.getText();
				
	}

	@Override
	public String getPortName() {
		return this.text_port.getText();
				
	}
	
	@Override
	public void viewDataset() {
		grafica.viewDataset();
	}
	
	@Override
	public double getDatasetItem(int index) {
		return grafica.getDataItem(index);
	}

	@Override
	public void buttonSetVisible(String button) {
		switch(button) {
		case "connect":
			this.button_connect.setVisible(true);
			this.button_disconnect.setVisible(false);
			

			break;
		case"disconnect":
			this.button_connect.setVisible(false);
			this.button_disconnect.setVisible(true);
			this.button_start.setVisible(true);
			break;
		
		case "start_pushed":	
			this.button_start.setVisible(false);
			
			
		}
	}
	
	// retorna -1 si el dato ingresado no es un entero valido
	@Override 
	public int getBaudRate() {
		String cad = null; 
		cad = JOptionPane.showInputDialog("Baud Rate [bauds]");
		if (cad == null) { // significa que presiono cancelar
			return -2;
		}else if(isNumeric(cad) && Integer.parseInt(cad)>0) {
			return(Integer.parseInt(cad));
		}else {
			return -1;
		}
	}
	
	private boolean isNumeric(String cadena) {
		try {	
			Integer.parseInt(cadena);
        	return true;
    	} catch (NumberFormatException excepcion) {
        	return false;
		}
	}
}





