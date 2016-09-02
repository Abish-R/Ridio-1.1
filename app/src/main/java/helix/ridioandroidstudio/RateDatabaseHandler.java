/** Ridio v1.0.1
 * 	Purpose	   : RateDatabaseHandler
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 20-02-2016 (modified for sync)
 *  Verified by: Srinivas
 *  Verified Dt: 13-01-2016
 * **/

package helix.ridioandroidstudio;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RateDatabaseHandler extends SQLiteOpenHelper {
	 
    /** All Static variables*/
    private static final int DATABASE_VERSION = 1; 				  // Database Version
    private static final String DATABASE_NAME = "ridio";  	   	  // Database Name    
    private static final String TABLE_RIDE_RATE = "rate_master";  // Contacts table name
 
    /** Contacts Table Columns names*/
    private static final String KEY_ID1 = "rate_id";
    private static final String KEY_RATE = "rate_small";
    private static final String KEY_RATE1 = "rate_medium";
    private static final String KEY_RATE2 = "rate_high";
    private static final String KEY_RATE_DATE = "rate_date";
    private static final String KEY_DATE = "created_date_rate";//changed
    private static final String KEY_RATE_UP_STATE = "rate_upload_status";
 
    public RateDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /** Creating Tables*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RIDE_RATE + "("
                + KEY_ID1 + " INTEGER PRIMARY KEY autoincrement not null,"+ KEY_RATE+" TEXT,"
        		+ KEY_RATE1 + " TEXT,"+ KEY_RATE2 + " TEXT,"+KEY_RATE_DATE+" DATETIME,"+
                KEY_DATE+ " TEXT,"+KEY_RATE_UP_STATE+" TEXT)";//DATETIME DEFAULT CURRENT_DATE"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
     
    /** Upgrading database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_RATE);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    /** Adding new rate*/
    void addContact(RateGetSetter contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID1, contact.getID());						// Get ID
        values.put(KEY_RATE, contact.getRate());                    // Get Rate
        values.put(KEY_RATE1, contact.getRate1());                  // Get Rate
        values.put(KEY_RATE2, contact.getRate2());                  // Get Rate
        values.put(KEY_RATE_DATE,contact.getRateDate());
        values.put(KEY_DATE,contact.getCreatedDate());				// Get Created date
        values.put(KEY_RATE_UP_STATE,contact.getUpState());                  // Get Up State
 
        /** Inserting Row*/
        db.insert(TABLE_RIDE_RATE, null, values);
        Log.e(values.getAsString(KEY_ID1), "Done");
        db.close(); // Closing database connection
    }
 
    /** Update rate of a date */
    void updateRates(String rt1,String rt2,String rt3,String dt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RATE, rt1);
        values.put(KEY_RATE1, rt2);
        values.put(KEY_RATE2, rt3);
        values.put(KEY_RATE_UP_STATE, "N");
        /** updating row*/
        int temp= db.update(TABLE_RIDE_RATE, values, KEY_RATE_DATE + " = ?",
                new String[] {dt});
        db.close();
    }
     
    /** Getting All Rates*/
    public List<RateGetSetter> getAllRates(String load_rate_data, String date) {
        List<RateGetSetter> contactList = new ArrayList<RateGetSetter>();
        String selectQuery;
        if(load_rate_data.equals("all"))
            selectQuery = "SELECT  * FROM " + TABLE_RIDE_RATE+" WHERE "+KEY_RATE_DATE+
                    " >= Datetime('"+date+"')";
        else if(load_rate_data.equals("date_ascending"))
            selectQuery = "SELECT  * FROM " + TABLE_RIDE_RATE+" WHERE "+KEY_RATE_DATE+
                    " >= Datetime('"+date+"') LIMIT 10";
        else if(load_rate_data.equals("date_descending"))
            selectQuery = "SELECT  * FROM " + TABLE_RIDE_RATE+" WHERE "+KEY_RATE_DATE+
                    " <= Datetime('"+date+"') ORDER BY "+KEY_RATE_DATE+" DESC LIMIT 10";
        else
            selectQuery = "SELECT  * FROM " + TABLE_RIDE_RATE+" WHERE "+KEY_RATE_DATE+
                " >= Datetime('"+date+"') LIMIT "+load_rate_data;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                RateGetSetter contact = new RateGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setRate(cursor.getString(1));
                contact.setRate1(cursor.getString(2));
                contact.setRate2(cursor.getString(3));
                contact.setRateDate(cursor.getString(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }
    
    /** Getting All Rates with Current Dates*/
//    public List<RateGetSetter> getAllRatesWithDate() {
//        List<RateGetSetter> contactList = new ArrayList<RateGetSetter>();
//        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_RATE +" WHERE date("+KEY_DATE+")>"
//        		+ "date(28/01/2016)";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        /** looping through all rows and adding to list*/
//        if (cursor.moveToFirst()) {
//            do {
//                RateGetSetter contact = new RateGetSetter();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setRate(cursor.getString(1));
//                contact.setRate1(cursor.getString(2));
//                contact.setRate2(cursor.getString(3));
//                contact.setCreatedDate(cursor.getString(4));
//                // Adding contact to list
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        return contactList;
//    }
    
    /** Getting Rates for a particular Date*/
    public List<RateGetSetter> getRatesOfaDate(String date) {
        List<RateGetSetter> contactList = new ArrayList<RateGetSetter>();
        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_RATE +" WHERE "+KEY_DATE+" ='"+date+"'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                RateGetSetter contact = new RateGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setRate(cursor.getString(1));
                contact.setRate1(cursor.getString(2));
                contact.setRate2(cursor.getString(3));
                contact.setRateDate(cursor.getString(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }
 
    // Updating single rate
//    public int updateContact(RateGetSetter contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_RATE, contact.getRate());
//        values.put(KEY_DATE, contact.getCreatedDate());
//
//        return db.update(TABLE_RIDE_RATE, values, KEY_ID1 + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }
 
    // Deleting single rate
//    public void deleteContact(RateGetSetter contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_RIDE_RATE, KEY_ID1 + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }
 
 
    // Getting contacts Count
    public int getRateCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RIDE_RATE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp=cursor.getCount();
        cursor.close();
        db.close();
        return temp;
    }
    /** Getting Last Updated rate date */
    public String getLastDate(String count) {
    	String temp="";
        String Query = "SELECT * FROM " + TABLE_RIDE_RATE +" WHERE "+KEY_ID1 +"='"+count+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                RateGetSetter contact = new RateGetSetter();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setRate(cursor.getString(1));
//                contact.setRate1(cursor.getString(2));
//                contact.setRate2(cursor.getString(3));
                contact.setRateDate(cursor.getString(4));
                temp=contact.getRateDate();
                // Adding contact to list
            } while (cursor.moveToNext());
        }        
        cursor.close();
        db.close();
 
        return temp;
    }

    public int getNotUploadedRatesCount(){
        int count=0;
        String Query= "SELECT * FROM "+TABLE_RIDE_RATE+ " WHERE "+KEY_RATE_UP_STATE+"='N' OR 'UP'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(Query,null);
        count=cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public List<RateGetSetter> getNotUploadedRates(){
        List<RateGetSetter> list = new ArrayList<RateGetSetter>();
        String Query= "SELECT * FROM "+TABLE_RIDE_RATE+ " WHERE "+KEY_RATE_UP_STATE+"='N' OR 'UP'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(Query,null);
        if (cursor.moveToFirst()) {
            do {
                RateGetSetter contact = new RateGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setRate(cursor.getString(1));
                contact.setRate1(cursor.getString(2));
                contact.setRate2(cursor.getString(3));
                contact.setRateDate(cursor.getString(4));
                contact.setCreatedDate(cursor.getString(5));
                contact.setUpState(cursor.getString(6));
                list.add(contact);
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**Update the upload status*/
    public int updateRatesUploadStatus(String dt, String st){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RATE_UP_STATE, st);

        /** updating row*/
        return db.update(TABLE_RIDE_RATE, values, KEY_RATE_DATE + " = ?",
                new String[] {dt});
    }
 
}