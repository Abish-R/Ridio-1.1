	/** Ridio v1.0.1
	 * 	Purpose	   : Bookings Hotel List Downloader
	 *  Created by : Abish 
	 *  Created Dt : 26-01-2016
	 *  Modified on: 28-01-2016
	 *  Verified by: Srinivas
	 *  Verified Dt: 13-01-2016
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
	import android.content.Intent;
	import android.os.AsyncTask;
	import android.util.Log;

	public class AsyncHotelFavUpdateClass extends AsyncTask<String, Void, String> {
		/**Class Variables*/
		Context context;
		private static final String TAG = "Inside Thread HotelAdd";
		ProgressDialog progressDialog;
		String hotelLengthSuccess="";

		/**Constructor to get context from NewCustomer**/
		public AsyncHotelFavUpdateClass(HotelMaster cus) {
			//this.activity = activity;
	        context = cus;
		}

		/**Before Getting data*/
		protected void onPreExecute() {
			super.onPreExecute();
	        progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
	    }	
		
		/**Asking for data*/
	    @Override
	    protected String doInBackground(String... arg0) {
	    	String result=null;
	       	try{
	            // url where the data will be posted                	
	       		String postReceiverUrl = "http://phpws.ridio.in/RUC_VendorSelectedHotels";
	            Log.v(TAG, "postURL: " + postReceiverUrl);                   
	            HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
	            HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header	                    
	                    
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);  // add your data
	            //   Log.d(hotel_name.getText().toString(), hotel_place.getText().toString());
	            nameValuePairs.add(new BasicNameValuePair("hotel_id", arg0[0]));
	            nameValuePairs.add(new BasicNameValuePair("hotel_name", arg0[1]));
	            nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[2]));
	            nameValuePairs.add(new BasicNameValuePair("favourite", arg0[3]));
	          	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
	            httpPost.setEntity(entity);	                                   
	                    
	            HttpResponse response = httpClient.execute(httpPost);  // execute HTTP post request
	            result = EntityUtils.toString(response.getEntity());
	        } catch (Exception e) {
	            e.printStackTrace();
	        	}
	        return result;
	    }
	 
	    /**After Getting data*/
	    @Override
	    protected void onPostExecute(String result) {
	        // do stuff after posting data
	    	progressDialog.dismiss();
	    	JSONObject root;
	    	int validation=0;
	    	String message=null;        	
			try {
				root = new JSONObject(result);
				validation = root.getInt("response");
				message = root.getString("message");
				Log.d(result, message);
				if(validation==1) {
					Log.d(result, message);					
				}
				else
					Log.d(result, message);
					//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	}