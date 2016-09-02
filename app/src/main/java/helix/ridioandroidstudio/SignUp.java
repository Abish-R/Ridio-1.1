/** Ridio v1.0.1
 * 	Purpose	   : Sign Up for new Vendors 
 *  Created by : Abish 
 *  Created Dt : ols file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SignUp extends Activity implements OnItemSelectedListener {
	/**Global Class Variables*/
	TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9,
		textView10,textView11,textView12,textView13,textView14,textView15;
	EditText f_name,l_name,uname,pass,dob,phone,mobile,email,address,city,state,country,zipcode,plan_id;
	ImageButton img_dob;
	Spinner gender;
	ArrayAdapter<String> gender_adapter;
	Button ok;
	String text = "<font color=#cc0029>*</font>";
	String f_nam=null,l_nam=null,unam,pas,gende=null,dobb=null,phon=null,mobil,emai,addres=null,cit=null,
			stat=null,countr=null,zipcod=null,plan_i=null;
	String[] gender_list={"Male","Female"};
	private int day,month,year,hour,minute;
	Calendar c = Calendar.getInstance();
	SimpleDateFormat df;
	int funValue=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initializeAllViews();
		changeTextViewText();
		defaultDateCal();
		
		ok.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				funValue=getAllValues();
				if(funValue==1){
					if(isOnline()){
						/**Sign Up procedure call*/
						new AsyncSignUpClass(SignUp.this).execute(f_nam,l_nam,unam,pas,gende,dobb,phon,mobil,
								emai,addres,cit,stat,countr,zipcod,plan_i,df.format(c.getTime()));
					}
					else
						alertInternet();
				}
			}
		});
		img_dob.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
		
		gender_adapter = new ArrayAdapter<String>(this,R.layout.custom_dropdown, gender_list);
		gender.setAdapter(gender_adapter);
		gender.setOnItemSelectedListener(this);
	}
	/**Gets the gender from the list*/
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		gende=parent.getItemAtPosition(position).toString();
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	/**Once sign Up done takes you to the login Screen*/
	public void startLogin(){
		alertBeforeSave("SignUp Success.!");
		Intent gologin=new Intent(this,Ridiologin.class);
		startActivity(gologin);
		finish();
	}
	/**Get all the values I entered in editext and validation */
	private int getAllValues() {
		f_nam=f_name.getText().toString();
		l_nam=l_name.getText().toString();
		unam=uname.getText().toString();
		pas=pass.getText().toString();
		//gende=gender.getText().toString();
		dobb=dob.getText().toString();
		phon=phone.getText().toString();
		mobil=mobile.getText().toString();
		emai=email.getText().toString();
		addres=address.getText().toString();
		cit=city.getText().toString();
		stat=state.getText().toString();
		countr=country.getText().toString();
		zipcod=zipcode.getText().toString();
		plan_i=plan_id.getText().toString();
		
		boolean isDigit=false;boolean isDigit1=false;
		if(mobil.length()>9){
			char c = mobil.charAt(0);
		 	isDigit = (c >= '7' && c <= '9');
		}else{
			isDigit=false;
		}
		if(phon.length()>4){
			char c = phon.charAt(0);
		 	isDigit1 = (c >= '2' && c <= '9');
		}else{
			isDigit1=false;
		}
		int count=0;
		for(int i=0;i<emai.length();i++){
			if(emai.charAt(i)=='@')
				count++;
		}
		
//		if(f_nam.length()<2 || f_nam.equals("  ") || (f_nam.charAt(0)==' ')){
//			alertBeforeSave("Check First Name");
//			return 0;
//		}
//		else if(l_nam.length()<1 || l_nam.equals("  ") || (l_nam.charAt(0)==' ')){
//			alertBeforeSave("Check Last Name");
//			return 0;
//		}
//		else 
		if(unam.length()<3 || unam.equals(" ")){
			alertBeforeSave("Check Username");
			return 0;
		}
		else if(pas.length()<3 || pas.equals(" ")){
			alertBeforeSave("Check Password");
			return 0;
		}
//		else if(gende.length()<1){
//			alertBeforeSave("Check Gender");
//			return 0;
//		}
//		else if(dobb.length()<10 || dobb.equals(" ") || getDateDiffString()<1){
//			alertBeforeSave("Check Date of Birth");
//			return 0;
//		}
//		else if(phon.length()<5 || !isDigit1){
//			alertBeforeSave("Check Phone Number");
//			return 0;
//		}
		else if(mobil.length()<10 || !isDigit){
			alertBeforeSave("Check Mobile Number");
			return 0;
		}
		else if(!emai.contains("@") || (!emai.contains(".co") && !emai.contains(".com") && !emai.contains(".org") && !emai.contains(".net") && 
				!emai.contains(".edu") && !emai.contains(".info") && !emai.contains(".in")) || emai.length()<6 || 
				(emai.length()>50) || emai.contains("@@") ||  emai.contains("@.") || emai.contains(".@") || 
				emai.contains("..") || emai.contains("--") || emai.contains("__") || emai.charAt(0)=='@' || 
				emai.charAt(0)=='.' || emai.charAt(0)=='_' || emai.charAt(0)=='-' || count>1){
			alertBeforeSave("Check Email ID");
			return 0;
		}
//		else if(addres.length()<5 || addres.equals("  ") || (addres.charAt(0)==' ') || 
//				(addres.charAt(0)=='/') || (addres.charAt(0)=='-') || addres.contains("--")
//				 || addres.contains("//")){
//			alertBeforeSave("Check Address");
//			return 0;
//		}
//		else if(cit.length()<3){
//			alertBeforeSave("Check City");
//			return 0;
//		}
//		else if(stat.length()<3){
//			alertBeforeSave("Check State");
//			return 0;
//		}
//		else if(countr.length()<4){
//			alertBeforeSave("Check Country");
//			return 0;
//		}
//		else if(zipcod.length()<5){
//			alertBeforeSave("Check Zipcode");
//			return 0;
//		}
//		else if(plan_i){
//			alertBeforeSave("Check Plan");
//			return 0;
//		}
		else		
			return 1;
	}
	/**View initialization*/
	private void initializeAllViews() {
//		textView1=(TextView)findViewById(R.id.textView1);
//		textView2=(TextView)findViewById(R.id.textView2);
//		textView3=(TextView)findViewById(R.id.textView3);
//		textView4=(TextView)findViewById(R.id.textView4);
//		textView5=(TextView)findViewById(R.id.textView5);
//		textView6=(TextView)findViewById(R.id.textView6);
//		textView7=(TextView)findViewById(R.id.textView7);
//		textView8=(TextView)findViewById(R.id.textView8);
//		textView9=(TextView)findViewById(R.id.textView9);
//		textView10=(TextView)findViewById(R.id.textView10);
//		textView11=(TextView)findViewById(R.id.textView11);
//		textView12=(TextView)findViewById(R.id.textView12);
//		textView13=(TextView)findViewById(R.id.textView13);
//		textView14=(TextView)findViewById(R.id.textView14);
//		textView15=(TextView)findViewById(R.id.textView15);
		
		f_name=(EditText)findViewById(R.id.f_name);
		l_name=(EditText)findViewById(R.id.l_name);
		uname=(EditText)findViewById(R.id.uname);
		pass=(EditText)findViewById(R.id.pass);
		//gender=(EditText)findViewById(R.id.gender);
		dob=(EditText)findViewById(R.id.dob);
		phone=(EditText)findViewById(R.id.phone);
		mobile=(EditText)findViewById(R.id.mobile);
		email=(EditText)findViewById(R.id.email);
		address=(EditText)findViewById(R.id.address);
		city=(EditText)findViewById(R.id.city);
		state=(EditText)findViewById(R.id.state);
		country=(EditText)findViewById(R.id.country);
		zipcode=(EditText)findViewById(R.id.zipcode);
		plan_id=(EditText)findViewById(R.id.plan_id);
		
		gender=(Spinner)findViewById(R.id.gender);
		ok=(Button)findViewById(R.id.ok);
		img_dob=(ImageButton)findViewById(R.id.img_dob);
	}
	/**Change textField with '*' */
	private void changeTextViewText() {
		//textView1.append(Html.fromHtml(text));
		//textView2.append(Html.fromHtml(text));
		//textView3.append(Html.fromHtml(text));
		//textView4.append(Html.fromHtml(text));
		//textView5.append(Html.fromHtml(text));
		//textView6.append(Html.fromHtml(text));
    	//textView8.append(Html.fromHtml(text));
    	//textView9.append(Html.fromHtml(text));
    	//textView10.append(Html.fromHtml(text));
		//textView11.append(Html.fromHtml(text));
		//textView12.append(Html.fromHtml(text));
    	//textView13.append(Html.fromHtml(text));
    	//textView14.append(Html.fromHtml(text));	
	}
	/**Default Date Sets*/
	@SuppressLint("SimpleDateFormat")
	public void defaultDateCal(){		  
		year  = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day   = c.get(Calendar.DAY_OF_MONTH);
		hour  = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		getDateDiffString();
	}
	/**Date Difference Calculation*/
	public Long getDateDiffString(){
		String today=df.format(c.getTime());
		long diff=0;
		Date inputDate,nowdate;
		try {
			inputDate=df.parse(dob.getText().toString());
			inputDate=df.parse(df.format(inputDate));
			nowdate=df.parse(today);
			nowdate=df.parse(df.format(nowdate));
			diff=nowdate.getTime()-inputDate.getTime();
			diff=diff/86400000;
			if(diff >= 1)
				diff=diff;
			else{
				alertWrongDate();
				diff=0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diff;
	}
	/**Alert Messages*/
	 private void alertWrongDate() {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);
		 alertDialogBuilder.setTitle("Sign Up");
		 alertDialogBuilder.setMessage("Check Date of birth..!");
		 // set positive button: Yes message
		 alertDialogBuilder.setPositiveButton("Reset",new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog,int id) {
				 showDialog(0);
			 }						
		 });
		 AlertDialog alertDialog = alertDialogBuilder.create();
		 alertDialog.show();
	 }	
	 private void alertBeforeSave(String val) {
		 final String val1=val;
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);
         alertDialogBuilder.setTitle("SignUp Details");
         alertDialogBuilder.setMessage(val1+" .!");
         // set positive button: Yes message
         alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        	 public void onClick(DialogInterface dialog,int id) {
        		 Log.d("Wrong While saving ", val1);
        	 }						
         });
         AlertDialog alertDialog = alertDialogBuilder.create();
         alertDialog.show();		
	 }	
	 private void alertInternet() {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);
         alertDialogBuilder.setTitle("Need Hotels.?");
         alertDialogBuilder.setMessage("Check Internet.!");
         // set positive button: Yes message
         alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        	 public void onClick(DialogInterface dialog,int id) {
        	 }						
         });
         AlertDialog alertDialog = alertDialogBuilder.create();
         alertDialog.show();
	 }
	
	/** Date picker action listeners*/
	 @Override
	 @Deprecated
	 protected Dialog onCreateDialog(int id) {
		 if(id==0)
			 return new DatePickerDialog(this, datePickerListener, year, month, day);
		 return new DatePickerDialog(this, datePickerListener, 0, 0, 0);
	 }
	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		 public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			 if(selectedMonth<10 && selectedDay<10)
				 dob.setText(selectedYear + "-0" + (selectedMonth + 1) + "-0"  + selectedDay);
			 else if(selectedMonth<10)
				 dob.setText(selectedYear + "-0" + (selectedMonth + 1) + "-"  + selectedDay);
			 else if(selectedDay<10)
				 dob.setText(selectedYear + "-" + (selectedMonth + 1) + "-0"  + selectedDay);
			 getDateDiffString();
		 }
	 };
	/**Check Internet*/
	public boolean isOnline() {
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) 
                for (int i = 0; i < info.length; i++) 
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
        }
        return false;
	}
}
