/** Ridio v1.0.1
 * 	Purpose	   : RateGetSetter get and set values
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 20-02-2016 (modified for sync)
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/
package helix.ridioandroidstudio;

public class RateGetSetter {
    
    /**private variables*/
    int _id;
    String _rate;
    String _rate1;
    String _rate2;
    String _rate_date;
    String _created_date;
    String _up_state;
     
    /** Empty constructor*/
    public RateGetSetter(){         
    }
    
 	/** constructor*/
    public RateGetSetter(String rate,String rate1,String rate2,String rate_dt, String crte_date,String state){
    	this._rate = rate;
    	this._rate1 = rate1;
    	this._rate2 = rate2;
        this._rate_date=rate_dt;
        this._created_date = crte_date;
        this._up_state = state;
    }
        
    /** constructor*/
    public RateGetSetter(int id, String rate,String rate1,String rate2,String rate_dt, String crte_date,String state){
        this._id = id;
        this._rate = rate;
        this._rate1 = rate1;
    	this._rate2 = rate2;
        this._rate_date=rate_dt;
        this._created_date = crte_date;
        this._up_state = state;
    }

    /** getting ID*/
    public int getID(){
        return this._id;
    }
     
    /** setting id*/
    public void setID(int id){
        this._id = id;
    }
         
    /** getting rate*/
    public String getRate(){
        return this._rate;
    }
     
    /** setting rate*/
    public void setRate(String name){
        this._rate = name;
    }
    
    /** getting rate1*/
    public String getRate1(){
        return this._rate1;
    }
     
    /** setting rate1*/
    public void setRate1(String rate){
        this._rate1 = rate;
    }
    
    /** getting rate2*/
    public String getRate2(){
        return this._rate2;
    }
     
    /** setting rate2*/
    public void setRate2(String rate){
        this._rate2 = rate;
    }

    /** getting Rate Date*/
    public String getRateDate(){
        return this._rate_date;
    }

    /** setting Rate Date*/
    public void setRateDate(String date){
        this._rate_date = date;
    }
     
    /** getting CreatedDate*/
    public String getCreatedDate(){
        return this._created_date;
    }
     
    /** setting CreatedDate*/
    public void setCreatedDate(String crte_date){
        this._created_date = crte_date;
    }

    /** getting Upload State*/
    public String getUpState(){
        return this._up_state;
    }

    /** setting Upload State*/
    public void setUpState(String state){
        this._up_state = state;
    }
        
    /** setting all*/
    public void setAll(String rate, String crte_date){
    	this._rate = rate;
        this._created_date = crte_date;
    }
}