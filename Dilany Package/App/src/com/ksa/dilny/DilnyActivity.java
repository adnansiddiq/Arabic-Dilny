package com.ksa.dilny;

import java.io.File;

import libs.db.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;

import com.ksa.dilny.utils.ConnectionController;
import com.ksa.dilny.utils.ConnectionController.onConnectionDoneCC;
import com.ksa.dilny.utils.DataHolder;
import com.yahia.libs.SdCard;

public class DilnyActivity extends Activity implements onConnectionDoneCC {
	/** Called when the activity is first created. */
	static final String ActiveInternet = "Active Internet";
	static final String ActivatedInternet = "Internet is Activated";

	static final String ActiveGPS = "Active GPS";
	static final String ActivatedGPS = "GPS is Activated";
	public static String tmpImageDir;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		DBAdapter dba = new DBAdapter(this);
		// Remove System Bar

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		DataHolder.getData(this);

		// CheckCatsStatus();
		// always load Cats

		tmpImageDir = Environment.getExternalStorageDirectory().toString()
				+ "/." + this.getApplicationInfo().packageName + ".tempImage/";
		if (SdCard.checkExternalStorageState()) {
			File mainDir = new File(tmpImageDir);
			if (!mainDir.exists()) {
				mainDir.mkdirs();
			}
		}

		ConnectionController cc = new ConnectionController(
				"http://dilny.com/api/initApp", this,
				ConnectionController.Con_initApp, false, null);
		cc.setOnConnectionDone(this);
		cc.startGetConnection();

	}

	public void openApp() {
		System.out.println("openApp()");
		Intent N = new Intent(this, Act_Dilny.class);
		//Intent N = new Intent(this, Act_SingIn.class);
		startActivity(N);

	}

	@Override
	public void done(int code, String response) {
		openApp();

	}

	// get Cats
	public void CheckCatsStatus() {
		if (DataHolder.getCatsFromDB(this) == null) {
			System.out.println("DataHolder.getCatsFromDB(this) == null");
			ConnectionController cc = new ConnectionController(
					"http://dilny.com/api/getCats/0/0", this,
					ConnectionController.Con_Cats_data_only, false, null);
			cc.setOnConnectionDone(this);
		} else {
			openApp();
		}
	}

}
