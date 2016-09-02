/** Ridio v1.0.1
 *  Purpose	   : Login Validation
 *  Created by : Abish
 *  Created on : 12/15/2015
 *  Modified on: 27-02-2016 (Two ways sync)
 *  Verified by:
 *  Verified Dt:
 */

package helix.ridioandroidstudio;

import helix.general.CheckInternet;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Ridiologin extends Activity{
	/** Global declarations **/
	VehicleDatabaseHandler vdbh=new VehicleDatabaseHandler(this);
	VehicleNameDatabaseHandler vndbh= new VehicleNameDatabaseHandler(this);
	RateDatabaseHandler rdbh=new RateDatabaseHandler(this);
	NewCustomerDatabaseHandler ncdbh=new NewCustomerDatabaseHandler(this);
	CheckInternet ci=new CheckInternet(this);
	EditText uname,pass;
	private static final String TAG = "Validation";
	private String[] veh_name,veh_category;
	private static String pref_login_status=null;
	private static int validation=0;
	PreferenceManager prfMan;
	JSONObject root = null;
	String user_id=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ridiologin);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		/** Initialization Starts */
		Button btn_login=(Button)findViewById(R.id.btn_login);
		//Button btn_signup=(Button)findViewById(R.id.btn_signup);
		uname=(EditText)findViewById(R.id.uname);
		pass=(EditText)findViewById(R.id.pass);
		/** Initialization Ends */

		btn_login.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				createTable();
				/**Create a row in vehicle master*/
				vdbh.addVehicle(new VehicleGetSetter(0,"","","All Bikes","",
						"","","","","","","","U"));
				if(ci.isOnline()){
					new AsyncGetVehicleNameFromCloud(Ridiologin.this).execute("http://phpws.ridio.in/RUC_VehicleNameMaster");
					//new LoginCheck().execute(uname.getText().toString(), pass.getText().toString());
				}
				else{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Ridiologin.this);
			        alertDialogBuilder.setTitle("Internet Problem");
			        alertDialogBuilder.setMessage("Check Internet and try again.!");
			        // set positive button: Yes message
			        alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
			        	public void onClick(DialogInterface dialog,int id) {
			        	}						
			        });
			        AlertDialog alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
			        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				}
			}
		});
		
