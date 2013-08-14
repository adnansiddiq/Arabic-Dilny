package com.ksa.dilny; 

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.ksa.dilny.utils.DataProcessor;

 

public class Act_SingUp extends SherlockActivity   implements OnMenuItemClickListener ,onConnectionDoneCC  {
	    /** Called when the activity is first created. */
	EditText edit_name; 
	EditText edit_mail;
	EditText edit_password;
	
	
	ProgressDialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme( R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.act_singup);
	        
	    edit_mail=(EditText)findViewById(R.id.reg_edi_mail);
	    edit_password=(EditText)findViewById(R.id.reg_edit_password);
	    edit_name=(EditText)findViewById(R.id.reg_edi_name);
	    
	  
	     
	         
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub  
		 
		menu.add("Done")
        .setIcon( R.drawable.ic_done)
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		
		
		menu.add("back")
        .setIcon( R.drawable.ic_error)
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		   
		return super.onCreateOptionsMenu(menu);
	}
	 
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if(item.getTitle() =="Done"){

			String mail=edit_mail.getText().toString();
			String password=edit_password.getText().toString();
			String name=edit_name.getText().toString();
			String link=DataProcessor.getConnectionLink_SiginUP(Act_SingUp.this,mail,  password,  name);
			if(link !=null){
				System.out.println("link - > "+link);
				ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_Sigup,true,getString(R.string.str_msg_waitting_for_Sigin));
				cc.setOnConnectionDone(this);
				cc.startGetConnection();
			} 
		 }else {
			 Intent N = new Intent(Act_SingUp.this, Act_Dilny.class);
			    startActivity(N); 
			 finish();
		 }
		return false;
	}
	 
@Override
public void onBackPressed() {
	Intent N = new Intent(Act_SingUp.this, Act_Dilny.class);
    startActivity(N); 
 finish();
super.onBackPressed();
} 
	@Override
	public void done(int code, String response) {
		DataHolder.User_ID=response;
		DataHolder.setData(this, edit_mail.getText().toString(), edit_password.getText().toString(), response, true);
		Toast.makeText(this, getString(R.string.str_msg_signup_done), Toast.LENGTH_SHORT).show(); 
		finish(); 
		startApp();
		
	}
	private void startApp() {
		Intent N = new Intent(Act_SingUp.this, Act_Dilny.class);  
		startActivity(N);	
		finish();
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