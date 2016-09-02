/** Ridio v1.0.1
 * 	Purpose	   : RateMasterClass
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 20-02-2016 (Added Created date and modified for sync)
 *  Verified by:
 *  Verified Dt:
 * **/
package helix.ridioandroidstudio;

import helix.general.AlertMessages;
import java.io.File;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RateMasterClass extends Activity implements OnClickListener{
	/** Global declarations **/
	AlertMessages am = new AlertMessages(this);
	RateDatabaseHandler rdb=new RateDatabaseHandler(this);
	ListView list_rate;
	Button save_rate,load_rate_list,return_rate_master,popup_open;
	int count=0,size=1;
	Calendar c;
	List<Integer> id=new ArrayList<Integer>();
	List<String> rate=new ArrayList<String>();
	List<String> rate1=new ArrayList<String>();
	List<String> rate2=new ArrayList<String>();
	List<String> date=new ArrayList<String>();
	String selectedoption,intent_val="";
	String small_rate="0",medium_rate="0",high_rate="0",popup_enabled="";
	ArrayAdapter<String> vehicle_cat_adapter;
	EditText rate_id,rate_amount_small,rate_amount_medium,rate_amount_high,rate_date,input_on_list;
	boolean isDigit=false;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Calendar cc = Calendar.getInstance();
	/** Global declarations ends **/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate_master);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initializeViews();

		Intent checkintent=getIntent();
		intent_val = checkintent.getExtras().getString("invoker");
		if(intent_val.equals("editing"))
			Log.d("Selected: ","Editing");
		else {
			final int rate_count = rdb.getRateCount();
			//rate_id.setText(rate_count+1+"");
			if (rate_count > 0) {
				try {
					Date date1 = df.parse(rdb.getLastDate(rate_count + ""));
					c.setTime(date1);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				c.add(Calendar.DATE, 1);
				rate_date.setText(df.format(c.getTime()));
			} else
				rate_date.setText(df.format(c.getTime()));
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.save_rate) {
			/**Rate Validation*/
		boolean check_rate=false;
			if (rate_amount_small.getText().toString().length() > 1 &&
					Integer.parseInt(rate_amount_small.getText().toString()) > 49) {
				char tmp = rate_amount_small.getText().toString().charAt(0);
				isDigit = (tmp >= '1');
				if ((rate_amount_medium.getText().toString().length() > 0 &&
						Integer.parseInt(rate_amount_medium.getText().toString()) < 49) ||
						(rate_amount_high.getText().toString().length() > 0 &&
						Integer.parseInt(rate_amount_high.getText().toString()) < 49))
					check_rate=false;
				else
					check_rate=true;
			} else if (rate_amount_medium.getText().toString().length() > 1 &&
					Integer.parseInt(rate_amount_medium.getText().toString()) > 49) {
				char tmp = rate_amount_medium.getText().toString().charAt(0);
				isDigit = (tmp >= '1');
				if ((rate_amount_small.getText().toString().length() > 0 &&
						Integer.parseInt(rate_amount_small.getText().toString()) < 49) ||
						(rate_amount_high.getText().toString().length() > 0 &&
								Integer.parseInt(rate_amount_high.getText().toString()) < 49))
					check_rate=false;
				else
					check_rate=true;
			} else if (rate_amount_high.getText().toString().length() > 1 &&
					Integer.parseInt(rate_amount_high.getText().toString()) > 49) {
				char tmp = rate_amount_high.getText().toString().charAt(0);
				isDigit = (tmp >= '1');
				if ((rate_amount_small.getText().toString().length() > 0 &&
						Integer.parseInt(rate_amount_small.getText().toString()) < 49) ||
						(rate_amount_medium.getText().toString().length() > 0 &&
								Integer.parseInt(rate_amount_medium.getText().toString()) < 49))
					check_rate=false;
				else
					check_rate=true;
			} else {
				isDigit = false;
			}
			if (!isDigit || !check_rate || rate_amount_small.getText().toString().length() > 5 || rate_amount_medium.getText().toString().length() > 5
					&& rate_amount_high.getText().toString().length() > 5)
				am.SingleButtonAlert("Rate Master", "Wrong Rate \n Rate cant't be added..!", "Reset");
			else {
				getRatesFromEditText();
				if (intent_val.equals("oncreate")) {
					/**Add Rate in Local DB*/
					addRateToTable(small_rate, medium_rate, high_rate,
							rate_date.getText().toString(), df.format(cc.getTime()), "N");
					am.alertClassClasses("Rate Master", "Rate added successfullly..!", "ratemaster");
					//reloadActivityAgain("oncreate");
				} else {
					rdb.updateRates(small_rate, medium_rate, high_rate, rate_date.getText().toString());
					am.alertClassClasses("Rate Master", "Rate edited successfullly..!", "ratemaster");
					//reloadActivityAgain("oncreate");
				}
			}
		}
		if(v.getId()==R.id.return_rate_master){
			input_on_list.setVisibility(View.GONE);
			if(popup_enabled.equals("range"))
				showForRange();
			else
				reloadActivityAgain("oncreate");
		}
		if(v.getId()==R.id.load_rate_list) {
			/**Loads the vehicles list*/
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			setContentView(R.layout.rate_listview_show);
			list_rate = (ListView) findViewById(R.id.list_rate);
			return_rate_master=(Button)findViewById(R.id.return_rate_master);
			popup_open=(Button)findViewById(R.id.popup_open);
			input_on_list=(EditText)findViewById(R.id.input_on_list);
			popup_open.setOnClickListener(this);
			return_rate_master.setOnClickListener(this);
			input_on_list.setOnClickListener(this);
			getAdapterView("10", df.format(cal.getTime()));
		}
		if(v.getId()==R.id.popup_open){
			popUpOptionHandler();
		}
	}

	/** Layout views initializer */
	private void initializeViews(){
		c = Calendar.getInstance();
		rate_amount_small=(EditText)findViewById(R.id.rate_amount_small);
		rate_amount_medium=(EditText)findViewById(R.id.rate_amount_medium);
		rate_amount_high=(EditText)findViewById(R.id.rate_amount_high);
		rate_date=(EditText)findViewById(R.id.vehicle_created_date);
		save_rate=(Button)findViewById(R.id.save_rate);
		load_rate_list=(Button)findViewById(R.id.load_rate_list);

		save_rate.setOnClickListener(this);
		load_rate_list.setOnClickListener(this);
	}

	/**Add Rate in Local DB*/
	public void addRateToTable(String s_rt,String m_rt, String h_rt,String rt_dt,String cr_dt, String up_st) {
		final int rate_count = rdb.getRateCount();
		rdb.addContact(new RateGetSetter(rate_count + 1, s_rt, m_rt, h_rt, rt_dt, cr_dt, up_st));
	}
	
	/**Get rates from editText*/
	public void getRatesFromEditText(){
		small_rate=rate_amount_small.getText().toString();
		medium_rate=rate_amount_medium.getText().toString();
		high_rate=rate_amount_high.getText().toString();
	}
	
	/**Activity Reload after rated Added*/
	public void reloadActivityAgain(String how_invoking) {
		finish();
		Intent loadratemaster=new Intent(getApplicationContext(),RateMasterClass.class);
		if(how_invoking.equals("oncreate")) {
			loadratemaster.putExtra("invoker","oncreate");
			startActivity(loadratemaster);
		}
		else{
			loadratemaster.putExtra("invoker", "editing");
			startActivity(loadratemaster);

		}
	}
	
	/**Pass the rates to Display in list*/
	private void getAdapterView(String how_to_load_list,String date_) {
		id.clear(); rate.clear(); rate1.clear(); rate2.clear(); date.clear();
		List<RateGetSetter> contacts = rdb.getAllRates(how_to_load_list,date_);
		 for (RateGetSetter cn : contacts) {
			 size++;
 		 	 id.add(cn.getID());			rate.add(cn.getRate());		rate1.add(cn.getRate1());
             rate2.add(cn.getRate2());      date.add(cn.getRateDate());

	         Log.d(cn.getID()+"",cn.getRate()+" "+cn.getCreatedDate()+" "+count);
	         count++;
	        }
		 //rate_id.setText(count+1+"");			
		 list_rate.setAdapter(new RateCustomAdapter(this, id, rate, rate1, rate2, date));
	}
	/**Editing handler of rates*/
	public void editRateList(String rate1,String rate2,String rate3,String date){
		am.alertDoubleButton("Rate Master", "Rate of " + date + " asking to edit.\n Do you want to edit.?", "Edit", "Cancel");
		setContentView(R.layout.rate_master);
		intent_val="editing";
		initializeViews();
		small_rate=rate1;medium_rate=rate2;high_rate=rate3;
		rate_amount_small.setText(rate1);
		rate_amount_medium.setText(rate2);
		rate_amount_high.setText(rate3);
		rate_date.setText(date);
	}

	public void checkToast(int result){
		Toast.makeText(getApplicationContext(),"You clicked: "+result,Toast.LENGTH_SHORT).show();
	}

	/**Pop up menu for filter the list */
	private void popUpOptionHandler(){
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.popoup, null);
		final PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.showAtLocation(popupView, Gravity.RIGHT, 10, -10);
		Button range = (Button)popupView.findViewById(R.id.range);
		Button date_ascen = (Button)popupView.findViewById(R.id.date_ascen);
		Button date_descen = (Button)popupView.findViewById(R.id.date_descen);
		Button all = (Button)popupView.findViewById(R.id.all);
		Button dismiss = (Button)popupView.findViewById(R.id.dismiss);

		range.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				return_rate_master.setBackgroundResource(R.drawable.press_effect_rect);
				return_rate_master.setText("Get");
				input_on_list.setVisibility(View.VISIBLE);
				input_on_list.setEnabled(true);
				input_on_list.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
				popupWindow.dismiss();
				popup_enabled="range";
				checkToast(1);
			}});
		date_ascen.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				//input_on_list.setVisibility(View.VISIBLE);
				showDialog(1);
				popup_enabled="date_ascending";
				checkToast(2);
			}});
		date_descen.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				//input_on_list.setVisibility(View.VISIBLE);
				showDialog(1);
				popup_enabled="date_descending";
				checkToast(3);
			}});
		all.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				input_on_list.setVisibility(View.GONE);
				return_rate_master.setBackgroundResource(R.drawable.press_effect_ratelist);
				return_rate_master.setText("");
				popup_enabled="all_entry";
				cc.add(Calendar.DATE, -1);
				getAdapterView("all", df.format(cc.getTime()));
				popup_enabled="ready_exit";
				checkToast(4);
			}});
		dismiss.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				input_on_list.setVisibility(View.GONE);
				return_rate_master.setBackgroundResource(R.drawable.press_effect_ratelist);
				return_rate_master.setText("");
				popupWindow.dismiss();
				popup_enabled="ready_exit";
			}});
		popupWindow.showAsDropDown(popup_open, 0, 0);
	}
	private void showForRange(){
		if(input_on_list.getText().toString().length()>1 && Integer.parseInt(input_on_list.getText().toString())%10==0) {
			getAdapterView(input_on_list.getText().toString(), df.format(cc.getTime()));
			popup_enabled="ready_exit";
			return_rate_master.setBackgroundResource(R.drawable.press_effect_ratelist);
			return_rate_master.setText("");
		}
		else
			Toast.makeText(getApplicationContext(),"Range error.",Toast.LENGTH_SHORT).show();

	}

