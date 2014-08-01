package com.example.smartlibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityJoin extends Activity implements OnClickListener {
	InputStream is = null;
	String line = null;
	String result = null;
	private final String urlPath = "http://112.108.40.87/memberjoin.php";
//	private SharedPreferences prvPref;
    private SharedPreferences sharedPref;
 //   private SharedPreferences.Editor prvEditor;
    private SharedPreferences.Editor sharedEditor;

	EditText id;
	EditText pw;
	EditText name;
	EditText phone;
	LogBean bean = new LogBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		Button btnjoin = (Button) findViewById(R.id.join);
		Button btncancle = (Button) findViewById(R.id.cancle);

		id = (EditText) findViewById(R.id.id);
		pw = (EditText) findViewById(R.id.pw);
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phone);

		btnjoin.setOnClickListener(this);
		btncancle.setOnClickListener(this);
		
//		prvPref = getPreferences(Activity.MODE_PRIVATE);
        sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
         
 //       prvEditor = prvPref.edit();
        sharedEditor = sharedPref.edit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			Intent intent_setting = new Intent();
			intent_setting.setClass(ActivityJoin.this, SettingActivity.class);
			Log.d("kh", "setting home button ");
			startActivity(intent_setting);
			break;
		case R.id.join:
			// select(id.getText().toString(), pw.getText().toString(),
			// name.getText().toString(), phone.getText().toString());
			 String prvValue = id.getText().toString();
             String sharedValue = id.getText().toString();
 //            prvEditor.putString("id", prvValue);
             sharedEditor.putString("id", sharedValue);
             sharedEditor.commit();
			
			new HttpTask().execute();
			//bean.setUserId(id.getText().toString());
			
			Toast toast = Toast.makeText(getApplicationContext(), prvValue+"님 가입되었습니다!",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
			Intent intent_settin = new Intent();
			intent_settin.setClass(ActivityJoin.this, SettingActivity.class);
			Log.d("kh", "setting home button ");
			startActivity(intent_settin);
			finish();
			
			break;

		}
	}

	class HttpTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {

			
				HttpPost request = new HttpPost(urlPath);
				// 전달할 인자들
				Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
				nameValue.add(new BasicNameValuePair("id", id.getText()
						.toString()));
				
				Log.d("kh", "ID : " + id.getText().toString());
				nameValue.add(new BasicNameValuePair("secret", pw.getText()
						.toString()));
				nameValue.add(new BasicNameValuePair("name", name.getText()
						.toString()));
				nameValue.add(new BasicNameValuePair("phone", phone.getText()
						.toString()));

				// 웹 접속 - utf-8 방식으로
				UrlEncodedFormEntity enty = new UrlEncodedFormEntity(nameValue,
						HTTP.UTF_8);
				request.setEntity(enty);

				HttpClient client = new DefaultHttpClient();
				HttpResponse res = client.execute(request);
				// 웹 서버에서 값받기
				HttpEntity entityResponse = res.getEntity();
				InputStream im = entityResponse.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(im, HTTP.UTF_8));

				String total = "";
				String tmp = "";
				// 버퍼에있는거 전부 더해주기
				// readLine -> 파일내용을 줄 단위로 읽기
				while ((tmp = reader.readLine()) != null) {
					if (tmp != null) {
						total += tmp;
					}
				}
				im.close();
				// 결과창뿌려주기 - ui 변경시 에러
				// result.setText(total);

				return total;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 오류시 null 반환
			return null;
		}

		protected void onPostExecute(String value) {
			super.onPostExecute(value);
			// result.setText(value);
		}

	}

}
