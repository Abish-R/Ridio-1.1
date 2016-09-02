package helix.ridioandroidstudio;

/** Ridio v1.0.1
 * 	Purpose	   : Download or Sync the Bookings for Cloud DB
 *  Created by : Abish
 *  Created Dt : 23-02-2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 * **/

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AsyncSyncDownload extends AsyncTask<String, Void, String> {
    /**Class Variables*/
    Context context;
    String TAG="AsyncSignUpClass",data_format="";
    int validation=0;
    JSONObject root;
    ProgressDialog progressDialog;
    public AsyncSyncDownload(Context contx, String data_form) {/** Edit the URL **/
        //this.activity = activity;
        context = contx;
        data_format=data_form;
    }
    /**Before Sending Data*/
    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
       // if(data_format.equals("Old")) {
            progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage("Loading. Please Wait...");
            progressDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            progressDialog.show();
      //  }
    }
    /**While sending datas to DB*/
    @Override
    protected String doInBackground(String... arg0) {
        String result=null;
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://phpws.ridio.in/SyncDownloadData";/** Edit the url **/
            Log.v(TAG, "postURL: " + postReceiverUrl);
            HttpClient httpClient = new DefaultHttpClient();   // HttpClient
            HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header

            /** our data for sending*/
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //Log.d(uname.getText().toString(), pass.getText().toString());
            nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
            nameValuePairs.add(new BasicNameValuePair("data_format", arg0[1]));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
            httpPost.setEntity(entity);

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
           // sendErrorReport(); /** add the function definition **/
            e.printStackTrace();
        }
        return result;
    }
    /**After Data p  assed, returned datas are below*/
    @Override
    protected void onPostExecute(String result) {
        // do stuff after posting data
        //if(data_format.equals("Old"))
            progressDialog.dismiss();
        String message=null,insert_request=null;
        JSONArray rate;
        JSONArray vehicle;
        JSONArray bookings;
        try {
            Log.d("Inside Post", "message");
            root = new JSONObject(result);
            validation = root.getInt("response");
            message = root.getString("message");

            Log.d(result, "");
            if(validation==1) {
                rate=root.getJSONArray("rates");
                vehicle=root.getJSONArray("vehicles");
                bookings=root.getJSONArray("bookings");
                /**Doing Rate insertion process, from which the datas are downloaded from cloud*/
                for(int i=0;i<rate.length();i++) {
                    String s_rt = rate.getJSONObject(i).getString("small_rate");
                    String m_rt = rate.getJSONObject(i).getString("medium_rate");
                    String h_rt = rate.getJSONObject(i).getString("high_rate");
                    String rt_dt = rate.getJSONObject(i).getString("rate_date");
                    String cr_dt = rate.getJSONObject(i).getString("created_date");
                    String up_st = rate.getJSONObject(i).getString("upload_status");
                    if(data_format.equals("Old"))
                        ((Ridiologin) context).rateAddPasser(s_rt, m_rt, h_rt, rt_dt, cr_dt, up_st);
                    else
                        ((SyncClass) context).rateAddPasser(s_rt, m_rt, h_rt, rt_dt, cr_dt, up_st);
                }
                /**Doing Vehicle insertion process, from which the datas are downloaded from cloud*/
                for (int i = 0; i < vehicle.length();i++) {
                    String clr = vehicle.getJSONObject(i).getString("vehicle_color");
                    String cat = vehicle.getJSONObject(i).getString("v_category");
                    String v_no = vehicle.getJSONObject(i).getString("vehicle_number");
                    String bk_nm = vehicle.getJSONObject(i).getString("vehicle_model_name");
                    String s_dt = vehicle.getJSONObject(i).getString("last_service_date");
                    String p_dt = vehicle.getJSONObject(i).getString("purchased_date");
                    String rpr_pnd = vehicle.getJSONObject(i).getString("known_repair_issues_pending");
                    String rpr_pst = vehicle.getJSONObject(i).getString("past_repairs_and_issues");
                    String cr_dt = vehicle.getJSONObject(i).getString("created_date");
                    String bk_st = vehicle.getJSONObject(i).getString("vehicle_status");
                    String vr_id = vehicle.getJSONObject(i).getString("vendor_id");
                    String up_st = vehicle.getJSONObject(i).getString("upload_status");
                    if(data_format.equals("Old"))
                        ((Ridiologin) context).vehicleAddPasser(clr, cat, v_no, bk_nm, s_dt,
                            p_dt, rpr_pnd, rpr_pst, cr_dt, bk_st, vr_id, up_st);
                    else
                        ((SyncClass) context).vehicleAddPasser(clr, cat, v_no, bk_nm, s_dt,
                                p_dt, rpr_pnd, rpr_pst, cr_dt, bk_st, vr_id, up_st);
                }
                /**Doing Bookings insertion process, from which the datas are downloaded from cloud*/
                for(int i=0;i<bookings.length();i++) {
                    String n = bookings.getJSONObject(i).getString("name");
                    String m1 = bookings.getJSONObject(i).getString("mobile1");
                    String m2 = bookings.getJSONObject(i).getString("mobile2");
                    String e = bookings.getJSONObject(i).getString("email_id");
                    String h = bookings.getJSONObject(i).getString("hotel_name");
                    String bike_name = bookings.getJSONObject(i).getString("bike_name");
                    String bike_color = bookings.getJSONObject(i).getString("bike_color");
                    String bike_reg_number = bookings.getJSONObject(i).getString("bike_reg_number");
                    String bp = bookings.getJSONObject(i).getString("per_day_rate");
                    String fd = bookings.getJSONObject(i).getString("from_date");
                    String to = bookings.getJSONObject(i).getString("to_date");
                    String td = bookings.getJSONObject(i).getString("number_of_days");
                    String tc = bookings.getJSONObject(i).getString("total_cost");
                    String vendor_id = bookings.getJSONObject(i).getString("vendor_id");
                    String booking_date = bookings.getJSONObject(i).getString("booking_date");
                    String bk_status = bookings.getJSONObject(i).getString("bike_status");
                    String  up_status = bookings.getJSONObject(i).getString("upload_status");
                    if(up_status.equals("N") && bk_status.equals("P"))
                        up_status="UP";

                    /**Data sync from cloud no need to save the ratings, we are getting the ratings
                     * while booking from cloud. So dummy values are passed to save */
                    String ratingfromcloud = "99.0";//bookings.getJSONObject(i).getString("customer_trust_rating");
                    String ratingcommentcloud = "Download";//bookings.getJSONObject(i).getString("customer_id");

                    if(data_format.equals("Old"))
                        ((Ridiologin)context).bookingsDataPasser(n, m1, m2, e, h,
                            bike_name, bike_color, bike_reg_number, bp, fd, to, td, tc, vendor_id,
                            booking_date, ratingfromcloud, bk_status, up_status, ratingcommentcloud);
                    else
                        ((SyncClass)context).bookingsDataPasser(n, m1, m2, e, h,
                                bike_name, bike_color, bike_reg_number, bp, fd, to, td, tc, vendor_id,
                                booking_date, ratingfromcloud, bk_status, up_status, ratingcommentcloud);

                }
                if(data_format.equals("Old"))
                    ((Ridiologin)context).loadMainscreen();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**Alert Messages*/
    private void alertWrongSignUp(String val){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("SignUp Error");
        alertDialogBuilder.setMessage(val);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
