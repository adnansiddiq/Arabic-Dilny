package com.yahia.libs.InternetConnections;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
 
public  class   ConnectionController2 { 
	
	
	 
	int connectionType; 
	ProgressDialog dialog; 
	Boolean showLoading;
	
	onConnectionController listener;
	
	 /** 
	  * ConnectionController 
	  *<p>
	  * @param context Applicaiton Context
	  * <p>
	  * @param connectionType Connection int type
	  * <p>
	  * Before start Connection you have to setOnConnectionDone(onConnectionController listen)
	  * <p>
	  * To start Connection you have to use : startConnectoin(String _url)
	  * <p>
	  * Also you can use : setLoadingDialogCancelableStatus(Boolean status) <br>  setLoadingDialogMSG(String msg) <br> setShowLoadingDialog(Boolean status)
	  */
	public ConnectionController2(Context context,int connectionType){		 
		this.connectionType=connectionType;
		dialog=new ProgressDialog(context);			
	}
	 
	public void startConnectoin(String _url){
		new HttpConnection(handler).get(_url);
	}
	
	 
	public void setOnConnectionDone(onConnectionController listen) {
		listener = listen;
    }
	public void setLoadingDialogCancelableStatus(Boolean status){
		dialog.setCancelable(status);
	}
	public void setLoadingDialogMSG(String msg){
		dialog.setMessage(msg);
	}
	public void setShowLoadingDialog(Boolean status){
		showLoading=status;
	}
 	
 	 
	Handler handler = new Handler() {
	  	  public void handleMessage(Message message) {	  		
	  	    switch (message.what) {
		  	    case HttpConnection.DID_START:
		  	    	dialogHandler.sendEmptyMessage(1);
		  	    	listener.onConnectionStarted(connectionType, null);		  	    	
		  	    break;
		  	    case HttpConnection.DID_SUCCEED:	
		  	    	dialogHandler.sendEmptyMessage(0);
		  	  	String response = (String) message.obj;	
		  	   	listener.onConnectionDone(connectionType, response);
			  	break;
		  	    case HttpConnection.DID_ERROR:
		  	    	dialogHandler.sendEmptyMessage(0);
		  	   	listener.onConnectionError(connectionType, null);
		  	    break;
	  	    }
	  	  }
	};
	  	  
	  	
	Handler dialogHandler = new Handler() {
	  	  public void handleMessage(Message message) {	  		
	  	    switch (message.what) {
	  	    case 0:
	  	    		if(showLoading){
	  	    			dialog.dismiss();
	  	    		}   	    	
	  	    	
	  	    	break;
	  	    case 1:
	  	    		if(showLoading){
	  	    			dialog.show();
	  	    		}	  	    		  	    	
	  	    	break;
	  	    
	  	    }
	  	  }
	};

	
	  	
	 
	  	 

}
