/** Ridio v1.0.1
 * 	Purpose	   : Vehicle Details Get and Set in Vehicle Master
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/


package helix.ridioandroidstudio;

public class VehicleGetSetter {    
    /** class private variables*/
    int _id;
    String _color,categ, _number, _model_yr, _srvce_dt, _prchse_dt, _rpr_pnd, _pst_rpr_issue,
    	_created_date, _status, _vend_id,veh_upload_status;
    
    /** Empty constructor*/
    public VehicleGetSetter(){         
    }
    
    /** constructor*/
    public VehicleGetSetter(String color,String cate,String number,String model_yr,String srvce_dt,
    		String prchse_dt,String rpr_pnd,String pst_rpr_issue,String date,String status,
    		String vdr_id,String up_stat){
    	this._color = color;
    	this.categ = cate;
    	this._number=number;
        this._model_yr = model_yr;
        this._srvce_dt=srvce_dt;
        this._prchse_dt=prchse_dt;
        this._rpr_pnd=rpr_pnd;
        this._pst_rpr_issue=pst_rpr_issue;
        this._created_date=date;
        this._status=status;
        this._vend_id=vdr_id;
        this.veh_upload_status=up_stat;
    }
        
    /** constructor*/
    public VehicleGetSetter(int id,String color,String cate,String number,String model_yr,String srvce_dt,
    		String prchse_dt,String rpr_pnd,String pst_rpr_issue,String date,String status,
    		String vdr_id,String up_stat){
    	this._id=id;
    	this._color = color;
    	this.categ = cate;
    	this._number=number;    	
        this._model_yr = model_yr;
        this._srvce_dt=srvce_dt;
        this._prchse_dt=prchse_dt;
        this._rpr_pnd=rpr_pnd;
        this._pst_rpr_issue=pst_rpr_issue;
        this._created_date=date;
        this._status=status;
        this._vend_id=vdr_id;
        this.veh_upload_status=up_stat;
    }
    /** constructor*/
    public VehicleGetSetter(String number,String status){    	
    	this._number=number;
        this._status=status;
    }

    /** constructor for edited update vehicle from Vehicle Master*/
    public VehicleGetSetter(String no,String clr, String categ,String srvc_dt,String prchse_dt,String rpr_pndng,
                            String pst_rpr, String crte_dt,String up){
        this._number=no;
        this._color = clr;
        this.categ = categ;
        this._srvce_dt=srvc_dt;
        this._prchse_dt=prchse_dt;
        this._rpr_pnd=rpr_pndng;
        this._pst_rpr_issue=pst_rpr;
        this._created_date=crte_dt;
        this.veh_upload_status=up;
    }

    /** getting ID*/
    public int getID(){
        return this._id;
    }
     
    /** setting id*/
    public void setID(int id){
        this._id = id;
    }
    /** getting number*/
    public String getNumber(){
        return this._number;
    }
     
    /** setting number*/
    public void setNumber(String color){
        this._number = color;
    }   
    /** getting Color*/
    public String getColor(){
        return this._color;
    }
     
    /** setting Color*/
    public void setColor(String color){
        this._color = color;
    }
    
    /** getting Category*/
    public String getCategory(){
        return this.categ;
    }
     
    /** setting Category*/
    public void setCategory(String cate){
        this.categ = cate;
    }
     
    /** getting Model Name */
    public String getModelName(){
        return this._model_yr;
    }
     
    /** setting Model Name*/
    public void setModelName(String modelyr){
        this._model_yr = modelyr;
    }
    
 	/** getting Service Date */
    public String getSeriveDate(){
        return this._srvce_dt;
    }
     
    /** setting Service Date */
    public void setServiceDate(String srvc_date){
        this._srvce_dt = srvc_date;
    }
 	/** getting Purchase Date*/
    public String getPurchaseDate(){
        return this._prchse_dt;
    }
     
    /** setting Purchase Date */
    public void setPurchaseDate(String pr_date){
        this._prchse_dt = pr_date;
    }
  	/** getting Pending Repair */
    public String getPendingRepair(){
        return this._rpr_pnd;
    }
 	/** setting Pending Repair */
    public void setPendingRepair(String rpr){
        this._rpr_pnd = rpr;
    }
    
 	/**getting Past Issues */
    public String getPastIssues(){
        return this._pst_rpr_issue;
    }
     
    /** setting Past Issues */
    public void setPastIssues(String past_issues){
        this._pst_rpr_issue = past_issues;
    }     
    
    /** getting Created dte */
    public String getCreatedDate(){
        return this._created_date;
    }
     
    /** setting Created dte */
    public void setCreatedDate(String date){
        this._created_date = date;
    }
    /** Status*/
    public String getStatus(){
        return this._status;
    }
     
    /** setting Status*/
    public void setStatus(String status){
        this._status = status;
    }
    /** get VendorId */
    public String getVendorId(){
        return this._vend_id;
    }
     
    /** setting VendorId*/
    public void setVendorId(String vr_id){
        this._vend_id = vr_id;
    }
    /** get Upload State */
    public String getUploadState(){
        return this.veh_upload_status;
    }
     
    /** setting Upload State*/
    public void setUploadState(String up){
        this.veh_upload_status = up;
    }
        
    /** setting all*/
    public void setAll(String rate, String crte_date){
    	//this._rate = rate;
       // this._created_date = crte_date;
    }
}