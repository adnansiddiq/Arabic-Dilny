package com.yahia.libs.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * DBAdapter Used to deal with Android SQLite DB 
 * 
 * 
 * @author      Yahia Ragae
 * @version     1
 * @since       1.0
*/
public class DBAdapter {
   
 private SQLiteDatabase db;
 private DBOpenHandler dbHandler;
 

 /** 
  * DBAdapter Constructor  
  *<p>
  * @param _context Applicaiton Context
  * <p>
  * @param DATABASE_NAME DataBase Name String
  * <p>
  * @param DATABASE_VERSION DataBase Version int
  * <p>
  * @param Creat_TABLE_Statemnts String Array contains tables Creation Statements  every  element may to be like that :
  * <p>
  * String Creat_TABLE_Test1 =
  * <p>
       "CREATE TABLE " + DATABASE_TABLE_test1 + " (" +
       <p>
       DATABASE_TABLE_test1_id + " integer, " +
       <p>
       DATABASE_TABLE_test1_country + " TEXT, " +
       <p>
       DATABASE_TABLE_test1_name + " TEXT);";
  * <p>
  * @return void
  */
    public DBAdapter(Context _context,String DATABASE_NAME,int DATABASE_VERSION,String [] Creat_TABLE_Statemnts) {

        //Log.d("DBAdapter", "DBAdapter(Context,"+DATABASE_NAME+","+DATABASE_VERSION+","+Creat_TABLE_Statemnts+")");
        
        
        dbHandler = new DBOpenHandler(_context,DATABASE_NAME,DATABASE_VERSION,Creat_TABLE_Statemnts);
        
    }
    
     

    /** 
     * Close Connection   
     * <p>
     *  @return void
     */
    
    public void close() {
        db.close();
    }

    /** 
     * Open Connection
     * <p>
     *  @return void
     *  <p> 
     *     @throws SQLiteException
     */
    public void open() throws SQLiteException {
        try {
            db = dbHandler.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbHandler.getReadableDatabase();
        }
    }
   

    /** 
     * Select * From TableName  
     *<p>
     * @param tableName  table name
     *  <p>
     *  @return Cursor
     */
    
    public Cursor selectAll(String tableName){
    	String qury = "SELECT * FROM " + tableName;
    	Cursor x = db.rawQuery(qury, null);
        return x;
    }
    
    
    /** 
     * Select * From TableName where filed  
     *<p>
     * @param tableName  Table name
     * <p>
     * @param filed  Filed name
     *  <p>
     *  @return Cursor
     */
    
    
    public Cursor selectOneFiled(String tableName,String filed){
    	String qury = "SELECT "+filed+" FROM " + tableName ;
        Cursor x = db.rawQuery(qury,null);
        return x;
    }
    
    /** 
     * Select filedTobeSelected From Table Where FiledName  
     *<p>
     * @param filedTobeSelected  filed Name which will be searched 
     *<p>
     * @param tableName  table name
     *<p>
     * @param filed  filed Name 
     * <p> 
     * @param value  The value of the specific filed              
         <p>
     *  @return Cursor
     */
    
    public Cursor selectOneFiledWhere(String filedTobeSelected,String tableName,String filed,String value){
    	String qury = "SELECT "+filedTobeSelected+" FROM " + tableName + " Where " +filed +" = ?  ";
    	String [] d={value};
        Cursor x = db.rawQuery(qury, d);
        return x;
    }
    
    /** 
     * Select * From Table Where FiledName  
     *<p>
     * @param tableName  table name
     *<p>
     * @param filed  filed Name 
     * <p> 
     * @param value  The value of the specific filed              
         <p>
     *  @return Cursor
     */
    
    public Cursor selectAllWhere(String tableName,String filed,String value){
    	 	//Log.d("DBAdapter", "selectAllWhere( "+tableName+", "+filed+", "+value+")");
    	 	String qury = "SELECT * FROM " + tableName + " Where " +filed +" = ?  ";
    	 	String [] d={value};   
    	 	//Log.d("DBAdapter", qury+" "+d[0]);
        Cursor x = db.rawQuery(qury, d);
        return x;
    }
    
    
    /** 
     * Insert Values in Specific table 
     *<p>
     * @param tableName  table name
     *<p>
     * @param contentValues  New Values will be added to specific record
     * using ContentValues :
     * <p> 
     * 	ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnName1, value);
        contentValues.put(ColumnName2, value);      
        contentValues.put(ColumnName3, value);
        ...            
         <p>
     *  @return long 	<p> return db.insert(tableName, null, newTaskValues) 
     */
    
    public long insertValuesIntoTable(String tableName,ContentValues newTaskValues) {
    	//Log.d("DBAdapter", "return db.insert(tableName, null, newTaskValues);");
    	return db.insert(tableName, null, newTaskValues);
    }
    
    
    /**
     * Update Values of Specific table via the ID of record
     *<p>
     * @param tableName  table name
     *<p>
     * @param contentValues  New Values will be added to specific record
     * using ContentValues :
     * <p> 
     * 	ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnName, value);      
        <p>
     * @param idColumnName  The ID ColumnName
     * <p>
     * @param id  array with one element , the id of the specific record
     *  <p>
     *  @return int <p>	return   db.update(tableName, contentValues,idColumnName +" = ? ",id);
     */

    public int updateValuesInTable(String tableName,ContentValues contentValues,String idColumnName,String []id) {
    
        
       return   db.update(tableName, contentValues,idColumnName +" = ? ",id);
 
    }
     
    /**
     * Drop Table(s)
     *<p>
     * @param tables  String Array with tables names 
     *<p>
     * @return void
     */
    public void Truncate(String [] tables){
    	for(int x=0 ;x<tables.length;x++){
    		db.delete(tables[x], null, null);
    	}
    	
        
    }
    
    
    
}
