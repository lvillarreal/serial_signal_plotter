package modelo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import gnu.io.SerialPort;

public interface InterfaceModelo {

	
	 void setFs(double fs);
	 void setSignalName(String name);
	 void setPortName(String port_name);
	 void setCantBits(int bits);
	 void setInputRange(double input_range);
	 void setData(byte MSB,byte LSB, int i) throws Exception;
	 void setCantMuestras(int cant);
	 void setDate(String date);
	 void setUserText(String text);
	 void calculateAllFeatures();
	 
	 
	 void resetData();
	 void calculateFFT()throws IOException, FileNotFoundException;
	 void saveData(String file) throws FileNotFoundException, IOException;
	 void openFile(String file) throws FileNotFoundException, IOException, ClassNotFoundException;
	 void removeFFTfiles();
	 //byte openFile(byte option);
	 //byte closeFile();
	 //String readLine();
	 
	 int getCantBits();
	 String getSignalName();
	 double getSamplingRate();
	 String getPortName();
	 double getInputRange();
	 double[][] getData();
	 int getCantMuestras();
     String getDate();
     String obtainDate();
     String getUserText();
     double getCCvalue();
     double getMaxValue();
     double getMinValue();
     double getRMSvalue();
     double[][] getFFT(String option) throws IOException, FileNotFoundException;
     void setImportData(String file) throws FileNotFoundException, IOException, Exception;
     //double[] getOnlyData();
     
     	final static String fft_module_file = "fft_module.bin";
     	final static String fft_phase_file = "fft_phase.bin";
     	final static byte 	fft_module	=	(byte)0;
     	final static byte 	fft_phase	=	(byte)1;
     	
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
