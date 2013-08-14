package com.yahia.libs.Geo.MapsOverLay;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

 
public class CustomMapItem extends OverlayItem {
	Drawable marker=null;
	private String imagePath;
	private int imageRes;

    public CustomMapItem(GeoPoint pt, String name, String snippet,Drawable d,String path ) {
		super(pt, name, snippet);
		marker=d;
		setImagePath(path);
	 
		// TODO Auto-generated constructor stub
	}
    public CustomMapItem(GeoPoint pt, String name, String snippet,Drawable d,int path) {
		super(pt, name, snippet);
		marker=d;
		setImageRes(path);
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public Drawable getMarker(int stateBitset) {
     
      return marker;
    }

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getImageRes() {
		return imageRes;
	}

	public void setImageRes(int imageRes) {
		this.imageRes = imageRes;
	}
 
  }
