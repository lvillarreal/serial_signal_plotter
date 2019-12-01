
import controlador.Controlador;
import vista.*;
import modelo.*;

public class Main {
	public static void main(String args[]) {
		InterfaceVista vista = new FramePrincipal();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo,vista);
		
		// inicializa ventana principal
		vista.start();
		
		vista.setControlador(controlador);

		
	}
}
