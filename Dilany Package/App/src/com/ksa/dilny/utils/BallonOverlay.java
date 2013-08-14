package com.ksa.dilny.utils;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.ksa.dilny.Act_ItemDetalis;
import com.ksa.dilny.Act_Map;
import com.ksa.dilny.DataModel.ListItemModel;
import com.yahia.libs.Geo.MapsOverLay.BallonOverlayDefult;
 

public class BallonOverlay extends BallonOverlayDefult{

	public BallonOverlay(Drawable defaultMarker, MapView _mapView, Context _c) {
		super(defaultMarker, _mapView, _c);
		mapView=_mapView;
		c=_c;
	}
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		if(c.getClass()==Act_Map.class){		 
			 
			 if(item.getTitle() !=null){
				 ListItemModel itm=Act_Map.places.get(index);
				 Intent n=new Intent(c,Act_ItemDetalis.class);
				 n.putExtra("item",itm.getJsonItem());
				 c.startActivity(n);
			 } 
		 }
		return true;
	}  

}
 