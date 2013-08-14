package com.ksa.dilny;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.DataHolder;
import com.ksa.dilny.utils.ResultListAdapter;

public class Act_Favorites extends SherlockListActivity implements OnItemClickListener{
	ArrayList<ListItemModel> favos;
	  
	 ResultListAdapter ad;
	 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme( R.style.Theme_Sherlock_Light_DarkActionBar); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search_result);
		refreshList();
		 
		
		ColorDrawable divcolor = new ColorDrawable(this.getResources().getColor(R.color.grey));
		this.getListView().setDivider(divcolor);
		this.getListView().setDividerHeight(1); 
		 
		this.getListView().setOnItemClickListener(this);
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("Here - >refreshList() ");
		refreshList();
	}
	private void refreshList(){
		favos= DataHolder.getfavosFromDB(this); 
		  if(favos !=null){
			  System.out.println("favos.size() : "+favos.size());
			  if(favos.size()>0){
					ad=new ResultListAdapter(this, favos); 
					setListAdapter(ad); 					
				}
		  }else {
			  setListAdapter(null);
			  Toast.makeText(this, getString(R.string.str_msg_no_favorites),Toast.LENGTH_LONG).show();
		  }
		  
		  
	}
	  
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		DataHolder.item=favos.get(arg2);
		System.out.println("DataHolder.item");
		Intent N = new Intent(this, Act_ItemDetalis.class);
		ListItemModel item= favos.get(arg2);
		N.putExtra("item",item.getJsonItem());
	    startActivity(N);
		
		
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
 
