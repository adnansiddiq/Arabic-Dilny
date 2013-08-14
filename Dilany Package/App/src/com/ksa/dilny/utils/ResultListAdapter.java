package com.ksa.dilny.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ksa.dilny.Act_Favorites;
import com.ksa.dilny.Act_SearchResult;
import com.ksa.dilny.R;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.yahia.libs.CustomViews.ImageFromUrl.ImageFileModel;
import com.yahia.libs.CustomViews.ImageFromUrl.UIImage;

public class ResultListAdapter extends BaseAdapter implements onConnectionDoneCC  {
	ArrayList<ListItemModel> items;
	Context c;
	LayoutInflater loi; 
	public ResultListAdapter (Context _c,ArrayList<ListItemModel> _items){	
		c=_c;
		items=_items;
		loi= LayoutInflater.from(_c);
	}
	@Override
	public int getCount() { 
		return items.size();
	}

	@Override
	public Object getItem(int index) {
		return items.get(index);
	}

	@Override
	public long getItemId(int index) {
		ListItemModel item=items.get(index);
		return Integer.parseInt(item.getId());
		
	}
	private boolean isSiginedIn() {
		if (DataHolder.User_ID != null) {
			return true;
		} else {
			return false;
		}

	}
	ViewHolder vh;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	  
	
	vh=new ViewHolder();
	final ListItemModel item=items.get(position);
	 
		//System.out.println("items.size()-1 : "+(items.size()-1));
		//System.out.println("position : "+position);
		if(position==items.size()-1 && item.getId().equals(Act_SearchResult.LAST_ITEM_ID)){
			convertView=loi.inflate(R.layout.result_list_last_row,null);  
		}else {
			int height=0;
			
			convertView=loi.inflate(R.layout.result_list_row,null);  
		
			vh.row_rating_bar=(RatingBar)convertView.findViewById(R.id.row_rating_bar);
			vh.row_im_thumb=(UIImage)convertView.findViewById(R.id.row_im_thumb);
			vh.im_favo=(ImageButton)convertView.findViewById(R.id.row_im_favo);
			
			vh.row_tv_title=(TextView)convertView.findViewById(R.id.row_tv_title);
			vh.row_tv_address=(TextView)convertView.findViewById(R.id.row_tv_address);
			vh.row_tv_price=(TextView)convertView.findViewById(R.id.row_tv_price);
			vh.row_tv_distance=(TextView)convertView.findViewById(R.id.row_tv_distance);

			 //System.out.println("1");
			convertView.setTag(vh);
			
			height=vh.row_tv_title.getHeight()+vh.row_tv_address.getHeight()+vh.row_tv_price.getHeight()+vh.row_tv_distance.getHeight()
					+vh.row_rating_bar.getHeight()+vh.row_im_thumb.getHeight()+vh.im_favo.getHeight();
			
			convertView.setMinimumHeight(height);
			/*
			WindowManager mWinMgr = (WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
			int displayWidth = mWinMgr.getDefaultDisplay().getWidth();
			
			convertView.setLayoutParams(new LayoutParams(displayWidth, height));*/
			
			
			vh.row_tv_title.setText(item.getTitle());
			
			String addressStr="";
			
			if(!(item.getCountry().equals("untranslated"))){
				addressStr=item.getCountry();
			}
			if(!(item.getAddress_line_1().equals("untranslated"))){
				addressStr=addressStr+", "+item.getAddress_line_1();
			}
			if(!(item.getAddress_line_2().equals("untranslated"))){
				addressStr=addressStr+", "+item.getAddress_line_2();
			}
			vh.row_tv_address.setText(addressStr);

			
			
			/*if(item.getPriceMin() !=null && item.getPriceMax()!=null){
				vh.row_tv_price.setText((int) Double.parseDouble(item.getPriceMin())  +" - "+(int) Double.parseDouble(item.getPriceMax()) + " ر.س");
			}*/
			if(item.getDistance()!= null && !item.getDistance().equals("null")){
				vh.row_tv_distance.setText((int) Double.parseDouble(item.getDistance())+" كم");
			}
			
			
			//vh.row_ib_favo.setBackgroundResource(R.drawable.ic_error);
			if(item.getRatings() !=null){
				vh.row_rating_bar.setRating((float)Double.parseDouble(item.getRatings()));
			}else {
				vh.row_rating_bar.setRating(0);
			}
			
			vh.row_im_thumb.setImage(new ImageFileModel(item.getThumb(), null, null));  
			vh.row_im_thumb.refreshImage();
			//vh.row_ib_favo.setFocusable(false);
			if(c.getClass()!=Act_Favorites.class   ){
				if(isSiginedIn()){
					vh.im_favo.setVisibility(View.VISIBLE);
					vh.im_favo.setFocusable(false);
					
					if(DataHolder.checkFavoriteItem(item, c)){
						vh.im_favo.setBackgroundResource(R.drawable.ic_favo);
					}else {
						vh.im_favo.setBackgroundResource(R.drawable.ic_favo_in);
					}
					vh.im_favo.setOnClickListener(new OnClickListener() {
						 
						@Override
						public void onClick(View v) {			
							String process=DataHolder.processFavorites( item,c);
							//System.out.println("process : "+process);
						  
							if(process!=null){
								if(process.equals(Consts.FAVO_ADDED))
								{
									v.invalidate();
									v.setBackgroundResource( R.drawable.ic_favo);
									v.invalidate();
								}else if(process.equals(Consts.FAVO_REMOVED)){
									v.invalidate();
									v.setBackgroundResource( R.drawable.ic_favo_in);
									v.invalidate();
								}
							 	
							}
							String link="http://dilny.com/api/addToFavorites/id/"+item.getId()+"/user/"+DataHolder.User_ID;
							ConnectionController cc=new ConnectionController(link, c, ConnectionController.Con_AddNewFavorites,false,null);
							cc.setOnConnectionDone(ResultListAdapter.this);
							cc.startGetConnection();
							//notifyDataSetChanged();
							
							 
						}
					});
				}else{
					vh.im_favo.setVisibility(View.INVISIBLE);
					vh.im_favo.setFocusable(false);
				}
				
			}
			
		}
		return convertView;
	}

	static class ViewHolder {
	 	TextView row_tv_title;
	 	TextView row_tv_address;
	 	TextView row_tv_price;
	 	TextView row_tv_distance;
	 	//ImageButton row_ib_favo;
	 	UIImage row_im_thumb;
	 	RatingBar row_rating_bar;
	 	ImageButton im_favo;
	
	 	 }

	@Override
	public void done(int code, String response) {
		// TODO Auto-generated method stub
		
	}
	 
}
	