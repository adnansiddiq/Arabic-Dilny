package com.ksa.dilny.utils;

import java.util.ArrayList;

import libs.db.DBAdapter;
import android.content.Context;
import android.content.SharedPreferences;

import com.ksa.dilny.Act_SearchResult;
import com.ksa.dilny.DataModel.CatsModel;
import com.ksa.dilny.DataModel.LevelModel;
import com.ksa.dilny.DataModel.ListItemModel;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressModel;


public class DataHolder {
	public  static String res_cats;
	public  static String res_nearby;
	public  static String res_favo;
	
	public  static String litingID;
	
	public static ListItemModel item;
	public static GoogleGeoAddressModel SelectedAddress;
	
	
	public static String Mail;
	public static String Password;
	public static String User_ID;
	public static String Name;
	
	public static String phoneNumberRegx = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";
	public static Boolean rememberMe=false;
    public static final String PREFS_NAME = "Data";

    
    public static void setData(Context c,String _Mail,String _Password,String _User_ID,Boolean _rememberMe){
   	 // Set preferences
    	Mail=_Mail;
    	Password=_Password;
    	User_ID=_User_ID;
    	rememberMe=_rememberMe;
    //	GeoLocations.setGeoData(c);
    	SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Mail",_Mail);
        editor.putString("Password",_Password);
        editor.putString("User_ID",_User_ID);
        editor.putBoolean("rememberMe",_rememberMe);
        


        // Commit the edits!
        editor.commit();
   
   }
    public static void saveImageUri(Context c,String imageUri){
      	 
       	SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
           SharedPreferences.Editor editor = settings.edit();
           editor.putString("image",imageUri); 
           // Commit the edits!
           editor.commit();
      
      }
    public static String loadImageUri(Context c){
    		SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
    		return settings.getString("image", null);
        
    }
    public static void saveSearchData(Context c,String searchWord,String catID,Double lat,Double lng){
       	SharedPreferences settings = c.getSharedPreferences("Search", 0);
           SharedPreferences.Editor editor = settings.edit();
           editor.putString("searchWord",searchWord);
           editor.putString("catID",catID);
           if(lat==null){
        	   editor.putString("lat",null);
           }else {
        	   editor.putString("lat",lat+"");
           }
           if(lng==null){
        	   editor.putString("lng",null);
           }else {
        	   editor.putString("lng",lng+"");
           }
                    


           // Commit the edits!
           editor.commit();
      
      }
    
    public static void  getData(Context c){
    	 // Restore preferences
    	
  //  	GeoLocations.getGeoData(c);
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);

        Password = settings.getString("Password", null);
        Mail = settings.getString("Mail", null);
        User_ID=settings.getString("User_ID", null);

        rememberMe=settings.getBoolean("rememberMe", false);
        
        System.out.println("Mail - > "+Mail);
        System.out.println("Password - > "+Password);
        System.out.println("rememberMe - > "+rememberMe);
        System.out.println("User_ID - > "+User_ID);
        
       
    
    }
    
    public static void getSearchData(Context c) {
    	
    	 SharedPreferences settings = c.getSharedPreferences("Search", 0);
    	 if(settings.getString("searchWord", null)!=null){
    		 Act_SearchResult.searchWord= settings.getString("searchWord", null); 
    	 }
    	 if(settings.getString("catID", null)!=null){
    		 Act_SearchResult.catID= settings.getString("catID", null);
    	 }
    	 if(settings.getString("lat", null)!=null){
    		 Act_SearchResult.lat= Double.parseDouble(settings.getString("lat", null));
    	 }
    	 if(settings.getString("lng", null)!=null){
    		 Act_SearchResult.lng= Double.parseDouble(settings.getString("lng", null));
    	 }
    	 	
    	 	
    	 	
    	 	
    	 			
      
	}
    
    

    public static ArrayList<CatsModel> getCatsFromDB(Context _c){
    	ArrayList<CatsModel> cats = new ArrayList<CatsModel>();
    	DBAdapter dba = new DBAdapter(_c);

        dba.open();
        cats = dba.GetCats();
        dba.close();
        
        if(cats.size()<1){
        	return null;
        }else {
        	return cats;
        }
        
    }
    public static ArrayList<LevelModel> getLevelsFromDB(Context _c){
    	ArrayList<LevelModel> levs = new ArrayList<LevelModel>();
    	DBAdapter dba = new DBAdapter(_c);

        dba.open();
        levs = dba.GetLevels();
        dba.close();
        
        if(levs.size()<1){
        	return null;
        }else {
        	return levs;
        }
        
    }
    
    public static ArrayList<ListItemModel> getfavosFromDB(Context _c){
    	ArrayList<ListItemModel> favos = new ArrayList<ListItemModel>();
    	DBAdapter dba = new DBAdapter(_c);
    	dba.open(); 
    	favos=dba.GetFavorites();
    	dba.close();   
        return favos;
        }
    
    public static Boolean checkFavoriteItem(ListItemModel item,Context _c){    	 
    	DBAdapter dba = new DBAdapter(_c);
    	dba.open(); 
    	Boolean status =dba.checkFavoriteItem(item);
    	dba.close();         
        return status;
    }
    
    public static String processFavorites(ListItemModel item,Context _c){    	 
    	DBAdapter dba = new DBAdapter(_c);
    	dba.open(); 
    	String process =dba.processFavorites(item);
    	dba.close();         
        return process;
    }
    
    public static Long insertIntoFavos(Context _c,ListItemModel item){ 
    	DBAdapter dba = new DBAdapter(_c);
        dba.open();
        Long x=dba.insertIntoFavorites(item);         
        dba.close(); 
    	return x;
    }

	
   
}
