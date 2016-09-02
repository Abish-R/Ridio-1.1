package helix.ridioandroidstudio;

/** Ridio v1.0.1
 * 	Purpose	   : RateMaster Sync Class to upload rates
 *  Created by : Abish
 *  Created Dt : 20-02-2016
 *  Modified on: 20-02-2016
 *  Verified by:
 *  Verified Dt:
 * **/

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

public class AyncSyncRateUploadClass extends AsyncTask<String, Void, String> {
    private Context context;
    int size_booking=0;
    public AyncSyncRateUploadClass(Context con){
        this.context=con;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
//          progressDialog = new ProgressDialog(SyncClass.this, AlertDialog.THEME_HOLO_LIGHT);
//			progressDialog.setMessage("Loading. Please Wait...");
//			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			progressDialog.show();
    }
    /**Passing Datas*/
    @Override
    protected String doInBackground(String... arg0) {
        String result=null;
        try{
            /** url where the data will be posted*/
            String postReceiverUrl = "http://phpws.ridio.in/RUC_RateMaster";
            Log.v("Inside Sync Vehicle", "postURL: " + postReceiverUrl);
            HttpClient httpClient = new DefaultHttpClient();   // HttpClient
            HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header

            /** add your data*/
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            //Log.d(uname.getText().toString(), pass.getText().toString());
            nameValuePairs.add(new BasicNameValuePair("rate_date", arg0[0]));
            nameValuePairs.add(new BasicNameValuePair("small_rate", arg0[1]));
            nameValuePairs.add(new BasicNameValuePair("medium_rate", arg0[2]));
            nameValuePairs.add(new BasicNameValuePair("high_rate", arg0[3]));
            nameValuePairs.add(new BasicNameValuePair("created_date", arg0[4]));
            nameValuePairs.add(new BasicNameValuePair("upload_status", arg0[5]));
            nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[6]));
            size_booking= Integer.parseInt(arg0[7]);

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
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
        String message="",rate_date="",up_state="";
        int validation=0;
        JSONObject root;
        try {
            Log.d("Inside Post", "message");
            root = new JSONObject(result);
            validation = root.getInt("response");
            message = root.getString("message");
            rate_date = root.getString("rate_date");
            up_state=root.getString("upload_status");

            if(validation==1) {
                Log.e(message, up_state);
                ((SyncClass) context).updateRateUploadStatus(rate_date, up_state);
                ((SyncClass)context).uploadStatus(size_booking,"rate");
            }
//				else
//					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
