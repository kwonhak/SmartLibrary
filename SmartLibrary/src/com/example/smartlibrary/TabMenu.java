package com.example.smartlibrary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TabMenu extends FragmentActivity implements OnClickListener {

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
		bt_threeFragment.setOnClickListener(this);

		mCurrentFragmentIndex = FRAGMENT_ONE; 

		fragmentReplace(mCurrentFragmentIndex);
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
			break;
		case FRAGMENT_THREE:
			newFragment = new SettingActivity();
			break;

		default:
			Log.d(TAG, "Unhandle case");
			break;
		}

		return newFragment;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_oneFragment:
			mCurrentFragmentIndex = FRAGMENT_ONE;
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.bt_twoFragment:
			mCurrentFragmentIndex = FRAGMENT_TWO;
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.bt_threeFragment:
			mCurrentFragmentIndex = FRAGMENT_THREE;
			fragmentReplace(mCurrentFragmentIndex);
			break;

		}

	}


}
