package com.example.smartlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.activity_setting);
		
		Button btnsearch = (Button)findViewById(R.id.bt_search);
		Button btnbookinfo = (Button)findViewById(R.id.bt_book);
		Button btnpersoninfo = (Button)findViewById(R.id.bt_lock);
		Button btnenroll = (Button)findViewById(R.id.bt_enroll);
		
		btnsearch.setOnClickListener(this);
		btnbookinfo.setOnClickListener(this);
		btnpersoninfo.setOnClickListener(this);
		btnenroll.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v){
		switch(v.getId())
		{
		case R.id.bt_search:
			Intent intent_search = new Intent();
			intent_search.setClass(SettingActivity.this, TabMenuActivity.class);
			
			Log.d("kh", "setting home button ");
			startActivity(intent_search);
			break;
		case R.id.bt_book:
			Intent intent_book = new Intent();
			intent_book.setClass(SettingActivity.this, SettingActivity.class);
			
			Log.d("kh", "setting book button ");
			startActivity(intent_book);
			break;
		case R.id.bt_lock:
			Intent intent_person = new Intent();
			intent_person.setClass(SettingActivity.this, SettingActivity.class);		
			Log.d("kh", "setting personinfo button ");
			startActivity(intent_person);
			break;
		
		case R.id.bt_enroll:
			Intent intent_enroll = new Intent();
			intent_enroll.setClass(SettingActivity.this, BarcodeScan.class);		
			Log.d("kh", "setting personinfo button ");
			startActivity(intent_enroll);
			break;
		case R.id.bt_bluetooth:
			Intent intent_bluetooth = new Intent();
			intent_bluetooth.setClass(SettingActivity.this, ActivityBluetooth.class);
			Log.d("kh", "setting bluetooth ");
			startActivity(intent_bluetooth);
			break;
		}
	}
}
