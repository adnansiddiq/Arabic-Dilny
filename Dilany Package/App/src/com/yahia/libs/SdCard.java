package com.yahia.libs;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class SdCard {
	/**
     * saveToSd
     *<p>
     * @param image  Bitmap image that will be saved to sd
     *<p>
     *@param dir  full path string to the directory taht will hold the image
     *<p>
     *@param fileName  String file name 
     *<p>
     * @return void
     */
	public static Boolean saveToSd(Bitmap image,String dir,String fileName) {
		 //Log.d("SdCard", "dir : "+dir);
		 //Log.d("SdCard", "fileName : "+fileName);
		if(checkExternalStorageState()){ 
			File mainDir = new File(dir);
			if(!mainDir.exists()){
				mainDir.mkdirs(); 
			}
			//Next create a file, the example below will save to the SDCARD using JPEG format 
			File file = new File(dir+fileName);
			try {
				image.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
			} catch (Exception e) {			
				e.printStackTrace();
				 //Log.d("SdCard", "FileNotFoundException : "+e.getMessage());
				return false;
			}
			 //Log.d("SdCard", "Image Saved");
			return true;	
	  }else {
		  //Log.d("SdCard", "External Storage is not avaliable");
		  return false;
	  }
	}
	
	public static Boolean checkExternalStorageState() {
		 boolean mExternalStorageAvailable = false;
		 boolean mExternalStorageWriteable = false;
		    String state = Environment.getExternalStorageState();
		    if (Environment.MEDIA_MOUNTED.equals(state)) {
		        mExternalStorageAvailable = mExternalStorageWriteable = true;
		    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		        mExternalStorageAvailable = true;
		        mExternalStorageWriteable = false;
		    } else {
		        mExternalStorageAvailable = mExternalStorageWriteable = false;
		    }
		     
		    return (mExternalStorageAvailable && mExternalStorageWriteable);
		}
}
