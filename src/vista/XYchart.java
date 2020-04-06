package vista;

import java.awt.Color;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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

public class XYchart {
	
	private JFreeChart xylineChart;
	private ChartPanel chartPanel;
	private XYPlot plot;
	private XYSeries medicion;
	//private XYSeriesCollection dataset;
	private DefaultXYDataset dataset;
	public XYchart() {
		
	}
	
	
	public void startChart(String chartTitle) {
		
		dataset = new DefaultXYDataset();
		
		xylineChart = ChartFactory.createXYLineChart(chartTitle, "time", "Signal", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		chartPanel = new ChartPanel(xylineChart);
		//chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		plot = xylineChart.getXYPlot();
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		
		renderer.setSeriesShapesVisible(0, false);	// quita los puntos
		renderer.setSeriesPaint(0, Color.RED);		
//		renderer.setSeriesPaint(1, Color.GREEN);
//		renderer.setSeriesPaint(2, Color.YELLOW);
		renderer.setSeriesStroke(0, new BasicStroke(1.5f));
//		renderer.setSeriesStroke(1, new BasicStroke(3.0f));
//		renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		plot.setRenderer(renderer);	
		
	}

	public void createDataset() {
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
	}
	
	public void actualiceDataset(String signal_name, double[][]data) {
	//	medicion.add(x,y);
		dataset.addSeries(signal_name, data);
		
		
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
}
