/** Ridio v1.0.1
 *  Purpose    : MainScreen to navigate all screens 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/
package helix.ridioandroidstudio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreen extends Activity implements OnClickListener{
	/** Private Variables **/
	Ridiologin rl =new Ridiologin();
	Button rate,vehicle,user,report,sync,hotel,return_vehicle,settings;//button1;
	String user_id=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
		rate=(Button)findViewById(R.id.rate);
		vehicle=(Button)findViewById(R.id.vehicle);
		hotel=(Button)findViewById(R.id.hotel);
		user=(Button)findViewById(R.id.user);
		report=(Button)findViewById(R.id.report);
		sync=(Button)findViewById(R.id.sync);
		return_vehicle=(Button)findViewById(R.id.return_vehicle);
		settings=(Button)findViewById(R.id.settings);
		//button1=(Button)findViewById(R.id.button1);
		rate.setOnClickListener(this);
		vehicle.setOnClickListener(this);
		hotel.setOnClickListener(this);
		user.setOnClickListener(this);
		report.setOnClickListener(this);
		sync.setOnClickListener(this);
		return_vehicle.setOnClickListener(this);
		settings.setOnClickListener(this);
		//button1.setOnClickListener(this);
		
		/** get vendor_id from session(shared preference) **/
		SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
        user_id = sp.getString("value", "");
        if(user==null)
        	user_id=rl.user_id;
        Log.d("User in Main Screen: ", user_id);
	}
	
	/** On back button pressed close the current Activity **/
	public void onBackPressed() {
	     super.onBackPressed();
	     finishApplicationCall();
	}
	
	private void finishApplicationCall() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainScreen.this);
		alertDialog.setTitle("Exit");
		alertDialog.setMessage("Surely Exit.?");
        // set positive button: Yes message
		alertDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog,int id) {
				  //Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK
            	  finish();
           	   Log.d("Before Exit ", "Exiting");
               }						
             });
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.d("Before Exit ", "Exiting");
			}
		});
		AlertDialog alertDialog1 = alertDialog.create();
		alertDialog.show();
	}

	/** On button press navigate to consecutive screens **/
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.rate)
			callRateMasterClass();
		if(v.getId()==R.id.vehicle)
			callVehicleMasterClass();
		if(v.getId()==R.id.hotel)
			callHotelMasterClass();
		if(v.getId()==R.id.user)
			callNewUserClass();
		if(v.getId()==R.id.sync)
			callSyncClass();
		if(v.getId()==R.id.report)
			callReportClass();
		if(v.getId()==R.id.return_vehicle)
			callReturnClass();
		if(v.getId()==R.id.settings)
			callPasswordReset();
		//break;
//		case R.id.button1:
//			callSignOrLogin();
//		break;
	}
	
	/** Navigate controller intents **/
	/** Call Rate Master Activity **/
	private void callRateMasterClass(){
		Intent callratemaster=new Intent(this,RateMasterClass.class);
		callratemaster.putExtra("invoker","oncreate");
		startActivity(callratemaster);
	}
	/** Call Vehicle Master Activity **/
	private void callVehicleMasterClass(){
		Intent callratemaster=new Intent(this,VehicleMasterClass.class);
		callratemaster.putExtra("invoker", "oncreate");
		startActivity(callratemaster);
	}
	/** Call Hotel Master Activity **/
	private void callHotelMasterClass() {
		Intent callhotelmaster=new Intent(this,HotelMaster.class);
		startActivity(callhotelmaster);		
	}
	/** Call New Customer Activity **/
	private void callNewUserClass(){
		Intent callratemaster=new Intent(this,NewCustomer.class);
		startActivity(callratemaster);
	}
	/** Call Synchronization Activity **/
	private void callSyncClass(){
		Intent sync=new Intent(this,SyncClass.class);
		startActivity(sync);
		//Toast.makeText(getApplicationContext(), "Synchronization will start", Toast.LENGTH_LONG).show();
		
	}
	/** Call Report Generation Activity **/
	private void callReportClass(){
		//Toast.makeText(getApplicationContext(), "Report will be generated", Toast.LENGTH_LONG).show();
		Intent callratemaster=new Intent(this,ReportMainClass.class);
		startActivity(callratemaster);
	}
	/** Call Return Vehicle Activity **/
	private void callReturnClass(){
		Intent callreturn_vehicle=new Intent(this,ReturnVehicle.class);
		startActivity(callreturn_vehicle);
	}
	/** Call Password Reset Activity **/
	private void callPasswordReset(){
		Intent callreset=new Intent(this,ResetPassword.class);
		startActivity(callreset);
	}
//	private void callSignOrLogin(){
//		Intent logsign=new Intent(this,Ridiologin.class);
//		startActivity(logsign);
//	}
}
