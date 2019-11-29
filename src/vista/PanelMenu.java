package vista;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/*
public class PanelMenu extends JPanel {
	// OBJETOS RELACIONADOS AL MENU 
	
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

	
	//Constructor
	public PanelMenu() {
		// setMenu crea el menu
		setMenu();
	}
	

	// Metodos
	
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
	
	//metodo getter para retornar la barra
	public JMenuBar get_barra() {
		return barra;
	}
	
}*/
