/** Ridio v1.0.1
 * 	Purpose	   : Return Vehicle by customers Handler Class 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 26-02-2016 (comments, image retrieve)
 *  Verified by:
 *  Verified Dt: 
 * **/

package helix.ridioandroidstudio;

import helix.general.AlertMessages;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ReturnVehicle extends Activity implements OnClickListener,CompoundButton.OnCheckedChangeListener {
	/**Object creation for accessing other classes*/
	NewCustomerDatabaseHandler ncdbh=new NewCustomerDatabaseHandler(this);
	VehicleDatabaseHandler vdb=new VehicleDatabaseHandler(this);
	AlertMessages am= new AlertMessages(this);
	/**Global Class Variables*/
	EditText name,mobile,bike,from,price,eto,tdays,extend,search,rating_comments;
	TextView name1,mobile1,tbike,tfrom,tto,textnd,price1;
	ImageView thanks; Switch list_toggle;
	Button ok,save;
	RatingBar ratingBar1;
	ListView list_return;
	LinearLayout list_tit,nm_img_lyot;
	float rating;
	int count=0;
	private int day,month,year,hour,minute;
	SimpleDateFormat df,dateformat;
	String name_,mobile_,bike1,price_,from_date1,to_date1,days1,amount1;

	ArrayList<ReturnVehicleListValueGetSet> veh_list = new ArrayList<ReturnVehicleListValueGetSet>();
	
	Calendar c = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.return_vehicle);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initializeViews();
		getAdapterView("today");
		/**Default values assigner for Calender*/
		year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        hour  = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        df = new SimpleDateFormat("yyyy-MM-dd HH:MM",Locale.ENGLISH);

		ok.setOnClickListener(this);
		save.setOnClickListener(this);
		list_toggle.setOnCheckedChangeListener(this);

		/**Rating Set*/
		ratingBar1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float ratin,
										boolean fromUser) {
				rating = ratin;
				Log.e("Rating", "" + rating);
			}
		});
	}
	/**Get all bikes needs to be returned and serach filter feature*/
	private void getAdapterView(String allortoady) {
		dateformat= new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		String send_date=dateformat.format(c.getTime()).toString()+" 23:59";
		List<NewCustomerGetSetter> cont = ncdbh.getAllCustomerBikeWithStatusOfP(allortoady,
				send_date);
		veh_list.clear();
		 for (NewCustomerGetSetter cn : cont) {
			 ReturnVehicleListValueGetSet rl=new ReturnVehicleListValueGetSet();
	         rl.setName(cn.getName());
	         rl.setMob(cn.getMobile1());
	         rl.setVehNum(cn.getBikeNo());
			 rl.setFromDt(cn.getFromDate());
			 rl.setToDt(cn.getToDate());
	         rl.setDayRate(cn.getDayRate());
	         rl.setDys(cn.getTotalDays());
	         rl.setAmt(cn.getTotalCost());
	                // Writing Contacts to log
	        Log.d(cn.getID()+"",cn.getName()+" "+cn.getMobile1()+" "+count);
	        count++;
	        veh_list.add(rl);
	        }

		 final ReturnVehicleCustomAdapter rvca = new ReturnVehicleCustomAdapter(ReturnVehicle.this,veh_list);
		 list_return.setAdapter(rvca);//(this, veh_list));
		 
		 search.addTextChangedListener(new TextWatcher() {
			 @Override
			 public void afterTextChanged(Editable arg0) {
			 }
			 @Override
			 public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
			 }
			 @Override
			 public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
				 rvca.getFilter().filter(arg0);
			 }
		 });
	}
	/**View initializer*/
	private void initializeViews() {
		name=(EditText)findViewById(R.id.name);
		mobile=(EditText)findViewById(R.id.mobile);
		price=(EditText)findViewById(R.id.price);
		bike=(EditText)findViewById(R.id.bike);
		from=(EditText)findViewById(R.id.from);
		eto=(EditText)findViewById(R.id.eto);
		extend=(EditText)findViewById(R.id.extend);
		tdays=(EditText)findViewById(R.id.tdays);
		search=(EditText)findViewById(R.id.search);
		rating_comments=(EditText)findViewById(R.id.rating_comments);
		
		name1=(TextView)findViewById(R.id.name1);
		mobile1=(TextView)findViewById(R.id.mobile1);
		tbike=(TextView)findViewById(R.id.tbike);
		tfrom=(TextView)findViewById(R.id.tfrom);
		tto=(TextView)findViewById(R.id.tto);
		textnd=(TextView)findViewById(R.id.textnd);
		
		thanks=(ImageView)findViewById(R.id.thanks);
		ok=(Button)findViewById(R.id.ok);
		save=(Button)findViewById(R.id.save);
		ratingBar1=(RatingBar)findViewById(R.id.ratingBar1);
		list_return = (ListView) findViewById(R.id.list_return);
		list_tit=(LinearLayout)findViewById(R.id.list_tit);
		nm_img_lyot=(LinearLayout)findViewById(R.id.nm_img_lyot);

		list_toggle=(Switch)findViewById(R.id.list_toggle);
	}
	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.ok){
			if(name.getText().toString().length()>2){
				nm_img_lyot.setVisibility(v.GONE);				mobile.setVisibility(v.GONE);
				name1.setVisibility(v.GONE);					mobile1.setVisibility(v.GONE);
				tbike.setVisibility(v.GONE);					bike.setVisibility(v.GONE);
				tfrom.setVisibility(v.GONE);					from.setVisibility(v.GONE);
				tto.setVisibility(v.GONE);						eto.setVisibility(v.GONE);
				textnd.setVisibility(v.GONE);					extend.setVisibility(v.GONE);
				ok.setVisibility(v.GONE);						list_tit.setVisibility(v.GONE);
				list_return.setVisibility(v.GONE);				search.setVisibility(v.GONE);
				list_toggle.setVisibility(v.GONE);

				thanks.setVisibility(v.VISIBLE);				save.setVisibility(v.VISIBLE);
				ratingBar1.setVisibility(v.VISIBLE);			rating_comments.setVisibility(v.VISIBLE);
			}
			else{
				am.SingleButtonAlert("Return Vehicle", "Select Vehicle to return..!", "Ok");
			}
		}
		if(v.getId()== R.id.save){
			updateReturn();
			String mbl=mobile.getText().toString().substring(3, 13);
			File sdCardDirectory = Environment.getExternalStorageDirectory();
			File image = new File(sdCardDirectory+ File.separator + "Ridio Storage",
					mbl+".png");
			if(image.exists()) {
				/**Delete the image of customer from phone*/
				image.delete();
		    }
			am.alertClassClasses("Return Vehicle", "Vehicle Returned", "returnVehicle");
		}
	}
	public void onCheckedChanged(CompoundButton buttonView,
								 boolean isChecked) {
		if(isChecked) {
			list_toggle.setText("All");
			getAdapterView("all");
			Log.d("You are :", "Checked");
		}
		else {
			list_toggle.setText("Today's");
			getAdapterView("today");
			Log.d("You are :", " Not Checked");
		}
	}
	/**Set dummy image if image is not found*/
	public void getCustomerPhoto(String mbl_){
		String mbl=mbl_.substring(3,13);
		File sdCardDirectory = Environment.getExternalStorageDirectory();
		File image = new File(sdCardDirectory+ File.separator + "Ridio Storage", mbl+".png");
		ImageView cus_img = (ImageView) findViewById(R.id.cus_img);
		if(image.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
			cus_img.setImageResource(0);
			cus_img.setImageBitmap(myBitmap);
		}
		else
			cus_img.setImageResource(R.drawable.user);
	}

	/** It will close the return vehicle class*/
	public void onBackPressed(){
		super.onBackPressed();
	}

	/**Update Bike return Status to Vehicle Master and new customer tables*/
	private void updateReturn() {
		String comment="";
		comment=rating_comments.getText().toString();

		ncdbh.updateVehStatusRatingInBookings(new NewCustomerGetSetter(mobile_, bike1, "R", "" + rating,
				extend.getText().toString(), tdays.getText().toString(), price.getText().toString(), comment));
		if(ncdbh.checkSameVehiclePendingAvailability(bike1)>0)
			vdb.updateContact(new VehicleGetSetter(bike1,"R"));
	}
	/**Set the date difference in edittext. This method called from ReturnVehicleCustomAdapter */
	public void diffDateSet(String rate, String dy) {
		DecimalFormat decimal=new DecimalFormat("0.0");
		double res = getDateDiffString(Double.parseDouble(dy));
		tdays.setText(""+decimal.format(res));
		long b_rate = Long.parseLong(rate);
		double tot_amt = res * b_rate;
		long tot_amt1=(long)tot_amt;
		price.setText(""+tot_amt1);
	}
	/**Get the date difference*/
	public Double getDateDiffString(double day){
		long diff=0,ffrm=0,tto=0,eextd=0;
		Date frm,to,extd;
		double difference=0.0;
		try {
			frm = df.parse(from.getText().toString());
			to = df.parse(eto.getText().toString());
			extd = df.parse(extend.getText().toString());

			frm=df.parse(df.format(frm));	to=df.parse(df.format(to));		extd=df.parse(df.format(extd));
			ffrm=frm.getTime();			tto=to.getTime();			eextd=extd.getTime();

			if(tto<eextd)
				diff=eextd-ffrm;
//			else
//				diff=tto-ffrm;
			difference = (double)diff;
			difference=(difference/86400000);//one day value
			if(diff>day) {
				if (diff >= 0 && diff <= 1)
					return difference = 1;
				else if (diff > 1) {
					return difference;
				} else {
					return difference;
				}
			}
			else
				return day;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return difference;
	}
}
