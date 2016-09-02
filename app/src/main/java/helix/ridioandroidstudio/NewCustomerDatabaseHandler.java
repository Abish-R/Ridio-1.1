/** Ridio v1.0.1
 * 	Purpose	   : NewCustomerDatabaseHandler
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 27-02-2016 ( Comment filed and time handled )
 *  Verified by:
 *  Verified Dt:
 * **/

package helix.ridioandroidstudio;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewCustomerDatabaseHandler extends SQLiteOpenHelper {
	 
    /** All Static variables*/
    private static final int DATABASE_VERSION = 1;   // Database Version
    private static final String DATABASE_NAME = "ridio"; // Database Name    
    private static final String TABLE_RIDE_CUSTOMER = "bookings"; // bookings table name
    private static final String TABLE_RIDE_RATE = "rate_master"; // rate_master table name
    private static final String TABLE_RIDE_VEHICLE = "vehicle_master";
 
    /** Contacts Table Columns names */
    private static final String KEY_ID = "customer_id";
    private static final String KEY_Name = "name";
    private static final String KEY_MOBILE1 = "mobile1";
    private static final String KEY_MOBILE2 = "mobile2";
    private static final String KEY_EMAIL = "email_id";
    private static final String KEY_HOTEL_NAME = "hotel_name";
    private static final String KEY_BIKE_REG_NO = "bike_reg_number";
    private static final String KEY_BIKE_NAME_SAVE = "bike_name";
    private static final String KEY_BIKE_COLOR = "bike_color";
    private static final String KEY_PER_DAY_RATE = "per_day_rate";
    private static final String KEY_FROM_DATE = "from_date";
    private static final String KEY_TO_DATE = "to_date";
    private static final String KEY_NO_OF_DAYS = "number_of_days";
    private static final String KEY_TOTAL_COST = "total_cost";
    private static final String KEY_VENDOR_ID = "vendor_id";
    private static final String KEY_BOOK_DATE = "booking_date";
    private static final String KEY_TRUST_RATING = "customer_trust_rating";
    private static final String KEY_BIKE_STATUS = "bike_status";
    private static final String KEY_UPLOAD_STATUS = "upload_status";
    private static final String KEY_FEEDBACK = "feedback";
 
    public NewCustomerDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }//customer_id,name,mobile1,mobile2,email_id,hotel_name,bike_reg_number,per_day_rate,from_date,to_date,number_of_days,total_cost,vendor_id,booking_date,customer_trust_rating
 
    /** Creating Tables */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RIDE_CUSTOMER + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement not null," + KEY_Name + " TEXT,"+
                KEY_MOBILE1+ " TEXT,"+ KEY_MOBILE2 + " TEXT,"+ KEY_EMAIL+ " TEXT,"+
                 KEY_HOTEL_NAME + " TEXT,"+ KEY_BIKE_NAME_SAVE+ " TEXT,"+KEY_BIKE_COLOR+ " TEXT,"+
                KEY_BIKE_REG_NO+ " TEXT,"+KEY_PER_DAY_RATE + " TEXT,"+ KEY_FROM_DATE+ " DATETIME,"+
                 KEY_TO_DATE + " DATETIME,"+ KEY_NO_OF_DAYS+ " TEXT,"+ KEY_TOTAL_COST + " TEXT,"+
                KEY_VENDOR_ID+ " TEXT,"+ KEY_BOOK_DATE + " DATETIME DEFAULT CURRENT_DATE,"+
                KEY_TRUST_RATING+ " TEXT,"+KEY_BIKE_STATUS + " TEXT,"+KEY_UPLOAD_STATUS+" TEXT," +
                KEY_FEEDBACK+" TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /** Upgrading database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_CUSTOMER);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    /** Adding new Bookings */
    void addContact(NewCustomerGetSetter contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, contact.getID());					 // Get ID
        values.put(KEY_Name, contact.getName());				 // Get Name
        values.put(KEY_MOBILE1,contact.getMobile1());			 // Get Mobil1
        values.put(KEY_MOBILE2, contact.getMobile2());			 // Get Mobil1
        values.put(KEY_EMAIL,contact.getEmail());				 // Get Email
        values.put(KEY_HOTEL_NAME, contact.getHotel());			 // Get Hotel
        values.put(KEY_BIKE_NAME_SAVE, contact.getBikeName());	 // Get Bike Name
        values.put(KEY_BIKE_COLOR, contact.getBikeColor());		 // Get Bike Color
        values.put(KEY_BIKE_REG_NO, contact.getBikeNo());		 // Get Bike Number
        values.put(KEY_PER_DAY_RATE,contact.getDayRate());		 //	Get Day Rate
        values.put(KEY_FROM_DATE, contact.getFromDate());		 // Get From Date
        values.put(KEY_TO_DATE, contact.getToDate());			 // Get To Date
        values.put(KEY_NO_OF_DAYS,contact.getTotalDays());		 // Get No of Days
        values.put(KEY_TOTAL_COST, contact.getTotalCost());		 // Get Total Cost
        values.put(KEY_VENDOR_ID, contact.getVendorId());		 // Get Vendor ID
        values.put(KEY_BOOK_DATE,contact.getBookDate());		 // Get Booking Date
        values.put(KEY_TRUST_RATING,contact.getRating());		 // Get Trust Rating
        values.put(KEY_BIKE_STATUS,contact.getBikeStatus());	 // Get Bike Status
        values.put(KEY_UPLOAD_STATUS,contact.getUploadStatus()); // Get Upload Status
        values.put(KEY_FEEDBACK,contact.getFeedback());          // Get Feedback
 
        /** Inserting Row */
        db.insert(TABLE_RIDE_CUSTOMER, null, values);
        Log.e("Customer Table :", ""+contact.getID()+contact.getName()+contact.getMobile1()+
        		contact.getMobile2()+contact.getEmail()+contact.getHotel()+contact.getBikeNo()+
        		contact.getDayRate()+contact.getFromDate()+contact.getToDate()+contact.getTotalDays()+
        		contact.getTotalCost()+contact.getVendorId()+contact.getBookDate()+contact.getRating()+
        		contact.getBikeStatus()+contact.getUploadStatus()+contact.getFeedback());
        db.close(); // Closing database connection
    }
 
    // Getting single contact
