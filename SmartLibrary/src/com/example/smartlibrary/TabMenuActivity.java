package com.example.smartlibrary;

import com.google.android.gcm.GCMRegistrar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TabMenuActivity extends FragmentActivity implements
		OnClickListener {

	final String TAG = "MainActivity";

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_ONE = 0;
	public final static int FRAGMENT_TWO = 1;
	public final static int FRAGMENT_THREE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tab_menu);

		Button bt_oneFragment = (Button) findViewById(R.id.bt_oneFragment);
		bt_oneFragment.setOnClickListener(this);
		Button bt_twoFragment = (Button) findViewById(R.id.bt_twoFragment);
		bt_twoFragment.setOnClickListener(this);
		Button bt_threeFragment = (Button) findViewById(R.id.bt_threeFragment);
		bt_threeFragment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent_search = new Intent();
				intent_search.setClass(TabMenuActivity.this,
						SettingActivity.class);

				Log.d("kh", "setting button ");
				startActivity(intent_search);

			}
		});

		mCurrentFragmentIndex = FRAGMENT_ONE;

		fragmentReplace(mCurrentFragmentIndex);

//		registerGcm();
	}

	public void fragmentReplace(int reqNewFragmentIndex) {

		android.support.v4.app.Fragment newFragment = null;

		Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);

		newFragment = getFragment(reqNewFragmentIndex);

		// replace fragment
		final android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.ll_fragment, newFragment);

		// Commit the transaction
		transaction.commit();

	}

	private android.support.v4.app.Fragment getFragment(int idx) {
		android.support.v4.app.Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_ONE:
			newFragment = new SearchViewerActivity();
			break;
		case FRAGMENT_TWO:
			newFragment = new SearchViewerActivity();
			Log.d("kh", "프래그먼트2");
			break;
		// case FRAGMENT_THREE:
		// newFragment = new SettingActivity();
		// Log.d("kh","프래그먼트3");
		// break;

		default:
			Log.d(TAG, "Unhandle case");
			break;
		}

		return newFragment;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		}

	}

//	public void registerGcm() {
//
//		GCMRegistrar.checkDevice(this);
//		GCMRegistrar.checkManifest(this);
//
//		final String regId = GCMRegistrar.getRegistrationId(this);
//
//		if (regId.equals("")) {
//			GCMRegistrar.register(this, "savvy-kit-671");
//		} else {
//			Log.e("id", regId);
//		}
//
//	}

}
