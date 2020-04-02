package controlador;

import java.awt.event.*;
import java.util.Enumeration;
//import java.util.Map;

import java.io.*;

import javax.swing.JOptionPane;
//import javax.swing.*;

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
	private int fs;
	private int count;	// variable auxiliar para contar los bytes recibidos
	private int cant_bytes;	// cantidad de bytes del dato medido
	private boolean disconnect_flag; // indica cuando se desconecta el puerto serial
	private boolean start_flag;
	private int datos;	// dato recibido por puerto serie
	private int i;	// variable auxiliar para saber que byte se esta recibiendo
	private int index_buff;
	
	private long timeExec;
		
	// arrays para guardar el MSB y LSB del dato recibido por serial port
	private byte[] buffer_serial;	
	
	/****************************************************************
	*					Valores de STATUS							*
	* status indica el estado en que se encuentra la comunicacion	*
	* -1: no hay comunicacion										*
	*  0: se estan recibiendo los datos de la medicion				*
	*  1: se envia STX												*	
	*  2: espera ack												*
	*  3: se recibe tasa de muestreo y se envia 5 para init comm	*
	*  4: Se espera	ACK												*
	* 																*
	*****************************************************************/
	private byte status;	
	
	public Controlador(InterfaceModelo modelo, InterfaceVista vista) {

		this.modelo = modelo;
		this.vista = vista;
		serial_comm = new SerialCommunication();	// instanciacion de serialCommunication
		index = 0;
		buffer = -1;
		errores = 0;
		status = -1;
		fs = 0;
		count = 0;
		disconnect_flag = false;
		start_flag = false;
		datos = 0;
		i = 0;
		
		timeExec = 0;
		
		buffer_serial = new byte[9600000];
		
		resetBuffer();
		
		index_buff = 0;

	}
	
	/*
	 * PROTOCOLO DE COMUNICACION
	 * Cuando se presiona el boton start se envia el codigo ascii 2 (STX,start of text) al dispositivo
	 * El dispositivo responde con ACK (codigo ascii 6), luego la tasa de muestreo respresentada en 3 bytes (24 bits),
	 * luego el codigo ascii 10 (new line) seguido de la cantidad de bits del AD (maximo 24)seguido del rango del AD.
	 * Al final envia el codigo ascii 4 (EOT, end of text)
	 * Luego se envia el codigo ascii 5 (ENQ) para comenzar la transmision de la informacion*/
	

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
							buffer = -1;
							vista.buttonSetVisible("disconnect"); 	// se activa el boton desconectar
							disconnect_flag = false;
							start_flag = false; 
						}
					}
				}.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ButtonDisconnectPushed)){
				//t_serialComm.start();
				new Thread(){
					@Override
					public void run() {
						disconnect_flag = true;
						status = -1;
						
						timeExec = System.currentTimeMillis() - timeExec;
						serial_comm.sendData((char)4);
						if(start_flag)vista.writeConsole("Recording time: "+String.valueOf(timeExec/1000.0)+" seconds");

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (buttonDisconnect() == 0) {
							vista.buttonSetVisible("connect"); 	// se activa el boton conectar
							vista.writeConsole("COM Port disconnected");
							disconnect_flag = true;	
					
							//if(start_flag) saveCurrentData();
							
							//if(start_flag)graphData();
							if(start_flag)graphDataWEC();
							start_flag = false;
							
						}else vista.writeConsole("ERROR! CANNOT DISCONNECT. PLEASE CLOSE PROGRAM");
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
				new Thread(){
					@Override
					public void run() {
						if (start_flag == false) {
							status = 1;
							start_flag = true;
							resetBuffer();
							index_buff = 0;
							serial_comm.sendData((char)2);
							index = 0;

							timeExec = System.currentTimeMillis();
							//vista.buttonSetVisible("start_pushed");
							
						}
					}
				}.start();
				//t_printData.start();
			}else if(e.getActionCommand().equals(InterfaceVista.CalculateFFT)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.GraphFFTmodule)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigSetBaudRate)){
				t_barOptions.start();
				
			}
			
		}

		/*LOS DATOS DEL AD SE ENVIAN EN DOS BYTES (8 BITS) CONSECUTIVOS, POR LO QUE SE DEBEN CONCATENAR PARA FORMAR EL DATO ORIGINAL*/
		@Override
		public void serialEvent(SerialPortEvent oEvent) {
			
			int MSB=-1;
			int LSB=-1;

			//serial_comm.setEnableInterrupt(this, false);
			
			while(oEvent.DATA_AVAILABLE>0 && this.status != -1) {
		    //while ((oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) && this.status != -1) {//(serial_comm.getState() == true)) {
	            try {	                
	                if(status == 0){
	                	
	                	//serial_comm.readData();	                	   
	                	
	                	if((MSB = serial_comm.readData()) > -1 && (LSB = serial_comm.readData()) > -1) {
	                		buffer_serial[index_buff]   = (byte)MSB;
	                		buffer_serial[index_buff+1] = (byte)LSB;
	                   		index_buff+=2;
	                	}
	                	if((MSB = serial_comm.readData()) > -1 && (LSB = serial_comm.readData()) > -1) {
	                		buffer_serial[index_buff]   = (byte)MSB;
	                		buffer_serial[index_buff+1] = (byte)LSB;
	                   		index_buff+=2;
	                	}	                	
	                	
	                }else if(status != -1) {
	                	commHandler();
	                }
      
	                
	            } catch (Exception e) {
	                System.err.println(e.toString());
	            }
	         
		    }
	
		}

		
		private void saveCurrentData() {
	        FileOutputStream fos = null;
	        DataOutputStream salida = null;
	        try {
	            fos = new FileOutputStream("/ficheros/datos.dat");
	            salida = new DataOutputStream(fos);
	        }catch(IOException e) {
	        	
	        }
			
			
			
			// se abre el archivo
			/* File archivo = new File("DataBase.txt");
		     BufferedWriter bw;
		
		     int j=0;
		     
			try {
				bw = new BufferedWriter(new FileWriter(archivo));
				vista.writeConsole("SAVING DATA. PLEASE WAIT ...");
				bw.write("Sample rate: "+modelo.getSamplingRate()+" "+modelo.getSampleRateUnits());
				bw.newLine();
				bw.write("ADC bits: "+modelo.getCantBits());
				bw.newLine();
				bw.write("ADC input range: +-"+modelo.getInputRange()+" V");
				bw.newLine();
				
				while(status == -1 && j < index_buff-1) {
					bw.write(String.valueOf(((((int)buff_MSB[j])*256)+(int)buff_LSB[j])));
				}
				
				bw.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
		
		private void graphData() {
			int dat = 0;
			for(int j=0;j<index_buff-1;j=j+2) {
				dat =((int)(buffer_serial[j]&0xFF))*256+(int)(buffer_serial[j+1]&0xFF);
				vista.actualiceChartData(j, dat);
			}	
		}
		
		// graph data With Error Control
		private void graphDataWEC() {
			//Bloque para corroborar que no haya errores
			int dat1 = 0;
			int dat = ((int)(buffer_serial[0]&0xFF))*256+(int)(buffer_serial[1]&0xFF);
			vista.actualiceChartData(0, dat);
			for(int j=2;j<index_buff-1;j=j+2) {
				if((dat1 = ((int)(buffer_serial[j]&0xFF))*256+(int)(buffer_serial[j+1]&0xFF)) - dat != 1){
					System.out.println(dat1+ " "+ dat+" "+j);
				}
				dat = ((int)(buffer_serial[j]&0xFF))*256+(int)(buffer_serial[j+1]&0xFF);
				vista.actualiceChartData(j,dat );
			}
		}
		
		// se encarga de la manejar la comunicacion serial
		private void commHandler() {
			if(status == 1) {	//Espera confirmacion (ACK)
   
        		datos = serial_comm.readData();
        		System.out.println("RECIBE ACK: "+datos);
        		if(datos == 6) {
        			status = 3;
        			fs = 0;
        			count = 0;
        		}
        	}else if(status == 3) {	// tasa de muestreo
        		datos = serial_comm.readData();
        		System.out.println("DATO RECIBIDO: "+datos);
        		count +=1;
        		if (datos == 10 && count == 4) {	// Si recibe 10 (new line) es porque envio correctamente la tasa de muestreo
        			
           			modelo.setFs(fs);
        			modelo.setSampleRateUnits("Hz"); 
        			serial_comm.sendData((char)5);	// se envia 5 para comenzar a recibir la info
        			count = 0;
        			status = 4;
        			
        		}else {	   
        			fs = fs << 8;
            		fs = fs+datos;
            		
        		}
        		
          	}else if(status == 4) {	// cantidad de bits
          		
        		datos = serial_comm.readData();
        		modelo.setCantBits(datos);
    			if(datos<=8) cant_bytes = 1;
    			else if(datos >8 && datos <= 16) cant_bytes = 2;
    			else if(datos >16 && datos <= 24) cant_bytes = 3;
    			System.out.println("cant bits: "+datos);
    			status = 5;
          	}else if(status == 5) { 
          		modelo.setInputRange(((double)(serial_comm.readData()*256+serial_comm.readData()))/1000.0);
          		if(serial_comm.readData() == 4)	{
        			status = 0;
        			datos = 0;
        			vista.writeConsole("*****************************************************");
        			vista.writeConsole("CONFIGURATION DATA RECEIVED CORRECTLY");
        			vista.writeConsole("Sample rate: "+modelo.getSamplingRate()+" "+modelo.getSampleRateUnits());
        			vista.writeConsole("ADC bit count: "+modelo.getCantBits()+" bits");
        			vista.writeConsole("ADC input range: +-"+modelo.getInputRange()+" [V]");
        			vista.writeConsole("*****************************************************");
        			
        		}else {
        			vista.writeConsole("ERROR EN RECEPCION DE DATOS DE CONFIGURACION");
        		}
        	}
		}
		
		
		// metodo para graficar desde un archivo de texto
		 private void actualiceChartFromFile() {
				
				byte status;
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
			}else {
				vista.writeConsole(vista.getPortName()+" connection successfully");
				return 0;
			}
			
		}
		
		private byte buttonDisconnect() {
			
			return serial_comm.closePort();
			
		}
		
		private void resetBuffer() {

			for (int j=0;j<this.buffer_serial.length; j++) {
				buffer_serial[j] = 0;
			}
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
				
			case InterfaceVista.ConfigSetBaudRate:
				int baud = vista.getBaudRate();
				if (baud > -1) {
					this.serial_comm.setBaudRate(baud);
					vista.writeConsole("New baud rate is "+serial_comm.getBaudRate()+" bauds");
				}else if(baud == -1){
					vista.writeConsole("ERROR! Input valid baud rate (integer number)");
				}
				

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
					vista.writeConsole("New sample rate is "+modelo.getSamplingRate()+" "+modelo.getSampleRateUnits());
				}
			}
			break;
			
		case InterfaceVista.ConfigGraphAddSampleRate:
			dataIn = vista.getConfigSampleRate();
			if (dataIn != null) {
				if(isDouble(dataIn)) {
					modelo.setFs(Double.parseDouble(dataIn));
					modelo.setSampleRateUnits(vista.getSampleRateUnits());
					vista.writeConsole("New sample rate is "+modelo.getSamplingRate()+" "+modelo.getSampleRateUnits());
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
	
	
	// valida si el dato es entero
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
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
		if(serial_comm.getState()) serial_comm.closePort();
		System.exit(0);
	}
	
	// Lista en consola los puertos conectados
    private void listSerialPorts() { 
	    
	 	Enumeration ports = CommPortIdentifier.getPortIdentifiers();  
        
	 	if(ports.hasMoreElements()) {
            vista.writeConsole(((CommPortIdentifier)ports.nextElement()).getName().toString() + " is available");

	 	}else {
	 		vista.writeConsole("No serial ports connected");
	 	}
        while(ports.hasMoreElements())  {
            vista.writeConsole(((CommPortIdentifier)ports.nextElement()).getName().toString() + " is available" );
        }
	 }

	
}




	


