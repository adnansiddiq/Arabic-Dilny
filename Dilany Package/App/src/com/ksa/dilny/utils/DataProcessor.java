package com.ksa.dilny.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.ksa.dilny.DataModel.CatsModel;
import com.ksa.dilny.DataModel.LevelModel;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.DataModel.MSGs;
import com.yahia.libs.InternetConnections.UTFEncoder;


public class DataProcessor {

	public static Boolean mailValidator(String mail){
		Pattern pattern= Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+");
	    return pattern.matcher(mail).matches();
	}
	
	public static Boolean lengthValidator(String name){
		if(name.length()>=5 && name.length()<=40){
			return true;
		}else {
			return false;
		} 
	}
	
	public static Boolean priceValidator(String max,String min){
		if(max !=null && min!=null){
			if(Integer.parseInt(max)>=Integer.parseInt(min)){
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
		
	}
	public static Boolean addNewValidator(Context c, String user_id,String title,String desc,String keywords,String geoname,String location,
			String address,String lat,String lng,String mapIconFile, String max,String min,String phone){
		 if(priceValidator(max, min)){
			 if(user_id.length()>0 && title.length()>0 && desc.length()>0 && keywords.length()>0 && geoname.length()>0 && location.length()>0 && address.length()>0 && lat.length()>0 && lng.length()>0 && mapIconFile.length()>0 && phone.length()>0 ){
				 return true;
			 }else {
				 Toast.makeText(c, MSGs.MSG_ERROR_FILED_EMPTY, Toast.LENGTH_SHORT).show();
				 return false;
			 }
		 }else {
			 Toast.makeText(c, MSGs.MSG_ERROR_PRICE_IS_NO_VALID, Toast.LENGTH_SHORT).show();
			 return false;
		 }
		  
	}
	
	public static Boolean isNumeric(String number){
		  if (number.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {  
	           return true;
	        } else {  
	        	return false;
	        }  
	}
	 
	
	public static String passwordMd5(String password){
    	MessageDigest md=null;
    	byte[] bytesOfMessage =null;
		try {
	    	bytesOfMessage = password.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		md.update(password.getBytes(), 0, password.length());
		String md5 = new BigInteger(1, md.digest()).toString(16); // Encrypted 
				
    	 return md5;
    }
	
	public static String getConnectionLink_SiginIN(Context c,String mail,String password){
    	password=DataProcessor.passwordMd5(password);
    	password=new UTFEncoder().encod(password);
    	
    	if(DataProcessor.mailValidator(mail)){
    		String link="http://dilny.com/api/siginIn/mail/"+mail+"/password/"+password;
    		return link;
    		
    	}else {
    		Toast.makeText(c, MSGs.MSG_ERROR_MAIL_IS_NOT_VALID, Toast.LENGTH_SHORT).show();
    		return null;
    	}
    	
    }
	
	public static String getConnectionLink_SiginUP(Context c,String mail,String password,String name){
    	//password=DataProcessor.passwordMd5(password);
		UTFEncoder encode=new UTFEncoder();
    	if(mailValidator(mail)){
    		mail=encode.encod(mail);
    		if(lengthValidator(password)){
    			password=encode.encod(password);
    			if(lengthValidator(name)){
    				name=encode.encod(name);
    				String link="http://dilny.com/api/siginUp/mail/"+mail+"/password/"+password+"/login/"+name;
    	    		return link;
    			}else{
    				Toast.makeText(c, MSGs.MSG_LOGIN_IS_TOO_LONG, Toast.LENGTH_SHORT).show();
    	    		return null;
    			}
    		}else {
    			Toast.makeText(c, MSGs.MSG_PASSWORD_IS_TOO_LONG, Toast.LENGTH_SHORT).show();
        		return null;
    		}
    	}else {
    		Toast.makeText(c, MSGs.MSG_ERROR_MAIL_IS_NOT_VALID, Toast.LENGTH_SHORT).show();
    		return null;
    	}
	} 
	public static String getConnectionLink_UploadImage( String lisiting_id){
    	 String link="http://dilny.com/api/uploadImage/user/"+DataHolder.User_ID+"/listing_id/"+lisiting_id+"/image/";
    	 return link;
    }
	static URI uri;
	public static String getConnectionLink_ِAddNew(Context c,String user_id,String level_id,String title,String desc,String keywords,String geoname,String location,
			String address,String lat,String lng,String mapIconFile,String cat,String max,String min,String phone){ 

		if(addNewValidator(c, user_id, title, desc, keywords, geoname, location, address, lat, lng, mapIconFile,  max, min, phone)){
			UTFEncoder encode=new UTFEncoder();
			 /*
			title.replace("\n", " ");
			desc.replace("\n", " ");
			 
			title=encode.encod(title);
			desc=encode.encod(desc);
			keywords=encode.encod(keywords);
			geoname=encode.encod(geoname);
			location=encode.encod(location);
			address=encode.encod(address);
			
			
			String link="http://dilny.com/api/addListing/user/"+user_id+"/level_id/"+level_id+"/title/"+title+"/desc/"+desc+"/keywords/"+keywords+"/geoname/"+geoname+"/location/"
					+location+"/address/"+address+"/lat/"+lat+"/lng/"+lng+"/mapIconFile/"+mapIconFile+"/cats/"+cat+"/price/"+min+","+max+"/phone/"+phone;
			 */
			
			try {
				//link = "http://dilny.com/api/addReview/user/"+DataHolder.User_ID+"/listing/"+id+"/review/"+URLEncoder.encode(reviewString,"UTF-8");
				uri = new URI("http", null, "www.dilny.com", 80,"/api/addListing/user/"+user_id+"/level_id/"+level_id+"/title/"+title+"/desc/"+desc+"/keywords/"+keywords+"/geoname/"+geoname+"/location/"
						+location+"/address/"+address+"/lat/"+lat+"/lng/"+lng+"/mapIconFile/"+mapIconFile+"/cats/"+cat+"/price/"+min+","+max+"/phone/"+phone,null, null);


			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*String link=uri.toString();
			link.replace("A", "%20");
			link.replace("%0", "%20");*/
			return uri.toASCIIString().replace("%0A", "%20");
		}else {
			return null;
		}
		
		
		 
	} 
 
   
 
	public static ArrayList<ListItemModel> getListinigsListFromResponse(String response) {
		System.out.println("response : "+response);
		JSONArray jsonArray; 
		ArrayList<ListItemModel> list=new ArrayList<ListItemModel>();
		try {
			jsonArray = new JSONArray(response.trim());
			for(int x=0;x<jsonArray.length();x++){
				JSONObject jsonObject = jsonArray.getJSONObject(x);
				ListItemModel item=new ListItemModel();
				item.setId(jsonObject.getString(Consts.LITINIG_ID));
				item.setTitle(jsonObject.getString(Consts.LITINIG_TITLE));
				item.setDesc(jsonObject.getString(Consts.LITINIG_DESC));
				item.setDesc((item.getDesc()).replace("<p>", "\n"));
				item.setDesc((item.getDesc()).replace("<p/>", ""));
				
				item.setAddress_line_1(jsonObject.getString(Consts.LITINIG_ADRESS1));
				item.setAddress_line_2(jsonObject.getString(Consts.LITINIG_ADRESS2));
				/*item.setAddress_line_1((item.getAddress_line_1()).replace("untranslated", ""));
				item.setAddress_line_2((item.getAddress_line_2()).replace("untranslated", ""));*/
				
				item.setThumb(jsonObject.getString(Consts.LITINIG_THUMB));
				item.setImage(jsonObject.getString(Consts.LITINIG_IMAGE));
		 

				item.setCountry(jsonObject.getString(Consts.COUNTRY)); 
				
				if(jsonObject.getString(Consts.LITINIG_RATINGS)==null){
					item.setRatings("0.0");
				}else {
					item.setRatings(jsonObject.getString(Consts.LITINIG_RATINGS));
				}
				if(jsonObject.getString(Consts.LITINIG_DISTANCE)==null){
					item.setDistance("0.0");
				}else {
					item.setDistance(jsonObject.getString(Consts.LITINIG_DISTANCE));
				}
				//item.setPrice( jsonObject.getJSONArray(Consts.LITINIG_PRICE));
				if(jsonObject.getString(Consts.LITINIG_LAT)==null){
					item.setlat("0.0");
				}else {
					item.setlat(jsonObject.getString(Consts.LITINIG_LAT));
				}
				
				if(jsonObject.getString(Consts.LITINIG_LNG)==null){
					item.setlng("0.0");
				}else {
					item.setlng(jsonObject.getString(Consts.LITINIG_LNG));
				}				
				if(jsonObject.getString(Consts.LITINIG_PHONE)==null){
					item.setPhone("");
				}else {
					item.setPhone(jsonObject.getString(Consts.LITINIG_PHONE));
				}	
				if(jsonObject.getString(Consts.LITINIG_CAT_ID)==null){
					item.setCat_id("0");
				}else {
					item.setCat_id(jsonObject.getString(Consts.LITINIG_CAT_ID));
				}
				item.setReviewsHtml(jsonObject.getString(Consts.LITINIG_REVIEW));
				item.setJsonItem(jsonObject.toString());
				
				
				try{
					item.setPoint(new GeoPoint((int)((Double.parseDouble(item.getlat()))*1000000), 
			        	     (int)((Double.parseDouble(item.getlng()))*1000000)));
				}catch (Exception e) {
					item.setPoint(new GeoPoint(0,0));
				}
				////System.out.println(item.getJsonItem());
				list.add(item);  
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static ListItemModel  getListingFromResponse(String response) {
		 
		ListItemModel item=null;
 		try { 
 			JSONObject jsonObject = new JSONObject(response);
			item=new ListItemModel();
			item.setId(jsonObject.getString(Consts.LITINIG_ID));
			item.setTitle(jsonObject.getString(Consts.LITINIG_TITLE));
			item.setThumb(jsonObject.getString(Consts.LITINIG_THUMB));
			item.setDesc(jsonObject.getString(Consts.LITINIG_DESC));
			item.setDesc((item.getDesc()).replace("<p>", "\n"));
			item.setDesc((item.getDesc()).replace("</p>", "\n"));
			item.setDesc((item.getDesc()).replace("<p/>", "\n"));
			item.setAddress_line_1(jsonObject.getString(Consts.LITINIG_ADRESS1));
			item.setAddress_line_2(jsonObject.getString(Consts.LITINIG_ADRESS2));
			item.setImage(jsonObject.getString(Consts.LITINIG_IMAGE));
			item.setCountry(jsonObject.getString(Consts.COUNTRY));
			//item.setPrice( jsonObject.getJSONArray(Consts.LITINIG_PRICE));
			item.setReviewsHtml(jsonObject.getString(Consts.LITINIG_REVIEW));
			if(jsonObject.getString(Consts.LITINIG_RATINGS).equals("null")){
				item.setRatings("0.0");
			}else {
				item.setRatings(jsonObject.getString(Consts.LITINIG_RATINGS));
			}
			if(jsonObject.getString(Consts.LITINIG_DISTANCE).equals("null")){
				item.setDistance("0.0");
			}else {
				item.setDistance(jsonObject.getString(Consts.LITINIG_DISTANCE));
			}
			//item.setPrice( jsonObject.getJSONArray(Consts.LITINIG_PRICE));
			if(jsonObject.getString(Consts.LITINIG_LAT).equals("null")){
				item.setlat("0.0");
			}else {
				item.setlat(jsonObject.getString(Consts.LITINIG_LAT));
			}
			
			if(jsonObject.getString(Consts.LITINIG_LNG).equals("null")){
				item.setlng("0.0");
			}else {
				item.setlng(jsonObject.getString(Consts.LITINIG_LNG));
			}				
			if(jsonObject.getString(Consts.LITINIG_PHONE).equals("null")){
				item.setPhone("");
			}else {
				item.setPhone(jsonObject.getString(Consts.LITINIG_PHONE));
			}	
			if(jsonObject.getString(Consts.LITINIG_CAT_ID).equals("null")){
				item.setCat_id("0");
			}else {
				item.setCat_id(jsonObject.getString(Consts.LITINIG_CAT_ID));
			}
			item.setJsonItem(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}
	
	public static ArrayList<CatsModel> getCatsFromResponse(String response){
		ArrayList<CatsModel> catsList =new ArrayList<CatsModel> ();
		JSONArray catsArray;
		try {
			catsArray = new JSONArray(response);
			//names=new String [catsArray.length()];
			 
			for(int x=0;x<catsArray.length();x++){
				JSONObject jsonObject = catsArray.getJSONObject(x);
				//System.out.println("jsonObject : "+jsonObject);
				CatsModel cat=new CatsModel(new JSONObject());
				cat.setName(jsonObject.getString("name") );
				cat.setId( jsonObject.getString("id" ) );
				cat.setIcon( jsonObject.getString("icon" ) );
				cat.setImage( jsonObject.getString("image" ) );
				//System.out.println("cat.getId() : "+cat.getId());
				//System.out.println("cat.getName() : "+cat.getName());
				catsList.add(cat);
				//names[x]=cat.getName();
			}
						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return catsList;
	}
	
	
	public static ArrayList<LevelModel> getLevelsFromResponse(String response){
		ArrayList<LevelModel> levList =new ArrayList<LevelModel> ();
		JSONArray catsArray;
		try {
			catsArray = new JSONArray(response); 
			 
			for(int x=0;x<catsArray.length();x++){
				JSONObject jsonObject = catsArray.getJSONObject(x);
				//System.out.println("jsonObject : "+jsonObject);
				LevelModel lev=new LevelModel(jsonObject); 
				levList.add(lev); 
			}
						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return levList;
	}
}
