package com.ksa.dilny.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import libs.HttpConnection;
import libs.db.DBAdapter;

import org.apache.http.NameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ksa.dilny.R;
import com.ksa.dilny.DataModel.CatsModel;
import com.ksa.dilny.DataModel.LevelModel;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.DataModel.MSGs;
import com.yahia.libs.ImageFactory;
import com.yahia.libs.db.ExportDB;

public class ConnectionController {
	public static int Con_Nerby=200;
	public static int Con_Cats=201;
	public static int Con_AddNew=202;
	public static int Con_AddNewFavorites=203;
	public static int Con_UploadImage=204;
	public static int Con_Sigin=205;
	public static int Con_Sigup=206;
	public static int Con_Cats_data_only=207;
	public static int Con_Favorites=208;
	public static int Con_TextSearch=209;
	public static int Con_map=210;
	public static int Con_map_long_click=211;
	public static int Con_rating=212;
	public static int Con_reviews=213;
	public static int Con_addreviews=214;
	public static int Con_initApp=215;
	public static int Con_GeoAddress=216;
	public static int Con_LisitingDetalis=217; 
	
	Context c;
	int connectionType;
	String url;
	ProgressDialog dialog;
	Boolean showLoading;
	String Msg;
	public ConnectionController(String _url,Context _c,int _connectionType,Boolean _showLoading,String _MSG){
		c=_c;
		connectionType=_connectionType;
		url=_url;
		System.out.println("Link : "+url);
		
		dialog=new ProgressDialog(c);
		dialog.setCancelable(false);
		showLoading=_showLoading;
		Msg=_MSG;
	}
	public void startGetConnection(){
		new HttpConnection(handler).get(url);
	}
	
	public void startPostConnection( List<NameValuePair> data){
		new HttpConnection(handler).post(url, data) ;
	}
	 
