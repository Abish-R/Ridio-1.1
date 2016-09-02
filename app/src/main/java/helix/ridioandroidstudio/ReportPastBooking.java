/** Ridio v1.0.1
 * 	Purpose	   : Handles Past and Current booking reports 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 26-01-2016
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

import helix.general.AlertMessages;
import helix.general.CheckInternet;
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
import android.content.res.Resources;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReportPastBooking extends Activity implements OnClickListener {
	/**Class Global Declarations*/
	BikeSpinnerAdapter adapter;
	VehicleDatabaseHandler vdb=new VehicleDatabaseHandler(this);
	NewCustomerDatabaseHandler ncdbh= new NewCustomerDatabaseHandler(this);
	AlertMessages am=new AlertMessages(this);
	CheckInternet ci=new CheckInternet(this);
	List<Integer> c_id=new ArrayList<Integer>();
	
	//TextView cid,nm,mob,eml,bk_no,dy_rt,frm,to,dy,cst,vid,book_dt,rting,
	TextView heading,avl_veh,avl_veh_s,avl_veh_m,avl_veh_h;
	LinearLayout text_layout,entry_layout,current_spl_layout;
	EditText from_date,to_date;
	Button go;	
	ImageView down_arrow;
	ImageButton img_from,img_to;
	ListView list_past_report;
	Spinner bike_spin;
	String bike_no_from_adapter,what_bike,vendor_id,spvalue,intentvalue,today;
	public  ArrayList<BikeSpinner> CustomListViewValuesArr = new ArrayList<BikeSpinner>();
	
	SimpleDateFormat df;
    Date oldDate,newDate,oldDate1,newDate1;
    private int day,month,year,hour,minute;
    Calendar c = Calendar.getInstance();
    
    JSONObject root;
    int validation=0,size=0;
    List<String> id=new ArrayList<String>();
	List<String> c_name1=new ArrayList<String>();
	List<String> m_no1=new ArrayList<String>();
	List<String> email1=new ArrayList<String>();
	List<String> b_no1=new ArrayList<String>();
	List<String> day_rate1=new ArrayList<String>();
	List<String> from1=new ArrayList<String>();
	List<String> to1=new ArrayList<String>();
	List<String> days1=new ArrayList<String>();
	List<String> cost1=new ArrayList<String>();
	List<String> v_id1=new ArrayList<String>();
	List<String> book_date1=new ArrayList<String>();
	List<String> rating1=new ArrayList<String>();
    
	List<String> bike_reg=new ArrayList<String>();
	List<String> bike_n=new ArrayList<String>();
	List<String> total_dy=new ArrayList<String>();
	List<String> booked_dy=new ArrayList<String>();
	List<String> days_percent=new ArrayList<String>();
	List<String> total_amt=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_past_current_future);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initialize();
		df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		/**Getting vendor id*/
		SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
        spvalue = sp.getString("value", "");
        vendor_id=spvalue;
        Log.e("UserId in New Customer", spvalue);
        
        /**Get Intent value*/
		intentvalue = getIntent().getExtras().getString("intent_is");
		if(intentvalue.equals("pastbooking")){// || intentvalue.equals("projectedrevenue")){
			defaultDateCal();
			setListData();
			setBikeAdapterValues();
		}
		else{
			hideViews();
			int temp=bringLocalTableCustomerCount();
			if(temp>1)
				alertWarning();				
			else{
				today=df.format(c.getTime());
				Log.d("Date Check", df.format(c.getTime()));
				if(ci.isOnline()){
					showViews();
					new AsyncReportPastCurrentFuture(this).execute(intentvalue,vendor_id,today,"All Bikes");
				}
				else
					alertInternet();
			}
		}
		go.setOnClickListener(this);
		img_from.setOnClickListener(this);
		img_to.setOnClickListener(this);
	}
	/**Get the not uploaded datas count*/
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
	
	/**Hide some views when current booking selected*/
	private void hideViews() {
		
		heading.setText("Current Bookings");
		entry_layout.setVisibility(View.GONE);
		current_spl_layout.setVisibility(View.VISIBLE);
//		from_date.setVisibility(View.GONE);
//		to_date.setVisibility(View.GONE);
//		img_from.setVisibility(View.GONE);
//		img_to.setVisibility(View.GONE);
//		//list_past_report.setVisibility(View.GONE);
//		bike_spin.setVisibility(View.GONE);
//		go.setVisibility(View.GONE);
//		down_arrow.setVisibility(View.GONE);
	}
	/**shows all the textview for current booking*/
	public void showViews() {
		text_layout.setVisibility(View.VISIBLE);
//		cid.setVisibility(View.VISIBLE);
//		nm.setVisibility(View.VISIBLE);
//		mob.setVisibility(View.VISIBLE);
//		eml.setVisibility(View.VISIBLE);
//		bk_no.setVisibility(View.VISIBLE);
//		dy_rt.setVisibility(View.VISIBLE);
//		frm.setVisibility(View.VISIBLE);
//		to.setVisibility(View.VISIBLE);
//		dy.setVisibility(View.VISIBLE);
//		cst.setVisibility(View.VISIBLE);
//		vid.setVisibility(View.VISIBLE);
//		book_dt.setVisibility(View.VISIBLE);
//		rting.setVisibility(View.VISIBLE);
	}
	/**Initialize all the views*/
	private void initialize() {
		from_date=(EditText)findViewById(R.id.from_date);
		to_date=(EditText)findViewById(R.id.to_date);
		go=(Button)findViewById(R.id.go);
		down_arrow=(ImageView)findViewById(R.id.down_arrow);
		img_from=(ImageButton)findViewById(R.id.img_from);
		img_to=(ImageButton)findViewById(R.id.img_to);
		list_past_report=(ListView)findViewById(R.id.list_past_report);
		bike_spin=(Spinner)findViewById(R.id.bike_spin);
				
		current_spl_layout=(LinearLayout)findViewById(R.id.current_spl_layout);
		text_layout=(LinearLayout)findViewById(R.id.text_layout);
		entry_layout=(LinearLayout)findViewById(R.id.entry_layout);
		heading=(TextView)findViewById(R.id.heading);
		avl_veh=(TextView)findViewById(R.id.avl_veh);
		avl_veh_s=(TextView)findViewById(R.id.avl_veh_s);
		avl_veh_m=(TextView)findViewById(R.id.avl_veh_m);
		avl_veh_h=(TextView)findViewById(R.id.avl_veh_h);
//		cid=(TextView)findViewById(R.id.cid);
//		nm=(TextView)findViewById(R.id.nm);
//		mob=(TextView)findViewById(R.id.mob);
//		eml=(TextView)findViewById(R.id.eml);
//		bk_no=(TextView)findViewById(R.id.bk_no);
//		dy_rt=(TextView)findViewById(R.id.dy_rt);
//		frm=(TextView)findViewById(R.id.frm);
//		to=(TextView)findViewById(R.id.to);
//		dy=(TextView)findViewById(R.id.dy);
//		cst=(TextView)findViewById(R.id.cst);
//		vid=(TextView)findViewById(R.id.vid);
//		book_dt=(TextView)findViewById(R.id.book_dt);
//		rting=(TextView)findViewById(R.id.rting);
	}
	/**Get and set default date*/
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
	/**Find the date difference*/
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
						alertWarning();
				else{
					if(ci.isOnline()){
						showViews();
						new AsyncReportPastCurrentFuture(this).execute(intentvalue,vendor_id,from_date.getText().toString(),
								to_date.getText().toString(),bike_no_from_adapter);
						Log.d("Date Check", from_date.getText().toString()+" "+to_date.getText().toString());
					}
					else
						alertInternet();
				}
	        }
