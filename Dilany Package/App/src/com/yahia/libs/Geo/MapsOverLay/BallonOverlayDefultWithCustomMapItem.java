package com.yahia.libs.Geo.MapsOverLay; 
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public abstract class BallonOverlayDefultWithCustomMapItem extends BalloonItemizedOverlay<CustomMapItem>{
	public Context c;
	public MapView mapView;
	protected List<CustomMapItem> items=new ArrayList<CustomMapItem>();
	public BallonOverlayDefultWithCustomMapItem(Drawable defaultMarker, MapView _mapView,Context _c,List<CustomMapItem> items) {
		super(boundCenter(defaultMarker), _mapView); 	
		mapView=_mapView;
		 c=_c;
		 this.items=items;
		 //populate();
		 //mapView.invalidate();
		 
	}
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
 
	protected Drawable getMarker(int resource) {
		
	      Drawable marker=c.getResources().getDrawable(resource);
	      
	      marker.setBounds(0, 0, marker.getIntrinsicWidth(),
	                        marker.getIntrinsicHeight());
	      boundCenter(marker);

	      return(marker);
	    }
	protected abstract Drawable getMarker(String resource) ;
	@Override
    public void draw(Canvas canvas, MapView mapView,
                      boolean shadow) {
      super.draw(canvas, mapView, shadow);
      
    } 
	 
    @Override
    public int size() {
      return(items.size());
    }
	@Override
	protected CustomMapItem createItem(int i) {
		return items.get(i);
	}
 
	 
	@Override
	public boolean onTap(GeoPoint point, MapView mv) {		
		//System.out.println("onTap(GeoPoint point, MapView mv)");
 		  
		return super.onTap(point, mv);
	}
	
	@Override
	protected abstract boolean onBalloonTap(int index, CustomMapItem item) ;

	
	
}
