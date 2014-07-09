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
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SearchBookList extends Activity implements OnItemClickListener, OnClickListener  {

	String id;
	InputStream is=null;
	String result = null;
	String line = null;
	String bookname;
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
	  
	 mLvBooklist = (ListView) findViewById(R.id.book_list);
	 
	 mAlData = new ArrayList<String>();
	 mAaBooklist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mAlData);

	 //리스트뷰에 어댑터를 세팅
	 mLvBooklist.setAdapter(mAaBooklist);
	 
	 mLvBooklist.setOnItemClickListener(this);
	  
	 }

		public void select()
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					
					nameValuePairs.add(new BasicNameValuePair("id",id));
					
					try{
						Log.d("kh", "1");
						HttpClient httpclient = new DefaultHttpClient();
						Log.d("kh", "2");
						HttpPost httppost = new HttpPost("http://112.108.40.87/select.php");
						Log.d("kh", "3");
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						Log.d("kh", "4");
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						Log.d("kh", "5");
						is = entity.getContent();
						Log.d("kh", "connection success");
								
						
						
					}
					catch(Exception e)
					{
						Log.e("Fail 1", e.toString());
						//Toast.makeText(getApplicationContext(), "Invalid IP Address",Toast.LENGTH_LONG).show();
						
					}
					
					

					try{
						BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
						StringBuilder sb = new StringBuilder();
						while((line = reader.readLine())!=null)
						{
							sb.append(line+"\n");
						}
						is.close();
						result = sb.toString();
						Log.e("pass 2","Connection success");
					}
					catch(Exception e)
					{
						Log.e("Fail 2", e.toString());

					}
					
					try
			    	{
			        	JSONObject json_data = new JSONObject(result);
			        	bookname=(json_data.getString("bookname"));

			        	//리스트뷰에 넣기
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

		         

		        // ArrayList 초기화

		        mAlData.clear();

		         

		        // ArrayList에 더미 데이터 입력

		        defaultData();              

		    }
		  private void defaultData()

		    {

		        mAlData.add("아이템 00");

		        mAlData.add("아이템 01");

		        mAlData.add("아이템 02");

		        mAlData.add("아이템 03");

		        mAlData.add("아이템 04");

		        mAlData.add("아이템 05");

		        mAlData.add("아이템 06");

		        mAlData.add("아이템 07");

		        mAlData.add("아이템 08");

		        mAlData.add("아이템 09");

		        mAlData.add("아이템 10");

		        mAlData.add("아이템 11");

		        mAlData.add("아이템 12");

		        mAlData.add("아이템 13");

		        mAlData.add("아이템 14");

		        mAlData.add("아이템 15");

		        mAlData.add("아이템 16");

		        mAlData.add("아이템 17");

		        mAlData.add("아이템 18");

		        mAlData.add("아이템 19");

		    }
		  
		  public void onItemClick(AdapterView<?> parent, View v, final int position, long id)

		    {

/*  

		         

		        // 리스트에서 데이터를 받아온다.

//		      String data = (String) parent.getItemAtPosition(position);

		        String data = mAlData.get(position);

		         

		        // 삭제 다이얼로그에 보여줄 메시지를 만든다.

		        String message = "해당 데이터를 삭제하시겠습니까?<br />" + 

		                "position : " + position + "<br />" +

		                "data : " + data + "<br />";

		         

		        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener()

		        {

		            @Override

		            public void onClick(DialogInterface arg0, int arg1)

		            {

		                // 선택된 아이템을 리스트에서 삭제한다.

		                mAlData.remove(position);

		                 

		                // Adapter에 데이터가 바뀐걸 알리고 리스트뷰에 다시 그린다.

		                mAaBooklist.notifyDataSetChanged();               

		            }

		        };

		         

		        // 삭제를 물어보는 다이얼로그를 생성한다.

		        new AlertDialog.Builder(this)

		            .setTitle("http://croute.me - 예제")

		            .setMessage(Html.fromHtml(message))

		            .setPositiveButton("삭제", deleteListener)

		            .show();
		        */

		    }

		 

		    public void onClick(View v)

		    {

		        
/*
		         

		        switch(v.getId())

		        {

		        // 리스트에 추가 버튼이 클릭되었을때의 처리

		        case R.id.main_b_input_to_list:

		            if(mEtInputText.getText().length() == 0)

		            {

		                // 데이터를 입력하라는 메시지 토스트를 출력한다.

		                Toast.makeText(this, "데이터를 입력하세요.", Toast.LENGTH_SHORT).show();

		            }

		            else

		            {

		                // 입력할 데이터를 받아온다.

		                String data = mEtInputText.getText().toString();

		                 

		                // 리스트에 데이터를 입력한다.

		                mAlData.add(data);

		                 

		                // Adapter에 데이터가 바뀐걸 알리고 리스트뷰에 다시 그린다.

		                mAaString.notifyDataSetChanged();

		                 

		                // 데이터 추가 성공 메시지 토스트를 출력한다.

		                Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show();

		                 

		                // EditText의 내용을 지운다.

		                mEtInputText.setText("");

		                 

		                // 데이터가 추가된 위치(리스트뷰의 마지막)으로 포커스를 이동시킨다.

		                mLvList.setSelection(mAlData.size()-1);

		            }

		            break;

		        }
*/
		    }
  

	 
	 
	 
}