	Handler handler = new Handler() {
	  	  public void handleMessage(Message message) {	  		
	  	    switch (message.what) {
		  	    case HttpConnection.DID_START:
		  	    	System.out.println("DID_START"); 
		  	    	if(showLoading){
		  	    		dialogHandler.sendEmptyMessage(1);
		  	    	}
		  	    	
		  	    break;
		  	    case HttpConnection.DID_SUCCEED:
		  	    	if(showLoading){
		  	    		dialogHandler.sendEmptyMessage(0);
		  	    	}
		  	    	
		  	    	String response = (String) message.obj;	
		  	    	if(response.length()==5 && response.charAt(0)=='E'){
			  	    	MSGs.showMessage(c, response);
			  	    	if(connectionType==Con_rating){
			  	    		listener.done(connectionType, response);
			  	    	}
			  	    }else { 
			  	    	if(connectionType==Con_Cats_data_only){
			  	    		catsNoDataMethod(response);
			  	    	}else if(connectionType==Con_initApp){
			  	    		initAppMethod(response);
			  	    	}else if(connectionType==Con_Favorites){
			  	    		favoritesMethod(response);
			  	    	}else {
			  	    	
			  	    		listener.done(connectionType, response);
			  	    	}
			  	    	/*
			  	    	if(connectionType==Con_Cats){
			  	    		catsMethod( response);
			  	    	}else if(connectionType==Con_Nerby){
			  	    		nearbyMethod(response);
			  	    	}else if(connectionType==Con_Sigin){
			  	    		siginMethod(response);
			  	    	}else if(connectionType==Con_Cats_data_only){
			  	    		catsNoDataMethod(response);
			  	    	}else if(connectionType==Con_Favorites){
			  	    		favoritesMethod(response);
			  	    	}else if(connectionType==Con_AddNew){
			  	    		addNewMethod(response);
			  	    	}*/
			  	    	
			  	    		
			  	    }
		  	    	
		  	    	 
			  	break;
		  	    case HttpConnection.DID_ERROR:
		  	    	Toast.makeText(c, c.getString(R.string.str_connection_error), Toast.LENGTH_SHORT).show();
		  	    	dialogHandler.sendEmptyMessage(0);
		  	      Exception e = (Exception) message.obj;
		  	      System.out.println("DID_ERROR");
		  	      System.out.println("e : "+e.getMessage());
		  	    break;
	  	    }
	  	  }
	  	  
	  	

	




		Handler dialogHandler = new Handler() {
		  	  public void handleMessage(Message message) {	  		
		  	    switch (message.what) {
		  	    case 0:
		  	    	try {
		  	    		dialog.dismiss();
		  	    	}catch (Exception e){
		  	    		
		  	    	}
		  	    	
		  	    	
		  	    	break;
		  	    case 1:
		  	    	try {
		  	    		dialog.setMessage(Msg);
			  	    	dialog.show();
		  	    	}catch (Exception e){
		  	    		
		  	    	}
		  	    	
		  	    	break;
		  	    
		  	    }
		  	  }
	  	};

			private void catsNoDataMethod(String response) {  
		  	    	 ArrayList<CatsModel> catsList=DataProcessor.getCatsFromResponse(response);
		  	    	 if(catsList.size()>=1){
		  	    		DBAdapter dba = new DBAdapter(c);
		  	    		dba.open();
		  	    		dba.truncateCatsTable();
		  	    		for(int x=0;x<catsList.size();x++){
		  	    			CatsModel cat=catsList.get(x);
		  	    			dba.insertIntoCats(cat);
		  	    		}
		  	    		dba.close();  
		  	    		listener.done(0, "");
		  	    	 }
			}
			


			private void initAppMethod(String response) { 
				ArrayList<LevelModel> levList=DataProcessor.getLevelsFromResponse(response);   
	  	    	 if(levList.size()>=1){
	  	    		DBAdapter dba = new DBAdapter(c);
	  	    		dba.open();
	  	    		dba.truncateCatsTable();
	  	    		dba.truncateLevelsTable();
	  	    		
	  	    		for(int x=0;x<levList.size();x++){ 
	  	    			LevelModel lev=levList.get(x);
	  	    			ArrayList<CatsModel> cats=lev.getCats();
	  	    			for(int y=0;y<cats.size();y++){   
		  	    			CatsModel cat=cats.get(y);
		  	    			dba.insertIntoCats(cat);
		  	    		}
	  	    			dba.insertIntoLevels(lev);
	  	    		}
	  	    		dba.close();  
	  	    		listener.done(0, "");
	  	    	 }
	  	    	 
	  	    	 ExportDB.extract("some_folder","dilny.db","/data/data/com.ksa.dilny/databases/dilny.db"); 
				
			}


			private void nearbyMethod(String response) {				
				listener.done(Con_Nerby, response);	  	    	 
			}
			private void favoritesMethod(String response) {
				DBAdapter dba=new DBAdapter(c);
				ArrayList<ListItemModel> items=DataProcessor.getListinigsListFromResponse(response);
				dba.open();
				dba.truncateFavoritesTable();
				for(int x=0;x<items.size();x++){
					ListItemModel item=items.get(x);
					dba.insertIntoFavorites(item);
				}
				dba.close();
				listener.done(Con_Favorites, response);
			}

			private void catsMethod(String response) {
	  	    	listener.done(Con_Cats, response)	;			
			}
			private void siginMethod(String response) {
				listener.done(Con_Sigin,response);
			}
			private void addNewMethod(String response) {
				listener.done(Con_AddNew, response);
			}

	  	};
	  	
