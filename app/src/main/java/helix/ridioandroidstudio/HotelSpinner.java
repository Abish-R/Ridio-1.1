/** Ridio v1.0.1
 *  Purpose    : HotelSpinner setting and getting values
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

public class HotelSpinner {
	/** Private variables **/
	private  String hotel_id="";
     private  String hotel_place=""; 
     private  String hotel_name="";
      
     /*** Set Methods ***/
     public void setHotelId(String id)
     {
         this.hotel_id = id;
     }
      
     public void setHotelPlace(String plc)
     {
         this.hotel_place = plc;
     }
      
     public void setHotelName(String name)
     {
         this.hotel_name = name;
     }
      
     /*** Get Methods **/
     public String getHotelId()
     {
         return this.hotel_id;
     }
      
     public String getHotelPlace()
     {
         return this.hotel_place;
     }
  
     public String getHotelName()
     {
         return this.hotel_name;
     }   
}

