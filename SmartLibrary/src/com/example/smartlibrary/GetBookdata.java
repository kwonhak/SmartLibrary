package com.example.smartlibrary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class GetBookdata extends Activity {

	
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enrollbook);
		
		Intent intent = getIntent();
		String searchIsbn = intent.getStringExtra("isbn");
		
		 boolean inTitle = false, inAuthor = false, inPublisher = false, inIsbn = false, inPubdate = false;
		
		  System.out.println(searchIsbn);
		  
		        try{
		            URL url = new URL("http://book.interpark.com/api/search.api?"
		           +"key=BC35EF5561A23FF8286B4F06137D0161AA3F1C9637E1A1A7FBD31F12643FA7CF"
		           +"&query="+searchIsbn //여기는 쿼리를 넣으세요(검색어)
		           +"&queryType=isbn");
		            
		       
		         XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
		         XmlPullParser parser = parserCreator.newPullParser();
		            
		         parser.setInput(url.openStream(), null);
		                  
		        // status.setText("파싱 중이에요..");
		         
		         int parserEvent = parser.getEventType();
		         
		         while (parserEvent != XmlPullParser.END_DOCUMENT){
		          switch(parserEvent){                     
		           case XmlPullParser.START_TAG:  //parser가 시작 태그를 만나면 실행
		            if(parser.getName().equals("title")){
		             inTitle = true;
		            }
		            if(parser.getName().equals("pubDate")){ //title 만나면 내용을 받을수 있게 하자 
		             inPubdate = true;              
		            }
		            if(parser.getName().equals("publisher")){ //address 만나면 내용을 받을수 있게 하자
		            inPublisher = true;              
		            }
		            if(parser.getName().equals("author")){ //mapx 만나면 내용을 받을수 있게 하자  
		             inAuthor = true;              
		            }
		            if(parser.getName().equals("isbn")){ //mapy 만나면 내용을 받을수 있게 하자  
		             inIsbn = true;              
		            }            
		            if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력 
		         //    status1.setText(status1.getText()+"에러");
		              //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
		            }            
		            break;
		            
		           case XmlPullParser.TEXT://parser가 내용에 접근했을때
		            if(inTitle){ //isTitle이 true일 때 태그의 내용을 저장.
		             title = parser.getText();             
		                inTitle = false;
		            }
		            if(inAuthor){ //isAddress이 true일 때 태그의 내용을 저장.
		             author = parser.getText();             
		                inAuthor = false;
		            }
		            if(inPublisher){ //isMapx이 true일 때 태그의 내용을 저장.
		             publisher = parser.getText();             
		                inPublisher = false;
		            }
		            if(inPubdate){ //isMapy이 true일 때 태그의 내용을 저장.
		             pubdate = parser.getText();             
		                inPubdate = false;
		            }
		            if(inIsbn){ //isMapy이 true일 때 태그의 내용을 저장.
			             isbn = parser.getText();             
			                inIsbn = false;
			            }
		            break;            
		           case XmlPullParser.END_TAG:
		            if(parser.getName().equals("item")){
		           //  status1.setText(status1.getText()+"상호 : "+ title +"\n주소 : "+ address +"\n좌표 : " + mapx + ", " + mapy+"\n\n");
//		             inItem = false;
		            	
		            	Toast toast = Toast.makeText(this, "Title: " + title
								+ " Author:" + author+ " publisher:" + publisher+ " pubdate:" + pubdate+ " isbn:" + isbn, Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP, 25, 400);
						toast.show();
		            }
		            break;                   
		          }          
		           parserEvent = parser.next();
		         }
		        // status2.setText("파싱 끝!");
		        } catch(Exception e){
		        // status1.setText("에러가..났습니다...");
		        }        
		
		
	}
	
	

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
					
					
				}
			}.execute("")).get();
			
		} catch (Exception e) {
			return null;
		}
		


	}
}
