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
	private int index;
	private int buffer;
	private int errores;
	// status indica el estado en que se encuentra la comunicacion
	// -1: no hay comunicacion
	//  0: se estan recibiendo los datos de la medicion
	//  1: se estan recibiendo datos de configuracion
	private byte status;	
	
	public Controlador(InterfaceModelo modelo, InterfaceVista vista) {
		this.modelo = modelo;
		this.vista = vista;
		serial_comm = new SerialCommunication();	// instanciacion de serialCommunication
		index = 0;
		buffer = 0;
		errores = 0;
		status = -1;
	}
	
	// inicializa la comunicacion, pide al dispositivo la tasa de muestreo
	// Se envia el valor ascii 2 (STX, inicio texto) para comenzar la transmision
	// El client envia la tasa de muestreo en hertz, en 24 bits (3 bytes)
	// Luego, el cliente envia ascii 4 (EOT, fin transmision);
	// Una vez obtenida la informacion, se envia ascii 5 (ENQ, consulta) para comenzar a recibir la medicion del AD

		@Override 
		public void actionPerformed(ActionEvent e)  {
			
			Runnable r_barOptions = new barOptions(vista,modelo,serial_comm,e.getActionCommand());
			Thread t_barOptions = new Thread(r_barOptions);
			
		//	Runnable r_serialComm = new SerialComm(vista,modelo,serial_comm,this,e.getActionCommand());
		//	Thread t_serialComm = new Thread(r_serialComm);
			
			if (e.getActionCommand().equals(InterfaceVista.ButtonConnectPushed)){
				//t_serialComm.start();	
				new Thread(){
					@Override
					public void run() {
						if (buttonConnect() == 0) {
							index = 0;
							errores = 0;
							buffer = 0;
							vista.buttonSetVisible("disconnect"); 	// se activa el boton desconectar
						}
					}
				}.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ButtonDisconnectPushed)){
				//t_serialComm.start();
				new Thread(){
					@Override
					public void run() {
						if (buttonDisconnect() == 0) {
							vista.buttonSetVisible("connect"); 	// se activa el boton conectar
						}
					}
				}.start();
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
				//t_serialComm.start();
				new Thread(){
					@Override
					public void run() {
						status = 1;
						serial_comm.sendData((char)2);
						//actualiceChart();
					}
				}.start();
			}else if(e.getActionCommand().equals(InterfaceVista.CalculateFFT)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.GraphFFTmodule)){
				t_barOptions.start();
				
			}
			
		}

		/*LOS DATOS DEL AD SE ENVIAN EN DOS BYTES (8 BITS) CONSECUTIVOS, POR LO QUE SE DEBEN CONCATENAR PARA FORMAR EL DATO ORIGINAL*/
		@Override
		public void serialEvent(SerialPortEvent oEvent) {
			
			
		    while ((oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) && (serial_comm.getState() == true)) {
		    	
	            try {
	                int datos = 0;
	                
	                // serial_comm.getState retorna la variable state que se usa para saber si esta o no conectado
	                if(serial_comm.getState() == true) {
	                	if(status == 0) { // se recibe el dato del AD
	                		datos = serial_comm.readData(); //Se lee los datos en el puerto serie
	                		datos = datos << 8;
	                		datos = datos + serial_comm.readData();
	                	}else if(status == 1) {	//se recibe la tasa de muestreo
	                		datos = serial_comm.readData();
	                		System.out.println(datos);
	                		datos = datos << 16;
	                		System.out.println(datos);
	                		datos = datos + serial_comm.readData();
	                		System.out.println(datos);
	                		datos = datos << 8;
	                		System.out.println(datos);
	                		datos = datos + serial_comm.readData();
	                		System.out.println("FINAL: "+datos);

	                		if (serial_comm.readData() == 4) {	// Si recibe 4 (EOT) es por que el cliente envio correctamente la tasa de muestreo
	                			status = 0;
	                			modelo.setFs(datos);
	                			modelo.setSampleRateUnits("Hz");
	                			serial_comm.sendData((char)5);	// se envia 5 para comenzar a recibir la info
	                			System.out.println("TASA DE MUESTREO: "+modelo.getSamplingRate()+" "+modelo.getSampleRateUnits());
	                		}
	                	}
	                }else break;
	                if (datos > 0) { //Si el valor leido es mayor a 0...
	                	
	                    //serial_comm.setMensaje(serial_comm.getMensaje()+(char)datos); //Se acumula el mensaje
		                //System.out.println(datos);
		                
		       
		                if(buffer != 0 && (datos > buffer*0.01 && datos < buffer*100))
		                	vista.actualiceChartData(index, datos);
		                else if(buffer!=0){
		                	errores+=1;
		                	System.out.println("Errores: "+errores+" index: "+index);
		                }
		                buffer = datos;	// guarda el dato para compararlo con el proximo dato, para saber si hubo error
		                index+=1;

	                    
	                }else break;     
	             /*       if (serial_comm.getMensaje().charAt(serial_comm.getMensaje().length() - 1) == ',') { //Cuando se recibe la coma
	                        //el mensaje ha llegado a su final, por lo que se procede a imprimir
	                        //La parte ENTERA de la humedad. Se busca el punto, donde quiera que esté
	                        //y se transforma de String a entero
	                    	System.out.println(serial_comm.getMensaje());
	                    	serial_comm.setMensaje("");
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
	                
	                }*/
		            

	            } catch (Exception e) {
	                System.err.println(e.toString());
	            }

		    }
			
			
		}


		// metodos para la comunicacion serial
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
				
		
		
		private byte buttonConnect() {
			vista.deleteChartData();
			modelo.setPortName(vista.getPortName());	//Se guarda el puerto ingresado  por el usuario
			if(serial_comm.portConnect(this, modelo.getPortName()) == -1) {
				vista.writeConsole("Cannot connect to port "+vista.getPortName());
				return -1;
			}
			else {
				vista.writeConsole(vista.getPortName()+" connection successfully");
				return 0;
			}
			
		}
		
		private byte buttonDisconnect() {
			return serial_comm.closePort();
		}


		

}

		
// Implementa las acciones que se realizan en la comunicacion serial. Lectura/escritura del puerto
/*class SerialComm implements Runnable{
	
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
			if (buttonConnect() == 0) {
				vista.buttonSetVisible("disconnect"); 	// se activa el boton desconectar
			}
			break;
			
		case InterfaceVista.ButtonStartPushed:	
			actualiceChart();
			break;
			
		case InterfaceVista.ButtonDisconnectPushed:
			//System.out.println(buttonDisconnect());
			buttonDisconnect();
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
			
	
	
	private byte buttonConnect() {
		vista.deleteChartData();
		modelo.setPortName(vista.getPortName());	//Se guarda el puerto ingresado  por el usuario
		if(serial_comm.portConnect(c, modelo.getPortName()) == -1) {
			vista.writeConsole("Cannot connect to port "+vista.getPortName());
			return -1;
		}
		else {
			vista.writeConsole(vista.getPortName()+" connection successfully");
			return 0;
		}
		
	}
	
	private void buttonDisconnect() {
		 serial_comm.closePort();
	}


}

*/

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
		serial_comm.closePort();
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




	


