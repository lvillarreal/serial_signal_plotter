package controlador;

import java.awt.event.*;
//import java.util.Date;
import java.util.Enumeration;
//import java.util.Map;
import java.util.regex.Pattern;
import java.io.*;
//import java.net.MalformedURLException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.net.URL;


import javax.swing.JOptionPane;
//import javax.swing.*;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import modelo.*;
import vista.*;



public class Controlador implements ActionListener, SerialPortEventListener{

	/**
	 * Definitions
	 */
	private static final byte SP_DATA = (byte)0;
	private static final byte MATLAB_DATA = (byte)1;
	
	private InterfaceModelo modelo;
	private InterfaceVista vista;
	
	private SerialCommunication serial_comm;
//	private int index;
//	private int buffer;
//	private int errores;
	private int fs;
	private int count;	// variable auxiliar para contar los bytes recibidos
	private int cant_bytes;	// cantidad de bytes del dato medido
//	private boolean disconnect_flag; // indica cuando se desconecta el puerto serial
	
	private boolean start_flag;
	private boolean shapes_flag;	// indica si estan habilitados o deshabilitados los shapes en la grafica
	private boolean user_text_flag;
	private boolean features_flag;
	
	private int datos;	// dato recibido por puerto serie
//	private int i;	// variable auxiliar para saber que byte se esta recibiendo
	private int index_buff;
	
	private boolean flag_full_buffer;	// indica que se ha llenado el array donde se almacena el dato entrante
	
	private long timeExec;
	
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
		status = -1;
		fs = 0;
		count = 0;
		
		start_flag = false;
		shapes_flag = false;
		user_text_flag = false;
		features_flag = false;
		
		datos = 0;

		timeExec = 0;
		
