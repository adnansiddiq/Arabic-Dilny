package com.ksa.dilny;
 

 import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.ksa.dilny.utils.DataProcessor;

 

public class Act_SingIn extends SherlockActivity implements  OnMenuItemClickListener,onConnectionDoneCC{
    /** Called when the activity is first created. */
 
	EditText edit_userName;
	EditText edit_password;
	//Button btn_sigin;
	CheckBox  check_signin_remember;
	ProgressDialog dialog;
	TextView tv_SingUp;
	String mail;
	String password;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) { 
    	setTheme( R.style.Theme_Sherlock_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_singin);
        
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped);
            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setBackgroundDrawable(bg);

            BitmapDrawable bgSplit = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
            bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setSplitBackgroundDrawable(bgSplit);
        }
        
      
          
        edit_password=(EditText)findViewById(R.id.edit_password);
        edit_userName=(EditText)findViewById(R.id.edit_user_name);
        
        edit_userName=(EditText)findViewById(R.id.edit_user_name);
        tv_SingUp=(TextView)findViewById(R.id.tv_sigup);
        
        check_signin_remember=(CheckBox)findViewById(R.id.signin_remember);
        //btn_sigin=(Button)findViewById(R.id.signin_btn_signin);
        
        tv_SingUp.setMovementMethod(LinkMovementMethod.getInstance());
         System.out.println("Act_SingIn :: onCreate");
        fillUserfileds();        
        checkSavedData();
        
        tv_SingUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent N = new Intent(Act_SingIn.this, Act_SingUp.class); 
				N.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(N);
				finish();
				
			}
		});
       /* btn_sigin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sigin();
			}
		});*/
    }
    
    public void fillUserfileds(){
    	if(DataHolder.Mail != null){
        	edit_userName.setText(DataHolder.Mail );
        }
        
        if(DataHolder.Password != null){
        	edit_password.setText(DataHolder.Password );
        }
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub   
    		menu.add("Info")
        .setIcon( R.drawable.info_in)
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS );
		/*
		menu.add("SignUp")
        .setIcon( R.drawable.ic_signup)
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		*/
		menu.add("SignIn")
        .setIcon( R.drawable.ic_done)
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		   
		return super.onCreateOptionsMenu(menu);
	}
    
	 
	 
	public void checkSavedData(){
		if(DataHolder.rememberMe){
			sigin();
		}
	} 
	
	public void checkRememberMeStatus(String user_id){
		if(check_signin_remember.isChecked()){
			System.out.println("checkRememberMeStatus - > true");
			mail=edit_userName.getText().toString();
			password=edit_password.getText().toString();
			DataHolder.setData(this,mail,password,user_id,true);
		}
		startApp();
	}
	 
 

	private void startApp() {
		Intent N = new Intent(Act_SingIn.this, Act_Dilny.class); 
		finish();
		startActivity(N);	
	}
  	 
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if(item.getTitle() =="Info"){
			Intent n =new Intent(this,Act_ApoutApp.class);
			startActivity(n);
			
		}else	if(item.getTitle() =="SignIn"){ 
			sigin();
		 }else if(item.getTitle() =="SignUp"){ 
			Intent N = new Intent(Act_SingIn.this, Act_SingUp.class); 
			startActivity(N);
			finish();
		 } 
		return false;
	}

	private void sigin() {
		mail= edit_userName.getText().toString();
		password=edit_password.getText().toString();
		String link=DataProcessor.getConnectionLink_SiginIN(this,mail,password );
		if(link !=null){
			System.out.println("link ->"+link);
			//new HttpConnection(handler).get(link);
			ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_Sigin,true,getString(R.string.str_msg_waitting_for_Sigin));
			cc.setOnConnectionDone(this);
			cc.startGetConnection();
		}
		
	}
	@Override
	public void onBackPressed() {
		Intent N = new Intent( this, Act_Dilny.class);
	    startActivity(N); 
	 finish();
	super.onBackPressed();
	}
	@Override
	public void done(int code, String response) {
		String [] result=response.split(",");
		checkRememberMeStatus(result[0]);
		if(result[1].equals("1")){
			Toast.makeText(this, getString(R.string.str_msg_you_need_to_activate_account), Toast.LENGTH_LONG).show();
		}
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
	 
	
}