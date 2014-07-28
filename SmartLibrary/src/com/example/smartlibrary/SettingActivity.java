package com.example.smartlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener {

	LogBean logbean = new LogBean();
	String userid=null;
	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.activity_setting);
		
		Button btnsearch = (Button)findViewById(R.id.bt_search);
		Button btnbookinfo = (Button)findViewById(R.id.bt_book);
		Button btnpersoninfo = (Button)findViewById(R.id.bt_lock);
		Button btnenroll = (Button)findViewById(R.id.bt_enroll);
		Button btnbluetooth = (Button)findViewById(R.id.bt_bluetooth);
		Button btnlogin = (Button)findViewById(R.id.bt_login);
		Button btnjoin = (Button)findViewById(R.id.bt_join);
		
		btnsearch.setOnClickListener(this);
		btnbookinfo.setOnClickListener(this);
		btnpersoninfo.setOnClickListener(this);
		btnenroll.setOnClickListener(this);
		btnbluetooth.setOnClickListener(this);
		btnlogin.setOnClickListener(this);
		btnjoin.setOnClickListener(this);


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
			
		case R.id.bt_join:
			Log.d("kh","userid : "+logbean.getUserId());
			if(logbean.getUserId()==null)
			{
				Log.d("kh", "setting join 1");
				Intent intent_join = new Intent();
				intent_join.setClass(SettingActivity.this, ActivityJoin.class);		
				Log.d("kh", "setting join ");
				startActivity(intent_join);
			}
			else
			{
				Toast toast = Toast.makeText(getApplicationContext(), "로그인 되어있습니다.",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			}
			
			break;	
			
		case R.id.bt_enroll:			
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage("com.google.zxing.client.android");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");

            try{
            startActivityForResult(intent, 0);
            }
            catch(ActivityNotFoundException e){
            	Log.d("___", e.toString());
            	// downloadFromMarket();
            }
			break;
		case R.id.bt_bluetooth:
			Log.d("kh", "setting bluetooth ");
			Intent intent_bluetooth = new Intent();
			intent_bluetooth.setClass(SettingActivity.this, ActivityBluetooth.class);	
			startActivity(intent_bluetooth);
			break;
		case R.id.bt_login:
			Log.d("kh", "setting login ");
			showAlertLogin();
			break;
		}
	}
	
	
	  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	        if (requestCode == 0) {
	        	 Log.d("kh", "0");
	            if (resultCode == RESULT_OK) {
	                Log.d("kh", "1");
	                String contents = intent.getStringExtra("SCAN_RESULT");
	                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	                // Handle successful scan
	                Log.d("kh", "2");
	                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format , Toast.LENGTH_LONG);
	                toast.setGravity(Gravity.TOP, 25, 400);
	                toast.show();
	            } else if (resultCode == RESULT_CANCELED) {
	                // Handle cancel
	                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
	                toast.setGravity(Gravity.TOP, 25, 400);
	                toast.show();
	            
	                
	            }
	        }
	    }
	
	private void showAlertLogin()
	{
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.logindialog, null);
	final EditText id = (EditText) loginLayout.findViewById(R.id.id);
	final EditText pw = (EditText) loginLayout.findViewById(R.id.pw);
	
	new AlertDialog.Builder(this).setTitle("로그인").setView(loginLayout).setNeutralButton("확인", new DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int which){
			Toast.makeText(
					SettingActivity.this,"ID : "+id.getText().toString()+"\nPW : "+pw.getText().toString(),Toast.LENGTH_LONG).show();
			//여기서 서버로 로그인 정보 전송
			logbean.setUserId( id.getText().toString());
			
		}
	}).show();
	}
}
