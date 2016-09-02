/** Ridio v1.0.1
 * 	Purpose	   : Vehicle Name Spinner get and set values
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 
 * **/
package helix.ridioandroidstudio;

public class VehicleNameSpinner { 
	/*** Initialize Strings **/
	private  String BikeName="";
	private  String BikeNumber="";
 
	/*** Set Methods **/
	public void setBikeName(String BikeName1){
		this.BikeName = BikeName1;
	}
 
	public void setBikeNumber(String BikeNumber1){
		this.BikeNumber = BikeNumber1;
	}
 
	/*** Get Methods **/
	public String getBikeName(){
		return this.BikeName;
	}

	public String getBikeNumber(){
		return this.BikeNumber;
	}   
}

