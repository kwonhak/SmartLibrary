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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
	String userid = null;
	InputStream is = null;
	String result = null;
	String line = null;
	String userId=null;
	String passwd=null;
	EditText id;
	EditText pw;
	boolean end = false;
	// 애플리케이션 전체에서 사용할 사용자 아이디값
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;

	Button btnlogin;
	private ProgressDialog mLoagindDialog; // Loading Dialog
	private Context mContext;

	@Override
	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.activity_setting);

		Button btnsearch = (Button) findViewById(R.id.bt_search);
		Button btnbookinfo = (Button) findViewById(R.id.bt_book);
		Button btnpersoninfo = (Button) findViewById(R.id.bt_lock);
		Button btnenroll = (Button) findViewById(R.id.bt_enroll);
		Button btnbluetooth = (Button) findViewById(R.id.bt_bluetooth);
		btnlogin = (Button) findViewById(R.id.bt_login);
		Button btnjoin = (Button) findViewById(R.id.bt_join);

		btnsearch.setOnClickListener(this);
		btnbookinfo.setOnClickListener(this);
		btnpersoninfo.setOnClickListener(this);
		btnenroll.setOnClickListener(this);
		btnbluetooth.setOnClickListener(this);
		btnlogin.setOnClickListener(this);
		btnjoin.setOnClickListener(this);

		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		if (sharedPref.getString("id", "") != "") {
			btnlogin.setText("Logout");
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			Log.d("kh", "setting join 1");
			Intent intent_join = new Intent();
			intent_join.setClass(SettingActivity.this, ActivityJoin.class);
			Log.d("kh", "setting join ");
			startActivity(intent_join);
			break;

		case R.id.bt_enroll:
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.setPackage("com.google.zxing.client.android");
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			try {
				startActivityForResult(intent, 0);
			} catch (ActivityNotFoundException e) {
				Log.d("kh", e.toString());
				// downloadFromMarket();
			}

			break;

		case R.id.bt_bluetooth:
			Log.d("kh", "setting bluetooth ");
			Intent intent_bluetooth = new Intent();
			intent_bluetooth.setClass(SettingActivity.this,
					ActivityBluetooth.class);
			startActivity(intent_bluetooth);
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
			}

			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
//			Log.d("kh", "0");
			if (resultCode == RESULT_OK) {
	//			Log.d("kh", "1");
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
//				Log.d("kh", "2");
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
						
						//while(!end){}
//						
//						if (userId.equals(id.getText().toString())) {
//
//							// 여기서 서버로 로그인 정보 전송
//							
//							if(!passwd.equals(pw.getText().toString()))
//							{
//								Toast.makeText(SettingActivity.this,
//										"비밀번호가 잘못되었습니다.", Toast.LENGTH_LONG)
//										.show();							
//							}
//							else
//							{
//								Toast.makeText(SettingActivity.this,
//								"ID : " + id.getText().toString(),
//								Toast.LENGTH_LONG).show();
//
//								sharedEditor.putString("id", id.getText()
//										.toString());
//								sharedEditor.commit();
//								btnlogin.setText("Logout");
//								Log.d("kh", "userId : " + userId + "  id: "
//										+ id.getText().toString() + "  성공");
//							}
//
//							end = false;
//						} else {
//							Toast.makeText(SettingActivity.this,
//									"회원 정보가 올바르지 않습니다.", Toast.LENGTH_LONG)
//									.show();
//							Log.d("kh", "  fail T.T");
//							end = false;
//						}

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
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("id", id.getText()
							.toString()));
					nameValuePairs.add(new BasicNameValuePair("secret", pw.getText()
							.toString()));
	
					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/memberlogin.php");
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
								HTTP.UTF_8));
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
						return ;
					
					
					if (userId.equals(id.getText().toString())) {

						// 여기서 서버로 로그인 정보 전송
						
						if(!passwd.equals(pw.getText().toString()))
						{
							Toast.makeText(SettingActivity.this,
									"비밀번호가 잘못되었습니다.", Toast.LENGTH_LONG)
									.show();							
						}
						else
						{
							Toast.makeText(SettingActivity.this,
							"ID : " + id.getText().toString(),
							Toast.LENGTH_LONG).show();

							sharedEditor.putString("id", id.getText()
									.toString());
							sharedEditor.commit();
							btnlogin.setText("Logout");
							Log.d("kh", "userId : " + userId + "  id: "
									+ id.getText().toString() + "  성공");
						}

						end = false;
					} else {
						Toast.makeText(SettingActivity.this,
								"회원 정보가 올바르지 않습니다.", Toast.LENGTH_LONG)
								.show();
						Log.d("kh", "  fail T.T");
						end = false;
					}
				}
			}.execute("")).get();
			
		} catch (Exception e) {
			return null;
		}
		
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//
//				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				nameValuePairs.add(new BasicNameValuePair("id", id.getText()
//						.toString()));
//				nameValuePairs.add(new BasicNameValuePair("secret", pw.getText()
//						.toString()));
//
//				try {
//					HttpClient httpclient = new DefaultHttpClient();
//					HttpPost httppost = new HttpPost(
//							"http://112.108.40.87/memberlogin.php");
//					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
//							HTTP.UTF_8));
//					HttpResponse response = httpclient.execute(httppost);
//					HttpEntity entity = response.getEntity();
//					is = entity.getContent();
//					 Log.d("kh", "connection success");
//
//				} catch (Exception e) {
//					Log.e("Fail 1", e.toString());
//				}
//
//				try {
//					BufferedReader reader = new BufferedReader(
//							new InputStreamReader(is, "UTF_8"), 8);
//					StringBuilder sb = new StringBuilder();
//
//					while ((line = reader.readLine()) != null) {
//						sb.append(line + "\n");
//					}
//					is.close();
//					Log.d("kh", "result");
//					result = sb.toString();
//					Log.d("kh", result);
//				} catch (Exception e) {
//					Log.e("Fail 2", e.toString());
//
//				}
//
//				try {
//					Log.d("kh", "1");
//					JSONObject json_data = null;
//					json_data = new JSONObject(result);
//					Log.d("kh", "1.5"); // 여기는 됨
//					JSONArray bkName = json_data.getJSONArray("results");
//					for (int i = 0; i < bkName.length(); i++) {
//						Log.d("kh", "i " + i);
//						JSONObject jo = bkName.getJSONObject(i);
//						userId = jo.getString("id");
//						passwd = jo.getString("secret");
//					}
//
//				} catch (Exception e) {
//					Log.e("Fail 3", e.toString());
//				}
//				end = true;
//			}
//
//		}).start();

	}

}
