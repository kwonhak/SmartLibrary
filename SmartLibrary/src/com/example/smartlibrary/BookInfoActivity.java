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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BookInfoActivity extends Activity {
	String id;
	InputStream is = null;
	String result = null;
	String querytxt;
	String line = null;
	String searchIsbn=null;
	TextView txtTitle;
	TextView txtAuthor;
	TextView txtPublisher;
	TextView txtIsbn;
	TextView txtLocation;
	TextView txtReservation;
	String title;
	String author;
	String isbn;
	String publisher;
	String location;
	String reservation;
	String pubdate;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookinfo);

		Intent intent = getIntent();
		searchIsbn = intent.getStringExtra("isbn");
		
		
		txtTitle = (TextView)findViewById(R.id.title);
		txtAuthor = (TextView)findViewById(R.id.author);
		txtPublisher = (TextView)findViewById(R.id.publisher);
		txtIsbn = (TextView)findViewById(R.id.isbn);
		select(searchIsbn);
		
		
	}
	/*
	public void select(final String qtx) {
		new Thread(new Runnable() {

			@Override
			public void run() {

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
					// bkList = new ArrayList<BookInfo>();
					for (int i = 0; i < bkName.length(); i++) {
						Log.d("kh", "i " + i);
						JSONObject jo = bkName.getJSONObject(i);
						txtIsbn.setText(jo.getString("isbn"));
						txtTitle.setText(jo.getString("bookname"));
						txtAuthor.setText(jo.getString("author"));
						txtPublisher.setText(jo.getString("publisher"));
						Log.d("kh","ok");
//						final String bookIsbn = jo.getString("isbn");
//						final String bookNum = jo.getString("num");
//						final String bookAuthor = jo.getString("author");
//						final String bookPublisher = jo.getString("publisher");
//						final String bookPubdate = jo.getString("pubdate");
//						final String bookCategory = jo.getString("category");
//						final String bookTitle = jo.getString("bookname");
//						final String bookReservation = jo.getString("reservation");
//						Log.d("kh", "title " + bookTitle); // 여기는 됨
						
//						 runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//							
////								 adapter.add(new
////										 BookInfo(getApplicationContext(),bookIsbn,bookNum,bookCategory,bookAuthor,
////										 bookPublisher,bookPubdate,bookTitle,bookReservation));
//							}
//						});
//
					}
					

					
				} catch (Exception e) {
					Log.e("Fail 3", e.toString());
				}

			}
		}).start();

	}
	*/
	
public String select(final String qtx) {
		
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
							isbn=(jo.getString("isbn"));
							final String bookNum = jo.getString("num");
							title=(jo.getString("bookname"));
							author=(jo.getString("author"));
							publisher=jo.getString("publisher");
							final String bookPubdate = jo.getString("pubdate");
							location=jo.getString("category");
							reservation=jo.getString("reservation");
							Log.d("kh","ok");
						}

						return isbn;
						
						
					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}
					
					return null;
				}
				
				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return ;
					
					
					
				//텍스트 값 넣기
					txtIsbn.setText(isbn);
					txtTitle.setText(title);
					txtAuthor.setText(author);
					txtPublisher.setText(publisher);
					
				}
			}.execute("")).get();
			
		} catch (Exception e) {
			return null;
		}
		


	}

}
