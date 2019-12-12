package controlador;

import java.awt.event.*;
import java.util.Enumeration;

import javax.swing.JOptionPane;

import gnu.io.CommPortIdentifier;

import modelo.*;
import vista.*;



public class Controlador implements ActionListener{
	
	
	private InterfaceModelo modelo;
	private InterfaceVista vista;
	

	
	public Controlador(InterfaceModelo modelo, InterfaceVista vista) {
		this.modelo = modelo;
		this.vista = vista;

	}

		@Override 
		public void actionPerformed(ActionEvent e)  {
			
			Runnable r_barOptions = new barOptions(vista,modelo,e.getActionCommand());
			Thread t_barOptions = new Thread(r_barOptions);
			
			Runnable r_serialComm = new SerialComm(vista,modelo,e.getActionCommand());
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

				
			}
			
		}

		

}

		
// Implementa las acciones que se realizan en la comunicacion serial. Lectura/escritura del puerto
class SerialComm implements Runnable{
	private InterfaceVista vista;
	private InterfaceModelo modelo;
	private String option;
	
	public SerialComm(InterfaceVista vista,InterfaceModelo modelo,String option) {
		this.vista = vista;
		this.modelo = modelo;
		this.option = option;
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
		
			String status = null;
			String line = null;
			int index = 0;

			
			double fs = modelo.getSamplingRate();

			// Se abre el archivo
			status = modelo.openFile();
			vista.writeConsole(status);
			
			// Controla que si el archivo no existe, no continue en el metodo
			if(status.equalsIgnoreCase("No se encuentra el Archivo  \"files/DataBase.txt\"")){
				vista.setButtonEnable(InterfaceVista.ButtonStartEnable, true);
				return;
			}else {
		    vista.setButtonEnable(InterfaceVista.ButtonStartEnable, false);
			// Se lee la frecuencia de muestreo que el sistema esta utilizando
			fs = Double.parseDouble(modelo.readLine());
			vista.writeConsole("Frecuencia de muestreo: "+fs + " [Hz]");
			modelo.setFs(fs); // Se guarda la frecuencia de muestreo
			modelo.setSampleRateUnits("Hz");
			
			// Se borran los datos actuales del grafico
			vista.deleteChartData();
			
			// Se leen los datos
			line = modelo.readLine();
			//vista.writeConsole(line);
	
			while (line != null) {
				vista.actualiceChartData(((double )index)/fs, Double.parseDouble(line));
				index =index + 1;
				line = modelo.readLine();
				//vista.writeConsole(String.valueOf(((double )index)/fs)+" ; "+line );
			}
			
			status = modelo.closeFile();
			vista.writeConsole(status);
			vista.setButtonEnable(InterfaceVista.ButtonStartEnable, true);
			}
	}
			
	
	
	private void buttonConnect() {
		/*for (int i=1; i<=3000; i++){
			System.out.println(i);
			try {
				Thread.sleep(4);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
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
	
	public barOptions(InterfaceVista vista,InterfaceModelo modelo,String option) {
		this.option = option;
		this.vista = vista;
		this.modelo = modelo;
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
		vista.writeConsole("Time rang)e: " + modelo.getTimeRange()+" "+modelo.getTimeUnits());
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
		System.exit(0);
	}
	
	// Lista en consola los puertos conectados
    private void listSerialPorts() { 
	    
	 	Enumeration ports = CommPortIdentifier.getPortIdentifiers();  
        
	 	if(ports.hasMoreElements()) {
	 		vista.writeConsole("Puertos conectados");
            vista.writeConsole(((CommPortIdentifier)ports.nextElement()).getName().toString() );

	 	}else {
	 		vista.writeConsole("No se detectan puertos conectados");
	 	}
        while(ports.hasMoreElements())  {
            vista.writeConsole(((CommPortIdentifier)ports.nextElement()).getName().toString() );
        }
	 }
	
}




	


