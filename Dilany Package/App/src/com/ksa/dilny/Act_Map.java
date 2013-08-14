package com.ksa.dilny;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.ksa.dilny.DataModel.CatsModel;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.BallonOverlay;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.CustomBallonOverlay;
import com.ksa.dilny.utils.DataHolder;
import com.ksa.dilny.utils.DataProcessor;
import com.ksa.dilny.utils.OrdenaryMapOverLay;
import com.yahia.libs.CustomViews.ImageFromUrl.ImDBHandler;
import com.yahia.libs.Geo.GeoLocationService;
import com.yahia.libs.Geo.MapsOverLay.CustomMapItem;

public class Act_Map extends MapActivity implements onConnectionDoneCC,
		OnGestureListener {
	MapView mapView;
	MapController mapController;
	GeoPoint currentPoint;

	CustomBallonOverlay customBallonOverlay;
	List<Overlay> mapOverlays;
	BallonOverlay ballonOverlay;
	OrdenaryMapOverLay ordenaryOverLay;
	Drawable drawable;
	public static ArrayList<ListItemModel> places = new ArrayList<ListItemModel>();

	int initialZoom = 16;
	Boolean inConneciton = false;
	Context c;
	ImDBHandler dbHandler;

	List<CustomMapItem> items = new ArrayList<CustomMapItem>();
	Boolean mainMap = true;
	private GestureDetector gestureDetector;

	TextView tvDistabce;
	ProgressBar pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove System Bar
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		c = this;
		dbHandler = new ImDBHandler(c);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_map);
		mapView = (MapView) findViewById(R.id.mapview);

		this.gestureDetector = new GestureDetector(this);
		this.gestureDetector.setIsLongpressEnabled(true);
		drawable = getResources().getDrawable(R.drawable.ic_launcher);
		ballonOverlay = new BallonOverlay(this.getResources().getDrawable(
				R.drawable.marker_place), mapView, this);
		ordenaryOverLay = new OrdenaryMapOverLay(this.getResources()
				.getDrawable(R.drawable.ic_current_location), c);
		// ================== Map Controller =====================//
		mapController = mapView.getController();
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapController.setZoom(initialZoom);

		// ================== Current Location =====================//
		currentPoint = new GeoPoint((int) (GeoLocationService.lat * 1000000),
				(int) (GeoLocationService.lng * 1000000));
		checkIntent();
		if (intentLat == null && intentLng == null) {
			tvDistabce=(TextView)findViewById(R.id.map_distance);
			pd=(ProgressBar)findViewById(R.id.map_pd);
			
			tvDistabce.setVisibility(View.VISIBLE);
			pd.setVisibility(View.VISIBLE);
			uiHandler.sendEmptyMessage(2);
			
			mapController.animateTo(currentPoint);
			CustomMapItem item = new CustomMapItem(currentPoint,
					"مكانك الحالي", "", null, R.drawable.ic_current_location);
			items.add(item);
			customBallonOverlay = new CustomBallonOverlay(drawable, mapView, c,
					items,mainMap);
			mapOverlays = mapView.getOverlays();
			mapOverlays.add(customBallonOverlay);
		}

		// ================== Search By Location =====================//

		if (intentLat == null && intentLng == null) {
			operateConnection(GeoLocationService.lat, GeoLocationService.lng,
					true);
		}

	}

	Double iLat;
	Double iLng;
	GeoPoint intentPoint;
	ListItemModel itemDetalisItem;
	// this varibale say's if the marker of the place is placed or not , when
	// the acticity start
	// form Act_ItemDetalis
	Boolean markerplacedForThePlace = false;
	Boolean ordenaryplacedForThePlace = false;

	private void checkIntent() {
		intentLat = (String) getIntent().getSerializableExtra("lat");
		intentLng = (String) getIntent().getSerializableExtra("lng");

		
		if (intentLat != null && intentLng != null) {
			mainMap = false;
			itemDetalisItem = DataProcessor
					.getListingFromResponse((String) getIntent()
							.getSerializableExtra("jsonItem"));

			places.add(itemDetalisItem);
			Log.d("Act_Map", " intentLat : " + intentLat);
			Log.d("Act_Map", " intentLng : " + intentLng);
			intentPoint = new GeoPoint(
					(int) ((Double.parseDouble(intentLat)) * 1000000),
					(int) ((Double.parseDouble(intentLng)) * 1000000));

			mapController.animateTo(intentPoint);
			/*
			 * 
			 * addOverLay(intentPoint,itemDetalisItem.getTitle(),itemDetalisItem.
			 * getDesc(),ballonOverlay,itemDetalisItem.getCat_id());
			 * 
			 * addOverLay(currentPoint,null,null,ordenaryOverLay,null);
			 */

			CustomMapItem item = new CustomMapItem(currentPoint,
					"مكانك الحالي", "", null, R.drawable.ic_current_location);
			items.add(item);

			CustomMapItem overlayitem = new CustomMapItem(intentPoint,
					itemDetalisItem.getTitle(), itemDetalisItem.getDesc(),
					null, getCatBitmap(itemDetalisItem.getCat_id()));
			items.add(overlayitem);

			Log.d("Act_Map", " items.size() : " + items.size());
			customBallonOverlay = new CustomBallonOverlay(drawable, mapView, c,
					items,mainMap);
			mapOverlays = mapView.getOverlays();
			mapOverlays.add(customBallonOverlay);

		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	private void operateConnection(Double lat, Double lng, Boolean showLoading) {
		inConneciton = true;
		String link = "http://dilny.com/api/searchNearby/lat/" + lat + "/lng/"
				+ lng + "/radius/50/page/all/cat/";
		System.out.println(link);
		ConnectionController cc = new ConnectionController(link, this,
				ConnectionController.Con_map, showLoading,
				c.getString(R.string.str_loading));
		cc.setOnConnectionDone(this);
		cc.startGetConnection();
	}

	private void operateConnection(int lat, int lng, Boolean showLoading,String distance) {
		inConneciton = true;
		String link = "http://dilny.com/api/searchNearby/lat/"
				+ (lat / 1000000) + "/lng/" + (lng / 1000000)
				+ "/radius/"+distance+"/page/all/cat/";
		System.out.println(link);
		ConnectionController cc = new ConnectionController(link, this,
				ConnectionController.Con_map, showLoading,
				c.getString(R.string.str_loading));
		cc.setOnConnectionDone(this);
		cc.startGetConnection();
	}

	private void addOverLay(GeoPoint point, String title, String des,
			ItemizedOverlay overLayType, String catID) {
		mapOverlays = mapView.getOverlays();
		mapView.removeAllViews();
		if (intentLat != null && intentLng != null) {
			Log.d("Act_Map", "addOverLay intentLat !=null && intentLng !=null ");
			if (!markerplacedForThePlace) {
				Log.d("Act_Map", "addOverLay markerplacedForThePlace ");
				markerplacedForThePlace = true;

				OverlayItem overlayitem = new OverlayItem(point, title, des);
				// ballonOverlay.hideAllBalloons();
				ballonOverlay.addOverlay(overlayitem);
				mapOverlays.add(ballonOverlay);
				// ballonOverlay.populateOverLay();
				// ballonOverlay.onTap(0);

			}
			if (!ordenaryplacedForThePlace) {
				ordenaryplacedForThePlace = true;
				Log.d("Act_Map", "addOverLay markerplacedForThePlace else ");
				Log.d("Act_Map", "addOverLay OrdenaryMapOverLay ");
				OverlayItem overlayitem = new OverlayItem(point, null, null);
				ordenaryOverLay.addOverlay(overlayitem);
				mapOverlays.add(ordenaryOverLay);
			}
		} else {
			if (overLayType.getClass() == BallonOverlay.class) {

				Log.d("Act_Map", "addOverLay BallonOverlay ");
				OverlayItem overlayitem = new OverlayItem(point, title, des);
				 
				ballonOverlay.hideAllBalloons();
				ballonOverlay.addOverlay(overlayitem);
				mapOverlays.add(ballonOverlay);

			} else if (overLayType.getClass() == OrdenaryMapOverLay.class) {
				Log.d("Act_Map", "addOverLay OrdenaryMapOverLay ");
				OverlayItem overlayitem = new OverlayItem(point, null, null);
				ordenaryOverLay.addOverlay(overlayitem);
				mapOverlays.add(ordenaryOverLay);
			}
		}
 
	}
	private int getCatBitmap(String catID) {
		ArrayList<CatsModel> cats = DataHolder.getCatsFromDB(this);
		for (int x = 0; x < cats.size(); x++) {
			CatsModel cat = cats.get(x);
			
			if (cat.getId().equals(catID)) {
				String a[]=cat.getImage().split("/");
				String image = a[a.length-1];
				a =image.toString().split("\\.");
				image=a[0];
				System.out.println("image = "+image);
				int resID = getResources().getIdentifier(image , "drawable", getPackageName());
				if(resID==0){
					resID = getResources().getIdentifier("ic_launcher" , "drawable", getPackageName());
				}else{
					return resID;
				}
				
			}
		}
	/*private String getCatBitmap(String catID) {
		ArrayList<CatsModel> cats = DataHolder.getCatsFromDB(this);
		for (int x = 0; x < cats.size(); x++) {
			CatsModel cat = cats.get(x);
			
			if (cat.getId().equals(catID)) {
				Log.d("getCatBitmap", cat.getImage());
				ImageFileModel imag = dbHandler.GetImageFileFromDbWithUrl(cat
						.getImage());

				if (imag != null) {
					Log.d("imag", imag.getPathOnSD());
					return imag.getPathOnSD();
				} else {
					Log.d("imag", "null");
					return null;
				}

			}
		}*/

		return 0;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false; 
	}

	@Override
	public void done(int code, String response) {
		if (code == ConnectionController.Con_map_long_click) {
			inConneciton = false;
			Intent N = new Intent(this, Act_SearchResult.class);
			N.putExtra("response", response);
		 	N.putExtra("connection",code);
		 	N.putExtra("lat",iLat);
		 	N.putExtra("lng",iLng);
			startActivity(N);
		} else {
			uiHandler.sendEmptyMessage(2);
			// ballonOverlay = new BallonOverlay(
			// this.getResources().getDrawable(R.drawable.marker_place),mapView,this);

			places = DataProcessor.getListinigsListFromResponse(response);

			if (intentLat != null && intentLng != null) {
				places.add(0, itemDetalisItem);
			} else {
				ListItemModel item = new ListItemModel();
				item.setPoint(currentPoint);
				item.setTitle("مكانك الحالي");
				item.setDesc(null);
				item.setCat_id(null);
				places.add(0, item);
			}
			items = new ArrayList<CustomMapItem>();

			// items.add(new CustomMapItem(currentPoint, ,
			// null,null,R.drawable.ic_current_location));

			for (int x = 0; x < places.size(); x++) {
				ListItemModel item = places.get(x);
				System.out.println("getTitle()" + item.getTitle());
				System.out.println("getlat()" + item.getlat());
				System.out.println("getlng()" + item.getlng());
				CustomMapItem overlayitem;
				if (x == 0) {
					overlayitem = new CustomMapItem(item.getPoint(),
							item.getTitle(), item.getDesc(), null,
							R.drawable.ic_current_location);
				} else {
					GeoPoint point = new GeoPoint((int) (Double.valueOf(item
							.getlat()) * 1000000), (int) (Double.valueOf(item
							.getlng()) * 1000000));
					int image = getCatBitmap(item.getCat_id());

					if (image != 0) {
						overlayitem = new CustomMapItem(point, item.getTitle(),
								item.getDesc(), null,
								getCatBitmap(item.getCat_id()));
					} else {
						overlayitem = new CustomMapItem(point, item.getTitle(),
								item.getDesc(), null, R.drawable.marker_place);
					} 
				} 

				items.add(overlayitem);

				// addOverLay(,ballonOverlay,item.getCat_id());
			}
			// addOverLay(currentPoint,null,null,ordenaryOverLay,null);
			// handler.sendEmptyMessage(0);

			customBallonOverlay = new CustomBallonOverlay(drawable, mapView, c,
					items, mainMap);
			mapOverlays = mapView.getOverlays();
			mapOverlays.clear();
			mapOverlays.add(customBallonOverlay);
			inConneciton = false;

		}

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

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
				searchNearby(e);

			}
		});

		dialog.show();

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		GeoPoint initGeoPoint = mapView.getMapCenter();

		Location gpsLocation = new Location("");
		gpsLocation.setLatitude(currentPoint.getLatitudeE6() / 1E6);
		gpsLocation.setLongitude(currentPoint.getLongitudeE6() / 1E6);

		Location mapLocation = new Location("");
		mapLocation.setLatitude(initGeoPoint.getLatitudeE6() / 1E6);
		mapLocation.setLongitude(initGeoPoint.getLongitudeE6() / 1E6);
		Double distance = round(
				(mapLocation.distanceTo(gpsLocation) / 1000), 2);
		System.out.println("Distance : " + distance + "KM");
		
		strDistance="بعدك عن موضعك الحالي : "+distance +" كم";
		uiHandler.sendEmptyMessage(0);
		if (intentLat == null && intentLng == null) {
			if (!inConneciton) {

				int zoomLevel = mapView.getZoomLevel();

				
				/*
				 * System.out.println("zoomLevel : " +zoomLevel);
				 * 
				 * System.out.println("currentPoint : "+(currentPoint.getLatitudeE6
				 * ()/1E6) + "KM");
				 * System.out.println("currentPoint : "+(currentPoint
				 * .getLongitudeE6()/1E6) + "KM");
				 * 
				 * System.out.println("initGeoPoint : "+(initGeoPoint.getLatitudeE6
				 * ()/1E6) + "KM");
				 * System.out.println("initGeoPoint : "+(initGeoPoint
				 * .getLongitudeE6()/1E6) + "KM");
				 */

				

				if (distance > 50) {
					uiHandler.sendEmptyMessage(1);
					operateConnection(currentPoint.getLatitudeE6(),
							currentPoint.getLongitudeE6(), false,distance+"");
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
	String intentLng;
	String intentLat;
	private void searchNearby(MotionEvent e) {
		Projection proj = mapView.getProjection();
		GeoPoint loc = proj.fromPixels((int) e.getX(), (int) e.getY());
		iLng=loc.getLongitudeE6() / 1E6;
		iLat=loc.getLatitudeE6() / 1E6;
		String lng= Double.toString(iLng);
		String  lat= Double.toString(iLat);
		inConneciton = true;
		String link = "http://dilny.com/api/searchNearby/lat/" + lat + "/lng/"
				+ lng + "/radius/50/page/all/cat/";
		System.out.print("ACTION_DOWN :" + link);
		ConnectionController cc = new ConnectionController(link, c,
				ConnectionController.Con_map_long_click, true,
				c.getString(R.string.str_loading));
		cc.setOnConnectionDone(Act_Map.this);
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
		DataHolder.setData(this, DataHolder.Mail, DataHolder.Password,
				DataHolder.User_ID, DataHolder.rememberMe);
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
	
String strDistance;

	Handler uiHandler = new Handler() {
	  	  public void handleMessage(Message message) {	  		
	  	    switch (message.what) {
	  	    case 0:
	  	    	if(tvDistabce!=null){
	  	    		tvDistabce.setText(strDistance);
	  	    	}
	  	    	 
	  	    	break;
	  	    case 1:
	  	    	pd.setVisibility(View.VISIBLE);
	  	         	
	  	    	break;
	  	  case 2:
	  	    	pd.setVisibility(View.INVISIBLE);
	  	     
	  	    	break;
	  	    
	  	    }
	  	  }
  	};

}
