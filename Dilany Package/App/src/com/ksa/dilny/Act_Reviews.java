package com.ksa.dilny;


import java.net.URI;
import java.net.URLEncoder;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.Consts;
import com.ksa.dilny.utils.DataHolder;

public class Act_Reviews extends SherlockActivity implements onConnectionDoneCC,OnMenuItemClickListener , OnClickListener, OnRatingBarChangeListener{
	WebView rev_webview;
	String id;
	
	Dialog review_dialog;
	EditText review;
	RatingBar ratingBar1;
	Button send;
	float rate;
	Boolean rated=false;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		setTheme( R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_review);
		id= (String) getIntent().getSerializableExtra("id");
		rev_webview=(WebView)findViewById(R.id.rev_webview);
		
		refreshReviews();
	}
	private void refreshReviews(){
		String link="http://dilny.com/api/listingReview/id/"+id;
		ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_reviews,true,getString(R.string.str_msg_waitting_for_reviews));
		cc.setOnConnectionDone(this);
		cc.startGetConnection();
	}

	private boolean isSiginedIn() {
		if (DataHolder.User_ID != null) {
			return true;
		} else {
			return false;
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(isSiginedIn()){
			menu.add("send")
	        //.setIcon( R.drawable.ic_signup)
			.setTitle("إضافة")
	        .setOnMenuItemClickListener(this)
	        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		} 
		
		menu.add("refresh")
        .setIcon( R.drawable.ic_refresh_inverse)
        .setOnMenuItemClickListener(this)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS );
	  
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if(item.getTitle().equals("إضافة")){
			review_dialog = new Dialog(this);
			 
			review_dialog.setContentView(R.layout.review_dialog); 
			
			review_dialog.setTitle("تعليقك هنا : ");
			review=(EditText)review_dialog.findViewById(R.id.review_di_edit);
			ratingBar1=(RatingBar)review_dialog.findViewById(R.id.ratingBar1); 
			ratingBar1.setOnRatingBarChangeListener(this);
			send=(Button)review_dialog.findViewById(R.id.review_di_send);
			send.setOnClickListener(this);
		 
			review_dialog.show();
		}else if(item.getTitle().equals("refresh")){
			refreshReviews();
		}
		return false;
	}
	URI uri;
	@Override
	public void onClick(View v) {
		if(v==send){
			if(rated){
				rated=false;
				String link="http://dilny.com/api/addRate/user/"+DataHolder.User_ID+"/listing/"+id+"/rate/"+rate;
				ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_rating,true,getString(R.string.str_msg_waitting_for_ratting));
				cc.setOnConnectionDone(this);
				cc.startGetConnection();
			}
			if(review.getText().length()>0){
				 
				String reviewString=(review.getText().toString()).replace("\n","<p>");;
				String link="";
				try {
					//link = "http://dilny.com/api/addReview/user/"+DataHolder.User_ID+"/listing/"+id+"/review/"+URLEncoder.encode(reviewString,"UTF-8");
					uri = new URI("http", null, "www.dilny.com", 80, "/api/addReview/user/"+DataHolder.User_ID+"/listing/"+id+"/review/"+ URLEncoder.encode(reviewString,"UTF-8"),null, null);


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				link=uri.toASCIIString().replace("%0A", "%20");
				 
				ConnectionController cc=new ConnectionController(link, this, ConnectionController.Con_addreviews,true,getString(R.string.str_msg_waitting_for_sending_review));
				cc.setOnConnectionDone(this);
				cc.startGetConnection();
			}else {
				Toast.makeText(this, getString(R.string.str_msg_empty_review), Toast.LENGTH_SHORT).show();
			}
			review_dialog.dismiss();
		}
		
	}
	
	@Override
	public void done(int code, String response) { 
		if(code ==ConnectionController.Con_reviews){
			String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"+Consts.CSSStyle+"</head><html>"+response+"</html>";
			rev_webview.loadData( header, "text/html; charset=UTF-8", null); 
		}else if(code==ConnectionController.Con_rating){
			rated=false;
			ratingBar1.setEnabled(false);
			System.out.println("Con_rating : response : "+response);
			if(response.equals("E5009")){
				Toast.makeText(this, getString(R.string.str_msg_rating_added), Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(this, getString(R.string.str_msg_rating_added), Toast.LENGTH_SHORT).show();
			}
			
			 
		} else {			
		 
			Toast.makeText(this, getString(R.string.str_msg_review_added), Toast.LENGTH_SHORT).show();
			refreshReviews();
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
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		rated=true;
		rate=rating;
		
	}
	 
	
	
}