	  	public interface onConnectionDoneCC  {
		    public void done(int code,String response);
		}
	  	onConnectionDoneCC listener;
	  	public void setOnConnectionDone(onConnectionDoneCC listen) {
			listener = listen;
	    }
	  	public void CreatePostConnection(final String URL,final String [] params,final String [] paramsNames , final String [] imagesLinks,final String [] imagesNames){
	  		 Thread r=new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpURLConnection connection = null;
			  	    DataOutputStream outputStream = null;
			  	    
			  	  int bytesRead, bytesAvailable, bufferSize;
			  	    byte[] buffer;
			  	    int maxBufferSize = 1 *  1024;
			  	    
			  	 
			  	String boundary = "*****";
			  	
				try {
					URL url = new URL(URL);
					connection = (HttpURLConnection) url.openConnection();
					
					 // Allow Inputs & Outputs
				      connection.setDoInput(true);
				      connection.setDoOutput(true);
				      connection.setUseCaches(false);
				      
				   // Enable POST method
				      connection.setRequestMethod("POST");
				      connection.setRequestProperty("Connection", "Keep-Alive");
				      connection.setRequestProperty("Content-Type",
				          "multipart/form-data;boundary=" + boundary);
	 
				      
				      StringBuilder body=new StringBuilder();
				  		for(int x=0;x<params.length;x++){
				  			body.append("--");
				  			body.append(boundary);
				  			body.append("\r\n");
				  			body.append("Content-Disposition: form-data; name=\"");
				  			body.append(paramsNames[x]);
				  			body.append("\"\r\n\r\n");
				  			body.append(params[x]);
				  			body.append("\r\n");
				  					
				  		}
				  		
				      outputStream = new DataOutputStream(connection.getOutputStream());
				      outputStream.writeBytes(body.toString());
				      outputStream.flush(); 
				        
				      for(int x=0;x<imagesLinks.length;x++){
				    	  outputStream.writeBytes("--");
				    	  outputStream.writeBytes(boundary);
				    	  outputStream.writeBytes("\r\n");
				    	  outputStream.writeBytes("Content-Disposition: form-data; name=");
				    	  outputStream.writeBytes(imagesNames[x]);
				    	  outputStream.writeBytes(";filename=\"");
				  			String [] ar=imagesLinks[x].split("\\\\."); 
				  			outputStream.writeBytes(ar[ar.length-1]);
				  			outputStream.writeBytes("\"\r\n");
				  			System.gc();
						    Runtime.getRuntime().gc();
				  			outputStream.writeBytes("Content-Type: image/png\r\n\r\n"); 
				  			
				  			/*BitmapFactory.Options opts = new BitmapFactory.Options(); 
				  			opts.inSampleSize=4;
				  			Bitmap b = BitmapFactory.decodeFile(imagesLinks[x],opts);
				  			
 				            ByteArrayOutputStream baos = new ByteArrayOutputStream();
					  		 b.compress(CompressFormat.PNG, 50, baos);
					  		 
					  		byte[] data = baos.toByteArray();*/
				  			System.out.println("imagesLinks[x] : "+imagesLinks[x]);
				  			Bitmap im=ImageFactory.getImage(imagesLinks[x],0,0);
				  			ByteArrayOutputStream baos = new ByteArrayOutputStream();
				  			im.compress(CompressFormat.PNG, 50, baos);
				  			byte[] data = baos.toByteArray();
					  		outputStream.write(data);
					  		  
				  		 /*
				  			 FileInputStream fileInputStream = new FileInputStream(new File(imagesLinks[x]));
						      bytesAvailable = fileInputStream.available();
	 					      bufferSize = Math.min(bytesAvailable, maxBufferSize);
	 					      Log.d("bufferSize", bufferSize+"");
						      buffer = new byte[bufferSize];
						   // Read file
						      bytesRead = fileInputStream.read(buffer, 0, bufferSize);

						      while (bytesRead > 0) {
						        outputStream.write(buffer, 0, bufferSize);
						        
						        bytesAvailable = fileInputStream.available();
						        bufferSize = Math.min(bytesAvailable, maxBufferSize);
						        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
						      } */
						      outputStream.writeBytes("\r\n");
						      outputStream.flush();
						     // fileInputStream.close();
				  					
				  		}
				  
				      
				      outputStream.writeBytes("--"+boundary+"--\r\n");
				      outputStream.flush();		
	 			      outputStream.close();
				      
				      InputStream is = connection.getInputStream();
				      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				      String line;
				      StringBuffer response = new StringBuffer(); 
				      while((line = rd.readLine()) != null) {
				        response.append(line);
				        response.append('\r');
				      }
				      rd.close();
				      Log.d("response ",response.toString());
				      
				      listener.done(Con_UploadImage,MSGs.MSG_DONE);	  
				} catch (Exception e) {
					listener.done(Con_UploadImage,"E1111");
					e.printStackTrace();
				} finally {
				      connection.disconnect();
			    }
			    
					
				}
			});
		         r.start();
	  	} 
	  	private Bitmap decodeFile(File f){
	  	    try {
	  	        //Decode image size
	  	        BitmapFactory.Options o = new BitmapFactory.Options();
	  	        o.inJustDecodeBounds = true;
	  	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	  	        //The new size we want to scale to
	  	        final int REQUIRED_SIZE=70;

	  	        //Find the correct scale value. It should be the power of 2.
	  	        int width_tmp=o.outWidth, height_tmp=o.outHeight;
	  	        int scale=1;
	  	        while(true){
	  	            if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	  	                break;
	  	            width_tmp/=2;
	  	            height_tmp/=2;
	  	            scale*=2;
	  	        }

	  	        //Decode with inSampleSize
	  	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	  	        o2.inSampleSize=scale;
	  	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	  	    } catch (FileNotFoundException e) {}
	  	    return null;
	  	}
}
