///** Ridio v1.0
// * 	Purpose	   : Database Handler for Saving the received vehicles from Helix DB 
// *  Created by : Abish 
// *  Created Dt : 
// *  Modified on: 
// *  Verified by: Srinivas
// *  Verified Dt: 
// * **/
//
//package ridio.helixtech;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class VehicleGetBikeNameFromDB  extends SQLiteOpenHelper{
//	Context context;
//	private static final String TABLE_RIDE_CUSTOMER = "bookings"; // bookings table name
//	private static final String KEY_BIKE_ID = "v_id";
//    private static final String KEY_BIKE_NAME = "v_name";
//	private static final String TABLE_RIDE_VEHICLE_NAME = "vehicle_name_master"; // Contacts table name
//	
//	/**Constructor*/
//	public VehicleGetBikeNameFromDB(Context context, String name, CursorFactory factory, int version) {
//		super(context, name, factory, version);
//	}
//	/**Constructor*/
//	public VehicleGetBikeNameFromDB(Context con) {
//		super(con, "ridio", null, 1);
//	}
//	/**Display all the vehicles in vehicle master*/
////	public List<VehicleNameGetSet> getVehicleFromNameMaster() {
////        List<VehicleNameGetSet> contactList = new ArrayList<VehicleNameGetSet>();
////        String selectQuery = "SELECT  * FROM "+TABLE_RIDE_VEHICLE_NAME+ " WHERE "+;
//// 
////        SQLiteDatabase db = this.getWritableDatabase();
////        Cursor cursor = db.rawQuery(selectQuery, null);
////        int len=cursor.getCount();
////        /** looping through all rows and adding to list*/
////        if (cursor.moveToFirst()) {
////            do {
////            	VehicleNameGetSet contact = new VehicleNameGetSet();
////                contact.setID(cursor.getString(0));
////                contact.setName(cursor.getString(2));
////                /** Adding vehicles to list*/
////                contactList.add(contact);
////            } while (cursor.moveToNext());
////        }
////        return contactList;
////    }
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		String CREATE_CONTACTS_TABLE3 = "CREATE TABLE " + TABLE_RIDE_VEHICLE_NAME + "("
//                + KEY_BIKE_ID + " INTEGER PRIMARY KEY autoincrement not null," + KEY_BIKE_NAME + " TEXT)";
//        		
//        db.execSQL(CREATE_CONTACTS_TABLE3);
//	}
//
//	 @Override
//	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//	        /** Drop older table if existed*/
//	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_CUSTOMER);
//	 
//	        /** Create tables again*/
//	        onCreate(db);
//	    }
//
//}
