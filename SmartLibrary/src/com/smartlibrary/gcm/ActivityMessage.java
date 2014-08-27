package com.smartlibrary.gcm;

import java.io.InputStream;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.smartlibrary.R;
import com.example.smartlibrary.TabMenuActivity;

public class ActivityMessage extends Activity {
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;
	String messagesender;
	String message;

	InputStream is = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gcmmessage);
		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		messagesender = sharedPref.getString("msgsender", "");
		Intent intent = getIntent();
		message = intent.getStringExtra("msg");
		alert();

	}

	public void alert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 여기서
																		// this는
																		// Activity의
																		// this

		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("message")
				// 제목 설정
				.setMessage(message)
				// 메세지 설정
				.setCancelable(false)
				// 뒤로 버튼 클릭시 취소 가능 설정
				.setPositiveButton("답장", new DialogInterface.OnClickListener() {
					// 확인 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton) {
						showAlertSend();

					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					// 취소 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent_person = new Intent();
						intent_person.setClass(ActivityMessage.this,
								TabMenuActivity.class);
						startActivity(intent_person);
						finish();
					}
				});

		AlertDialog dialog = builder.create(); // 알림창 객체 생성
		dialog.show(); // 알림창 띄우기
	}

	private void showAlertSend() {
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout loginLayout = (LinearLayout) vi.inflate(
				R.layout.activity_message, null);
		final EditText message = (EditText) loginLayout
				.findViewById(R.id.message);

		new AlertDialog.Builder(this).setTitle("보낼 메시지").setView(loginLayout)
				.setNeutralButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 여기서 메시지랑 gcm아이디 보냄
						Log.d("kh", "확인버튼누름");
						sendmessage(message.getText().toString());

					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent_person = new Intent();
						intent_person.setClass(ActivityMessage.this,
								TabMenuActivity.class);
						startActivity(intent_person);
						finish();
						return;
					}
				}).show();
	}

	public String sendmessage(final String qtx) {

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

					String gcmid = sharedPref.getString("gcmid", "");
					nameValuePairs.add(new BasicNameValuePair("gcmid",
							messagesender));
					nameValuePairs.add(new BasicNameValuePair("msg", qtx + "@"
							+ gcmid));
					Log.d("kh", "message : " + qtx);
					Log.d("kh", "GcmId : " + messagesender);

					// nameValuePairs.add(new BasicNameValuePair("sender",
					// gcmid));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/gcmsender.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "message sending success");

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}

					return message;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					Intent intent_person = new Intent();
					intent_person.setClass(ActivityMessage.this,
							TabMenuActivity.class);
					startActivity(intent_person);
					// finish();

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}
}
