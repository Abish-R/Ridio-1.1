/** Ridio v1.0.1
 * 	Purpose	   : Generate revenue reports based on bikes 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: (not using. changed to Future reports)
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ReportRevenueBasedOnBikes extends Activity implements OnClickListener {
	/**Create object for other class to access*/
	VehicleDatabaseHandler vdb=new VehicleDatabaseHandler(this);
	NewCustomerDatabaseHandler ncdbh= new NewCustomerDatabaseHandler(this);
	BikeSpinnerAdapter adapter;
	/**Class Variables*/
	List<Integer> c_id=new ArrayList<Integer>();
	public  ArrayList<BikeSpinner> CustomListViewValuesArr = new ArrayList<BikeSpinner>();
	
	EditText from_date,to_date;
	TextView heading;
	LinearLayout text_layout_revenue;
	Button go;
	ImageButton img_from,img_to;
	ListView list_past_report;
	Spinner bike_spin;
	
	List<String> bike_reg=new ArrayList<String>();
	List<String> bike_n=new ArrayList<String>();
	List<String> total_dy=new ArrayList<String>();
	List<String> booked_dy=new ArrayList<String>();
	List<String> days_percent=new ArrayList<String>();
	List<String> total_amt=new ArrayList<String>();
	String bike_no_from_adapter,what_bike,vendor_id,spvalue,intentvalue,today;
	
	SimpleDateFormat df;
    Date oldDate,newDate,oldDate1,newDate1;
    private int day,month,year,hour,minute;
    Calendar c = Calendar.getInstance();
    int validation=0,size=0;
    JSONObject root;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_past_current_future);
		initialize();
		df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		/**Get vendor id*/
		SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
		spvalue = sp.getString("value", "");
		vendor_id=spvalue;
        Log.e("UserId in New Customer", spvalue);
//      intentvalue = getIntent().getExtras().getString("intent_is");
//      if(intentvalue.equals("projectedrevenue")){
        defaultDateCal();
        setListData();
        setBikeAdapterValues();
