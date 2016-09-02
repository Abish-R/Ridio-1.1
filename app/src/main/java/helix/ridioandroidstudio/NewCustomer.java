/** Ridio v1.0.1
 * 	Purpose	   : New customer Details Entry Class 
 *  Created by : Abish 
 *  Created Dt : 15-12-2015
 *  Modified on: 26-02-2016 (Rating Comment and time implementation)
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.ridioandroidstudio;

import helix.general.CheckInternet;
import helix.general.AlertMessages;
import helix.general.SaveCameraImageInStorage;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class NewCustomer extends Activity implements OnClickListener, TextWatcher,OnItemSelectedListener,
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
	/**  Class access Objects */
	RateDatabaseHandler rdbh = new RateDatabaseHandler(this);
	VehicleDatabaseHandler vdb = new VehicleDatabaseHandler(this);
	NewCustomerBikeAdapterViewsCreater bikeadapterobj = new NewCustomerBikeAdapterViewsCreater(this);
	NewCustomerDatabaseHandler ncdh = new NewCustomerDatabaseHandler(this);
	//HotelGetSetter hgs;
	CheckInternet chk_inrnt = new CheckInternet(this);
	AlertMessages am = new AlertMessages(this);
	SaveCameraImageInStorage sciis = new SaveCameraImageInStorage(this);

	/** Global Declarations*/
	List<String> hotel_id1 = new ArrayList<String>();
	List<String> hotel_name1 = new ArrayList<String>();
	List<String> hotel_place1 = new ArrayList<String>();
	List<String> h_created_date1 = new ArrayList<String>();
	public ArrayList<BikeSpinner> CustomListBikeName = new ArrayList<BikeSpinner>();
	public ArrayList<BikeSpinner> CustomListBikeColor = new ArrayList<BikeSpinner>();
	public ArrayList<BikeSpinner> CustomListBikeNumber = new ArrayList<BikeSpinner>();
	public ArrayList<HotelSpinner> CustomListhotel = new ArrayList<HotelSpinner>();

	BikeSpinnerAdapter adapter, coloradapter, numberadapter;
	//BikeSpinnerColor coloradapter;
	HotelSpinnerAdapter htl_adapter;

	EditText name, cust_mobile1_code, mobile1, mobile2, email1, hotelname, bikedetail, bike_price,
			from_date, to_date, total_days, total_cost;
	TextView custname, mob1, email, bike, bike_rate, frm_date, to_dat, days, cost, text_vendor;
	Button cust_photo, cust_save, plus, minus;
	private ImageButton ib_from_date, ib_to_date;
	Spinner mySpinner, h_spinner, cust_bike_color, cust_bike_number, bike_categ;

	int increase, decrease, validation = 0, count = 0;
	String bike_reg_number = "", vendor_id = "", booking_date, customer_trust_rating = "", bk_status = "P", up_status = "N";//P-pending bike from customer
	String n, m1, m2 = "", e, h = "null", bd, bp, fd, to, td, tc, mcc = "";
	String text = "<font color=#cc0029>*</font>";
	String bike_no_from_adapter, bike_name = "", bike_color = "", what_bike, hhotel_id, hhotel_name, hhotel_place;
	String TAG = "Inside Async", selected_veh_category;
	String ratingfromcloud="0", ratingcommentcloud="";

	String[] vehicle_category = {"Small", "Medium", "High"};
	ArrayAdapter<String> vehicle_cat_adapter;

	ImageView imageView2;
	public ImageView cam_img;
	Bitmap bm;
	static int CAMERA_REQUEST = 1;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String STORAGE_DIRECTORY = "Ridio Storage";
	private static int date_invoker = 0;
	private static StringBuilder from_to_date_time;

	LinearLayout last_layout;
	SimpleDateFormat df, timeformat;
	private DateFormat dateFormat;
	Date oldDate, newDate, oldDate1, newDate1;
	private int day, month, year, hour, minute;
	Calendar c = Calendar.getInstance();
	//JSONObject root= new JSONObject();
	Resources res = null;

	/** Global Declarations Ends */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_bookings);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		/**Call to initialize views**/
		initializeLayoutViews();

		/**get Values from temporary storage*/
		SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
		vendor_id = sp.getString("value", "");
		Log.e("UserId in New Customer", vendor_id);
		text_vendor.append(" " + vendor_id);

		/**Call Functions to calender initializer,load Bikes from DB,call List adapter*/
		defaultDateCal();
