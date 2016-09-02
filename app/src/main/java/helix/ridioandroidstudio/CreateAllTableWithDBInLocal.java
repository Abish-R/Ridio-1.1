/** Ridio v1.0.1
 * 	Purpose	   : Create All Tables in Ridio DB in locally
 *  Created by : Abish 
 *  Created Dt : old file
 *  Modified on: 22-02-2016 (bookings comment field added)
 *  Verified by:
 *  Verified Dt:
 * **/


package helix.ridioandroidstudio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateAllTableWithDBInLocal extends SQLiteOpenHelper {
	/** Default Constructors**/
	public CreateAllTableWithDBInLocal(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	/** Default Constructors**/
	public CreateAllTableWithDBInLocal(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}
	
    private static final int DATABASE_VERSION = 1;   // Database Version 
    private static final String DATABASE_NAME = "ridio"; // Database Name 	
	
	private static final String TABLE_RIDE_RATE = "rate_master"; // Contacts table name
	private static final String TABLE_RIDE_CUSTOMER = "bookings"; // bookings table name
	private static final String TABLE_RIDE_VEHICLE = "vehicle_master"; // Contacts table name
	private static final String TABLE_RIDE_VEHICLE_NAME = "vehicle_name_master"; // Contacts table name
	
	/** Create Rate Master Table Columns names**/
	private static final String KEY_ID1 = "rate_id";
	private static final String KEY_RATE = "rate_small";
    private static final String KEY_RATE1 = "rate_medium";
    private static final String KEY_RATE2 = "rate_high";
    private static final String KEY_RATE_DATE = "rate_date";
    private static final String KEY_DATE = "created_date_rate";//changed
    private static final String KEY_RATE_UP_STATE = "rate_upload_status";
    
    /** Create Vehicle Master Table Columns names**/
    private static final String KEY_IDV = "vehicle_id";
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
    private static final String KEY_VEHICLE_VEND_ID = "vendor_id_vehicle";//changed
    private static final String KEY_VEHICLE_UPLOAD_STATUS = "vehicle_upload_status";
    
    /** Create Bookings Table Columns names**/
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
	
    /** Create Vehicle_Name_Master Table Columns names**/
    private static final String KEY_BIKE_ID = "v_id";
    private static final String KEY_BIKE_CATOG = "v_category";
    private static final String KEY_BIKE_NAME = "v_name";
		
	 @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {        
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_CUSTOMER);  // Drop older table if existed      
        onCreate(db);	// Create tables again
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
        /** Create Rate Master Table **/
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RIDE_RATE + "("
                + KEY_ID1 + " INTEGER PRIMARY KEY autoincrement not null,"+ KEY_RATE+" TEXT,"
                + KEY_RATE1 + " TEXT,"+ KEY_RATE2 + " TEXT,"+KEY_RATE_DATE+" DATETIME,"+
                KEY_DATE+ " TEXT,"+KEY_RATE_UP_STATE+" TEXT)";//DATETIME DEFAULT CURRENT_DATE"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        /** Create Vehicle Master Table **/
	    String CREATE_CONTACTS_TABLE1 = "CREATE TABLE " + TABLE_RIDE_VEHICLE + "("
	       + KEY_IDV + " INTEGER PRIMARY KEY autoincrement not null," + KEY_COLOR + " TEXT,"+
	       KEY_VEH_CATEGORY+" TEXT,"+KEY_NUMBER + " TEXT,"+KEY_MODEL + " TEXT,"+KEY_SERVICE_DATE + 
	       " TEXT,"+ KEY_PURCHASE_DATE + " TEXT,"+KEY_REPAIR_PENDING + " TEXT,"+KEY_PAST_ISSUES+
	      " TEXT,"+KEY_CREATED_DATE+ " TEXT,"+KEY_VEHICLE_STATUS+ " TEXT,"+KEY_VEHICLE_VEND_ID+" TEXT,"+
	      KEY_VEHICLE_UPLOAD_STATUS+ " TEXT)";
	    db.execSQL(CREATE_CONTACTS_TABLE1);

        /** Create Bookings Table **/
        String CREATE_CONTACTS_TABLE2 = "CREATE TABLE " + TABLE_RIDE_CUSTOMER + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement not null," + KEY_Name + " TEXT,"+
                KEY_MOBILE1+ " TEXT,"+ KEY_MOBILE2 + " TEXT,"+ KEY_EMAIL+ " TEXT,"+
                KEY_HOTEL_NAME + " TEXT,"+ KEY_BIKE_NAME_SAVE+ " TEXT,"+KEY_BIKE_COLOR+ " TEXT,"+
                KEY_BIKE_REG_NO+ " TEXT,"+KEY_PER_DAY_RATE + " TEXT,"+ KEY_FROM_DATE+ " DATETIME,"+
                KEY_TO_DATE + " DATETIME,"+ KEY_NO_OF_DAYS+ " TEXT,"+ KEY_TOTAL_COST + " TEXT,"+
                KEY_VENDOR_ID+ " TEXT,"+ KEY_BOOK_DATE + " DATETIME DEFAULT CURRENT_DATE,"+
                KEY_TRUST_RATING+ " TEXT,"+KEY_BIKE_STATUS + " TEXT,"+KEY_UPLOAD_STATUS+" TEXT," +
                KEY_FEEDBACK+" TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE2);

        /** Create Vehicle Name Master Table **/
        String CREATE_CONTACTS_TABLE3 = "CREATE TABLE " + TABLE_RIDE_VEHICLE_NAME + "("
                + KEY_BIKE_ID + " INTEGER PRIMARY KEY autoincrement not null,"+KEY_BIKE_CATOG+ " TEXT,"
        		+ KEY_BIKE_NAME + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE3);
		}
}
