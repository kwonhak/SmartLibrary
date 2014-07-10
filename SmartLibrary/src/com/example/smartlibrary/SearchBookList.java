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
	  
	  
	 mLvBooklist = (ListView) findViewById(R.id.book_list);
	 
	 mAlData = new ArrayList<String>();
	 mAaBooklist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mAlData);

	 //����Ʈ�信 ����͸� ����
	 mLvBooklist.setAdapter(mAaBooklist);
	 
	 mLvBooklist.setOnItemClickListener(this);
	  
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
			        	JSONObject json_data = new JSONObject(result);
			        	Log.e("kh", "2");
			        	bookname=(json_data.getString("bookname"));
			        	Log.d("kh", "3");
			        	Toast.makeText(getApplicationContext(), bookname, Toast.LENGTH_LONG).show();
			        	Log.d("kh", bookname);
						

			        	//����Ʈ�信 �ֱ�
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

		         

		        // ArrayList �ʱ�ȭ

		        mAlData.clear();
		       

		         

		        // ArrayList�� ���� ������ �Է�

		        defaultData();              

		    }
		  private void defaultData()

		    {

		        mAlData.add("������ 00");

		        mAlData.add("������ 01");

		        mAlData.add("������ 02");

		        mAlData.add("������ 03");

		        mAlData.add("������ 04");

		        mAlData.add("������ 05");

		        mAlData.add("������ 06");

		        mAlData.add("������ 07");

		        mAlData.add("������ 08");

		        mAlData.add("������ 09");

		        mAlData.add("������ 10");

		        mAlData.add("������ 11");

		        mAlData.add("������ 12");

		        mAlData.add("������ 13");

		        mAlData.add("������ 14");

		        mAlData.add("������ 15");

		        mAlData.add("������ 16");

		        mAlData.add("������ 17");

		        mAlData.add("������ 18");

		        mAlData.add("������ 19");

		    }
		  
		  public void onItemClick(AdapterView<?> parent, View v, final int position, long id)

		    {

/*  

		         

		        // ����Ʈ���� �����͸� �޾ƿ´�.

//		      String data = (String) parent.getItemAtPosition(position);

		        String data = mAlData.get(position);

		         

		        // ���� ���̾�α׿� ������ �޽����� �����.

		        String message = "�ش� �����͸� �����Ͻðڽ��ϱ�?<br />" + 

		                "position : " + position + "<br />" +

		                "data : " + data + "<br />";

		         

		        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener()

		        {

		            @Override

		            public void onClick(DialogInterface arg0, int arg1)

		            {

		                // ���õ� �������� ����Ʈ���� �����Ѵ�.

		                mAlData.remove(position);

		                 

		                // Adapter�� �����Ͱ� �ٲ�� �˸��� ����Ʈ�信 �ٽ� �׸���.

		                mAaBooklist.notifyDataSetChanged();               

		            }

		        };

		         

		        // ������ ����� ���̾�α׸� ���Ѵ�.

		        new AlertDialog.Builder(this)

		            .setTitle("http://croute.me - ����")

		            .setMessage(Html.fromHtml(message))

		            .setPositiveButton("����", deleteListener)

		            .show();
		        */

		    }

		 

		    public void onClick(View v)

		    {

		        
/*
		         

		        switch(v.getId())

		        {

		        // ����Ʈ�� �߰� ��ư�� Ŭ���Ǿ������� ó��

		        case R.id.main_b_input_to_list:

		            if(mEtInputText.getText().length() == 0)

		            {

		                // �����͸� �Է��϶�� �޽��� �佺Ʈ�� ����Ѵ�.

		                Toast.makeText(this, "�����͸� �Է��ϼ���.", Toast.LENGTH_SHORT).show();

		            }

		            else

		            {

		                // �Է��� �����͸� �޾ƿ´�.

		                String data = mEtInputText.getText().toString();

		                 

		                // ����Ʈ�� �����͸� �Է��Ѵ�.

		                mAlData.add(data);

		                 

		                // Adapter�� �����Ͱ� �ٲ�� �˸��� ����Ʈ�信 �ٽ� �׸���.

		                mAaString.notifyDataSetChanged();

		                 

		                // ������ �߰� ���� �޽��� �佺Ʈ�� ����Ѵ�.

		                Toast.makeText(this, "�����Ͱ� �߰��Ǿ���ϴ�.", Toast.LENGTH_SHORT).show();

		                 

		                // EditText�� ������ �����.

		                mEtInputText.setText("");

		                 

		                // �����Ͱ� �߰��� ��ġ(����Ʈ���� ������)���� ��Ŀ���� �̵���Ų��.

		                mLvList.setSelection(mAlData.size()-1);

		            }

		            break;

		        }
*/
		    }
  

	 
	 
	 
}