//		btn_signup.setOnClickListener(new OnClickListener() {			
//			@Override
//			public void onClick(View v) {
//				Intent signup=new Intent(getApplicationContext(),SignUp.class);
//				startActivity(signup);
//				finish();
//			}
//		});
	}
	/**Create table reference giving*/
	public void createTable() {
	    CreateAllTableWithDBInLocal dbhelper=new CreateAllTableWithDBInLocal(getBaseContext());
	    dbhelper.getWritableDatabase();
		dbhelper.close();
	}
	/** Login Validation Starts **/
	public class LoginCheck extends AsyncTask<String, Void, String> {		
		ProgressDialog progressDialog;
		protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
            progressDialog = new ProgressDialog(Ridiologin.this, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
        }
 
        @Override
        protected String doInBackground(String... arg0) {
        	String result=null;
            	try{
                    // url where the data will be posted                	
                    String postReceiverUrl = "http://phpws.ridio.in/RUC_VendorLogin";
                    Log.v(TAG, "postURL: " + postReceiverUrl);                   
                    HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
                    HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
                    
                    // add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    //Log.d(uname.getText().toString(), pass.getText().toString());
                    nameValuePairs.add(new BasicNameValuePair("user_name", arg0[0]));
                    nameValuePairs.add(new BasicNameValuePair("password", arg0[1]));
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
			try {
				Log.d("Inside Post", "message");
				root = new JSONObject(result);
				validation = root.getInt("response");
				user_id = root.getString("userId");
				message = root.getString("message");
				Log.d(result, user_id);
				//Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_LONG).show();
				if(validation==1) {
					SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
					SharedPreferences.Editor ed=sp.edit();
					ed.putString("value", user_id);
					ed.commit();

					/** Call for rate master, vehicle master, bookings bulk sync*/
					new AsyncSyncDownload(Ridiologin.this,"Old").execute(user_id, "Old");
					//saveSession();
				}
				else{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Ridiologin.this);
					alertDialogBuilder.setTitle("Login Error");
					alertDialogBuilder.setMessage("Username/Password wrong.!");
					// set positive button: Yes message
					alertDialogBuilder.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
						}						
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
					//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
        }
        /** Login Validation Ends **/
        
        /** save the user in session starts here **/
        private void saveSession() {
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().
				putString("username", uname.getText().toString()).apply();
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().
				putString("user_id", user_id).apply();
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().
				putString("ridio_logged", user_id).apply();
			pref_login_status = prfMan.getDefaultSharedPreferences(getApplicationContext()).
				getString("ridio_logged",pref_login_status);
			Log.d("ridio_logged :", pref_login_status);
		}
		/** save the user in session ends here **/
	}
	
	/** Checks Internet Connection*/
//	public boolean isOnline() {
//		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//        }
//        return false;
//	}
	/**Add rows in the vehicle master table, save Vehicle name which got From cloud DB */
	public void saveStringToDB(String[] category,String[] name) {
		veh_name=name;
		veh_category=category;
		for(int i=0;i<veh_name.length;i++)
			vndbh.addContact(new VehicleNameGetSet(veh_category[i],veh_name[i]));
		new LoginCheck().execute(uname.getText().toString(), pass.getText().toString());
		}

	/** Load MainScreen*/
	public void loadMainscreen(){
		finish();
		Intent intent= new Intent(getApplicationContext(), MainScreen.class);
		startActivity(intent);

	}

	/**Passes all the datas to rate master table for insert*/
	public void rateAddPasser(String s_rt,String m_rt, String h_rt,String rt_dt,String cr_dt, String up_st) {
		//RateMasterClass rm=new RateMasterClass();
		int rt_cnt= rdbh.getRateCount();
		rdbh.addContact(new RateGetSetter(rt_cnt + 1, s_rt, m_rt, h_rt, rt_dt, cr_dt, up_st));
	}

	/**Passes the datas to Vehicle master table for insert*/
	public void vehicleAddPasser(String clr,String cat,String v_no,String bk_nm,String s_dt,
								 String p_dt,String rpr_pnd,String rpr_pst,
								 String cr_dt,String bk_st,String vr_id,String up_st){
		//VehicleMasterClass vm = new VehicleMasterClass();
		if(rpr_pnd.length()<1)
			rpr_pnd="Nil";
		if(rpr_pst.length()<1)
			rpr_pst="Nil";
		int state=vdbh.addVehicle(new VehicleGetSetter(clr,cat,v_no,bk_nm,s_dt,p_dt,rpr_pnd,rpr_pst,
				cr_dt,bk_st,vr_id,up_st));
		//return state;
	}

	/**Passes all the datas which recieved from cloud to booking(NewCustomer) table for insert*/
	public void bookingsDataPasser(String n,String m1,String m2,String e,String h,
								   String bike_name,String bike_color,String bike_reg_number,
								   String bp,String fd,String to,String td,String tc,
								   String vendor_id, String booking_date, String ratingfromcloud,
								   String bk_status,String up_status,String ratingcommentcloud){
		//NewCustomer nc = new NewCustomer();
		/**Save Bookings in Local DB **/
		ncdbh.addContact(new NewCustomerGetSetter(n, m1, m2, e, h, bike_name, bike_color,
				bike_reg_number, bp, fd, to, td, tc, vendor_id, booking_date, ratingfromcloud,
				bk_status, up_status,ratingcommentcloud));
		/** Update Vehicles status in Local**/
		vdbh.updateContact(new VehicleGetSetter(bike_reg_number, "P"));
	}

}
