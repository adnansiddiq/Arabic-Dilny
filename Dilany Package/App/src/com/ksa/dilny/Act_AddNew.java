package com.ksa.dilny;

import java.io.File;
import java.util.ArrayList;

import libs.ImageUploader;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.Window;
import com.ksa.dilny.DataModel.CatsModel;
import com.ksa.dilny.DataModel.LevelModel;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.ksa.dilny.utils.DataProcessor;
import com.yahia.libs.ImageFactory;
import com.yahia.libs.SdCard;
import com.yahia.libs.Geo.GeoLocationService;
import com.yahia.libs.Geo.Coder.GoogleGeoAddressModel;

public class Act_AddNew extends SherlockActivity implements OnMenuItemClickListener ,onConnectionDoneCC {
	private final int CAMERA_PICTURE = 1;
    private final int GALLERY_PICTURE = 2;
    private final int MAP_INTENT = 3;
    
	EditText edit_title;
	EditText edit_desc;
	EditText edit_tags;
	//EditText edit_country;
	//EditText edit_city;
	EditText edit_adress;
	EditText edit_lat;
	EditText edit_lng; 
	Spinner spinner_cats;
	Spinner spinner_levels;
	EditText edit_price_max;
	EditText edit_price_min;
	EditText edit_phone;
	Button btn_image_galery;
	Button btn_image_camera;
	ImageView imageView;
	Button btn_open_map;
	Button btn_current_cord;
	
	ProgressDialog dialog; 

	ArrayList<CatsModel> catsList;
	ArrayList<LevelModel> levsList;
	Bitmap sourceImage;
	
	Context c;
	
	int catPosition=0;
	int levelPosition=0;
	
