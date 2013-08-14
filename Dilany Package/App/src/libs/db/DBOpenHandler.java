package libs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ksa.dilny.utils.Consts;

public class DBOpenHandler extends SQLiteOpenHelper {

    public DBOpenHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public static final String DATABASE_NAME = "dilny.db";
    public static final String DATABASE_TABLE_CATS = "cats";
    public static final String DATABASE_TABLE_LEVELS = "levels";
    public static final String DATABASE_TABLE_FAVORITES = "favorites";
    public static final String DATABASE_TABLE_IMAGES = "images";
    public static final int DATABASE_VERSION = 1;
    
  
    public static final String KEY_IMAGES_URL="url";
    public static final String KEY_IMAGES_PATH = "path";
    public static final String KEY_IMAGES_ID= "id"; 
    
    public static final String KEY_Listing_ID = "listing_id";
    public static final String KEY_Listing_TITLE = "title";
    public static final String KEY_Listing_DESC = "desc";
    public static final String KEY_Listing_THUMB = "thumb";
    public static final String KEY_Listing_IMAGE = "image";
    public static final String KEY_Listing_RATINGS = "ratings";
    public static final String KEY_Listing_COUNTRY = "country";
    public static final String KEY_Listing_PRICE_JSONArray = "price";
    public static final String KEY_Listing_ADDRESS_1 = "address_line_1";
    public static final String KEY_Listing_ADDRESS_2 = "address_line_2";
    public static final String KEY_Listing_LAT = "lat";
    public static final String KEY_Listing_LNG = "lng";
    public static final String KEY_Listing_DISTANCE = "distance";
    public static final String KEY_Listing_JSON_ITEM = "JsonItem";
    public static final String KEY_Listing_PRICE_MAX = "priceMax";
    public static final String KEY_Listing_PRICE_MIN = "PriceMin";
    public static final String KEY_Listing_DB_ID = "id";
    public static final String KEY_Listing_REVIEWS = "reviews";
    public static final String KEY_Listing_PHONE= "phone";
    public static final String KEY_Listing_CAT_ID= "cat_id";
	
 
   
    
    private static final String  CREATE_TABLE_CATS =
            "create table " + DATABASE_TABLE_CATS + " ("
            + Consts.CATS_ID   + "  text not null, "
            + Consts.CATS_NAME + " text not null, "
            + Consts.CATS_IMAGE + " text not null, "
            + Consts.CATS_TYPEID + " text not null, "
            + Consts.CATS_ICON + " text not null);" ;
    
    
 
    private static final String  CREATE_TABLE_LEVELS =
            "create table " + DATABASE_TABLE_LEVELS + " ("
            + Consts.LEV_ID   + "  text not null, "
            + Consts.LEV_NAME + " text not null, " 
            + Consts.LEV_CATS_JSON + " text not null, " 
            + Consts.LEV_TYPE + " text not null);" ;
    
    private static final String  CREATE_TABLE_IMAGES =
            "create table " + DATABASE_TABLE_IMAGES + " ("
            + KEY_IMAGES_ID  + "  integer primary key autoincrement, "
            + KEY_IMAGES_URL + " text UNIQUE not null, "
            + KEY_IMAGES_PATH + " text not null);" ;
    
    private static final String CREATE_TABLE_FAVORITES ="create table " + DATABASE_TABLE_FAVORITES + " ("
	+ KEY_Listing_DB_ID   + "  integer primary key autoincrement , "
	+ KEY_Listing_ID + " text UNIQUE , "
	+ KEY_Listing_TITLE + " text , "
	+ KEY_Listing_DESC + " text , "
	+ KEY_Listing_IMAGE + " text , "
	+ KEY_Listing_THUMB + " text , "
	+ KEY_Listing_RATINGS + " text , "
	+ KEY_Listing_COUNTRY + " text , "
	+ KEY_Listing_PRICE_JSONArray + " text , "
	+ KEY_Listing_ADDRESS_1 + " text , "
	+ KEY_Listing_ADDRESS_2 + " text , "
	+ KEY_Listing_LAT + " text , "
	+ KEY_Listing_LNG + " text , "
	+ KEY_Listing_DISTANCE + " text , "
	+ KEY_Listing_JSON_ITEM + " text , "    		
	+ KEY_Listing_PRICE_MIN + " text , "
	+ KEY_Listing_REVIEWS + " text , "
	+ KEY_Listing_PHONE + " text , "	
	+ KEY_Listing_CAT_ID + " text , "	
	+ KEY_Listing_PRICE_MAX + " text );" ; 

    @Override
    public void onCreate(SQLiteDatabase _db) {
        _db.execSQL(CREATE_TABLE_CATS);
        _db.execSQL(CREATE_TABLE_LEVELS);        
        _db.execSQL(CREATE_TABLE_FAVORITES);
        _db.execSQL(CREATE_TABLE_IMAGES);
        
        System.out.println(CREATE_TABLE_FAVORITES);System.out.println(CREATE_TABLE_LEVELS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version "
                + _oldVersion + " to "
                + _newVersion + ", which will destroy all old data");
        // Drop the old table.
        _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CATS);
        _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_FAVORITES);
        _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_IMAGES);
        _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LEVELS);
        // Create a new one.
        onCreate(_db); 
    }
}
