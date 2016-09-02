/** Ridio v1.0.1
 * 	Purpose	   : Return Vehicle List Value Get and Set 
 *  Created by : Abish 
 *  Created Dt : 16-01-2016
 *  Modified on: 
 *  Verified by:
 *  Verified Dt: 
 * **/

package helix.ridioandroidstudio;

public class ReturnVehicleListValueGetSet {
	/**Class variables*/
	private String name,mob,veh_num,from_dt,to_dt,day_rate,days,amt;
	
	/**Get methods to get the value*/
	public String getName() {
	    return name;
	}
	public String getMob() {
	    return mob;
	}
	public String getVehNum() {
	    return veh_num;
	}
	public String getFromDt() {
	    return from_dt;
	}
	public String getToDt() {
	    return to_dt;
	}
	public String getDayRate() {
	    return day_rate;
	}
	public String getDys() {
	    return days;
	}
	public String getAmt() {
	    return amt;
	}
	
	/**Set methods to set the value*/
	public void setName(String name) {
	    this.name = name;
	}	
	public void setMob(String m) {
	    this.mob = m;
	}
	public void setVehNum(String v) {
	    this.veh_num = v;
	}	
	public void setFromDt(String f) {
	    this.from_dt = f;
	}
	public void setToDt(String t) {
	    this.to_dt = t;
	}	
	public void setDayRate(String dr) {
	    this.day_rate = dr;
	}
	public void setDys(String d) {
	    this.days = d;
	}	
	public void setAmt(String a) {
	    this.amt = a;
	}
}
