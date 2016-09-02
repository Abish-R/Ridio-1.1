/** Ridio v1.0.1
 * 	Purpose	   : Vehicle Master Class Details Entry Class  
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 22-02-2016 (edit, delete vehicles from vehicle list)
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.ridioandroidstudio;

import helix.general.SaveCameraImageInStorage;
import helix.general.AlertMessages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class VehicleMasterClass extends Activity implements OnClickListener, TextWatcher,OnItemSelectedListener{
	/**Class Objects to access them*/
	VehicleDatabaseHandler vdbh=new VehicleDatabaseHandler(this);
//	VehicleGetBikeNameFromDB gbndb= new VehicleGetBikeNameFromDB(this);
	VehicleNameDatabaseHandler vndh= new VehicleNameDatabaseHandler(this);
	VehicleNameSpinnerAdapter adapter;
	SaveCameraImageInStorage sciis = new SaveCameraImageInStorage(this);
	AlertMessages am = new AlertMessages(this);
	
	/**Class Variables*/	
	Bitmap bm;
	LinearLayout name_layout,categ_layout;
	Button get_pic,save_vehicle,load_my_vehicle_list,return_vehicle_master;
	ImageButton ib_purchase_date,ib_service_date;
	ListView list_vehicle;
	EditText vehicle_model,vehicle_id,vehicle_color,vehicle_number,vehicle_purchase_date,service_date,vehicle_model_year,
		repair_pending,past_repair_issues,categ_edit;
	TextView text_id,text_color,text_number,text_purchase_date,text_service_date,text_model_year,
		text_repair_pending,text_past_repair_issues,text_categ;
	
	int CAMERA_REQUEST=1,count=0;
	File image;
	ImageView catch_vehicle;
	private int year,month,day;
	public String tripleAlertResult="";
    SimpleDateFormat dateFormat;
    final Calendar c = Calendar.getInstance();
    List<Integer> vehicle_id_=new ArrayList<Integer>();
	List<String> vehicle_categ_=new ArrayList<String>();
	List<String> vehicle_color_=new ArrayList<String>();
	List<String> vehicle_number_=new ArrayList<String>();
	List<String> service_date_=new ArrayList<String>();
	List<String> vehicle_model_name_=new ArrayList<String>();
	List<String> repair_pending_=new ArrayList<String>();
	List<String> past_repair_issues_=new ArrayList<String>();
	List<String> vehicle_purchase_date_=new ArrayList<String>();
	List<String> vehicle_imagePath=new ArrayList<String>();
	String[] vehicle_category={"Small","Medium","High"};
	ArrayAdapter<String> vehicle_cat_adapter;
	String text = "<font color=#cc0029>*</font>";
	String vehicle_number1,color,what_bike,model,pdate,sdate,vendor_id,statekey="no",
			selected_veh_category,imagePathe="",intent_val;
		
	Spinner vehicle_model_name_spinner,vehicle_category_spinner;	
	public  ArrayList<VehicleNameGetSet> CustomListViewValuesArr = new ArrayList<VehicleNameGetSet>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_master_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initializeViews();

		catch_vehicle.buildDrawingCache();
		bm=catch_vehicle.getDrawingCache();	
		changeTextViewText();

		Intent checkintent=getIntent();
		intent_val = checkintent.getExtras().getString("invoker");
		if(intent_val.equals("editing"))
			Log.i("Intent :",intent_val);
		else {
			/**Default calender values*/
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			defaultDateCal();

			/**Spinner Color Setting*/
			goColorSpinner("create", "");
		}

		//list_vehicle = (ListView) findViewById(R.id.list_vehicle);
		/**Get Vendor ID*/
		SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
        String spvalue = sp.getString("value", "");
        vendor_id=spvalue;
        Log.e("UserId in New Customer", spvalue);
		
