package com.ksa.dilny;
 
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.DataHolder;
import com.yahia.libs.Geo.GeoLocationService;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressCoder;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressCoder.GoogleGeoAddressCoderListener;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressModel;
import com.yahia.libs.Geo.MapsOverLay.BallonOverlayOneClick;
import com.yahia.libs.Geo.MapsOverLay.BallonOverlayOneClick.BallonOverlayListener;
 

public class Act_AddFromMap   extends MapActivity  implements BallonOverlayListener , GoogleGeoAddressCoderListener  {
	MapView mapView;
	MapController mapController ;  
	
	GeoPoint selectedPoint; 
	
	List<Overlay> mapOverlays;
	Drawable drawable;
	BallonOverlayOneClick overlay;
	ArrayList<ListItemModel> places=new ArrayList<ListItemModel>();
 
	String adresss;
	GoogleGeoAddressCoder addre;
	Boolean opend=false; 
	GoogleGeoAddressModel adModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 	//Remove System Bar
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_map);
		mapView=(MapView)findViewById(R.id.mapview); 
		drawable = this.getResources().getDrawable(R.drawable.marker_current); 
		 overlay = new BallonOverlayOneClick(drawable,mapView,this); 
		 overlay.setTheListener(this);
		 
		 addre=new GoogleGeoAddressCoder( this,true,getString(R.string.str_msg_address_loading));
			addre.setTheListener(this);
	      
			
			
		 //================== Map Controller =====================//       
	       mapController = mapView.getController();
	       mapView.setBuiltInZoomControls(true);
	       mapView.setClickable(true);
	       mapController.setZoom(18);  
	       
	     //================== Current Location  =====================// 
	       
	       GeoPoint  current= new GeoPoint((int)(GeoLocationService.lat*1E6), 
	        	     (int)(GeoLocationService.lng*1E6));
	       mapController.animateTo(current);  
	       
	       getAddress(current);
	       

	       
		
	}
	
	private void getAddress(GeoPoint point){
		addre.getGeoCoderWithThread(point);
	}
	
	private void addOverLay(GeoPoint point,int image,String title,String des){ 
		System.out.println("addOverLay");
				mapOverlays = mapView.getOverlays();			
				
			    drawable = this.getResources().getDrawable(R.drawable.marker_current); 
			     
			    
			    OverlayItem overlayitem = new OverlayItem(point, title, des);

				overlay.setShowClose(true); 
				overlay.setSnapToCenter(true);
				
				overlay.addOverlay(overlayitem);
				
				mapOverlays.clear();
			    mapOverlays.add(overlay);  
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
 

	@Override
	public void sendSelectedPoint(GeoPoint point) {
		System.out.println("sendSelectedPoint");
		System.out.println(point.getLatitudeE6());
		System.out.println(point.getLongitudeE6());
		selectedPoint=point;  
		 getAddress(point);  
	   // addOverLay(point,  R.drawable.marker_current  , "d", "d");
		
	}
 
	@Override
	public void addressfail(GeoPoint point) {
		selectedPoint=point;
		handler.sendEmptyMessage(1);	
	}	
	@Override
	public void addressNoResultFound(GeoPoint point) {
		selectedPoint=point;
		handler.sendEmptyMessage(2);	
		
	}
	
	 
	Handler handler = new Handler() {
	  	  public void handleMessage(Message message) {	  		
	  	 
	  			switch (message.what) {
		  	    case 0: 
		  	    	if(!opend){
		  	    		addOverLay(selectedPoint,  R.drawable.marker_current  , adresss, null);
		  	    		opend=true;
		  	    	}else {
		  	    		if(adresss!=null){
		  	    			addOverLay(selectedPoint,  R.drawable.marker_place  , adresss, null);
		  	    			//addOverLay(selectedPoint,R.drawable.marker_place,null,adresss);				  	 
		  	    		} else {
		  	    			addOverLay(selectedPoint,R.drawable.marker_place,null,null);
		  	    		 	
		  			}
		  	    	}
		  	    		
		  	    	break;
		  	    case 1:
		  	    		addOverLay(selectedPoint,R.drawable.marker_place,null,null);
		  	    		Toast.makeText(Act_AddFromMap.this, getString(R.string.str_msg_limit_exeded) ,Toast.LENGTH_SHORT).show();
		  	    	break;
		  	  case 2:
	  	    		addOverLay(selectedPoint,R.drawable.marker_place,null,null);
	  	    		Toast.makeText(Act_AddFromMap.this, getString(R.string.str_msg_no_results_Found) ,Toast.LENGTH_SHORT).show();
	  	    	break;
		  	    
		  	    }
	  		  
	  	    
	  	  }
	};
	@Override
	public void addressDone(GeoPoint point,GoogleGeoAddressModel address) {
		System.out.println("addressDone(GeoPoint point,AddressModel address)");
		selectedPoint=point;
		this.adModel=address;
		this.adresss=address.getFullAdress();	
		handler.sendEmptyMessage(0);
		
	}
	
	@Override
	public void sendBallonPressed(){
		System.out.println("sendBallonPressed : " );
		DataHolder.SelectedAddress=this.adModel;
		Intent resultIntent = new Intent(); 
		setResult(2, resultIntent );
		finish();
		 
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
	 
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater oMenu = getMenuInflater();
		oMenu.inflate(R.menu.act_map_menu, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("Act_Map", "onOptionsItemSelected : " + item.getNumericShortcut());
		switch (item.getNumericShortcut()) {
		case '1':
			Log.d("Act_Map", "onOptionsItemSelected : case 1");
			mapView.setSatellite(false);
			mapView.invalidate();
			return true;

		case '2':
			Log.d("Act_Map", "onOptionsItemSelected : case 2");
			mapView.setSatellite(true);
			mapView.invalidate();
			return true;

		}

		return false;
	}

	
}
