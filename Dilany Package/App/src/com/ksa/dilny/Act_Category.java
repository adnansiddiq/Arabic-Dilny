package com.ksa.dilny;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListActivity;
import com.ksa.dilny.DataModel.CatsModel;
import com.ksa.dilny.utils.CatsListAdapter;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.yahia.libs.Geo.GeoLocationService;

public class Act_Category extends SherlockListActivity implements OnItemClickListener , onConnectionDoneCC{
	
	ArrayList<CatsModel> catsList;
	String [] names;
	CatsListAdapter ad;
	
	public static final String ALL_CATS="5511";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		setTheme( R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_category);
		
		catsList =DataHolder.getCatsFromDB(this);
		 
		CatsModel all=new CatsModel();
		all.setId(ALL_CATS);
		all.setName("ابحث في جميع التصنيفات");
		all.setImage("");		
		catsList.add(catsList.size(), all); 
		
		if(catsList.size()>0){
			ad=new CatsListAdapter(this, catsList);
			setListAdapter(ad);
		}
		getListView().setOnItemClickListener(this);
		ColorDrawable divcolor = new ColorDrawable(this.getResources().getColor(R.color.grey));
		this.getListView().setDivider(divcolor);
		this.getListView().setDividerHeight(1);
	}
	String catID;
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
		CatsModel cat=catsList.get(arg2); 
		String id=cat.getId();
		if(id.equals(ALL_CATS)){
			 
			id="all";
		}
		
		catID=id;
		 
		lat=GeoLocationService.lat;
		lng=GeoLocationService.lng;
	    ConnectionController cc=new ConnectionController("http://dilny.com/api/searchByCat/lat/"+lat+"/lng/"+lng+"/radius/50/page/0/cat/"+id, this, 
	    		ConnectionController.Con_Cats,true,getString(R.string.str_loading));
	    cc.setOnConnectionDone(this);
	    cc.startGetConnection();
	}
	 
	double lat;
	double lng;
   	 

	@Override
	public void done(int code, String response) {
 	      Intent N = new Intent(Act_Category.this, Act_SearchResult.class); 
 	      N.putExtra("response",response);
 	     N.putExtra("connection",code);
 	    N.putExtra("catID",catID);
 	   N.putExtra("lat",lat);
 	  N.putExtra("lng",lng);
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
