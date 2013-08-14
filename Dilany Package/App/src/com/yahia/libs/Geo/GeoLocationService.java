package com.yahia.libs.Geo;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressCoder;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressCoder.GoogleGeoAddressCoderListener;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressModel;

public class GeoLocationService implements LocationListener,
		GoogleGeoAddressCoderListener {
	public static double lat = 0.0;
	public static double lng = 0.0;
	public static StringBuilder Adress = null;
	public static String Country = null;
	public static String City = null;
	public static String Area = null;
	public static String FullAdress = null;
	public static Boolean loadAddress = false;
	String provider;
	Boolean serviceStatus = true;
	public static Context c;
	GeoPoint point;
	GoogleGeoAddressCoder ad;
	LocationManager locationManager;

	public interface GeoLocationServiceListener {
		public void updateUi(String _Adress, int code);
	}

	GeoLocationServiceListener listener;

	public void setTheListener(GeoLocationServiceListener listen) {
		listener = listen;
	}

	/**
	 * GeoLocationService
	 * <p>
	 * 
	 * @param Context
	 *            context of activity that will use thw Geo Service
	 */
	public GeoLocationService(Context _c) {
		c = _c;
		ad = new GoogleGeoAddressCoder(c, false, "");
		ad.setTheListener(this);
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) c
				.getSystemService(Context.LOCATION_SERVICE);
		// Register the listener with the Location Manager to receive location
		// updates
		String provider = "";
		List<String> proidersStr = locationManager.getAllProviders();
		for (int x = 0; x < proidersStr.size(); x++) {
			provider = proidersStr.get(x);
			if (provider.equals(LocationManager.GPS_PROVIDER)) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, this);
				provider = LocationManager.GPS_PROVIDER;
				break;
			}
		}
	 
			for (int x = 0; x < proidersStr.size(); x++) {
				provider = proidersStr.get(x);
				if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 0, 0, this);
					provider = LocationManager.NETWORK_PROVIDER;
					break;
				}

				if (provider.equals(LocationManager.PASSIVE_PROVIDER)) {
					locationManager.requestLocationUpdates(
							LocationManager.PASSIVE_PROVIDER, 0, 0, this);
					provider = LocationManager.PASSIVE_PROVIDER;
					break;
				}

			}
		 

		// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		// 0, 0, this);
		/*
		 * try{ Location
		 * location=locationManager.getLastKnownLocation(LocationManager
		 * .GPS_PROVIDER); lat=location.getLatitude();
		 * lng=location.getLongitude();
		 * 
		 * point=new GeoPoint((int)(lat*1E6), (int)(lng*1E6));
		 * ad.getGeoCoderWithThread(point);
		 * //locationManager.requestLocationUpdates
		 * (LocationManager.NETWORK_PROVIDER, 0, 0, this);
		 * 
		 * 
		 * }catch (Exception e){
		 * //Log.d("GeoLocationService","GPS_PROVIDER : Error : "
		 * +e.getMessage()); try{ if(lat ==0.0){ Location
		 * location=locationManager
		 * .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		 * lat=location.getLatitude(); lng=location.getLongitude(); point=new
		 * GeoPoint((int)(lat*1E6), (int)(lng*1E6));
		 * ad.getGeoCoderWithThread(point); } }catch (Exception ee){
		 * //Log.d("GeoLocationService"
		 * ,"NETWORK_PROVIDER : Error : "+e.getMessage()); } }
		 */

		/*
		 * if(lat !=0.0){ //Log.d("GeoLocationService","lat !=0.0"); point=new
		 * GeoPoint((int)(lat*1E6), (int)(lng*1E6));
		 * ad.getGeoCoderWithThread(point);
		 * 
		 * }
		 */
		
		
	 
	}

	@Override
	public void onLocationChanged(Location location) {
		if (serviceStatus) {
			// Log.d("GeoLocationService","onLocationChanged");
			lat = location.getLatitude();
			lng = location.getLongitude();
			point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
			if (loadAddress) {
				ad.getGeoCoderWithThread(point);
				loadAddress = false;
			}

		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Log.d("GeoLocationService","Geo-> provider : "+provider);
		// Log.d("GeoLocationService","Geo-> status : "+status);

	}

	@Override
	public void addressDone(GeoPoint point, GoogleGeoAddressModel address) {
		Adress = new StringBuilder();
		Country = address.getCountry();
		FullAdress = address.getFullAdress();

		City = "";
		if (address.getSubLocality() != null) {
			City = City + address.getSubLocality() + ", ";
			// Log.d("GeoLocationService","address.getSubLocality() : "+address.getSubLocality());
		}
		if (address.getLocality() != null) {
			City = City + address.getLocality() + ", ";
			// Log.d("GeoLocationService","address.getLocality() : "+address.getLocality());
		}
		if (address.getAdminLevel1() != null) {
			City = City + address.getAdminLevel1();
			// Log.d("GeoLocationService","address.getAdminLevel1() : "+address.getAdminLevel1());
		}
		if (address.getAdminLevel2() != null) {
			City = City + ", " + address.getAdminLevel2();
			// Log.d("GeoLocationService","address.getAdminLevel2() : "+address.getAdminLevel2());
		}
		if (address.getAdminLevel3() != null) {
			City = City + ", " + address.getAdminLevel3();
			// Log.d("GeoLocationService","address.getAdminLevel3() : "+address.getAdminLevel3());
		}

		if (address.getStreet_number() != null) {
			Adress.append(address.getStreet_number() + ",");
		}

		if (address.getRoute() != null) {
			Adress.append(address.getRoute());
		}
		setGeoData(c, "");
		listener.updateUi(Adress.toString(),1);

	}

	@Override
	public void addressfail(GeoPoint point) {
		listener.updateUi(null,2);

	}
	public void addressNoResultFound(GeoPoint point){
		listener.updateUi(null,3);
	}
	public static void setGeoData(Context c, String PREFS_NAME) {
		SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("LAT", (float) lat);
		editor.putFloat("LNG", (float) lng);
		if (Adress != null) {
			editor.putString("Adress", Adress.toString());
		}

		editor.putString("Country", Country);
		editor.putString("City", City);
		editor.putString("Area", Area);
		editor.putString("FullAdress", FullAdress);

		// Commit the edits!
		editor.commit();

	}

	public void stopService() {
		serviceStatus = false;
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
	}

	public Boolean checkService() {
		return serviceStatus;
	}

	public void startService() {
		serviceStatus = true;
		if (provider != null) {
			if (locationManager != null) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, this);
				// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				// 0, 0, this);
			}
		}
	}

	public static void getGeoData(String PREFS_NAME) {
		// Restore preferences
		SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);

		Adress = new StringBuilder();
		Adress.append(settings.getString("Adress", null));
		Country = settings.getString("Country", null);
		City = settings.getString("City", null);
		Area = settings.getString("Area", null);
		FullAdress = settings.getString("FullAdress", null);
		if (settings.getFloat("LAT", 0) == 0) {
			lat = 0.0;
		} else {
			lat = settings.getFloat("LAT", 0);
		}

		if (settings.getFloat("LNG", 0) == 0) {
			lng = 0.0;
		} else {
			lng = settings.getFloat("LNG", 0);
		}

	}

}
