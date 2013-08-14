package com.ksa.dilny;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.yahia.libs.Geo.GeoLocationService;
import com.yahia.libs.Geo.GeoLocationService.GeoLocationServiceListener;
import com.yahia.libs.InternetConnections.UTFEncoder;
import com.yahia.libs.db.ExportDB;

public class Act_Dilny extends SherlockActivity implements OnClickListener,
		OnMenuItemClickListener, GeoLocationServiceListener, onConnectionDoneCC {

	Button btn_cats;
	Button btn_nearby;
	Button btn_new_lisitng;
	ImageButton refreshBtn;
	Button btn_favorites;
	ImageButton btnInfo;
	TextView tv_address;
	ProgressBar pb;

	ProgressDialog dialog;

	Boolean ConnectionDone = false;

	Context c;
	Boolean loadFavoStatus = false;
	GeoLocationService geoLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_dilny);

		c = this;

		btn_cats = (Button) findViewById(R.id.dilny_btn_cats);
		btn_nearby = (Button) findViewById(R.id.dilny_btn_nearby);
		btn_new_lisitng = (Button) findViewById(R.id.dilny_btn_new_lisitng);
		btnInfo = (ImageButton) findViewById(R.id.dilny_btn_info);
		refreshBtn = (ImageButton) findViewById(R.id.refreshBtn);

		btn_favorites = (Button) findViewById(R.id.dilny_btn_favorites);
		pb = (ProgressBar) findViewById(R.id.dilny_pb);
		tv_address = (TextView) findViewById(R.id.dilny_address);

		btn_cats.setOnClickListener(this);
		btnInfo.setOnClickListener(this);
		btn_nearby.setOnClickListener(this);
		btn_new_lisitng.setOnClickListener(this);
		btn_favorites.setOnClickListener(this);
		refreshBtn.setOnClickListener(this);

		if (!btn_nearby.isEnabled()) {
			mNearst.run();
		}

		geoLocation = new GeoLocationService(this);
		GeoLocationService.loadAddress = true;
		geoLocation.setTheListener(this);

		checkGPS();

	}

	private void loadFavorites() {
		
		ConnectionController cc = new ConnectionController(
				"http://dilny.com/api/getFavoritesList/user/"
						+ DataHolder.User_ID + "/lat/" + GeoLocationService.lat
						+ "/lng/" + GeoLocationService.lng, this,
				ConnectionController.Con_Favorites, false,
				getString(R.string.str_msg_loading_favorites));
		cc.startGetConnection();
		cc.setOnConnectionDone(this);
	}

	EditText search;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add("Map").setIcon(R.drawable.ic_map)
				.setOnMenuItemClickListener(this)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add("Search")
				.setIcon(R.drawable.ic_search)
				.setOnMenuItemClickListener(this)
				.setActionView(R.layout.collapsible_edittext)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		MenuItem item = menu.getItem(1);
		System.out.println(item.getTitle());
		View v = item.getActionView();

		search = (EditText) v.findViewById(R.id.edit_search);
		search.setText("مطعم");
		search.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					startWordSearch();
				}
				return true;
			}

		});

		search.addTextChangedListener(inputTextWatcher);

		if (!isSiginedIn()) {
			menu.add("Sigin").setIcon(R.drawable.ic_sigin)
					.setOnMenuItemClickListener(this)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		} else {
			menu.add("Out").setIcon(R.drawable.out)
					.setOnMenuItemClickListener(this)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return super.onCreateOptionsMenu(menu);
	}

	private TextWatcher inputTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			char[] tex = text.toCharArray();
			if (tex.length > 0) {
				for (int x = 0; x < tex.length; x++) {
					if (tex[x] == '\n') {
						System.out.println("Search text : " + text);
						startWordSearch();
					}
				}

			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};

	public void startWordSearch() {
		String text = search.getText().toString();
		String[] a = text.split("\n");
		/*
		 * text.replace("\n", ""); search.setText(text);
		 */
		search.setText("");
		text = a[0];
		System.out.println("text.toCharArray().length : "
				+ text.toCharArray().length);

		if (text.trim().length() >= 1) {
			if (GeoLocationService.lat != 0.0 && GeoLocationService.lng != 0.0) {
				String searchWord = text;
				UTFEncoder encoder = new UTFEncoder();
				String link = "http://dilny.com/api/searchByText/title/"
						+ encoder.encod(searchWord) + "/lat/"
						+ GeoLocationService.lat + "/lng/"
						+ GeoLocationService.lng + "/radius/50/page/0/cat/";
				System.out.println(link);
				ConnectionController cc = new ConnectionController(link, c,
						ConnectionController.Con_TextSearch, true,
						getString(R.string.str_msg_waitting_for_search));
				cc.setOnConnectionDone(Act_Dilny.this);
				cc.startGetConnection();
			} else {
				Toast.makeText(c,
						c.getString(R.string.str_msg_waitting_for_loaction),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	Runnable enableRefreshBtn = new Runnable() {
		@Override
		public void run() {

			refreshBtn.setEnabled(true);
		}
	};

	@Override
	public void onClick(View v) {
		if (v == refreshBtn) {
			if (refreshBtn.isEnabled()) {
				pb.setVisibility(ProgressBar.VISIBLE);
				String text = "جاري تحديد المكان ..";
				tv_address.setText(text);
				GeoLocationService.loadAddress = true;
				mNearst.run();
				refreshBtn.setEnabled(false);
				handler.postDelayed(enableRefreshBtn, 5000);
			}

		} else if (v == btnInfo) {
			Intent n = new Intent(this, Act_ApoutApp.class);
			startActivity(n);

		} else if (v == btn_cats) {
			/*
			 * ConnectionController cc= new
			 * ConnectionController("http://dilny.com/api/getCats/0/0", this,
			 * ConnectionController
			 * .Con_Cats,true,getString(R.string.str_loading));
			 * cc.setOnConnectionDone(this);
			 */

			Intent N = new Intent(this, Act_Category.class);
			startActivity(N);

		} else if (v == btn_favorites) {
			if (isSiginedIn()) {
				ExportDB.extract("some_folder", "images.db",
						"/data/data/com.ksa.dilny/databases/images.db");
				if (loadFavoStatus) {
					Intent N = new Intent(this, Act_Favorites.class);
					startActivity(N);
				} else {
					Toast.makeText(c,
							c.getString(R.string.str_location_loading),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(c, "عفواً , لم تقم بتسجيل الدخول بعد",
						Toast.LENGTH_SHORT).show();
			}

		} else if (v == btn_nearby) {
			ConnectionController cc = new ConnectionController(
					"http://dilny.com/api/searchNearby/lat/"
							+ GeoLocationService.lat + "/lng/"
							+ GeoLocationService.lng + "/radius/50/page/0/cat/",
					this, ConnectionController.Con_Nerby, true,
					getString(R.string.str_loading));
			cc.setOnConnectionDone(this);
			cc.startGetConnection();

		} else if (v == btn_new_lisitng) {
			if (isSiginedIn()) {
				if(GeoLocationService.Adress.toString() !=null){
					Intent N = new Intent(c, Act_AddNew.class);
					startActivity(N);
				}else{
					Toast.makeText(c, "جاري جلب إحداثيات المكان",
							Toast.LENGTH_SHORT).show();
				}
				
			} else {
				Toast.makeText(c, "عفواً , لم تقم بتسجيل الدخول بعد",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private boolean isSiginedIn() {
		if (DataHolder.User_ID != null) {
			return true;
		} else {
			return false;
		}

	}

	Runnable mNearst = new Runnable() {
		@Override
		public void run() {
			if (pb.VISIBLE == ProgressBar.VISIBLE) {
				String text = tv_address.getText().toString();
				if (text.equals("جاري تحديد المكان ..")) {
					tv_address.setText("جاري تحديد المكان ...");
				} else if (text.equals("جاري تحديد المكان ...")) {
					tv_address.setText("جاري تحديد المكان");
				} else if (text.equals("جاري تحديد المكان")) {
					tv_address.setText("جاري تحديد المكان .");
				} else if (text.equals("جاري تحديد المكان .")) {
					tv_address.setText("جاري تحديد المكان ..");
				}
			}
			if (GeoLocationService.lat != 0.0) {
				btn_nearby.setEnabled(true);
				if (!loadFavoStatus) {
					if (isSiginedIn()) {
						loadFavorites();
					}

				}
			}
			handler.postDelayed(mNearst, 500);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case 0:
				try {
					tv_address.setText(Adress);
					pb.setVisibility(ProgressBar.INVISIBLE);
				} catch (Exception e) {
					System.out.println("Act_Dilny : updateUi : error : +"
							+ e.getMessage());
				}
				break;
			case 10:
				Toast.makeText(c, c.getString(R.string.str_msg_limit_exeded),
						Toast.LENGTH_SHORT).show();
				break;
			case 11:
				Toast.makeText(c, c.getString(R.string.str_msg_no_results_Found),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};
	String Adress;

	@Override
	public void updateUi(String _Adress, int code) {
		if (code == 1) {
			Adress = GeoLocationService.FullAdress;
			handler.sendEmptyMessage(0);
		}
		if (code == 2) {
			handler.sendEmptyMessage(10);
		}
		if (code == 3) {
			Adress = GeoLocationService.FullAdress;
			handler.sendEmptyMessage(11);
		}  

	}

	@Override
	public void done(int code, String response) {
		// System.out.println("Res : "+response);
		if(code==ConnectionController.Con_Favorites){
			loadFavoStatus = true;
		}else		if (code == ConnectionController.Con_Cats) {
			DataHolder.res_cats = response;
			Intent N = new Intent(this, Act_Category.class);
			startActivity(N);
		} else if (code == ConnectionController.Con_Nerby) {
			Intent N = new Intent(this, Act_SearchResult.class);
			N.putExtra("response", response);
			N.putExtra("lat", GeoLocationService.lat);
			N.putExtra("lng", GeoLocationService.lng);
			N.putExtra("connection", code);
			startActivity(N);
		} else if (code == ConnectionController.Con_TextSearch) {
			System.out.println("response : " + response);
			if (response.equals("[]")) {
				Toast.makeText(c,
						c.getString(R.string.str_msg_search_not_found),
						Toast.LENGTH_SHORT).show();
			} else {
				Intent N = new Intent(this, Act_SearchResult.class);
				N.putExtra("response", response);
				N.putExtra("connection", code);
				N.putExtra("searchWord", search.getText().toString());
				startActivity(N);
			}
		}

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.getTitle() == "Sigin") {
			showSiginDialog();
		} else if (item.getTitle() == "Out") {
			DataHolder.setData(this, null, null, null, false);
			Intent n = new Intent(this, Act_SingIn.class);
			finish();
			startActivity(n);
		} else if (item.getTitle() == "Map") {
			if (GeoLocationService.lat != 0.0 && GeoLocationService.lng != 0.0) {
				Intent N = new Intent(this, Act_Map.class);
				startActivity(N);
			} else {
				Toast.makeText(c,
						c.getString(R.string.str_msg_waitting_for_loaction),
						Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}

	private void showSiginDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("أهلاً بك , هل لديك حساب , أم انك مستخدم جديد ؟!");
		builder.setPositiveButton("تسجيل الدخول",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startActivity(new Intent(c, Act_SingIn.class));
						finish();
					}
				});
		builder.setNegativeButton("تسجيل عضوية جديدة",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startActivity(new Intent(c, Act_SingUp.class));
						finish();
					}
				});
		// Create the AlertDialog object and return it
		Dialog dialog = builder.create();
		dialog.show();
	}

	public boolean checkGpsEnabled() {
		LocationManager locationManager = (LocationManager) c
				.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public void checkGPS() {
		if (!checkGpsEnabled()) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(c);
			dialog.setTitle("رسالة إرشادية");
			dialog.setMessage(" : الرجاء تفعيل خدمة المكان");
			dialog.setPositiveButton("تفعيل",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent n = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(n);

						}

					});

			dialog.show();

		}
	}

	@Override
	protected void onResume() {
		Log.d("Act_Dilny", "onResume");
		if (geoLocation != null) {
			if (geoLocation.checkService()) {
				GeoLocationService.loadAddress = true;
				geoLocation.setTheListener(this);
				geoLocation.startService();
			}

		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d("Act_Dilny", "onPause");
		/*
		 * geoLocation.setTheListener(null); geoLocation.stopService();
		 */
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		geoLocation.setTheListener(null);
		geoLocation.stopService();
		super.onDestroy();
	}

}