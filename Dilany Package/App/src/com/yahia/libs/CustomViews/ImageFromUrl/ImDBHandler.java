package com.yahia.libs.CustomViews.ImageFromUrl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import com.yahia.libs.db.DBAdapter;

public class ImDBHandler {
	Context c;
	public ImDBHandler(Context c){
		this.c=c;
	}
	/*-------------- DB -----------------*/
	public static final String KEY_IMAGES_URL="url";
	public static final String KEY_IMAGES_PATH = "path";
	public static final String KEY_IMAGES_ID= "id"; 
	public static final String DATABASE_TABLE_IMAGES = "images";
	public static final String DATABASE_IMAGES_DB = "images.db";
	
    private static final String  CREATE_TABLE_IMAGES =
            "create table " + DATABASE_TABLE_IMAGES + " ("
            + KEY_IMAGES_ID  + "  integer primary key autoincrement, "
            + KEY_IMAGES_URL + " text UNIQUE not null, "
            + KEY_IMAGES_PATH + " text not null);" ;
    static String [] create_images_table={CREATE_TABLE_IMAGES};
    /*------------------------------------*/
    
/*-------------- DB -----------------*/
	
	public ImageFileModel GetImageFileFromDbWithUrl(String url) {		
		//Log.d("ImDBHandler", "GetImageFileFromDbWithUrl ---- Started ");
		//Log.d("ImDBHandler", "url :  "+url);
		DBAdapter dba=new DBAdapter(c,DATABASE_IMAGES_DB,1,create_images_table);		 
		dba.open();			 
		Cursor c=dba.selectAllWhere( DATABASE_TABLE_IMAGES, KEY_IMAGES_URL, url);
	
		//Log.d("ImDBHandler", "c.getCount() =  "+c.getCount());
		if (c.getCount() >= 1) {
			//Log.d("ImDBHandler", "c.getCount() >= 1  ");
			c.moveToNext(); 
			ImageFileModel im = new ImageFileModel(c.getString(1),BitmapFactory.decodeFile(c.getString(2)),c.getString(2));
			c.close();
			dba.close();
			//Log.d("ImDBHandler", "GetImageFileFromDbWithUrl ---- End ");
			return im;			
		}
		c.close();
		dba.close();
		//Log.d("ImDBHandler", "null");
		//Log.d("ImDBHandler", "GetImageFileFromDbWithUrl ---- End ");
		return null;
	}
	
	public void insertImages(Context c,ImageFileModel image){
		//Log.d("ImDBHandler", "insertImages  ");
		ImageFileModel im=GetImageFileFromDbWithUrl(image.getUrl());
		if(im==null){
			DBAdapter dba=new DBAdapter(c,DATABASE_IMAGES_DB,1,create_images_table);	
			ContentValues newTaskValues = new ContentValues();
			// Assign values for each row.
			newTaskValues.put(KEY_IMAGES_PATH, image.getPathOnSD());
			newTaskValues.put(KEY_IMAGES_URL, image.getUrl());		
			dba.open();			 
			long result=dba.insertValuesIntoTable(DATABASE_TABLE_IMAGES, newTaskValues);
			dba.close();
			//Log.d("ImDBHandler", "Image Url : "+image.getUrl());
			//Log.d("ImDBHandler", "result : "+result);	
		}else {
			//Log.d("ImDBHandler", "image is exest : "+im.getUrl());	
		}
			
		
	} 
	/*--------------------------------*/
}
