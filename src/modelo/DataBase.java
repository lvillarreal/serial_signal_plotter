package modelo;

import java.util.ArrayList;

public class DataBase implements InterfaceModelo {
	
	private FileHandler file_handler;	// clase para el manejo de archivos
	
	private FFT fft;
	
	private String sample_rate_units; // unidades de frecuencia de muestreo
	private String time_units;	// unidades del rango de tiempo
	private double time_range;	// rango de tiempo de la grafica
	private double fs;			// frecuencia de muestreo
	private double Ts;			// periodo de muestreo
	private String signal_name;	// nombre de la se�al
	
	private String serialPort;

	private static String FILE_NAME;
	private static String fftModule_FILE_NAME;
	private static String fftModule_FILE_NAME_shifted;
	
	public DataBase() {
		this.fft = new FFT();
		this.file_handler = new FileHandler();
		
		// valores por defecto
		this.signal_name = "Signal name";
		this.fs = 1;
		this.Ts = 1/fs;
		this.time_range = 1;
		this.time_units = "ms";
		this.sample_rate_units = "kHz";
		this.serialPort = null;
		
		FILE_NAME = "files/DataBase.txt";
		fftModule_FILE_NAME = "files/signal_fft_module_noshifted.txt";
		fftModule_FILE_NAME_shifted= "files/signal_fft_module.txt";
		
	}
	
	
	@Override
	public byte calculateFFT() {
		byte status=-1;
		status = fft.fft(FILE_NAME,fftModule_FILE_NAME);
		if(status != InterfaceModelo.fftCalculateOk ) return status;
		
		//return fft.fftShift(fftModule_FILE_NAME, fftModule_FILE_NAME_shifted);
		return status;
	}
	
	@Override
	public String getSampleRateUnits() {
		return this.sample_rate_units;
	}
	
	@Override
	public double getTimeRange() {
		return this.time_range;
	}
	@Override
	public double getSamplingRate() {
		return this.fs;
	}


	@Override
	public String getTimeUnits() {
		return time_units;
	}
	
	@Override
	public String getSignalName() {
		return this.signal_name;
	}
	
	@Override
	public void setTimeRange(double time) {
		time_range = time;
		
	}
	@Override
	public void setFs(double fs) {
		this.fs = fs;
		this.Ts = 1/fs;
		
	}
	
	@Override
	public void setSignalName(String name) {
		this.signal_name = name;
		
	}
	
	

	@Override
	public byte openFile(byte option) {
		switch(option){
		case InterfaceModelo.fileFFTmodule:
			return file_handler.openFile(fftModule_FILE_NAME);
		
		case InterfaceModelo.fileData:
			return file_handler.openFile(FILE_NAME);
			
		case InterfaceModelo.fileFFTshiftedModule:
			return file_handler.openFile(this.fftModule_FILE_NAME_shifted);
		}
		return 0;
	}
	
	@Override 
	public byte closeFile() {
		return file_handler.closeFile();
	}
	@Override
	public String readLine() {
		return file_handler.readLine();
	}
	

	
	@Override
	public void setSampleRateUnits(String units) {
		this.sample_rate_units = units;
	}
	
	@Override
	public void setTimeUnits(String units) {
		this.time_units = units;
	}
	
	@Override
	public String getFileName(byte option) {
		switch (option) {
		case InterfaceModelo.fileData:
			return this.FILE_NAME;
			
		
		case InterfaceModelo.fileFFTmodule:
			return this.fftModule_FILE_NAME;
			
		case InterfaceModelo.fileFFTshiftedModule:
			return this.fftModule_FILE_NAME_shifted;
			
		}
		return "";
	}
	
}