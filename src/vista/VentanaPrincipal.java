package vista;

import javax.swing.*;
import java.awt.*;

// MARCO o FRAME
public class VentanaPrincipal extends JFrame {


	// Constructor
	public VentanaPrincipal() {

		startFrame("Siglan plotter");
		
	}

	// Metodos
	
	/*	startFrame inicializa el frame (marco) 
	 */
	public void startFrame(String title) {
		PanelPrincipal panel_principal = new PanelPrincipal();
		this.add(panel_principal);
		this.setJMenuBar(panel_principal.get_barra());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);	// el marco se abre con ventana completa
		this.setTitle(title);
		this.setVisible(true);
		
		
	}
}


// LAMINA o PANEL
class PanelPrincipal extends JPanel {

	/* OBJETOS RELACIONADOS AL MENU */
	
	JMenuBar barra;		// Objeto que almacena la barra de menu
	
	// Opciones del menu
	JMenu menu_file;
//	JMenu menu_edit;
	JMenu menu_view;
	JMenu menu_help;
	
	//Items de cada opcion
	//Items de file
	JMenuItem menuItem_save;
	JMenuItem menuItem_saveAs;
	JMenuItem menuItem_import;
	JMenuItem menuItem_export;
	JMenuItem menuItem_exit;
	
	//Items de edit
	
	//Items de view
	JMenuItem menuItem_graph;
	JMenuItem menuItem_math;
		
	//Items de help
	JMenuItem menuItem_helpContent;
	JMenuItem menuItem_about;

	/* OBJETOS RELACIONADOS A LA COMUNICACION SERIE */
	
	JTextField puertoCOM;	// El usuario ingresa el puerto COM al que se desea conectar
	JButton buttonConnect;	// Boton para conectar con puerto COM
	
	public PanelPrincipal() {
		// setMenu crea el menu
		setMenu();
		// setSerieConection crea la seccion para la conexion serie
		setSerieConection();

	}
	
	private void setSerieConection() {
			
		puertoCOM = new JTextField("COM 1");

		puertoCOM.setEditable(true);

		add(puertoCOM);
		puertoCOM.setLocation(300, 10);
		//puertoCOM.setVisible(true);
		


	}
	
	private void setMenu() {
		// Inicializacion JMenuBar
		barra = new JMenuBar();
		
		// Inicializacion JMenu
		menu_file   = new JMenu("File");
//		menu_edit = new JMenu("Edit");
		menu_view   = new JMenu("View");
		menu_help 	= new JMenu("Help");
		
		// Inicializacion JMenuItem
			//Items de file
		menuItem_save = new JMenuItem("Save");
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
		menu_file.addSeparator();
		menu_file.add(menuItem_saveAs);
		menu_file.addSeparator();
		menu_file.add(menuItem_import);
		menu_file.addSeparator();
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
	
	public JMenuBar get_barra() {
		return barra;
	}
	
}