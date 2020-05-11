package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
//import gnu.io.SerialPortEvent;
//import gnu.io.SerialPortEventListener;

/*Implementa la conexion, transmision y recepcion de datos por puerto serie. Los eventos los maneja el controlador.*/

public class SerialCommunication{
	 
	private static final String RXTX_LIB = "rxtxSerial";
    private static final int TIME_OUT = 2000;
    private int DATA_RATE;
    private SerialPort serialPort;
    private CommPortIdentifier portId;
    private OutputStream Output;
    private static InputStream Input;
    private boolean connected;
  //  private static int aux;
    
    private String mensaje;
    private int dato_entrada;
        
    public SerialCommunication() {
    	portId = null;
    	Output = null;
    	Input = null;
    	mensaje = "";
    	dato_entrada = 0;
    	connected = false;
    	this.DATA_RATE = 900000;
    	
    	copy(getClass().getResourceAsStream("/"+RXTX_LIB+".dll"),"./"+RXTX_LIB+".dll");
    	System.loadLibrary(RXTX_LIB);
    	}
   

    private static boolean copy(InputStream source , String destination) {
        boolean succeess = true;
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
        	ex.printStackTrace();
            succeess = false;
        }
        return succeess;
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
            connected = false;
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
            connected = true;
            
            
            
            return 0;
        } catch (Exception e) {
        	connected = false;
            return -1;
        }
        
    }
    
    public byte readByte() {
    	byte[] data = new byte[1];
        byte output = -1;
    	try {
			Input.read(data);
			output = data[0];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return output;
     } 
    
    
    public int read2Bytes() {
   // 	byte[] buff = new byte[2];
    	int status = -1;
    	try {
    		
    		status = Input.read();
    		status = (status <<8)+Input.read();
    		//status = ((buff[0]&0xFF)<<8)+(buff[1]&0xFF);	
    		System.out.println(status);
    	}catch(Exception e){
           e.printStackTrace();
     	   System.out.println("ERROR READING");
        }
    	return status;
    }

    public byte setEnableInterrupt(Controlador c,boolean status) {
    	byte stat = -1;
    	try {
	    	if (status) {
	    		serialPort.removeEventListener();
	       	}else {
	       		serialPort.addEventListener(c);
	       	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return stat;
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
    
    public byte sendData(char data) {

            try {
                Output.write(data);
                return 0;
            } catch (IOException e) {
            	e.printStackTrace();
            	return -1;
            }
      
    }
    
    public byte closePort() {
    	try{
    		
    		serialPort.removeEventListener();
    		
    		serialPort.close();
    		Output.close();
    		
    		Input.close();
    		portId = null;
    		connected = false;
    		return 0;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return -1;
    	}
    }
    
 /*   public void closePort() {

    	new Thread(){
        @Override
        public void run(){
            try{
            //serialPort.removeEventListener();
            Input.close();
            serialPort.close();
            connected = false;
            }catch (IOException e) {
        		e.printStackTrace();
            }
        }
        }.start();
    }*/
    
    public void setMensaje(String msje) {
    	this.mensaje = msje;
    }
    
    public String getMensaje() {
    	return mensaje;
    }
    
    public void setDatoEntrada(int dato) {
    	this.dato_entrada = dato;
    }
    
    public void setBaudRate(int baudrate) {
    	this.DATA_RATE = baudrate;
    }
    
    public int getDatoEntrada() {
    	return this.dato_entrada;
    }
    
    public boolean getState() {
    	return connected;
    }
    
    public int getBaudRate() {
    	return this.DATA_RATE;
    }

}


