package modelo;

/*CONSUME DEMASIADOS RECURSOS! SE PROPONE IMPLEMENTAR EL ALGORITMO COLEY-TUKEY. 
 * ES MEJOR REALIZARLO EN FPGA */

public class FFT {

	private Complejo y_k;	//resultado y(k)
	//private String FILE_NAME;
	private FileHandler file_x;	// para abrir archivo donde se encuentra la senial x(n)
	private FileHandler file_y;	// para crear el archivo donde se guardara la fft de x(n)
	private FileHandler file_shifted;
	
	//private String file_fft_name;
	
	private int k,N;
	
	public FFT() {
		file_x = new FileHandler();
		file_y = new FileHandler();
		file_shifted = new FileHandler();
		
	}
	
	// funcion que calcula la transformada rapida de fourier de la señal muestreada
	// El archivo debe tener la frecuencia de muestreo en la primer fila y las muestras en las filas subsiguientes
	public byte fft(String signal_FILE_NAME, String fft_FILE_NAME) {
		
		double fs = 0;
		k = 0;
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
		file_x.closeFile();
		if(file_x.openFile(signal_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;
		

		// Se crea el archivo para guardar la fft (no shifted)
		if (file_y.createFile(fft_FILE_NAME) == InterfaceModelo.CreateFileError) return InterfaceModelo.CreateFileError;
		

		
		line = file_x.readLine();	    // se lee la primer linea que es la frecuencia de muestreo
		fs = Double.parseDouble(line);	// se guarda la frecuencia de muestreo
		
		// Se escribe la fs en el archivo de FFT
		file_y.printLine(line);
		
		
		// Comienza la transformacion
		
		
		
		// Y0 se trata diferente ya que es la suma de todos las muestras sin ponderar
		calculateY0();
		file_y.printLine(String.valueOf(y_k.modulo()));
		
		file_x.closeFile();
		if(file_x.openFile(signal_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;
		line = file_x.readLine();	// se desprecia la primer linea
		
		while(k<N) {
			k=k+1;
			calculateYk();
			file_y.printLine(String.valueOf(y_k.modulo()));
			// cierra archivo y vuelve a abrirlo
			file_x.closeFile();
			if(file_x.openFile(signal_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;
			file_x.readLine();
		}
		
		
		
		//calculateYk();
		
		
		
		file_x.closeFile();
		file_y.closeFile();
		
		
		return InterfaceModelo.fftCalculateOk;
		
	}
	
	
	
	public byte fftShift(String fft_FILE_NAME, String fft_shifted_FILE_NAME) {
		int N1=0;
		String line=null;
		int i = 0;
		// abre el archivo donde se encuentra la fft
		if (file_y.openFile(fft_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;
		// obtiene la cantidad de muestras
		N = file_y.getCantLines()-1;
		// Si es impar lo convierte en par
		N1 = N/2;
		N = N1*2;

		//cierra el archivo
		file_y.closeFile();
		// Se vuelve a abrir
		if (file_y.openFile(fft_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;
		// Se crea el archivo para guardar la fft (shifted)
		if (file_shifted.createFile(fft_shifted_FILE_NAME) == InterfaceModelo.CreateFileError) return InterfaceModelo.CreateFileError;
		
		// lee la primer linea que es la frecuencia
		file_shifted.printLine(file_y.readLine());
		
		// recorre el archivo hasta la mitad
		for(i=1;i<N1;i++) {
			file_y.readLine();			
		}
		line = file_y.readLine();
		while(line != null) {
			file_shifted.printLine(line);
			line = file_y.readLine();
		}
		
		// aqui ya relleno la mitad del archivo
		file_y.closeFile();
		if (file_y.openFile(fft_FILE_NAME) == InterfaceModelo.OpenFileError) return InterfaceModelo.OpenFileError;
		
		for(i=0;i<N1;i++) line = file_y.readLine();
		
		line = file_y.readLine();
		
		while(i<=N+1 && line != null) {
			file_shifted.printLine(line);
			line = file_y.readLine();
			i = i+1;
		}
		
		file_y.closeFile();
		file_y.delete();
		
		file_shifted.closeFile();
	
		return InterfaceModelo.fftShiftedOk;
	}
	
	private void calculateY0() {
		double y0 = 0;
		Complejo aux = new Complejo();
		String line=null;
		line = file_x.readLine();
		while (line != null) {
			y0 =y0 + Double.parseDouble(line);
			line = file_x.readLine();
		}
		y_k = new Complejo(y0,0.0);		
		
	}
	
	private void calculateYk() {
		Complejo exp = new Complejo();
		double n=0;
		String line=null;
		
		line = file_x.readLine();
		y_k = new Complejo(Double.parseDouble(line),0.0);	// para n=0, el termino es x(0)
		line = file_x.readLine();
		while(line != null) {
			n = n+1;
			exp = Complejo.exponencial(new Complejo(0.0,-2*Math.PI*k*n/N));
			y_k = Complejo.suma(y_k,Complejo.producto(exp,Double.parseDouble(line)));
			line = file_x.readLine();
		}
		
				
	}

}




