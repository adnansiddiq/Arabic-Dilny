package com.yahia.libs.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

public class ExportDB {
	/** 
	  * extract Used To extract db to a file on sd
	  *<p>
	  * @param folderNameOnSd folder name that will be created on sd to hold the db file
	  * <p>
	  * @param fileNameOnSd file name that will hold thd db 
	  * <p>
	  * @param fileNameOnSd the path of db like : </p>"/data/data/com.ksa.dilny/databases/dilny.db"
	  * <p> 
	  *  
	  */
	public static void extract(String folderNameOnSd,String fileNameOnSd , String dbPath){
		 InputStream myInput;
		 
			try {
				Log.d("ExportDB", "( "+folderNameOnSd+" , "+fileNameOnSd+" , "+dbPath  +" )");
				//myInput = new FileInputStream("/data/data/com.ksa.dilny/databases/dilny.db");//this is
				myInput = new FileInputStream(dbPath);//this is
	// the path for all apps
	//insert your package instead packagename,ex:com.mybusiness.myapp
	 
	 
			    // Set the output folder on the SDcard
			    //File directory = new File("/sdcard/some_folder");
				File directory = new File(Environment.getExternalStorageDirectory().toString()+"/"+folderNameOnSd);
			    // Create the folder if it doesn't exist:
			    if (!directory.exists()) 
			    {
			        directory.mkdirs();
			    } 
			    // Set the output file stream up:
	 
			    //OutputStream myOutput = new FileOutputStream(directory.getPath()+"/database_name.backup");
			    OutputStream myOutput = new FileOutputStream(directory.getPath()+"/"+fileNameOnSd);
	 
	 
			    
			    // Transfer bytes from the input file to the output file
			    byte[] buffer = new byte[1024];
			    int length;
			    while ((length = myInput.read(buffer))>0)
			    {
			        myOutput.write(buffer, 0, length);
			    }
			    // Close and clear the streams
	 
	 
	 
	 
	 
			    myOutput.flush();
	 
			    myOutput.close();
	 
			    myInput.close(); 
			} catch (FileNotFoundException e) {
				Log.d("ExportDB","Backup Unsuccesfull!");
				Log.d("ExportDB",e.getMessage());
	 
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("ExportDB","Backup Unsuccesfull!");
				Log.d("ExportDB",e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("ExportDB","Done Succesfully :)");
			 
	}
	
	 
}
