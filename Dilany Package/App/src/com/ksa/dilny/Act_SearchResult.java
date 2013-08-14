package com.ksa.dilny;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.ksa.dilny.utils.DataProcessor;
import com.ksa.dilny.utils.ResultListAdapter;
import com.yahia.libs.Geo.GeoLocationService;
import com.yahia.libs.InternetConnections.UTFEncoder;

public class Act_SearchResult extends SherlockListActivity implements OnItemClickListener,onConnectionDoneCC{
	 String names[]; 
	 ArrayList<ListItemModel> listItems=new ArrayList<ListItemModel>();
	 ResultListAdapter ad;
	 TextView tv;
	 int page=0;
	 int connectionCode;
	 
	 
		public static Double lat;
		public static Double lng;
		public static String catID;
		public static String searchWord;
	public static final String LAST_ITEM_ID="0000";
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("Act_SearchResult","onCreate - start");
		DataHolder.saveSearchData(this,null,null,null,null);
		setTheme( R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search_result);
		tv=(TextView)findViewById(R.id.tv);
		
		String response= (String) getIntent().getSerializableExtra("response");
		connectionCode=  (Integer) getIntent().getSerializableExtra("connection");
		lat= (Double) getIntent().getSerializableExtra("lat");
		lng= (Double) getIntent().getSerializableExtra("lng");
		catID= (String) getIntent().getSerializableExtra("catID");
		searchWord= (String) getIntent().getSerializableExtra("searchWord");
		
		//System.out.println(response);
		
		loadList(true,response);
		
		
		ColorDrawable divcolor = new ColorDrawable(this.getResources().getColor(R.color.grey));
		this.getListView().setDivider(divcolor);
		this.getListView().setDividerHeight(1);
		
		 
		this.getListView().setOnItemClickListener(this);
		Log.d("Act_SearchResult","onCreate - end");
	}
	  private void loadList(Boolean st,String response){
		  ListItemModel lastItem=new ListItemModel();
		  lastItem.setId(LAST_ITEM_ID);
		  ArrayList<ListItemModel> newItems =new ArrayList<ListItemModel>();
		  if(st){
			  
			  this.listItems=DataProcessor.getListinigsListFromResponse(response) ;
			  if(this.listItems.size()>=20){
				  this.listItems.add(lastItem);
			  }
		  }else {
			  newItems =DataProcessor.getListinigsListFromResponse(response) ;
			  if(newItems.size()>0){
				  Log.d("loadList","newItems.size()>0");
				  this.listItems.remove(this.listItems.size()-1);
				  this.listItems.addAll(newItems);
				  this.listItems.add(lastItem); 
			  }else {
				  Log.d("loadList","ELSE");
				  this.listItems.remove(this.listItems.size()-1);
			  }
		  }
		  
		  if(listItems.size()>0){
				ad=new ResultListAdapter(this, listItems); 
				setListAdapter(ad);  
				//getListView().smoothScrollToPosition(ad.getCount() - 1);
				if(!st){
					 int x=ad.getCount()-newItems.size();
					if(x!=0){
						getListView().setSelection(x- 2);
					}else {
						getListView().setSelection(ad.getCount() );
					}
					
				}
			}else 
			{
				if(connectionCode==ConnectionController.Con_TextSearch){
					tv.setText("لا توجد أماكن بالقرب من موقعك الحالي");
				}
				tv.setVisibility(View.VISIBLE);
			}
		  st=false;
	  }
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		DataHolder.item=listItems.get(position);
		if(position==listItems.size()-1 && DataHolder.item.getId().equals(LAST_ITEM_ID)  ){
			loadMore();
		}else {
			DataHolder.item=listItems.get(position);
			System.out.println("DataHolder.item");
			Intent N = new Intent(Act_SearchResult.this, Act_ItemDetalis.class);
			ListItemModel item= listItems.get(position);
			N.putExtra("item",item.getJsonItem());
	 	    startActivity(N);
		}
		
		
	}
	 
	
	private void loadMore() {
		Log.d("Act_SearchResult", "loadMore");
		page=page+1;
		 
		
		if(connectionCode==ConnectionController.Con_map_long_click){
			
			String link = "http://dilny.com/api/searchNearby/lat/" + lat + "/lng/"+ lng + "/radius/50/page/"+page+"/cat/";
			Log.d("Con_map_long_click :" , link);
			ConnectionController cc = new ConnectionController(link, this,	ConnectionController.Con_map_long_click, true,
					getString(R.string.str_loading));
			cc.setOnConnectionDone(this);
			cc.startGetConnection();
			
		}else if(connectionCode==ConnectionController.Con_Cats){
			
			ConnectionController cc=new ConnectionController("http://dilny.com/api/searchByCat/lat/"+lat+"/lng/"+lng+"/radius/50/page/"+page+"/cat/"+catID, this, 
		    		ConnectionController.Con_Cats,true,getString(R.string.str_loading));
		    cc.setOnConnectionDone(this);
		    cc.startGetConnection();
		}else if(connectionCode==ConnectionController.Con_Nerby){
			 ConnectionController cc=new ConnectionController("http://dilny.com/api/searchNearby/lat/"+lat+"/lng/"+lng+"/radius/50/page/"+page+"/cat/", this, ConnectionController.Con_Nerby,true,getString(R.string.str_loading));
			 cc.setOnConnectionDone(this);
			 cc.startGetConnection();
		}else if(connectionCode==ConnectionController.Con_TextSearch){
			
		 	UTFEncoder encoder=new UTFEncoder();	
			String link="http://dilny.com/api/searchByText/title/"+encoder.encod(searchWord)+"/lat/"+GeoLocationService.lat+"/lng/"+GeoLocationService.lng+"/radius/50/page/"+page+"/cat/";
		 	ConnectionController cc= new ConnectionController(link, this, ConnectionController.Con_TextSearch,true,getString(R.string.str_msg_waitting_for_search));
			 cc.setOnConnectionDone(this); 
			 cc.startGetConnection();
		}	 


	}
	 
	@Override
	protected void onResume() {
		Log.d("Act_SearchResult","onResume");
		System.out.println("onResume()");
		DataHolder.getData(this);
 
		DataHolder.getSearchData(this);
		if(ad!=null){
			ad.notifyDataSetChanged();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.d("Act_SearchResult","onPause");
		System.out.println("onPause()");
		DataHolder.setData(this, DataHolder.Mail, DataHolder.Password, DataHolder.User_ID, DataHolder.rememberMe);
		DataHolder.saveSearchData(this,searchWord,catID,lat,lng);
		if(ad!=null){
			ad.notifyDataSetChanged();
		}
		
		super.onPause();
	}
	@Override
	public void done(int code, String response) {
		loadList(false,response);
		 
	}
	 
	
	
}
