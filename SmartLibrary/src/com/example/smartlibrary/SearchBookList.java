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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SearchBookList extends Activity implements OnItemClickListener, OnClickListener  {

	String id;
	InputStream is=null;
	String result = null;
	String line = null;
	String bookname;
	String querytxt;
	private ListView mLvBooklist;
	private ArrayList<String> mAlData;
	private ArrayAdapter<String> mAaBooklist;
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
	  select();
	  Button btn = (Button)findViewById(R.id.searchButton);
	  
	 mLvBooklist = (ListView) findViewById(R.id.book_list);
	 
	 mAlData = new ArrayList<String>();
	 mAaBooklist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mAlData);

	 //����Ʈ�信 ����͸� ����
	 mLvBooklist.setAdapter(mAaBooklist);
	 
	 mLvBooklist.setOnItemClickListener(this);
	 btn.setOnClickListener(new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		String json = DownloadHtml("http://112.108.40.87/select.php");
		Log.d("kh",json);
		try{
			
			
		}
		 catch(Exception e) {
		     Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		    }
		

		} 
	 });
	  
	 }
	 
	 String DownloadHtml(String addr){
		  StringBuilder jsonHtml = new StringBuilder();
		   try{
		      // 연결 url 설정
		      URL url = new URL(addr);
		      // 커넥션 객체 생성
		      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		      // 연결되었으면.
		      if(conn != null){
		         conn.setConnectTimeout(10000);
		         conn.setUseCaches(false);
		         // 연결되었음 코드가 리턴되면.
		         if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
		            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "EUC-KR"));
		            for(;;){
		                // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.  
		                String line = br.readLine();
		                if(line == null) break;
		                // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
		                jsonHtml.append(line + "\n");
		             }
		          br.close();
		       }
		        conn.disconnect();
		     }
		   } catch(Exception ex){
		      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		   }
		   return jsonHtml.toString();
	 }
		

	 
		public void select()
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
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
						Log.d("kh",result);
					}
					catch(Exception e)
					{
						Log.e("Fail 2", e.toString());

					}
					
					try
			    	{
						Log.d("kh", "1");
//						JSONArray ja = new JSONArray(result);
			        	JSONObject json_data = new JSONObject(result);
			        	Log.e("kh", "2");
			        	bookname=(json_data.getString("bookname"));
			        	Log.d("kh", "3");
			        	Toast.makeText(getApplicationContext(), bookname, Toast.LENGTH_LONG).show();
			        	Log.d("kh", bookname);
						

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

		        mAlData.clear();
		        defaultData();              

		    }
		  private void defaultData()

		    {

		        mAlData.add("검색한 자료가 없습니다.");
		    }
		  
		  public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
		    {

		    }

		 

		    public void onClick(View v)
		    {

		    }
  

	 
	 
	 
}
