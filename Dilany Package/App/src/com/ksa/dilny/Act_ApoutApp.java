package com.ksa.dilny;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockActivity;

public class Act_ApoutApp extends SherlockActivity implements OnClickListener{
	ImageButton btn_Site;
	ImageButton btn_Mail;
	ImageButton btn_Site_site;
	ImageButton btn_Mail_site;
	ImageButton btn_Facebook_site;
	ImageButton btn_Twitter_site;
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		
	  	setTheme( R.style.Theme_Sherlock_Light);
	  	super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_app);
        
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            BitmapDrawable bg = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped);
            bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setBackgroundDrawable(bg);

            BitmapDrawable bgSplit = (BitmapDrawable)getResources().getDrawable(R.drawable.bg_striped_split_img);
            bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            getSupportActionBar().setSplitBackgroundDrawable(bgSplit);
        }
        
        
		
		btn_Mail=(ImageButton)findViewById(R.id.btn_Mail);
		btn_Site=(ImageButton)findViewById(R.id.btn_Site);
		btn_Mail_site=(ImageButton)findViewById(R.id.btn_Mail_site);
		btn_Site_site=(ImageButton)findViewById(R.id.btn_Site_site);
		btn_Facebook_site=(ImageButton)findViewById(R.id.btn_Facebook);
		btn_Twitter_site=(ImageButton)findViewById(R.id.btn_Twitter);
		
		btn_Mail.setOnClickListener(this);
		btn_Site.setOnClickListener(this);
		btn_Mail_site.setOnClickListener(this);
		btn_Site_site.setOnClickListener(this);
		btn_Twitter_site.setOnClickListener(this);
		btn_Facebook_site.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		if(v==btn_Mail){
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

			String[] recipients = new String[]{"info@etqanApps.com" };

			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);

			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sent From Dilny Android App"); 

			emailIntent.setType("text/plain");

			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}else if(v==btn_Site){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.EtqanApps.com"));
			startActivity(browserIntent);
		}else if(v==btn_Twitter_site){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/EtqanApps"));
			startActivity(browserIntent);
		}else if(v==btn_Facebook_site){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/EtqanApps"));
			startActivity(browserIntent);
		}
		else if(v==btn_Mail_site){
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

			String[] recipients = new String[]{"Dilny.com@gmail.com" };

			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);

			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sent From Dilny Android App"); 

			emailIntent.setType("text/plain");

			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}else if(v==btn_Site_site){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dilny.com"));
			startActivity(browserIntent);
		}
		
	}
}
