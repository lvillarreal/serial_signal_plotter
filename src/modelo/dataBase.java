package modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class dataBase implements Serializable{
	//private String sample_rate_units; // unidades de frecuencia de muestreo
	//private String time_units;	// unidades del rango de tiempo
	//private double time_range;	// rango de tiempo de la grafica
	private double fs;			// frecuencia de muestreo
	private double Ts;			// periodo de muestreo
	private String signal_name;	// nombre de la señal
	private int cant_bits;		// cantidad de bits del AD
	private double input_range;
	private byte[] data;		// bytes que ingresan por puerto serial
    private int cant_muestras;	//cantidad de muestras en data
	private String date;		// fecha y hora
    private String userText;	// texto que ingrese el usuario en notas

    private double cc_value;	// valor de continua de la señal
    private double max_value;
    private double min_value;
    private double rms_value;
    
    private boolean import_flag;	//verdadero si el dato almacenado es un dato importado
    private ArrayList<Float> imported_data; 
    
    public dataBase(){
    	this.data = new byte[9600000];
    	imported_data = new ArrayList<Float>();
    	setCantBits(18);
    	setCantMuestras(0);
    	setFs(1);
    	this.setInputRange(0);  
    	this.setCCvalue(0);
    	this.setMaxvalue(0);
    	this.setRMSvalue(0);
    	this.setMinvalue(0);
    }
	

	/******************** METODOS GETTER ********************/
    
		
    public byte getDataStatus(){
    	if(this.import_flag) return (byte)1;
    	else return (byte)-1;
    		
    	
    }
    
	public double getSamplingRate() {
		return this.fs;
	}

	public double getTs(){
		return this.Ts;
	}
	
	
	
	public String getSignalName() {
		return this.signal_name;
	}
	
	
	public int getCantBits() {
		return cant_bits;
	}
	
	public double getInputRange() {
		return this.input_range;
	}
	
		
	
	
		
	
	public double[][] getData() {
		
		
		double[][] output;
		double escalar = 4.0;
		
		if(import_flag){
			output = new double[2][this.imported_data.size()];
			for(int i=0;i<this.imported_data.size();i++){
				output[0][i] = (((double)i)*this.Ts);
				output[1][i] = (double)(imported_data.get(i).floatValue());
			}
		}else{
			output = new double[2][this.cant_muestras];
			for(int i=0;i<cant_muestras*2;i=i+2) {
				output[0][i/2] = (((double)i)*this.Ts)/(2.0);
				//output[1][i/2] = ((double)((data[i]*256+data[i+1])*8))*Math.pow(2, -14);
				output[1][i/2] = ((((double)((this.data[i]&0xFF)*256+(this.data[i+1]&0xFF)))*escalar)*((2*this.input_range)/(262144.0)))-this.input_range-this.cc_value;
				//output[1][i/2] = Math.sin(2*Math.PI*200*output[0][i/2]);
				//output[0][i/2] = i/2;
				//output[1][i/2] = ((double)(this.data[i]&0xFF)*256+(this.data[i+1]&0xFF))*escalar;
			}
		}
		return output;
	}
	
	
	public int getCantMuestras() {
		return this.cant_muestras;
	}
	

	public String getDate() {
		return this.date;
	}
	

	
	public String getUserText() {
		return this.userText;
	}
	
	public double getCCvalue(){
		return this.cc_value;
	}
	
	public double getMaxvalue(){
		return this.max_value;
	}
	
	public double getMinvalue(){
		return this.min_value;
	}
	
	public double getRMSvalue(){
		return this.rms_value;
	}
	
	
	/******************** METODOS SETTER ********************/
	
	public void clearImportedData(){
		this.imported_data.clear();
	}
	
	public void  setImportDataFlag(boolean flag){
		this.import_flag = flag;
	}
	
	public void setImportedData(float data) throws Exception{
		this.import_flag = true;
		this.imported_data.add(data);
		
	}
	
	
	public void setCantMuestras(int cant) {
		this.cant_muestras = cant;
		
	}
	
	
	public void setCantBits(int bits) {
		this.cant_bits = bits;
	}
	
	
	
	public void setFs(double fs) {
		this.fs = fs;
		this.Ts = 1/fs;
		
	}
	
	public void setTs(double Ts) {
		this.Ts = Ts;
		this.fs = 1/Ts;
		
	}
	
	
	public void setSignalName(String name) {
		this.signal_name = name;
		
	}
	
		
	
	public void setInputRange(double input_range) {
		this.input_range = input_range;
	}
	
	
	public void setData(byte MSB, byte LSB, int i) throws Exception{
		this.data[i] = MSB;
		this.data[i+1] = LSB;
	}
	
	
	
	public void resetData() {
		for(int j=0;j<this.data.length;j++) {
			this.data[j] = 0;
		}
	}
	
		
	
	public void setDate(String date) {
		this.date = date;
	}	
	
	public void setUserText(String text) {
		this.userText = text;
	}
	
	public void setCCvalue(double data){
		this.cc_value = data;
	}
    
	public void setMaxvalue(double data){
		this.max_value = data;
	}
	
	
	public void setMinvalue(double data){
		this.min_value = data;
	}
	
	public void setRMSvalue(double data){
		this.rms_value = data;
	}





}
