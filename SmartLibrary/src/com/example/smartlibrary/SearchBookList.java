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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchBookList extends Activity implements OnItemClickListener,
		OnClickListener {

	String id;
	InputStream is = null;
	String result = null;
	String line = null;
	String bookname;
	String querytxt;
    DataAdapter adapter;
	private ListView mListView = null;
    EditText e_id;

	private ArrayList<BookInfo> bkList;
//	ArrayAdapter<String> adapter;

	 private ArrayAdapter<String> mAaBooklist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("kh", "SearchBooklist " );
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbooklist);

		Intent intent = getIntent();
		String searchText = intent.getStringExtra("text");
		Log.d("kh", "searchtext: "+searchText );
		querytxt = searchText;

		Button btnselect = (Button) findViewById(R.id.searchButton);
		Button btnsearch = (Button) findViewById(R.id.bt_search);
		Button btnhome = (Button) findViewById(R.id.bt_home);
		Button btnsetting = (Button) findViewById(R.id.bt_setting);
		btnhome.setOnClickListener(this);
		btnsetting.setOnClickListener(this);
		btnselect.setOnClickListener(this);
		btnsearch.setOnClickListener(this);
		
	    e_id = (EditText) findViewById(R.id.book_input);
		e_id.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		e_id.setInputType(InputType.TYPE_CLASS_TEXT);
		
		e_id.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent arg2) {
				// TODO Auto-generated method stub
				switch(actionId){
				case EditorInfo.IME_ACTION_SEARCH:
					adapter.clear();
					select(e_id.getText().toString());
					break;
				default:
					return false;
				}
				return true;
			}
		});
		
		mListView = (ListView) findViewById(R.id.book_list);
		bkList = new ArrayList<BookInfo>();
		adapter = new DataAdapter(this,bkList);

		mListView.setAdapter(adapter);
		select(querytxt);

		//여기부터
		mListView.setOnItemClickListener(this);

	}

	@Override
	public void onClick(View v){
		switch(v.getId())
		{
		case R.id.bt_home:
			Intent intent_home = new Intent();
			intent_home.setClass(SearchBookList.this, TabMenuActivity.class);
			
			Log.d("kh", "list home button ");
			startActivity(intent_home);
			break;
		case R.id.searchButton:
			adapter.clear();
			bkList = new ArrayList<BookInfo>();
			Log.d("kh", e_id.getText().toString());
			Log.d("kh", "json start");
			select(e_id.getText().toString());		
			Log.d("kh", "list search button ");

			break;
		case R.id.bt_setting:
			Intent intent_setting = new Intent();
			intent_setting.setClass(SearchBookList.this, SettingActivity.class);
			
			Log.d("kh", "list setting button ");
			startActivity(intent_setting);
			break;
		case R.id.bt_search:
			
			Log.d("kh", "list setting button ");
//			startActivity(intent_search);
			break;
		
		}
	}
	
	public void select(final String qtx) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.d("kh", "select start ");

				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("id", qtx));

				try {

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://112.108.40.87/select.php");
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
							
								 adapter.add(new
										 BookInfo(getApplicationContext(),bookIsbn,bookNum,bookCategory,bookAuthor,
										 bookPublisher,bookPubdate,bookTitle,bookReservation));
							}
						});

					}
					
				} catch (Exception e) {
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
//		 adapter.add(new
//				 BookInfo(getApplicationContext(),"null","null","null","null",
//						 "null","null","검색결과가 없습니다.","null"));
	}

	public void onItemClick(AdapterView<?> parent, View v, final int position,
			long id) {

		String select_isbn = ((TextView) v.findViewById(R.id.isbn)).getText().toString();
		Log.d("kh","isbn "+select_isbn);
		Intent intent_search = new Intent();
		intent_search.setClass(SearchBookList.this, BookInfoActivity.class);
		intent_search.putExtra("isbn", select_isbn);
		startActivity(intent_search);
	}



	private class DataAdapter extends ArrayAdapter<BookInfo> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<BookInfo> object) {

			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;

			// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

			if (v == null) {

				// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
				view = mInflater.inflate(R.layout.activity_bookitem, null);
			} else {

				view = v;
			}

			// 자료를 받는다.
			final BookInfo data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				TextView bkname = (TextView) view.findViewById(R.id.bookname);
				TextView bkauthor = (TextView) view.findViewById(R.id.author);
				TextView bkisbn = (TextView) view.findViewById(R.id.isbn);
				
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				bkname.setText(data.getBookname());
				
				
				// 텍스트뷰2에 getData()을 출력 즉 두번째 인수값
				bkauthor.setText(data.getAuthor()+"/");

		//		bkauthor.append(data.getAuthor());
				//bkauthor.setTextColor(Color.BLACK);
				bkisbn.setText(data.getIsbn());
			}

			return view;

		}

	}

}
