package com.ksa.dilny.utils;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.MapView;
import com.ksa.dilny.Act_ItemDetalis;
import com.ksa.dilny.Act_Map;
import com.ksa.dilny.R;
import com.ksa.dilny.DataModel.ListItemModel;
import com.yahia.libs.Geo.MapsOverLay.BallonOverlayDefultWithCustomMapItem;
import com.yahia.libs.Geo.MapsOverLay.CustomMapItem;
 

public class CustomBallonOverlay extends BallonOverlayDefultWithCustomMapItem{
	Boolean mainMap=true;
	public CustomBallonOverlay(Drawable defaultMarker, MapView _mapView, Context _c,List<CustomMapItem> currentItems,	 Boolean mainMap) {
		super(boundCenter(defaultMarker), _mapView, _c,currentItems);
		
		 
		mapView=_mapView;
		c=_c;
		Log.d("CustomBallonOverlay", "items.size() : "+currentItems.size());
		 
		 this.mainMap=mainMap;
		for(int x=0;x<items.size();x++){
			CustomMapItem item=items.get(x);
			items.remove(x);
			if(item.getImagePath()==null){
				items.add(x,new CustomMapItem(item.getPoint(),item.getTitle(),item.getSnippet(),getMarker(item.getImageRes()),0));
			}else {
				items.add(x,new CustomMapItem(item.getPoint(),item.getTitle(),item.getSnippet(),getMarker(item.getImagePath()),0));
			}
			
		}
		 
		populate();
		mapView.invalidate();
	}
	 
	@Override
	protected boolean onBalloonTap(int index, CustomMapItem item) {
		 
		if(c.getClass()==Act_Map.class){		 
			 
			 if(item.getSnippet() !=null){
				 Log.d("CustomBallonOverlay","onBalloonTap  : "+mainMap);
				 if(mainMap){
					 ListItemModel itm=Act_Map.places.get(index);
					 Intent n=new Intent(c,Act_ItemDetalis.class);
					 n.putExtra("item",itm.getJsonItem());
					 c.startActivity(n);					  
				 }
				 
			 } 
		 } 
		return true;
	}

	@Override
	protected Drawable getMarker(String resource) {
		Log.d("getMarker", "resource : "+resource);
		Drawable d=BitmapDrawable.createFromPath(resource);
		if(d==null){
			Log.d("getMarker", "d==null");
			d = c.getResources().getDrawable(R.drawable.marker_place);
		}else {
			Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
			d = new BitmapDrawable(c.getResources(), bitmap);
		}
		
	 
	      boundCenter(d);

	      return(d);
		 
	}  

}
 