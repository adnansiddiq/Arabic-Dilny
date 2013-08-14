package com.yahia.libs.Geo.Coder;

public class OpenMapGeoCoder {

}
/*
package com.yahia.libs.Geo.Coder;

import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ProgressDialog;
import android.content.Context; 
import android.content.Entity;
import android.location.Address;
import android.location.Geocoder;
import android.net.ParseException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.maps.GeoPoint;   

public class GeoAddressCoder    {
	List<Address> adresss;
	double lat;
	double lng;
	Context c;
	GeoPoint point;
	ProgressDialog pd;
	Boolean showDialog;
	
	String googleApiKey = "2130968578";
    HttpURLConnection connection = null;
    URL serverAddress = null;
	
	Handler handler = new Handler() {
	  	  public void handleMessage(Message message) {	  		
	  		  if(showDialog){
	  			switch (message.what) {
		  	    case 0:
		  	    	pd.dismiss();	  	    	
		  	    	break;
		  	    case 1:
		  	    	pd.show();
		  	    	break;
		  	    
		  	    }
	  		  }
	  	    
	  	  }
	};
	
	
	public interface GeoAddressCoderListener  {
	    public void addressDone(GeoPoint point,GeoAddressModel address); 
	    public void addressfail(GeoPoint point);
	}
	
	GeoAddressCoderListener listener;
	
	public void setTheListener(GeoAddressCoderListener listen) {
		listener = listen;
    }
	
	
	public GeoAddressCoder( Context _c,Boolean _showDialog,String loadingMsg){
		c=_c;
		showDialog=_showDialog;		
		 
		pd=new ProgressDialog(c);
		pd.setMessage(loadingMsg);
		
	}
	public void getGeoCoderWithThread( GeoPoint _point){	
		point=_point;
		lat=point.getLatitudeE6()/1E6;
		lng=point.getLongitudeE6()/1E6;
		
		Thread d=new Thread(new Runnable() {			
			@Override
			public void run() {			
				handler.sendEmptyMessage(1);
				String Link="http://nominatim.openstreetmap.org/reverse?format=json&lat="+lat+"&lon="+lng+"&accept-language=arabic"; 
				//String Link="http://maps.google.com/maps/geo?output=json&key=AIzaSyCy9oAh44fIW88aRIuto_enmKDJr_3rVoc&lh=ar&q="+lat+","+lng;
			
				Log.d("GeoAddressCoder","Link : "+Link);
				if(listener!=null)
				{
					try {
						HttpClient httpclient = new DefaultHttpClient();  
		               HttpGet httpGet = new HttpGet(Link);    

		               HttpResponse response = httpclient.execute(httpGet);
		               
		               HttpEntity entity = response.getEntity();
		                
		               String response_text = getResponseBody(entity);
		               Log.d("GeoAddressCoder","response_text : "+response_text);
		               
		               JSONObject objs=new JSONObject(response_text);
		               String Status=  objs.get("status").toString();
		               //Log.d("GeoAddressCoder","Status : "+Status.toString());
		               
		               if(Status.equals("OK")){
		               //if(Status.get("code").equals("200")){
		            	   ArrayList<InfoPoint> ip=parsePoints(response_text);		              
			               //Log.d("GeoAddressCoder"," ip.size() : "+ ip.size());
			               GeoAddressModel adress=new GeoAddressModel( );
			               //ArrayList<String> ad=new ArrayList<String>();
			               InfoPoint p=ip.get(0);
			               adress.setFullAdress(p.getStrFormattedAddress());
			               adress.setLat(p.getDblLatitude());
			               adress.setLng(p.getDblLongitude());
	           	   			for(int x=0;x<p.getAddressFields().size();x++){
	           	   				HashMap<String, Object> obj=p.getAddressFields().get(x);
	           	   				
	           	   				String object=obj.get("long_name").toString();
	           	   				String type=obj.get("types").toString();
	           	   				// Log.d("GeoAddressCoder","type.toString() : "+type.toString());
	           	   				
	           	   				if(type.toString().equals(GeoAddressModel.CODE_COUNTRY)){
	           	   					adress.setCountry(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_LOCALITY)){
	           	   					adress.setLocality(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_STREET_NUMBER)){
	           	   					adress.setStreet_number(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_ADMIN_LEVEL_1)){
	           	   					adress.setAdminLevel1(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_ADMIN_LEVEL_2)){
	           	   					adress.setAdminLevel2(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_ADMIN_LEVEL_3)){
	           	   					adress.setAdminLevel3(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_ROUTE)){
	           	   					adress.setRoute(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_PSTAL_CODE)){
	           	   					adress.setPostalCcode(object);
	           	   				}else if(type.toString().equals(GeoAddressModel.CODE_SUBLOCALITY)){
	           	   					adress.setSubLocality(object);
	           	   				} 

	           	   				 
	           	   			}
	           	   		listener.addressDone(point, adress);
		               }else {
		            	   	listener.addressfail(point);
		               }
		               
		               	
           	   		 
           	   		handler.sendEmptyMessage(0);
		               
		            
		               
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		});
		d.start();
		
	
	}   
	public static String getResponseBody(final HttpEntity entity) throws IOException, ParseException {  
		 InputStream instream = entity.getContent(); 
		 String charset = HTTP.UTF_8;
		 Reader reader = new InputStreamReader(instream, charset);
		 StringBuilder buffer = new StringBuilder();
		  try {
			  char[] tmp = new char[1024];
			  int l;
			  while ((l = reader.read(tmp)) != -1) {
				  buffer.append(tmp, 0, l);
				  }
			} finally {
				reader.close();
		}
		return buffer.toString();
	 }
	
	private ArrayList<InfoPoint> parsePoints(String strResponse) {
        // TODO Auto-generated method stub
        ArrayList<InfoPoint> result=new ArrayList<InfoPoint>();
        try {
            JSONObject obj=new JSONObject(strResponse);
            JSONArray array=obj.getJSONArray("results");
             
            for(int i=0;i<array.length();i++)
            {
                            InfoPoint point=new InfoPoint();

                JSONObject item=array.getJSONObject(i);
                ArrayList<HashMap<String, Object>> tblPoints=new ArrayList<HashMap<String,Object>>();
                JSONArray jsonTblPoints=item.getJSONArray("address_components");
                for(int j=0;j<jsonTblPoints.length();j++)
                {
                    JSONObject jsonTblPoint=jsonTblPoints.getJSONObject(j);
                    HashMap<String, Object> tblPoint=new HashMap<String, Object>();
                    Iterator<String> keys=jsonTblPoint.keys();
                    while(keys.hasNext())
                    {
                        String key=(String) keys.next();
                        if(tblPoint.get(key) instanceof JSONArray)
                        {
                            tblPoint.put(key, jsonTblPoint.getJSONArray(key));
                        }
                        tblPoint.put(key, jsonTblPoint.getString(key));
                    }
                    tblPoints.add(tblPoint);
                }
                point.setAddressFields(tblPoints);
                point.setStrFormattedAddress(item.getString("formatted_address"));
                JSONObject geoJson=item.getJSONObject("geometry");
                JSONObject locJson=geoJson.getJSONObject("location");
                point.setDblLatitude(Double.parseDouble(locJson.getString("lat")));
                point.setDblLongitude(Double.parseDouble(locJson.getString("lng")));

                result.add(point);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
 

}*/

 
