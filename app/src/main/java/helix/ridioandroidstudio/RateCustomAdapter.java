/** Ridio v1.0.1
 * 	Purpose	   : RateCustomAdapter Rate List Display
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 02-03-2016 (Rate Master Edit)
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RateCustomAdapter extends BaseAdapter{ 
	
	/**Class Variables*/
    List<Integer> id1=new ArrayList<Integer>();
    List<String> rate=new ArrayList<String>();
	List<String> rate1=new ArrayList<String>();
	List<String> rate2=new ArrayList<String>();
	List<String> date1=new ArrayList<String>();
    Context context;
    private static LayoutInflater inflater=null;
      
    /**Empty Constructor*/
    public RateCustomAdapter(RateMasterClass mainActivity, int[] id, String[] cate, String[] rate, String[] date) {
        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    /**RateMaster Constructor*/
    public RateCustomAdapter(RateMasterClass rt_mster, List<Integer> id,
    		List<String> rat,List<String> rat1,List<String> rat2, List<String> date) {
    	id1=id;
        rate=rat;
        rate1=rat1;
        rate2=rat2;
        date1=date;
        context=rt_mster;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
    public int getCount() {
        return rate1.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView tv,tv1,tv2,tv3,tv4;
    }
    
    /**Called for Each row display in Rate Master*/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	final Object[] id_obj=id1.toArray();
        final Object[] rate_obj=rate.toArray();
        final Object[] rate_obj1=rate1.toArray();
        final Object[] rate_obj2=rate2.toArray();
        final Object[] date_obj=date1.toArray();
        Holder holder=new Holder();
        View rowView;         
             rowView = inflater.inflate(R.layout.rate_custom_list, null);
             if(position%2==0)
               	 rowView.setBackgroundColor(Color.argb(255, 249, 222, 170));
                else
               	 rowView.setBackgroundColor(Color.argb(255,254,253,251));
             
             holder.tv=(TextView) rowView.findViewById(R.id.id_rate);
             holder.tv1=(TextView) rowView.findViewById(R.id.id_rate_);
             holder.tv2=(TextView) rowView.findViewById(R.id.id_rate_1);
             holder.tv3=(TextView) rowView.findViewById(R.id.id_rate_2);
             holder.tv4=(TextView) rowView.findViewById(R.id.id_rate_date);
             id1.toString();
         holder.tv.setText(""+id_obj[position]);
         holder.tv1.setText((CharSequence) rate_obj[position]);
         holder.tv2.setText((CharSequence) rate_obj1[position]);
         holder.tv3.setText((CharSequence) rate_obj2[position]);
         holder.tv4.setText((CharSequence) date_obj[position]);
             
         rowView.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                ((RateMasterClass)context).editRateList(rate_obj[position].toString(),
                        rate_obj1[position].toString(),rate_obj2[position].toString(),date_obj[position].toString());
            }
        });   
        return rowView;
    }

}