//        setListData();
//        setBikeAdapterValues();
		//getAdapterView();
	}
	private void goColorSpinner(String from_where,String categ) {
//		if(from_where.equals("create"))
//			Log.i(from_where,categ);
//		else
//			for(int i=0;i<vehicle_category.length;i++) {
//				if (vehicle_category[i].equals(categ)) {
//					String temp = vehicle_category[i];
//					vehicle_category[i] = vehicle_category[0];
//					vehicle_category[0] = temp;
//				}
//			}
		vehicle_cat_adapter = new ArrayAdapter<String>(this, R.layout.custom_dropdown, vehicle_category);
		vehicle_category_spinner.setAdapter(vehicle_cat_adapter);
	}
	
	/**List the Name of Vehicles already saved in DB, recieved from Helix*/
	public void setListData(){
		/** Now i have taken static values by loop.**/
		List<VehicleNameGetSet> contacts = vndh.getVehicleFromNameMaster(selected_veh_category);
		CustomListViewValuesArr.clear();
		for (VehicleNameGetSet cn : contacts) {
			final VehicleNameGetSet sched = new VehicleNameGetSet();
			/** Firstly take data in model object */
			sched.setName(cn.getName());
			/** Take Model Object in ArrayList */
            CustomListViewValuesArr.add(sched); 
        }         
	}
	/**Display all the vehicles*/
	private void setBikeAdapterValues() {
		Resources res = getResources();
		adapter = new VehicleNameSpinnerAdapter(VehicleMasterClass.this, R.layout.bike_spinner, CustomListViewValuesArr,res);
		vehicle_model_name_spinner.setAdapter(adapter);
		vehicle_model_name_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
				// Get selected row data to show on screen
				String bike_no_from_adapter = ((TextView) v.findViewById(R.id.textbike_numbr)).getText().toString();
				what_bike = ((TextView) v.findViewById(R.id.textbike)).getText().toString();
				String OutputMsg = "Selected : \n\n" + bike_no_from_adapter;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				//Toast.makeText(getApplicationContext(), "You Must Select Vehicle", Toast.LENGTH_LONG).show();
			}
		});
	}

	/**Loads the vehicles list in different layout*/
	private void showVehicleList(){
		setContentView(R.layout.vehicle_list_of_vendor);
		list_vehicle   = (ListView) findViewById(R.id.list_vehicle);
		return_vehicle_master=(Button)findViewById(R.id.return_vehicle_master);

		getAdapterView();
		return_vehicle_master.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View vv) {
				reloadActivityAgain("oncreate");
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.get_pic){
			/**Request Camera Activity*/
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		    startActivityForResult(cameraIntent, CAMERA_REQUEST);			
		}
		else if(v.getId()==R.id.load_my_vehicle_list){
			showVehicleList();
		}
		else if(v.getId()==R.id.save_vehicle){
			/**Validation Goes before Saving*/
			//vndh.addContact(new VehicleNameGetSet("Hero Splender"));
			vehicle_number1=vehicle_number.getText().toString();
			color=vehicle_color.getText().toString();
			Log.e("Value is :", vehicle_number1+vehicle_number.getText().toString());
			long ll1=diffDateChecker();
			boolean isDigit=false,isDigit1=false,isDigit2=false,isDigit3=false,isDigit4=false,isDigit5=false;
			char char4,char5,char7,char8,char9,char10;
			Character char0=null,char1=null,char11=null,char12=null;
			int hyphen_count=0,taluk=0,nmbr=0;
			if(vehicle_number1.length()>=10){
				if(vehicle_number1.length()<=12)
					char11=vehicle_number1.charAt(6);
				if(vehicle_number1.length()==12)
					char12=vehicle_number1.charAt(7);

				char0=vehicle_number1.charAt(0);				char1=vehicle_number1.charAt(1);
				char4=vehicle_number1.charAt(3);				char5=vehicle_number1.charAt(4);

				char7=vehicle_number1.charAt(vehicle_number1.length()-4);
				char8=vehicle_number1.charAt(vehicle_number1.length()-3);
				char9=vehicle_number1.charAt(vehicle_number1.length()-2);
				char10=vehicle_number1.charAt(vehicle_number1.length()-1);
				isDigit = (char4 >= '0' && char4 <= '9');		isDigit1 = (char5 >= '0' && char5 <= '9');
				isDigit2 = (char7 >= '0' && char7 <= '9');		isDigit3 = (char8 >= '0' && char8 <= '9');
				isDigit4 = (char9 >= '0' && char9 <= '9');		isDigit5 = (char10 >= '0' && char10 <= '9');
				String one=vehicle_number1.substring(3,4);
				String two = vehicle_number1.substring(4,5);
				if((isDigit && isDigit1) || (isDigit2 && isDigit3 && isDigit4 && isDigit5)) {
					taluk = Character.getNumericValue(char4) + Character.getNumericValue(char5);
					nmbr = Character.getNumericValue(char7) + Character.getNumericValue(char8) +
							Character.getNumericValue(char9) + Character.getNumericValue(char10);
					Log.d("Taluk & Nmbr :", taluk+" "+nmbr);
					hyphen_count=vehicle_number1.length() - vehicle_number1.replace("-", "-").length();
					Log.d("Nmbr Changed :", hyphen_count+"");
				}
				else {
					taluk = 0;
					nmbr = 0;
				}
			}

			if(vehicle_number1.length()<10 || !Character.isLetter(char0) || !Character.isLetter(char1) ||
					vehicle_number1.contains("  ") || !isDigit || !isDigit1 || !isDigit2 || !isDigit3 || 
					!isDigit4 || !isDigit5 || vehicle_number1.charAt(2)!='-' || vehicle_number1.charAt(5)!='-' ||
					hyphen_count>2 || taluk<1 || nmbr<1)
				am.SingleButtonAlert("Vehicle Master","Correct Format is: " +
						"\nGA-01-0001\nGA-01-A0001\nGA-01-AA0001","Ok");
			else if((vehicle_number1.length()==11) && !Character.isLetter(char11))
				am.SingleButtonAlert("Vehicle Master","Correct Format is: " +
						"\nGA-01-0001\nGA-01-A0001\nGA-01-AA0001","Ok");
			else if((vehicle_number1.length()==12) && (!Character.isLetter(char12)))
				am.SingleButtonAlert("Vehicle Master","Correct Format is: " +
						"\n        GA-01-0001\n        GA-01-A0001\n        GA-01-AA0001","Ok");
			else if(color.length()<3)
				am.SingleButtonAlert("Vehicle Master","Check Color","Ok");

			else if(ll1>=1){
				if(intent_val.equals("editing")) {
					vdbh.updateVehicles(new VehicleGetSetter(vehicle_number.getText().toString(),vehicle_color.getText().toString(),
							selected_veh_category,service_date.getText().toString(),vehicle_purchase_date.getText().toString(),
							 repair_pending.getText().toString(),past_repair_issues.getText().toString(),
							 dateFormat.format(c.getTime()).toString(), "N"));
					sciis.saveImage("Ridio Storage", vehicle_number1, catch_vehicle);
					am.alertClassClasses("Vehicle Master", "Vehicle edited successfullly..!", "ReloadVehicleMaster");
				}
				else {
					int result=addVehiclesInLocal(vehicle_color.getText().toString(), selected_veh_category,
							vehicle_number.getText().toString(),what_bike, service_date.getText().toString(),
							vehicle_purchase_date.getText().toString(), repair_pending.getText().toString(),
							past_repair_issues.getText().toString(), dateFormat.format(c.getTime()).toString(), "R", vendor_id, "N");
					if(result==0)
						am.SingleButtonAlert("Vehicle Master","This Vehicle Already Added.","Ok");
					else {
						sciis.saveImage("Ridio Storage", vehicle_number1, catch_vehicle);
						am.alertClassClasses("Vehicle Master", "Vehicle added successfullly..!", "ReloadVehicleMaster");
					}
				}
			}
		}
		else if(v.getId()==R.id.ib_purchase_date){
			if(intent_val.equals("editing"))
				am.SingleButtonAlert("Vehicle Master","You can't edit the purchase date","Ok");
			else
				showDialog(1);
		}
		else if(v.getId()==R.id.ib_service_date){
			showDialog(2);
		}
	}

	/**Insert vehicle datas into vehicle master table*/
	public int addVehiclesInLocal(String clr,String cat,String v_no,String bk_nm,String s_dt,
								  String p_dt,String rpr_pnd,String rpr_pst,
								  String cr_dt,String bk_st,String vr_id,String up_st){

		if(rpr_pnd.length()<1)
			rpr_pnd="Nil";
		if(rpr_pst.length()<1)
			rpr_pst="Nil";
		int state=vdbh.addVehicle(new VehicleGetSetter(clr,cat,v_no,bk_nm,s_dt,p_dt,rpr_pnd,rpr_pst,
				cr_dt,bk_st,vr_id,up_st));
		return state;
	}
	/**Default Date sets*/
	public void defaultDateCal(){		  
		Calendar ca = Calendar.getInstance();
		year  = ca.get(Calendar.YEAR);
		month = ca.get(Calendar.MONTH);
		day   = ca.get(Calendar.DAY_OF_MONTH);
		service_date.setText(dateFormat.format(ca.getTime()));
		vehicle_purchase_date.setText(dateFormat.format(ca.getTime()));
		Log.e(dateFormat.format(ca.getTime()), dateFormat.format(ca.getTime()));
	}
	/** Date Difference Calculation */
	private long diffDateChecker() {
		Calendar cal = Calendar.getInstance();
		Date pdt, sdt,now;
		long diff=0,diff1=0,diff2=0;
		pdate=vehicle_purchase_date.getText().toString();
		sdate=service_date.getText().toString();
		try {
			now=cal.getTime();
			pdt=dateFormat.parse(pdate);
			sdt=dateFormat.parse(sdate);
			now=dateFormat.parse(dateFormat.format(now));
			pdt=dateFormat.parse(dateFormat.format(pdt));
			sdt=dateFormat.parse(dateFormat.format(sdt));
			diff = sdt.getTime() - pdt.getTime();
			diff1= pdt.getTime() - now.getTime();
			diff2= sdt.getTime() - now.getTime();
			diff=diff/86400000;
			diff1=diff1/86400000;
			diff2=diff2/86400000;			
			if(diff1>0){
				diff=0;
				am.alertWrongDate("Vehicle Master","Check Purchase Date..!","Reset",1);
			}
			else if(diff<0 || diff2>0){
				diff = 0;
				am.alertWrongDate("Vehicle Master","Check Service Date..!","Reset",2);
			}
			else
				diff=1;
			}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return diff;
	}
	/**Camera activity set image*/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
			Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            catch_vehicle.setImageBitmap(photo);
        }
	}

	/**Set dummy image if image is not found*/
	public int getCustomerPhoto(String veh){
		File image = new File(veh);
		if(image.exists())
			return 1;
		else
			return 0;
	}

	/**Date Picker Listeners*/
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 1:             
            /** open picker dialog,set picker for current date,add pickerListener to date picker*/
            return new DatePickerDialog(this, pickerListener, year, month,day);
		case 2:
			/** open picker dialog,set picker for current date,add pickerListener to date picker*/
            return new DatePickerDialog(this, pickerListener1, year, month,day);            
        }
        return null;
    }
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        /** when dialog box is closed, below method will be called.*/
        @Override
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
        	year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            if(month<9 && day<10)
            	vehicle_purchase_date.setText(new StringBuilder().append(year).append("-0").append(month+1).append("-0").append(day));
            else if(month<9)
            	vehicle_purchase_date.setText(new StringBuilder().append(year).append("-0").append(month+1).append("-").append(day));
            else if(day<10) 
            	vehicle_purchase_date.setText(new StringBuilder().append(year).append("-").append(month+1).append("-0").append(day));
            else
            	vehicle_purchase_date.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));
            //diffDateChecker();
           }
	};
	private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
		/** when dialog box is closed, below method will be called.*/
		@Override
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			year  = selectedYear;
			month = selectedMonth;
			day   = selectedDay;
			if(month<9 && day<10)
				service_date.setText(new StringBuilder().append(year).append("-0").append(month+1).append("-0").append(day));
			else if(month<9)
				service_date.setText(new StringBuilder().append(year).append("-0").append(month+1).append("-").append(day));
			else if(day<10)
				service_date.setText(new StringBuilder().append(year).append("-").append(month+1).append("-0").append(day));
			else
				service_date.setText(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day));   
			// diffDateChecker();
		}
	};

	/**Reload the activity again*/
	public void reloadActivityAgain(String how_load) {
		finish();
		Intent loadvehiclemaster=new Intent(getApplicationContext(),VehicleMasterClass.class);
		if(how_load.equals("oncreate"))
			loadvehiclemaster.putExtra("invoker", how_load);
		else
			loadvehiclemaster.putExtra("invoker", how_load);
		startActivity(loadvehiclemaster);
	}
	/**Get the Adapter View in Vehicle master*/
	private void getAdapterView() {
		/**Stored image path*/
		imagePathe=Environment.getExternalStorageDirectory()+"/Ridio Storage/";
		//Toast.makeText(getApplicationContext(), imagePathe, Toast.LENGTH_LONG).show();
		List<VehicleGetSetter> cont = vdbh.getAllContacts();
		vehicle_id_.clear();vehicle_color_.clear();vehicle_number_.clear();
		vehicle_model_name_.clear();service_date_.clear();vehicle_purchase_date_.clear();
		repair_pending_.clear();past_repair_issues_.clear();vehicle_imagePath.clear();
		for (VehicleGetSetter cn : cont) {
			vehicle_id_.add(cn.getID());//[count]=cn.getID();
			vehicle_categ_.add(cn.getCategory());
			vehicle_color_.add(cn.getColor());
			vehicle_number_.add(cn.getNumber());
			vehicle_model_name_.add(cn.getModelName());
			service_date_.add(cn.getSeriveDate());
			vehicle_purchase_date_.add(cn.getPurchaseDate());
			repair_pending_.add(cn.getPendingRepair());
			past_repair_issues_.add(cn.getPastIssues());
			vehicle_imagePath.add(imagePathe+cn.getNumber()+".png");
			Log.d(cn.getID()+"",cn.getColor()+" "+cn.getPurchaseDate()+" "+count);
			count++;
		}
		vehicle_id.setText(count + "");	/**Passing Value to adapter Class*/
		list_vehicle.setAdapter(new VehicleCustomAdapter(this, vehicle_id_, vehicle_categ_, vehicle_color_,
				vehicle_number_, vehicle_model_name_, service_date_, vehicle_purchase_date_, repair_pending_,
				past_repair_issues_, vehicle_imagePath));
	}
	/**Change TextView with '*' */
	private void changeTextViewText() {
		text_id.append(Html.fromHtml(text));
		text_color.append(Html.fromHtml(text));
		text_categ.append(Html.fromHtml(text));
		text_number.append(Html.fromHtml(text));
		text_purchase_date.append(Html.fromHtml(text));
		text_service_date.append(Html.fromHtml(text));
		text_model_year.append(Html.fromHtml(text));
		//text_repair_pending.append(Html.fromHtml(text));
		//text_past_repair_issues.append(Html.fromHtml(text));
	}
	
	/**Category items list Handling*/
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		selected_veh_category=parent.getItemAtPosition(position).toString();
		setListData();
        setBikeAdapterValues();
		//Toast.makeText(getApplicationContext(), selected_veh_category, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	public void callToEditVehicle(String cate,String clr,String num, String mdl_name, String prchs_dt,
								  String srvc_dt, String pnd_isu, String pst_isu,String img_pth){
		am.alertThreeButtonWarning("Edit Vehicle Details", "You selected the Vehicle to (Edit / Delete) \n " +
				"Press 'Cancel' to load Vehicles List.!", "Delete", "Edit", "Cancel");
//		Toast.makeText(this, "You Clicked "+clr+" "+num+" "+mdl_name+" "+prchs_dt+" "+
//				srvc_dt+" "+pnd_isu+" "+pst_isu, Toast.LENGTH_LONG).show();

			setContentView(R.layout.vehicle_master_layout);
			intent_val = "editing";
			initializeViews();
			selected_veh_category = cate;
			//goColorSpinner("edit", selected_veh_category);
			categ_layout.setVisibility(View.GONE);
			name_layout.setVisibility(View.GONE);
			vehicle_model.setVisibility(View.VISIBLE);
			categ_edit.setVisibility(View.VISIBLE);
			categ_edit.setText(cate);
			vehicle_model.setText(mdl_name);
			vehicle_number.setText(num);
			vehicle_number.setEnabled(false);
			vehicle_color.setText(clr);
			vehicle_purchase_date.setText(prchs_dt);
			service_date.setText(srvc_dt);
			repair_pending.setText(pnd_isu);
			past_repair_issues.setText(pst_isu);
		catch_vehicle.setImageResource(0);
		if(getCustomerPhoto(img_pth) == 1)
			catch_vehicle.setImageBitmap(BitmapFactory.decodeFile((String) img_pth));
		else
			catch_vehicle.setImageResource(R.drawable.veh_cam);
	}

	public void triplAlertResult(int result){
		if(result==1){
			vdbh.deleteSelectedVehicle(vehicle_number.getText().toString());
			reloadActivityAgain("oncreate");
		}
		if(result == 3) {
			reloadActivityAgain("oncreate");
		}
	}

	/**Views Initialization */
	private void initializeViews(){
		catch_vehicle=(ImageView)findViewById(R.id.catch_vehicle);
		save_vehicle=(Button)findViewById(R.id.save_vehicle);
		get_pic=(Button)findViewById(R.id.get_pic);
		load_my_vehicle_list=(Button)findViewById(R.id.load_my_vehicle_list);
		ib_purchase_date=(ImageButton)findViewById(R.id.ib_purchase_date);
		ib_service_date=(ImageButton)findViewById(R.id.ib_service_date);

		text_id=(TextView)findViewById(R.id.text_id);
		text_color=(TextView)findViewById(R.id.text_color);
		text_number=(TextView)findViewById(R.id.text_number);
		text_categ=(TextView)findViewById(R.id.text_categ);
		text_purchase_date=(TextView)findViewById(R.id.text_purchase_date);
		text_service_date=(TextView)findViewById(R.id.text_service_date);
		text_model_year=(TextView)findViewById(R.id.text_model_year);
		text_repair_pending=(TextView)findViewById(R.id.text_repair_pending);
		text_past_repair_issues=(TextView)findViewById(R.id.text_past_repair_issues);

		vehicle_id=(EditText)findViewById(R.id.vehicle_id);
		vehicle_color=(EditText)findViewById(R.id.vehicle_color);
		vehicle_model=(EditText)findViewById(R.id.vehicle_model);
		vehicle_number=(EditText)findViewById(R.id.vehicle_number);
		service_date=(EditText)findViewById(R.id.service_date);
		//vehicle_model_year=(EditText)findViewById(R.id.vehicle_model_year);
		repair_pending=(EditText)findViewById(R.id.repair_pending);
		past_repair_issues=(EditText)findViewById(R.id.past_repair_issues);
		//search_vehicle_name=(EditText)findViewById(R.id.search_vehicle_name);
		vehicle_purchase_date=(EditText)findViewById(R.id.vehicle_purchase_date);
		categ_edit=(EditText)findViewById(R.id.categ_edit);
		vehicle_model_name_spinner=(Spinner)findViewById(R.id.vehicle_model_name_spinner);
		vehicle_category_spinner=(Spinner)findViewById(R.id.vehicle_category_spinner);

		name_layout=(LinearLayout)findViewById(R.id.name_layout);
		categ_layout=(LinearLayout)findViewById(R.id.categ_layout);

		ib_purchase_date.setOnClickListener(this);
		ib_service_date.setOnClickListener(this);
		save_vehicle.setOnClickListener(this);
		get_pic.setOnClickListener(this);
		load_my_vehicle_list.setOnClickListener(this);
		vehicle_number.addTextChangedListener(this);
		vehicle_category_spinner.setOnItemSelectedListener(this);
	}
	
	/**Auto change Text Handling*/
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	@Override
	public void afterTextChanged(Editable s) {
//				String temp = s.toString();
//				Log.d("Deleting", temp);
//				if(statekey.equals("yes"))
//					Log.d("Deleting", temp);
//					else{
//						
//				if(s.length()==2){
//					if(temp.contains("-"))
//						s.replace(1, 2, "");
//				Character char0=temp.charAt(0);
//				Character char1=temp.charAt(1);
//				boolean isDigit,isDigit1;
//				//char char3=temp.charAt(3);
//				//char char4=temp.charAt(4);
//				//isDigit = (char3 >= '0' && char3 <= '9');
//				//isDigit1 = (char3 >= '0' && char3 <= '9');
//				
//				 if(Character.isLetter(char0) && Character.isLetter(char1)){				 
//						 s.append('-');
//						 int pos=vehicle_number.getText().length();
//						 vehicle_number.setSelection(pos);					
//				 }					
//				}
//				else if(s.length()==5){
//					if(s.length()==5 && s.charAt(5)=='-'){
//						s.replace(5, 6, "");}
////					if(s.charAt(5)=='-')
////						s.replace(5, 6, "");					
//					else
//						Log.d("Deleting", temp);
//					//}
//				Character char0=temp.charAt(0);
//				Character char1=temp.charAt(1);
//				boolean isDigit,isDigit1;
//				char char3=temp.charAt(3);
//				char char4=temp.charAt(4);
//				isDigit = (char3 >= '0' && char3 <= '9');
//				isDigit1 = (char4 >= '0' && char4 <= '9');
//				
//				 if(s.length()==5 && Character.isLetter(char0) && Character.isLetter(char1) && isDigit && isDigit1){				 
//						 s.append('-');
//						 int pos=vehicle_number.getText().length();
//						 vehicle_number.setSelection(pos);					
//				 }		
//				}
//				else
//					Log.d("Deleting", temp);
//				}
//			
//				
	}
}
