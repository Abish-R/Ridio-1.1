/** Ridio v1.0
 * 	Purpose	   : VehicleDatabaseHandler
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 18-02-2016 (Updated : Pending Vehicle not showing, fixed)
 *  Verified by:
 *  Verified Dt:
 * **/


package helix.ridioandroidstudio;

import helix.general.AlertMessages;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VehicleDatabaseHandler extends SQLiteOpenHelper {
	 
    /** All Static variables*/
    private static final int DATABASE_VERSION = 1;			 		   // Database Version
    private static final String DATABASE_NAME = "ridio";		 	   // Database Name    
    private static final String TABLE_RIDE_VEHICLE = "vehicle_master"; // Contacts table name
 
    /** Contacts Table Columns names*/
    private static final String KEY_ID = "vehicle_id";
    private static final String KEY_COLOR = "vehicle_color";
    private static final String KEY_VEH_CATEGORY = "vehicle_category";
    private static final String KEY_NUMBER = "vehicle_number";
    private static final String KEY_MODEL = "vehicle_model_name";
    private static final String KEY_SERVICE_DATE = "last_service_date";
    private static final String KEY_PURCHASE_DATE = "purchased_date";
    private static final String KEY_REPAIR_PENDING = "known_repair_issues_pending";
    private static final String KEY_PAST_ISSUES = "past_repairs_and_issues";
    private static final String KEY_CREATED_DATE = "created_date_vehicle";//changed
    private static final String KEY_VEHICLE_STATUS = "vehicle_status";
    private static final String KEY_VEHICLE_UPLOAD_STATUS = "vehicle_upload_status";
    private static final String KEY_VEHICLE_VEND_ID = "vendor_id_vehicle";//changed
 
    /**Contructor*/
    public VehicleDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 

	/** Creating Tables*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RIDE_VEHICLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement not null," + KEY_COLOR + " TEXT,"+
                KEY_VEH_CATEGORY+" TEXT,"+KEY_NUMBER + " TEXT,"+KEY_MODEL + " TEXT,"+KEY_SERVICE_DATE + 
                " TEXT,"+ KEY_PURCHASE_DATE + " TEXT,"+KEY_REPAIR_PENDING + " TEXT,"+KEY_PAST_ISSUES+
                " TEXT,"+KEY_CREATED_DATE+ " TEXT,"+KEY_VEHICLE_STATUS+ " TEXT,"+KEY_VEHICLE_VEND_ID+" TEXT,"
                		+KEY_VEHICLE_UPLOAD_STATUS+ " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
     
    /** Upgrading database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** Drop older table if existed*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_VEHICLE); 
        /** Create tables again*/
        onCreate(db);
    }
 
    /*** All CRUD(Create, Read, Update, Delete) Operations  */
 
    /** Adding new Vehicle*/
    int addVehicle(VehicleGetSetter contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, contact.getID()); 						// Get ID
        values.put(KEY_COLOR, contact.getColor()); 					// Get Color
        values.put(KEY_VEH_CATEGORY, contact.getCategory());
        values.put(KEY_NUMBER,contact.getNumber()); 				// Get Number
        values.put(KEY_MODEL, contact.getModelName()); 				// Get Model
        values.put(KEY_SERVICE_DATE, contact.getSeriveDate()); 		// Get Service Date
        values.put(KEY_PURCHASE_DATE,contact.getPurchaseDate()); 	// Get Purchase date
        values.put(KEY_REPAIR_PENDING, contact.getPendingRepair()); // Get Pending Repairs
        values.put(KEY_PAST_ISSUES, contact.getPastIssues()); 		// Get Past Issues
        values.put(KEY_CREATED_DATE, contact.getCreatedDate()); 	// Get Created Date
        values.put(KEY_VEHICLE_STATUS, contact.getStatus());		// Get Vehicle Status
        values.put(KEY_VEHICLE_VEND_ID, contact.getVendorId());		// Get Vendor ID
        values.put(KEY_VEHICLE_UPLOAD_STATUS, contact.getUploadState());
        //db.insert(TABLE_RIDE_VEHICLE, null, values);

        String number=contact.getNumber();
        if(getVehiclesCountByNumber(number)>0) {
            Log.d("Count: ", getVehiclesCountByNumber(number) + "");
           // db.close(); // Closing database connection
            return 0;
        }
        else {
            /** Inserting Row*/
            //db.insertWithOnConflict(TABLE_RIDE_VEHICLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            db.insert(TABLE_RIDE_VEHICLE, null, values);
            //db.close(); // Closing database connection
            return 1;
        }
    }
     
    /** Getting All Vehicles*/
    public List<VehicleGetSetter> getAllContacts() {
        List<VehicleGetSetter> contactList = new ArrayList<VehicleGetSetter>();
        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_VEHICLE;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        /** looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                VehicleGetSetter contact = new VehicleGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setColor(cursor.getString(1));
                contact.setCategory(cursor.getString(2));
                contact.setNumber(cursor.getString(3));
                contact.setModelName(cursor.getString(4));
                contact.setServiceDate(cursor.getString(5));
                contact.setPurchaseDate(cursor.getString(6));
                contact.setPendingRepair(cursor.getString(7));
                contact.setPastIssues(cursor.getString(8));
                contact.setStatus(cursor.getString(9));
                contact.setUploadState(cursor.getString(10));
                /** Adding contact to list*/
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }
    
    /** Getting All Vehicles with Status P*/
