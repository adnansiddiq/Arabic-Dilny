package com.ksa.dilny.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

import com.ksa.dilny.utils.Consts;

public class CatsModel {
	
	private String id;
	private String name;
	private String icon;
	private String image;
	private String typeID;	
	 
	public CatsModel(){
		
	}
	public CatsModel(JSONObject jsonObject){
		if( jsonObject.length()>0){
			try {
				System.out.println(jsonObject);
				this.setName(jsonObject.getString(Consts.CATS_NAME) );
				this.setId( jsonObject.getString(Consts.CATS_ID ) );
				this.setIcon( jsonObject.getString(Consts.CATS_ICON) );
				this.setImage( jsonObject.getString(Consts.CATS_IMAGE) );
				this.setTypeID( jsonObject.getString(Consts.CATS_TYPEID ) );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTypeID() {
		return typeID;
	}
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

}