		index_buff = 0;
		

		
	
		

	}
	// Constructor vacio
	public Controlador() {
		
	}
	
	/******************************************************************************************************************
	 * PROTOCOLO DE COMUNICACION
	 * Cuando se presiona el boton start se envia el codigo ascii 2 (STX,start of text) al dispositivo
	 * El dispositivo responde con ACK (codigo ascii 6), luego la tasa de muestreo respresentada en 3 bytes (24 bits),
	 * luego el codigo ascii 10 (new line), la cantidad de bits del AD (maximo 24) seguido del rango dee entrada del AD (en 2 bytes).
	 * Al final se recibe el codigo ascii 4 (EOT, end of text)
	 * Luego se envia el codigo ascii 5 (ENQ) para comenzar la transmision de la informacion*/
	/******************************************************************************************************************/
	

		@Override 
		public void actionPerformed(ActionEvent e)  {

			Runnable r_barOptions = new barOptions(vista,modelo,serial_comm,this,e.getActionCommand());
			Thread t_barOptions = new Thread(r_barOptions);
			
		//	Runnable r_serialComm = new SerialComm(vista,modelo,serial_comm,this,e.getActionCommand());
		//	Thread t_serialComm = new Thread(r_serialComm);
			
			if (e.getActionCommand().equals(InterfaceVista.ButtonConnectPushed)){
				//t_serialComm.start();	
				new Thread(){
					@Override
					public void run() {
						if (buttonConnect() == 0) {
//							index = 0;
//							errores = 0;
//							buffer = -1;
							vista.buttonSetVisible("disconnect"); 	// se activa el boton desconectar
//							disconnect_flag = false;
							start_flag = false; 
						}
					}
				}.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ButtonDisconnectPushed)){
				//t_serialComm.start();
				new Thread(){
					@Override
					public void run() {
						buttonDisconnect();
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
						startPushed();
				
					}
				}.start();
				//t_printData.start();
			}else if(e.getActionCommand().equals(InterfaceVista.CalculateFFT)){
				//t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.GraphFFTmodule)){
				//t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigSetBaudRate)){
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.MenuButtonOpenFile)){
				new Thread() {
					public void run() {
						openFilePushed();
					}
				}.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ViewGraphData)){
				new Thread() {
					public void run() {
						graphData();
					}
				}.start();
				
			}else if(e.getActionCommand().equals(InterfaceVista.MenuSaveAs)){
				new Thread() {
					public void run() {
						saveAsPushed();
					}
				}.start();
			}else if(e.getActionCommand().equals(InterfaceVista.FileExportMatlab)){
				new Thread() {
					public void run() {
						saveForMatlabPushed();
					}
				}.start();
			}else if(e.getActionCommand().equals(InterfaceVista.ConfigShapesVisible)){
				this.shapes_flag = !this.shapes_flag;
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.MenuWindowUserText)){
				this.user_text_flag = !this.user_text_flag;
				t_barOptions.start();
			}else if(e.getActionCommand().equals(InterfaceVista.MenuWindowFeatures)){
				features_flag = ! features_flag;
				t_barOptions.start();
			}
		}

		
		
		/*LOS DATOS DEL AD SE ENVIAN EN DOS BYTES (8 BITS) CONSECUTIVOS, POR LO QUE SE DEBEN CONCATENAR PARA FORMAR EL DATO ORIGINAL*/
		@Override
		public void serialEvent(SerialPortEvent oEvent) {
			
			int MSB=-1;
			int LSB=-1;
			
			while(oEvent.DATA_AVAILABLE>0 && this.status != -1) {
	            try {	                
	                if(status == 0){	                		                	
	                	if((MSB = serial_comm.readData()) > -1 && (LSB = serial_comm.readData()) > -1) {
	                		modelo.setData((byte)MSB,(byte)LSB,index_buff);	                
	                   		index_buff+=2;
	                	}	                			                	
	                	if((MSB = serial_comm.readData()) > -1 && (LSB = serial_comm.readData()) > -1) {
	                		modelo.setData((byte)MSB,(byte)LSB,index_buff);	                			
	                   		index_buff+=2;
	                	}            		                	
	                }else if(status != -1) {
	                	commHandler();
	                }
	            } catch(ArrayIndexOutOfBoundsException e) {
	            	e.printStackTrace();
	            	errorFullBuffer();
	     
	            } catch (Exception e) {
	                System.err.println(e.toString());
	                
	            }	         
		    }	
		}

		private void startPushed() {
			if (start_flag == false) {
				flag_full_buffer = false;
				status = 1;
				start_flag = true;

				modelo.resetData();

				index_buff = 0;
				serial_comm.sendData((char)2);
//				index = 0;

				timeExec = System.currentTimeMillis();
				modelo.setDate(modelo.obtainDate());
				//vista.buttonSetVisible("start_pushed");
				this.features_flag = false;
				vista.featuresVisible(features_flag);
				this.user_text_flag = false;
				vista.textUserVisible(user_text_flag);

				
			}
		}
		
		/*
		 * Se ha presionado el boton save for matlab.
		 * Funcion que abre el cuadro de dilogo para guardar archivo*/
		private void saveForMatlabPushed(){
			String file_name = vista.fileWindow(InterfaceVista.optionSaveForMatlab);
			if(file_name != "_CANCEL_") {
				String[] aux = file_name.split("\\.");
				file_name = aux[0]+".bin";
				saveForMatlab(file_name);
				try{
					createMatlabScript(aux[0]+".m", file_name);
				}catch(IOException e){
					e.printStackTrace();
				}
				vista.writeConsole("File "+file_name+" saved successfully");
			}
		}
		
		/**
		 * Guarda los datos medidos en formato double, en archivo binario.
		 * @param fichero:	path+nombre del archivo 
		 */
		private void saveForMatlab(String fichero){
	        FileOutputStream fos = null;
	        DataOutputStream salida = null;
        	double[][] data = modelo.getData();

	        try {
	            fos = new FileOutputStream(fichero);
	            salida = new DataOutputStream(fos);
	            for(int i=0;i<data[1].length;i++){
	            	salida.writeFloat((float)data[1][i]);
	            }
	        } catch (FileNotFoundException e) {
	            System.out.println(e.getMessage());
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            try {
	                if (fos != null) {
	                    fos.close();
	                }
	                if (salida != null) {
	                    salida.close();
	                }
	            } catch (IOException e) {
	                System.out.println(e.getMessage());
	            }
	        }        
		}
		
		/**
		 * Crea un script para abrir el archivo .bin
		 * @param file_name	:	nombre del archivo .m
		 * @param file_data_name	:	nombre del archivo .bin
		 * @throws IOException
		 */
		private void createMatlabScript(String file_name, String bin_name) throws IOException{
			String separator = Pattern.quote("\\");
			String[] aux = bin_name.split(separator);
			File file = new File(file_name);
			if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("%{\n");
            bw.write("Measurement date: "+modelo.getDate()+"\n");
            bw.write("Sample rate: "+modelo.getSamplingRate()+" Hz\n");
            bw.write("Bits: "+modelo.getCantBits()+"\n");
            bw.write("Samples: "+modelo.getCantMuestras()+"\n");
            bw.write("%}\n\n");
            bw.write("clc, clear all, close all\n");
            bw.write("format long;\n\n");
            bw.write("Fs = "+modelo.getSamplingRate()+"; %Frecuencia de muestreo\n");
            bw.write("N = "+modelo.getCantMuestras()+"; %Cantidad de muestras\n\n");
            bw.write("t = (0:N-1)/Fs; % vector de tiempo\n\n");
            bw.write("fileID = fopen('"+aux[aux.length-1]+"');\n");
            bw.write("data = fread(fileID,"+modelo.getCantMuestras()+",'single','b');\n");
            bw.write("fclose(fileID);");
            bw.close();
		}
		
		private void saveAsPushed() {
			modelo.setUserText(vista.getUserText());
			String file_name = vista.fileWindow(InterfaceVista.optionSaveFile);
			if(file_name != "_CANCEL_") {
				String[] aux = file_name.split("\\.");
				file_name = aux[0]+".dat";
				saveData(file_name);
				vista.writeConsole("File "+file_name+" saved successfully");
			}

			
		}
		
		private void openFilePushed() {
			byte stat = openSavedData(vista.fileWindow(InterfaceVista.optionOpenFile));
			if(stat > -1) {
				features_flag = false;
				user_text_flag = false;
				vista.featuresVisible(this.features_flag);
				vista.textUserVisible(this.user_text_flag);
				
				vista.deleteConsole();
    			vista.writeConsole("*****************************************************");
    			vista.writeConsole("FILE OPENED SUCCESSFULLY");
    			vista.writeConsole("Sample rate: "+modelo.getSamplingRate()+" "+modelo.getSampleRateUnits());
    			vista.writeConsole("ADC bit count: "+modelo.getCantBits()+" bits");
    			vista.writeConsole("ADC input range: +-"+modelo.getInputRange()+" [V]");
    			vista.writeConsole("Date: "+modelo.getDate());
    			vista.writeConsole("*****************************************************");
				
    			vista.setSignalName(modelo.getSignalName());
    			
			}else if(stat == -1){
				vista.writeConsole("File has not been opened");
			}
		}
		
		private byte openSavedData(String fichero){
			byte status = -1;
	        try{
	        	if(fichero != "_CANCEL_") {	
		            // Se crea un ObjectInputStream
	        		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));
	        		this.modelo =(InterfaceModelo) ois.readObject();
	        		ois.close();
	        		status = 0;
	        	}else {
	        		status = -2;
	        	}
	        }
	        catch (Exception e2){
	            e2.printStackTrace();
	        }
	        return status;
	    }
		

		
		
		
		private void saveData(String fichero) {

				try {
						modelo.setUserText(vista.getUserText());
						ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(fichero));
						oos1.writeObject(modelo);
						oos1.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		// grafica los datos 
		private void graphData() {
			//long time = System.currentTimeMillis();
			vista.actualiceChartData(modelo.getSignalName(), modelo.getData());
			//int index_buff = (modelo.getCantMuestras()+1)*2;
		/*	double dat = 0;
			for(int j=0;j<index_buff-1;j=j+2) {
				dat = modelo.getData(j);
				vista.actualiceChartData(j/2, dat);
			}*/	
			//vista.writeConsole(""+(System.currentTimeMillis()-time));
		}
		
		
		
		// graph data With Error Control
		// Usado para debug
	/*	private void graphDataWEC() {
			//Bloque para corroborar que no haya errores

			double dat1 = 0;
			double dat = modelo.getData(0);
			int j = 0;
			vista.actualiceChartData(j, dat);
			for(j=2;j<index_buff;j=j+2) {
				dat1 = modelo.getData(j);
				if(dat1-dat != 1.0){
					System.out.println(dat1 + " "+ dat+" "+j);
				}
				dat = dat1;
				vista.actualiceChartData(j/2,dat);
			}
			vista.writeConsole("index: "+index_buff);
			vista.writeConsole("CANTIDAD DE MUESTRAS: "+(j-1)/2);
		}*/
		
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
        			vista.writeConsole("Date: "+modelo.getDate());
        			vista.writeConsole("*****************************************************");
        			vista.writeConsole("         MEASURING . . .");
        			
        		}else {
        			vista.writeConsole("ERROR! WRONG CONFIGURATION DATA. PLEASE RESET MODULE");
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
					vista.deleteChartData(modelo.getSignalName());
					
					// Se leen los datos  del archivo
					
					line = modelo.readLine();
					//vista.writeConsole(line);
			
					while (line != null) {
	//					vista.actualiceChartData(((double )index)/fs, Double.parseDouble(line));
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
			vista.deleteChartData(modelo.getSignalName());
			vista.deleteConsole();

			modelo.setPortName(vista.getPortName());	//Se guarda el puerto ingresado  por el usuario
			if(serial_comm.portConnect(this, modelo.getPortName()) == -1) {
				vista.writeConsole("Cannot connect to port "+vista.getPortName());
				return -1;
			}else {
				vista.writeConsole(vista.getPortName()+" connection successfully");
				return 0;
			}
			
		}
		 
		private void buttonDisconnect() {
			
			status = -1;
			
			// Entra al if si no se lleno el buffer de recepcion de datos
			if(!flag_full_buffer) 
				timeExec = System.currentTimeMillis() - timeExec;
			
			serial_comm.sendData((char)4);
			if(start_flag)vista.writeConsole("Recording time: "+String.valueOf(timeExec/1000.0)+" seconds");

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (serial_comm.closePort() == 0) {
				vista.buttonSetVisible("connect"); 	// se activa el boton conectar
				vista.writeConsole("COM Port disconnected");
				modelo.setCantMuestras((index_buff/2)-1);
				if(start_flag) {
					saveData("AutoSave.dat");	// guarda el objeto modelo, que contiene toda la informacion de la grafica
					modelo.calculateAllFeatures(); // calcula las caracteristicas de la señal
				}
			
				start_flag = false;
				
			}else vista.writeConsole("ERROR! CANNOT DISCONNECT. PLEASE CLOSE PROGRAM");
		}
			
		
		private void errorFullBuffer() {
	    	if(!flag_full_buffer) { 
	    		timeExec = System.currentTimeMillis() - timeExec;
	    		status = -1;
        		serial_comm.sendData((char)4); // Envia 4 para que el sistema deje de enviar info
        		flag_full_buffer = true;
        		vista.writeConsole("FULL BUFFER ERROR! Measuring aborted. Please click on DISCONNECT");
        	}
		}
			
		public boolean getShapesFlag() {
			return this.shapes_flag;
		}
		
		public boolean getUserTextFlag() {
			return this.user_text_flag;
		}
		
		public boolean getFeaturesFlag() {
			return this.features_flag;
		}
		
		public void setUserTextFlag(boolean option) {
			this.user_text_flag = option; 
		}

		public void setFeaturesFlag(boolean option) {
			this.features_flag = option; 
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
	private Controlador controlador;
	
	public barOptions(InterfaceVista vista,InterfaceModelo modelo,SerialCommunication serial_comm,Controlador controlador,String option) {
		this.option = option;
		this.vista = vista;
		this.modelo = modelo;
		this.serial_comm = serial_comm;
		this.controlador = controlador;
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
				break;
				
			case InterfaceVista.ConfigShapesVisible:
				vista.setShapesVisible(this.controlador.getShapesFlag());
				break;
				
			case InterfaceVista.MenuWindowUserText:
				if(this.controlador.getFeaturesFlag()) {
					vista.featuresVisible(false);
					this.controlador.setFeaturesFlag(false);
				}
				if(this.controlador.getUserTextFlag()) {
					vista.setUserText(modelo.getUserText());
				}else modelo.setUserText(vista.getUserText());
				
				vista.textUserVisible(this.controlador.getUserTextFlag());
				break;
				
			case InterfaceVista.MenuWindowFeatures:
				if(this.controlador.getUserTextFlag()) {	// si user text esta visible
					modelo.setUserText(vista.getUserText()); // guardo el texto
					controlador.setUserTextFlag(false);
					vista.textUserVisible(false);
				}
					showAllFeatures();
					vista.featuresVisible(controlador.getFeaturesFlag());
					
				
				
				
				break;
				
			
				

		}
	}
	
	
	private void showAllFeatures() {
		
		vista.setFeatures("Signal : "+ modelo.getSignalName()+
						  "\nSamples: "+ modelo.getCantMuestras()+
						  "\n\n>> CC value: "+modelo.getCCvalue()+
						  "\n>> Max value: "+modelo.getMaxValue()+
						  "\n>> Min value: "+modelo.getMinValue()+
						  "\n>> Peak-to-peak value: "+(modelo.getMaxValue()-modelo.getMinValue())+
						  "\n>> RMS value: "+modelo.getRMSvalue());
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
			vista.deleteChartData(modelo.getSignalName());
			
			// Se leen los datos  del archivo
			
			line = modelo.readLine();
			//vista.writeConsole(line);
	
			while (line != null) {
	//			vista.actualiceChartData(((double )index)*fs, Double.parseDouble(line));
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




	


