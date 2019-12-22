package controlador;

import java.awt.event.*;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.JOptionPane;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import modelo.*;
import vista.*;



public class Controlador implements ActionListener, SerialPortEventListener{
	
	
	private InterfaceModelo modelo;
	private InterfaceVista vista;
	
	private SerialCommunication serial_comm;

	
	public Controlador(InterfaceModelo modelo, InterfaceVista vista) {
		this.modelo = modelo;
		this.vista = vista;
		serial_comm = new SerialCommunication();	// instanciacion de serialCommunication

	}

		@Override 
		public void actionPerformed(ActionEvent e)  {
			
			Runnable r_barOptions = new barOptions(vista,modelo,serial_comm,e.getActionCommand());
			Thread t_barOptions = new Thread(r_barOptions);
			
			Runnable r_serialComm = new SerialComm(vista,modelo,serial_comm,this,e.getActionCommand());
			Thread t_serialComm = new Thread(r_serialComm);
			
			if (e.getActionCommand().equals(InterfaceVista.ButtonConnectPushed)){
				t_serialComm.start();				
			}else if(e.getActionCommand().equals(InterfaceVista.ListSerialPorts)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.MenuButtonExitPushed)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigTimeRange)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigSamplingRate)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.GetSamplingRate)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.GetTimeRange)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigGraph)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigGraphClose)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigGraphAddSampleRate)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigGraphAddTime)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigGraphAddSignalName)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ButtonStartPushed)){
				t_serialComm.start();
			}else if(e.getActionCommand().equals(InterfaceVista.CalculateFFT)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.GraphFFTmodule)){
				t_barOptions.start();
				
			}
			
		}

		@Override
		public void serialEvent(SerialPortEvent oEvent) {
			int index = 0;
			
		    while (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

	            try {
	                int datos; //Se declaran las variables
	                datos = serial_comm.readData(); //Se lee los datos en el puerto serie

	                if (datos > 0) { //Si el valor leido es mayor a 0...
	                    serial_comm.setMensaje(serial_comm.getMensaje()+(char)datos); //Se acumula el mensaje

	                    if (serial_comm.getMensaje().charAt(serial_comm.getMensaje().length() - 1) == ',') { //Cuando se recibe la coma
	                        //el mensaje ha llegado a su final, por lo que se procede a imprimir
	                        //La parte ENTERA de la humedad. Se busca el punto, donde quiera que esté
	                        //y se transforma de String a entero
	                     
	                    	for (int i = 0; i <= serial_comm.getMensaje().length() - 1; i++) {
	                            if (serial_comm.getMensaje().charAt(i) == '.') {
	                                serial_comm.setDatoEntrada(Integer.parseInt(serial_comm.getMensaje().substring(0, i)));
	                                System.out.println(serial_comm.getDatoEntrada());

	                                serial_comm.setMensaje(""); //Se limpia la variable y se prepara para nueva lectura
	            		            
	                                vista.actualiceChartData(((double )index)/modelo.getSamplingRate(), (double)serial_comm.getDatoEntrada());
	            		            index = index+1;
	                            }
	                        }
	                    }
	                
	                }
		            

	            } catch (Exception e) {
	                System.err.println(e.toString());
	            }

		    }
			
			
		}

		

}

		
// Implementa las acciones que se realizan en la comunicacion serial. Lectura/escritura del puerto
class SerialComm implements Runnable{
	
	private InterfaceVista vista;
	private InterfaceModelo modelo;
	private String option;
	private SerialCommunication serial_comm;
	private Controlador c;
	
	public SerialComm(InterfaceVista vista,InterfaceModelo modelo,SerialCommunication serial_comm,Controlador c,String option) {
		this.vista = vista;
		this.modelo = modelo;
		this.option = option;
		this.serial_comm = serial_comm;
		this.c = c;
	}
	
	 public void run() {	// metodo de interface runnable
		switch(option) {
		case InterfaceVista.ButtonConnectPushed:
			buttonConnect();
			break;
			
		case InterfaceVista.ButtonStartPushed:	
			actualiceChart();
			break;
		}
	}
	

