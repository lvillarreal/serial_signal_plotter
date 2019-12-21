package modelo;

 public interface InterfaceModelo {

	
	 void setTimeRange(double time);
	 void setFs(double fs);
	 void setSignalName(String name);
	 void setSampleRateUnits(String units);
	 void setTimeUnits(String units);
	 
	 byte calculateFFT();
	 byte openFile(byte option);
	 byte closeFile();
	 String readLine();
	 String getFileName(byte option);
	
	 String getSignalName();
	 String getTimeUnits();
	 double getTimeRange();
	 double getSamplingRate();
	 String getSampleRateUnits();

	 
		
		final static byte OpenFileSuccessfully = 0;
		final static byte closeFileSuccessfully = 0;
		final static byte OpenFileError = -1;
		final static byte CloseFileError = -1;
		final static byte CreateFileSuccessfully = 2;
		final static byte CreateFileError = -2;
		
		final static byte printLineSuccessfully = 0;
		final static byte printLineError = -1;
		
		final static byte CloseFileReader = 0;
		final static byte CloseFileWriter = 1;

		final static byte fileData = 0;
		final static byte fileFFTmodule = 1;
		final static byte fftCalculateOk = 2;	// indica que la fft se calculo correctamente
		final static byte fftShiftedOk = 3;		// indica que realizo bien el shift
		final static byte fileFFTshiftedModule = 4;
		
}
