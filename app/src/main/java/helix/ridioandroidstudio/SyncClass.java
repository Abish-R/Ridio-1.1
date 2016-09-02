/** Ridio v1.0.1
 * 	Purpose	   : SyncClass, sync data from local to main Helix db 
 *  Created by : Abish
 *  Created Dt : old file
 *  Modified on: 27-02-2016 (Sync Two ways)
 *  Verified by:
 *  Verified Dt:
 * **/


package helix.ridioandroidstudio;

import helix.general.CheckInternet;
import helix.general.AlertMessages;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SyncClass extends Activity implements OnClickListener{
	/**Object Creation for other classes*/
	RateDatabaseHandler rdbh=new RateDatabaseHandler(this);
	VehicleDatabaseHandler vdbh=new VehicleDatabaseHandler(this);
	NewCustomerDatabaseHandler ncdbh= new NewCustomerDatabaseHandler(this);
	private Handler handler = new Handler();
	CheckInternet chk_i=new CheckInternet(this);
	AlertMessages am = new AlertMessages(this);
	/**Class Variables*/
	List<Integer> c_id=new ArrayList<Integer>();
	List<String> c_name=new ArrayList<String>();
	List<String> c_mobile1=new ArrayList<String>();
	List<String> c_mobile2=new ArrayList<String>();
	List<String> c_email_id=new ArrayList<String>();
	List<String> c_hotel_name=new ArrayList<String>();
	List<String> c_bike_name=new ArrayList<String>();
	List<String> c_bike_color=new ArrayList<String>();
	List<String> c_bike_reg_number=new ArrayList<String>();
	List<String> c_per_day_rate=new ArrayList<String>();
	List<String> c_from_date=new ArrayList<String>();
	List<String> c_to_date=new ArrayList<String>();
	List<String> c_number_of_days=new ArrayList<String>();
	List<String> c_total_cost=new ArrayList<String>();
	List<String> c_vendor_id=new ArrayList<String>();
	List<String> c_booking_date=new ArrayList<String>();
	List<String> c_customer_trust_rating=new ArrayList<String>();
	List<String> c_bik_status=new ArrayList<String>();
	List<String> c_upld_status=new ArrayList<String>();
	List<String> c_feedback=new ArrayList<String>();
	ArrayList<VehicleGetSetter> vehicle_list=new ArrayList<VehicleGetSetter>();
	ArrayList<RateGetSetter> rate_list= new ArrayList<RateGetSetter>();
	
	JSONObject root;
	Button btn_sync,sync_done;
	TextView sync_decrip;
	ProgressBar sync_progress;
	int validation=0,size=0,count=0;
	boolean sucess_flag=false;
	String id,n,m1,m2=null,e,h=null,bname,bclr,bnum,bp,fd,to,td,tc,v_id,bk_d,r,bk_s,up_s,feedb;
	String vendor_id;
	int current_status,total_count=0,veh_nUpload_count=0,rat_nUpload_count=0,book_nUpload_count=0;
	final int INTERVAL = 10000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_layout);
		initializeView();
		btn_sync.setOnClickListener(this);

		//scheduleForSync();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.btn_sync) {
			if(btn_sync.getText().toString().equals("Done"))
				callToMainPage();
			else {
				onSyncButton();
//				sync_decrip.setText("Already all datas uploaded..!");
//				btn_sync.setText("Done");
			}
		}
	}

	/**Initialize Views */
	private void initializeView() {
		btn_sync=(Button)findViewById(R.id.btn_sync);
		//sync_done=(Button)findViewById(R.id.sync_done);
		sync_decrip=(TextView)findViewById(R.id.sync_decrip);
		sync_progress=(ProgressBar)findViewById(R.id.sync_progress);
	}

	/**Upload status updater*/
	public void uploadStatus(int size_, String who_invoked){
		int size_temp=0;
		String invoker =who_invoked;
		if(size_>0) {
			total_count--;
			count++;
			size_temp = 100 / size_;
			current_status += size_temp;
			sync_progress.setProgress(current_status);
			sync_decrip.setText(current_status + "% " + invoker + " is uploaded from total of " + size_);
			Log.i("Status : ", current_status + "");
		}
		else{
			sync_decrip.setText("Already all datas uploaded..!");
			size_temp=0;
		}
		if(total_count==0){
			current_status=0;
			sync_progress.setProgress(100);
			scheduleForSync();
			sync_decrip.setText("100% bookings is uploaded from total of " + size_);
			btn_sync.setText("Done");
		}
	}

	/**Auto Invoke Function for Syncing datas from local DB to Cloud*/
	public void scheduleForSync() {
		handler.postDelayed(new Runnable() {
			public void run() {
				onSyncButton();
				/** Call for rate master, vehicle master, bookings bulk sync*/
				if (chk_i.isOnline())
					new AsyncSyncDownload(SyncClass.this,"New").execute(vendor_id, "New");
				handler.postDelayed(this, INTERVAL);
			}
		}, INTERVAL);
	}
	
	/** On back button pressed close the current Activity **/
	public void onBackPressed() {
	     super.onBackPressed();
	     finish();
	}

	/** On Sync Button Press this Function will execute*/
	public void onSyncButton(){
		veh_nUpload_count=vdbh.getVehiclesCountWithStatusN();
		rat_nUpload_count=rdbh.getNotUploadedRatesCount();
		book_nUpload_count=ncdbh.getAllCustomerNotUploadedCount();
		total_count=veh_nUpload_count+rat_nUpload_count+book_nUpload_count;
		/**get Values from temporary storage*/
		SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
		vendor_id = sp.getString("value", "");

		if(veh_nUpload_count>0){
			bringNotUploadedVehicle();
			doSyncVehicle();
		}
		if(rat_nUpload_count>0){
			bringNotUploadedRates();
			doSyncRates();
		}
		if(book_nUpload_count>0){
			bringLocalTableCustomer();
			doSyncBookings();
		}
		if(veh_nUpload_count<1 && rat_nUpload_count<1 && book_nUpload_count<1) {
			sync_decrip.setText("Already all datas uploaded..!");
			btn_sync.setText("Done");
		}

		/** Call for rate master, vehicle master, bookings bulk sync*/
		if (chk_i.isOnline())
			new AsyncSyncDownload(SyncClass.this,"New").execute(vendor_id, "New");
	}

	/**Starts Doing Booking Sync*/
	public void doSyncBookings() {
//		int booking_size=c_id.size();
//		id=""+booking_size;
		for(int i=0;i<book_nUpload_count;i++){
			//id=c_id.get(i).toString();
			n=c_name.get(i).toString();					m1=c_mobile1.get(i).toString();
			m2=c_mobile2.get(i).toString();				e=c_email_id.get(i).toString();
			h=c_hotel_name.get(i).toString();			bname=c_bike_name.get(i).toString();
			bclr=c_bike_color.get(i).toString();		bnum=c_bike_reg_number.get(i).toString();
			bp=c_per_day_rate.get(i).toString();		fd=c_from_date.get(i).toString();
			to=c_to_date.get(i).toString();				td=c_number_of_days.get(i).toString();
			tc=c_total_cost.get(i).toString();			v_id=c_vendor_id.get(i).toString();
			bk_d=c_booking_date.get(i).toString();		r=c_customer_trust_rating.get(i).toString();
			bk_s=c_bik_status.get(i).toString();		up_s = c_upld_status.get(i).toString();
			feedb = c_feedback.get(i).toString();
			if (chk_i.isOnline()){
				new AsyncSyncBookingsUploadClass(SyncClass.this).execute(book_nUpload_count+"", n, m1, m2,
						e, h, bname, bclr, bnum, bp, fd, to, td, tc, v_id, bk_d, r, bk_s, up_s, feedb);
			}
			else
				am.SingleButtonAlert("Synchronization", "Check Internet.!","Ok");
		}
	}

	/**Starts Doing Vehicle Master Sync*/
	public void doSyncVehicle() {
//		int vehicle_size=vehicle_list.size();
//		String tot=""+vehicle_size;
		for(int i=0;i<veh_nUpload_count;i++){
			String no,clr,cate,mdl_nam,src_dt,pr_dt,pnd_rpr,pst_isu,crt_dt,vr_id,up_ste,vh_st;
			no=vehicle_list.get(i).getNumber().toString();				clr=vehicle_list.get(i).getColor().toString();
			cate=vehicle_list.get(i).getCategory().toString();			mdl_nam=vehicle_list.get(i).getModelName().toString();
			src_dt=vehicle_list.get(i).getSeriveDate().toString();		pr_dt=vehicle_list.get(i).getPurchaseDate().toString();
			pnd_rpr=vehicle_list.get(i).getPendingRepair().toString();	pst_isu=vehicle_list.get(i).getPastIssues().toString();
			crt_dt=vehicle_list.get(i).getCreatedDate().toString();		vr_id=vehicle_list.get(i).getVendorId().toString();
			up_ste=vehicle_list.get(i).getUploadState().toString();		vh_st=vehicle_list.get(i).getStatus().toString();
			if(pnd_rpr.length()<1)
				pnd_rpr=null;
			if(pst_isu.length()<1)
				pst_isu=null;
			Log.d(no, clr+" "+cate+" "+mdl_nam+" "+src_dt+" "+pr_dt+" "+pnd_rpr+" "+pst_isu+" "+crt_dt+" "+vr_id+" "+up_ste);
			if(chk_i.isOnline()){
				new AsyncSyncVehicleUploadClass(SyncClass.this).execute(no,clr,cate,mdl_nam,src_dt,pr_dt,pnd_rpr,
						pst_isu,crt_dt,vr_id,up_ste,vh_st,veh_nUpload_count+"");
		}
		else
			am.SingleButtonAlert("Synchronization", "Check Internet.!","Ok");
		}
	}

	/**Starts Doing Rate Master Sync*/
	public void doSyncRates() {
//		int rate_size=rate_list.size();
//		String len=""+rate_size;
		for(int i=0;i<rat_nUpload_count;i++){
			String date,rate1,rate2,rate3,created_dt,up_state;
			date=rate_list.get(i).getRateDate().toString();				rate1=rate_list.get(i).getRate().toString();
			rate2=rate_list.get(i).getRate1().toString();				rate3=rate_list.get(i).getRate2().toString();
			created_dt=rate_list.get(i).getCreatedDate().toString();	up_state=rate_list.get(i).getUpState().toString();
			Log.d(date, rate1+" "+rate2+" "+rate3+" "+created_dt+" "+up_state+" "+rat_nUpload_count);
			if(chk_i.isOnline()){
				new AyncSyncRateUploadClass(SyncClass.this).execute(date,rate1,rate2,rate3,created_dt,
						up_state,vendor_id,rat_nUpload_count+"");
			}
			else
				am.SingleButtonAlert("Synchronization", "Check Internet.!","Ok");
		}
	}

	/**Bring All the Bookings Datas which Are not uploaded yet*/
	private void bringLocalTableCustomer() {
		List<NewCustomerGetSetter> cust_list = ncdbh.getAllCustomerNotUploaded();
		for (NewCustomerGetSetter cn : cust_list) {
			size++;
			c_id.add(cn.getID());							c_name.add(cn.getName());
			c_mobile1.add(cn.getMobile1());
//			String mob2=cn.getMobile2();
//			if(mob2.length()<1 || mob2==null || mob2.equals("null"))
//				c_mobile2.add("null");
//			else
				c_mobile2.add(cn.getMobile2());
			c_email_id.add(cn.getEmail());					c_hotel_name.add(cn.getHotel());
			c_bike_name.add(cn.getBikeName());				c_bike_color.add(cn.getBikeColor());
			c_bike_reg_number.add(cn.getBikeNo());			c_per_day_rate.add(cn.getDayRate());
			c_from_date.add(cn.getFromDate());				c_to_date.add(cn.getToDate());
			c_number_of_days.add(cn.getTotalDays());		c_total_cost.add(cn.getTotalCost());
			c_vendor_id.add(cn.getVendorId());				c_booking_date.add(cn.getBookDate());
			c_customer_trust_rating.add(cn.getRating());	c_bik_status.add(cn.getBikeStatus());
			c_upld_status.add(cn.getUploadStatus());		c_feedback.add(cn.getFeedback());
		}
		if(size<1){
			sync_decrip.setText("Already all datas uploaded..!");
		}
	}

	/**Bring All the Vehicles Datas which Are not uploaded yet*/
	public void bringNotUploadedVehicle(){
		List<VehicleGetSetter> veh_data=vdbh.getVehicleByUploadStatusN();
		for (VehicleGetSetter cn : veh_data) {
			VehicleGetSetter vgs=new VehicleGetSetter();
			vgs.setID(cn.getID());							vgs.setNumber(cn.getNumber());
			vgs.setColor(cn.getColor());					vgs.setCategory(cn.getCategory());
			vgs.setModelName(cn.getModelName());			vgs.setServiceDate(cn.getSeriveDate());
			vgs.setPurchaseDate(cn.getPurchaseDate());		vgs.setPendingRepair(cn.getPendingRepair());
			vgs.setPastIssues(cn.getPastIssues());			vgs.setCreatedDate(cn.getCreatedDate());
			vgs.setStatus(cn.getStatus());					vgs.setVendorId(cn.getVendorId());
			vgs.setUploadState(cn.getUploadState());

			vehicle_list.add(vgs);
		}
	}
	/**Bring All the Rate Datas which Are not uploaded yet*/
	public void bringNotUploadedRates(){
		List<RateGetSetter> rate_data=rdbh.getNotUploadedRates();
		for (RateGetSetter rgs : rate_data) {
			RateGetSetter obj1=new RateGetSetter();
			obj1.setID(rgs.getID());						obj1.setRate(rgs.getRate());
			obj1.setRate1(rgs.getRate1());					obj1.setRate2(rgs.getRate2());
			obj1.setRateDate(rgs.getRateDate());			obj1.setCreatedDate(rgs.getCreatedDate());
			obj1.setUpState(rgs.getUpState());

			rate_list.add(rgs);
		}
	}

	/**Update status to the local table about the upload status*/
	public void updateBikeUploadStatus(String bike_no,String state) {
		vdbh.updateUploadStateDB(bike_no,state);
	}

	public void updateRateUploadStatus(String rt_dt,String state){
		rdbh.updateRatesUploadStatus(rt_dt,state);
	}
	/**returns to main page*/
	private void callToMainPage() {
		Intent callmainpage=new Intent(this,MainScreen.class);
		startActivity(callmainpage);
		finish();
	}

	/**Passes all the datas to rate master table for insert*/
	public void rateAddPasser(String s_rt,String m_rt, String h_rt,String rt_dt,String cr_dt, String up_st) {
		//RateMasterClass rm=new RateMasterClass();
		int rt_cnt= rdbh.getRateCount();
		rdbh.addContact(new RateGetSetter(rt_cnt + 1, s_rt, m_rt, h_rt, rt_dt, cr_dt, up_st));
	}

	/**Passes the datas to Vehicle master table for insert*/
	public void vehicleAddPasser(String clr,String cat,String v_no,String bk_nm,String s_dt,
								 String p_dt,String rpr_pnd,String rpr_pst,
								 String cr_dt,String bk_st,String vr_id,String up_st){
		//VehicleMasterClass vm = new VehicleMasterClass();
		if(rpr_pnd.length()<1)
			rpr_pnd="Nil";
		if(rpr_pst.length()<1)
			rpr_pst="Nil";
		int state=vdbh.addVehicle(new VehicleGetSetter(clr,cat,v_no,bk_nm,s_dt,p_dt,rpr_pnd,rpr_pst,
				cr_dt,bk_st,vr_id,up_st));
		//return state;
	}

	/**Passes all the datas which recieved from cloud to booking(NewCustomer) table for insert*/
	public void bookingsDataPasser(String n,String m1,String m2,String e,String h,
								   String bike_name,String bike_color,String bike_reg_number,
								   String bp,String fd,String to,String td,String tc,
								   String vendor_id, String booking_date, String ratingfromcloud,
								   String bk_status,String up_status,String ratingcommentcloud){
		//NewCustomer nc = new NewCustomer();
		/**Save Bookings in Local DB **/
		ncdbh.addContact(new NewCustomerGetSetter(n, m1, m2, e, h, bike_name, bike_color,
				bike_reg_number, bp, fd, to, td, tc, vendor_id, booking_date, ratingfromcloud,
				bk_status, up_status, ratingcommentcloud));
		/** Update Vehicles status in Local**/
		vdbh.updateContact(new VehicleGetSetter(bike_reg_number, "P"));
	}
}