	 private void actualiceChart() {
		
			int status;
			String line = null;
			int index = 0;

			
			double fs = modelo.getSamplingRate();

			// Se abre el archivo
			status = modelo.openFile(InterfaceModelo.fileData);
			
			if (status == InterfaceModelo.OpenFileSuccessfully) {	// si se abre satisfactoriamente
				vista.writeConsole("File \""+modelo.getFileName(InterfaceModelo.fileData)+"\" opened successfully");
				
				// inhabilita el boton de start
			    vista.setButtonEnable(InterfaceVista.ButtonStartEnable, false);
			    
				// Se lee la frecuencia de muestreo que el sistema esta utilizando
				fs = Double.parseDouble(modelo.readLine());
				vista.writeConsole("Sample rate: "+fs + " [Hz]");
				
				modelo.setFs(fs); // Se guarda la frecuencia de muestreo
				modelo.setSampleRateUnits("Hz");
				
				// Se borran los datos actuales del grafico
				vista.deleteChartData();
				
				// Se leen los datos  del archivo
				
				line = modelo.readLine();
				//vista.writeConsole(line);
		
				while (line != null) {
					vista.actualiceChartData(((double )index)/fs, Double.parseDouble(line));
					index =index + 1;
					line = modelo.readLine();
					//vista.writeConsole(String.valueOf(((double )index)/fs)+" ; "+line );
				}
				
				// Se cierra el archivo
				status = modelo.closeFile();
				
				if(status == InterfaceModelo.closeFileSuccessfully) {	// si el archivo se cierra correctamente
					vista.writeConsole("File \""+modelo.getFileName(InterfaceModelo.fileData)+"\" closed successfully" );
					
					// se habilita el boton start
					vista.setButtonEnable(InterfaceVista.ButtonStartEnable, true);
				}else if(status == InterfaceModelo.CloseFileError) {	
					vista.writeConsole("ERROR. File \""+modelo.getFileName(InterfaceModelo.fileData)+"\" cannot be closed" );
				}
			}else {	// el archivo no se abrio correctamente
				vista.writeConsole("ERROR. File \""+modelo.getFileName(InterfaceModelo.fileData)+"\" cannot be opened" );
				
			}
			
	}
			
	
	
	private void buttonConnect() {
		vista.deleteChartData();
		modelo.setPortName(vista.getPortName());	//Se guarda el puerto ingresado  por el usuario
		if(serial_comm.portConnect(c, modelo.getPortName()) == -1)	vista.writeConsole("Cannot connect to port "+vista.getPortName());
		else vista.writeConsole(vista.getPortName()+" connection successfully");
		
	}
	


}



// Implementa lo relacionado a la barra de menu
class barOptions implements Runnable{
	
	// atributos para setear valores de la grafica
	private String dataIn;
	private double timeRange;
	private double fs;
	
	// otros atributos
	private String option;
	private InterfaceVista vista;
	private InterfaceModelo modelo;
	private SerialCommunication serial_comm;
	
	public barOptions(InterfaceVista vista,InterfaceModelo modelo,SerialCommunication serial_comm,String option) {
		this.option = option;
		this.vista = vista;
		this.modelo = modelo;
		this.serial_comm = serial_comm;
	}
	
