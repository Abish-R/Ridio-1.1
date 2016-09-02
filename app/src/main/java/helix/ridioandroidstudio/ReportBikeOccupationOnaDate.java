package helix.ridioandroidstudio;

/** Ridio v1.0.1
 * 	Purpose	   : Report Bike
 *  Created by : Abish
 *  Created Dt : old file (Suresh discussed and modified. So not used)
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 * **/

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ReportBikeOccupationOnaDate extends Activity implements OnClickListener{
	
	NewCustomerDatabaseHandler ncdbh= new NewCustomerDatabaseHandler(this);
	EditText date;
	ImageButton ib_date;
	LinearLayout list_title_layout;
	ListView report_list;
	
	private int day,month,year,hour,minute;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df;
    
    int validation=0,size=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_bike_occupation_onadate);
		date=(EditText)findViewById(R.id.date);
		ib_date=(ImageButton)findViewById(R.id.ib_date);
		list_title_layout=(LinearLayout)findViewById(R.id.list_title_layout);
		report_list=(ListView)findViewById(R.id.report_list);
		
		df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		defaultDateCal();
		
		ib_date.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		list_title_layout.setVisibility(v.VISIBLE);
	}
	/**Default date setting*/
	public void defaultDateCal(){		  
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        hour  = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
		date.setText(df.format(c.getTime()));		
		compareDate();
	}
	
	private Long compareDate() {
		long diff=0,findfromdatefault=0;
		Date now,newDate;
		try {
			newDate = df.parse(date.getText().toString());
			newDate=df.parse(df.format(newDate));
			now = df.parse(df.format(c.getTime()));
			now=df.parse(df.format(now));
			diff=newDate.getTime()-now.getTime();
			diff=diff/86400000;
			if(diff > 0){
				alertMessageWrongDate("Report Details","Select Correct Date.!");
				diff=0;
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return diff;
	}
	/** On back button pressed close the current Activity **/
	public void onBackPressed() {
	     super.onBackPressed();
	     finish();
	}
	
	/**get not uploaded count from db*/
	private int bringLocalTableCustomerCount() {
		List<NewCustomerGetSetter> cust_list = ncdbh.getAllCustomerNotUploaded();
		 for (NewCustomerGetSetter cn : cust_list) {
			 size++;
			 //c_id.add(cn.getID());//[count]=cn.getID();
		 }
		 return size;
	}
	
	private void alertMessageWrongDate(String title,String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportBikeOccupationOnaDate.this);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg);
		// set positive button: Yes message
		alertDialogBuilder.setPositiveButton("Reset",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				showDialog(1);
			}						
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
