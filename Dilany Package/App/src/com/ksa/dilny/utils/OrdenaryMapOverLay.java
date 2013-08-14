package com.ksa.dilny.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.yahia.libs.Geo.MapsOverLay.MapOverlay;

public class OrdenaryMapOverLay extends MapOverlay{
	
	public OrdenaryMapOverLay(Drawable drawable, Context _c) {
		super(drawable,_c);
		 
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
	 
	 */
}
