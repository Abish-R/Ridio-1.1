/** Ridio v1.0.1
 * 	Purpose	   : Bookings Hotel List Downloader from cloud
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 18-02-2016 (Hotel list sort)
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.ridioandroidstudio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncNewCustomerHotelGet extends AsyncTask<String, Void, String> {
	/**Class Variables*/
	Context context;
	private static final String TAG = "Inside Thread HotelAdd";
	ProgressDialog progressDialog;
	
	/**Constructor to get context from NewCustomer**/
	public AsyncNewCustomerHotelGet(NewCustomer cus) {
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
       		String postReceiverUrl = "http://phpws.ridio.in/HotelMaster";
            Log.v(TAG, "postURL: " + postReceiverUrl);                   
            HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
            HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header	                    
                    
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);  // add your data
            //   Log.d(hotel_name.getText().toString(), hotel_place.getText().toString());
            nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
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
    	int validation=0,count=0;
    	String message=null;        	
		try {
			Log.d("Inside Post", "message");
			root = new JSONObject(result);
			validation = root.getInt("response");
			message = root.getString("message");
			JSONArray data=root.getJSONArray("data");
			data.length();				
			Log.d(result, message);
			if(validation==1) {
				/** Dummy entry on the arraylist*/
				final HotelSpinner htl_spin_dummy = new HotelSpinner();
				htl_spin_dummy.setHotelName("Please select Hotel");
				((NewCustomer) context).CustomListhotel.add(htl_spin_dummy);

				for(int i=0;i<data.length();i++){
					final HotelSpinner htl_spin = new HotelSpinner();
					String j_hotel_id=data.getJSONObject(i).getString("hotel_id");
					String j_hotel_name=data.getJSONObject(i).getString("hotel_name");
					String j_hotel_place=data.getJSONObject(i).getString("city");
					String j_hotel_date=data.getJSONObject(i).getString("created_date");
					String j_hotel_fav=data.getJSONObject(i).getString("favourite");
					htl_spin.setHotelId(j_hotel_id);
					htl_spin.setHotelName(j_hotel_name);
					htl_spin.setHotelPlace(j_hotel_place);

					((NewCustomer) context).CustomListhotel.add(htl_spin);
					if(j_hotel_fav.equals("true")) {
						count++;
						Collections.swap(((NewCustomer) context).CustomListhotel, i + 1, count);//(htl_spin, 2, 5);
						//Toast.makeText(context, j_hotel_name, Toast.LENGTH_LONG).show();

					}
				}
				/**Pass Value to HotelAdapter in NewCustomer*/
				((NewCustomer) context).setHotelAdapterValues();
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