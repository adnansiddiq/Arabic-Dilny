package libs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ksa.dilny.Act_ItemDetalis;
import com.ksa.dilny.R;
import com.ksa.dilny.DataModel.MSGs;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.yahia.libs.Geo.GeoLocationService;

public class ImageUploader implements onConnectionDoneCC{
	Context c;
	//Bitmap image; 
	String user_id;
	String listinig_id;

	String listinigDetalis;
	private final int notification_ID=200;
	private final int notification_goto=201;
	private final int notification_Lisiting=201;
	
	public ImageUploader(Context _c,String imageFilePath,String _user_id,String _listinig_id){
		c=_c; 
		//image=_image;
		user_id=_user_id;
		listinig_id=_listinig_id;
		showUploadingProgressBar(); 
		
		//ConnectionController cc=new ConnectionController("http://dilny.com/api/uploadImage", c, ConnectionController.Con_UploadImage,true,c.getString(R.string.str_msg_waitting_for_adding_new));
		//cc.setOnConnectionDone(this);
		String [] params={user_id,listinig_id};
		String [] paramsNames={"user","listing_id"};
		 
		String [] imagesLinks={imageFilePath };
		String [] imagesNames={"image" };
		
		ConnectionController cc=new ConnectionController("http://dilny.com/api/uploadImage", c, ConnectionController.Con_UploadImage,true,c.getString(R.string.str_msg_waitting_for_adding_new));
		cc.setOnConnectionDone(this);
		cc.CreatePostConnection("http://dilny.com/api/uploadImage", params, paramsNames, imagesLinks, imagesNames);
			
		/*
		//Convert Image To Base64
		image = Bitmap.createScaledBitmap(image, 500, 500, true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 50, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        
        //Initialise varibales of link
        String image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT); 
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("user", user_id));
	        nameValuePairs.add(new BasicNameValuePair("listing_id",listinig_id ));
	        nameValuePairs.add(new BasicNameValuePair("image", image_str)); 
	        
		cc.startPostConnection(nameValuePairs) ;*/
			
			 
		 
	} 
	 private void hideNotification(int id){
		 Log.d("ImageUploader","hideGoToNotification");
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(ns);
		 mNotificationManager.cancel(id);
	 }
	 
	 
	 private void showLoadingLitinigDetalis(){
		String link="http://dilny.com/api/listingDetails/id/"+listinig_id+"/lat/"+GeoLocationService.lat+"/lng/"+GeoLocationService.lng;
		 ConnectionController cc=new ConnectionController(link, c, ConnectionController.Con_LisitingDetalis,true,c.getString(R.string.str_msg_waitting_for_adding_new));
		cc.setOnConnectionDone(this);
		cc.startGetConnection();
		
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(ns);
		 
		 int icon = R.drawable.ic_launcher;
		 CharSequence tickerText = c.getString(R.string.str_uploading_image);
		 long when = System.currentTimeMillis();
		 Notification notification = new Notification(icon, tickerText, when);
		 RemoteViews contentView = new RemoteViews(c.getPackageName(), R.layout.loading_lisiting_notificationbar);  
		 
		 notification.contentView = contentView;
		 
		 Intent notificationIntent = new Intent(c, ImageUploader.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(c, 0, notificationIntent, 0);
		 notification.contentIntent = contentIntent;
		 
		 mNotificationManager.notify(notification_Lisiting, notification);		 
	 }
	 private void showUploadingProgressBar(){
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(ns);
		 
		 int icon = R.drawable.ic_launcher;
		 CharSequence tickerText = c.getString(R.string.str_uploading_image);
		 long when = System.currentTimeMillis();
		 Notification notification = new Notification(icon, tickerText, when);
		 RemoteViews contentView = new RemoteViews(c.getPackageName(), R.layout.progressbar_notificationbar);  
		 
		 notification.contentView = contentView;
		 
		 Intent notificationIntent = new Intent(c, ImageUploader.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(c, 0, notificationIntent, 0);
		 notification.contentIntent = contentIntent;
		 
		 mNotificationManager.notify(notification_ID, notification);		 
	 }
	 Handler handler = new Handler() {
		 
	  	  public void handleMessage(Message message) {
	  	    switch (message.what) {
	  	   
	  	case 0:  
	  	    if(response.equals(MSGs.MSG_DONE)){  
	  	    		Toast.makeText(c, c.getString(R.string.str_uploaded_success), Toast.LENGTH_SHORT).show();
	  	    		hideNotification(notification_ID);
	  	    		showLoadingLitinigDetalis() ;
	  	    		
	  	    }else if(response.length()==5 && response.charAt(0)=='E'){ 
	  	    		MSGs.showMessage(c, response);	  	    	
	  	    	} 
		    break;  
	  	case 1:
	  		hideNotification(notification_Lisiting);
	  		showGoToListinig();
	  		break;
		 
		    }
	  	  }

		
	  	};
	  	private void showGoToListinig() {
			String ns = Context.NOTIFICATION_SERVICE;
			 NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(ns);
			 
			 int icon = R.drawable.ic_launcher;
			 CharSequence tickerText = "اضغط لعرض المكان المضاف";
			 long when = System.currentTimeMillis();
			 
			 Notification notification = new Notification(icon, tickerText, when);
			 
			 RemoteViews contentView = new RemoteViews(c.getPackageName(), R.layout.open_litinig_notificationbar);  
			 
			 notification.contentView = contentView;
			 notification.defaults |= Notification.DEFAULT_VIBRATE;
			 notification.flags |=Notification.FLAG_AUTO_CANCEL;
			 
			 Intent notificationIntent = new Intent(c, Act_ItemDetalis.class);
			 notificationIntent.putExtra("item", response) ;
			 notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			 PendingIntent contentIntent = PendingIntent.getActivity(c, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			 notification.contentIntent = contentIntent;
			 
			  
			 
			 
			 mNotificationManager.notify(notification_goto, notification);
			
		}
	  	String response;
			@Override
			public void done(int code, String response) {
				if(code==ConnectionController.Con_UploadImage){
			  	    this.response=response;
			  	    handler.sendEmptyMessage(0);
			  	    	
				}else if(code==ConnectionController.Con_LisitingDetalis){
					this.response=response;
			  	    handler.sendEmptyMessage(1);
				}
				
			} 
}
