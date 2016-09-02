/** Ridio v1.0.1
 *  Purpose    : HotelMaster class to get hotel details 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 28-01-2016
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

import helix.general.CheckInternet;
import helix.general.AlertMessages;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HotelMaster extends Activity{
	/** Variables of Hotel Master class **/
	HotelGetSetter hgs;
	HotelCustomAdapter hca;
	CheckInternet chk_intrnt=new CheckInternet(this);
	AlertMessages am = new AlertMessages(this);
	ArrayList<HotelGetSetter> hotelList = new ArrayList<HotelGetSetter>();
	List<String> checkedList = new ArrayList<String>();
	List<String> checkedListUpdate = new ArrayList<String>();
	Button save_hotel;
	LinearLayout list_title_layout;
	TextView h_place,h_name;
	JSONObject root;
	int validation=0,count=0,counter=0;
	ListView list_hotel;
	String vendor_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotel_master);
		save_hotel=(Button)findViewById(R.id.save_hotel);
		h_place=(TextView)findViewById(R.id.h_place);
		h_name=(TextView)findViewById(R.id.h_name);
		list_hotel = (ListView) findViewById(R.id.list_hotel);
		list_title_layout=(LinearLayout)findViewById(R.id.list_title_layout);
		//checkButtonClick();
		
		/**Getting vendor id*/
		SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
		vendor_id = sp.getString("value", "");
        Log.e("UserId in New Customer", vendor_id);
		
		/** Get Hotels from Helix DB */		
		if(chk_intrnt.isOnline()){
			new HotelAdd().execute(vendor_id);
			list_title_layout.setVisibility(View.VISIBLE);
		}
		else{	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HotelMaster.this);
	        alertDialogBuilder.setTitle("Internet Problem");
		    alertDialogBuilder.setMessage("Check Internet and try again.!");
		    // set positive button: Yes message
	        alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog,int id) {
	        	}						
	        });
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
			//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		}
		
		/** Update Table with Favourite list **/
		save_hotel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer responseText = new StringBuffer();
				responseText.append("The following were selected...\n");
				ArrayList<HotelGetSetter> list = hca.hotelList;
				int listLength=list.size();
				for(int i=0;i<listLength;i++){
					HotelGetSetter country = list.get(i);
					if(country.isSelected()){
//						checkedListUpdate.add(country.getID());
//						checkedListUpdate.add(country.getName());
//						checkedListUpdate.add(Boolean.toString(country.isSelected()));
						new AsyncHotelFavUpdateClass(HotelMaster.this).execute(country.getID(),country.getName(),
		  						vendor_id,Boolean.toString(country.isSelected()));
						responseText.append("\n" + country.getName());						
					}
					else{
						for(int j=1;j<checkedList.size();j+=2){
				  			if(checkedList.get(j).toString().equals(country.getName().toString())){	
//				  				checkedListUpdate.add(country.getID());
//				  				checkedListUpdate.add(country.getName());
//				  				checkedListUpdate.add(Boolean.toString(country.isSelected()));
				  				new AsyncHotelFavUpdateClass(HotelMaster.this).execute(country.getID(),country.getName(),
				  						vendor_id,Boolean.toString(country.isSelected()));
				  				responseText.append("\n" + country.getName()+" false");				  				
				  			}
				  			else{
				  				Log.d("Checked List ",checkedList.get(j).toString());
				  			}
				  		 }
					}
//					if(listLength==i)
//						alertMessageShow();
				}
				am.alertClassClasses("Hotel Master", "Favourite Hotels updated..!", "callDashboardFromHotel");
//				Toast.makeText(getApplicationContext(),responseText, Toast.LENGTH_LONG).show();
//				for(int i=0;i<checkedListUpdate.size()/3;i+=3){
//					if(checkedListUpdate.size()==i)
//						temp="success";
//					Log.e("Hotel Async",""+ i);
//				new AsyncHotelFavUpdateClass(HotelMaster.this).execute(checkedListUpdate.get(i),checkedListUpdate.get(i+1),
//  						"1",checkedListUpdate.get(i+2),temp);
//				}
			}
		});
	}
		
//	/** Checking for internet connection **/
//	public boolean isOnline() {
//		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
//                        return true;
//                    }
//        }
//        return false;
//  }

	/** Hotel List gets from Helix DB Starts **/
	class HotelAdd extends AsyncTask<String, Void, String> {		
		private static final String TAG = "Inside Thread HotelAdd";
		ProgressDialog progressDialog;
		/** Loader screen **/
		protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HotelMaster.this, AlertDialog.THEME_HOLO_LIGHT);
			progressDialog.setMessage("Loading. Please Wait...");
			progressDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			progressDialog.show();
        }
 
        @Override
        protected String doInBackground(String... arg0) {
        	String result=null;
            	try{
                    // url where the data will be posted or retrieved              	
                    String postReceiverUrl = "http://phpws.ridio.in/HotelMaster";
                    Log.v(TAG, "postURL: " + postReceiverUrl);                   
                    HttpClient httpClient = new DefaultHttpClient();   // HttpClient                    
                    HttpPost httpPost = new HttpPost(postReceiverUrl);	// post header
                    
                    // add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
                    nameValuePairs.add(new BasicNameValuePair("vendor_id", arg0[0]));
                  	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8);
                    httpPost.setEntity(entity);
                                   
                    // execute HTTP post request
                    HttpResponse response = httpClient.execute(httpPost);
                    result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
            // stuff after posting data
        	progressDialog.dismiss();
        	String message=null;        	
			try {
				Log.d("Inside Post", "message");
				root = new JSONObject(result);
				validation = root.getInt("response");
				message = root.getString("message");
				JSONArray data=root.getJSONArray("data");
				data.length();
				
				Log.d(result, message);
				//Log.d(j_hotel_id, j_hotel_name+" "+j_hotel_place+" "+j_hotel_date+"   "+data.length());
				//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				if(validation==1) {
					hotelList.clear();
					for(int i=0;i<data.length();i++){
						hgs=new HotelGetSetter();
						hgs.setID(data.getJSONObject(i).getString("hotel_id"));
						hgs.setName(data.getJSONObject(i).getString("hotel_name"));
						hgs.setPlace(data.getJSONObject(i).getString("city"));
						hgs.setSelected(Boolean.parseBoolean((data.getJSONObject(i).getString("favourite"))));
						if(hgs.isSelected()){
							checkedList.add(data.getJSONObject(i).getString("favourite"));
							checkedList.add(data.getJSONObject(i).getString("hotel_name"));
						}
						count++;
						hotelList.add(hgs);
					}
					mainMethod();					
				}
//				else
//					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				}
			catch (JSONException e) {
				e.printStackTrace();
        }
     }
	}
	
	/** will send datas to the list Adapter class **/
	public void mainMethod(){
		hca=new HotelCustomAdapter(HotelMaster.this,R.layout.hotel_custom_listview,hotelList);
		list_hotel.setAdapter(hca);
		//list_hotel.setAdapter(new HotelCustomAdapter(this, hotel_id1,hotel_name1,hotel_place1));
	}

	/**Call Dashboard*/
	public void callDashboard(){
		finish();
		Intent gomain=new Intent(getApplicationContext(),MainScreen.class);
		startActivity(gomain);
	}

}


