/** Ridio v1.0.1
 * 	Purpose	   : Report Custom Adapter for Revenue/FutureBooking
 * /Refer ReportCustomAdapterRevenueDisplay(not using)
 *  Created by : Abish 
 *  Created Dt : 02/02/2016
 *  Modified on: 
 *  Verified by: 
 *  Verified Dt: 
 * **/

package helix.ridioandroidstudio;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ReportCustomAdapterFutureBooking extends BaseAdapter{ 
    List<String> _id,c_name1,m_no1,email1,b_no1,day_rate1,from1,to1=new ArrayList<String>();
	List<String> days1,cost1,v_id1,book_date1,rating1=new ArrayList<String>();
    Context context;
    private static LayoutInflater inflater=null;
    
    /**Constructor intialized from RevenueBasedOnBikes*/
	public ReportCustomAdapterFutureBooking(ReportRevenueBasedOnBikes reportPastBooking, List<String> id, List<String> _name,
			List<String> _number, List<String> eml, List<String> bike_number, List<String> dy_rt){
		_id=id;
		c_name1=_name;
		m_no1=_number;
		email1=eml;		
		b_no1=bike_number;
		day_rate1=dy_rt;
		context=reportPastBooking;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	 /**Constructor intialized from ReportPastBooking*/
	public ReportCustomAdapterFutureBooking(ReportPastBooking reportPastBooking, List<String> id, List<String> _name,
			List<String> _number, List<String> eml, List<String> bike_number, List<String> dy_rt) {
		_id=id;
		c_name1=_name;
		m_no1=_number;
		email1=eml;		
		b_no1=bike_number;
		day_rate1=dy_rt;
		context=reportPastBooking;
	    inflater = ( LayoutInflater )context.
	            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
    public int getCount() {
        return _id.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv,tv1,tv2,tv3,tv4,tv5;
    }
    
    /**Called data.length() number of times*/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	final Object[] id_obj=_id.toArray();
    	Object[] name_obj=c_name1.toArray();
    	Object[] mb_obj=m_no1.toArray();
    	Object[] eml_obj=email1.toArray();
    	Object[] bk_obj=b_no1.toArray();
    	Object[] dy_rt_obj=day_rate1.toArray();
        Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.report_revenue_custom_list, null);
             if(position%2==0)
            	 rowView.setBackgroundColor(Color.argb(255, 249, 222, 170));
             else
            	 rowView.setBackgroundColor(Color.argb(255,254,253,251));
             //holder.tv=(TextView) rowView.findViewById(R.id.id1);
            // holder.tv1=(TextView) rowView.findViewById(R.id.nm1);
             holder.tv2=(TextView) rowView.findViewById(R.id.td);
             holder.tv3=(TextView) rowView.findViewById(R.id.bd);
             holder.tv4=(TextView) rowView.findViewById(R.id.dp);
             holder.tv5=(TextView) rowView.findViewById(R.id.ta);             
             
	        // holder.tv.setText((CharSequence) id_obj[position]);
	        // holder.tv1.setText((CharSequence) name_obj[position]);
	         holder.tv2.setText((CharSequence) mb_obj[position]);
	         holder.tv3.setText((CharSequence) eml_obj[position]);
	         holder.tv4.setText((CharSequence) bk_obj[position]);
	         holder.tv5.setText((CharSequence) dy_rt_obj[position]);
	         rowView.setOnClickListener(new OnClickListener() {            
         @Override
         public void onClick(View v) {
             //Toast.makeText(context, "You Clicked "+id_obj[position], Toast.LENGTH_LONG).show();
           }
        });   
        return rowView;
    }



}
