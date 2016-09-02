/** Ridio v1.0
 * 	Purpose	   : New customer Bike Name Color Number Display 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 06-02-2016 (Remove Duplicate value from bike spinner, Pending Vehicle not showing also fixed)
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewCustomerBikeAdapterViewsCreater {
	Context context;
	String bike_name_from_adapter,bike_color_from_adapter,bike_no_from_adapter;
	Resources res;
	String vendor_id="";
	List<VehicleGetSetter> savelistTemp;
	
	/** Constructor context from New Customer*/
	public NewCustomerBikeAdapterViewsCreater(NewCustomer newcus){
		this.context =newcus;
	}
	
	/**Get Bike Name From DB*/
	public void setListBikeName(String vr_id,String categ,String frm,String to){
		vendor_id=vr_id;																/**Change the arguments*/
		List<VehicleGetSetter> contacts = ((NewCustomer)context).vdb.getVehicleListToCustomer(vendor_id,categ,frm,to);
		savelistTemp=contacts;
		((NewCustomer)context).CustomListBikeName.clear();
		for (VehicleGetSetter cn : contacts) {
	        final BikeSpinner sched = new BikeSpinner();
	              /** Firstly take data in model object */
//	        		if(cn.getModelName().equals(""))
//	        			sched.setBikeName("Select Bike");
	        			sched.setBikeName(cn.getModelName());
	        			if(((NewCustomer)context).CustomListBikeName.contains(sched))
	        				Log.d("Value to adapter", "Data already there");
	        			else
	        				((NewCustomer)context).CustomListBikeName.add(sched); /** Take Model Object in ArrayList */
	            
	        } 
		if(contacts.size()<1){
			((NewCustomer)context).cust_bike_color.setAdapter(null);
			((NewCustomer)context).cust_bike_color.setAdapter(null);
			((NewCustomer)context).cust_bike_number.setAdapter(null);
			((NewCustomer)context).last_layout.setVisibility(View.GONE);
			((NewCustomer)context).passBikeValueToCustomer("","","");
		}
    }
	
	/**Get Bike color from DB of selected bike*/
	public void setListBikeColor(String name){
		//List<VehicleGetSetter> contacts = ((NewCustomer)context).vdb.getVehicleColorListToCustomer(vendor_id,name);
		((NewCustomer)context).CustomListBikeColor.clear();
		List<VehicleGetSetter> contacts=savelistTemp;
		
		for (VehicleGetSetter cn : contacts) {
	        final BikeSpinner sched = new BikeSpinner();
	              /** Firstly take data in model object */
	        			sched.setBikeName(cn.getColor());
	        		if(name.equals(cn.getModelName()))
	        			((NewCustomer)context).CustomListBikeColor.add(sched); /** Take Model Object in ArrayList */
	        		else{
	        			Log.d("Value to adapter", "Data already there");
	        		//((NewCustomer)context).colorAdapterIntermediater();
	        		}
	        }
		((NewCustomer)context).colorAdapterIntermediater();
	}
	
	/**Get Bike number from DB of selected bike and selected color*/
	public void setListBikeNumber(String name,String color){
		//((NewCustomer)context).CustomListBikeNumber.clear();
		//List<VehicleGetSetter> contacts = ((NewCustomer)context).vdb.getVehicleNumberListToCustomer(vendor_id,name,color);
		
		((NewCustomer)context).CustomListBikeNumber.clear();
		for (VehicleGetSetter cn : savelistTemp) {
	        final BikeSpinner sched = new BikeSpinner();
	              /** Firstly take data in model object */
//	        		if(cn.getNumber().equals("") || cn.getNumber().equals("All Bikes"))
//	        			sched.setBikeName("Select Number");
	        			sched.setBikeName(cn.getNumber());
	        			if(name.equals(cn.getModelName()) && color.equals(cn.getColor()))
	        				((NewCustomer)context).CustomListBikeNumber.add(sched); /** Take Model Object in ArrayList */
	        				
	        			else{
	        				Log.d("Value to adapter", "Data already there");
	        		//((NewCustomer)context).numberAdapterIntermediater();
	        			}
	        }
		((NewCustomer)context).numberAdapterIntermediater();
	}
	
	/**Show List in Spinner Bike Name,Color,Number*/
	public void setBikeAdapterValues(final ArrayList<BikeSpinner> CustomListBikeName,BikeSpinnerAdapter adapter,
			final Spinner mySpinner,final String str,Resources res) {
		res = context.getResources();
		Spinner temp=mySpinner;
		adapter = new BikeSpinnerAdapter((NewCustomer) context, R.layout.bike_spinner, CustomListBikeName,res);
		temp.setAdapter(adapter);
		temp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
            	if(str.equals("name")){
            		bike_name_from_adapter   = ((TextView) v.findViewById(R.id.textbike_numbr)).getText().toString();
            		Log.d("bike_name_from_adapter", bike_name_from_adapter);
            		setListBikeColor(bike_name_from_adapter);
            		}
            	else if(str.equals("color")){
            		bike_color_from_adapter   = ((TextView) v.findViewById(R.id.textbike_numbr)).getText().toString();
            		Log.d("bike_name_from_adapter", bike_name_from_adapter);
            		setListBikeNumber(bike_name_from_adapter, bike_color_from_adapter);
            	}
            	else if(str.equals("number")){
            		bike_no_from_adapter   = ((TextView) v.findViewById(R.id.textbike_numbr)).getText().toString();
            		Log.d("bike_name_from_adapter", bike_name_from_adapter);
            		if(bike_no_from_adapter.length()>=10){
            			((NewCustomer)context).last_layout.setVisibility(View.VISIBLE);                	                	
                    }
                    else
                    	((NewCustomer)context).last_layout.setVisibility(View.GONE);
            		((NewCustomer)context).passBikeValueToCustomer(bike_name_from_adapter,bike_color_from_adapter,bike_no_from_adapter);
            	}
            	else{
            	}
            	//what_bike   = ((TextView) v.findViewById(R.id.textbike)).getText().toString();
            }
	 
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
	                //Toast.makeText(getApplicationContext(), "You Must Select Vehicle", Toast.LENGTH_LONG).show();
            }
        });
	}

}
