/** Ridio v1.0.1
 *  Purpose	   : HotelCustomAdapter to get view 
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import helix.ridioandroidstudio.RateCustomAdapter.Holder;
import helix.ridioandroidstudio.ReturnVehicleCustomAdapter.ViewHolder;

public class HotelCustomAdapter extends ArrayAdapter<HotelGetSetter>{ 
	HotelGetSetter hgs=new HotelGetSetter(this);
	ArrayList<HotelGetSetter> hotelList = new ArrayList<HotelGetSetter>();
    List<String> h_id=new ArrayList<String>();
	List<String> h_name=new ArrayList<String>();
	List<String> h_place=new ArrayList<String>();
	List<String> h_date=new ArrayList<String>();
    Context context;
    private static LayoutInflater inflater=null;
      
      /***  CustomAdapter Constructor, initializer for Hotel Master entry **/
//	public HotelCustomAdapter(HotelMaster hotelAdd1, List<String> hotel_id1, List<String> hotel_name1,
//			List<String> hotel_place1, List<String> h_created_date1) {
//		super(context, textViewResourceId, countryList);
//		h_id=hotel_id1;
//    	h_name=hotel_name1;
//    	h_place=hotel_place1;
//    	h_date=h_created_date1;
//        context=hotelAdd1;
//        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
//	
//	/***  CustomAdapter Constructor, initializer for Hotel Master entry **/
//	public HotelCustomAdapter(HotelMaster hotelAdd1, List<String> hotel_id1, List<String> hotel_name1,
//			List<String> hotel_place1) {
//		h_id=hotel_id1;
//    	h_name=hotel_name1;
//    	h_place=hotel_place1;
//        context=hotelAdd1;
//        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
//	
	public HotelCustomAdapter(HotelMaster hotelMaster, int textViewResourceId,ArrayList<HotelGetSetter> hotelLists) {
		super(hotelMaster,textViewResourceId, hotelLists);
		hotelList=hotelLists;
		context=hotelMaster;
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

//	public HotelCustomAdapter(HotelMaster hotelMaster) {
//		context=hotelMaster;
//	}

	@Override
    public int getCount() {
        return hotelList.size();
    }

//    @Override
//    public Object getItem(int position) {
//        return position;
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public class ViewHolder{
       // TextView tv;
        TextView tv1,tv2;
        CheckBox check_status;
        //TextView tv3;
    }
    
    /***  Place the values from json into the listview **/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	final Object[] id_obj=h_id.toArray();
    	Object[] name_obj=h_name.toArray();
    	Object[] place_obj=h_place.toArray();
    	//Object[] date_obj=h_date.toArray();
        final ViewHolder holder;
        if (convertView == null) {
			convertView = inflater.inflate(R.layout.hotel_custom_listview, null);
	            
			/** Creates a ViewHolder and store references to the two children to bind data to.*/
			holder=new ViewHolder();
			convertView.setTag(holder);
			holder.tv1=(TextView) convertView.findViewById(R.id.h_place);
            holder.tv2=(TextView) convertView.findViewById(R.id.h_name);
            holder.check_status=(CheckBox) convertView.findViewById(R.id.check_status);
            
            holder.check_status.setOnClickListener( new View.OnClickListener() {  
                public void onClick(View v) {  
                  CheckBox cb = (CheckBox) v ;  
                  HotelGetSetter planet = (HotelGetSetter) cb.getTag(); 
                  Log.d("Check Selected : ",cb.getText()+":"+ cb.isChecked());
                  planet.setSelected( cb.isChecked() );  
                }  
              });
	        } 
        else {
			/** Get the ViewHolder back to get fast access to the TextView and the ImageView.*/
			holder = (ViewHolder) convertView.getTag();		
        }
        if(position%2==0)
        	convertView.setBackgroundColor(Color.argb(255, 249, 222, 170));
        else
        	convertView.setBackgroundColor(Color.argb(255,254,253,251));
        HotelGetSetter hgs=hotelList.get(position);
        holder.tv1.setText(hotelList.get(position).getPlace());
        holder.tv2.setText(hotelList.get(position).getName());
        holder.check_status.setChecked((hotelList.get(position).isSelected()));
        //holder.tv3.setText((CharSequence) date_obj[position]);
        holder.check_status.setTag(hgs);
        
        convertView.setOnClickListener(new OnClickListener() {            
        	@Override
        	public void onClick(View v) {
        	}
        });   
        return convertView;
    }
}