//	private void showDateDescen(){
//		String in_rate_list=input_on_list.getText().toString();
//		if(in_rate_list.length()>1 && Integer.parseInt(in_rate_list)%10==0){
//			getAdapterView(in_rate_list,df.format(cc.getTime()));
//			popup_enabled="ready_exit";
//		}
//		else
//			Toast.makeText(getApplicationContext(),"Range error.",Toast.LENGTH_SHORT).show();
//
//	}

	/** Date Picker Listener */
	@Override
	protected Dialog onCreateDialog(int id) {
				/** open picker dialog,set picker for current date,add pickerListener to date picker*/
		int year  = cc.get(Calendar.YEAR); int month = cc.get(Calendar.MONTH);	int day   = cc.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(this, pickerListener, year, month,day);
	}
	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
	/** when dialog box is closed, below method will be called.*/
		@Override
		public void onDateSet(DatePicker view, int slctdYr,int slctdMoth, int slctdDy) {
			//input_on_list.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			input_on_list.setVisibility(View.GONE);
			return_rate_master.setBackgroundResource(R.drawable.press_effect_ratelist);
			return_rate_master.setText("");
			StringBuilder strg_bld=new StringBuilder();
			if(slctdMoth<9 && slctdDy<10)
				strg_bld.append(slctdYr).append("-0").append(slctdMoth+1).append("-0").append(slctdDy);
			else if(slctdMoth<9)
				strg_bld.append(slctdYr).append("-0").append(slctdMoth+1).append("-").append(slctdDy);
			else if(slctdDy<10)
				strg_bld.append(slctdYr).append("-").append(slctdMoth+1).append("-0").append(slctdDy);
			else
				strg_bld.append(slctdYr).append("-").append(slctdMoth+1).append("-").append(slctdDy);
			String temp=strg_bld.toString().substring(0,9);
			int tmp;
			if(popup_enabled.equals("date_ascending")) {
				tmp = Integer.parseInt(strg_bld.toString().substring(9, 10)) - 1;
				getAdapterView("date_ascending", temp + tmp);
			}
			else if(popup_enabled.equals("date_descending")) {
				//tmp = Integer.parseInt(strg_bld.toString().substring(9, 10)) + 1;
				getAdapterView("date_descending", strg_bld.toString());
			}
			popup_enabled="ready_exit";
		}
	};
}
