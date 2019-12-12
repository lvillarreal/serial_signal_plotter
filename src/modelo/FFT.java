package modelo;

public class FFT {

	private Complejo y_k;	//resultado y(k)
	//private String FILE_NAME;
	private FileHandler file_x;	// para abrir archivo donde se encuentra la senial x(n)
	private FileHandler file_y;	// para crear el archivo donde se guardara la fft de x(n)
	
	//private String file_fft_name;
	
	private long k,n,N;
	
	public FFT() {
		file_x = new FileHandler();
		file_y = new FileHandler();
		
	}
	
	// funcion que calcula la transformada rapida de fourier de la señal muestreada
	// El archivo debe tener la frecuencia de muestreo en la primer fila y las muestras en las filas subsiguientes
	public byte fft(String signal_FILE_NAME, String data_FILE_NAME) {
		
		double fs = 0;
		k = 0;
		n = 0;
		N = 1;
		String line = null;
		
		// inicializa el resultado complejo
		y_k = new Complejo();
		
		
		// Se abre el archivo donde estan los puntos de la senial x(n), si no se puede abrir se retorna con error
		if(file_x.openFile(signal_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;

		// Se obtiene la cantidad de muestras. Se resta 1 por que la primer linea es la frecuencia de muestreo, no es una muestra
		N = file_x.getCantLines()-1;
		// Controla que el valor sea correcto
		if (N == InterfaceModelo.OpenFileError)  return InterfaceModelo.OpenFileError;
		
		//Se cierra el archivo y se vuelve a abrir 
		//file_x.closeFile();
		//if(file_x.openFile(signal_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;
		
		file_x.FileReadReset();
		file_x.readLine();
		// Se crea el archivo para guardar la fft
		if (file_y.createFile(data_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.CreateFileError;
		

		
		line = file_x.readLine();	    // se lee la primer linea que es la frecuencia de muestreo
		fs = Double.parseDouble(line);	// se guarda la frecuencia de muestreo
		
		// Se escribe la fs en el archivo de FFT
		file_y.printLine(line);
		
		// Comienfa la transformacion
		
		// Y0 se trata diferente ya que es la suma de todos las muestras sin ponderar
		//calculateY0();
		
		//calculateYk();
		
		
		
		file_x.closeFile();
		file_y.closeFile();
		
		return 0;
		
	}
	
	private void calculateY0() {
		double y0 = 0;
		
		y0 =y0 + Double.parseDouble(file_x.readLine());
		//while ()
	}
	
	private void calculateYk() {
		Complejo exp = new Complejo();
		// factor exponencial
		//exp = Complejo.exponencial(new Complejo(0.0,-2*Math.PI*k*n/N))
	}

}




