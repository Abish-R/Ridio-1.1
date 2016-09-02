/** Ridio v1.0.1
 * 	Purpose	   : Vehicle Name Database Handler, Vehicle name List into Vehicle Master 
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on:
 *  Verified by: Srinivas
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

public class VehicleNameDatabaseHandler extends SQLiteOpenHelper  {

	/** All Static variables*/    
    private static final int DATABASE_VERSION = 1;							     // Database Version
    private static final String DATABASE_NAME = "ridio";					     // Database Name
    private static final String TABLE_RIDE_VEHICLE_NAME = "vehicle_name_master"; // Contacts table name
    private static final String KEY_BIKE_ID = "v_id";
    private static final String KEY_BIKE_CATOG = "v_category";
    private static final String KEY_BIKE_NAME = "v_name";
    
    /**Constructor*/
    public VehicleNameDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**Creating Tables*/
    @Override
    public void onCreate(SQLiteDatabase db) {
    	String CREATE_CONTACTS_TABLE3 = "CREATE TABLE " + TABLE_RIDE_VEHICLE_NAME + "("
                + KEY_BIKE_ID + " INTEGER PRIMARY KEY autoincrement not null,"+KEY_BIKE_CATOG+ " TEXT,"
                + KEY_BIKE_NAME + " TEXT)";
        		
        db.execSQL(CREATE_CONTACTS_TABLE3);
    }
    
    /***Upgrading database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** Drop older table if existed*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_VEHICLE_NAME);
 
        /** Create tables again*/
        onCreate(db);
    }
    
    /** Adding new vehicle name*/
    void addContact(VehicleNameGetSet contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BIKE_ID, contact.getID()); //Get ID
        values.put(KEY_BIKE_CATOG, contact.getCategory());
        values.put(KEY_BIKE_NAME, contact.getName()); // Get name
        db.insertWithOnConflict(TABLE_RIDE_VEHICLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        //db.insert(TABLE_RIDE_VEHICLE_NAME, null, values);
        Log.e(values.getAsString(KEY_BIKE_ID), "Done");
        db.close(); // Closing database connection
    }
    /**Display all the vehicles in vehicle master*/
	public List<VehicleNameGetSet> getVehicleFromNameMaster(String categ) {
        List<VehicleNameGetSet> contactList = new ArrayList<VehicleNameGetSet>();
        String selectQuery = "SELECT  * FROM "+TABLE_RIDE_VEHICLE_NAME+ " WHERE "+KEY_BIKE_CATOG+"='"+categ+"'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //int len=cursor.getCount();
        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
            	VehicleNameGetSet contact = new VehicleNameGetSet();
                contact.setID(cursor.getString(0));
                contact.setName(cursor.getString(2));
                /** Adding vehicles to list*/
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }
}
