package vista;

import controlador.Controlador;

public interface InterfaceVista {
		
	static final byte chart_signalName = 0;
	static final byte chart_timeRange = 1;
	static final byte chart_timeUnits = 2;
	static final byte chart_sampleRate = 3;
	static final byte chart_sampleRateRange = 4;
	static final byte chart_all_features = 5;
	static final byte chart_data = 6;
	
	void setControlador(Controlador c);
	void start();
	
    //void setConfigGraph();
    void showConfigGraph();
    void closeConfigGraph();
	
    void setSignalName(String name);
    void actualiceChartData(double x,double y);
    void deleteChartData();
    
    void setButtonEnable(String button,boolean option);	// permite cambiar el texto de los botones
    
    
	void writeConsole(String list);
	String getConfigSampleRate();
	String getSampleRateUnits();
	String getConfigTimeRange();
	String getTimeRangeUnits();
	String getNewSignalName();

	
	// ActionCommands para los eventos
	static final String ButtonConnectPushed = "ButtonConnectedPushed";	// Para ActionCommand del boton connect
	static final String ButtonStartPushed = "ButtonStartPushed";		// Para comenzar la captura de datos
	
	static final String ListSerialPorts = "ListSerialPorts";			// Para listar los puertos seriales conectados
	static final String MenuButtonExitPushed = "MenuButtonExitPushed";	// Cuando se presiona el boton File/Exit

	static final String ConfigTimeRange = "ConfigTimeRange";			// Configurar rango de tiempo de la grafica
	static final String ConfigSamplingRate = "ConfigSamplingRate";		// Configurar frecuencia de muestreo
	
	static final String GraphFFTmodule = "GraphFFTmodule";
	static final String GraphFFTangle = "GraphFFTangle";
	static final String CalculateFFT = "CalculateFFT";					// Calcula la FFT

	
	static final String ConfigGraph = "ConfigGraph";					// Configurar Grafico
	static final String ConfigGraphAddTime = "ConfigGraphAddTime";
	static final String ConfigGraphAddSampleRate = "ConfigGraphAddSampleRate";
	static final String ConfigGraphAddSignalName = "ConfigGraphAddSignalName";
	static final String ConfigGraphClose = "ConfigGraphClose";	// cierra la ventana de configuracion de grafico

	static final String GetSamplingRate = "GetSamplingRate";	// muestra la frecuencia de muestreo actual
	static final String GetTimeRange = "GetTimeRange";			// muestra el rango de tiempo de la grafica

	static final String ButtonStartEnable = "ButtonStartEnable";	// permite habilitar y deshabilitar el boton start
	static final String ButtonConnectEnable = "ButtonConnectEnable";	// permite habilitar y deshabilitar el boton connect

	
}
