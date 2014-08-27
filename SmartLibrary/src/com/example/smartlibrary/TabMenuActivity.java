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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.smartlibrary.gcm.PreferenceUtil;

public class TabMenuActivity extends FragmentActivity implements
		OnClickListener {
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;
	public static String SENDER_ID = "940516598130";
	final String TAG = "MainActivity";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	// private static final String SENDER_ID = "388136674604";
	InputStream is = null;
	String result = null;
	String line = null;

	private GoogleCloudMessaging _gcm;
	private String _regId = "";
	 private Context context;
	 String messagesender;
	 String message;

	int mCurrentFragmentIndex;
	public final static int FRAGMENT_ONE = 0;
	public final static int FRAGMENT_TWO = 1;
	public final static int FRAGMENT_THREE = 2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tab_menu);
		
		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		
		messagesender = sharedPref.getString("msgsender", "");
		Intent intent = getIntent();
		message = intent.getStringExtra("msg");
		String gcmmyid = sharedPref.getString("gcmid", "");
		
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
		


		
		
	
	    
	    // google play service가 사용가능한가
	    if (checkPlayServices())
	    {
	      _gcm = GoogleCloudMessaging.getInstance(this);
	      _regId = getRegistrationId();
	     
	      if (TextUtils.isEmpty(_regId))
	      {
	    	  
	        registerInBackground();
	      }
	    }
	    else
	    {
	      Log.i("TabMenuActivity.java | onCreate", "|No valid Google Play Services APK found.|");
	     // _textStatus.append("\n No valid Google Play Services APK found.\n");
	    }
	    
	    // display received msg
	    String msg = getIntent().getStringExtra("msg");
	    if (!TextUtils.isEmpty(msg))
	    {
	    	Log.d("kh", "Textutils msg: "+msg);
//	    	sharedEditor.putString("gcmid",msg);
//			sharedEditor.commit();
	    }


	
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
	
	
	//gcm
	

	  @Override
	  protected void onNewIntent(Intent intent)
	  {
	    super.onNewIntent(intent);
	    
	    // display received msg
	    String msg = intent.getStringExtra("msg");
	    Log.i("TabMenuActivity.java | onNewIntent", "|" + msg + "|");
	    if (!TextUtils.isEmpty(msg))
	    	Log.d("kh", "msg: "+msg);
	     // _textStatus.append("\n" + msg + "\n");
	  }
	  
	  
	  // google play service가 사용가능한가
	  public boolean checkPlayServices()
	  {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS)
	    {
	      if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
	      {
	        GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
	      }
	      else
	      {
	        Log.i("TabMenuActivity.java | checkPlayService", "|This device is not supported.|");
	      //  _textStatus.append("\n This device is not supported.\n");
	        finish();
	      }
	      return false;
	    }
	    return true;
	  }
	  
	  
	  // registration  id를 가져온다.
	  public String getRegistrationId()
	  {
	    String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
	    if (TextUtils.isEmpty(registrationId))
	    {
	      Log.i("TabMenuActivity.java | getRegistrationId", "|Registration not found.|");
	     // _textStatus.append("\n Registration not found.\n");
	      return "";
	    }
	    int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
	    int currentVersion = getAppVersion();
	    if (registeredVersion != currentVersion)
	    {
	      Log.i("TabMenuActivity.java | getRegistrationId", "|App version changed.|");
	     // _textStatus.append("\n App version changed.\n");
	      return "";
	    }
	    return registrationId;
	  }
	  
	  
	  // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
	  public int getAppVersion()
	  {
	    try
	    {
	      PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
	      return packageInfo.versionCode;
	    }
	    catch (NameNotFoundException e)
	    {
	      // should never happen
	      throw new RuntimeException("Could not get package name: " + e);
	    }
	  }
	  
	  
	  // gcm 서버에 접속해서 registration id를 발급받는다.
	  public void registerInBackground()
	  {
	    new AsyncTask<Void, Void, String>()
	    {
	      @Override
	      protected String doInBackground(Void... params)
	      {
	        String msg = "";
	        try
	        {
	          if (_gcm == null)
	          {
	        	  Log.d("kh", "1");
	            _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
	          }
	          Log.d("kh", "2");
	          _regId = _gcm.register(SENDER_ID);
	          msg = "Device registered, registration ID=" + _regId;
	          Log.d("kh", "ID: "+_regId);
	          sharedEditor.putString("gcmid",_regId);
			  sharedEditor.commit();
	          
	          // For this demo: we don't need to send it because the device
	          // will send upstream messages to a server that echo back the
	          // message using the 'from' address in the message.
	          
	          // Persist the regID - no need to register again.
	          
	          storeRegistrationId(_regId);
	        }
	        catch (IOException ex)
	        {
	          msg = "Error :" + ex.getMessage();
	          // If there is an error, don't just keep trying to register.
	          // Require the user to click a button again, or perform
	          // exponential back-off.
	        }
	        
	        return msg;
	      }
	      
	      
	      @Override
	      protected void onPostExecute(String msg)
	      {
	        Log.i("TabMenuActivity.java | onPostExecute", "|" + msg + "|");
	        Log.d("kh", "onPostExecute msg: "+msg);
	      //  _textStatus.append(msg);
	      }
	    }.execute(null, null, null);
	  }
	  
	  
	  // registraion id를 preference에 저장한다.
	  public void storeRegistrationId(String regId)
	  {
		  Log.d("kh", "store");
	    int appVersion = getAppVersion();
	    Log.i("TabMenuActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
	    PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
	    PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
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
//								isbn = (jo.getString("isbn"));
//								student = jo.getString("student");
//								title = (jo.getString("title"));
//								startdate = jo.getString("startdate");
//								card = jo.getString("card");
//								enddate = jo.getString("enddate");
//
//								extension = jo.getString("extension");
								Log.d("kh", "ok");
								
//								dataList.add(new BorrowInfo(getApplicationContext(),
//									card, student, isbn, startdate, enddate,
//										extension, title));
								
		
							}
							
							

//							return isbn;

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
//						adapter.clear();
//						adapter.addAll(dataList);
//						adapter.notifyDataSetChanged();

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
		        case KeyEvent.KEYCODE_BACK :
		            AlertDialog.Builder alt_bld = new AlertDialog.Builder(TabMenuActivity.this);
		            alt_bld.setMessage("종료하시겠습니까?").setCancelable(false).setPositiveButton("예",
		                new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) 
		                { 
		                    dialog.cancel();
		                    finish();
		                }
		                }).setNegativeButton("아니오",
		                new DialogInterface.OnClickListener() 
		                {
		                public void onClick(DialogInterface dialog, int id) 
		                {
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

}
