/** Ridio v1.0.1
 * 	Purpose	   : VehicleCustomAdapter used in Vehicle master for display List 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 20-02-2016 (Edit Vehicles)
 *  Verified by:
 *  Verified Dt:
 * **/


package helix.ridioandroidstudio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleCustomAdapter extends BaseAdapter{ 
	/**CLass Variables*/
	List<Integer> id_=new ArrayList<Integer>();
    List<String> categ_=new ArrayList<String>();
	List<String> color_=new ArrayList<String>();
	List<String> number_=new ArrayList<String>();
	List<String> date_=new ArrayList<String>();
	List<String> model_year_=new ArrayList<String>();
	List<String> pending_=new ArrayList<String>();
	List<String> pst_issues_=new ArrayList<String>();
	List<String> purchase_date_=new ArrayList<String>();
	List<String> vehicle_imagePath=new ArrayList<String>();
    Context context;
    private static LayoutInflater inflater=null;
    
    /**Constructor related to Vehicle master class*/
	public VehicleCustomAdapter(VehicleMasterClass vehicleMasterClass, List<Integer> vehicle_id_,
                                List<String> vehicle_categ_,List<String> vehicle_color_, List<String> vehicle_number_, List<String> vehicle_model_year_,
			List<String> service_date_, List<String> vehicle_purchase_date_,List<String> repair_pending_,
			List<String> past_repair_issues_,List<String> imagepath) {
		id_=vehicle_id_;
        categ_=vehicle_categ_;
		color_=vehicle_color_;
		number_=vehicle_number_;
		date_=service_date_;
		model_year_=vehicle_model_year_;
		pending_=repair_pending_;
		pst_issues_=past_repair_issues_;
		purchase_date_=vehicle_purchase_date_;
		vehicle_imagePath=imagepath;
		context=vehicleMasterClass;
		inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
    public int getCount() {
        return id_.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder1{
        //TextView tv;
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
        ImageView image;
    }
    
    /**This function called data.length times*/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	//final Object[] id_obj=id_.toArray();
        final Object[] categ_obj=categ_.toArray();
    	final Object[] color_obj=color_.toArray();
        final Object[] number_obj=number_.toArray();
        final Object[] date_obj=date_.toArray();
        final Object[] model_year_obj=model_year_.toArray();
        final Object[] pending_obj=pending_.toArray();
        final Object[] pst_issues_obj=pst_issues_.toArray();
        final Object[] purchase_date_obj=purchase_date_.toArray();
        final Object[] imagelst=vehicle_imagePath.toArray();
        Holder1 holder1=new Holder1();
        View rowView1;       
        if(position==0){
        	//convertView.setBackgroundColor(Color.GRAY);
        	rowView1 = inflater.inflate(R.layout.empty_layout_for_list, null);
        }
        else{        	
             rowView1 = inflater.inflate(R.layout.vehicle_custom_listview, null);
             if(position%2==0)
            	 rowView1.setBackgroundColor(Color.argb(255, 249, 222, 170));
             else
            	 rowView1.setBackgroundColor(Color.argb(255,254,253,251)); 
             holder1.image=(ImageView) rowView1.findViewById(R.id.imagelist);
             //holder1.tv=(TextView) rowView1.findViewById(R.id.textView1);
             holder1.tv1=(TextView) rowView1.findViewById(R.id.textView2);
             holder1.tv2=(TextView) rowView1.findViewById(R.id.textView3);
             holder1.tv3=(TextView) rowView1.findViewById(R.id.textView4);
             holder1.tv4=(TextView) rowView1.findViewById(R.id.textView5);
             holder1.tv5=(TextView) rowView1.findViewById(R.id.textView6);
             holder1.tv6=(TextView) rowView1.findViewById(R.id.textView7);
             holder1.tv7=(TextView) rowView1.findViewById(R.id.textView8);
             id_.toString();
            //String imagelst1=imagelst[position].toString();

            holder1.image.setImageResource(0);
            if(((VehicleMasterClass)context).getCustomerPhoto(imagelst[position].toString())==1)
                holder1.image.setImageBitmap(BitmapFactory.decodeFile((String) imagelst[position]));
            else
                holder1.image.setImageResource(R.drawable.veh_w);
             //holder1.tv.setText(""+id_obj[position]);
             holder1.tv1.setText((CharSequence) color_obj[position]);
             holder1.tv2.setText((CharSequence) number_obj[position]);
             holder1.tv3.setText((CharSequence) model_year_obj[position]);
             holder1.tv4.setText((CharSequence) purchase_date_obj[position]);
             holder1.tv5.setText((CharSequence) date_obj[position]);         
             holder1.tv6.setText((CharSequence) pending_obj[position]);
             holder1.tv7.setText((CharSequence) pst_issues_obj[position]);
        }
        rowView1.setOnClickListener(new OnClickListener() {            
        	@Override
        	public void onClick(View v) {
                //Toast.makeText(context, "You Clicked "+color_obj[position], Toast.LENGTH_LONG).show();
                /**Convert Object to string and pass the selected vehicle to edit*/
                ((VehicleMasterClass)context).callToEditVehicle(categ_obj[position] + "", color_obj[position] + "",
                        number_obj[position] + "", model_year_obj[position] + "", purchase_date_obj[position] + "",
                        date_obj[position] + "", pending_obj[position] + "", pst_issues_obj[position] + "",
                        imagelst[position]+"");
        	}
        });   
        return rowView1;
    }

}