	public void run() {
		switch(option){
			case InterfaceVista.MenuButtonExitPushed:
				exitProgram();
				break;
				
			case InterfaceVista.ListSerialPorts:
				listSerialPorts();
				break;	
				
			case InterfaceVista.ConfigTimeRange:
				setTimeRange(option);
				break;
				
			case InterfaceVista.ConfigSamplingRate:
				setSampleRate(option);
				break;
				
			case InterfaceVista.GetSamplingRate:
				getSamplingRate();
				break;
				
			case InterfaceVista.GetTimeRange:
				getTimeRange();
				break;
				
			case InterfaceVista.ConfigGraph:
				setConfigGraph();
				break;
				
			case InterfaceVista.ConfigGraphClose:
				closeConfigGraph();
				break;
				
			case InterfaceVista.ConfigGraphAddSampleRate:
				setSampleRate(option);
				break;
				
			case InterfaceVista.ConfigGraphAddTime:
				setTimeRange(option);
				break;
				
			case InterfaceVista.ConfigGraphAddSignalName:
				setSignalName();
				break;
				
			case InterfaceVista.CalculateFFT:
				calculateFFT();
				break;
				
			case InterfaceVista.GraphFFTmodule:
				actualiceChartFFT(InterfaceModelo.fileFFTmodule);
				break;
				

		}
	}
	

	private void calculateFFT() {
		vista.writeConsole("FFT proccessing");
		byte status = modelo.calculateFFT();
		switch(status) { 
		case InterfaceModelo.OpenFileError:
			vista.writeConsole("ERROR. \""+modelo.getFileName(InterfaceModelo.fileData)+"\" cannot be opened");
			break;
			
		case InterfaceModelo.CreateFileError:
			vista.writeConsole("ERROR. \""+modelo.getFileName(InterfaceModelo.fileFFTmodule)+"\" cannot be opened");
			break;
			
		case InterfaceModelo.fftCalculateOk:
			vista.writeConsole("FFT calculated successfully");
		}

	}
	
	
	
	private void actualiceChartFFT(byte file) {
		
		int status;
		String line = null;
		int index = 0;

		
		//double fs = modelo.getSamplingRate();

		// Se abre el archivo
		status = modelo.openFile(file);
		
		if (status == InterfaceModelo.OpenFileSuccessfully) {	// si se abre satisfactoriamente
			vista.writeConsole("File \""+modelo.getFileName(InterfaceModelo.fileFFTshiftedModule)+"\" opened successfully");
			
			// inhabilita el boton de start
		    vista.setButtonEnable(InterfaceVista.ButtonStartEnable, false);
		    
		    
			// Se lee la frecuencia de muestreo que el sistema esta utilizando
			fs = Double.parseDouble(modelo.readLine());
			vista.writeConsole("Sample rate: "+fs + " [Hz]");
			
			//modelo.setFs(fs); // Se guarda la frecuencia de muestreo
			//modelo.setSampleRateUnits("Hz");
			
			// Se borran los datos actuales del grafico
			vista.deleteChartData();
			
			// Se leen los datos  del archivo
			
			line = modelo.readLine();
			//vista.writeConsole(line);
	
			while (line != null) {
				vista.actualiceChartData(((double )index)*fs, Double.parseDouble(line));
				index =index + 1;
				line = modelo.readLine();
				//vista.writeConsole(String.valueOf(((double )index)/fs)+" ; "+line );
			}
			
			// Se cierra el archivo
			status = modelo.closeFile();
			
			if(status == InterfaceModelo.closeFileSuccessfully) {	// si el archivo se cierra correctamente
				vista.writeConsole("File \""+modelo.getFileName(InterfaceModelo.fileFFTmodule)+"\" closed successfully" );
				
				// se habilita el boton start
				vista.setButtonEnable(InterfaceVista.ButtonStartEnable, true);
			}else if(status == InterfaceModelo.CloseFileError) {	
				vista.writeConsole("ERROR. File \""+modelo.getFileName(InterfaceModelo.fileFFTmodule)+"\" cannot be closed" );
			}
		}else {	// el archivo no se abrio correctamente
			vista.writeConsole("ERROR. File \""+modelo.getFileName(InterfaceModelo.fileData)+"\" cannot be opened" );
			
		}
		
	}
	
	
	private void setSignalName() {
		//vista.setSignalName(vista.getNewSignalName());
		modelo.setSignalName(vista.getNewSignalName());
		vista.setSignalName(modelo.getSignalName());
	}
	
	private void closeConfigGraph() {
		vista.closeConfigGraph();
	}
	
