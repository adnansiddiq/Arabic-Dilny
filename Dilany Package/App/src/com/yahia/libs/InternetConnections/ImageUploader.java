package com.yahia.libs.InternetConnections;
 
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

public class ImageUploader {
	Context c; 
	
	private final int notification_ID=200;
	public ImageUploader(Context _c,final List<NameValuePair> nameValuePairs,String Message,int drawable,int layout){
		c=_c; 	  
		showUploadingProgressBar(Message, drawable, layout);
		
		Thread d=new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("ImageUploader"); 	        
		        
		        new HttpConnection(handler).post("http://dilny.com/api/uploadImage",nameValuePairs );
				
			} 
		});
		d.start();
	} 
	 private void hideUploadingProgressBar(){
		 System.out.println("hideUploadingProgressBar");
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(ns);
		 mNotificationManager.cancel(notification_ID);
	 }
	 private void showUploadingProgressBar(String Message,int drawable,int layout){
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(ns);
		 
		 //int icon = R.drawable.ic_launcher;
		 int icon = drawable;
		 //CharSequence tickerText = c.getString(R.string.str_uploading_image);
		 CharSequence tickerText =Message;
		 long when = System.currentTimeMillis();
		 Notification notification = new Notification(icon, tickerText, when);
		 //RemoteViews contentView = new RemoteViews(c.getPackageName(), R.layout.progressbar_notificationbar);
		 RemoteViews contentView = new RemoteViews(c.getPackageName(), layout);	 
		 
		 notification.contentView = contentView;
		 
		 Intent notificationIntent = new Intent(c, ImageUploader.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(c, 0, notificationIntent, 0);
		 notification.contentIntent = contentIntent;
		 
		 mNotificationManager.notify(notification_ID, notification);

		 
	 }
	 Handler handler = new Handler() {
		 
	  	  public void handleMessage(Message message) {
	  	    switch (message.what) {
	  	   
	  	case HttpConnection.DID_START:  
	  		 
		    break;  
		case HttpConnection.DID_SUCCEED:
			hideUploadingProgressBar();
	  	    break;
		case HttpConnection.DID_ERROR: 
			hideUploadingProgressBar();
		    break;
		     
		    }
	  	  } 
	  	};
}
