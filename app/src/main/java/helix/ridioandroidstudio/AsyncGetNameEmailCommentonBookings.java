/** Ridio v1.0.1
 * 	Purpose	   : Get Customer Details from HelixDB while Booking
 *  Created by : Abish 
 *  Created Dt : old_file
 *  Modified on: 25-02-2016(Rating and rating comments get)
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

public class AsyncGetNameEmailCommentonBookings extends AsyncTask<String, Void, String> {
	
	/**Class Variables*/
	Context context;
	String TAG="Get NameEmailComment onBookings";
	int validation=0;
	JSONObject root;
	ProgressDialog progressDialog;
		
		/**Constructor context pass*/
		public AsyncGetNameEmailCommentonBookings(NewCustomer cus) {
			//this.activity = activity;
            context = cus;
		}
		
		/**do stuff before posting data*/
		protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
        }
 
		/**Passing values to db*/
        @Override
        protected String doInBackground(String... arg0) {
        	String result=null;
            	try{
                    // url where the data will be posted                	
                    String postReceiverUrl = "http://phpws.ridio.in/RUC_GetParticularCustomerDetails";
                    Log.v(TAG, "postURL: " + postReceiverUrl);                   
                    HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
                    HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
                    
                    // add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    //Log.d(uname.getText().toString(), pass.getText().toString());
                    nameValuePairs.add(new BasicNameValuePair("mobile1", arg0[0]));
                    nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[1]));
                  	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
                    httpPost.setEntity(entity);
                    Log.d(httpPost.toString(),httpClient.toString());
                    // execute HTTP post request
                    HttpResponse response = httpClient.execute(httpPost);
                    result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
 
        /**Recieving values from db*/
        @Override
        protected void onPostExecute(String result) {
            // do stuff after posting data
        	progressDialog.dismiss();
        	String message=null,user_id=null;
        	JSONArray data = null;
			try {
				Log.d("Inside Post", "Success");
				root = new JSONObject(result);
				validation = root.getInt("response");
				message = root.getString("message");
				Log.d(result, message);
				if(validation==1) {
					data=root.getJSONArray("data");
					String name=data.getJSONObject(0).getString("name");
					String mobile1=data.getJSONObject(0).getString("mobile1");
					String email_id=data.getJSONObject(0).getString("email_id");
					String rating=data.getJSONObject(0).getString("rating");
					if(rating.length()<1)
						rating="99";
					float ratin=Float.parseFloat(rating);
					String comments=data.getJSONObject(0).getString("feedback");
					if(comments.length()<1)
						comments="Null";
					((NewCustomer) context).setTextInField(name, mobile1, email_id,ratin,comments);
				}
				else{
					/**Pass Recieved Values to NewCustomer*/
					String mobile1=root.getString("data");
					((NewCustomer) context).setTextInField("Fail",mobile1,"",99,"");
				}
				}
			catch (JSONException e) {
				e.printStackTrace();
			}
        }
}