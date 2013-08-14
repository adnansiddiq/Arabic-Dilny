package com.ksa.dilny;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.BallonOverlay;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.yahia.libs.Geo.GeoLocationService;

public class Act_OneItemMap extends MapActivity implements onConnectionDoneCC ,  OnGestureListener {
	MapView mapView;
	MapController mapController ;
	GeoPoint currentPoint;
	
	List<Overlay> mapOverlays;  
	
	public static ArrayList<ListItemModel> places=new ArrayList<ListItemModel>();
	
	int initialZoom=16;
	Boolean inConneciton=false;
	Context c;

 	 String intentLat;
 	 String intentLng;
 	 GeoPoint intentPoint;
	private GestureDetector gestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState){		 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	//Remove System Bar
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        c=this;
       super.onCreate(savedInstanceState);
       setContentView(R.layout.act_map);
       mapView=(MapView)findViewById(R.id.mapview); 
       
       this.gestureDetector = new GestureDetector(this);
       this.gestureDetector.setIsLongpressEnabled(true);
       
        
       //================== Map Controller =====================//       
       mapController = mapView.getController();
       mapView.setBuiltInZoomControls(true);
       mapView.setClickable(true);
       mapController.setZoom(initialZoom);  
       
     //================== Current Location  =====================//  
       	intentLat= (String) getIntent().getSerializableExtra("lat");
		 intentLng = (String) getIntent().getSerializableExtra("lng"); 
	       if(intentLat !=null && intentLng !=null){
	    	   Log.d("Act_Map", " intentLat : "+intentLat);
	    	   Log.d("Act_Map", " intentLng : "+intentLng);
	    	   intentPoint= new GeoPoint((int)((Double.parseDouble(intentLat))*1000000), 
		        	     (int)((Double.parseDouble(intentLng))*1000000));
	    	   	addOverLay(intentPoint,null,null);
		       mapController.animateTo(intentPoint);
	       }  
	       currentPoint= new GeoPoint((int)(GeoLocationService.lat*1000000), 
	        	     (int)(GeoLocationService.lng*1000000));
		       addOverLay(currentPoint,null,null);
		       
	            
	}

	 
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		 gestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
 
	private void addOverLay(GeoPoint point,String title,String des){
		mapOverlays = mapView.getOverlays();
		BallonOverlay ballonOverlay = new BallonOverlay( this.getResources().getDrawable(R.drawable.marker_place),mapView,this); 
		OverlayItem overlayitem = new OverlayItem(point, title, des);
 
		//ballonOverlay.addOverlay(overlayitem);					
	    //mapOverlays.add(ballonOverlay);
	   
		//ballonOverlay.onTap(0);
	    
	}

	 
	private void operateConnection(Double lat,Double lng,Boolean showLoading){
		inConneciton=true;
		String link="http://dilny.com/api/searchNearby/lat/"+lat+"/lng/"+lng+"/radius/50/page/0/cat/";
		System.out.println(link);
		ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_map,showLoading,c.getString(R.string.str_loading));
		 cc.setOnConnectionDone(this);
		 cc.startGetConnection();
	}
	private void operateConnection(int lat,int lng,Boolean showLoading){
		inConneciton=true;
		String link="http://dilny.com/api/searchNearby/lat/"+(lat/1000000)+"/lng/"+(lng/1000000)+"/radius/50/page/0/cat/";
		System.out.println(link);
		ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_map,showLoading,c.getString(R.string.str_loading));
		 cc.setOnConnectionDone(this);
		 cc.startGetConnection();
	} 
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void done(int code, String response) {
		if(code ==ConnectionController.Con_map_long_click){
			inConneciton=false;
			Intent N = new Intent(this, Act_SearchResult.class); 
	 	    N.putExtra("response",response);
	 	   N.putExtra("connection",code); 
	 	    startActivity(N);
		} 
		
		
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		 
		  
		  
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		System.out.println("onFling");
		return false;
	}

	@Override
	public void onLongPress(final MotionEvent e) { 
		AlertDialog.Builder dialog = new AlertDialog.Builder(c);
		 
		 dialog.setTitle("بحث حول المكان"); 
		 dialog.setPositiveButton("بحث حول المكان", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				searchNearby( e);
				
			}
		});
		 
		 dialog.show(); 
		
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		 if(intentLat ==null && intentLng ==null){
			 if(!inConneciton){

					int zoomLevel=mapView.getZoomLevel();
					
					GeoPoint initGeoPoint = mapView.getMapCenter();
					
					
					Location gpsLocation = new Location("");
					gpsLocation.setLatitude(currentPoint.getLatitudeE6()/ 1E6);
					gpsLocation.setLongitude(currentPoint.getLongitudeE6()/1E6);
					
					
					Location mapLocation = new Location("");
					mapLocation.setLatitude(initGeoPoint.getLatitudeE6()/1E6);
					mapLocation.setLongitude(initGeoPoint.getLongitudeE6()/1E6);
					
					/*
					System.out.println("zoomLevel : " +zoomLevel);
					
					System.out.println("currentPoint : "+(currentPoint.getLatitudeE6()/1E6) + "KM");
					System.out.println("currentPoint : "+(currentPoint.getLongitudeE6()/1E6) + "KM");
					
					System.out.println("initGeoPoint : "+(initGeoPoint.getLatitudeE6()/1E6) + "KM");
					System.out.println("initGeoPoint : "+(initGeoPoint.getLongitudeE6()/1E6) + "KM");
					*/
					
					Double distance=round((mapLocation.distanceTo(gpsLocation)/1000),2);
					System.out.println("Distance : "+  distance + "KM");
					 
					 
						if(distance>10){ 
							operateConnection(initGeoPoint.getLatitudeE6(),initGeoPoint.getLongitudeE6(),false);
						}
					  
				}
		 }
		
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		System.out.println("onShowPress");
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		System.out.println("onSingleTapUp");
		return false;
	}

	private void searchNearby(MotionEvent e){
		Projection proj = mapView.getProjection();
        GeoPoint loc = proj.fromPixels((int)e.getX(), (int)e.getY()); 
        String lng=Double.toString(loc.getLongitudeE6()/ 1E6);
        String lat=Double.toString(loc.getLatitudeE6()/ 1E6);
        inConneciton=true;
        String link="http://dilny.com/api/searchNearby/lat/"+lat+"/lng/"+lng+"/radius/50/page/0/cat/";
        System.out.print("ACTION_DOWN :"+link);
		ConnectionController cc=new ConnectionController(link, c, ConnectionController.Con_map_long_click,true,c.getString(R.string.str_loading));
		 cc.setOnConnectionDone(Act_OneItemMap.this);
		 cc.startGetConnection();
	}	
	@Override
	protected void onResume() {
		System.out.println("onResume()");
		DataHolder.getData(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		System.out.println("onPause()");
		DataHolder.setData(this, DataHolder.Mail, DataHolder.Password, DataHolder.User_ID, DataHolder.rememberMe);
		super.onPause();
	}
	 
	
}
