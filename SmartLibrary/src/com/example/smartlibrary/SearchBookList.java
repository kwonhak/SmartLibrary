package com.example.smartlibrary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchBookList extends Activity implements OnItemClickListener, OnClickListener  {

	String id;
	InputStream is=null;
	String result = null;
	String line = null;
	String bookname;
	String querytxt;
	DataAdapter adapter;
	private ListView mLvBooklist;
	private ArrayList<BookInfo> bkList;
//	private ArrayAdapter<String> mAaBooklist;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_searchbooklist);
	
	  Intent intent = getIntent();
	  String searchText = intent.getStringExtra("text");
	  querytxt = searchText;
	  Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG).show();
	  Log.d("kh","query "+searchText);

	  Button btn = (Button)findViewById(R.id.searchButton);
	  final EditText e_id = (EditText)findViewById(R.id.book_input);
	  
	 mLvBooklist = (ListView) findViewById(R.id.book_list);
	 
	 bkList = new ArrayList<BookInfo>(); 
	// adapter = new DataAdapter(this, bkList); 
	
//	 mLvBooklist.setAdapter(adapter);
	  select();
	

	 
	 mLvBooklist.setOnItemClickListener(this);
	 btn.setOnClickListener(new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		Log.d("kh",e_id.getText(). toString());
		Log.d("kh","json start");
		
		try{
		
			
		}
		 catch(Exception e) {
		     Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		    }
		

		} 
	 });
	  
	 }
	 
	
		

	 
		public void select()
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String bookTitle;
					String bookAuthor;
					String bookIsbn;
					String bookNum;
					String bookPublisher;
					String bookPubdate;
					String bookCategory;
					String bookReservation;
					
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					
					nameValuePairs.add(new BasicNameValuePair("id",querytxt));
					
					try{
						
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost("http://112.108.40.87/select.php");
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();						
						is = entity.getContent();
						//Log.d("kh", "connection success"+is);
								
						
						
					}
					catch(Exception e)
					{
						Log.e("Fail 1", e.toString());
						//Toast.makeText(getApplicationContext(), "Invalid IP Address",Toast.LENGTH_LONG).show();
						
					}

					try{
						
						BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF_8"),8);
						StringBuilder sb = new StringBuilder();
						
						while((line = reader.readLine())!=null)
						{
							sb.append(line+"\n");
						}
						is.close();
			        	Log.d("kh", "result");
						result = sb.toString();
//						result = "\""+result.replace("\"","\\\"")+"\"";
						Log.d("kh",result);
					}
					catch(Exception e)
					{
						Log.e("Fail 2", e.toString());

					}
				
					try
			    	{
//						Log.d("kh", "1");				
//			        	JSONObject json_data = null;
//			        	json_data = new JSONObject(result);
//			        	Log.d("kh", "1.5");	//여기는 됨
//			        	JSONArray bkName = json_data.getJSONArray("results");
//			        	
//			        	Log.e("kh", "2");
//			        	bookname=(json_data.getString("results"));
//			        	Log.d("kh", "3"+bookname);
//			        	//Toast.makeText(getApplicationContext(), bookname, Toast.LENGTH_LONG).show();
//			        	Log.d("kh", bookname);
//						BookInfo bkinfo = new BookInfo( bookAuthor, bookAuthor, bookAuthor, bookAuthor, bookAuthor, bookAuthor, bookAuthor, bookAuthor);
						Log.d("kh", "1");				
			        	JSONObject json_data = null;
			        	json_data = new JSONObject(result);
			        	Log.d("kh", "1.5");	//여기는 됨
			        	JSONArray bkName = json_data.getJSONArray("results");
	//		        	bkList = new ArrayList<BookInfo>();
			        	for(int i=0;i<bkName.length();i++){
			        		Log.d("kh", "i "+i);
			        		JSONObject jo = bkName.getJSONObject(i);
			        		bookIsbn = jo.getString("isbn");
			        		bookNum = jo.getString("num");
			        		bookAuthor = jo.getString("author");
			        		bookPublisher = jo.getString("publisher");
			        		bookPubdate = jo.getString("pubdate");
			        		bookCategory = jo.getString("category");
			        		bookTitle = jo.getString("bookname");
			        		bookReservation = jo.getString("reservation");
			        		Log.d("kh", "title "+bookTitle);	//여기는 됨
//			        		bkList.add(object)
//			        		bkList.add(new BookInfo(getApplicationContext(),bookIsbn,bookNum,bookCategory,bookAuthor,
//			        				bookPublisher,bookPubdate,bookTitle,bookReservation));
			        		
//			        		adapter.add(new BookInfo(getApplicationContext(),bookIsbn,bookNum,bookCategory,bookAuthor,
//			        				bookPublisher,bookPubdate,bookTitle,bookReservation));
//			        		 		
			        	}   
//			        	adapter = new DataAdapter(this, bkList); 
				        		

			        	//여기에 리스트뷰 추가
			        	//mAaBooklist.add("");
			        	//mAaBooklist.notifyDataSetChanged();
			    	}
			        catch(Exception e)
			    	{
			        	Log.e("Fail 3", e.toString());
			    	}
			
				}
			}).start();
		
		}

		 @Override
		    protected void onResume()

		    {

		        super.onResume();

		        bkList.clear();
		        defaultData();              

		    }
		  private void defaultData()

		    {

//		        bkList.add("검색한 자료가 없습니다.");
		    }
		  
		  public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
		    {

		    }
	 

		    public void onClick(View v)
		    {

		    }
		  
}
