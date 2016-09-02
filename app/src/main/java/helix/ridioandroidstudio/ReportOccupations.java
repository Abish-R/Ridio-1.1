/** Ridio v1.0.1
 * 	Purpose	   : Displaying Occupation Chart and Report
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 25-01-2016
 *  Verified by:
 *  Verified Dt: 
 * **/

package helix.ridioandroidstudio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReportOccupations extends Activity implements OnClickListener,OnItemSelectedListener{
	/**Objects for accessing the Class*/
	NewCustomerDatabaseHandler ncdbh= new NewCustomerDatabaseHandler(this);
	VehicleDatabaseHandler vdbh= new VehicleDatabaseHandler(this);
	//ReportOccupationChart roc=new ReportOccupationChart();
	
	/**Initialization Starts*/
	List<Integer> c_id=new ArrayList<Integer>();	
	List<String> bk_percent=new ArrayList<String>();
	List<String> bk_total=new ArrayList<String>();
	List<String> mnth_=new ArrayList<String>();
	List<String> mnth_days=new ArrayList<String>();
	List<String> what_month=new ArrayList<String>();
	List<String> tl_bikes=new ArrayList<String>();
	List<String> tl_revenue=new ArrayList<String>();
	
	LinearLayout header_layout,week_layout,month_layout,title_layout;
	
	TextView heading;
	EditText from_date,to_date;
	Button go;
	ImageButton img_from,img_to;
	ListView list_occupation_report;
	Spinner week_month,from_year,from_month,to_year,to_month;
	String bike_no_from_adapter,what_bike,vendor_id,spvalue,intentvalue,today;
	String[] weekmonth={"Weekly","Monthly"};
	String[] month_dips={"January","February","March","April","May","June","July","August",
			"September","October","November","December"};
	String[] no_of_days={"31","28","31","30","31","30","31","31","30","31","30","31"};
	String[] years=new String[6];
	String selectedoption,selected_fromyear,selected_toyear,selected_frommonth,selected_tomonth;
	String ss,mm,jsonFromDate,jsonToDate,no_of_day;
	
	SimpleDateFormat df,df1,df2;
    Date oldDate,newDate,oldDate1,newDate1;
    private int day,month,year,hour,minute;
    Calendar c = Calendar.getInstance();    
    JSONObject root;
    int validation=0,size=0,positionFromSelectedMonth,positionToSelectedMonth,get_count;
    
    ArrayAdapter<String> weekly_monthly,months_year,from_year1;
    /**Initialization Ends*/
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_occupation_report);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initialize();
		/**Date format informer*/
		df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		df1 = new SimpleDateFormat("yyyy",Locale.ENGLISH);
		df2 = new SimpleDateFormat("MM",Locale.ENGLISH);
		ss=df1.format(c.getTime());
		mm=df2.format(c.getTime());
		for(int i =5;i>=0;i--){
			int temp=Integer.parseInt(ss)+i-5;	
			years[5-i]=temp+"";
		}
		
		/**Getting Intent and set Title*/
		intentvalue = getIntent().getExtras().getString("intent_is");
		if(intentvalue.equals("occupationreport"))
			heading.setText("Occupancy Report");
		else
			heading.setText("Occupancy Chart");
		
		/**Getting Vendor ID*/
		SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
         spvalue = sp.getString("value", "");
         vendor_id=spvalue;
        Log.e("UserId in New Customer", spvalue);
        
        /**Default Date Display*/
		defaultDateCal(); 
		
		go.setOnClickListener(this);
		img_from.setOnClickListener(this);
		img_to.setOnClickListener(this);
		week_month.setOnItemSelectedListener(this);
		from_month.setOnItemSelectedListener(this);
		to_month.setOnItemSelectedListener(this);
		from_year.setOnItemSelectedListener(this);
		to_year.setOnItemSelectedListener(this);
		
		/**Spinner Setting*/
		weekly_monthly = new ArrayAdapter<String>(this,R.layout.custom_dropdown, weekmonth);
		week_month.setAdapter(weekly_monthly);
		months_year = new ArrayAdapter<String>(this,R.layout.custom_dropdown, month_dips);
		from_month.setAdapter(months_year);
		to_month.setAdapter(months_year);
		from_year1 = new ArrayAdapter<String>(this,R.layout.custom_dropdown, years);
		from_year.setAdapter(from_year1);
		to_year.setAdapter(from_year1);

		}
    
    /**On Item Selected Decider*/
    @Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	if(parent.getId()== R.id.week_month){
        	selectedoption=parent.getItemAtPosition(position).toString();
        	checkText();
    	}
    	if(parent.getId()== R.id.from_month){
        	selected_frommonth=parent.getItemAtPosition(position).toString();
        	positionFromSelectedMonth=position+1;
    	}
    	if(parent.getId()== R.id.from_year){
        	selected_fromyear=parent.getItemAtPosition(position).toString();
    	}
    	if(parent.getId()== R.id.to_month){
        	selected_tomonth=parent.getItemAtPosition(position).toString();
        	positionToSelectedMonth=position+1;
    	}
    	if(parent.getId()== R.id.to_year){
        	selected_toyear=parent.getItemAtPosition(position).toString();
    	}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
		
	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.go){
			get_count=vdbh.getVehiclesCount(vendor_id);
			
			long temp=0;
			int temp1=bringLocalTableCustomerCount();
			if(selectedoption.equals("Monthly")){
				jsonFromDate=selected_fromyear+"-"+positionFromSelectedMonth+"-"+"01";
				jsonToDate=selected_toyear+"-"+positionToSelectedMonth+"-"+no_of_days[positionToSelectedMonth-1];
				if(checkYear()==1){
					if(temp1>1)
						alertWarning();
					else{
						if(isOnline()){
							if(intentvalue.equals("occupationreport"))
								title_layout.setVisibility(View.VISIBLE);
							new ReportOccupation().execute(vendor_id,jsonFromDate,jsonToDate,get_count+"",selectedoption);
							
						}
						else
							alertInternet();
						}
					}
			}
			else{
				temp=getDateDiffString();
		        if(temp>0){			        	
					if(temp1>1)
						if(isOnline())
							alertWarning();
						else
							alertInternet();
					else{
						if(isOnline()){
							if(intentvalue.equals("occupationreport"))
								title_layout.setVisibility(View.VISIBLE);
			        		new ReportOccupation().execute(vendor_id,from_date.getText().toString(),
			        				to_date.getText().toString(),get_count+"",selectedoption);
			        		
			        		Log.d("Date Check", from_date.getText().toString()+" "+to_date.getText().toString());
						}
		        		else
							alertInternet();
					}
		        }
			}
		}
		if(v.getId()== R.id.img_from)
			showDialog(0);
		if(v.getId()== R.id.img_to)
			showDialog(1);
	}
	/**Checks year and month selected */
	private int checkYear() {
		if(Integer.parseInt(selected_toyear)<Integer.parseInt(selected_fromyear)){
				// || Integer.parseInt(ss)>Integer.parseInt(selected_toyear)){ 
			alertForWrongYear();
			return 0;
		}
		else if(Integer.parseInt(selected_fromyear)==Integer.parseInt(selected_toyear) && 
			positionFromSelectedMonth>positionToSelectedMonth){
			alertForWrongMonth();
			return 0;
		}
		else if(Integer.parseInt(ss)==Integer.parseInt(selected_toyear) && 
			Integer.parseInt(mm)<positionToSelectedMonth){
			alertForWrongMonth();
			return 0;
		}
		return 1;
	}
	/**Check the text of 1st dropdown*/
	private void checkText() {
		if(selectedoption.equals("Weekly"))
			hideviews();
		else
			secondHideViews();
	}
	/**Hide some widgets when seleted weekly */
	private void hideviews() {
		month_layout.setVisibility(View.GONE);
		week_layout.setVisibility(View.VISIBLE);
//		from_year.setVisibility(View.GONE);
//		from_month.setVisibility(View.GONE);
//		to_year.setVisibility(View.GONE);
//		to_month.setVisibility(View.GONE);
//		from_date.setVisibility(View.VISIBLE);
//		to_date.setVisibility(View.VISIBLE);
//		img_from.setVisibility(View.VISIBLE);
//		img_to.setVisibility(View.VISIBLE);
	}
	/**Hide some widgets when seleted monthly */
	private void secondHideViews() {
		week_layout.setVisibility(View.GONE);
		month_layout.setVisibility(View.VISIBLE);
//		from_year.setVisibility(View.VISIBLE);
//		from_month.setVisibility(View.VISIBLE);
//		to_year.setVisibility(View.VISIBLE);
//		to_month.setVisibility(View.VISIBLE);
//		from_date.setVisibility(View.GONE);
//		to_date.setVisibility(View.GONE);
//		img_from.setVisibility(View.GONE);
//		img_to.setVisibility(View.GONE);
	}
	/**All Views Initialization*/
	private void initialize() {
		heading=(TextView)findViewById(R.id.heading);
		from_date=(EditText)findViewById(R.id.from_date);
		to_date=(EditText)findViewById(R.id.to_date);
		go=(Button)findViewById(R.id.go);
		img_from=(ImageButton)findViewById(R.id.img_from);
		img_to=(ImageButton)findViewById(R.id.img_to);
		list_occupation_report=(ListView)findViewById(R.id.list_occupation_report);
		week_month=(Spinner)findViewById(R.id.week_month);
		from_year=(Spinner)findViewById(R.id.from_year);
		from_month=(Spinner)findViewById(R.id.from_month);
		to_year=(Spinner)findViewById(R.id.to_year);
		to_month=(Spinner)findViewById(R.id.to_month);
		
		title_layout=(LinearLayout)findViewById(R.id.title_layout);
		week_layout=(LinearLayout)findViewById(R.id.week_layout);
		month_layout=(LinearLayout)findViewById(R.id.month_layout);
		header_layout=(LinearLayout)findViewById(R.id.header_layout);
	}
	/**Default date set 1st time*/
	public void defaultDateCal(){		  
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        hour  = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
			
		from_date.setText(df.format(c.getTime()));
		to_date.setText(df.format(c.getTime()));		
		getDateDiffString();
	}
	/**Check Date difference*/
	public Long getDateDiffString(){
		long diff=0,findfromdatefault=0;
		Date now,nowdte;
		try {
			oldDate = df.parse(from_date.getText().toString());
			newDate = df.parse(to_date.getText().toString());
			oldDate1=df.parse(df.format(oldDate));
			newDate1=df.parse(df.format(newDate));
			nowdte = df.parse(df.format(c.getTime()));
			now=df.parse(df.format(nowdte));
			findfromdatefault=newDate1.getTime()-now.getTime();
			findfromdatefault=findfromdatefault/86400000;
			diff = newDate1.getTime() - oldDate1.getTime();
			diff=diff/86400000;
			if(findfromdatefault > 0){
				alertMessageWrongToDate();
				return diff=0;
			}
			else if(diff<0) {
				alertMessageWrongFromDate();
				return diff=0;
			}
			else {
				return diff=1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diff;
	}
	/**Get table data count those are pending to upload*/
	private int bringLocalTableCustomerCount() {
		List<NewCustomerGetSetter> cust_list = ncdbh.getAllCustomerNotUploaded();
		 for (NewCustomerGetSetter cn : cust_list) {
			 size++;
			 c_id.add(cn.getID());//[count]=cn.getID();
		 }
		 return size;
	}
		
	/** Date picker action listeners*/
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		if(id==0)
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		else if(id==1)
			return new DatePickerDialog(this, datePickerListener1, year, month, day);
		return new DatePickerDialog(this, datePickerListener1, 0, 0, 0);
	 }
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			  if(selectedMonth<9 && selectedDay<10)
				  from_date.setText(selectedYear + "-0" + (selectedMonth + 1) + "-0"  + selectedDay);
			  else if(selectedMonth<9)
				  from_date.setText(selectedYear + "-0" + (selectedMonth + 1) + "-"  + selectedDay);
			  else if(selectedDay<10)
				  from_date.setText(selectedYear + "-" + (selectedMonth + 1) + "-0"  + selectedDay);
			  else
				  from_date.setText(selectedYear + "-" + (selectedMonth + 1) + "-"  + selectedDay);
			  //getDateDiffString();
		  }
	};
	private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
		  if(selectedMonth<9 && selectedDay<10)
			  to_date.setText(selectedYear + "-0" + (selectedMonth + 1) + "-0"  + selectedDay);
		  else if(selectedMonth<9)
			  to_date.setText(selectedYear + "-0" + (selectedMonth + 1) + "-"  + selectedDay);
		  else if(selectedDay<10)
			  to_date.setText(selectedYear + "-" + (selectedMonth + 1) + "-0"  + selectedDay);
		  else
			  to_date.setText(selectedYear + "-" + (selectedMonth + 1) + "-"  + selectedDay);
		  //getDateDiffString();
		}
	};
						 
	/**Alert messages*/
	private void alertMessageWrongFromDate() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportOccupations.this);
        alertDialogBuilder.setTitle("Report Details");
        alertDialogBuilder.setMessage("From date not be greater than today's / To date.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Reset",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          	showDialog(0);
        }						
        });
	AlertDialog alertDialog = alertDialogBuilder.create();
	alertDialog.show();
	}
	private void alertMessageWrongToDate() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportOccupations.this);
        alertDialogBuilder.setTitle("Report Details");
        alertDialogBuilder.setMessage("To Date not be greater than today's / lesser than From date.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Reset",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
      	showDialog(1);
        }						
        });
	AlertDialog alertDialog = alertDialogBuilder.create();
	alertDialog.show();
	}
	private void alertForWrongYear(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportOccupations.this);
        alertDialogBuilder.setTitle("Wrong Year");
        alertDialogBuilder.setMessage("Select correct year.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Check",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
        }
        });
    AlertDialog alertDialog = alertDialogBuilder.create();
  	alertDialog.show();
	}
	private void alertForWrongMonth(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportOccupations.this);
        alertDialogBuilder.setTitle("Wrong Month");
        alertDialogBuilder.setMessage("Select correct Month.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Check",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          	//goSyncClass();
        }
       });
       AlertDialog alertDialog = alertDialogBuilder.create();
       alertDialog.show();
	}
	private void alertWarning() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportOccupations.this);
        alertDialogBuilder.setTitle("Sync Pending Available");
        alertDialogBuilder.setMessage("Do Sync To get accurate Reports.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Go Sync",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          	goSyncClass();
        }
        });
        alertDialogBuilder.setNeutralButton("Cancel",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          	goMainReport();
        }
        });
        alertDialogBuilder.setNegativeButton("No Problem",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          	if(selectedoption.equals("Monthly")){
              	if(isOnline()){
              		if(intentvalue.equals("occupationreport"))
              			title_layout.setVisibility(View.VISIBLE);
              		new ReportOccupation().execute(vendor_id,jsonFromDate,jsonToDate,get_count+"",selectedoption);
              	}
				else
					alertInternet();
               	}
           	else{
           		if(isOnline()){
           			if(intentvalue.equals("occupationreport"))
           				title_layout.setVisibility(View.VISIBLE);
	        		new ReportOccupation().execute(vendor_id,from_date.getText().toString(),
        				to_date.getText().toString(),get_count+"",selectedoption);
	        		Log.d("Date Check", from_date.getText().toString()+" "+to_date.getText().toString());
				}
           		else
           			alertInternet();
           	}
        }						
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
	/**Check Internet*/
	public boolean isOnline() {
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) 
                for (int i = 0; i < info.length; i++) 
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }	        
        return false;
	}
	private void alertInternet() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportOccupations.this);
        alertDialogBuilder.setTitle("Reports");
        alertDialogBuilder.setMessage("Check Internet.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
        }						
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
	/**If pending available goes to sync class*/
	private void goSyncClass() {
		Intent sync=new Intent(getApplicationContext(),SyncClass.class);
       	startActivity(sync);
       	finish();
	}
	/**If dont want to see reports takes to reports navigation page*/
	private void goMainReport() {
		Intent main_report=new Intent(getApplicationContext(),ReportMainClass.class);
       	startActivity(main_report);
       	finish();
	}
		
	/** On back button pressed close the current Activity **/
	public void onBackPressed() {
	     super.onBackPressed();
	     finish();
	}
		
	/**Report Getting Class from Helix DB*/
	class ReportOccupation extends AsyncTask<String, Void, String> {		
		private static final String TAG = "Inside Thread Past/Current Report";
		int temp=0;
		ProgressDialog progressDialog;
		protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
            progressDialog = new ProgressDialog(ReportOccupations.this, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
        }
	 
        @Override
        protected String doInBackground(String... arg0) {
        	String result=null;
           	try{
           		String postReceiverUrl;
           		//int i=0;
                // url where the data will be posted   
      			postReceiverUrl = "http://phpws.ridio.in/OccupancyChart";
                //Log.d(TAG, "postURL: " + postReceiverUrl);
                HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
                HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
	                    
               // add your data
               List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
               //   Log.d(hotel_name.getText().toString(), hotel_place.getText().toString());
               nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
               nameValuePairs.add(new BasicNameValuePair("from_date", arg0[1]));
               nameValuePairs.add(new BasicNameValuePair("to_date", arg0[2]));
               nameValuePairs.add(new BasicNameValuePair("totalvehiclesofvendor", "5"));
               nameValuePairs.add(new BasicNameValuePair("report_basedon", arg0[4]));
               UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
               httpPost.setEntity(entity);
	                                   
               // execute HTTP post request
               HttpResponse response = httpClient.execute(httpPost);
               result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            	}
       return result;
     }
	 
    @Override
    protected void onPostExecute(String result) {
        // do stuff after posting data
     	progressDialog.dismiss();
      	String message=null;   
      	JSONArray data=null;
		try {
			Log.d("Inside Post", "message");
			root = new JSONObject(result);
			validation = root.getInt("response");
			message = root.getString("message");
			if(message.equals("Failure"))
				alertNoDatafromCloudDB();
				//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			else
				data=root.getJSONArray("data");
				//data.length();
				Log.d(result, message);
				if(validation==1) {
					//new ReportGetSetter();
					bk_percent.clear();
					bk_total.clear();
					mnth_.clear();
					mnth_days.clear();
					what_month.clear();
					tl_bikes.clear();
					tl_revenue.clear();
					String[] period=new String[data.length()];int[] values=new int[data.length()];
					for(int i=0;i<data.length();i++){
						String _mnth="";
						String percen=data.getJSONObject(i).getString("BookingPercentage");
						String tot_book=data.getJSONObject(i).getString("TotalBookings");
						values[i]=Integer.parseInt(tot_book);
						//String _mnth=data.getJSONObject(i).getString("Period");
						String tot_days_mnth=data.getJSONObject(i).getString("TotalDays");
						if(Integer.parseInt(tot_days_mnth)==7){
							_mnth="Week "+data.getJSONObject(i).getString("Period");
							period[i]=_mnth;
						}
						else{
							_mnth=data.getJSONObject(i).getString("Period");
							period[i]=_mnth;
						}
						String month=data.getJSONObject(i).getString("ReportBasedOn");
						String tot_veh=data.getJSONObject(i).getString("TotalVehicles");
						String tot_revnue=data.getJSONObject(i).getString("TotalAmount");
							
						bk_percent.add(percen);						
						bk_total.add(tot_book);	
						mnth_.add(_mnth);	
						mnth_days.add(tot_days_mnth);							
						what_month.add(month);
						tl_bikes.add(tot_veh);	
						tl_revenue.add(tot_revnue);
					}
					if(intentvalue.equals("occupationreport"))												
						callOccupationsReport();
					else{
						list_occupation_report.setVisibility(View.GONE);
						//roc.secondChart(period,values);
						secondChart(period,values);
					}
				}
//			else
//				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
    }
	}
	private void alertNoDatafromCloudDB() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportOccupations.this);
		alertDialogBuilder.setTitle("Report");
		alertDialogBuilder.setMessage("No Data found for the Date.!");
		// set positive button: Yes message
		alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
			}						
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	/**Shows occupation report*/
	public void callOccupationsReport(){	        		
		list_occupation_report.setAdapter(new ReportCustomAdapterOccupanyReport(this, bk_percent,bk_total,
				mnth_,mnth_days,what_month,tl_bikes,tl_revenue));
	}
	/**Shows chart for the vendor*/
	public void secondChart(String[] period,int[] values){
		String[] mMonth = new String[] {
				"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
				"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
		};
		int yy[]=values;
		String[] periods=period;
		int len=0;
	    	//int[] x = {0,1,2,3,4,5,6,7,8,8,10};
	    	//int[] income = { 20,5,8,25,2,50,5,10,30,10,40};
	    	//int[] expense = {2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400, 3000, 3300, 3400 };
	    	
		// Creating an  XYSeries for Income
		//CategorySeries incomeSeries = new CategorySeries("Income");
		XYSeries incomeSeries = new XYSeries("Occupations per month/week");
		// Creating an  XYSeries for Income
		XYSeries expenseSeries = new XYSeries("");
		
		// Adding data to Income and Expense Series
		for(int i=0;i<yy.length;i++){    		
			incomeSeries.add(i,yy[i]);
			//expenseSeries.add(i,expense[i]);
		}
	    	
		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(incomeSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);    	
	    	
		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		incomeRenderer.setColor(Color.rgb(130, 130, 230));
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(2);
		incomeRenderer.setDisplayChartValues(true);
		
		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.rgb(130, 130, 230));
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(1);
		expenseRenderer.setDisplayChartValues(true);    	
	    	
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Vehicle Occupation Chart");
		multiRenderer.setXTitle("");
		multiRenderer.setYTitle("Total Bookings");
		multiRenderer.setZoomButtonsVisible(true);    	    	
		for(int i=0; i< yy.length;i++){
			multiRenderer.addXTextLabel(i, periods[i]);    		
		}    	
	    	
		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);
		//Log.d("The Value of Intent is: ", ""+dataset+" "+multiRenderer);
	    	
		// Creating an intent to plot bar chart using dataset and multipleRenderer    	
//		Intent intent = ChartFactory.getBarChartIntent(getApplicationContext(), dataset, multiRenderer, Type.DEFAULT);
//		// Start Activity
//		startActivity(intent);
		View intent = ChartFactory.getBarChartView(getApplicationContext(), dataset, multiRenderer, Type.DEFAULT);
		header_layout.addView(intent);
	}
}
