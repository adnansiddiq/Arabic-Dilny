package com.ksa.dilny.DataModel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ksa.dilny.utils.Consts;

public class LevelModel {
	private String id;
	private String name;
	private String typeID;
	private String catsJson;
	private ArrayList<CatsModel> cats=new ArrayList<CatsModel>();
	
	

    public static final String LEV_ID="id";
    public static final String LEV_NAME="name";
    public static final String LEV_CATS="cats";
    public static final String LEV_TYPE="type";
    
    
    public LevelModel (){
    	
    }
	public LevelModel (JSONObject jsonObject ){
		if( jsonObject.length()>0){
			try {
				this.setId(jsonObject.getString(Consts.LEV_ID));
				this.setName(jsonObject.getString(Consts.LEV_NAME));
				this.setTypeID(jsonObject.getString(Consts.LEV_TYPE));
				this.setCatsJson(jsonObject.getString(Consts.LEV_CATS));
				
				JSONArray array=new JSONArray(jsonObject.getString(Consts.LEV_CATS));
				for(int x=0;x<array.length();x++){
					JSONObject obj=array.getJSONObject(x);
					System.out.println(obj.toString());
					CatsModel cat=new CatsModel(obj);
					cats.add(cat);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		 
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeID() {
		return typeID;
	}
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}
	public ArrayList<CatsModel> getCats() {
		return cats;
	}
	public void setCats(ArrayList<CatsModel> cats) {
		this.cats = cats;
	}
	public String getCatsJson() {
		return catsJson;
	}
	public void setCatsJson(String catsJson) {
		this.catsJson = catsJson;
	}
	
	 

}
