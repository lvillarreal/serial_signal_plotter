package modelo;

 public interface InterfaceModelo {

	
	 void setTimeRange(double time);
	 void setFs(double fs);
	 void setSignalName(String name);
	 void setSampleRateUnits(String units);
	 void setTimeUnits(String units);
	 
	 String openFile();
	 String closeFile();
	 String readLine();
	 
	
	 String getSignalName();
	 String getTimeUnits();
	 double getTimeRange();
	 double getSamplingRate();
	 String getSampleRateUnits();

}
