package vista;

import java.awt.Color;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

public class XYchart{
	
	private JFreeChart xylineChart;
	private ChartPanel chartPanel;
	private XYPlot plot;
	private XYSeries medicion;
	//private XYSeriesCollection dataset;
	private DefaultXYDataset dataset;
	private XYLineAndShapeRenderer renderer;
	private String actualSerie;
	
	
	public XYchart() {
	}

	
	
	public void startChart(String chartTitle) {
		
		dataset = new DefaultXYDataset();
		
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "time [s]", "Signal", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		chartPanel = new ChartPanel(xylineChart);
		//chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		plot = xylineChart.getXYPlot();
	
		renderer = new XYLineAndShapeRenderer();
		
		renderer.setSeriesShapesVisible(0, false);	// quita los puntos
		renderer.setSeriesPaint(0, Color.BLUE);		
//		renderer.setSeriesPaint(1, Color.GREEN);
//		renderer.setSeriesPaint(2, Color.YELLOW);
		renderer.setSeriesStroke(0, new BasicStroke(1.0f));
//		renderer.setSeriesStroke(1, new BasicStroke(3.0f));
//		renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		plot.setRenderer(renderer);	
		
		
	}

	
/*	public void createDataset() {
		//medicion = new XYSeries("Medicion");
		
		//medicion.add(1.0, 1.0);
		//medicion.add(2.0, 4.0);
		//medicion.add(3.0, 3.0);
		/*final XYSeries chrome = new XYSeries("Chrome");
		chrome.add(1.0, 4.0);
		chrome.add(2.0, 5.0);
		chrome.add(3.0, 6.0);
		final XYSeries iexplorer = new XYSeries("InternetExplorer");
		iexplorer.add(3.0, 4.0);
		iexplorer.add(4.0, 5.0);
		iexplorer.add(5.0, 4.0);*/
//		dataset = new XYSeriesCollection();
//		dataset.addSeries(medicion);
		
//		dataset.addSeries(chrome);
//		dataset.addSeries(iexplorer);
		//return dataset;
//	}
	
	
	public void actualiceDataset(String signal_name, double[][]data) {
	//	medicion.add(x,y);
		 dataset.removeSeries(this.actualSerie);
		 XYToolTipGenerator xyTooltipGenerator  = new XYToolTipGenerator()
		 {
		     public String generateToolTip(XYDataset dataset, int series, int item)
		     {
		         Number x1 = dataset.getX(series, item);
		         Number y1 = dataset.getY(series, item);
		         StringBuilder stringBuilder = new StringBuilder();
		         stringBuilder.append(String.format("<html><p style='color:#0000ff;'>Serie: '%s'</p>", dataset.getSeriesKey(series)));
		         stringBuilder.append(String.format("X:'%.5f'<br/>", x1.doubleValue()));
		         stringBuilder.append(String.format("Y:'%.5f'", y1.doubleValue()));
		         stringBuilder.append("</html>");
		         return stringBuilder.toString();
		     }
		 };
		
		dataset.addSeries(signal_name, data);
		this.actualSerie = signal_name;
		renderer.setBaseToolTipGenerator(xyTooltipGenerator);

	}
	

	
	public void actualiceTitle(String title) {
		xylineChart.setTitle(title);
	}
	
	public void deleteDataset(String name) {
		//medicion.clear();
		dataset.removeSeries(name);
		
		
	}

	
	public ChartPanel getChartPanel() {
		return chartPanel;
	}
	
	public XYPlot getXYPlot() {
		return plot;
	}

	public void viewDataset() {
		System.out.println("Cantidad de items: "+medicion.getItemCount());
		
	}
	
	public double getDataItem(int index) {
		return Integer.parseInt(String.valueOf(medicion.getY(index)));
	}

	public void setActualSerie(String name) {
		this.actualSerie = name;
	}
	
	// muestra u oculta los puntos de medicion
	public void setShapesVisible(boolean option) {
		renderer.setSeriesShapesVisible(0,option);
	}

}
