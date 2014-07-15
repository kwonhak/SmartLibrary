package com.example.smartlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity  {

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.activity_setting);
		
		Button btnsearch = (Button)findViewById(R.id.bt_search);
		Button btnbookinfo = (Button)findViewById(R.id.bt_book);
		Button btnpersoninfo = (Button)findViewById(R.id.bt_lock);
		
		btnsearch.setOnClickListener(this);
		btnbookinfo.setOnClickListener(this);
		btnpersoninfo.setOnClickListener(this);
		
		public void onClick(View v){
			switch(v.getId())
			{
			case R.id.bt_search:
				break;
			case R.id.bt_book:
				break;
			case R.id.bt_lock:
				break;
			
			}
		}
		

	}
}
