package modelo;

public class FFT {

	private Complejo y_k;	//resultado y(k)
	//private String FILE_NAME;
	private FileHandler file_x;	// para abrir archivo donde se encuentra la senial x(n)
	private FileHandler file_y;	// para crear el archivo donde se guardara la fft de x(n)
	
	private String file_fft_name;
	
	public FFT() {
		file_x = new FileHandler();
		file_y = new FileHandler();
		file_fft_name = "files/signal_fft.txt";
	}
	
	// funcion que calcula la transformada rapida de fourier de la señal muestreada
	
	public int fft(String signal_FILE_NAME) {
		double fs;
		long k = 0,n = 0,N = 1;
		String line;
		
		// inicializa el resultado complejo
		y_k = new Complejo();
		
		// Se abre el archivo donde estan los puntos de la senial x(n), si no se puede abrir se retorna con error
		if(file_x.openFile(signal_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;

		// Se obtiene la cantidad de muestras
		N = file_x.getCantLines();
		// Controla que el valor sea correcto
		if (N == InterfaceModelo.OpenFileError)  return InterfaceModelo.OpenFileError;
		
		//Se cierra el archivo y se vuelve a abrir 
		file_x.closeFile();
		if(file_x.openFile(signal_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;

		// Se crea el archivo para guardar la fft
		if (file_y.createFile(file_fft_name) == InterfaceModelo.OpenFileError) return InterfaceModelo.CreateFileError;
		

		

		
		
		line = file_x.readLine();	// se lee la primer linea que es la frecuencia de muestreo
		System.out.println(line);
		fs = Double.parseDouble(line);	// se guarda la frecuencia de muestreo
		
		file_y.printLine(line);
		
		
		
		file_x.closeFile();
		file_y.closeFile();
		
		return 0;
		
	}
	
}