//		bikeadapterobj.setListBikeName(vendor_id,selected_veh_category);	
//		bikeadapterobj.setBikeAdapterValues(CustomListBikeName,adapter,mySpinner,"name",res);
		changeTextViewText();

		if (chk_inrnt.isOnline()) {
			/**Get Hotels in Booking*/
			new AsyncNewCustomerHotelGet(NewCustomer.this).execute(vendor_id);
		} else {
			//setHotelAdapterValues();
			am.SingleButtonAlert("Need Hotels.?", "Check Internet.!", "Ok");
			h_spinner.setVisibility(View.GONE);
			imageView2.setVisibility(View.GONE);
			hotelname.setVisibility(View.VISIBLE);
			hotelname.setText("No access to Hotels");

		}

		cust_save.setOnClickListener(this);
		cust_photo.setOnClickListener(this);
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		ib_from_date.setOnClickListener(this);
		ib_to_date.setOnClickListener(this);
		mobile1.addTextChangedListener(this);
		total_days.addTextChangedListener(this);
		bike_categ.setOnItemSelectedListener(this);
		cam_img.buildDrawingCache();
		bm = cam_img.getDrawingCache();

		/**Spinner for category*/
		vehicle_cat_adapter = new ArrayAdapter<String>(this, R.layout.custom_dropdown, vehicle_category);
		bike_categ.setAdapter(vehicle_cat_adapter);
	}

	public void callDateTimePicker(int val) {
		date_invoker = val;
		DatePickerDialog.newInstance(this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).
				show(getFragmentManager(), "datePicker");
	}

	/**Save Bookings in Local DB and Update Vehicles status in Local**/
	public void saveBookingsChangeVehicleStateLocal(String n,String m1,String m2,String e,String h,String bike_name,
		String bike_color,String bike_reg_number,String bp,String fd,String to,String td,String tc,String vendor_id,
		String booking_date, String ratingfromcloud,String bk_status,String up_status,String ratingcommentcloud){
		/**Save Bookings in Local DB **/
		ncdh.addContact(new NewCustomerGetSetter(n, m1, m2, e, h, bike_name, bike_color,
				bike_reg_number, bp, fd, to, td, tc, vendor_id, booking_date, ratingfromcloud,
				bk_status, up_status,ratingcommentcloud));
		/** Update Vehicles status in Local**/
		vdb.updateContact(new VehicleGetSetter(bike_reg_number, "P"));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cal_frm_date) {   /**get date picker Dialog**/
			callDateTimePicker(1);
		}
		else if (v.getId() == R.id.cal_to_date) {    /**get date picker Dialog**/
			callDateTimePicker(2);
		}
		else if (v.getId() == R.id.cust_save) {
			int check = getAllStringFromFields();

			if (check == 1) {
				double check1 = getDateDiffString();
				if (check1 >= 1) {
					saveBookingsChangeVehicleStateLocal(n, mcc + m1, m2, e, h, bike_name, bike_color,
							bike_reg_number, bp, fd, to, td, tc, vendor_id, booking_date, ratingfromcloud,
							bk_status, up_status,ratingcommentcloud);

					/**Call to save Image*/
					sciis.saveImage(STORAGE_DIRECTORY,m1,cam_img);
					am.alertClassClasses("New Customer", "Booking Successful.!", "reloadBookings");
				}
			}
		}
		else if (v.getId() == R.id.cust_photo) {
			/**Request for Capture Image*/
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_REQUEST);
		}
		else if (v.getId() == R.id.plus) {
			/**Increase Rate*/
			String temp = bike_price.getText().toString();
			increase = Integer.parseInt(temp);
			increase = increase + 50;
			if (increase < 10000 && increase > 49)
				bike_price.setText("" + increase);
			else {
				if (increase <= 50)
					increase = increase + 50;
				else
					increase = increase - 50;
				am.SingleButtonAlert("New Customer", "Invalid Price.!", "Ok");
			}
			diffDateSet();
		}
		else if (v.getId() == R.id.minus) {
			/**Decrease Rate*/
			String temp = bike_price.getText().toString();
			increase = Integer.parseInt(temp);
			increase = increase - 50;
			if (increase > 49 && increase < 10000)
				bike_price.setText("" + increase);
			else {
				if (increase <= 50)
					increase = increase + 50;
				else
					increase = increase - 50;
				am.SingleButtonAlert("New Customer", "Invalid Price.!", "Ok");
			}
			diffDateSet();
		}
	}
	/**Hotel List sets in the spinner*/
	public void setHotelAdapterValues() {
		/**Set Hotels in Bookings Spinner List*/
		Resources res1 = getResources();
		htl_adapter = new HotelSpinnerAdapter(NewCustomer.this, R.layout.hotel_spinner, CustomListhotel, res1);
		h_spinner.setAdapter(htl_adapter);
		h_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
				// Get selected row data to show on screen
				// hhotel_id   = ((TextView) v.findViewById(R.id.texthid)).getText().toString();
				hhotel_name = ((TextView) v.findViewById(R.id.texthname)).getText().toString();
				if (hhotel_name.equals("Please select Hotel"))
					hhotel_name = "Null";
				hhotel_place = ((TextView) v.findViewById(R.id.texthplace)).getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				//Toast.makeText(getApplicationContext(), "Select a hotel", Toast.LENGTH_LONG).show();
			}
		});
	}

	/** Call Bike Color Spinner Adapter */
	public void colorAdapterIntermediater() {
		/**Removes Duplicate values*/
		for (int ii = 0; ii < CustomListBikeColor.size() - 1; ii++) {
			if (CustomListBikeColor.get(ii).getBikeName().equals(CustomListBikeColor.get(ii + 1).getBikeName())) {
				CustomListBikeColor.remove(ii);
				ii--;
			}
		}
		bikeadapterobj.setBikeAdapterValues(CustomListBikeColor, coloradapter, cust_bike_color, "color", res);
	}

	/** Call Bike Color Spinner Adapter */
	public void numberAdapterIntermediater() {
		bikeadapterobj.setBikeAdapterValues(CustomListBikeNumber, numberadapter, cust_bike_number, "number", res);
	}

	/** Recieved Bike Name, Bike Color, Bike Number after Selection */
	public void passBikeValueToCustomer(String name, String colr, String bkno) {
		if (bkno.equals("null") || bkno.equals("")) {
			bike_name = "";
			bike_color = "";
			bike_reg_number = "";
			Log.d("Bike Details : ", bike_name + " " + bike_color + " " + bike_reg_number);
		} else {
			bike_name = name;
			bike_color = colr;
			bike_reg_number = bkno;
			Log.d("Bike Details : ", bike_name + " " + bike_color + " " + bike_reg_number);
		}
		//Toast.makeText(getApplicationContext(), bike_name+" "+bike_color+" "+bike_reg_number, Toast.LENGTH_SHORT).show();
	}

	/**Get all values and validation done here*/
	private int getAllStringFromFields() {
		boolean b = false;
		booking_date = df.format(c.getTime());
		mcc = cust_mobile1_code.getText().toString();
		n = name.getText().toString();
		m1 = mobile1.getText().toString();
		m2 = mobile2.getText().toString();
		e = email1.getText().toString();
		h = hhotel_name + " " + hhotel_place;
		//bd=bikedetail.getText().toString();
		bp = bike_price.getText().toString();
		fd = from_date.getText().toString();
		to = to_date.getText().toString();
		td = total_days.getText().toString();
		tc = total_cost.getText().toString();
		//Character.isDigit(m1.charAt(0));
		boolean isDigit = false;
		boolean isDigit1 = false, firstChar = false;
		String remov = null;
		if (m1.length() > 9) {
			char c = m1.charAt(0);
			isDigit = (c >= '7' && c <= '9');
		} else {
			isDigit = false;
		}
		if (m1.length() > 6 && mcc.length() > 1) {
			char c = m1.charAt(0);
			firstChar = mcc.charAt(0) == '+';
			isDigit1 = (c >= '2' && c <= '9');
			remov = mcc.substring(1);
		} else {
			isDigit1 = false;
		}
		int count = 0;
		for (int i = 0; i < e.length(); i++) {
			if (e.charAt(i) == '@')
				count++;
		}
		if (m2.length() < 1){
			m2 = "Nill";
		}

		if (n.length() < 3 || n.length() > 50 || n.charAt(0) == ' ' || (n.contains("  "))) {
			am.SingleButtonAlert("New Customer", "Check Name.!", "Ok");
			b = false;
			return 0;
		} else if (!e.contains("@") || (!e.contains(".co") && !e.contains(".org") && !e.contains(".net") && !e.contains(".com") &&
				!e.contains(".edu") && !e.contains(".info") && !e.contains(".in")) || e.length() < 6 ||
				(e.length() > 50) || e.contains("@.") || e.contains(".@") || e.contains("..") ||
				e.contains("--") || e.contains("__") || e.charAt(0) == ' ' || e.contains("@@") ||
				e.charAt(0) == '@' || e.charAt(0) == '.' || e.charAt(0) == '_' || e.charAt(0) == '-' || count > 1) {
			b = false;
			am.SingleButtonAlert("New Customer", "Check Email.!", "Ok");
			return 0;
		} else if (bike_reg_number.length() < 3 || bike_name.length() < 2 || bike_color.length() < 3) {
			am.SingleButtonAlert("New Customer", "Select bike for Booking.!", "Ok");
			//Log.d("Days or bike or Validation Wrong", td);
			return 0;
		} else if (mcc.lastIndexOf("+91") > 0 || !isDigit) {
			am.SingleButtonAlert("New Customer", "Check Mobile Number.!", "Ok");
			b = false;
			return 0;
		} else if (mcc.lastIndexOf("+91") < 0) {
			if (!firstChar || !isDigit1 || Integer.parseInt(remov) < 1) {
				am.SingleButtonAlert("New Customer", "Check Mobile Number 2.!", "Ok");
				b = false;
				return 0;
			} else
				return 1;
		} else {
			b = true;
			return 1;
		}
	}

	/** Default Date Display in Fields*/
	@SuppressLint("SimpleDateFormat")
	public void defaultDateCal() {
		dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
		timeformat = new SimpleDateFormat("HH:MM", Locale.ENGLISH);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		Log.i("Initial Time", "" + c.getTime());
		from_date.setText(df.format(c.getTime()));
		to_date.setText(df.format(c.getTime()));

		diffDateSet();
	}

	/**Total days setter*/
	private void diffDateSet() {
		DecimalFormat decimal = new DecimalFormat("0.0");
		double day = getDateDiffString();
		total_days.setText("" + decimal.format(day));
		totalrateset(day);
	}

	/**
	 * Find Date difference
	 */
	public Double getDateDiffString() {
		long diff = 0, findfromdatefault = 0;
		Date now, nowdte;
		double difference = 0.0;
		try {
			oldDate = df.parse(from_date.getText().toString());
			newDate = df.parse(to_date.getText().toString());
			oldDate1 = df.parse(df.format(oldDate));
			newDate1 = df.parse(df.format(newDate));
			nowdte = df.parse(df.format(c.getTime()));
			now = df.parse(df.format(nowdte));
			findfromdatefault = oldDate1.getTime() - now.getTime();
			if (findfromdatefault < 0)
				am.alertMessageWrongFromDate();
			else {
				diff = newDate1.getTime() - oldDate1.getTime();
				difference = (double) diff;
				if (difference < 0)
					am.alertMessageWrongToDate();
				else {
					difference = difference / 86400000;
					if (difference <= 1 && difference >= 0) {
						return difference = 1;
					} else if (difference > 1) {
						if(difference>15)
							am.alertMessageWrongToDate();
						else
							return difference;
					} else {
						am.alertMessageWrongToDate();
						return 0.0;
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return difference;
	}

	/**Total Amount setter*/
	private void totalrateset(double day){
		long b_rate = Long.parseLong(bike_price.getText().toString());
		double tot_amt = day * b_rate;
		long tot_amt1=(long)tot_amt;
		total_cost.setText(""+tot_amt1);
	}

	@Override
	public void onDateSet(DatePickerDialog dialog, int yearselected, int monthOfYear, int dayOfMonth) {
		//c.set(yearselected, monthOfYear, dayOfMonth);
		monthOfYear+=1;
		from_to_date_time = new StringBuilder();
		if(monthOfYear<10 && dayOfMonth<10)
			from_to_date_time.append(yearselected).append("-0" + monthOfYear).append("-0"+dayOfMonth);
		else if(monthOfYear<10)
			from_to_date_time.append(yearselected).append("-0"+monthOfYear).append("-"+dayOfMonth);
		else if(dayOfMonth<10)
			from_to_date_time.append(yearselected).append("-"+monthOfYear).append("-0"+dayOfMonth);
		else
			from_to_date_time.append(yearselected).append("-"+monthOfYear).append("-"+dayOfMonth);

		//Toast.makeText(this, from_to_date_time, Toast.LENGTH_LONG).show();
		TimePickerDialog.newInstance(this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
		//update();
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		//c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		//c.set(Calendar.MINUTE, minute);
		if (hourOfDay < 10 && minute < 10)
			from_to_date_time.append(" 0" + hourOfDay).append(":0" + minute);
		else if (hourOfDay < 10)
			from_to_date_time.append(" 0" + hourOfDay).append(":" + minute);
		else if (minute < 10)
			from_to_date_time.append(" " + hourOfDay).append(":0" + minute);
		else
			from_to_date_time.append(" " + hourOfDay).append(":" + minute);
		if (date_invoker == 1){
			from_date.setText(from_to_date_time);
			setTextInRate();
		}
		else if(date_invoker==2) {
			to_date.setText(from_to_date_time);
			diffDateSet();
			callBikeNameAdapter();
		}
	}
	 
	 /** After Capturing Image*/
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            cam_img.setImageBitmap(photo);
	        }
		}
	
	/** All Layout View Initializer */
	private void initializeLayoutViews() {
		custname=(TextView)findViewById(R.id.custname);
		mob1=(TextView)findViewById(R.id.mob1);
		email=(TextView)findViewById(R.id.email);
		bike=(TextView)findViewById(R.id.bike);
		bike_rate=(TextView)findViewById(R.id.bike_rate);
		frm_date=(TextView)findViewById(R.id.frm_date);
		to_dat=(TextView)findViewById(R.id.to_dat);
		text_vendor=(TextView)findViewById(R.id.text_vendor);
		//days=(TextView)findViewById(R.id.days);
		//cost=(TextView)findViewById(R.id.cost);
		
		cust_save=(Button)findViewById(R.id.cust_save);
		cust_photo=(Button)findViewById(R.id.cust_photo);
		plus=(Button)findViewById(R.id.plus);
		minus=(Button)findViewById(R.id.minus);
		
	    cam_img=(ImageView)findViewById(R.id.cam_img);
	    imageView2=(ImageView)findViewById(R.id.imageView2);
		ib_from_date = (ImageButton) findViewById(R.id.cal_frm_date);
		ib_to_date = (ImageButton) findViewById(R.id.cal_to_date);
		
		name=(EditText)findViewById(R.id.cust_name);
		mobile1=(EditText)findViewById(R.id.cust_mobile1);
		mobile2=(EditText)findViewById(R.id.cust_mobile2);
		email1=(EditText)findViewById(R.id.cust_email);
		hotelname=(EditText)findViewById(R.id.cust_hotel1);
		//bikedetail=(EditText)findViewById(R.id.cust_bike);
		bike_price=(EditText)findViewById(R.id.cust_bike_rate);
		from_date=(EditText)findViewById(R.id.cust_frm_date);
		to_date=(EditText)findViewById(R.id.cust_to_date);
		total_days=(EditText)findViewById(R.id.cust_total_days);
		total_cost=(EditText)findViewById(R.id.cust_total_cost);
		cust_mobile1_code=(EditText)findViewById(R.id.cust_mobile1_code);
		
		mySpinner = (Spinner)findViewById(R.id.cust_bike);
		cust_bike_color = (Spinner)findViewById(R.id.cust_bike_color);
		cust_bike_number = (Spinner)findViewById(R.id.cust_bike_number);
		h_spinner = (Spinner)findViewById(R.id.cust_hotel);
		bike_categ=(Spinner)findViewById(R.id.bike_categ);
		
		last_layout=(LinearLayout)findViewById(R.id.last_layout);
	}
	
	/** Set * on Required Fields**/
	private void changeTextViewText() {
		mcc=cust_mobile1_code.getText().toString();
		custname.append(Html.fromHtml(text));
		mob1.append(Html.fromHtml(text));
		email.append(Html.fromHtml(text));
		bike.append(Html.fromHtml(text));
		bike_rate.append(Html.fromHtml(text));
		frm_date.append(Html.fromHtml(text));
		to_dat.append(Html.fromHtml(text));
		//days.append(Html.fromHtml(text));
		//cost.append(Html.fromHtml(text));		
	}
	/** Reloads the Activity once**/
	public void reloadActity() {
		finish();
		Intent samescreen = new Intent(getApplicationContext(), NewCustomer.class);
		startActivity(samescreen);
	}

	/**Auto Fill Name Email while Bookings**/
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		mcc=cust_mobile1_code.getText().toString();
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	@Override
	public void afterTextChanged(Editable s) {
		if (s == mobile1.getEditableText()) {
			boolean firstChar = false, country = false;
			String remov = "";
			if (mcc.length() > 1) {
				firstChar = mcc.charAt(0) == '+';
				remov = mcc.substring(1);
			}
			if (mcc.contains("+91"))
				country = true;

			else if (!mcc.contains("+91") && firstChar && Integer.parseInt(remov) >= 1)
				country = true;
			else
				country = false;

			boolean isDigit, isGreater6;
			String mob1 = "";
			if (s.length() >= 1) {
				char char3 = s.charAt(s.length() - 1);
				char char0 = s.charAt(0);
				isDigit = (char3 >= '0' && char3 <= '9');
				isGreater6 = (char0 >= '7' && char0 <= '9');
				if (isDigit && isGreater6) {
					if (country) {
						if (s.length() == 10 && inCount == 0) {
							inCount = 1;
							mob1 = s.toString();
							if (chk_inrnt.isOnline()) {
								new AsyncGetNameEmailCommentonBookings(NewCustomer.this).execute(cust_mobile1_code.
										getText().toString() + mob1, vendor_id);
								Log.d("Deleting", s.toString());
							} else
								am.SingleButtonAlert("New Customer", "Check Internet.!", "Ok");
						}
						if (s.length() < 10)
							inCount = 0;
					} else {
						mobile1.setText(mob1);
						am.SingleButtonAlert("New Customer", "Fill Country code First.!", "Ok");
					}
				} else {
					mobile1.setText(mob1);
					am.SingleButtonAlert("New Customer", "Fill Correct Mobile.!", "Ok");
				}
			}
		}
		else if (s == total_days.getEditableText()) {
			if(s.length()>0)
				totalrateset(Double.parseDouble(s.toString()));
		}
	}int inCount=0;		//Global Value

	/** Set Name, Mob, Email in Edittext**/
	public void setTextInField(String name1,String mobi,String email_id,float getrating,String feedback){
		ratingfromcloud=""+getrating;
		ratingcommentcloud=feedback;
		if(ncdh.getBookingCountForMobileWithStatusP(mobi)>0){
			mobile1.setText("");
			clearNamEml();
			mobile1.requestFocus();
			am.SingleButtonAlert("New Customer","You already have a booking & " +
					"now you can't book a bike..!",	"Ok");
		}else {
			/** Rating alert for vendor*/
			if(getrating<2.5 && getrating>0.0)
				am.SingleButtonAlert("New Customer",feedback,"Ok");
			/** New customer confirmation for vendor*/
			if (name1.equals("Fail")) {
				clearNamEml();
				name.requestFocus();
			}
			/** Old customer confirmation for vendor*/
			else {
				name.setText(name1);
				String mob = mobi.substring(mobi.length() - 10, mobi.length());
				mobile1.setText(mob);
				email1.setText(email_id);
			}
		}
	}
	/** Clear name and email field function*/
	private void clearNamEml(){
		name.setText("");
		email1.setText("");
	}

	/** After Category selection process handling*/
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		selected_veh_category=parent.getItemAtPosition(position).toString();
		callBikeNameAdapter();
		setTextInRate();
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	/**Call to set Bike Name in Spinner*/
	private void callBikeNameAdapter(){
		bikeadapterobj.setListBikeName(vendor_id,selected_veh_category,from_date.getText().toString(),
				to_date.getText().toString());
		/**Removes Duplicate values*/
		for( int ii=0; ii < CustomListBikeName.size()-1;ii++){
			if(CustomListBikeName.get(ii).getBikeName().equals(CustomListBikeName.get(ii+1).getBikeName())){
				CustomListBikeName.remove(ii);
				ii--;
			}
		}
		bikeadapterobj.setBikeAdapterValues(CustomListBikeName,adapter,mySpinner,"name",res);
	}
	/**Set Text in Rate fields which is got from rate master*/
	public void setTextInRate(){
		String temp_from_date=from_date.getText().toString();
		temp_from_date=temp_from_date.substring (0,10);
		List<RateGetSetter> rate_today=rdbh.getRatesOfaDate(temp_from_date);//
		float dys_count=Float.parseFloat(total_days.getText().toString());
		for (RateGetSetter cn : rate_today){
			String rate=cn.getRate();
			String rate1=cn.getRate1();
			String rate2=cn.getRate2();
			if(selected_veh_category.equals("Small")){
				bike_price.setText(rate);
				//String dy=total_days.getText().toString();
				if(rate.length()<1 || Integer.parseInt(rate)<50)
					rate="50";
				total_cost.setText(""+Integer.parseInt(rate) * (int)dys_count);
			}
			else if(selected_veh_category.equals("Medium")){
				bike_price.setText(rate1);
				//String dy=total_days.getText().toString();
				if(rate1.length()<1 || Integer.parseInt(rate1)<50)
					rate1="50";
				total_cost.setText(""+Integer.parseInt(rate1) * (int)dys_count);
			}
			else if(selected_veh_category.equals("High")){
				bike_price.setText(rate2);
				//String dy=total_days.getText().toString();
				if(rate2.length()<1 || Integer.parseInt(rate2)<50)
					rate2="50";
				total_cost.setText(""+Integer.parseInt(rate2) * (int)dys_count);
				}
			else{
				String rate3="50";
				bike_price.setText(rate3);
				total_cost.setText(""+Integer.parseInt(rate3) * (int)dys_count);
			}
			//Toast.makeText(getApplicationContext(), rate+rate1+rate2, Toast.LENGTH_LONG).show();
		}
	}
}
