///** Ridio v1.0
// * 	Purpose	   : ReportOccupationChart
// *  Created by : Abish 
// *  Created Dt : 
// *  Modified on: 
// *  Verified by: Srinivas
// *  Verified Dt: 13-01-2016
// */
//package ridio.helixtech;
//
//import java.util.ArrayList;
//
//import org.achartengine.ChartFactory;
//import org.achartengine.chart.BarChart.Type;
//import org.achartengine.model.XYMultipleSeriesDataset;
//import org.achartengine.model.XYSeries;
//import org.achartengine.renderer.XYMultipleSeriesRenderer;
//import org.achartengine.renderer.XYSeriesRenderer;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.util.Log;
//
//public class ReportOccupationChart extends Activity{
//	//ReportOccupations ro = new ReportOccupations();
//	Context context=null;
//	
//	 private void openChart(){		 
//		 ArrayList<BarEntry> entries = new ArrayList<>();
//			entries.add(new BarEntry(4f, 0));
//			entries.add(new BarEntry(8f, 1));
//			entries.add(new BarEntry(6f, 2));
//			entries.add(new BarEntry(12f, 3));
//			entries.add(new BarEntry(18f, 4));
//			entries.add(new BarEntry(9f, 5));			
//			entries.add(new BarEntry(4f, 7));
//			entries.add(new BarEntry(8f, 8));
//			entries.add(new BarEntry(6f, 9));
//			entries.add(new BarEntry(12f, 10));
//			entries.add(new BarEntry(18f, 11));
//			entries.add(new BarEntry(29f, 12));
//			
//			BarDataSet dataset = new BarDataSet(entries, "# of Calls");
//			ArrayList<String> labels = new ArrayList<String>();
//			labels.add("January"); 
//			labels.add("February"); 
//			labels.add("March"); 
//			labels.add("April"); 
//			labels.add("May");
//			labels.add("June");
//			labels.add("January"); 
//			labels.add("February"); 
//			labels.add("March"); 
//			labels.add("April"); 
//			labels.add("May");
//			labels.add("June");
//			BarChart chart = new BarChart(this);
//			setContentView(chart);
//			BarData data = new BarData(labels, dataset);
//			chart.setData(data);
//			chart.setDescription("# of times Alice called Bob");
//	    }
//	 public void secondChart(Context con,String[] period,String[] values){
//		 context = con;
//		 String[] mMonth = new String[] {
//					"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
//					"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
//				};
//	    	int[] x = { 0,1,2,3,4,5,6,7,8,8,10 };
//	    	int[] income = { 20,5,8,25,2,50,5,10,30,10,40};
//	    	int[] expense = {2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400, 3000, 3300, 3400 };
//	    	
//	    	
//	    	
//	    	
//	    	// Creating an  XYSeries for Income
//	    	//CategorySeries incomeSeries = new CategorySeries("Income");
//	    	XYSeries incomeSeries = new XYSeries("Income");
//	    	// Creating an  XYSeries for Income
//	    	XYSeries expenseSeries = new XYSeries("AAAAAAAA");
//	    	// Adding data to Income and Expense Series
//	    	for(int i=0;i<x.length;i++){    		
//	    		incomeSeries.add(i,income[i]);
//	    		expenseSeries.add(i,expense[i]);
//	    	}
//	    	
//	    	
//	    	// Creating a dataset to hold each series
//	    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
//	    	// Adding Income Series to the dataset
//	    	dataset.addSeries(incomeSeries);
//	    	// Adding Expense Series to dataset
//	    	dataset.addSeries(expenseSeries);    	
//	    	
//	    	
//	    	// Creating XYSeriesRenderer to customize incomeSeries
//	    	XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
//	    	incomeRenderer.setColor(Color.rgb(130, 130, 230));
//	    	incomeRenderer.setFillPoints(true);
//	    	incomeRenderer.setLineWidth(2);
//	    	incomeRenderer.setDisplayChartValues(true);
//	    	
//	    	// Creating XYSeriesRenderer to customize expenseSeries
//	    	XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
//	    	expenseRenderer.setColor(Color.rgb(130, 130, 230));
//	    	expenseRenderer.setFillPoints(true);
//	    	expenseRenderer.setLineWidth(1);
//	    	expenseRenderer.setDisplayChartValues(true);    	
//	    	
//	    	// Creating a XYMultipleSeriesRenderer to customize the whole chart
//	    	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
//	    	multiRenderer.setXLabels(0);
//	    	multiRenderer.setChartTitle("Occupation Chart");
//	    	multiRenderer.setXTitle("Periods of Chart");
//	    	multiRenderer.setYTitle("Total Bookings");
//	    	multiRenderer.setZoomButtonsVisible(true);    	    	
//	    	for(int i=0; i< x.length;i++){
//	    		multiRenderer.addXTextLabel(i, mMonth[i]);    		
//	    	}    	
//	    	
//	    	
//	    	// Adding incomeRenderer and expenseRenderer to multipleRenderer
//	    	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
//	    	// should be same
//	    	multiRenderer.addSeriesRenderer(incomeRenderer);
//	    	multiRenderer.addSeriesRenderer(expenseRenderer);
//	    	Log.d("The Value of Intent is: ", ""+dataset+" "+multiRenderer);
//	    	
//	    	// Creating an intent to plot bar chart using dataset and multipleRenderer    	
//	    	Intent intent = ChartFactory.getBarChartIntent(context, dataset, multiRenderer, Type.DEFAULT);
//	    	
//	    	// Start Activity
//	    	startActivity(intent);
//	 }
//}
