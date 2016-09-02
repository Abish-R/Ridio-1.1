/** Ridio v1.0.1
 * 	Purpose	   : Get Vehicles of Vendor from Helix DB to Save Locally 
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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncGetVehicleNameFromCloud extends AsyncTask<String, Void, String> {
	/**Class Variables*/
	Context context,context1;
	String TAG="Vehicle Name List";
	int validation=0;
	JSONObject root;
	ProgressDialog progressDialog;
	
	/**Constructor invoked from Login Page*/
	public AsyncGetVehicleNameFromCloud(Ridiologin login) {
		context1 = login;
	}
	/**Before sending or requesting values*/
	protected void onPreExecute() {
		super.onPreExecute();
		// do stuff before posting data
		progressDialog = new ProgressDialog(context1, AlertDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Loading. Please Wait...");
		progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		progressDialog.show();
	}
	/**Requesting Vehicles*/
	@Override
	protected String doInBackground(String... arg0) {
		String result=null;
		try{
			/** url where the data will be posted*/                	
			String postReceiverUrl=arg0[0];// = "http://phpws.ridio.in/RUC_VehicleNameMaster";
			Log.v(TAG, "postURL: " + postReceiverUrl);                   
			HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
			HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header

			/** execute HTTP post request*/
			HttpResponse response = httpClient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**After receiving the data from DB*/
	@Override
	protected void onPostExecute(String result) {
		// do stuff after posting data
		progressDialog.dismiss();
        String message=null,user_id=null;
        JSONArray data = null;
        int datalength=0;
        	
		try {
			Log.d("Inside Post", "Success");
			root = new JSONObject(result);
			validation = root.getInt("response");
			message = root.getString("message");
			Log.d(result, message);
			if(validation==1) {
				data=root.getJSONArray("data");
				datalength=data.length();
				String[] category = new String[datalength];
				String[] name = new String[datalength];
				for(int i =0;i<data.length();i++){
					category[i]=data.getJSONObject(i).getString("vehicle_category_name");
					name[i]=data.getJSONObject(i).getString("vehicle_model_name");
				}
				/**Pass retrieved values to insert on table*/
				((Ridiologin) context1).saveStringToDB(category,name);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
    }
}