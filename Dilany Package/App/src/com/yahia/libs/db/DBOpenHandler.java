package com.yahia.libs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBOpenHandler extends SQLiteOpenHelper {
	private   int DATABASE_VERSION ;
    
	private   String DATABASE_NAME ;
	    
	private   String [] TABLE_CREATE ;

	DBOpenHandler(Context context,String DATABASE_NAME,int DATABASE_VERSION, String [] TABLE_CREATE) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    this.TABLE_CREATE=TABLE_CREATE;
	    this.DATABASE_NAME=DATABASE_NAME;
	    this.DATABASE_VERSION=DATABASE_VERSION;	
	}
	
	 
	@Override
	public void onCreate(SQLiteDatabase db) {
	    	if(TABLE_CREATE !=null){
	    		for(int x=0;x<TABLE_CREATE.length;x++){
	    			db.execSQL(TABLE_CREATE[x]);
		    }
	    	}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
		


}
