package com.yahia.libs.Geo.MapsOverLay; 
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public abstract class BallonOverlayDefult extends BalloonItemizedOverlay<OverlayItem>{
	public Context c;
	public MapView mapView;
	public BallonOverlayDefult(Drawable defaultMarker, MapView _mapView,Context _c) {
		super(boundCenter(defaultMarker), _mapView); 	
		mapView=_mapView;
		 c=_c;
		 
		 
	}
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
 
	
	 

	public void addOverlay(OverlayItem overlay) { 
		//hideAllBalloons();   
		//mapView.invalidate();  
	    mOverlays.add(overlay);
	    mapView.invalidate();  
	    populate(); 
	     
	    
	    
	    
	    
	}
	public void populateOverLay(){
		  mapView.invalidate();  
		  populate(); 
	}
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	 
	@Override
	public boolean onTap(GeoPoint point, MapView mv) {		
		//System.out.println("onTap(GeoPoint point, MapView mv)");
 		  
		return super.onTap(point, mv);
	}
	
	@Override
	protected abstract boolean onBalloonTap(int index, OverlayItem item) ;

	
	
}
