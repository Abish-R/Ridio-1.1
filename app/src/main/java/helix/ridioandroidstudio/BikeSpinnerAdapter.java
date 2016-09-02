/** Ridio v1.0.1
 *  Purpose    : BikeSpinnerAdapter View Generator
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
import android.widget.ImageView;
import android.widget.TextView;

public class BikeSpinnerAdapter extends ArrayAdapter<String>{
    
   private Activity activity;
   private ArrayList data;
   public Resources res;
   BikeSpinner tempValues=null;
   LayoutInflater inflate;
    
   /***  CustomAdapter Constructor, initializer for customer entry **/
   public BikeSpinnerAdapter(NewCustomer activitySpinner,int textViewResourceId,ArrayList objects,
                         Resources resLocal){    
       super(activitySpinner, textViewResourceId, objects);
        
       /*** Take passed values ***/
       activity = activitySpinner;
       data     = objects;
       res      = resLocal;
   
       /***  Layout inflator to call external xml layout () **/
       inflate = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
     }
   
   /***  CustomAdapter Constructor , initializer for Past/current booking entry **/
   public BikeSpinnerAdapter(ReportPastBooking activitySpinner, int textViewResourceId,   
           ArrayList objects, Resources resLocal) {
	   super(activitySpinner, textViewResourceId, objects);
       
       /*** Take passed values */
       activity = activitySpinner;
       data     = objects;
       res      = resLocal;
   
       /**  Layout inflator to call external xml layout () **/
       inflate = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }
   
   /***  CustomAdapter Constructor , initializer for ReportRevenueBasedOnBikes booking entry **/
   public BikeSpinnerAdapter(ReportRevenueBasedOnBikes activitySpinner, int textViewResourceId,   
           ArrayList objects, Resources resLocal) {
	 super(activitySpinner, textViewResourceId, objects);
     
     /** Take passed values **/
     activity = activitySpinner;
     data     = objects;
     res      = resLocal;
 
     /**  Layout inflator to call external xml layout () ***/
     inflate = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}

   /***  CustomAdapter Constructor , initializer for ReportsFureBooking booking entry **/
public BikeSpinnerAdapter(ReportFutureBooking reportFutureBooking, int textViewResourceId,
		ArrayList objects, Resources res2) {
	super(reportFutureBooking, textViewResourceId, objects);
	/** Take passed values **/
    activity = reportFutureBooking;
    data     = objects;
    res      = res2;

    /**  Layout inflator to call external xml layout () ***/
    inflate = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}

@Override
   public View getDropDownView(int position, View convertView,ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       return getCustomView(position, convertView, parent);
   }

   /** This funtion called for each row ( Called data.size() times ) */
   public View getCustomView(int position, View convertView, ViewGroup parent) {

       /*** Inflate bike_spinner.xml file for each row ( Defined below ) **/
       View row = inflate.inflate(R.layout.bike_spinner, parent, false);
        
       /*** Get each Model object from Arraylist **/
       tempValues = null;
       tempValues = (BikeSpinner) data.get(position);
        
       TextView label        = (TextView)row.findViewById(R.id.textbike_numbr);
       TextView sub          = (TextView)row.findViewById(R.id.textbike);
        
       if(position==0){
            
           // Default selected Spinner item 
           //label.setText("All Bikes");
           label.setText(tempValues.getBikeName());
           sub.setText(tempValues.getBikeNumber());
           //sub.setText("");
       }
       else
       {
           // Set values for spinner each row 
           label.setText(tempValues.getBikeName());
           sub.setText(tempValues.getBikeNumber());
       }   

       return row;
     }
}