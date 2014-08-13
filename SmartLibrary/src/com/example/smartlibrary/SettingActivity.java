package com.example.smartlibrary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smartlibrary.bluetooth.BluetoothService;

public class SettingActivity extends Activity implements OnClickListener {

	LogBean logbean = new LogBean();
	String userid = null;
	InputStream is = null;
	String result = null;
	String line = null;
	String userId = null;
	String passwd = null;
	EditText id;
	EditText pw;
	boolean end = false;
	Button btnenroll;
	// 애플리케이션 전체에서 사용할 사용자 아이디값

	Button btnlogin;
	private ProgressDialog mLoagindDialog; // Loading Dialog
	private Context mContext;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;


	private BluetoothService btService = null;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;

	
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.activity_setting);

		Button btnsearch = (Button) findViewById(R.id.bt_home);
		Button btnbookinfo = (Button) findViewById(R.id.bt_book);
		//Button btnpersoninfo = (Button) findViewById(R.id.bt_lock);
	    btnenroll = (Button) findViewById(R.id.bt_enroll);
		Button btnbluetooth = (Button) findViewById(R.id.bt_bluetooth);
		btnlogin = (Button) findViewById(R.id.bt_login);
		Button btnjoin = (Button) findViewById(R.id.bt_join);

		btnsearch.setOnClickListener(this);
		btnbookinfo.setOnClickListener(this);
		//btnpersoninfo.setOnClickListener(this);
		btnenroll.setOnClickListener(this);
		btnbluetooth.setOnClickListener(this);
		btnlogin.setOnClickListener(this);
		btnjoin.setOnClickListener(this);

		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		String findId = sharedPref.getString("id", "");
		if(findId.equals("0001"))
		{
			//관리자 모드에서 보여질 버튼
			btnenroll.setVisibility(View.VISIBLE);
			
		}
		else
		{
			btnenroll.setVisibility(View.INVISIBLE);
		}
		
		if (sharedPref.getString("id", "") != "") {
			btnlogin.setText("Logout");
		}
		
		if (btService == null) {
			btService = new BluetoothService(this, mHandler);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_home:
			Intent intent_search = new Intent();
			intent_search.setClass(SettingActivity.this, TabMenuActivity.class);

			Log.d("kh", "setting home button ");
			startActivity(intent_search);
			finish();
			break;
		case R.id.bt_book:
			String userid = sharedPref.getString("id", "");
			if(userid.equals(""))
			{
				Toast toast = Toast.makeText(getApplicationContext(),
						"사용자 정보가 없습니다.", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			}
			else
			{
				Intent intent_book = new Intent();
				intent_book.setClass(SettingActivity.this, ActivityBorrow.class);

				Log.d("kh", "setting book button ");
				startActivity(intent_book);
			}
			
			break;
//		case R.id.bt_lock:
//			Intent intent_person = new Intent();
//			intent_person.setClass(SettingActivity.this, ActivityChunggu.class);
//			Log.d("kh", "setting personinfo button ");
//			startActivity(intent_person);
//			break;

		case R.id.bt_join:
			Log.d("kh", "setting join 1");
			Intent intent_join = new Intent();
			intent_join.setClass(SettingActivity.this, ActivityJoin.class);
			Log.d("kh", "setting join ");
			startActivity(intent_join);
			break;

		case R.id.bt_enroll:

			Intent intent_enroll = new Intent();
			intent_enroll.setClass(SettingActivity.this, BarcodeScan.class);
			Log.d("kh", "setting personinfo button ");
			startActivity(intent_enroll);

			break;

		case R.id.bt_bluetooth:
			Log.d("kh", "setting bluetooth ");

			if (btService.getDeviceState()) {
				// 블루투스가 지원 가능한 기기일 때
				btService.enableBluetooth();
			} else {
				finish();
			}
			// 여기서 데이터 수신하는 서비스 부분을 호출
			break;
		case R.id.bt_login:
			Log.d("kh", "setting login ");
			String sharedValue = sharedPref.getString("id", "");
			Log.d("kh", "userid : " + sharedValue);
			if (sharedValue == "") {
				Log.d("kh", "Login Button");
				showAlertLogin();
				
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"로그아웃되었습니다.", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
				sharedEditor.remove("id");
				sharedEditor.commit();
				btnlogin.setText("Login");
				String findId = sharedPref.getString("id", "");
				if(findId.equals("0001"))
				{
					//관리자 모드에서 보여질 버튼
					btnenroll.setVisibility(View.VISIBLE);
					
				}
				else
				{
					btnenroll.setVisibility(View.INVISIBLE);
				}
			}

			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			// Log.d("kh", "0");
			if (resultCode == RESULT_OK) {

				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
				Toast toast = Toast.makeText(this, "Content:" + contents
						+ " Format:" + format, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Toast toast = Toast.makeText(this, "Scan was Cancelled! T.T",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();

			}
		}
		Log.d("kh", "onActivityResult " + resultCode);

		switch (requestCode) {
		/** 추가된 부분 시작 **/
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// 블루투스가 활성화 되었다
				btService.getDeviceInfo(intent);
			}
			break;
		/** 추가된 부분 끝 **/

		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				btService.scanDevice();

			} else {

				Log.d("kh", "Bluetooth is not enabled");
			}
			break;
		}
	}

	private void showAlertLogin() {
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout loginLayout = (LinearLayout) vi.inflate(
				R.layout.logindialog, null);
		id = (EditText) loginLayout.findViewById(R.id.id);
		pw = (EditText) loginLayout.findViewById(R.id.pw);

		new AlertDialog.Builder(this).setTitle("로그인").setView(loginLayout)
				.setNeutralButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Log.d("kh", "userId : " + userId + "  id: "
								+ id.getText().toString());

						select();
					}
				}).show();
	}

	public String select() {

		try {
			return (new AsyncTask<String, String, String>() {

			
				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					// 여기서 데이터 전송
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("id", id
							.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("secret", pw
							.getText().toString()));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/memberlogin.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "UTF_8"), 8);
						StringBuilder sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						Log.d("kh", "result");
						result = sb.toString();
						Log.d("kh", result);
					} catch (Exception e) {
						Log.e("Fail 2", e.toString());

					}

					try {
						Log.d("kh", "1");
						JSONObject json_data = null;
						json_data = new JSONObject(result);
						Log.d("kh", "1.5"); // 여기는 됨
						JSONArray bkName = json_data.getJSONArray("results");
						for (int i = 0; i < bkName.length(); i++) {
							Log.d("kh", "i " + i);
							JSONObject jo = bkName.getJSONObject(i);
							userId = jo.getString("id");
							passwd = jo.getString("secret");
						}

						return userId + "/" + passwd;

					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}

					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;

					// 여기서 받은 데이터 처리

					if (userId.equals(id.getText().toString())) {

						// 여기서 서버로 로그인 정보 전송

						if (!passwd.equals(pw.getText().toString())) {
							Toast.makeText(SettingActivity.this,
									"비밀번호가 잘못되었습니다.", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(SettingActivity.this,
									"ID : " + id.getText().toString(),
									Toast.LENGTH_LONG).show();

							sharedEditor.putString("id", id.getText()
									.toString());
							sharedEditor.commit();
							btnlogin.setText("Logout");
							Log.d("kh", "userId : " + userId + "  id: "
									+ id.getText().toString() + "  성공");
							
							if(id.getText().toString().equals("0001"))
							{
								//관리자 모드에서 보여질 버튼
								btnenroll.setVisibility(View.VISIBLE);
								
							}
							else
							{
								btnenroll.setVisibility(View.INVISIBLE);
							}
						}

					} else {
						Toast.makeText(SettingActivity.this,
								"회원 정보가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
						Log.d("kh", "  fail T.T");

					}
				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

}
