package modelo;

public interface InterfaceModelo {

	
	public void setTimeRange(double time);
	public void setFs(double fs);
	public void setSignalName(String name);
	public void setSignalData(Integer dataIn);
	public void setSampleRateUnits(String units);
	public void setTimeUnits(String units);
	 
	
	public String getSignalName();
	public String getTimeUnits();
	public double getTimeRange();
	public double getSamplingRate();
	public String getSampleRateUnits();

}
