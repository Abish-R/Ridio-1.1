/** Ridio v1.0.1
 * 	Purpose	   : Vehicle Name Get and Set for List
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/
package helix.ridioandroidstudio;

public class VehicleNameGetSet {
	/**Private class variables*/
	String _id,category,_name;
    
    /***Empty constructor*/
    public VehicleNameGetSet(){         
    }
    
 	/***Constructor*/
    public VehicleNameGetSet(String id,String cate,String name){
    	this._id = id;
    	this._name=name;
    	this.category=cate;
    }
    
 	/**constructor*/
    public VehicleNameGetSet(String cate,String name){
    	this.category=cate;
    	this._name=name;
    }
    
 	public VehicleNameGetSet(String name) {
 		this._name=name;
	}

	/**getting ID*/
    public String getID(){
        return this._id;
    }
     
    /** setting id*/
    public void setID(String id){
        this._id = id;
    }
    
    /**getting Category*/
    public String getCategory(){
        return this.category;
    }
     
    /**setting Category*/
    public void setCategory(String cate){
        this.category = cate;
    }
    
 	/**getting Name*/
    public String getName(){
        return this._name;
    }
     
    /**setting name*/
    public void setName(String name){
        this._name = name;
    }
}