//    		}
        
        go.setOnClickListener(this);
		img_from.setOnClickListener(this);
		img_to.setOnClickListener(this);
	}
	/**get not uploaded count from db*/
	private int bringLocalTableCustomerCount() {
		List<NewCustomerGetSetter> cust_list = ncdbh.getAllCustomerNotUploaded();
		 for (NewCustomerGetSetter cn : cust_list) {
			 size++;
			 c_id.add(cn.getID());//[count]=cn.getID();
		 }
		 return size;
	}
	
	/** On back button pressed close the current Activity **/
	public void onBackPressed() {
	     super.onBackPressed();
	     finish();
	}
	/**Initialize views*/
	private void initialize() {
		heading=(TextView)findViewById(R.id.heading);
		heading.setText("Projected Revenue");
		from_date=(EditText)findViewById(R.id.from_date);
		to_date=(EditText)findViewById(R.id.to_date);
		go=(Button)findViewById(R.id.go);
		img_from=(ImageButton)findViewById(R.id.img_from);
		img_to=(ImageButton)findViewById(R.id.img_to);
		list_past_report=(ListView)findViewById(R.id.list_past_report);
		bike_spin=(Spinner)findViewById(R.id.bike_spin);		
		text_layout_revenue=(LinearLayout)findViewById(R.id.text_layout_revenue);
	}
	/**Default date setting*/
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
	/**Date Difference Calculation*/
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
	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.go){
			long temp=getDateDiffString();
			int temp1;
	        if(temp>0){
	        	temp1=bringLocalTableCustomerCount();
				if(temp1>1)
					if(isOnline())
						alertWarning();
					else
						alertInternet();
				else{
					if(isOnline()){
						text_layout_revenue.setVisibility(v.VISIBLE);
						new PastReport().execute(vendor_id,from_date.getText().toString(),
	        				to_date.getText().toString(),bike_no_from_adapter);
						Log.d("Date Check",to_date.getText().toString()+" "+bike_no_from_adapter);
					}
					else
						alertInternet();
				}
	        }
//	        else
//	        	Toast.makeText(getApplicationContext(), "Check Date", Toast.LENGTH_LONG).show();
		}
		if(v.getId()== R.id.img_from)
			showDialog(0);
		if(v.getId()== R.id.img_to)
			showDialog(1);
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
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
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
	/**Set all bike of vendor into the list*/
	private void setBikeAdapterValues() {
		Resources res = getResources();
		adapter = new BikeSpinnerAdapter(ReportRevenueBasedOnBikes.this, R.layout.bike_spinner, CustomListViewValuesArr,res);
		bike_spin.setAdapter(adapter);
		bike_spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
				// your code here
				// Get selected row data to show on screen
				bike_no_from_adapter   = ((TextView) v.findViewById(R.id.textbike_numbr)).getText().toString();
				what_bike   = ((TextView) v.findViewById(R.id.textbike)).getText().toString();
				String OutputMsg = "Selected : \n\n"+bike_no_from_adapter;
				//Toast.makeText(getApplicationContext(),OutputMsg+" "+" Position: "+position, Toast.LENGTH_LONG).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				//Toast.makeText(getApplicationContext(), "You Must Select Vehicle", Toast.LENGTH_LONG).show();
			}
		});
	}
	/**Set the list to adapter*/
	public void setListData(){
		// Now i have taken static values by loop.
		// For further inhancement we can take data by webservice / json / xml;
		List<VehicleGetSetter> contacts = vdb.getAllContacts();
		for (VehicleGetSetter cn : contacts) {
			final BikeSpinner sched = new BikeSpinner();
			/*** Firstly take data in model object **/
			sched.setBikeName(cn.getNumber());
			//sched.setImage("image"+i);
			sched.setBikeNumber(cn.getModelName());
			/** Take Model Object in ArrayList */
			CustomListViewValuesArr.add(sched);
		}
	}
	/**Alert Dialog messages*/
	private void alertMessageWrongFromDate() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportRevenueBasedOnBikes.this);
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
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportRevenueBasedOnBikes.this);
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
	private void alertWarning() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportRevenueBasedOnBikes.this);
		alertDialogBuilder.setTitle("Sync Pending Available");
		alertDialogBuilder.setMessage("Do Sync To get accurate Reports.!");
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
				//if(intentvalue.equals("projectedrevenue"))
				if(isOnline()){
					text_layout_revenue.setVisibility(View.VISIBLE);
					new PastReport().execute(vendor_id,from_date.getText().toString(),to_date.getText().toString(),bike_no_from_adapter);
				}
				else
					alertInternet();
			}						
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	private void alertInternet() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportRevenueBasedOnBikes.this);
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
	/**Check Internet Connection*/	
	public boolean isOnline() {
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) 
				for (int i = 0; i < info.length; i++) 
					if (info[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
		}
		return false;
	}
	/**Takes to sync view, if any pending sync is there*/
	private void goSyncClass() {
		Intent sync=new Intent(getApplicationContext(),SyncClass.class);
		startActivity(sync);
		finish();
	}
	/**Go back to main report navigation page*/
	private void goMainReport() {
		Intent main_report=new Intent(getApplicationContext(),ReportMainClass.class);
		startActivity(main_report);
		finish();
	}
	/**Revenue report getting class, from Helix DB*/
	class PastReport extends AsyncTask<String, Void, String> {		
		private static final String TAG = "Inside Thread Past/Current Report";
		int temp=0;
		ProgressDialog progressDialog;
		protected void onPreExecute() {
			super.onPreExecute();
			// do stuff before posting data
			progressDialog = new ProgressDialog(ReportRevenueBasedOnBikes.this, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
		}
		/**Background operation for data passing*/
		@Override
		protected String doInBackground(String... arg0) {
			String result=null;
			try{
				String postReceiverUrl;
				int i=3;
				// url where the data will be posted     
				postReceiverUrl = "http://phpws.ridio.in/ProjectRevenueAndHireRate";
				Log.v(TAG, "postURL: " + postReceiverUrl);                   
				HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
				HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
				// add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(i);
                nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[i-3]));
                nameValuePairs.add(new BasicNameValuePair("from_date", arg0[i-2]));
                nameValuePairs.add(new BasicNameValuePair("to_date", arg0[i-1]));
                nameValuePairs.add(new BasicNameValuePair("bike_reg_number", arg0[i]));
	                   
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
		/**After receiving the result from Helix DB*/
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
					
				Log.d(result, message);
				//Log.d(j_hotel_id, j_hotel_name+" "+j_hotel_place+" "+j_hotel_date+" "+data.length());
				if(validation==1) {
					bike_reg.clear();
					bike_n.clear();
					total_dy.clear();
					booked_dy.clear();
					days_percent.clear();
					total_amt.clear();
					for(int i=0;i<data.length();i++){
						/**Getting all the json values*/
						String _reg=data.getJSONObject(i).getString("bike_reg_number");
						String b_name=vdb.getVehicleName(_reg);
						//Log.e("It id From DB", b_name);
						//String b_name=data.getJSONObject(i).getString("bike_name");
						String ttl_dy=data.getJSONObject(i).getString("total_days");
						String bked_dy=data.getJSONObject(i).getString("booked_days");
						String percent=data.getJSONObject(i).getString("days_percentage");
						String ttl_amt=data.getJSONObject(i).getString("total_amount");
						bike_reg.add(_reg);
						bike_n.add(b_name);
						total_dy.add(ttl_dy);
						booked_dy.add(bked_dy);
						days_percent.add(percent);
						total_amt.add(ttl_amt);
					}
					callMethodRevenueList();
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	/**Alert Message*/
	private void alertNoDatafromCloudDB() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportRevenueBasedOnBikes.this);
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
	/**Pass values to the list to display result*/
	public void callMethodRevenueList(){	        		
		list_past_report.setAdapter(new ReportCustomAdapterRevenueDisplay(this, bike_reg,bike_n,total_dy,
				booked_dy,days_percent,total_amt));
	}
}
