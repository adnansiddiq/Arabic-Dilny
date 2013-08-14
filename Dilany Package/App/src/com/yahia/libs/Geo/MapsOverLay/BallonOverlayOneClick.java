package com.yahia.libs.Geo.MapsOverLay; 
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class BallonOverlayOneClick extends BalloonItemizedOverlay<OverlayItem>{
	Context c;
	MapView mapView;
	public BallonOverlayOneClick(Drawable defaultMarker, MapView _mapView,Context _c) {
		super(boundCenter(defaultMarker), _mapView); 	
		mapView=_mapView;
		 c=_c;
		 
		 
	}
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
 
	
	
	public interface BallonOverlayListener  {
	    public void sendSelectedPoint(GeoPoint point);
	    public void sendBallonPressed();
	}
	
	BallonOverlayListener listener;
	
	public void setTheListener(BallonOverlayListener listen) {
		listener = listen;
    }

	public void addOverlay(OverlayItem overlay) { 
		hideAllBalloons();
		mOverlays.clear();   
	    mOverlays.add(overlay);
	    mapView.invalidate();  
	    populate(); 
	    super.onTap(0);
	    System.out.println("populate()");
	    
	    
	    
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
		System.out.println("onTap(GeoPoint point, MapView mv)");
		listener.sendSelectedPoint(point);		 
		return super.onTap(point, mv);
	}
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		 System.out.println("onBalloonTap : "+index);
		 listener.sendBallonPressed();
		return true;
	}  

	
	
}
