package com.yahia.libs.CustomViews.ImageFromUrl;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ksa.dilny.R;
import com.yahia.libs.SdCard;

public class UIImage extends RelativeLayout{

	private ImageFileModel image;
	
	public ProgressBar pb;
	ImageView im; 
	Context c;
	String SD_PATH;
	ImDBHandler dbHandler;
    
	public UIImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		c=context; 
		dbHandler=new ImDBHandler(c);
		
		LayoutInflater.from(context).inflate(R.layout.image_from_url, this, true);
		
		 pb=(ProgressBar)findViewById(R.id.image_from_url_progressBar);
		 im=(ImageView)findViewById(R.id.image_from_url_imageView);
		 SD_PATH=Environment.getExternalStorageDirectory().toString()+"/."+c.getApplicationInfo().packageName+"/"; 
		 
 
	}
	
	 private void loadImage() {
		 if(!checkImage()){
			 Thread r=new Thread(new Runnable() {
					
					@Override
					public void run() {
						try{
						    String url1 = image.getUrl();
						     
						    
						    URL ulrn = new URL(url1);
						    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
						    InputStream is = con.getInputStream();
						    Bitmap bmp = BitmapFactory.decodeStream(is);
						    if (null != bmp)	{	
						    	image.setBitmap(bmp);
						    	handler.sendEmptyMessage(0);
						    }
								
						    else
						        Log.d("UIImage","The Bitmap is NULL");

						    }catch(Exception e){
						    	//Log.d("UIImage","Exception : "+e.getMessage());
						    }
						
					}
				});
				r.start();
		 }else {
			 handler.sendEmptyMessage(1);
		 }
		 
	}
 
	
	 private boolean checkImage() {
		 //Log.d("UIImage", "checkImage() ------- Start");
		 if(SdCard.checkExternalStorageState()){
			 ImageFileModel imag=dbHandler.GetImageFileFromDbWithUrl(image.getUrl());
			 if(imag !=null){ 
				 //Log.d("UIImage", "imag !=null : true");
				 if(imag.getBitmap()!=null){
					 //Log.d("UIImage", "imag.getBitmap()!=null : true");
					 image=imag;
					 //Log.d("UIImage", "checkImage() ------- finish : true");
					 return true;
				 }
			 }
		 }
		 //Log.d("UIImage", "checkImage() ------- finish : false");
		return false;
	}

	
	 
	 private void saveTosd(Bitmap image,String dir,String fileName){
		 	dir=SD_PATH+dir+"/";
		 	SdCard.saveToSd(image, dir, fileName);
	 }
	 
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	 	 
		if(image !=null){
			if(image.getUrl()!=null){
				 loadImage();
			 }
		}
		
		
	}
	
	public void refreshImage(){
		if(image !=null){
			if(image.getUrl()!=null){
				 loadImage();
			 }
		}
	}
	 
	 Handler handler = new Handler() {
	  	  public void handleMessage(Message message ) {
	  		//Log.d("UIImage","message.what : "+message.what);
	  	    switch (message.what) {
	  	    case 0:  	
	  	    	
	  	    	
	  	    	String [] urls=image.getUrl().split("/");
	  	    	String fileName=urls[urls.length-1];
	  	    	String dir="."+urls[urls.length-2];
	  	    	
	  	    	saveTosd(image.getBitmap(),dir, fileName);
	  	    	
	  	    	image.setPathOnSD(SD_PATH+dir+"/"+fileName);
	  	    	
	  	    	dbHandler.insertImages(getContext(), image);
	  	    	  
	  	    	im.setImageBitmap(image.getBitmap());
	  	    	pb.setVisibility(ProgressBar.GONE);
	  	    	im.invalidate();
	  	    	break;
	  	    case 1:
	  	    	im.setImageBitmap(image.getBitmap());
	  	    	pb.setVisibility(ProgressBar.GONE);
	  	    	break;

	  	    }
	  	  }
	 };
	 
	 
	public ImageFileModel getImage() {
		return image;
	}
  
	public void setImage(ImageFileModel image) {
		this.image = image;
	}
	
	public void setImageBitmap(Bitmap b){
		im.setImageBitmap(b);
	}
	
	
}
