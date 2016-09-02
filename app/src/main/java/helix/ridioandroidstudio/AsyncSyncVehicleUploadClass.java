package helix.ridioandroidstudio;

/** Ridio v1.0.1
 * 	Purpose	   : Vehicle Master Class Sync to Upload
 *  Created by : Abish
 *  Created Dt : 18-02-2016
 *  Modified on: 20-02-2016 (Added Created date and modified for sync)
 *  Verified by:
 *  Verified Dt:
 * **/

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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**Class which handles the sync with DB*/
public class AsyncSyncVehicleUploadClass extends AsyncTask<String, Void, String> {
	Context context;
	ProgressDialog progressDialog;
	int size_booking=0;
	public AsyncSyncVehicleUploadClass(SyncClass syncClass) {
		context=syncClass;
	}
	protected void onPreExecute() {
		super.onPreExecute();
	}
	/**Passing Datas*/
	@Override
	protected String doInBackground(String... arg0) {
		String result=null;
		try{
			/** url where the data will be posted*/                	
			String postReceiverUrl = "http://phpws.ridio.in/RUC_VehicleMaster";
			Log.v("Inside Sync Vehicle", "postURL: " + postReceiverUrl);                   
			HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
			HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
			
			/** add your data*/
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(12);
			//Log.d(uname.getText().toString(), pass.getText().toString());
			nameValuePairs.add(new BasicNameValuePair("vehicle_number", arg0[0]));
			nameValuePairs.add(new BasicNameValuePair("vehicle_color", arg0[1]));
			nameValuePairs.add(new BasicNameValuePair("v_category", arg0[2]));
			nameValuePairs.add(new BasicNameValuePair("vehicle_model_name", arg0[3]));
			nameValuePairs.add(new BasicNameValuePair("last_service_date", arg0[4]));
			nameValuePairs.add(new BasicNameValuePair("purchased_date", arg0[5]));
			nameValuePairs.add(new BasicNameValuePair("known_repair_issues_pending", arg0[6]));
			nameValuePairs.add(new BasicNameValuePair("past_repairs_and_issues", arg0[7]));
			nameValuePairs.add(new BasicNameValuePair("created_date", arg0[8]));
			nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[9]));
			nameValuePairs.add(new BasicNameValuePair("upload_status", arg0[10]));
			nameValuePairs.add(new BasicNameValuePair("vehicle_status", arg0[11]));
			size_booking= Integer.parseInt(arg0[12]);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
			httpPost.setEntity(entity);

			  Log.d("URL Is", httpPost+"");
			/** execute HTTP post request*/
			HttpResponse response = httpClient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**Response after the data is send*/
	@Override
	protected void onPostExecute(String result) {
		// do stuff after posting data
		//progressDialog.dismiss();
		String message="",bike_no="",up_state="";
		int validation=0;
		JSONObject root;
		try {
			Log.d("Inside Post", "message");
			root = new JSONObject(result);
			validation = root.getInt("response");
			message = root.getString("message");			
			bike_no=root.getString("vehicle_number");
			up_state=root.getString("upload_status");
			if(validation==1) {
				Log.e(message, up_state);
				((SyncClass) context).updateBikeUploadStatus(bike_no, "U");
				((SyncClass)context).uploadStatus(size_booking,"vehicle");
			}
//				else
//					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}	
}