//    RateGetSetter getContact(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
// 
//        Cursor cursor = db.query(TABLE_RIDE_RATE, new String[] { KEY_ID,
//        		KEY_RATE, KEY_DATE }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
// 
//        RateGetSetter contact = new RateGetSetter(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return contact;
//    }
     
    /** Getting All Booking*/
    public List<NewCustomerGetSetter> getAllContacts() {
        List<NewCustomerGetSetter> contactList = new ArrayList<NewCustomerGetSetter>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_CUSTOMER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                NewCustomerGetSetter contact = new NewCustomerGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setMobile1(cursor.getString(2));
                contact.setMobile2(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setHotel(cursor.getString(5));
                contact.setBikeName(cursor.getString(6));
                contact.setBikeColor(cursor.getString(7));
                contact.setBikeNo(cursor.getString(8));
                contact.setDayRate(cursor.getString(9));
                contact.setFromDate(cursor.getString(10));                
                contact.setToDate(cursor.getString(11));
                contact.setTotalDays(cursor.getString(12));
                contact.setTotalCost(cursor.getString(13));                
                //photoscanid
                contact.setVendorId(cursor.getString(14));
                contact.setBookDate(cursor.getString(15));
                contact.setRating(cursor.getString(16));
                contact.setBikeStatus(cursor.getString(17));
                contact.setUploadStatus(cursor.getString(18));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        /** return list*/
        return contactList;
    }
    
    /** Getting All Booking With bike Status P for Return Vehicle*/
    public List<NewCustomerGetSetter> getAllCustomerBikeWithStatusOfP(String alortday,String t_dt) {
        List<NewCustomerGetSetter> contactList = new ArrayList<NewCustomerGetSetter>();
        String selectQuery;
        // Select All Query
        if(alortday.equals("all"))
            selectQuery = "SELECT  * FROM " + TABLE_RIDE_CUSTOMER +" WHERE "+ KEY_BIKE_STATUS+ " = 'P'" ;
        else
            selectQuery = "SELECT  * FROM " + TABLE_RIDE_CUSTOMER +" WHERE "+ KEY_BIKE_STATUS+ " = 'P' AND "+
                    KEY_TO_DATE+" <= '"+t_dt+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                NewCustomerGetSetter contact = new NewCustomerGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setMobile1(cursor.getString(2));
                contact.setMobile2(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setHotel(cursor.getString(5));
                contact.setBikeName(cursor.getString(6));
                contact.setBikeColor(cursor.getString(7));
                contact.setBikeNo(cursor.getString(8));
                contact.setDayRate(cursor.getString(9));
                contact.setFromDate(cursor.getString(10));                
                contact.setToDate(cursor.getString(11));
                contact.setTotalDays(cursor.getString(12));
                contact.setTotalCost(cursor.getString(13));                
                //photoscanid
                contact.setVendorId(cursor.getString(14));
                contact.setBookDate(cursor.getString(15));
                contact.setRating(cursor.getString(16));
                contact.setBikeStatus(cursor.getString(17));
                contact.setUploadStatus(cursor.getString(18));
                //contact.setFeedback(cursor.getString(19));
                
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        /** return list*/
        return contactList;
    }

    /** Getting All Booking which is not uploaded or uploaded pending for Sync*/
    public List<NewCustomerGetSetter> getAllCustomerNotUploaded() {
        List<NewCustomerGetSetter> contactList = new ArrayList<NewCustomerGetSetter>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_CUSTOMER +" WHERE ("+ KEY_UPLOAD_STATUS+ " =? )"+" OR ("+
        		KEY_UPLOAD_STATUS+ " =? AND "+ KEY_BIKE_STATUS+ " =? )";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"N","UP","R"});//,"R"
        
        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                NewCustomerGetSetter contact = new NewCustomerGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setMobile1(cursor.getString(2));
                contact.setMobile2(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setHotel(cursor.getString(5));
                contact.setBikeName(cursor.getString(6));
                contact.setBikeColor(cursor.getString(7));
                contact.setBikeNo(cursor.getString(8));
                contact.setDayRate(cursor.getString(9));
                contact.setFromDate(cursor.getString(10));                
                contact.setToDate(cursor.getString(11));
                contact.setTotalDays(cursor.getString(12));
                contact.setTotalCost(cursor.getString(13));                
                //photoscanid
                contact.setVendorId(cursor.getString(14));
                contact.setBookDate(cursor.getString(15));
                contact.setRating(cursor.getString(16));
                contact.setBikeStatus(cursor.getString(17));
                contact.setUploadStatus(cursor.getString(18));
                contact.setFeedback(cursor.getString(19));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        /** return list*/
        return contactList;
    }
 
     /**Updating single record from return vehicle*/
    public int updateVehStatusRatingInBookings(NewCustomerGetSetter contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_BIKE_REG_NO, contact.getBikeNo());
        values.put(KEY_BIKE_STATUS, contact.getBikeStatus());
        values.put(KEY_TRUST_RATING, contact.getRating());
        values.put(KEY_TO_DATE, contact.getToDate());
        values.put(KEY_NO_OF_DAYS, contact.getTotalDays());
        values.put(KEY_TOTAL_COST, contact.getTotalCost());
        values.put(KEY_FEEDBACK, contact.getFeedback());
        /** updating row*/
        return db.update(TABLE_RIDE_CUSTOMER, values, KEY_BIKE_REG_NO + " = ? AND "+KEY_MOBILE1+" =? ",
                new String[] { contact.getBikeNo(),contact.getMobile1() });
    }

    public int checkSameVehiclePendingAvailability(String bk_no){
        String Query="SELECT * FROM "+TABLE_RIDE_CUSTOMER+" WHERE "+KEY_BIKE_REG_NO+" = '"+bk_no+"' AND "+
                KEY_BIKE_STATUS +" = 'P'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        int count=cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    
    /**Updating upload status from Sync*/
    public int updateUploadStatus(NewCustomerGetSetter contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
       //values.put(KEY_BIKE_REG_NO, contact.getBikeNo());
        values.put(KEY_UPLOAD_STATUS, contact.getUploadStatus());
 
        return db.update(TABLE_RIDE_CUSTOMER, values, KEY_MOBILE1 + " = ?",
                new String[] { String.valueOf(contact.getMobile1()) });
    }
 
//    // Deleting single contact
//    public void deleteContact(RateGetSetter contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_RIDE_CUSTOMER, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }
 
 
    /** Getting booking Count for a mobile number for Booking*/
    public int getBookingCountForMobileWithStatusP(String mob) {
        String countQuery = "SELECT  * FROM " + TABLE_RIDE_CUSTOMER+" WHERE "+ KEY_MOBILE1 +"= '"+mob+"' AND "
                + KEY_BIKE_STATUS+"='P'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();
        db.close();
        // return count
        return count;
    }

    /** Getting not uploaded and upload pending count for Sync*/
    public int getAllCustomerNotUploadedCount() {
        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_CUSTOMER +" WHERE ("+ KEY_UPLOAD_STATUS+ " =? ) OR ("+
                KEY_UPLOAD_STATUS+ " =? AND "+ KEY_BIKE_STATUS+ " =? )";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"N","UP","R"});//,"R"
        int temp=cursor.getCount();
        cursor.close();
        db.close();
        
        return temp;
    }
 
}