//		        else
//		        	Toast.makeText(getApplicationContext(), "Check Date", Toast.LENGTH_LONG).show();
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
	/**Get all bikes of vendor and displayed as list*/
	private void setBikeAdapterValues() {
		 Resources res = getResources();
		 adapter = new BikeSpinnerAdapter(ReportPastBooking.this, R.layout.bike_spinner, CustomListViewValuesArr,res);
		 bike_spin.setAdapter(adapter);
		 bike_spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			 @Override
			 public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
				 // Get selected row data to show on screen
				 bike_no_from_adapter   = ((TextView) v.findViewById(R.id.textbike_numbr)).getText().toString();
				 what_bike   = ((TextView) v.findViewById(R.id.textbike)).getText().toString();
				 String OutputMsg = "Selected : \n\n"+bike_no_from_adapter;
				 //Toast.makeText(getApplicationContext(),OutputMsg+" "+" Position: "+position, Toast.LENGTH_LONG).show();
			 }
			 @Override
			 public void onNothingSelected(AdapterView<?> parentView) {
			 }
		 });
	}
	public void setListData(){
		List<VehicleGetSetter> contacts = vdb.getAllContacts();
		for (VehicleGetSetter cn : contacts) {
			final BikeSpinner sched = new BikeSpinner();
			                 
			/******* Firstly take data in model object ******/
			sched.setBikeName(cn.getNumber());
			//sched.setImage("image"+i);
			sched.setBikeNumber(cn.getModelName());
				                
			/** Take Model Object in ArrayList*/
			CustomListViewValuesArr.add(sched);
		}
	}
	/**Alert messages*/
	private void alertMessageWrongFromDate() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportPastBooking.this);
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
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportPastBooking.this);
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
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportPastBooking.this);
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
				alertDecider();
			}						
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	private void alertInternet() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportPastBooking.this);
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
//	/**Check Internet*/
//	public boolean isOnline() {
//		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//		if (connectivity != null){
//			NetworkInfo[] info = connectivity.getAllNetworkInfo();
//			if (info != null)
//				for (int i = 0; i < info.length; i++)
//					if (info[i].getState() == NetworkInfo.State.CONNECTED)    {
//						return true;
//					}
//		}
//		return false;
//	}

	/**Decider function to produce report*/
	private void alertDecider(){
		if(ci.isOnline()) {
			if (intentvalue.equals("pastbooking"))// || intentvalue.equals("projectedrevenue"))
				new AsyncReportPastCurrentFuture(this).execute(intentvalue,vendor_id, from_date.getText().toString(),
						to_date.getText().toString(), bike_no_from_adapter);
			else
				new AsyncReportPastCurrentFuture(this).execute(intentvalue,vendor_id, df.format(c.getTime()), "All Bikes");
		}
		else
			alertInternet();
	}
	/**Go to sync class*/
	private void goSyncClass() {
		Intent sync=new Intent(getApplicationContext(),SyncClass.class);
       	startActivity(sync);
       	finish();
	}
	/**Go to main report class*/
	private void goMainReport() {
		Intent main_report=new Intent(getApplicationContext(),ReportMainClass.class);
		startActivity(main_report);
		finish();
	}
	/**This class bring past and current reports*/
	class PastReport extends AsyncTask<String, Void, String> {		
		private static final String TAG = "Inside Thread Past/Current Report";
		int temp=0;
		ProgressDialog progressDialog;
		protected void onPreExecute() {
			super.onPreExecute();
			// do stuff before posting data
			progressDialog = new ProgressDialog(ReportPastBooking.this, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
		}
	 
		@Override
		protected String doInBackground(String... arg0) {
			String result=null;
			try{
				String postReceiverUrl;
				int i=0;
				// url where the data will be posted     
				if(intentvalue.equals("pastbooking") || intentvalue.equals("futurebooking")){
					i=3;temp=1;
					postReceiverUrl = "http://phpws.ridio.in/PastBookingReport";
				}
//         		else if(intentvalue.equals("projectedrevenue")){
//	      			i=3;temp=2;
//	      			postReceiverUrl = "http://phpws.ridio.in/ProjectRevenueAndHireRate";
//         		}
				else{
					i=2;temp=0;
					postReceiverUrl = "http://phpws.ridio.in/CurrentBookingReport";
				}
				//Log.v(TAG, "postURL: " + postReceiverUrl);
				HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
				HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
	                    
				// add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(i);
				//   Log.d(hotel_name.getText().toString(), hotel_place.getText().toString());
				if(i==3 && temp==1){
					nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
					nameValuePairs.add(new BasicNameValuePair("from_date", arg0[1]));
					nameValuePairs.add(new BasicNameValuePair("to_date", arg0[2]));
					nameValuePairs.add(new BasicNameValuePair("bike_reg_number", arg0[3]));
				}
//		        else if(i==3 && temp==2){
//	                 nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[i-3]));
//	                 nameValuePairs.add(new BasicNameValuePair("from_date", arg0[i-2]));
//	                 nameValuePairs.add(new BasicNameValuePair("to_date", arg0[i-1]));
//	                 nameValuePairs.add(new BasicNameValuePair("bike_reg_number", arg0[i]));
//	            }
				else{
					nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
					nameValuePairs.add(new BasicNameValuePair("from_date", arg0[1]));
					nameValuePairs.add(new BasicNameValuePair("bike_reg_number", arg0[2]));
				}
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
			int available_vehicles=0,avail_small=0,avail_medium=0,avail_high=0;
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
					if(intentvalue.equals("pastbooking"))
						Log.d("Inside Post", message);
					else{
						//root.getInt("SmallBookedVehicles");
						avail_small=root.getInt("SmallAvailableVehicles");
						root.getInt("MediumBookedVehicles");
						avail_medium=root.getInt("MediumAvailableVehicles");
						root.getInt("HighBookedVehicles");
						avail_high=root.getInt("HighAvailableVehicles");
						available_vehicles=avail_small+avail_medium+avail_high;
						avl_veh.setText(""+available_vehicles);
						avl_veh_s.setText(""+avail_small);
						avl_veh_m.setText(""+avail_medium);
						avl_veh_h.setText(""+avail_high);
					}
					id.clear();		c_name1.clear();	m_no1.clear();
					email1.clear();	b_no1.clear();		day_rate1.clear();
					from1.clear();	to1.clear();		days1.clear();
					cost1.clear();	v_id1.clear();		book_date1.clear();
					rating1.clear();
					for(int i=0;i<data.length();i++){
						String _id=data.getJSONObject(i).getString("customer_id");
						String c_name=data.getJSONObject(i).getString("name");
						String m_no=data.getJSONObject(i).getString("mobile1");
						String email=data.getJSONObject(i).getString("email_id");
						String b_no=data.getJSONObject(i).getString("bike_reg_number");
						String day_rate=data.getJSONObject(i).getString("per_day_rate");
						String from=data.getJSONObject(i).getString("from_date");
						String to=data.getJSONObject(i).getString("to_date");
						String days=data.getJSONObject(i).getString("number_of_days");
						String cost=data.getJSONObject(i).getString("total_cost");
						String v_id=data.getJSONObject(i).getString("vendor_id");
						String book_date=data.getJSONObject(i).getString("booking_date");
						//String rating=data.getJSONObject(i).getString("customer_trust_rating");
							
						id.add(_id);		c_name1.add(c_name);	m_no1.add(m_no);							
						email1.add(email);	b_no1.add(b_no);		day_rate1.add(day_rate);							
						from1.add(from);	to1.add(to);			days1.add(days);						
						cost1.add(cost);	v_id1.add(v_id);		book_date1.add(book_date);
						//rating1.add(rating);
						//count++;
					}
					//callMethodPastCurrentList();
					showViews();
				}
//				else
//					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	/**Alert messages*/
	public void alertNoDatafromCloudDB() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportPastBooking.this);
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
	/**Set Report in a view*/
	public void callMethodPastCurrentList(List<String> id,List<String> c_name1,List<String> m_no1,
							List<String> email1,List<String> b_no1,List<String> day_rate1,List<String> from1,
							List<String> to1,List<String> days1,List<String> cost1,List<String> book_date1){
		list_past_report.setAdapter(new ReportCustomAdapterPastCurrent(this, id,c_name1,m_no1,email1,b_no1,
				day_rate1,from1,to1,days1,cost1,v_id1,book_date1));
	}
}
