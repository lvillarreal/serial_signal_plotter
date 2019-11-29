
import controlador.Controlador;
import vista.FramePrincipal;
import modelo.*;

public class Main {
	public static void main(String args[]) {
		FramePrincipal vista = new FramePrincipal();
		Modelo modelo = new Modelo();
		Controlador controlador = new Controlador(modelo,vista);
		
		// inicializa ventana principal
		vista.start();
		// inicializa vista
		vista.setControlador(controlador);
		
		
	}
}
