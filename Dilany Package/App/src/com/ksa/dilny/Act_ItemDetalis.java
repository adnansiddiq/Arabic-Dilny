package com.ksa.dilny;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.Window;
import com.ksa.dilny.DataModel.ListItemModel;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.Consts;
import com.ksa.dilny.utils.DataHolder;
import com.ksa.dilny.utils.DataProcessor;
import com.yahia.libs.CustomViews.ImageFromUrl.ImageFileModel;
import com.yahia.libs.CustomViews.ImageFromUrl.UIImage;

public class Act_ItemDetalis extends SherlockActivity implements
		OnMenuItemClickListener, OnRatingBarChangeListener, onConnectionDoneCC {
	public static ListItemModel item;
	JSONObject jsonObject;

	TextView item_tv_title;
	TextView item_tv_phone;
	TextView item_tv_address;
	TextView item_tv_destance;
	TextView item_tv_desc;
	RatingBar item_rating_bar;
	UIImage item_im;
	WebView reviews_webview;
	Button item_btn_reviews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.act_item_detalis);
		String jsonItem = (String) getIntent().getSerializableExtra("item");
		System.out.println("jsonItem : " + jsonItem);
		item = DataProcessor.getListingFromResponse(jsonItem);

		item_tv_title = (TextView) findViewById(R.id.item_tv_title);
		item_tv_phone = (TextView) findViewById(R.id.tv_phone);
		item_tv_address = (TextView) findViewById(R.id.item_tv_address);
		item_tv_destance = (TextView) findViewById(R.id.item_tv_destance);
		item_tv_desc = (TextView) findViewById(R.id.item_tv_desc);
		item_rating_bar = (RatingBar) findViewById(R.id.item_rating_bar);
		item_im = (UIImage) findViewById(R.id.item_im);
		reviews_webview = (WebView) findViewById(R.id.item_reviews_webview);
		item_btn_reviews = (Button) findViewById(R.id.item_btn_reviews);

		item_tv_phone.setMovementMethod(LinkMovementMethod.getInstance());
		item_tv_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				call();
			}
		});

		if (item.getPhone().length() > 0) {
			item_tv_phone.setText(item.getPhone());
		} else {
			item_tv_phone.setVisibility(View.GONE);
		}

		item_btn_reviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent n = new Intent(Act_ItemDetalis.this, Act_Reviews.class);
				n.putExtra("id", item.getId());
				startActivity(n);

			}
		});

		fillFileds();

	}

	private void fillFileds() {
		item_tv_title.setText(item.getTitle());

		String addressStr = "";

		if (!(item.getCountry().equals("untranslated"))) {
			addressStr = item.getCountry();
		}
		if (!(item.getAddress_line_1().equals("untranslated"))) {
			addressStr = addressStr + ", " + item.getAddress_line_1();
		}
		if (!(item.getAddress_line_2().equals("untranslated"))) {
			addressStr = addressStr + ", " + item.getAddress_line_2();
		}
		item_tv_address.setText(addressStr);

		item_tv_desc.setText((item.getDesc()).replace("<p>", "\n"));
		/*
		 * if(item.getPriceMin() !=null && item.getPriceMax()!=null){
		 * item_tv_price.setText((int) Double.parseDouble(item.getPriceMin())
		 * +" - "+(int) Double.parseDouble(item.getPriceMax()) + " ر.س"); }
		 */
		if (item.getDistance() != null && !item.getDistance().equals("null")) {
			item_tv_destance.setText((int) Double.parseDouble(item
					.getDistance()) + " كم");
		}

		// item_im.setImageResource(R.drawable.ic_error);
		item_rating_bar
				.setRating((float) Double.parseDouble(item.getRatings()));
		item_rating_bar.setOnRatingBarChangeListener(this);
		item_im.setImage(new ImageFileModel(item.getImage(), null, null));

		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ Consts.CSSStyle + "</head><html>" + item.getReviewsHtml()
				+ "</html>";
		System.out.println("header : " + header);

		reviews_webview.loadData(header, "text/html; charset=UTF-8", null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (isSiginedIn()) {
			if (DataHolder.checkFavoriteItem(item, getApplicationContext())) {
				menu.add("Favo").setIcon(R.drawable.ic_favo)
						.setOnMenuItemClickListener(this)
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			} else {
				menu.add("Favo").setIcon(R.drawable.ic_favo_in)
						.setOnMenuItemClickListener(this)
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			}
		} else {
			menu.add("Favo").setIcon(R.drawable.ic_favo_in)
					.setOnMenuItemClickListener(this).setEnabled(false)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}

		/*
		 * menu.add("Map").setIcon(R.drawable.ic_map)
		 * .setOnMenuItemClickListener(this)
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 */

		menu.add("GoogleMaps")
				.setIcon(R.drawable.ic_google_maps)
				.setOnMenuItemClickListener(this)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add("Share")
				.setIcon(R.drawable.ic_share)
				.setOnMenuItemClickListener(this)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add("Call")
				.setIcon(R.drawable.ic_call)
				.setOnMenuItemClickListener(this)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		return super.onCreateOptionsMenu(menu);
	}

	private boolean isSiginedIn() {
		if (DataHolder.User_ID != null) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean onMenuItemClick(MenuItem items) {
		if (items.getTitle().equals("Favo")) {
			if (isSiginedIn()) {
				System.out.println("thumb : " + item.getThumb());
				String process = DataHolder.processFavorites(item, this);
				System.out.println("process : " + process);
				if (process.equals(Consts.FAVO_ADDED)) {
					items.setIcon(R.drawable.ic_favo);
				} else if (process.equals(Consts.FAVO_REMOVED)) {
					items.setIcon(R.drawable.ic_favo_in);
				}
				String link = "http://dilny.com/api/addToFavorites/id/"
						+ item.getId() + "/user/" + DataHolder.User_ID;
				ConnectionController cc = new ConnectionController(link, this,
						ConnectionController.Con_AddNewFavorites, false, null);
				cc.setOnConnectionDone(this);
				cc.startGetConnection();
			} else {
				Toast.makeText(this, "عفواً , لم تقم بتسجيل الدخول بعد",
						Toast.LENGTH_SHORT).show();
			}

		} else if (items.getTitle().equals("Call")) {
			call();

		} else if (items.getTitle().equals("Map")) {
			Intent n = new Intent(this, Act_Map.class);
			n.putExtra("lat", item.getlat());
			n.putExtra("lng", item.getlng());
			n.putExtra("jsonItem", item.getJsonItem());

			startActivity(n);
		} else if (items.getTitle().equals("GoogleMaps")) {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("geo:0,0?q=" + item.getlat() + ","
							+ item.getlng() + "(" + item.getTitle() + ")"));
			startActivity(intent);

		} else if (items.getTitle().equals("Share")) {
			// share
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String placeLink = "http://maps.google.com/maps?q=" + item.getlat()
					+ "," + item.getlng();
			String strShare = item.getTitle() + "\n" + item_tv_address.getText().toString()
					+ "\n" + placeLink;
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, strShare);
			startActivity(Intent.createChooser(sharingIntent,
					"شارك باستخدام : "));

		}

		return false;
	}

	private void call() {
		if (item.getPhone().length() > 1) {
			String phone = item.getPhone();
			System.out.println("phone: " + phone);
			try {
				System.out.println("tel:" + phone);
				Intent callIntent = new Intent(Intent.ACTION_DIAL);
				callIntent.setData(Uri.parse("tel:" + phone));
				startActivity(callIntent);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.str_msg_no_phone_found),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.str_msg_no_phone_number_sfound),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		if (!rated) {
			String link = "http://dilny.com/api/addRate/user/"
					+ DataHolder.User_ID + "/listing/" + item.getId()
					+ "/rate/" + rating;
			ConnectionController cc = new ConnectionController(link, this,
					ConnectionController.Con_rating, true,
					getString(R.string.str_msg_waitting_for_ratting));
			cc.setOnConnectionDone(this);
			cc.startGetConnection();
		}

	}

	Boolean rated = false;

	@Override
	public void done(int code, String response) {
		if (code == ConnectionController.Con_rating) {
			rated = true;
			System.out.println("Con_rating : response : " + response);
			if (response.equals("E5009")) {
				float newRate = (float) Double.parseDouble(item.getRatings());
				item_rating_bar.setEnabled(false);
				item_rating_bar.setRating(newRate);
			} else {
				float newRate = Float.parseFloat(response);
				item_rating_bar.setEnabled(false);
				item_rating_bar.setRating(newRate);
				Toast.makeText(this, getString(R.string.str_msg_rating_added),
						Toast.LENGTH_SHORT).show();
			}

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
		DataHolder.setData(this, DataHolder.Mail, DataHolder.Password,
				DataHolder.User_ID, DataHolder.rememberMe);
		super.onPause();
	}

}
