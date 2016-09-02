/** Ridio v1.0.1
 * 	Purpose	   : HotelSpinnerAdapter 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/


package helix.ridioandroidstudio;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HotelSpinnerAdapter  extends ArrayAdapter<String>{
	/** Private variables **/
   private Activity activity;
   private ArrayList data;
   public Resources res;
   HotelSpinner tempValues=null;
   LayoutInflater inflater;
    
   /** CustomAdapter Constructor **/
//   public BikeSpinnerAdapter(NewCustomer activitySpinner,int textViewResourceId,   
//                         ArrayList objects,Resources resLocal){
//       super(activitySpinner, textViewResourceId, objects);
//        
//       /********** Take passed values **********/
//       activity = activitySpinner;
//       data     = objects;
//       res      = resLocal;
//   
//       /***********  Layout inflator to call external xml layout () **********************/
//       inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        
//     }
   /** CustomAdapter Constructor to customer entry page **/
   public HotelSpinnerAdapter(NewCustomer activitySpinner,int textViewResourceId,   
           ArrayList customListhotel,Resources resLocal){
		super(activitySpinner, textViewResourceId, customListhotel);
		
		/*** Take passed values **/
		activity = activitySpinner;
		data     = customListhotel;
		res      = resLocal;
		
		/***  Layout inflator to call external xml layout () */
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

   @Override
   public View getDropDownView(int position, View convertView,ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   /** This funtion called for ( data.size() times )**/
   public View getCustomView(int position, View convertView, ViewGroup parent) {

       /** Inflate spinner_rows.xml file for each row ( Defined below ) **/
       View row = inflater.inflate(R.layout.hotel_spinner, parent, false);
        
       /** Get each Model object from Arraylist **/
       tempValues = null;
       tempValues = (HotelSpinner) data.get(position);
        
       //TextView id        = (TextView)row.findViewById(R.id.texthid);
       TextView name          = (TextView)row.findViewById(R.id.texthname);
       TextView place          = (TextView)row.findViewById(R.id.texthplace);
        
       if(position==0){
            
           // Default selected Spinner item 
    	  // id.setText("");
    	   place.setText("");
    	   name.setText("Please select Hotel");
    	   
       }
       else
       {
           // Set values for spinner each row 
    	  // id.setText(tempValues.getHotelId());
    	   place.setText(tempValues.getHotelPlace());
    	   name.setText(tempValues.getHotelName());
          
       }   

       return row;
     }
}