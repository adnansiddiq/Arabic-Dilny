package com.yahia.libs.Geo.MapsOverLay;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public abstract class MapOverlay extends ItemizedOverlay{
	protected ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	protected Context c; 
	
	
	public interface MapOverlayListener  {
	    public void sendSelectedPoint(GeoPoint point);
	}
	
	MapOverlayListener listener;
	
	public void setTheListener(MapOverlayListener listen) {
		listener = listen;
    }
	
	
	public MapOverlay(Drawable arg0,Context _c) {
		super(boundCenterBottom(arg0)); 
		 c=_c;
	}
 
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
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
	
	/*
	@Override
	public boolean onTap(GeoPoint point, MapView mv) {
		if(c.getClass()==Act_AddFromMap.class){
			listener.sendSelectedPoint(point);
		 }
		return super.onTap(point, mv);
	}*/
	/*
	 
	@Override
    public boolean onTouchEvent(MotionEvent e, MapView mapView) 
    {   
		super.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_UP) {                
            GeoPoint p = mapView.getProjection().fromPixels(
                (int) e.getX(),
                (int) e.getY());
            MyMapActivity this.startActivityForResult(intent, requestCode);
        }                            
        return false;
    }   
	 
	 *//*
	@Override
	 protected boolean onTap(int index)
	 {
		 if(c.getClass()==Act_Map.class){
			 OverlayItem item = mOverlays.get(index);
			 
			 if(item.getTitle() !=null){
				 ListItemModel itm=Act_Map.places.get(index);
				 Intent n=new Intent(c,Act_ItemDetalis.class);
				 n.putExtra("item",itm.getJsonItem());
				 c.startActivity(n);
			 } 
		 }
		
		 return true;
	 }*/
	 
	 

}