	private void setConfigGraph() {
		vista.showConfigGraph();
	}
	
	// Muestra en consola la frecuencia de muestreo actual
	private void getSamplingRate() {
		vista.writeConsole("Sample rate: " + modelo.getSamplingRate() + " "+modelo.getSampleRateUnits());
	}
	
	
	// muestra en consola el rango de tiempo actual
	private void getTimeRange() {
		vista.writeConsole("Time range: " + modelo.getTimeRange()+" "+modelo.getTimeUnits());
	}
	
	
	// configurar frecuencia de muestreo
	private void setSampleRate(String option) {
		switch (option) {
		case InterfaceVista.ConfigSamplingRate:
			dataIn = JOptionPane.showInputDialog("Sample rate [Hz]");
			if (dataIn != null) {
				while(!isDouble(dataIn) && !(dataIn == null) || (Double.parseDouble(dataIn)<0)) {			
					dataIn = JOptionPane.showInputDialog(null, "Enter frequency in Hz (double value)", "Error!", JOptionPane.ERROR_MESSAGE);
					if(dataIn == null) return;
				}
			
				if(isDouble(dataIn)) {
					fs = Double.parseDouble(dataIn);
					modelo.setFs(fs);
					modelo.setSampleRateUnits("Hz");
					vista.writeConsole("New sample rate set successfully");
				}
			}
			break;
			
		case InterfaceVista.ConfigGraphAddSampleRate:
			dataIn = vista.getConfigSampleRate();
			if (dataIn != null) {
				if(isDouble(dataIn)) {
					modelo.setFs(Double.parseDouble(dataIn));
					modelo.setSampleRateUnits(vista.getSampleRateUnits());
					vista.writeConsole("New sample rate set successfully");
				}else {
					vista.writeConsole("ERROR! Sample rate not saved");
				}
			}
		}

		 
	}
	
	// configurar rango de tiempo de grafica
	private void setTimeRange(String option) {
		
		switch (option) {
		case InterfaceVista.ConfigTimeRange:
			dataIn = JOptionPane.showInputDialog("Set time range [ms]");
			if (dataIn != null) {
				while(!isDouble(dataIn) && !(dataIn == null) || (Double.parseDouble(dataIn)<0)) {
					dataIn = JOptionPane.showInputDialog(null, "Enter time in milliseconds (double value)", "Error!", JOptionPane.ERROR_MESSAGE);
					if(dataIn == null) return;
				}
				
				if(isDouble(dataIn)) {
					timeRange = Double.parseDouble(dataIn);
					modelo.setTimeRange(timeRange);
					modelo.setTimeUnits("ms");
				}
			}
			break;
			
		case InterfaceVista.ConfigGraphAddTime:
			dataIn = vista.getConfigTimeRange();
			if (dataIn != null) {
				if(isDouble(dataIn)) {
					modelo.setTimeRange(Double.parseDouble(dataIn));
					modelo.setTimeUnits(vista.getTimeRangeUnits());
					vista.writeConsole("New time range set successfully");
				}else {
					vista.writeConsole("ERROR! Time range not saved");
				}
			}
		}
		
	
	}
	
	/*
	// valida si el dato es entero
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	*/
	// valida si el dato es un double
	private static boolean isDouble(String cadena) {
		try {
			Double.parseDouble(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	// Sale del programa
	private void exitProgram() {
		System.out.println(serial_comm.closePort());
		//System.exit(0);
	}
	
	// Lista en consola los puertos conectados
    private void listSerialPorts() { 
	    
	 	Enumeration ports = CommPortIdentifier.getPortIdentifiers();  
        
	 	if(ports.hasMoreElements()) {
            vista.writeConsole(((CommPortIdentifier)ports.nextElement()).getName().toString() + " is connected");

	 	}else {
	 		vista.writeConsole("No serial ports connected");
	 	}
        while(ports.hasMoreElements())  {
            vista.writeConsole(((CommPortIdentifier)ports.nextElement()).getName().toString() );
        }
	 }
	
}




	


