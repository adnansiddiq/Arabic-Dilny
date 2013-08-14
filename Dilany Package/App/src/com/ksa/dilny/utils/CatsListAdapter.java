package com.ksa.dilny.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ksa.dilny.R;
import com.ksa.dilny.DataModel.CatsModel;
import com.yahia.libs.CustomViews.ImageFromUrl.ImageFileModel;
import com.yahia.libs.CustomViews.ImageFromUrl.UIImage;

public class CatsListAdapter extends BaseAdapter {
	ArrayList<CatsModel> items;
	Context c;
	ArrayList<View> views;

	public CatsListAdapter(Context _c, ArrayList<CatsModel> _items) {
		c = _c;
		items = _items;
		this.views = new ArrayList<View>();
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
		CatsModel item = items.get(index);
		return Integer.parseInt(item.getId());

	}

	@Override
	public View getView(int position, View convertViw, ViewGroup parent) {
		ViewHolder vh;
		RelativeLayout todoView = null;
		String tag = "" + position + "";
		vh = new ViewHolder();
		for (int x = 0; x < views.size(); x++) {
			todoView = (RelativeLayout) views.get(x);
			if (todoView.getTag().equals(tag)) {
				System.out.println("Reused Data");
				return todoView;
			}
		}
		
		LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		todoView=new RelativeLayout(c);
		todoView.setTag(tag);
         
        li.inflate(R.layout.cat_list_row,todoView,true);
        
		
		vh.cats_row_tv_title = (TextView) todoView
				.findViewById(R.id.cats_row_tv_title);
		vh.cats_row_im_thumbs = (UIImage) todoView
				.findViewById(R.id.cats_row_im_thumbs);

		CatsModel item = items.get(position);
		Log.d("CatsListAdapter", "getView : " + position);
		Log.d("CatsListAdapter", "item.getImage() : " + item.getImage());
		if (item.getImage().equals("")) {
			vh.cats_row_im_thumbs.setImageBitmap(BitmapFactory.decodeResource(
					c.getResources(), R.drawable.ic_launcher));
			vh.cats_row_im_thumbs.pb.setVisibility(View.INVISIBLE);
		} else {
			vh.cats_row_im_thumbs.setImage(new ImageFileModel(item.getImage(),
					null, null));
		}
		vh.cats_row_tv_title.setText(item.getName());

		views.add(todoView);
		System.out.println("New Data");
		return todoView;
	}

	static class ViewHolder {
		TextView cats_row_tv_title;
		UIImage cats_row_im_thumbs;

	}

}