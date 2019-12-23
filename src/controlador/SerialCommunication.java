package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/*Implementa la conexion, transmision y recepcion de datos por puerto serie. Los eventos los maneja el controlador.*/

public class SerialCommunication{
	 
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 250000 ;
    SerialPort serialPort;
    private CommPortIdentifier portId;
    private OutputStream Output;
    private InputStream Input;
    
    private String mensaje;
    private int dato_entrada;
    
    public SerialCommunication() {
    	portId = null;
    	Output = null;
    	Input = null;
    	mensaje = "";
    	dato_entrada = 0;
    }

    public byte portConnect(Controlador c,String portName) {
        
		// verifica que el puerto ingresado este conectado
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

            if (portName.equals(currPortId.getName().toString())) {
                portId = currPortId;
                
                break;
            }
        }

        if (portId == null) {
            return -1;	// si retorna -1 es por que hubo un error
        }

        try {

            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            Output = serialPort.getOutputStream();
            Input = serialPort.getInputStream();

            serialPort.addEventListener(c);
            serialPort.notifyOnDataAvailable(true);
            System.out.println("Se Conecto al puerto "+portName);
        } catch (Exception e) {

            return -1;
        }
        return 0;
    }
    
    public int readData() {
    	int output = -1;
    	try {
			output = (Input.read());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return output;
    }
    
    
    public byte closePort() {
    	try{
    		
    		serialPort.close();
    		Output.close();
    		Input.close();
    		return 0;
    	}catch(Exception e) {
    		return -1;
    	}
    }
    
    public void setMensaje(String msje) {
    	this.mensaje = msje;
    }
    
    public String getMensaje() {
    	return mensaje;
    }
    
    public void setDatoEntrada(int dato) {
    	this.dato_entrada = dato;
    }
    
    public int getDatoEntrada() {
    	return this.dato_entrada;
    }

}
