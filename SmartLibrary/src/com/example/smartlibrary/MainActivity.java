package com.example.smartlibrary;

import java.io.BufferedReader;
import java.io.IOException;
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.smartlibrary.bluetooth.BluetoothService;
import com.smartlibrary.gcm.PreferenceUtil;

public class MainActivity extends Activity {

	private GoogleCloudMessaging _gcm;
	private String _regId = "";
	private Context context;
	String messagesender;
	String message;
	public static String SENDER_ID = "940516598130";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Button btnhome = (Button) findViewById(R.id.home);
		Button btnbookinfo = (Button) findViewById(R.id.borrow);
		Button btnsearch = (Button) findViewById(R.id.search);
		btnenroll = (Button) findViewById(R.id.enroll);
		Button btnbluetooth = (Button) findViewById(R.id.bluetooth);
		btnlogin = (Button) findViewById(R.id.login);
		Button btnjoin = (Button) findViewById(R.id.member);

		btnsearch.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_search = new Intent();
				intent_search.setClass(MainActivity.this, ActivitySearch.class);

				Log.d("kh", "list home button ");
				startActivity(intent_search);

			}
		});

		// btnbookinfo.setOnClickListener(new Button.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent_home = new Intent();
		// intent_home.setClass(MainActivity.this, TabMenuActivity.class);
		//
		// Log.d("kh", "list home button ");
		// startActivity(intent_home);
		//
		// }
		// });

		// 대출정보
		btnbookinfo.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sharedValue = sharedPref.getString("id", "");
				Log.d("kh", "userid : " + sharedValue);
				if (sharedValue == "") {
					// Log.d("kh", "Login Button");
					Toast toast = Toast.makeText(getApplicationContext(),
							"로그인이 필요합니다.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 25, 400);
					toast.show();

				} else {
					Intent intent_bkinfo = new Intent();
					intent_bkinfo.setClass(MainActivity.this,
							ActivityBorrow.class);
					startActivity(intent_bkinfo);
				}

			}
		});

		// 책 등록
		btnenroll.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_enroll = new Intent();
				intent_enroll.setClass(MainActivity.this, ZBarScan.class);
				Log.d("kh", "setting personinfo button ");
				startActivity(intent_enroll);

			}
		});

		// 블루투스
		btnbluetooth.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("kh", "setting bluetooth ");

				if (btService.getDeviceState()) {
					// 블루투스가 지원 가능한 기기일 때
					btService.enableBluetooth();
				} else {
					finish();
				}

			}
		});

		// 로그인
		btnlogin.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("kh", "setting login ");
				String sharedValue = sharedPref.getString("id", "");
				Log.d("kh", "userid : " + sharedValue);
				if (sharedValue == "") {
					Log.d("kh", "Login Button");
					showAlertLogin();

				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"로그아웃되었습니다.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 25, 400);
					toast.show();
					sharedEditor.remove("id");
					sharedEditor.commit();
					btnlogin.setBackgroundResource(R.drawable.mainlogin);
					String findId = sharedPref.getString("id", "");
					if (findId.equals("0001")) {
						// 관리자 모드에서 보여질 버튼
						btnenroll.setVisibility(View.VISIBLE);

					} else {
						btnenroll.setVisibility(View.INVISIBLE);
					}
				}

			}
		});

		// 회원가입
		btnjoin.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// startActivity(intent_search); Log.d("kh", "setting join 1");
				Intent intent_join = new Intent();
				intent_join.setClass(MainActivity.this, ActivityJoin.class);
				Log.d("kh", "setting join ");
				startActivity(intent_join);

			}
		});

		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		String findId = sharedPref.getString("id", "");
		if (findId.equals("0001")) {
			// 관리자 모드에서 보여질 버튼
			btnenroll.setVisibility(View.VISIBLE);

		} else {
			btnenroll.setVisibility(View.INVISIBLE);
		}

		if (sharedPref.getString("id", "") != "") {
			btnlogin.setBackgroundResource(R.drawable.mainlogout);
			// btnjoin.setVisibility(View.INVISIBLE);
		}

		if (btService == null) {
			btService = new BluetoothService(this, mHandler);
		}

		// google play service가 사용가능한가
		if (checkPlayServices()) {
			_gcm = GoogleCloudMessaging.getInstance(this);
			_regId = getRegistrationId();

			if (TextUtils.isEmpty(_regId)) {

				registerInBackground();
			}
		} else {
			Log.i("TabMenuActivity.java | onCreate",
					"|No valid Google Play Services APK found.|");
			// _textStatus.append("\n No valid Google Play Services APK found.\n");
		}

		// display received msg
		String msg = getIntent().getStringExtra("msg");
		if (!TextUtils.isEmpty(msg)) {
			Log.d("kh", "Textutils msg: " + msg);
			// sharedEditor.putString("gcmid",msg);
			// sharedEditor.commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);

		return true;

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

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("로그인")
				.setView(loginLayout)
				.setNeutralButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Log.d("kh", "확인 userId : " + userId + "  id: "
								+ id.getText().toString().length());
						if (id.getText().toString().length() == 0) {
							Toast.makeText(getApplicationContext(),
									"아이디를 입력해주세요", 1).show();
						} else if (pw.getText().toString().length() == 0) {
							Toast.makeText(getApplicationContext(),
									"비밀번호를 입력해주세요", 1).show();

						} else {

							select();
						}
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
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
					// String gcmid = sharedPref.getString("gcmid", "");
					// Log.d("kh", "gcm Id: "+gcmid);
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
							Toast.makeText(MainActivity.this, "비밀번호가 잘못되었습니다.",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(MainActivity.this,
									"ID : " + id.getText().toString(),
									Toast.LENGTH_LONG).show();

							sharedEditor.putString("id", id.getText()
									.toString());
							sharedEditor.commit();
							btnlogin.setBackgroundResource(R.drawable.mainlogout);
							Log.d("kh", "userId : " + userId + "  id: "
									+ id.getText().toString() + "  성공");

							if (id.getText().toString().equals("0001")) {
								// 관리자 모드에서 보여질 버튼
								btnenroll.setVisibility(View.VISIBLE);

							} else {
								btnenroll.setVisibility(View.INVISIBLE);
							}
						}

					} else {
						Toast.makeText(MainActivity.this, "회원 정보가 올바르지 않습니다.",
								Toast.LENGTH_LONG).show();
						Log.d("kh", "  fail T.T");

					}
				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(
					MainActivity.this);
			alt_bld.setMessage("종료하시겠습니까?")
					.setCancelable(false)
					.setPositiveButton("예",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									finish();
								}
							})
					.setNegativeButton("아니오",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Action for 'NO' Button
									dialog.cancel();
								}
							});
			AlertDialog alert = alt_bld.create();
			alert.show();

			break;

		}
		return super.onKeyDown(keyCode, event);
	}
	

	// gcm

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// display received msg
		String msg = intent.getStringExtra("msg");
		Log.i("TabMenuActivity.java | onNewIntent", "|" + msg + "|");
		if (!TextUtils.isEmpty(msg))
			Log.d("kh", "msg: " + msg);
		// _textStatus.append("\n" + msg + "\n");
	}

	// google play service가 사용가능한가
	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("TabMenuActivity.java | checkPlayService",
						"|This device is not supported.|");
				// _textStatus.append("\n This device is not supported.\n");
				finish();
			}
			return false;
		}
		return true;
	}

	// registration id를 가져온다.
	public String getRegistrationId() {
		String registrationId = PreferenceUtil
				.instance(getApplicationContext()).regId();
		if (TextUtils.isEmpty(registrationId)) {
			Log.i("TabMenuActivity.java | getRegistrationId",
					"|Registration not found.|");
			// _textStatus.append("\n Registration not found.\n");
			return "";
		}
		int registeredVersion = PreferenceUtil
				.instance(getApplicationContext()).appVersion();
		int currentVersion = getAppVersion();
		if (registeredVersion != currentVersion) {
			Log.i("TabMenuActivity.java | getRegistrationId",
					"|App version changed.|");
			// _textStatus.append("\n App version changed.\n");
			return "";
		}
		return registrationId;
	}

	// app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
	public int getAppVersion() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	// gcm 서버에 접속해서 registration id를 발급받는다.
	public void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (_gcm == null) {
						Log.d("kh", "1");
						_gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					Log.d("kh", "2");
					_regId = _gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + _regId;
					Log.d("kh", "ID: " + _regId);
					sharedEditor.putString("gcmid", _regId);
					sharedEditor.commit();

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.

					storeRegistrationId(_regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}

				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.i("TabMenuActivity.java | onPostExecute", "|" + msg + "|");
				Log.d("kh", "onPostExecute msg: " + msg);
				// _textStatus.append(msg);
			}
		}.execute(null, null, null);
	}

	// registraion id를 preference에 저장한다.
	public void storeRegistrationId(String regId) {
		Log.d("kh", "store");
		int appVersion = getAppVersion();
		Log.i("TabMenuActivity.java | storeRegistrationId", "|"
				+ "Saving regId on app version " + appVersion + "|");
		PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
		PreferenceUtil.instance(getApplicationContext()).putAppVersion(
				appVersion);
	}

	public String select(final String qtx) {

		try {
			return (new AsyncTask<String, String, String>() {

				ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("student", qtx));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/selectborrow.php");
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
							// isbn = (jo.getString("isbn"));
							// student = jo.getString("student");
							// title = (jo.getString("title"));
							// startdate = jo.getString("startdate");
							// card = jo.getString("card");
							// enddate = jo.getString("enddate");
							//
							// extension = jo.getString("extension");
							Log.d("kh", "ok");

							// dataList.add(new
							// BorrowInfo(getApplicationContext(),
							// card, student, isbn, startdate, enddate,
							// extension, title));

						}

						// return isbn;

					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}

					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					//
					// adapter.clear();
					// adapter.addAll(dataList);
					// adapter.notifyDataSetChanged();

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}


}