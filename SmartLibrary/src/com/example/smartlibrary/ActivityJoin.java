package com.example.smartlibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityJoin extends Activity {
	InputStream is = null;
	String line = null;
	String result = null;
	private final String urlPath = "http://112.108.40.87/memberjoin.php";
	// private SharedPreferences prvPref;
	private SharedPreferences sharedPref;
	// private SharedPreferences.Editor prvEditor;
	private SharedPreferences.Editor sharedEditor;

	EditText id;
	EditText pw;
	EditText name;
	EditText phone;
	LogBean bean = new LogBean();
	String checkid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		Button btnjoin = (Button) findViewById(R.id.join);
		Button btncancle = (Button) findViewById(R.id.cancle);
		btnjoin.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String prvValue = id.getText().toString();
				if (prvValue.length() == 0) {
					Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", 1)
							.show();
				} else if (pw.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", 1)
							.show();

				} else if (name.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), "이름을 입력해주세요", 1)
							.show();
				} else if (phone.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요", 1)
							.show();
				} else {
					String sharedValue = id.getText().toString();
					// prvEditor.putString("id", prvValue);
					sharedEditor.putString("id", sharedValue);
					sharedEditor.commit();

					new HttpfindID().execute();
					// bean.setUserId(id.getText().toString());

				}

			}
		});

		btncancle.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_setting = new Intent();
				intent_setting.setClass(ActivityJoin.this, MainActivity.class);
				Log.d("kh", "setting home button ");
				startActivity(intent_setting);

			}
		});

		id = (EditText) findViewById(R.id.id);
		pw = (EditText) findViewById(R.id.pw);
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phone);

		// prvPref = getPreferences(Activity.MODE_PRIVATE);
		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

		// prvEditor = prvPref.edit();
		sharedEditor = sharedPref.edit();
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
			Toast toast = Toast.makeText(getApplicationContext(), id.getText()
					.toString() + "님 가입되었습니다!", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
			Intent intent_settin = new Intent();
			intent_settin.setClass(ActivityJoin.this, MainActivity.class);
			Log.d("kh", "setting home button ");
			startActivity(intent_settin);
			finish();
		}

	}
/*
	class HttpID extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {

				HttpPost request = new HttpPost(urlPath);
				// 전달할 인자들
				Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
				nameValue.add(new BasicNameValuePair("id", id.getText()
						.toString()));

				Log.d("kh", "ID : " + id.getText().toString());

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
*/
	
	/*
	class HttpTasks extends AsyncTask<Void, Void, String> {
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
			Toast.makeText(getApplicationContext(), "가입되었습니다.", 1)
			.show();
		}

	}
*/
	class HttpfindID extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", id.getText()
					.toString()));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://112.108.40.87/checkmemberenroll.php");
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
				String a = "";

				for (int i = 0; i < bkName.length(); i++) {
					Log.d("kh", "i " + i);
					JSONObject jo = bkName.getJSONObject(i);
					checkid = jo.getString("id");
					Log.d("kh","checkid : "+checkid);

				}

			} catch (Exception e) {
				Log.e("Fail 3", e.toString());
			}

			return result;
		}

		protected void onPostExecute(String value) {
			super.onPostExecute(value);
			// result.setText(value);
			if (result == null)
				return;
			// btpower=true;
			if (id.getText().toString().equals(checkid)) {
				Toast.makeText(getApplicationContext(), "이미 가입되었습니다.", 1)
						.show();
			} else {
				new HttpTask().execute();
			}
		}

	}

}
