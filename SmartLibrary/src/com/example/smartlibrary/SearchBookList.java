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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.smartlibrary.book.BookInfo;
import com.smartlibrary.book.BookInfoActivity;

public class SearchBookList extends Activity implements OnItemClickListener {

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

	private ArrayAdapter<String> mAaBooklist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("kh", "SearchBooklist ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbooklist);

		Intent intent = getIntent();
		String searchText = intent.getStringExtra("text");
		Log.d("kh", "searchtext: " + searchText);
		querytxt = searchText;

		Button btnselect = (Button) findViewById(R.id.searchButton);
		//Button btnsearch = (Button) findViewById(R.id.search);
		Button btnhome = (Button) findViewById(R.id.home);
		//Button btnsetting = (Button) findViewById(R.id.setting);

		
		btnselect.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Log.d("kh", e_id.getText().toString());
				Log.d("kh", "json start");
				if(e_id.getText().toString().length()!=0)
				{
					adapter.clear();
					bkList = new ArrayList<BookInfo>();
					select(e_id.getText().toString());
					Log.d("kh", "list search button ");
					//finish();	
				}
				else
				{
					Toast toast = Toast.makeText(getApplicationContext(),
							"검색어를 입력해주세요.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 25, 400);
					toast.show();
				}
				
				
			}
		});
		
		
		
		btnhome.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_home = new Intent();
				intent_home.setClass(SearchBookList.this, MainActivity.class);

				Log.d("kh", "list home button ");
				startActivity(intent_home);
				finish();
				
			}
		});
	
		

		e_id = (EditText) findViewById(R.id.book_input);
		e_id.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		e_id.setInputType(InputType.TYPE_CLASS_TEXT);

		e_id.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent arg2) {
				// TODO Auto-generated method stub
				switch (actionId) {
				case EditorInfo.IME_ACTION_SEARCH:
					if(e_id.getText().toString().length()!=0)
					{
						adapter.clear();
						select(e_id.getText().toString());
					}
					else
					{
						Toast toast = Toast.makeText(getApplicationContext(),
								"검색어를 입력해주세요.", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP, 25, 400);
						toast.show();
					}
					
					break;
				default:
					return false;
				}
				return true;
			}
		});

		mListView = (ListView) findViewById(R.id.book_list);
		bkList = new ArrayList<BookInfo>();
		adapter = new DataAdapter(this, bkList);

		mListView.setAdapter(adapter);
		select(querytxt);

		// 여기부터
		mListView.setOnItemClickListener(this);

	}

	public String select(final String qtx) {

		try {
			return (new AsyncTask<String, String, String>() {

				//ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();
				ArrayList<BookInfo> dataList = new ArrayList<BookInfo>();
				
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
								"http://112.108.40.87/select.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
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
						String a="";
						
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
							final String bookLocation = jo.getString("location");
							final String bookReservation = jo
									.getString("reservation");
							final String bookCard = jo.getString("card");
							Log.d("kh", "title " + bookTitle); // 여기는 됨
							
							
							dataList.add(new BookInfo(
									getApplicationContext(), bookIsbn,
									bookNum, bookCategory, bookAuthor,
									bookPublisher, bookPubdate, bookTitle,
									bookLocation, bookReservation,bookCard));
						}
						return result;

					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}

					return result;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
					{
						Toast toast = Toast.makeText(getApplicationContext(),
								"검색 결과가 없습니다.", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP, 25, 400);
						toast.show();
						
						return;
					}

					adapter.clear();
					adapter.addAll(dataList);
					adapter.notifyDataSetChanged();

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}
	

	@Override
	protected void onResume()

	{
		super.onResume();
	}

	private void defaultData()

	{
		// adapter.add(new
		// BookInfo(getApplicationContext(),"null","null","null","null",
		// "null","null","검색결과가 없습니다.","null"));
	}

	public void onItemClick(AdapterView<?> parent, View v, final int position,
			long id) {

		String select_isbn = ((TextView) v.findViewById(R.id.isbn)).getText()
				.toString();
		Log.d("kh", "isbn " + select_isbn);
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
				bkname.setText(data.gettitle());

				bkauthor.setText(data.getAuthor() + "/");

				bkisbn.setText(data.getIsbn());
			}

			return view;

		}

	}


	
	
}
