/** Ridio v1.0.1
 * 	Purpose	   : Report Past and Current Custom Adapter Handler
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
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
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ReportCustomAdapterPastCurrent extends BaseAdapter{ 
    List<String> _id=new ArrayList<String>();
	List<String> c_name1=new ArrayList<String>();
	List<String> m_no1=new ArrayList<String>();
	List<String> email1=new ArrayList<String>();
	List<String> b_no1=new ArrayList<String>();
	List<String> day_rate1=new ArrayList<String>();
	List<String> from1=new ArrayList<String>();
	List<String> to1=new ArrayList<String>();
	List<String> days1=new ArrayList<String>();
	List<String> cost1=new ArrayList<String>();
	List<String> v_id1=new ArrayList<String>();
	List<String> book_date1=new ArrayList<String>();
    Context context;
      private static LayoutInflater inflater=null;
    
    /**Constructor from Current and Past Booking*/
	public ReportCustomAdapterPastCurrent(ReportPastBooking reportPastBooking, List<String> id, List<String> _name,
			List<String> _number, List<String> eml, List<String> bike_number, List<String> dy_rt,
			List<String> from, List<String> to,List<String> day, List<String> cost,
			List<String> v_id, List<String> book_dt) {
		_id=id;
		c_name1=_name;
		m_no1=_number;
		email1=eml;		
		b_no1=bike_number;
		day_rate1=dy_rt;
		from1=from;
		to1=to;
		days1=day;
		cost1=cost;
		v_id1=v_id;
		book_date1=book_dt;
		context=reportPastBooking;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
//	public ReportCustomAdapterPastCurrent(ReportPastBooking reportPastBooking, List<String> id, List<String> _name,
//			List<String> _number, List<String> eml, List<String> bike_number, List<String> dy_rt){
//		_id=id;
//		c_name1=_name;
//		m_no1=_number;
//		email1=eml;		
//		b_no1=bike_number;
//		day_rate1=dy_rt;
//		context=reportPastBooking;
//        inflater = ( LayoutInflater )context.
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
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
        TextView tv,tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12;
    }
    
    /**Called for number of data found times*/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	final Object[] id_obj=_id.toArray();
    	Object[] name_obj=c_name1.toArray();
    	Object[] mb_obj=m_no1.toArray();
    	//Object[] eml_obj=email1.toArray();
    	Object[] bk_obj=b_no1.toArray();
    	Object[] dy_rt_obj=day_rate1.toArray();
    	Object[] frm_obj=from1.toArray();
    	Object[] to_obj=to1.toArray();
    	Object[] day_obj=days1.toArray();
    	Object[] cost_obj=cost1.toArray();
    	//Object[] vid_obj=v_id1.toArray();
    	Object[] book_dt_obj=book_date1.toArray();
        Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.report_past_current_custom_list, null);
             if(position%2==0)
            	 rowView.setBackgroundColor(Color.argb(255, 249, 222, 170));
             else
            	 rowView.setBackgroundColor(Color.argb(255,254,253,251));
             holder.tv=(TextView) rowView.findViewById(R.id.id);
             holder.tv1=(TextView) rowView.findViewById(R.id.nm);
             holder.tv2=(TextView) rowView.findViewById(R.id.mob);
             //holder.tv3=(TextView) rowView.findViewById(R.id.eml);
             holder.tv4=(TextView) rowView.findViewById(R.id.bk_no);
             holder.tv5=(TextView) rowView.findViewById(R.id.dy_rt);
             holder.tv6=(TextView) rowView.findViewById(R.id.frm);
             holder.tv7=(TextView) rowView.findViewById(R.id.to);
             holder.tv8=(TextView) rowView.findViewById(R.id.dy);
            // holder.tv9=(TextView) rowView.findViewById(R.id.vid);
             holder.tv10=(TextView) rowView.findViewById(R.id.book_dt);
             holder.tv12=(TextView) rowView.findViewById(R.id.cst);
             
             holder.tv.setText((CharSequence) id_obj[position]);
             holder.tv1.setText((CharSequence) name_obj[position]);
             holder.tv2.setText((CharSequence) mb_obj[position]);
             //holder.tv3.setText((CharSequence) eml_obj[position]);
	         holder.tv4.setText((CharSequence) bk_obj[position]);
	         holder.tv5.setText((CharSequence) dy_rt_obj[position]);
	         holder.tv6.setText((CharSequence) frm_obj[position]);
	         holder.tv7.setText((CharSequence) to_obj[position]);
	         holder.tv8.setText((CharSequence) day_obj[position]);
	        // holder.tv9.setText((CharSequence) vid_obj[position]);
	         holder.tv10.setText((CharSequence) book_dt_obj[position]);
	         holder.tv12.setText((CharSequence) cost_obj[position]);
         rowView.setOnClickListener(new OnClickListener() {            
         @Override
         public void onClick(View v) {
           //Toast.makeText(context, "You Clicked "+id_obj[position], Toast.LENGTH_LONG).show();
         }
         });
         
        return rowView;
    }
}
