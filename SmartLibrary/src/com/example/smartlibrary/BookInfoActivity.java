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

import com.example.smartlibrary.BookInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BookInfoActivity extends Activity {
	String id;
	InputStream is = null;
	String result = null;
	String querytxt;
	String line = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty);

		Intent intent = getIntent();
		String searchText = intent.getStringExtra("text");
		querytxt = searchText;
		select(querytxt);
	}
	public void select(final String qtx) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList bklist = new ArrayList<BookInfo>();
			//	adapter = new ArrayAdapter<BookInfo>()
				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("id", qtx));

				try {

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://112.108.40.87/bookinfo.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
							HTTP.UTF_8));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
					// Log.d("kh", "connection success"+is);

				} catch (Exception e) {
					Log.e("Fail 1", e.toString());
					// Toast.makeText(getApplicationContext(),
					// "Invalid IP Address",Toast.LENGTH_LONG).show();

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
					// result = "\""+result.replace("\"","\\\"")+"\"";
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
					// bkList = new ArrayList<BookInfo>();
					for (int i = 0; i < bkName.length(); i++) {
						Log.d("kh", "i " + i);
						JSONObject jo = bkName.getJSONObject(i);
						final String bookIsbn = jo.getString("isbn");
						final String bookNum = jo.getString("num");
						final String bookAuthor = jo.getString("author");
						final String bookPublisher = jo.getString("publisher");
						final String bookPubdate = jo.getString("pubdate");
						final String bookCategory = jo.getString("category");
						final String bookTitle = jo.getString("bookname");
						final String bookReservation = jo.getString("reservation");
						Log.d("kh", "title " + bookTitle); // 여기는 됨
						
						 runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
							
//								 adapter.add(new
//										 BookInfo(getApplicationContext(),bookIsbn,bookNum,bookCategory,bookAuthor,
//										 bookPublisher,bookPubdate,bookTitle,bookReservation));
							}
						});

					}
					

					// 여기에 리스트뷰 추가
					// mAaBooklist.add("");
					// mAaBooklist.notifyDataSetChanged();
				} catch (Exception e) {
					Log.e("Fail 3", e.toString());
				}

			}
		}).start();

	}

}
