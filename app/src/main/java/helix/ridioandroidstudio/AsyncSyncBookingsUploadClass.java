package helix.ridioandroidstudio;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;

/** Ridio v1.0.1
 *  Purpose	   : Sync Support class for sending booking details to cloud
 *  Created by : Abish
 *  Created Dt : 17-02-2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 * **/
public class AsyncSyncBookingsUploadClass extends AsyncTask<String, Void, String> {
    Context context;
    ProgressDialog progressDialog;
    int size_booking=0;
    public AsyncSyncBookingsUploadClass(SyncClass syncClass) {
        context=syncClass;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
    }
    /**Passing Datas*/
    @Override
    protected String doInBackground(String... arg0) {
        String result=null;
        try{
            /** url where the data will be posted*/
            String postReceiverUrl = "http://phpws.ridio.in/NewCustomerSignUp";
            Log.v("URL Is: ", "postURL: " + postReceiverUrl);
            HttpClient httpClient = new DefaultHttpClient();   // HttpClient
            HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header

            /** add your data*/
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(19);
            //Log.d(uname.getText().toString(), pass.getText().toString());
            size_booking = Integer.parseInt(arg0[0]);
            nameValuePairs.add(new BasicNameValuePair("name", arg0[1]));
            nameValuePairs.add(new BasicNameValuePair("mobile1", arg0[2]));
            nameValuePairs.add(new BasicNameValuePair("mobile2", arg0[3]));
            nameValuePairs.add(new BasicNameValuePair("email_id", arg0[4]));
            nameValuePairs.add(new BasicNameValuePair("hotel_name", arg0[5]));

            nameValuePairs.add(new BasicNameValuePair("bike_name", arg0[6]));
            nameValuePairs.add(new BasicNameValuePair("bike_color", arg0[7]));
            nameValuePairs.add(new BasicNameValuePair("bike_reg_number", arg0[8]));
            nameValuePairs.add(new BasicNameValuePair("per_day_rate", arg0[9]));
            nameValuePairs.add(new BasicNameValuePair("from_date", arg0[10]));
            nameValuePairs.add(new BasicNameValuePair("to_date", arg0[11]));
            nameValuePairs.add(new BasicNameValuePair("number_of_days", arg0[12]));
            nameValuePairs.add(new BasicNameValuePair("total_cost", arg0[13]));
            nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[14]));
            nameValuePairs.add(new BasicNameValuePair("booking_date", arg0[15]));
            nameValuePairs.add(new BasicNameValuePair("customer_trust_rating", arg0[16]));
            nameValuePairs.add(new BasicNameValuePair("bike_status", arg0[17]));
            nameValuePairs.add(new BasicNameValuePair("upload_status", arg0[18]));
            nameValuePairs.add(new BasicNameValuePair("feedback", arg0[19]));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
            httpPost.setEntity(entity);
            Log.d("Parameters :", "http://phpws.ridio.in/NewCustomerSignUp"+"/name/"+arg0[1]+"/mobile1/"+arg0[2]+
                    "/mobile2/"+arg0[3]+"/email_id/"+arg0[4]+"/hotel_name/"+arg0[5]+
                    "/bike_name/"+arg0[6]+"/bike_color/"+arg0[7]+"/bike_reg_number/"+arg0[8]+
                    "/per_day_rate/"+arg0[9]+"/from_date/"+arg0[10]+"/to_date/"+arg0[11]+
                    "/number_of_days/"+arg0[12]+"/total_cost/"+arg0[13]+"/vendor_id/"+
                    arg0[14]+"/booking_date/"+arg0[15]+"/customer_trust_rating/"+arg0[16]+
                    "/bike_status/"+arg0[17]+"/upload_status/"+arg0[18]+"/feedback/"+arg0[19]);
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
        JSONObject root;
        int validation;
        String message=null, mobile=null, up_state=null,user_id=null;
        try {
            Log.d("Inside Post", "message");
            root = new JSONObject(result);
            validation = root.getInt("response");
            user_id = root.getString("CustomerID");
            message = root.getString("CustomerName");
            message+=root.getString("MobileNumber");
            mobile=root.getString("MobileNumber");
            up_state=root.getString("UploadStatus");
            Log.d(result, user_id);
            if(validation==1) {
                Log.e(message, up_state+root.getString("Emailid"));
                updateUploadStatus(mobile, up_state);
                ((SyncClass)context).uploadStatus(size_booking,"booking");
            }
//				else
//					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**Update status to the local table about the upload status*/
    private void updateUploadStatus(String mob,String ste) {
        ((SyncClass)context).ncdbh.updateUploadStatus(new NewCustomerGetSetter(mob,ste));
    }
}

