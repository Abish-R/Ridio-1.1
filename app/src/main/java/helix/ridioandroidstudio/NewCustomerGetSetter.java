/** Ridio v1.0.1
 *  Purpose	   : NewCustomerGetSetter get and set values
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: (Comment field get set functions added)
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.ridioandroidstudio;

public class NewCustomerGetSetter {
    
	/** private variables **/
	int customer_id;
	String name, mobile1, mobile2, email_id, hotel_name, bike_reg_number, bike_name, bike_color;
	String per_day_rate, from_date, to_date, number_of_days, total_cost, vendor_id, booking_date;
	String customer_trust_rating, bike_status, upload_status, feedback;
    
    /** Empty constructor **/
    public NewCustomerGetSetter(){         
    }
    
    /** Constructor to intialize all the values **/
    public NewCustomerGetSetter(int _id,String name_,String mobile1_,String mobile2_,
    		String email_id_,String hotel_name_,String bike_nam,
    		String bike_colr,String bike_reg_number_,String per_day_rate_,
    		String from_date_,String to_date_,String number_of_days_,String total_cost_,
    		String vendor_id_,String booking_date_,String customer_trust_rating_,
    		String b_status_,String u_status_,String feedb){
    	this.customer_id=_id;
    	this.name=name_;
    	this.mobile1=mobile1_;
    	this.mobile2=mobile2_;
    	this.email_id=email_id_;
    	this.hotel_name=hotel_name_;
    	this.bike_name=bike_nam;
    	this.bike_color=bike_colr;
    	this.bike_reg_number=bike_reg_number_;
    	
    	this.per_day_rate=per_day_rate_;
    	this.from_date=from_date_;
    	this.to_date=to_date_;
    	this.number_of_days=number_of_days_;
    	this.total_cost=total_cost_;
    	this.vendor_id=vendor_id_;
    	this.booking_date=booking_date_;
    	this.customer_trust_rating=customer_trust_rating_;
    	this.bike_status=b_status_;
    	this.upload_status=u_status_;
		this.feedback=feedb;
    }
        
    /** Constructor to intialize without customer_id value **/
    public NewCustomerGetSetter(String name_,String mobile1_,String mobile2_,
    		String email_id_,String hotel_name_,String bike_nam,
    		String bike_colr,String bike_reg_number_,String per_day_rate_,
    		String from_date_,String to_date_,String number_of_days_,String total_cost_,
    		String vendor_id_,String booking_date_,String customer_trust_rating_,
    		String b_status_,String u_status_,String feedb){
    	this.name=name_;
    	this.mobile1=mobile1_;
    	this.mobile2=mobile2_;
    	this.email_id=email_id_;
    	this.hotel_name=hotel_name_;
    	
    	this.bike_name=bike_nam;
    	this.bike_color=bike_colr;
    	this.bike_reg_number=bike_reg_number_;
    	this.per_day_rate=per_day_rate_;
    	this.from_date=from_date_;
    	this.to_date=to_date_;
    	this.number_of_days=number_of_days_;
    	this.total_cost=total_cost_;
    	this.vendor_id=vendor_id_;
    	this.booking_date=booking_date_;
    	this.customer_trust_rating=customer_trust_rating_;
    	this.bike_status=b_status_;
    	this.upload_status=u_status_;
		this.feedback=feedb;
    }
    
    /** Constructor to intialize values while return bike called **/
    public NewCustomerGetSetter(String mobile,String bike,String b_status,String rate,String todate,String days,
								String price,String feedb){
		this.mobile1=mobile;
    	this.bike_reg_number=bike;
    	this.bike_status=b_status;
    	this.customer_trust_rating=rate;
    	this.to_date=todate;
    	this.number_of_days=days;
    	this.total_cost=price;
		this.feedback=feedb;
    }
    
    /** Constructor for intialize values while status changed on customer entry **/
    public NewCustomerGetSetter(String mob,String u_status){
    	this.mobile1=mob;
    	this.upload_status=u_status;
    }
   
    public int getID(){ return this.customer_id; }  // getting ID   
    public void setID(int id){ this.customer_id = id; } // setting id
    
    public String getName(){ return this.name; } // getting name   
    public void setName(String name){ this.name = name; }   // setting name 
    
    public String getMobile1(){ return this.mobile1; } // getting mobile1   
    public void setMobile1(String mobile){ this.mobile1 = mobile; }   // setting mobile1 
    
    public String getMobile2(){ return this.mobile2; } // getting mobile2   
    public void setMobile2(String mobile1){ this.mobile2 = mobile1; }   // setting mobile2 
    
    public String getEmail(){ return this.email_id; } // getting email_id   
    public void setEmail(String email){ this.email_id = email; }   // setting email_id 
    
    public String getHotel(){ return this.hotel_name; } // getting hotel_name   
    public void setHotel(String hotel){ this.hotel_name = hotel; }   // setting hotel_name 
    
    public String getBikeNo(){ return this.bike_reg_number; } // getting bike_reg_number   
    public void setBikeNo(String no){ this.bike_reg_number = no; }   // setting bike_reg_number 
    
    public String getBikeName(){ return this.bike_name; } // getting bike_reg_number   
    public void setBikeName(String name){ this.bike_name = name; }   // setting bike_reg_number 
    
    public String getBikeColor(){ return this.bike_color; } // getting bike_reg_number   
    public void setBikeColor(String clr){ this.bike_color = clr; }   // setting bike_reg_number 
    
    public String getDayRate(){ return this.per_day_rate; } // getting per_day_rate   
    public void setDayRate(String rate){ this.per_day_rate = rate; }   // setting per_day_rate 
    
    public String getFromDate(){ return this.from_date; } // getting from_date   
    public void setFromDate(String from_date1){ this.from_date = from_date1; }   // setting from_date 
    
    public String getToDate(){ return this.to_date; } // getting to_date   
    public void setToDate(String to){ this.to_date = to; }   // setting to_date 
    
    public String getTotalDays(){ return this.number_of_days; } // getting number_of_days   
    public void setTotalDays(String days_no){ this.number_of_days = days_no; }   // setting number_of_days 
    
    public String getTotalCost(){ return this.total_cost; } // getting total_cost   
    public void setTotalCost(String cost){ this.total_cost = cost; }   // setting total_cost 
    
    public String getVendorId(){ return this.vendor_id; } // getting vendor_id   
    public void setVendorId(String id){ this.vendor_id = id; }   // setting vendor_id 
    
    public String getBookDate(){ return this.booking_date; } // getting booking_date   
    public void setBookDate(String dt){ this.booking_date = dt; }   // setting booking_date 
    
    public String getRating(){ return this.customer_trust_rating; } // getting customer_trust_rating   
    public void setRating(String rte){ this.customer_trust_rating = rte; }   // setting customer_trust_rating
    
    public String getBikeStatus(){ return this.bike_status; } // getting bike status
    public void setBikeStatus(String sts){ this.bike_status = sts; } 
    
    public String getUploadStatus(){ return this.upload_status; } // getting upload status
    public void setUploadStatus(String sts){ this.upload_status = sts; }

	public String getFeedback(){ return this.feedback; } // getting status
	public void setFeedback(String feed){ this.feedback = feed; }
    
    public void setAll(String rate, String crte_date){
    	//this._rate = rate;
       // this._created_date = crte_date;
    }
}