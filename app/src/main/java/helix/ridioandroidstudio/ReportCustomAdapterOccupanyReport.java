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
import android.widget.TextView;
import helix.ridioandroidstudio.ReportCustomAdapterPastCurrent.Holder;

public class ReportCustomAdapterOccupanyReport extends BaseAdapter{ 
	List<String> bk_percent=new ArrayList<String>();
	List<String> bk_total=new ArrayList<String>();
	List<String> mnth_=new ArrayList<String>();
	List<String> mnth_days=new ArrayList<String>();
	List<String> what_month=new ArrayList<String>();
	List<String> tl_bikes=new ArrayList<String>();
	List<String> tl_revenue=new ArrayList<String>();

    Context context;
      private static LayoutInflater inflater=null;

    /**Constructor from OccupationReport*/
	public ReportCustomAdapterOccupanyReport(ReportOccupations reportOccupations, List<String> bk_percent2,
			List<String> bk_total2, List<String> mnth, List<String> mnth_days2, List<String> what_month2, 
			List<String> tl_bikes2,List<String> tl_reven) {
		bk_percent=bk_percent2;
		bk_total=bk_total2;
		mnth_=mnth;
		mnth_days=mnth_days2;
		what_month=what_month2;
		tl_bikes=tl_bikes2;
		tl_revenue=tl_reven;
		context=reportOccupations;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
    public int getCount() {
        return bk_percent.size();
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
        TextView tv,tv1,tv2,tv3,tv4,tv5,tv6;
    }
    
    /**Executed for number of datas present*/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	final Object[] id_obj=bk_percent.toArray();
    	Object[] name_obj=bk_total.toArray();
    	Object[] mb_obj=mnth_.toArray();
    	Object[] mb_obj1=mnth_days.toArray();    	
    	Object[] eml_obj=what_month.toArray();
    	Object[] bk_obj=tl_bikes.toArray();
    	Object[] bk_rev=tl_revenue.toArray();
        Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.report_occupation_custom_list, null);
             if(position%2==0)
            	 rowView.setBackgroundColor(Color.argb(255, 249, 222, 170));
             else
            	 rowView.setBackgroundColor(Color.argb(255,254,253,251));
             holder.tv=(TextView) rowView.findViewById(R.id._month);
             holder.tv1=(TextView) rowView.findViewById(R.id.mnth_days);
             holder.tv2=(TextView) rowView.findViewById(R.id.mnth_days1);
             holder.tv3=(TextView) rowView.findViewById(R.id.tl_bik);             
             holder.tv4=(TextView) rowView.findViewById(R.id.bk_tot);
             holder.tv5=(TextView) rowView.findViewById(R.id.percent);
             holder.tv6=(TextView) rowView.findViewById(R.id.revenue);
             
             holder.tv.setText((CharSequence) eml_obj[position]);
             holder.tv1.setText((CharSequence) mb_obj[position]);
             holder.tv2.setText((CharSequence) mb_obj1[position]);
             holder.tv3.setText((CharSequence) bk_obj[position]);
             holder.tv4.setText((CharSequence) name_obj[position]);
             holder.tv5.setText((CharSequence) id_obj[position]);
             holder.tv6.setText((CharSequence) bk_rev[position]);
         rowView.setOnClickListener(new OnClickListener() {            
         @Override
          public void onClick(View v) {
                //Toast.makeText(context, "You Clicked "+id_obj[position], Toast.LENGTH_LONG).show();
            }
        });   
        return rowView;
    }
}
