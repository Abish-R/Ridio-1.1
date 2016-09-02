/** Ridio v1.0.1
 * 	Purpose	   : Main Reporter Class Handler
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 */

package helix.ridioandroidstudio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ReportMainClass extends Activity implements OnClickListener{
	Button past_booking,current_booking,projected_revenue,average_occupancy,occupancy_chart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_main_layout);
		
		/**Initialize Views*/
		past_booking=(Button)findViewById(R.id.past_booking);
		current_booking=(Button)findViewById(R.id.current_booking);
		projected_revenue=(Button)findViewById(R.id.projected_revenue);
		average_occupancy=(Button)findViewById(R.id.average_occupancy);
		occupancy_chart=(Button)findViewById(R.id.occupancy_chart);
		past_booking.setOnClickListener(this);
		current_booking.setOnClickListener(this);
		projected_revenue.setOnClickListener(this);
		average_occupancy.setOnClickListener(this);
		occupancy_chart.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()== R.id.past_booking){
			/**Load PastBooking Activity*/
			Intent pastbooking=new Intent(this,ReportPastBooking.class);
			pastbooking.putExtra("intent_is", "pastbooking");
			startActivity(pastbooking);
		}
		if(v.getId()== R.id.current_booking){
			/**Load Currentbooking Activity*/
			Intent currentbooking=new Intent(this,ReportPastBooking.class);
			currentbooking.putExtra("intent_is", "currentbooking");
			startActivity(currentbooking);
		}
		if(v.getId()== R.id.projected_revenue){
			/**Load ReportRevenueBasedOnBikes Activity*/
			Intent projectedrevenue=new Intent(this,ReportFutureBooking.class);
			projectedrevenue.putExtra("intent_is", "projectedrevenue");
			startActivity(projectedrevenue);
		}
		if(v.getId()== R.id.average_occupancy){
			/**Load ReportOccupations Activity*/
			Intent occupationreport=new Intent(this,ReportOccupations.class);
			occupationreport.putExtra("intent_is", "occupationreport");
			startActivity(occupationreport);
		}
		if(v.getId()== R.id.occupancy_chart){
			/**Load ReportOccupation Chart Activity*/
			Intent occupationchart=new Intent(this,ReportOccupations.class);
			occupationchart.putExtra("intent_is", "occupationchart");
			startActivity(occupationchart);
		}
	}
	/**Activity Closed and load MainScreen*/
	@Override
	public void onBackPressed() {
		finish();
		Intent callmainpage=new Intent(this,MainScreen.class);
		startActivity(callmainpage);
	    return;
	}

}