	String capturedImagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme( R.style.Theme_Sherlock_Light_DarkActionBar);
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_PROGRESS);
	    setContentView(R.layout.act_add_new);
	    c=this;
	    
	    edit_title=(EditText)findViewById(R.id.new_Listing_edit_title);
	    edit_desc=(EditText)findViewById(R.id.new_Listing_edit_desc);
	    edit_tags=(EditText)findViewById(R.id.new_Listing_edit_tags);
	    /*edit_country=(EditText)findViewById(R.id.new_Listing_edit_country);
	    edit_city=(EditText)findViewById(R.id.new_Listing_edit_city);*/
	    edit_adress=(EditText)findViewById(R.id.new_Listing_edit_adress);
	    edit_lat=(EditText)findViewById(R.id.new_Listing_edit_lat);
	    edit_lng=(EditText)findViewById(R.id.new_Listing_edit_lng);
	    spinner_cats=(Spinner)findViewById(R.id.new_Listing_spinner_cats);
	    spinner_levels=(Spinner)findViewById(R.id.new_Listing_spinner_levels);
	    edit_price_max=(EditText)findViewById(R.id.new_Listing_edit_price_max);
	    edit_price_min=(EditText)findViewById(R.id.new_Listing_edit_price_min);
	    edit_phone=(EditText)findViewById(R.id.new_Listing_edit_phone);
	    btn_image_galery=(Button)findViewById(R.id.new_Listing_btn_image_From_Galary);
	    btn_image_camera=(Button)findViewById(R.id.new_Listing_btn_image_From_Camera);
	    imageView=(ImageView)findViewById(R.id.new_Listing_image_view);
	    btn_open_map=(Button)findViewById(R.id.new_btn_open_map);
		btn_current_cord=(Button)findViewById(R.id.new_btn_current_cord);
	     
		// Spinners Focus
		spinner_cats.setFocusable(true); 
		spinner_cats.setFocusableInTouchMode(true);
		spinner_cats.requestFocus();
		
		spinner_levels.setFocusable(true); 
		spinner_levels.setFocusableInTouchMode(true);
		spinner_levels.requestFocus(); 
	    
	   /* edit_desc.addTextChangedListener(inputTextWatcher);
	    edit_desc.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.d("Act_AddNew","actionId : " +keyCode );
				Log.d("Act_AddNew","event : " +event );
				  // If the event is a key-down event on the "enter" button
		        if (keyCode == KeyEvent.KEYCODE_ENTER  ) {
		          // Perform action on key press
		          Toast.makeText(Act_AddNew.this, "هذا الحقل لا يدعم إضافة سطر جديد", Toast.LENGTH_SHORT).show();
		          return true;
		        }
				return false; 
			}
		}); */
	    
	    dialog = new ProgressDialog(this);
	    		
	    fillLevelsSpinner();
	    
	    
	    btn_image_galery.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setType("image/*");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        startActivityForResult(Intent.createChooser(intent,
		                "Select Picture"), GALLERY_PICTURE);				
			}
		});
	    
	    btn_image_camera.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(SdCard.checkExternalStorageState()){
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
					capturedImagePath=DilnyActivity.tmpImageDir+ String.valueOf(System.currentTimeMillis()) + ".png";
					File file = new File(capturedImagePath); 			 		 
					Uri outputFileUri = Uri.fromFile(file); 
					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri); 
					 DataHolder.saveImageUri(c, capturedImagePath); 
					 System.out.println("capturedImagePath : "+capturedImagePath);
					startActivityForResult(intent, CAMERA_PICTURE);  
				}else{
					Toast.makeText(c, "عفواً , الرجاء التأكد من وجود الذاكرة الخارجية", Toast.LENGTH_SHORT).show();
				}
				
				/*
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		        startActivityForResult(Intent.createChooser(intent,
		                "Select Picture"), CAMERA_PICTURE);*/				
			}
		});
	    
	    btn_open_map.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent n=new Intent(Act_AddNew.this,Act_AddFromMap.class); 
		        startActivityForResult( n, MAP_INTENT);	 				
			}
		});
	    
	    btn_current_cord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getCurrentLocation();
				
			}
		});
	    
	    edit_lat.setText(GeoLocationService.lat+"");
	    edit_lng.setText(GeoLocationService.lng+"");
	    
	 
	    
	    
	    spinner_levels.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) { 
				levelPosition=position;
				LevelModel lev=levsList.get(position);
				catsList=DataProcessor.getCatsFromResponse(lev.getCatsJson());
				fillCatsSpinner();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				 
				
			}
		});
	    spinner_cats.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) { 
				catPosition=position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				 
				
			}
		});
	}
	private void fillLevelsSpinner() {
		levsList=DataHolder.getLevelsFromDB(this);
		String [] names=new String [levsList.size()];
		for(int x=0;x<levsList.size();x++){
			LevelModel cat=levsList.get(x);
			names[x]=cat.getName();
		}
		ArrayAdapter<CharSequence>  spinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, names );   	
		spinner_levels.setSelection(0);
	    spinner_levels.setAdapter(spinnerAdapter);
		
	}
	private void fillCatsSpinner() { 
		String [] names=new String [catsList.size()];
		for(int x=0;x<catsList.size();x++){
			CatsModel cat=catsList.get(x);
			names[x]=cat.getName();
		}
		ArrayAdapter<CharSequence>  spinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, names );   	
		spinner_cats.setSelection(0);
	    spinner_cats.setAdapter(spinnerAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub  
		 
		menu.add("Location")
        .setIcon( R.drawable.ic_location)
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		menu.add("Done")
        //.setIcon( R.drawable.ic_done)
        .setTitle("إضافة")
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		 
		   
		return super.onCreateOptionsMenu(menu);
	}

	 
 
  	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if(item.getTitle() =="إضافة"){ 
			
			String link=getDataFromFields() ;
			if(link  !=null){
				 
				System.out.println("Link = "+link);
				ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_AddNew,true,getString(R.string.str_msg_waitting_for_adding_new));
				cc.setOnConnectionDone(this);
				cc.startGetConnection();
			}
		 }else if(item.getTitle() =="Location"){ 
			 getCurrentLocation();
		 } 
		return false;
	}
	String str_country;
	String str_city;
	String str_address;
	private void getCurrentLocation() {
		dialogHandler.sendEmptyMessage(1);
		new Runnable() {
			
			@Override
			public void run() {
				str_country=GeoLocationService.Country;
				str_city=GeoLocationService.City;
				str_address=GeoLocationService.Adress.toString();
				
				edit_adress.setText(str_country+", "+str_city+", "+GeoLocationService.Adress);
				//edit_country.setText(GeoLocationService.Country);
				//edit_city.setText(GeoLocationService.City); 
			    edit_lat.setText(GeoLocationService.lat+"");
			    edit_lng.setText(GeoLocationService.lng+"");
				edit_lat.setText(GeoLocationService.lat+"");
				edit_lng.setText(GeoLocationService.lng+"");
				if(GeoLocationService.Adress != null || GeoLocationService.Country!=null || GeoLocationService.City!=null || GeoLocationService.lat!=0.0 || GeoLocationService.lng!=0.0){
					dialogHandler.sendEmptyMessage(0);
				}else {
					dialogHandler.postDelayed(this, 50);			
				}
							 
				
			}
		}.run();
		
	 
		
	}
	Handler dialogHandler = new Handler() {
		
	  	  public void handleMessage(Message message) {	  		
	  	    switch (message.what) {
	  	    case 0:
	  	    	dialog.dismiss();
	  	    	Toast.makeText(Act_AddNew.this,Act_AddNew.this.getString(R.string.str_location_success), Toast.LENGTH_SHORT).show();
	  	    	break;
	  	    case 1:
	  	    	dialog.setMessage(Act_AddNew.this.getString(R.string.str_location_loading));
	  	    	dialog.show();
	  	    	break;
	  	    
	  	    }
	  	  }
	};
	private String getDataFromFields() {
		String user_id=DataHolder.User_ID;
		String title=edit_title.getText().toString();
		String desc=(edit_desc.getText().toString()).replace("\n","<p>");
		String keywords=edit_tags.getText().toString();
		
		str_country=GeoLocationService.Country;
		str_city=GeoLocationService.City;
		str_address=GeoLocationService.Adress.toString();
		
		String geoname=str_country;
		String location=str_city;
		/*
		String geoname=edit_country.getText().toString();
		String location=edit_city.getText().toString();*/
		String address=edit_adress.getText().toString();
		String lat=edit_lat.getText().toString();
		String lng=edit_lng.getText().toString();
		

		String max=edit_price_max.getText().toString();
		String min=edit_price_min.getText().toString();
		String phone=edit_phone.getText().toString();
		
		int selectedCatPosition=spinner_cats.getSelectedItemPosition();
		CatsModel  catM= catsList.get(selectedCatPosition);
		String mapIconFile=catM.getIcon(); 
		
		LevelModel lev=levsList.get(levelPosition);
		CatsModel cat=catsList.get(catPosition);
		
		System.out.println("user_id : "+user_id);
		System.out.println("lev.getId() : "+lev.getId());
		System.out.println("title : "+title);
		System.out.println("title : "+title);
		System.out.println("keywords : "+keywords);
		System.out.println("geoname : "+geoname);
		System.out.println("location : "+location);
		System.out.println("address : "+address);
		System.out.println("lat : "+lat);
		System.out.println("lat : "+lat);
		System.out.println("lat : "+lat);
		System.out.println(" cat.getId() : "+ cat.getId());
		System.out.println("max : "+max);
		System.out.println("min : "+min);
		System.out.println("phone : "+phone);
		
		String link =DataProcessor.getConnectionLink_ِAddNew(this, user_id,lev.getId(), title, desc, keywords, geoname, location,
				 address, lat, lng, mapIconFile, cat.getId(), max, min, phone);
		
		return link;
		
	}
	 String imageFilePath;
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		 if (requestCode == CAMERA_PICTURE) {  
		       /*if(data !=null){
		    	   Bitmap b = (Bitmap) data.getExtras().get("data");  
		    	   sourceImage=b;
		    	   //b = Bitmap.createScaledBitmap(b, b.getWidth()/15, b.getHeight()/15, true); 
		    	   imageView.setImageBitmap(b);
		       }*/
			 //sourceImage = BitmapFactory.decodeFile(DataHolder.loadImageUri(c));
			 Log.d("sourceImage",DataHolder.loadImageUri(c));
			 sourceImage = ImageFactory.getImage(DataHolder.loadImageUri(c),0,0);
			 
			 if(sourceImage !=null){
				 Log.d("sourceImage !=null : ","sourceImage !=null");
				 imageFilePath=DataHolder.loadImageUri(c);
				 setImage( );
			 }else{
				 Log.d("sourceImage ==null : ","sourceImage ==null");
				 Toast toast = Toast.makeText(this, this.getString(R.string.str_no_image_selected), Toast.LENGTH_SHORT);
	             toast.show();
			 }
		   }  else 		 if (data !=null) { 
			 try{
				  if (requestCode == GALLERY_PICTURE) {  
					   if (data !=null) {
				        
						try {
							Uri uri = data.getData();
				              // User had pick an image.
				              Cursor cursor = getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
				              cursor.moveToFirst();
				              // Link to the image
				                 imageFilePath = cursor.getString(0);
				             
				              System.out.println("imageFilePath : "+imageFilePath); 
				              Bitmap b;
				              //BitmapFactory.decodeFile(imageFilePath);
				              sourceImage = ImageFactory.getImage(imageFilePath,0,0);
				              setImage( );
				              cursor.close();
				              System.gc();
							    Runtime.getRuntime().gc();
						} catch (Exception e) {
							Toast.makeText(this, "عفواً , حدث خطأ في جلب الصورة الرجاء المحاولة مرة آخرى", Toast.LENGTH_SHORT);
							e.printStackTrace();
						}
			              
			             
			          }					
				   }else  if (requestCode == MAP_INTENT) {  
					   fillFieldsFromMapIntent(DataHolder.SelectedAddress);
				   }
			 }catch (Exception e) {
				 Toast toast = Toast.makeText(this, this.getString(R.string.str_no_image_selected), Toast.LENGTH_SHORT);
	             toast.show();
			}
			
		 }else {
			 if(requestCode == GALLERY_PICTURE  ||  requestCode == CAMERA_PICTURE)  {
				 Toast toast = Toast.makeText(this, this.getString(R.string.str_no_image_selected), Toast.LENGTH_SHORT);
	             toast.show();
			 }else {
				 Toast toast = Toast.makeText(this, this.getString(R.string.str_no_location_selected), Toast.LENGTH_SHORT);
	             toast.show();
			 }
             
         }
	  
          
	    
	 }

	private void setImage( ) {
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		int width = display.getWidth();
		int height = display.getHeight();  
			   
		    int stX=width/2;
		    int stY=height/2;
		    
		    int mx=sourceImage.getWidth();
		    int my=sourceImage.getHeight();
		    
		   
		    
		    int x=0;
		    int y=0;
		    
		    if(mx>stX){
		        x=stX;
		        y=(x*my)/mx;
		        
		    }else if(my>stY){
		        y=stY;
		        x=(y*mx)/my;
		    }
		    
		    if(x==0 && y==0){
		    		x=sourceImage.getWidth();
			    y=sourceImage.getHeight();
		    }
		     
          imageView.setImageBitmap(Bitmap.createScaledBitmap(sourceImage, x,y, true));
		
	}
	@Override
	public void done(int code, String response) {
		if(code==ConnectionController.Con_AddNew){	
			//String [] result=response.split("spacerASDF");
			Toast.makeText(this, this.getString(R.string.str_msg_added_succe), Toast.LENGTH_SHORT).show();			
			//Log.d("Act_AddNew","done( "+ response+ " )");
			DataHolder.litingID=response;
			
			if(sourceImage!=null){		
				System.gc();
			    Runtime.getRuntime().gc();
				new ImageUploader(this, imageFilePath,  DataHolder.User_ID, response);
				finish();
			}else { 
				// Get The liting detalis and show it
				String link="http://dilny.com/api/listingDetails/id/"+response+"/lat/"+GeoLocationService.lat+"/lng/"+GeoLocationService.lng;
				System.out.println("Link = "+link); 
				ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_LisitingDetalis,true,getString(R.string.str_msg_loading_lisiting_detalis));
				cc.setOnConnectionDone(this); 
				cc.startGetConnection(); 
			}
			/*
			Intent N = new Intent (Act_AddNew.this, Act_ItemDetalis.class);
			N.putExtra("item",result[1]);
	 	    startActivity(N);
			finish();*/
		}
		if(code==ConnectionController.Con_LisitingDetalis){
			// After connection done with the lisiting detlis -> open liting detalis
			Intent N = new Intent (Act_AddNew.this, Act_ItemDetalis.class);
			N.putExtra("item",response);
	 	    startActivity(N);
			finish();
		}
	}
	
	public void fillFieldsFromMapIntent(GoogleGeoAddressModel address){ 
	
		
		StringBuilder Adress=new StringBuilder();
			String Country=address.getCountry();
			String FullAdress=address.getFullAdress();
			
			String City="";
			if(address.getSubLocality()!=null){
				City=City+ address.getSubLocality()+", ";
				System.out.println("address.getSubLocality() : "+address.getSubLocality());
			}
			if(address.getLocality()!=null){
				City=City+ address.getLocality()+", ";
				System.out.println("address.getLocality() : "+address.getLocality());
			}
			if(address.getAdminLevel1()!=null){
				City= City+ address.getAdminLevel1()+", ";
				System.out.println("address.getAdminLevel1() : "+address.getAdminLevel1());
			}
			if(address.getAdminLevel2()!=null){
				City=City+address.getAdminLevel2()+", ";
				System.out.println("address.getAdminLevel2() : "+address.getAdminLevel2());
			}
			if(address.getAdminLevel3()!=null){
				City=City+address.getAdminLevel3()+", ";
				System.out.println("address.getAdminLevel3() : "+address.getAdminLevel3());
			}
			
			if(address.getStreet_number()!=null){
				Adress.append(address.getStreet_number()+","); 
			}

			if(address.getRoute()!=null){
				Adress.append(address.getRoute()); 
			}
			
			 str_address=Adress.toString();
			 str_city=City;
			 str_country=Country;
			 
			edit_adress.setText(Country+", "+City+", "+Adress.toString());
			//edit_country.setText(Country);
			//edit_city.setText(City); 
			
			edit_lat.setText(address.getLat()+"");
			edit_lng.setText(address.getLng()+"");
			
			dialogHandler.sendEmptyMessage(0);
			
			System.out.println("_lat : "+edit_lat.getText().toString());
			System.out.println("_lng : "+edit_lng.getText().toString());
		 
	}
	 
	
	@Override
	protected void onResume() {
		System.out.println("onResume()");
		DataHolder.getData(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		System.out.println("onPause()");
		DataHolder.setData(this, DataHolder.Mail, DataHolder.Password, DataHolder.User_ID, DataHolder.rememberMe);
		super.onPause();
	}
 
	 
	private TextWatcher inputTextWatcher = new TextWatcher() {
	    public void afterTextChanged(Editable s) { }
	    public void beforeTextChanged(CharSequence s, int start, int count, int after)       { }
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	        Log.d("inputTextWatcher","s");          
	    }
	};
    
}