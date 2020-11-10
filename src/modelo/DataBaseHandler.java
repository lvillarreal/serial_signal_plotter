package modelo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
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
import java.util.Arrays;
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
	

    
    
	// Seccion comunicacion serial
	private String port_name;
	private String portId;	// para verificar si el puerto esta conectado o no

	private boolean fft_flag;	// verdadero si fft esta calculada para el dato actual
	private boolean fdiff_flag;	// verdadero si fdiff esta calculada para el dato actual
	
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
		this.fdiff_flag = false;
		
	}
	
	@Override
	public void calculateFirstDIFF() throws IOException, FileNotFoundException{
		int N = data_base.getCantMuestras();
		double[] dato = new double[N];
		double[] resul = new double[N];
		
		dato = data_base.getData()[1];
		
		for(int i=0;i<N-1;i++){
			resul[i] = (dato[i+1]-dato[i])*data_base.getSamplingRate();
		}
		resul[dato.length-1] = resul[dato.length-2];
		
		
		
		saveFirstDIFF(resul);
		
		this.fdiff_flag = true;
	}
	
	@Override
	public void calculateFFT() throws IOException, FileNotFoundException{
		int N = data_base.getCantMuestras();
		double[] dato = new double[N];
		
		Complex[] x = new Complex[N];
		Complex[] X = new Complex[N];
		
		dato = data_base.getData()[1];
		for (int i = 0; i < N; i++) {
            x[i] = new Complex(dato[i], 0);
			//x[i] = new Complex(Math.sin(2*Math.PI*200*i/20000.0),0);
		}
		X = InplaceFFT.fft(x);	
		saveFFT(X);
		this.fft_flag = true;
	}

	@Override
	public void removeFFTfiles(){
		File fftModFile = new File(InterfaceModelo.fft_module_file);
		File fftPhFile = new File(InterfaceModelo.fft_phase_file);
		
		fftModFile.delete();
		fftPhFile.delete();
	}
	
	private void saveFirstDIFF(double[] x)throws IOException, FileNotFoundException{
		FileOutputStream fos_fdiff = new FileOutputStream(InterfaceModelo.firstDiffFile);
        DataOutputStream salida_fdiff = new DataOutputStream(fos_fdiff);
        
        for(int i=0;i<x.length;i++){
        	salida_fdiff.writeFloat((float)x[i]);
        }
        
        
        if (fos_fdiff != null) {
            fos_fdiff.close();
        }
        if (salida_fdiff != null) {
            salida_fdiff.close();
        }
	}
	
	private void saveFFT(Complex[] X) throws IOException, FileNotFoundException {
		FileOutputStream fos_mod = new FileOutputStream(InterfaceModelo.fft_module_file);
        DataOutputStream salida_mod = new DataOutputStream(fos_mod);
        
        double fs = data_base.getSamplingRate();
        double N = (double)X.length;
        for(int i=0;i<X.length;i++){
        	salida_mod.writeFloat((float)(X[i].abs()/N));
        }
        
        
        if (fos_mod != null) {
            fos_mod.close();
        }
        if (salida_mod != null) {
            salida_mod.close();
        }
        
        FileOutputStream fos_ph = new FileOutputStream(InterfaceModelo.fft_phase_file);
        DataOutputStream salida_ph = new DataOutputStream(fos_ph);
        
        for(int i=0;i<X.length;i++){
        	salida_ph.writeFloat((float)(X[i].phase()));
        }
        
        if (fos_ph != null) {
            fos_ph.close();
        }
        
        if (salida_ph!= null) {
            salida_ph.close();
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
	public double[][] getMathData(String option) throws IOException, FileNotFoundException{
		FileInputStream fis = new FileInputStream(option);
		DataInputStream dis = new DataInputStream(fis);
		
		ArrayList<Double> data = new ArrayList<Double>();
		ArrayList<Double> freq = new ArrayList<Double>();
		int index = 0;
		double fs = data_base.getSamplingRate();
		// se lee el archivo
		while(dis.available()>0){
			data.add((double)dis.readFloat());
		}
		if(fis != null)	fis.close();
		if(dis != null) dis.close();
		
		double N = (double)data.size();	//cantidad de muestras
		double[][] resul = new double[2][data.size()];
		
		if(option == InterfaceModelo.firstDiffFile){
			for (int i=0;i<data.size();i++){
				//resul[0][index] = (((double)i)*fs)/N;
				resul[0][index] = (((double)i)/fs);
				resul[1][index] = data.get(index).doubleValue();
				index = index+1;
			}
		}else{
			// se genera el vector frecuencia
			for (int i=(int)(-(data.size())*0.5);i<data.size()*0.5;i++){
				resul[0][index] = (((double)i)*fs)/N;
				resul[1][index] = data.get(index).doubleValue();
				index = index+1;
			}
		}
		return resul;
		
		
	}
	
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
	
	private void calculateCCvalue(double[] data) {
		double cc_value = 0.0;
		for(int i=0;i<data_base.getCantMuestras();i++) {
			cc_value += data[i];
		}
		cc_value = cc_value/data_base.getCantMuestras();
		data_base.setCCvalue(cc_value);
	}
	
	
	private void calculateMaxMinValue(double[] data) {
		
		
		double max_value = data[0];
		//this.max_value = ((((double)((this.data[0]&0xFF)*256+(this.data[1]&0xFF)))*4)*((2*this.input_range)/(262144.0)))-this.input_range;
		double min_value = max_value;
		
		for(int i=1;i<data_base.getCantMuestras();i++) {
			if(data[i] > max_value) max_value = data[i];
			if(data[i] < min_value) min_value = data[i];
		}
		
		data_base.setMaxvalue(max_value);
		data_base.setMinvalue(min_value);
	}

	private void calculateRMSvalue(double[] data) {
		double rms_value = 0;
		for(int i=0;i<data.length;i=i++) {
			rms_value += Math.pow(data[i],2);
		}
		rms_value = rms_value/data.length;
		rms_value = Math.sqrt(rms_value);
		data_base.setRMSvalue(rms_value);
	}
	
	
	/* Calcula CC, Max, Min, RMS
	 * (non-Javadoc)
	 * @see modelo.InterfaceModelo#calculateAllFeatures()
	 */
	@Override
	public void calculateAllFeatures() {
		double[] data = data_base.getData()[1];
		
		this.calculateCCvalue(data);
		this.calculateMaxMinValue(data);
		//this.calculateRMSvalue(data);
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
		return data_base.getCCvalue();
	}
	
	@Override
	public double getMaxValue() {
		return data_base.getMaxvalue();
	}
	
	@Override
	public double getMinValue() {
		return data_base.getMinvalue();
	}
	
	@Override
	public double getRMSvalue() {
		return data_base.getRMSvalue();
	}
	

	@Override
	public String getUserText() {
		return data_base.getUserText();
	}


	
	/******************** METODOS SETTER ********************/

	
	@Override
	public void setImportData(String file) throws FileNotFoundException, IOException, Exception{
		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		int cant = 0;
		data_base.clearImportedData();	
		while(dis.available()>0){
			data_base.setImportedData(dis.readFloat());
			cant++;
		}
		if(fis != null)	fis.close();
		if(dis != null) dis.close();
		
		data_base.setImportDataFlag(true);
		data_base.setCantMuestras(cant);
	}
	
	
	
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
		this.fdiff_flag = false;
		data_base.setData(MSB, LSB, i);
		data_base.setImportDataFlag(false);
		
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


	

}