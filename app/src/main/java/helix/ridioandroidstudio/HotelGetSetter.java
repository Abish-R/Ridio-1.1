/** Ridio v1.0.1
 *  Purpose    : HotelGetSetter to get and set values
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 23-01-2016 (favourite selected boolean)
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

public class HotelGetSetter {
	    
	    /**private variables */
	    String h_id;
	    String h_name;
	    String h_place;
	    String h_created_date;
	    boolean selected = false;
	   
	     
	    /** Empty constructor */
	    public HotelGetSetter(){         
	    }
	    
	    /** Constructor, initialize value without id */
	    public HotelGetSetter(String name,String place, String crte_date){
	    	this.h_name = name;
	    	this.h_place=place;
	        this.h_created_date = crte_date;
	    }
	        
	    /** Constructor, initialize value with id */
	    public HotelGetSetter(String id, String name,String place, String crte_date){
	        this.h_id = id;
	        this.h_name = name;
	    	this.h_place=place;
	        this.h_created_date = crte_date;
	    }

	    public HotelGetSetter(HotelCustomAdapter hotelCustomAdapter) {
			
		}

		/** getting ID */
	    public String getID(){
	        return this.h_id;
	    }
	     
	    /** setting id */
	    public void setID(String id){
	        this.h_id = id;
	    }
	     
	    /** getting name */
	    public String getName(){
	        return this.h_name;
	    }
	     
	    /** setting name */
	    public void setName(String name){
	        this.h_name = name;
	    }
	    
	    /** getting name */
	    public String getPlace(){
	        return this.h_place;
	    }
	     
	    /** setting name */
	    public void setPlace(String place){
	        this.h_place = place;
	    }
	     
	    /** getting phone number */
	    public String getHCreatedDate(){
	        return this.h_created_date;
	    }
	     
	    /** setting phone number */
	    public void setHCreatedDate(String crte_date){
	        this.h_created_date = crte_date;
	    }
	    /**Selected Status Checkbox*/
	    public boolean isSelected() {
	    	  return selected;
	    }
	    public void setSelected(boolean selected) {
	    	this.selected = selected;
	    }
	        
	    /** setting all */
	    public void setAll(String id, String name,String place, String crte_date){
	    	this.h_id = id;
	        this.h_name = name;
	    	this.h_place=place;
	        this.h_created_date = crte_date;
	    }
	}