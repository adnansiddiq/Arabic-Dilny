package libs.db;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ksa.dilny.DataModel.CatsModel;
import com.ksa.dilny.DataModel.LevelModel;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.Consts;
import com.yahia.libs.db.ExportDB;

public class DBAdapter {

	private SQLiteDatabase db;
	private final Context context;

	private DBOpenHandler dbHandler;

	public DBAdapter(Context _context) {
		this.context = _context;
		dbHandler = new DBOpenHandler(context, DBOpenHandler.DATABASE_NAME,
				null, DBOpenHandler.DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	public void open() throws SQLiteException {
		try {
			db = dbHandler.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHandler.getReadableDatabase();
		}
	}

	public long insertIntoCats(CatsModel cat) {
		// Create a new row of values to insert.
		ContentValues newTaskValues = new ContentValues();
		// Assign values for each row.
		newTaskValues.put(Consts.CATS_NAME, cat.getName());
		newTaskValues.put(Consts.CATS_ICON, cat.getIcon());
		newTaskValues.put(Consts.CATS_ID, cat.getId());
		newTaskValues.put(Consts.CATS_IMAGE, cat.getImage());
		newTaskValues.put(Consts.CATS_TYPEID, cat.getTypeID());
		// Insert the row.
		return db
				.insert(DBOpenHandler.DATABASE_TABLE_CATS, null, newTaskValues);

	}

	public long insertIntoLevels(LevelModel lev) {
		// Create a new row of values to insert.
		ContentValues newTaskValues = new ContentValues();

		// Assign values for each row.
		newTaskValues.put(Consts.LEV_ID, lev.getId());
		newTaskValues.put(Consts.LEV_NAME, lev.getName());
		newTaskValues.put(Consts.LEV_TYPE, lev.getTypeID());
		newTaskValues.put(Consts.LEV_CATS_JSON, lev.getCatsJson());
		// Insert the row.
		return db.insert(DBOpenHandler.DATABASE_TABLE_LEVELS, null,
				newTaskValues);

	}
 

	public long insertIntoFavorites(ListItemModel item) {
		System.out.println("insertIntoFavorites");
		System.out.println("KEY_Listing_THUMB : "+item.getThumb());
		// Create a new row of values to insert.
		ContentValues newTaskValues = new ContentValues();
		// Assign values for each row.
		newTaskValues.put(DBOpenHandler.KEY_Listing_ID, item.getId());
		newTaskValues.put(DBOpenHandler.KEY_Listing_TITLE, item.getTitle());
		newTaskValues.put(DBOpenHandler.KEY_Listing_DESC, item.getDesc());
		newTaskValues.put(DBOpenHandler.KEY_Listing_THUMB, item.getThumb());
		newTaskValues.put(DBOpenHandler.KEY_Listing_IMAGE, item.getImage());
		newTaskValues.put(DBOpenHandler.KEY_Listing_RATINGS, item.getRatings());
		newTaskValues.put(DBOpenHandler.KEY_Listing_COUNTRY, item.getCountry());
		//newTaskValues.put(DBOpenHandler.KEY_Listing_PRICE_JSONArray, item.getPrice().toString());
		newTaskValues.put(DBOpenHandler.KEY_Listing_ADDRESS_1,
				item.getAddress_line_1());
		newTaskValues.put(DBOpenHandler.KEY_Listing_ADDRESS_2,
				item.getAddress_line_2());
		newTaskValues.put(DBOpenHandler.KEY_Listing_LAT, item.getlat());
		newTaskValues.put(DBOpenHandler.KEY_Listing_LNG, item.getlng());
		newTaskValues.put(DBOpenHandler.KEY_Listing_DISTANCE,
				item.getDistance());
		newTaskValues.put(DBOpenHandler.KEY_Listing_JSON_ITEM,
				item.getJsonItem());
		//newTaskValues.put(DBOpenHandler.KEY_Listing_PRICE_MAX,				item.getPriceMax());
		//newTaskValues.put(DBOpenHandler.KEY_Listing_PRICE_MIN,				item.getPriceMin());
		newTaskValues.put(DBOpenHandler.KEY_Listing_REVIEWS,
				item.getReviewsHtml());
		
		newTaskValues.put(DBOpenHandler.KEY_Listing_PHONE, item.getPhone());
		newTaskValues.put(DBOpenHandler.KEY_Listing_CAT_ID, item.getCat_id());
		// Insert the row.
		return db.insert(DBOpenHandler.DATABASE_TABLE_FAVORITES, null,
				newTaskValues);

	}

	public String processFavorites(ListItemModel item) {
		if (checkFavoriteItem(item)) {
			removeItemFromFavorites(item);
			return Consts.FAVO_REMOVED;
		} else {
			insertIntoFavorites(item);
			 ExportDB.extract("some_folder","dilny.db","/data/data/com.ksa.dilny/databases/dilny.db"); 
			return Consts.FAVO_ADDED;
		}
	}

	public void removeItemFromFavorites(ListItemModel item) {
		String[] d = { item.getId() };
		db.delete(DBOpenHandler.DATABASE_TABLE_FAVORITES,
				DBOpenHandler.KEY_Listing_ID + " = ? ", d);

	}

	public boolean checkFavoriteItem(ListItemModel item) {
		ArrayList<ListItemModel> favos = GetFavorites();
		if (favos != null) {
			for (int x = 0; x < favos.size(); x++) {
				ListItemModel favoItem = favos.get(x);
				if (favoItem.getId().equals(item.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<ListItemModel> GetFavorites() {
		// Create a new row of values to insert.
		ArrayList<ListItemModel> favos = new ArrayList<ListItemModel>();

		String[] result = { DBOpenHandler.KEY_Listing_ID,
				DBOpenHandler.KEY_Listing_TITLE,
				DBOpenHandler.KEY_Listing_DESC,
				DBOpenHandler.KEY_Listing_THUMB,
				DBOpenHandler.KEY_Listing_IMAGE,
				DBOpenHandler.KEY_Listing_RATINGS,
				DBOpenHandler.KEY_Listing_COUNTRY,
				DBOpenHandler.KEY_Listing_PRICE_JSONArray,
				DBOpenHandler.KEY_Listing_ADDRESS_1,
				DBOpenHandler.KEY_Listing_ADDRESS_2,
				DBOpenHandler.KEY_Listing_LAT, DBOpenHandler.KEY_Listing_LNG,
				DBOpenHandler.KEY_Listing_DISTANCE,
				DBOpenHandler.KEY_Listing_JSON_ITEM,
				DBOpenHandler.KEY_Listing_PRICE_MAX,
				DBOpenHandler.KEY_Listing_PRICE_MIN,
				DBOpenHandler.KEY_Listing_DB_ID,
				DBOpenHandler.KEY_Listing_PHONE ,
				DBOpenHandler.KEY_Listing_CAT_ID };
		Cursor c = db.query(DBOpenHandler.DATABASE_TABLE_FAVORITES, result,
				null, null, null, null, null, null);
		if (c.getCount() >= 1) {
			c.moveToNext();
			do {
				ListItemModel item = new ListItemModel();

				item.setId(c.getString(0));
				item.setTitle(c.getString(1));
				item.setDesc(c.getString(2));
				item.setThumb(c.getString(3));
				item.setImage(c.getString(4));
				item.setRatings(c.getString(5));
				item.setCountry(c.getString(6));
				// item.setPrice(c.getString(7));
				item.setAddress_line_1(c.getString(8));
				item.setAddress_line_2(c.getString(9));
				item.setlat(c.getString(10));
				item.setlng(c.getString(11));
				item.setDistance(c.getString(12));
				item.setJsonItem(c.getString(13));
				//item.setPriceMax(c.getString(14));
				//item.setPriceMin(c.getString(15));
				item.setDb_id(c.getString(16));
				item.setPhone(c.getString(17));
				item.setCat_id(c.getString(18));
				favos.add(item);
			} while (c.moveToNext());
		}
		c.close();
		if (favos.size() < 1) {
			return null;
		} else {
			return favos;
		}
	}

	public ArrayList<CatsModel> GetCats() {
		// Create a new row of values to insert.
		String[] result = { Consts.CATS_ID, Consts.CATS_NAME,
				Consts.CATS_IMAGE, Consts.CATS_ICON, Consts.CATS_TYPEID };
		Cursor c = db.query(DBOpenHandler.DATABASE_TABLE_CATS, result, null,
				null, null, null, null, null);

		ArrayList<CatsModel> cats = new ArrayList<CatsModel>();

		if (c.getCount() >= 1) {
			c.moveToNext();
			do {
				CatsModel cat = new CatsModel();
				cat.setId(c.getString(0));
				cat.setName(c.getString(1));
				cat.setImage(c.getString(2));
				cat.setIcon(c.getString(3));
				cat.setTypeID(c.getString(4));

				cats.add(cat);
			} while (c.moveToNext());
		}
		c.close();
		if (cats.size() < 1) {
			return null;
		} else {
			return cats;
		}
	}

	public ArrayList<LevelModel> GetLevels() {
		// Create a new row of values to insert.
		String[] result = { Consts.LEV_ID, Consts.LEV_NAME, Consts.LEV_TYPE,
				Consts.LEV_CATS_JSON };
		Cursor c = db.query(DBOpenHandler.DATABASE_TABLE_LEVELS, result, null,
				null, null, null, null, null);

		ArrayList<LevelModel> Levs = new ArrayList<LevelModel>();

		if (c.getCount() >= 1) {
			c.moveToNext();
			do {
				LevelModel lev = new LevelModel(new JSONObject());
				lev.setId(c.getString(0));
				lev.setName(c.getString(1));
				lev.setTypeID(c.getString(2));
				lev.setCatsJson(c.getString(3));

				Levs.add(lev);
			} while (c.moveToNext());
		}
		c.close();
		if (Levs.size() < 1) {
			return null;
		} else {
			return Levs;
		}
	}
 
 
	public int truncateCatsTable() {
		return db.delete(DBOpenHandler.DATABASE_TABLE_CATS, "1", null);
	}

	public int truncateLevelsTable() {
		return db.delete(DBOpenHandler.DATABASE_TABLE_LEVELS, "1", null);
	}

	public int truncateFavoritesTable() {
		return db.delete(DBOpenHandler.DATABASE_TABLE_FAVORITES, "1", null);
	}

}
