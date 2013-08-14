package com.yahia.libs.InternetConnections;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

public class ConvertBitmapToBase64 {
	public static String BitmapTOBase64(Bitmap image,int width,int height,int compressionRatio,CompressFormat format){
		image = Bitmap.createScaledBitmap(image, width, height, true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(format, compressionRatio, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);
		return image_str; 
	}
}
