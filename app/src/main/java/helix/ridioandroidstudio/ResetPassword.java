/** Ridio v1.0.1
 * 	Purpose	   : Reset password of vendor
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by:
 *  Verified Dt: 
 * **/

package helix.ridioandroidstudio;

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
import org.json.JSONArray;
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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPassword extends Activity {
	/**Global Class variables*/
	TextView tv_id,tfname,tlname,tmobile,temail,tgender,tdob,tcity,tv_id_value,tfname_value,tlname_value,
	tmobile_value,temail_value,tgender_value,tdob_value,tcity_value,topass,tnpass,tnpass1;
	EditText oldpass,newpass,confirmpass;
	Button reset_pass;
	String vendor_id_sp,vendor_id,fname,lname,mobile,email,gender,dob,city,old_password;
	String text = "<font color=#cc0029>*</font>",message=null;
	int check=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_reset);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initializeAll();
		/**Get Vendor ID*/
		SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
		vendor_id_sp = sp.getString("value", "");
        Log.e("UserId in Reset", vendor_id_sp);
		if(isOnline()){
			/**Ask for vendor details from Helix DB*/
			new GetVendorFromDB().execute(vendor_id_sp);
		}
		else
			alertInternet();
		reset_pass.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				check=checkPassword();
				if(check==1){
					if(isOnline())
						new GetVendorFromDB().execute(vendor_id_sp,oldpass.getText().toString(),
								newpass.getText().toString());
					else
						alertInternet();
				}
				else
					alertCheckPassword();
			}			
		});
	}
	/**Initialize All Views*/
	private void initializeAll() {
		tv_id=(TextView)findViewById(R.id.tv_id);
		tfname=(TextView)findViewById(R.id.tfname);
		tlname=(TextView)findViewById(R.id.tlname);
		tmobile=(TextView)findViewById(R.id.tmobile);
		temail=(TextView)findViewById(R.id.temail);
		tgender=(TextView)findViewById(R.id.tgender);
		tdob=(TextView)findViewById(R.id.tdob);
		tcity=(TextView)findViewById(R.id.tcity);
		topass=(TextView)findViewById(R.id.topass);
		tnpass=(TextView)findViewById(R.id.tnpass);
		tnpass1=(TextView)findViewById(R.id.tnpass1);
		tv_id_value=(TextView)findViewById(R.id.tv_id_value);
		tfname_value=(TextView)findViewById(R.id.tfname_value);
		tlname_value=(TextView)findViewById(R.id.tlname_value);
		tmobile_value=(TextView)findViewById(R.id.tmobile_value);
		temail_value=(TextView)findViewById(R.id.temail_value);
		tgender_value=(TextView)findViewById(R.id.tgender_value);
		tdob_value=(TextView)findViewById(R.id.tdob_value);
		tcity_value=(TextView)findViewById(R.id.tcity_value);
		topass.append(Html.fromHtml(text));
		tnpass.append(Html.fromHtml(text));
		tnpass1.append(Html.fromHtml(text));
		oldpass=(EditText)findViewById(R.id.oldpass);
		newpass=(EditText)findViewById(R.id.newpass);
		confirmpass=(EditText)findViewById(R.id.confirmpass);
		reset_pass=(Button)findViewById(R.id.reset_pass);
	}
	/**Password Validation*/
	private int checkPassword(){
		String oldp,newp,confirmp;
		oldp=oldpass.getText().toString();
		newp=newpass.getText().toString();
		confirmp=confirmpass.getText().toString();
		if(newp.contains(" ")){
			alertCheckPassword();
			check=0;
		}
		else if(newp.equals(confirmp) && oldp.length()>2 && newp.length()>2){
			check=1;
		}
		return check;
	}
	/**Alert Messages*/
	private void alertCheckPassword() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResetPassword.this);
        alertDialogBuilder.setTitle("Reset Password");
        alertDialogBuilder.setMessage("Check Old & New are Correct.!");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Reset",new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        	}						
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
	private void alertInternet() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResetPassword.this);
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
	/**Ckeck Internet*/
	public boolean isOnline() {
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)  {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) 
				for (int i = 0; i < info.length; i++) 
					if (info[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
		}
		return false;
	}
	
	/**Get Vendor Details from Helix DB*/
	class GetVendorFromDB extends AsyncTask<String, Void, String> {		
		private static final String TAG = "Inside Thread HotelAdd";
		ProgressDialog progressDialog;
		protected void onPreExecute() {
			super.onPreExecute();
			// do stuff before posting data
            progressDialog = new ProgressDialog(ResetPassword.this, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
        }	
		/**Pass values to PHP in Backgroud*/
        @Override
        protected String doInBackground(String... arg0) {
        	String result=null;
           	try{
           		String postReceiverUrl;
           		int temp=0;
                // url where the data will be posted   
           		if(check==1){
           			temp=2;
           			postReceiverUrl = "http://phpws.ridio.in/RUC_UpdateUserPassword";
           		}
           		else
           			postReceiverUrl = "http://phpws.ridio.in/RUC_VendorSettings";
                Log.v(TAG, "postURL: " + postReceiverUrl);                   
                HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
                HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header	                    
	                    
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(temp);  // add your data
                if(temp==0)
                	nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
                else{
                	nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
                	nameValuePairs.add(new BasicNameValuePair("existing_password", arg0[1]));
                	nameValuePairs.add(new BasicNameValuePair("new_password", arg0[2]));
                }
              	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
                httpPost.setEntity(entity);	                                   
	                    
                HttpResponse response = httpClient.execute(httpPost);  // execute HTTP post request
                result = EntityUtils.toString(response.getEntity());
           	} catch (Exception e) {
           		e.printStackTrace();
           	}
           	return result;
        }
        /**After Response*/
        @Override
        protected void onPostExecute(String result) {
            // do stuff after posting data
        	progressDialog.dismiss();        	 
        	int validation=0;
        	JSONObject root,inside_array;
			try {
				root = new JSONObject(result);
				validation = root.getInt("response");
				message = root.getString("message");
				JSONArray data=root.getJSONArray("data");
				inside_array=data.getJSONObject(0);
				Log.d(result, message);
				if(validation==1) {
					vendor_id=inside_array.getString("vendor_id");
					fname=inside_array.getString("first_name");
					lname=inside_array.getString("last_name");
					mobile=inside_array.getString("mobile");
					email=inside_array.getString("email_id");
					gender=inside_array.getString("gender");
					dob=inside_array.getString("dob");
					city=inside_array.getString("city");
					//old_password=root.getString("old_pass");
					setTextFieldWithJSONText();
					if(check==1)
						alertMessageFromJSON();
				}
				else{
					Log.d("Values", vendor_id+fname+lname+mobile+email+gender+dob+city);
					alertMessageFromJSON();
				}
			}
			catch (JSONException e) {
				Log.e("Error","JSON Retrieval error");
				e.printStackTrace();
			}
        }
        /**Alert Message*/
        private void alertMessageFromJSON() {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResetPassword.this);
	        alertDialogBuilder.setTitle("Change Password");
	        alertDialogBuilder.setMessage(message+" .!");
	        // set positive button: Yes message
	        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog,int id) {
	        		if(message.equals("Password updated successfully")){
	        			finish();
	        			Intent gologin=new Intent(getApplicationContext(),Ridiologin.class);
	        			startActivity(gologin);
	        		}
	        	}						
	        });
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
        }
        /**Set Text Received from Helix DB to textFields*/
        private void setTextFieldWithJSONText() {
			tv_id_value.setText(vendor_id);
			tfname_value.setText(fname);
			tlname_value.setText(lname);
			tmobile_value.setText(mobile);
			temail_value.setText(email);
			tgender_value.setText(gender);
			tdob_value.setText(dob);
			tcity_value.setText(city);
        }
	}
}