//    public List<VehicleGetSetter> getVehicleByStatusP() {
//        List<VehicleGetSetter> contactList = new ArrayList<VehicleGetSetter>();
//        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_VEHICLE+ " WHERE "+KEY_VEHICLE_STATUS+" = 'P'";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        /** looping through all rows and adding to list*/
//        if (cursor.moveToFirst()) {
//            do {
//                VehicleGetSetter contact = new VehicleGetSetter();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setColor(cursor.getString(1));
//                contact.setCategory(cursor.getString(2));
//                contact.setNumber(cursor.getString(3));
//                contact.setModelName(cursor.getString(4));
//                contact.setServiceDate(cursor.getString(5));
//                contact.setPurchaseDate(cursor.getString(6));
//                contact.setPendingRepair(cursor.getString(7));
//                contact.setPastIssues(cursor.getString(8));
//                contact.setStatus(cursor.getString(9));
//                /** Adding contact to list*/
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        return contactList;
//    }
    
    /** Getting All Vehicles with Status R*/
//    public List<VehicleGetSetter> getVehicleByStatusR() {
//        List<VehicleGetSetter> contactList = new ArrayList<VehicleGetSetter>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_VEHICLE+ " WHERE "+KEY_VEHICLE_STATUS+" = 'R'";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        /**looping through all rows and adding to list*/
//        if (cursor.moveToFirst()) {
//            do {
//                VehicleGetSetter contact = new VehicleGetSetter();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setColor(cursor.getString(1));
//                contact.setCategory(cursor.getString(2));
//                contact.setNumber(cursor.getString(3));
//                contact.setModelName(cursor.getString(4));
//                contact.setServiceDate(cursor.getString(5));
//                contact.setPurchaseDate(cursor.getString(6));
//                contact.setPendingRepair(cursor.getString(7));
//                contact.setPastIssues(cursor.getString(8));
//                contact.setStatus(cursor.getString(9));
//                /** Adding contact to list*/
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        return contactList;
//    }
    
    /** Getting All Vehicles with Upload Status N*/
    public List<VehicleGetSetter> getVehicleByUploadStatusN() {
        List<VehicleGetSetter> contactList = new ArrayList<VehicleGetSetter>();

        String selectQuery = "SELECT  * FROM " + TABLE_RIDE_VEHICLE+ " WHERE "+KEY_VEHICLE_UPLOAD_STATUS+" = 'N'";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        /**looping through all rows and adding to list*/
        if (cursor.moveToFirst()) {
            do {
                VehicleGetSetter contact = new VehicleGetSetter();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setColor(cursor.getString(1));
                contact.setCategory(cursor.getString(2));
                contact.setNumber(cursor.getString(3));
                contact.setModelName(cursor.getString(4));
                contact.setServiceDate(cursor.getString(5));
                contact.setPurchaseDate(cursor.getString(6));
                contact.setPendingRepair(cursor.getString(7));
                contact.setPastIssues(cursor.getString(8));
                contact.setCreatedDate(cursor.getString(9));
                contact.setStatus(cursor.getString(10));
                contact.setVendorId(cursor.getString(11));
                contact.setUploadState(cursor.getString(12));
                /** Adding contact to list*/
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }
    
    /**Get Vehicle Name related the bike number passed*/
    public String getVehicleName(String number){
    	String selectQuery = "SELECT  vehicle_model_name from " + TABLE_RIDE_VEHICLE+ " WHERE "+
    			KEY_NUMBER+" = '"+number+"'";
    	 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
            	return cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return null;
    }
    /**Vehicles names shown in Customer Booking(Huge process with database from & to date)*/
    public List<VehicleGetSetter> getVehicleListToCustomer(String id,String categ,String from_dt,String todt){
    	List<VehicleGetSetter> contactList = new ArrayList<VehicleGetSetter>();
    	List<VehicleGetSetter> contactList1 = new ArrayList<VehicleGetSetter>();
    	List<VehicleGetSetter> contactList2 = new ArrayList<VehicleGetSetter>();
    	String selectQuery = "SELECT DISTINCT vehicle_number from " + TABLE_RIDE_VEHICLE+ " WHERE "+
    			KEY_VEHICLE_VEND_ID+" = '"+id+"' AND "+KEY_VEHICLE_STATUS+" = 'P' AND "+ 
    			KEY_VEH_CATEGORY+" = '"+categ+"'";    	
    	 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
            	VehicleGetSetter contact = new VehicleGetSetter();
                contact.setNumber(cursor.getString(0));
                //contact.setColor(cursor.getString(1));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        List<VehicleGetSetter> vehList=contactList;
        for(VehicleGetSetter counter : vehList){
            String temp=counter.getNumber();
            String selectQuery1 = "SELECT bike_reg_number,from_date,to_date from bookings WHERE vendor_id = '"+id+"' AND "+
                    "bike_reg_number = '"+temp+"' AND bike_status='P'";//(( from_date > '"+from_dt+"' AND from_date > '"+todt+"') OR "
                            //+ "(to_date < '"+from_dt+"' AND to_date < '"+todt+"'))";
            Cursor cursor1 = db.rawQuery(selectQuery1, null);
            if (cursor1.moveToFirst()) {
                String[] vehicle_number=new String[cursor1.getCount()];
                String[] from_date=new String[cursor1.getCount()];
                String[] to_date=new String[cursor1.getCount()];
                //if(cursor1.getCount()>0){
                    int i=0;
                    do {
                        vehicle_number[i]=(cursor1.getString(0));
                        from_date[i]=(cursor1.getString(1));//dummy
                        to_date[i]=(cursor1.getString(2));//dummy
                        i++;
                    } while (cursor1.moveToNext());
                    try {
                        List<String> vehfound=getDateComparison(vehicle_number,from_date,to_date,from_dt,todt);
                        for(int ii=0; ii<vehfound.size(); ii++) {
                            VehicleGetSetter contact = new VehicleGetSetter();
                            contact.setNumber(vehfound.get(ii).toString());
                            contactList1.add(contact);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                //}
            }
        }
        List<VehicleGetSetter> vehSelList=contactList1;
        if(vehSelList.size()>0)
        for(VehicleGetSetter counter1 : vehSelList){
            String temp=counter1.getNumber();
        String selectQuery2 = "SELECT DISTINCT "+KEY_MODEL+","+KEY_COLOR+","+KEY_NUMBER+" from " + TABLE_RIDE_VEHICLE+ " WHERE ("+
    			KEY_VEHICLE_VEND_ID+" = '"+id+"' AND "+KEY_VEHICLE_STATUS+" = 'P' AND "+
    			KEY_VEH_CATEGORY+" = '"+categ+"' AND vehicle_number = '"+temp+"')";
        Cursor cursor2 = db.rawQuery(selectQuery2, null);
        if (cursor2.moveToFirst()) {
            do {
            	VehicleGetSetter contact2 = new VehicleGetSetter();
                contact2.setModelName(cursor2.getString(0));
                contact2.setColor(cursor2.getString(1));
                contact2.setNumber(cursor2.getString(2));
                contactList2.add(contact2);
            } while (cursor2.moveToNext());
        }
        String selectQuery3 = "SELECT DISTINCT "+KEY_MODEL+","+KEY_COLOR+","+KEY_NUMBER+" from " + TABLE_RIDE_VEHICLE+ " WHERE ("+
    			KEY_VEHICLE_VEND_ID+" = '"+id+"' AND "+KEY_VEHICLE_STATUS+" = 'R' AND "+ 
    			KEY_VEH_CATEGORY+" = '"+categ+"')";
        Cursor cursor3 = db.rawQuery(selectQuery3, null);
        if (cursor3.moveToFirst()) {
            do {
            	VehicleGetSetter contact2 = new VehicleGetSetter();
                contact2.setModelName(cursor3.getString(0));
                contact2.setColor(cursor3.getString(1));
                contact2.setNumber(cursor3.getString(2));
                contactList2.add(contact2);
            } while (cursor3.moveToNext());
        }
        }
        else{
        String selectQuery4 = "SELECT DISTINCT "+KEY_MODEL+","+KEY_COLOR+","+KEY_NUMBER+" from " + TABLE_RIDE_VEHICLE+ " WHERE ("+
    			KEY_VEHICLE_VEND_ID+" = '"+id+"' AND "+KEY_VEHICLE_STATUS+" = 'R' AND "+ 
    			KEY_VEH_CATEGORY+" = '"+categ+"')";
        Cursor cursor3 = db.rawQuery(selectQuery4, null);
        if (cursor3.moveToFirst()) {
            do {
            	VehicleGetSetter contact2 = new VehicleGetSetter();
                contact2.setModelName(cursor3.getString(0));
                contact2.setColor(cursor3.getString(1));
                contact2.setNumber(cursor3.getString(2));
                contactList2.add(contact2);
            } while (cursor3.moveToNext());
        }
        }
        return contactList2;//contactList;
    }
    private List getDateComparison(String[] veh, String[] from, String[] to,String from1, String to1) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.ENGLISH);
    	Date table_from,table_to,input_from,input_to;
    	long diff=0,diff1=0,diff2=0,diff3=0;
    	int count=0,county=0;;
        vehic_tmp.clear();
        vehic.clear();
    	for(int i=0;i<veh.length;i++){
            count++;
            table_from=sdf.parse(from[i].toString());
            table_to=sdf.parse(to[i].toString());
            input_from=sdf.parse(from1.toString());
            input_to=sdf.parse(to1.toString());
            table_from=sdf.parse(sdf.format(table_from));
            table_to=sdf.parse(sdf.format(table_to));
            input_from=sdf.parse(sdf.format(input_from));
            input_to=sdf.parse(sdf.format(input_to));
            diff=table_from.getTime()-input_from.getTime();
            diff1=table_from.getTime()-input_to.getTime();
            diff2=input_from.getTime()-table_to.getTime();
            diff3=input_to.getTime()-table_to.getTime();
            if((diff > 0 && diff1>0) || (diff2 > 0 && diff3>0)){
                removeAndAddList(veh[i],1);
                Log.d("Date", " ok with from");
            }
            else {
                removeAndAddList(veh[i],2);
            }
        }
        if(veh.length==count)
            return vehic;//= veh[0];
        else
            return null;
	}
    List<String> vehic=new ArrayList<String>();
    List<String> vehic_tmp=new ArrayList<String>();
    public void removeAndAddList(String vehi,int val){
        /**Removes Duplicate values*/
        if(val==1) {
            vehic.add(vehi);
            for (int ii = 0; ii < vehic.size()-1; ii++) {
                if (vehic.get(ii).equals(vehic.get(ii+1))) {
                    vehic.remove(ii);
                    ii--;
                }
            }
        }
        else if(val==2)
            vehic_tmp.add(vehi);
        //if(vehic.size()>1)
        for( int ii=0; ii < vehic.size();ii++) {
            for( int iii=0; iii < vehic_tmp.size();iii++) {
                if (vehic.get(ii).equals(vehic_tmp.get(iii))) {
                    vehic.remove(ii);
                    ii--;
                    if(vehic.size()==0)
                        iii=vehic_tmp.size()+1;
                }
            }
        }
    }

	/**Vehicles color shown in Customer Booking (Code Changed)*/
//    public List<VehicleGetSetter> getVehicleColorListToCustomer(String id,String bike_name){
//    	List<VehicleGetSetter> contactList = new ArrayList<VehicleGetSetter>();
//    	String selectQuery = "SELECT DISTINCT "+KEY_COLOR+" from " + TABLE_RIDE_VEHICLE+ " WHERE "+
//    			KEY_VEHICLE_VEND_ID+" = '"+id+"' AND "+KEY_MODEL+" = '"+
//    			bike_name+"' AND "+KEY_VEHICLE_STATUS+" = 'R'";
//    	 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//            	Log.d("Database Vehicle Name Getter", cursor.getString(0));
//            	VehicleGetSetter contact = new VehicleGetSetter();
//                contact.setColor(cursor.getString(0));
//                //contact.setColor(cursor.getString(1));
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        return contactList;
//    }
//    /**Vehicles number shown in Customer Booking*/
//    public List<VehicleGetSetter> getVehicleNumberListToCustomer(String id,String name,String color){
//    	List<VehicleGetSetter> contactList = new ArrayList<VehicleGetSetter>();
//    	String selectQuery = "SELECT "+KEY_NUMBER+" from " + TABLE_RIDE_VEHICLE+ " WHERE "+
//    			KEY_VEHICLE_VEND_ID+" = '"+id+"' AND "+KEY_MODEL+" = '"+name+
//    			"' AND "+KEY_COLOR+" = '"+color+"' AND "+KEY_VEHICLE_STATUS+" = 'R'";
//    	 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//            	Log.d("Database Vehicle Name Getter", cursor.getString(0));
//            	VehicleGetSetter contact = new VehicleGetSetter();
//                contact.setNumber(cursor.getString(0));
//                //contact.setColor(cursor.getString(1));
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        return contactList;
//    }
    
    /** Updating upload status*/
    public int updateUploadStateDB(String bike_no,String state) {
        SQLiteDatabase db = this.getWritableDatabase(); 
        ContentValues values = new ContentValues();
        values.put(KEY_VEHICLE_UPLOAD_STATUS, state);
 
        /** updating row*/
        return db.update(TABLE_RIDE_VEHICLE, values, KEY_NUMBER + " = ?",
                new String[] {bike_no});
    }
    
    /** Updating single vehicle status*/
    public int updateContact(VehicleGetSetter contact) {
        SQLiteDatabase db = this.getWritableDatabase(); 
        ContentValues values = new ContentValues();
        //values.put(KEY_, contact.getRate());
        values.put(KEY_VEHICLE_STATUS, contact.getStatus());
 
        /** updating row*/
        return db.update(TABLE_RIDE_VEHICLE, values, KEY_NUMBER + " = ?",
                new String[] { String.valueOf(contact.getNumber()) });
    }

    /** Updating single vehicle details from Vehicle Master*/
    public int updateVehicles(VehicleGetSetter contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COLOR, contact.getColor());
        values.put(KEY_VEH_CATEGORY, contact.getCategory());
        values.put(KEY_SERVICE_DATE, contact.getSeriveDate());
        values.put(KEY_PURCHASE_DATE, contact.getPurchaseDate());
        values.put(KEY_REPAIR_PENDING, contact.getPendingRepair());
        values.put(KEY_PAST_ISSUES, contact.getPastIssues());
        values.put(KEY_CREATED_DATE, contact.getCreatedDate());
        values.put(KEY_VEHICLE_UPLOAD_STATUS, contact.getUploadState());

        /** updating row*/
        return db.update(TABLE_RIDE_VEHICLE, values, KEY_NUMBER + " = ?",
                new String[] { contact.getNumber() });
    }
 
    /** Deleting single vehicle*/
    public void deleteSelectedVehicle(String veh_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RIDE_VEHICLE, KEY_NUMBER + " = ?",
                new String[]{veh_no});
        db.close();
    }
 
    /** Getting vehicles Count*/
    public int getVehiclesCount(String vend_id) {
        String countQuery = "SELECT  * FROM " + TABLE_RIDE_VEHICLE +" WHERE "+KEY_VEHICLE_VEND_ID+" = '"+vend_id+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = cursor.getCount();
        Log.d("Count of vehicle : ", "" + cursor.getCount());
        cursor.close();
        db.close();
        return temp;
    }
    /** Getting vehicles Count*/
    public int getVehiclesCountWithStatusN() {
        String countQuery = "SELECT  * FROM " + TABLE_RIDE_VEHICLE +" WHERE "+KEY_VEHICLE_UPLOAD_STATUS+" = 'N'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = cursor.getCount();
        Log.d("Count of vehicle : ", ""+cursor.getCount());
        cursor.close();
        db.close();
        return temp;
    }

    /** Getting vehicles Count*/
    public int getVehiclesCountByNumber(String veh_no) {
        String countQuery = "SELECT  * FROM " + TABLE_RIDE_VEHICLE +" WHERE "+KEY_NUMBER+" = '"+veh_no+"'";
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(countQuery, null);
        int temp = cursor.getCount();
        Log.d("Count of vehicle : ", ""+cursor.getCount());
        cursor.close();
        //db1.close();
        return temp;
    }
}