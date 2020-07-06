package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DataBaseHandler implements InterfaceModelo, Serializable {
	
	private dataBase data_base;
	
	private FileHandler file_handler;	// clase para el manejo de archivos
	
	//private InplaceFFT fft;
	
//	private String sample_rate_units; // unidades de frecuencia de muestreo
//	private String time_units;	// unidades del rango de tiempo
//	private double time_range;	// rango de tiempo de la grafica
//	private double fs;			// frecuencia de muestreo
//	private double Ts;			// periodo de muestreo
//	private String signal_name;	// nombre de la señal
//	private int cant_bits;
//	private double input_range;
//	private byte[] data;		// bytes que ingresan por puerto serial
//    private int cant_muestras;	//cantidad de muestras en data
//	private String date;		// fecha y hora
//    private String userText;	// texto que ingrese el usuario en notas
	
    private double cc_value;	// valor de continua de la señal
    private double max_value;
    private double min_value;
    private double rms_value;
    
	// Seccion comunicacion serial
	private String port_name;
	private String portId;	// para verificar si el puerto esta conectado o no

	private boolean fft_flag;	// verdadero si fft esta calculada para el dato actual
	
	
	// Seccion archivos
//	private static String FILE_NAME;
//	private static String fftModule_FILE_NAME;
//	private static String fftModule_FILE_NAME_shifted;
	
	public DataBaseHandler() {
		//this.fft = new FFT();
		//this.file_handler = new FileHandler();
		data_base = new dataBase();
		// valores por defecto
		data_base.setSignalName("Signal name");
		data_base.setFs(1);
		data_base.setCantBits(18);
		data_base.setInputRange(0);
		data_base.setDate(obtainDate());
		data_base.setUserText("");
		data_base.resetData();
		
		this.port_name = null;
		this.portId = null;
		this.fft_flag = false;
		//this.signal_name = "Signal name";
		//this.fs = 1;
		//this.Ts = 1/fs;
		//this.time_range = 1;
		//this.time_units = "ms";
		//this.sample_rate_units = "kHz";
		
		//this.cant_bits = 18;
		//this.input_range = 0;	// es +-2.56
		
		//this.data = new byte[9600000];
		//this.date = obtainDate();
		//this.userText = "";
		
		//resetData();
		//FILE_NAME = "files/DataBase.txt";
		//fftModule_FILE_NAME = "files/signal_fft_module_noshifted.txt";
		//fftModule_FILE_NAME_shifted= "files/signal_fft_module.txt";
		
	}
	
	
	@Override
	public void calculateFFT() {
		
		double[] dato = new double[data_base.getCantMuestras()];
		
		Complex[] x = new Complex[data_base.getCantMuestras()];
		Complex[] X = new Complex[data_base.getCantMuestras()];
		
		dato = data_base.getData()[1];
		for (int i = 0; i < data_base.getCantMuestras(); i++) {
            //x[i] = new Complex(dato[i], 0);
			x[i] = new Complex(Math.sin(2*Math.PI*200*i/20000.0),0);
		}
		X = InplaceFFT.fft(x);	
		saveFFT(X);
		this.fft_flag = true;
	}

	
	private void saveFFT(Complex[] X){
		
        FileOutputStream fos = null;
        DataOutputStream salida = null;

        try {
        	fos = new FileOutputStream(InterfaceModelo.fft_module_file);
        	salida = new DataOutputStream(fos);
            for(int i=0;i<X.length;i++){
            	salida.writeFloat((float)(X[i].abs()));
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
        
        try {
        	fos = new FileOutputStream(InterfaceModelo.fft_phase_file);
        	salida = new DataOutputStream(fos);
            for(int i=0;i<X.length;i++){
            	salida.writeFloat((float)X[i].phase());
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
	
	@Override
	public void saveData(String file) throws FileNotFoundException, IOException{
		ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(file));
		oos1.writeObject(data_base);
		oos1.close();
	}
	
	@Override
	public void openFile(String file) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		this.data_base = (dataBase)ois.readObject();
		ois.close();
	}
	
	
	/******************** METODOS GETTER ********************/
	
	
	
	@Override
	public double getSamplingRate() {
		return data_base.getSamplingRate();
	}

	
	@Override
	public String getSignalName() {
		return data_base.getSignalName();
	}
	

		
	@Override
	public String getPortName() {
		return this.port_name;
	}
	
	
	
	public int getCantBits() {
		return data_base.getCantBits();
	}
	
	public double getInputRange() {
		return data_base.getInputRange();
	}
	
	private void calculateCCvalue() {
		double[] data = new double[data_base.getCantMuestras()];
		data = data_base.getData()[1];
		this.cc_value = 0.0;
		for(int i=0;i<data_base.getCantMuestras();i++) {
			cc_value += data[i];
		}
		cc_value = cc_value/data_base.getCantMuestras();
	}
	
	
	public void calculateMaxMinValue() {
		double[] data = new double[data_base.getCantMuestras()];
		data = data_base.getData()[1];
		
		this.max_value = data[0];
		//this.max_value = ((((double)((this.data[0]&0xFF)*256+(this.data[1]&0xFF)))*4)*((2*this.input_range)/(262144.0)))-this.input_range;
		this.min_value = max_value;
		
		for(int i=1;i<data_base.getCantMuestras();i++) {
			if(data[i] > max_value) max_value = data[i];
			if(data[i] < min_value) min_value = data[i];
		}
	}

	public void calculateRMSvalue() {
		this.rms_value = 0;
//		rms_value = 0;
//		for(int i=0;i<cant_muestras*2;i=i+2) {
//			rms_value += Math.pow(((((double)((this.data[i]&0xFF)*256+(this.data[i+1]&0xFF)))*4)*((2*this.input_range)/(262144.0)))-this.input_range,2);
//		}
//		rms_value = rms_value/cant_muestras;
//		rms_value = Math.sqrt(rms_value);
	}
	
	@Override
	public void calculateAllFeatures() {
		calculateMaxMinValue();
		calculateCCvalue();
		calculateRMSvalue();
	}
	
	
	
		
	@Override
	public double[][] getData() {
		return data_base.getData();
	}
	
	@Override
	public int getCantMuestras() {
		return data_base.getCantMuestras();
	}
	
	@Override
	public double getCCvalue() {
		return this.cc_value;
	}
	
	@Override
	public double getMaxValue() {
		return this.max_value;
	}
	
	@Override
	public double getMinValue() {
		return this.min_value;
	}
	
	@Override
	public double getRMSvalue() {
		return this.rms_value;
	}
	


	
	/******************** METODOS SETTER ********************/

	
	
	
	@Override
	public void setCantMuestras(int cant) {
		data_base.setCantMuestras(cant);
	}
	
	@Override
	public void setCantBits(int bits) {
		data_base.setCantBits(bits);
	}
	
	
	@Override
	public void setFs(double fs) {
		data_base.setFs(fs);
		data_base.setTs(1/fs);
		
	}
	
	@Override
	public void setSignalName(String name) {
		data_base.setSignalName(name);
		
	}
	
	
	@Override
	public void setPortName(String port_name) {
		this.port_name = port_name;
	}
	
	@Override
	public void setInputRange(double input_range) {
		data_base.setInputRange(input_range);
	}
	
	@Override
	public void setData(byte MSB, byte LSB, int i) throws Exception{
		this.fft_flag = false;
		data_base.setData(MSB, LSB, i);
		//this.data[i] = MSB;
		//this.data[i+1] = LSB;
		
	}
	
	@Override
	public void resetData() {
		data_base.resetData();
		
	}
	
	
	@Override
	public void setDate(String date) {
		data_base.setDate(date);
	}
	
	@Override
	public String getDate() {
		return data_base.getDate();
	}
	
	@Override
	public String obtainDate() {
		Date date = new Date();
		return( new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date));

	}


	@Override
	public void setUserText(String text) {
		data_base.setUserText(text);
		
	}


	@Override
	public String getUserText() {
		return data_base.getUserText();
	}
	
	@Override
	public List<ArrayList<Double>> getFFT(String file) {
		List<ArrayList<Double>> resul = new ArrayList<>();
		ArrayList<Double> fft = new ArrayList<Double>();
		ArrayList<Double> f = new ArrayList<Double>();
		
		FileInputStream fis = null;
        DataInputStream entrada = null;  
        double fs = data_base.getSamplingRate();
        int N=0;
        try {
        	fis = new FileInputStream(file);
            entrada = new DataInputStream(fis);
            while (true) {
            	fft.add(entrada.readDouble());
            }
           
            
        } catch (EOFException e) {
            System.out.println("Fin de fichero");
            // vector frecuencia
            for(int i=-N/2; i<N/2;i++){
            	f.add(((double)i)*fs/((double)N));
            }
            resul.add(fft);
            resul.add(f);
        }catch(FileNotFoundException e){
        	e.printStackTrace();
		}catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());                                                               
            }
        }
        return resul;
	}
	
}



