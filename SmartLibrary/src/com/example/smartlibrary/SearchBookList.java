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
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

	private ArrayList<BookInfo> bkList;
//	ArrayAdapter<String> adapter;

	 private ArrayAdapter<String> mAaBooklist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbooklist);

		Intent intent = getIntent();
		String searchText = intent.getStringExtra("text");
		querytxt = searchText;
//		Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG)
//				.show();
//		Log.d("kh", "query " + searchText);

		Button btn = (Button) findViewById(R.id.searchButton);
		final EditText e_id = (EditText) findViewById(R.id.book_input);
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
		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.clear();
				// TODO Auto-generated method stub
				bkList = new ArrayList<BookInfo>();
				Log.d("kh", e_id.getText().toString());
				Log.d("kh", "json start");
				select(e_id.getText().toString());

				try {

				} catch (Exception e) {
					Log.e("Fail Button", e.toString());
				}

			}
		});

	}
	
	public void select(final String qtx) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				/*
				final String bookTitle;
				final String bookAuthor;
				final String bookIsbn;
				final String bookNum;
				final String bookPublisher;
				final String bookPubdate;
				final String bookCategory;
				final String bookReservation;
*/
				ArrayList bklist = new ArrayList<BookInfo>();
			//	adapter = new ArrayAdapter<BookInfo>()
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
							
								 adapter.add(new
										 BookInfo(getApplicationContext(),bookIsbn,bookNum,bookCategory,bookAuthor,
										 bookPublisher,bookPubdate,bookTitle,bookReservation));
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

	}

	public void onClick(View v) {

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
				TextView tv = (TextView) view.findViewById(R.id.bookname);
				TextView tv2 = (TextView) view.findViewById(R.id.author);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.getBookname());
				// 텍스트뷰2에 getData()을 출력 즉 두번째 인수값
				tv2.setText(data.getAuthor());
				tv2.setTextColor(Color.BLACK);

			}

			return view;

		}

	}

	public class BookInfo {
		private String isbn;
		private String num;
		private String category;
		private String author;
		private String publisher;
		private String pubdate;
		private String bookname;
		private String reservation;

		public BookInfo(Context context, String b_isbn, String b_num,
				String b_category, String b_author, String b_publisher,
				String b_pubdate, String b_bookname, String b_reservation) {
			isbn = b_isbn;
			num = b_num;
			category = b_category;
			author = b_author;
			publisher = b_publisher;
			pubdate = b_pubdate;
			bookname = b_bookname;
			reservation = b_reservation;

		}

		public String getIsbn() {
			return isbn;
		}

		public String getNum() {
			return num;
		}

		public String getCategory() {
			return category;
		}

		public String getAuthor() {
			return author;
		}

		public String getPublisher() {
			return publisher;
		}

		public String getPubdate() {
			return pubdate;
		}

		public String getBookname() {
			return bookname;
		}

		public String getReservation() {
			return reservation;
		}

	}
}
