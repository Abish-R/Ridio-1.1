/** Ridio v1.0.1
 * 	Purpose	   : Vehicle Name Spinner Adapter to display values
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

public class VehicleNameSpinnerAdapter extends ArrayAdapter<String>{
    
   private Activity activity;
   private ArrayList data;
   public Resources res;
   VehicleNameGetSet tempValues=null;
   LayoutInflater inflater;
    
   /***  CustomAdapter Constructor, initializer for customer entry **/
   public VehicleNameSpinnerAdapter(VehicleMasterClass vehicleMasterClass,int textViewResourceId,   
                         ArrayList objects,Resources resLocal){
       super(vehicleMasterClass, textViewResourceId, objects);
        
       /*** Take passed values ***/
       activity = vehicleMasterClass;
       data     = objects;
       res      = resLocal;
   
       /***  Layout inflator to call external xml layout () **/
       inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
     }
   
   /***  CustomAdapter Constructor , initializer for Past/current booking entry **/
   public VehicleNameSpinnerAdapter(ReportPastBooking activitySpinner,int textViewResourceId,   
           ArrayList objects,Resources resLocal) {
	   super(activitySpinner, textViewResourceId, objects);
       
       /*** Take passed values */
       activity = activitySpinner;
       data     = objects;
       res      = resLocal;
   
       /**  Layout inflator to call external xml layout () **/
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

   /** This funtion called for each row ( Called data.size() times ) */
   public View getCustomView(int position, View convertView, ViewGroup parent) {

       /*** Inflate bike_spinner.xml file for each row ( Defined below ) **/
       View row = inflater.inflate(R.layout.bike_spinner, parent, false);
        
       /*** Get each Model object from Arraylist **/
       tempValues = null;
       tempValues = (VehicleNameGetSet) data.get(position);
        
       TextView label        = (TextView)row.findViewById(R.id.textbike_numbr);
       TextView sub          = (TextView)row.findViewById(R.id.textbike);
        
       if(position>=0){  // Set values for spinner each row 
           label.setText(tempValues.getID());
           sub.setText(tempValues.getName());
       }   

       return row;
     }
}