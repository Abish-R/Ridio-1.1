/** Ridio v1.0.1
 * 	Purpose	   : Return Vehicle CustomAdapter 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 26-02-2016 (coments, image retrieval)
 *  Verified by:
 *  Verified Dt: 
 * **/

package helix.ridioandroidstudio;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.SyncStateContract.Constants;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ReturnVehicleCustomAdapter extends BaseAdapter implements Filterable{ 
	/**Class variables*/
	private Activity context;
	private ValueFilter valueFilter;
	ArrayList<ReturnVehicleListValueGetSet> veh_list = new ArrayList<ReturnVehicleListValueGetSet>();
	ArrayList<ReturnVehicleListValueGetSet> filter_list = new ArrayList<ReturnVehicleListValueGetSet>();
	private LayoutInflater inflater=null;
	      
	public ReturnVehicleCustomAdapter(Activity contxt,ArrayList<ReturnVehicleListValueGetSet> vehlist) {
			super();
			this.context=contxt;
			this.veh_list=vehlist;
			filter_list=vehlist;
			inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			getFilter();
		}
	@Override
	public int getCount() {
		return veh_list.size();
	}
	
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		TextView tv,tv1,tv2;
	}
	
	/**Called this for data.length() times*/
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder1;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.return_custom_listview, null);
	            
			/** Creates a ViewHolder and store references to the two children to bind data to.*/
			holder1=new ViewHolder();
			convertView.setTag(holder1);
			holder1.tv=(TextView) convertView.findViewById(R.id.r_name);
			holder1.tv1=(TextView) convertView.findViewById(R.id.r_mobile);
			holder1.tv2=(TextView) convertView.findViewById(R.id.r_bike);
	        }
		else {
			/** Get the ViewHolder back to get fast access to the TextView and the ImageView.*/
			holder1 = (ViewHolder) convertView.getTag();
		}
        if(position%2==0)
        	convertView.setBackgroundColor(Color.argb(255, 249, 222, 170));
        else
           	convertView.setBackgroundColor(Color.argb(255,254,253,251)); 
		holder1.tv.setText(veh_list.get(position).getMob());
		holder1.tv1.setText(veh_list.get(position).getName());
		holder1.tv2.setText(veh_list.get(position).getVehNum());
	             
		convertView.setOnClickListener(new OnClickListener() {            
			@Override
			public void onClick(View v) {
				/**Gets value from the list*/
				String dy_rate,tl_days;
				((ReturnVehicle)context).name_=filter_list.get(position).getName();       	
				((ReturnVehicle)context).mobile_=filter_list.get(position).getMob();
				((ReturnVehicle)context).getCustomerPhoto(filter_list.get(position).getMob());
				((ReturnVehicle)context).bike1=filter_list.get(position).getVehNum();
				((ReturnVehicle)context).from_date1=filter_list.get(position).getFromDt();
				((ReturnVehicle)context).to_date1=filter_list.get(position).getToDt();
				dy_rate=filter_list.get(position).getDayRate();
				((ReturnVehicle)context).days1=filter_list.get(position).getDys();
				((ReturnVehicle)context).amount1=filter_list.get(position).getAmt();
				/**Sets value to the edittext fields*/
				((ReturnVehicle)context).name.setText(((ReturnVehicle)context).name_);
				((ReturnVehicle)context).mobile.setText(((ReturnVehicle)context).mobile_);
	            ((ReturnVehicle)context).bike.setText(((ReturnVehicle)context).bike1);
	            ((ReturnVehicle)context).from.setText(((ReturnVehicle)context).from_date1);
	            ((ReturnVehicle)context).eto.setText(((ReturnVehicle)context).to_date1);
	            ((ReturnVehicle)context).tdays.setText(((ReturnVehicle)context).days1);
				tl_days=filter_list.get(position).getDys();
	            ((ReturnVehicle)context).price.setText(((ReturnVehicle)context).amount1);
	            ((ReturnVehicle)context).extend.setText(((ReturnVehicle)context).df.format(((ReturnVehicle)context).c.getTime()).toString());
	            ((ReturnVehicle)context).diffDateSet(dy_rate,tl_days);
	            //Toast.makeText(context, "You Clicked "+cust_mobile_obj[position], Toast.LENGTH_LONG).show();
			}
		});   
		return convertView;
	}
	
	/**Filter funtion for listView*/
	@Override
	public Filter getFilter() {
		if(valueFilter==null) {
			valueFilter=new ValueFilter();
		}
		return valueFilter;
	}
	/**This class do all the filters*/
	private class ValueFilter extends Filter {
		/**Invoked in a worker thread to filter the data according to the constraint.*/
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results=new FilterResults();
			if(constraint!=null && constraint.length()>0){
				ArrayList<ReturnVehicleListValueGetSet> filterList=new ArrayList<ReturnVehicleListValueGetSet>();
				for(int i=0;i<filter_list.size();i++){
					if((filter_list.get(i).getMob().toUpperCase()).contains(constraint.toString().toUpperCase())) {
						ReturnVehicleListValueGetSet contacts = new ReturnVehicleListValueGetSet();
						contacts.setName(filter_list.get(i).getName());
						contacts.setMob(filter_list.get(i).getMob());
						contacts.setVehNum(filter_list.get(i).getVehNum());
						filterList.add(contacts);
					}
				}
				results.count=filterList.size();
				results.values=filterList;
			}else{
				results.count=filter_list.size();
				results.values=filter_list;
			}
			return results;
		}

		/**Invoked in the UI thread to publish the filtering results in the user interface.*/
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) {
			veh_list=(ArrayList<ReturnVehicleListValueGetSet>) results.values;
			notifyDataSetChanged();
		}
	}
}