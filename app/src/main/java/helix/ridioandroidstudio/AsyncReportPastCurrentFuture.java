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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HelixTech-Admin on 3/4/2016.
 */
public class AsyncReportPastCurrentFuture extends AsyncTask<String, Void, String> {
    /**Class Variables*/
    Context context;
    int validation=0;
    JSONObject root;
    private static final String TAG="Inside Thread Past/Current Report";
    int temp=0;
    ProgressDialog progressDialog;
    String intentvalue="";
    int size=0;
    List<String> id=new ArrayList<String>();
    List<String> c_name1=new ArrayList<String>();
    List<String> m_no1=new ArrayList<String>();
    List<String> email1=new ArrayList<String>();
    List<String> b_no1=new ArrayList<String>();
    List<String> day_rate1=new ArrayList<String>();
    List<String> from1=new ArrayList<String>();
    List<String> to1=new ArrayList<String>();
    List<String> days1=new ArrayList<String>();
    List<String> cost1=new ArrayList<String>();
    List<String> v_id1=new ArrayList<String>();
    List<String> book_date1=new ArrayList<String>();
    List<String> rating1=new ArrayList<String>();

    /**Constructor*/
    AsyncReportPastCurrentFuture(ReportPastBooking conx){
        context=conx;
    }

    protected void onPreExecute(){
        super.onPreExecute();
        // do stuff before posting data
        progressDialog=new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("Loading. Please Wait...");
        progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String...arg0){
        String result=null;
        try{
            String postReceiverUrl;
            int i=0;
            intentvalue=arg0[0];
            List<NameValuePair>nameValuePairs=null;
            // url where the data will be posted
            if(intentvalue.equals("pastbooking") || intentvalue.equals("futurebooking")){
                i=4;
                postReceiverUrl="http://phpws.ridio.in/PastBookingReport";
                nameValuePairs=new ArrayList<NameValuePair>(i);
                nameValuePairs.add(new BasicNameValuePair("vendor_id",arg0[1]));
                nameValuePairs.add(new BasicNameValuePair("from_date",arg0[2]));
                nameValuePairs.add(new BasicNameValuePair("to_date",arg0[3]));
                nameValuePairs.add(new BasicNameValuePair("bike_reg_number",arg0[4]));
            }
            else{
                i=3;
                postReceiverUrl="http://phpws.ridio.in/CurrentBookingReport";
                nameValuePairs=new ArrayList<NameValuePair>(i);
                nameValuePairs.add(new BasicNameValuePair("vendor_id",arg0[1]));
                nameValuePairs.add(new BasicNameValuePair("from_date",arg0[2]));
                nameValuePairs.add(new BasicNameValuePair("bike_reg_number",arg0[3]));
            }
            //Log.v(TAG, "postURL: " + postReceiverUrl);
            HttpClient httpClient=new DefaultHttpClient();   // HttpClient
            HttpPost httpPost=new HttpPost(postReceiverUrl);    // post header

            UrlEncodedFormEntity entity=new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
            httpPost.setEntity(entity);

            // execute HTTP post request
            HttpResponse response=httpClient.execute(httpPost);
            result=EntityUtils.toString(response.getEntity());
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        // do stuff after posting data
        progressDialog.dismiss();
        String message=null;
        JSONArray data=null;
        int available_vehicles=0,avail_small=0,avail_medium=0,avail_high=0;
        try{
            Log.d("Inside Post","message");
            root=new JSONObject(result);
            validation=root.getInt("response");
            message=root.getString("message");
            if(message.equals("Failure"))
                ((ReportPastBooking)context).alertNoDatafromCloudDB();
            else
                data=root.getJSONArray("data");
            Log.d(result,message);
            if(validation==1){
                if(intentvalue.equals("pastbooking"))
                    Log.d("Inside Post",message);
                else{
                    //root.getInt("SmallBookedVehicles");
                    avail_small=root.getInt("SmallAvailableVehicles");
                    root.getInt("MediumBookedVehicles");
                    avail_medium=root.getInt("MediumAvailableVehicles");
                    root.getInt("HighBookedVehicles");
                    avail_high=root.getInt("HighAvailableVehicles");
                    available_vehicles=avail_small+avail_medium+avail_high;
                    ((ReportPastBooking)context).avl_veh.setText(""+available_vehicles);
                    ((ReportPastBooking)context).avl_veh_s.setText(""+avail_small);
                    ((ReportPastBooking)context).avl_veh_m.setText(""+avail_medium);
                    ((ReportPastBooking)context).avl_veh_h.setText(""+avail_high);
                }
                id.clear();         c_name1.clear();    m_no1.clear();  email1.clear(); b_no1.clear();
                day_rate1.clear();  from1.clear();      to1.clear();    days1.clear();  cost1.clear();
                v_id1.clear();book_date1.clear();       rating1.clear();
                for(int i=0; i<data.length() ; i++){
                    String _id=data.getJSONObject(i).getString("customer_id");
                    String c_name=data.getJSONObject(i).getString("name");
                    String m_no=data.getJSONObject(i).getString("mobile1");
                    String email=data.getJSONObject(i).getString("email_id");
                    String b_no=data.getJSONObject(i).getString("bike_reg_number");
                    String day_rate=data.getJSONObject(i).getString("per_day_rate");
                    String from=data.getJSONObject(i).getString("from_date");
                    String to=data.getJSONObject(i).getString("to_date");
                    String days=data.getJSONObject(i).getString("number_of_days");
                    String cost=data.getJSONObject(i).getString("total_cost");
                    String v_id=data.getJSONObject(i).getString("vendor_id");
                    String book_date=data.getJSONObject(i).getString("booking_date");
                    //String rating=data.getJSONObject(i).getString("customer_trust_rating");

                    id.add(_id);        c_name1.add(c_name);        m_no1.add(m_no);    email1.add(email);
                    b_no1.add(b_no);    day_rate1.add(day_rate);    from1.add(from);    to1.add(to);
                    days1.add(days);    cost1.add(cost);v_id1.add(v_id);book_date1.add(book_date);
                    //rating1.add(rating);
                    //count++;
                 }
                ((ReportPastBooking)context).callMethodPastCurrentList(id,c_name1,m_no1,email1,b_no1,
                        day_rate1,from1,to1,days1,cost1,book_date1);
                ((ReportPastBooking)context).showViews();
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
