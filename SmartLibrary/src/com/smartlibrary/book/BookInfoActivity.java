package com.smartlibrary.book;

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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlibrary.BorrowInfo;
import com.example.smartlibrary.R;
import com.example.smartlibrary.SearchBookList;
import com.example.smartlibrary.SearchViewerActivity;
import com.example.smartlibrary.SettingActivity;
import com.example.smartlibrary.TabMenuActivity;

public class BookInfoActivity extends Activity {
	String id;
	InputStream is = null;
	String result = null;
	String querytxt;
	String line = null;
	String searchIsbn = null;
	TextView txtTitle;
	TextView txtAuthor;
	TextView txtPublisher;
	TextView txtIsbn;
	TextView txtLocation;
	TextView txtReservation;
	Button btnreservation;
	Button btnmessage;
	String title;
	String author;
	String card;
	String booknum;
	String isbn;
	String category;
	String publisher;
	String location;
	String reservation;
	String pubdate;
	String selectcard;
	String userid;
	String state;

	private ListView mListView = null;
	DataAdapter adapter;
	private ArrayList<BookInfo> bkList;
	private boolean mCheckBoxState = false;
	String selectIsbn = null;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookinfo);

		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		
		Intent intent = getIntent();
		searchIsbn = intent.getStringExtra("isbn");

		Button btnsearch = (Button) findViewById(R.id.search);
		Button btnhome = (Button) findViewById(R.id.home);
		Button btnsetting = (Button) findViewById(R.id.setting);
		
		btnhome.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_home = new Intent();
				intent_home.setClass(BookInfoActivity.this, TabMenuActivity.class);

				Log.d("kh", "list home button ");
				startActivity(intent_home);
			}
		});
		
		btnsetting.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_setting = new Intent();
				intent_setting.setClass(BookInfoActivity.this, SettingActivity.class);

				Log.d("kh", "list setting button ");
				startActivity(intent_setting);
			}
		});
		btnsearch.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
//				adapter.clear();
//				bkList = new ArrayList<BookInfo>();
				// TODO Auto-generated method stub
				Intent intent_search = new Intent();
				intent_search.setClass(BookInfoActivity.this, TabMenuActivity.class);

				Log.d("kh", "list home button ");
				startActivity(intent_search);
			}
		});
		
		txtTitle = (TextView) findViewById(R.id.title);
		txtAuthor = (TextView) findViewById(R.id.author);
		txtPublisher = (TextView) findViewById(R.id.publisher);
		txtIsbn = (TextView) findViewById(R.id.isbn);
		btnreservation = (Button) findViewById(R.id.reservation);
		btnmessage = (Button) findViewById(R.id.message);
		btnreservation.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				   userid = sharedPref.getString("id", "");
				if(userid=="")
				{
					Toast toast = Toast.makeText(getApplicationContext(),
							"로그인을 해주세요.", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 25, 400);
					toast.show();
				}
				else
				{
					
					if (selectIsbn != null) {
						Log.d("kh","selectIbsn : "+selectIsbn);
					 
						Log.d("kh","userid : "+userid);
						if(state.equals("0")){
							//select(selectIsbn,"reservation");
							Toast toast = Toast.makeText(getApplicationContext(),
									"예약되었습니다.", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();
							reservation();
							//select(searchIsbn);
						}
						else if(state.equals("1")){
							Toast toast = Toast.makeText(getApplicationContext(),
									"대출중인 도서입니다.", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();
						}
						else if(state.equals("2")){
							Toast toast = Toast.makeText(getApplicationContext(),
									"관내 대여중입니다.", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();
						}
						else if(state.equals("3")){
							Toast toast = Toast.makeText(getApplicationContext(),
									"이미 예약된 도서입니다.", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();
						}
						
						

					}
					else
					{
						Toast toast = Toast.makeText(getApplicationContext(),
								"도서를 선택해 주세요.", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP, 25, 400);
						toast.show();
					}
				}
			
			}
		});

		mListView = (ListView) findViewById(R.id.selectbook);
		bkList = new ArrayList<BookInfo>();
		adapter = new DataAdapter(this, bkList);

		select(searchIsbn);

		mListView.setAdapter(adapter);

	}
	

	public String select(final String qtx) {

		try {
			return (new AsyncTask<String, String, String>() {
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
								"http://112.108.40.87/bookinfo.php");
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
						for (int i = 0; i < bkName.length(); i++) {
							Log.d("kh", "i " + i);
							JSONObject jo = bkName.getJSONObject(i);
							isbn = (jo.getString("isbn"));
							booknum = jo.getString("num");
							title = (jo.getString("bookname"));
							author = (jo.getString("author"));
							publisher = jo.getString("publisher");
							pubdate = jo.getString("pubdate");
							category = jo.getString("category");
							location = jo.getString("location");
							reservation = jo.getString("reservation");
							card = jo.getString("card");
							Log.d("kh", "ok");
							
							dataList.add(new BookInfo(getApplicationContext(),
									isbn, booknum, category, author, publisher,
									pubdate, title, location, reservation, card));
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
						return;

					// 텍스트 값 넣기
					txtIsbn.setText(isbn);
					txtTitle.setText(title);
					txtAuthor.setText(author);
					txtPublisher.setText(publisher);

					adapter.clear();
					adapter.addAll(dataList);
					adapter.notifyDataSetChanged();

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}
	
	public String reservation() {

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

					nameValuePairs.add(new BasicNameValuePair("student", userid));
					nameValuePairs.add(new BasicNameValuePair("card", selectcard));
					nameValuePairs.add(new BasicNameValuePair("isbn", selectIsbn));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/reservationinsert.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}

//					try {
//						BufferedReader reader = new BufferedReader(
//								new InputStreamReader(is, "UTF_8"), 8);
//						StringBuilder sb = new StringBuilder();
//
//						while ((line = reader.readLine()) != null) {
//							sb.append(line + "\n");
//						}
//						is.close();
//						Log.d("kh", "result");
//						result = sb.toString();
//						Log.d("kh", result);
//					} catch (Exception e) {
//						Log.e("Fail 2", e.toString());
//
//					}


					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;

					// 텍스트 값 넣기
//					txtIsbn.setText(isbn);
//					txtTitle.setText(title);
//					txtAuthor.setText(author);
//					txtPublisher.setText(publisher);
//
//					runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//
//							adapter.add(new BookInfo(getApplicationContext(),
//									isbn, booknum, category, author, publisher,
//									pubdate, title, location, reservation, card));
//							mListView.setAdapter(adapter);
//						}
//					});

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	private class DataAdapter extends ArrayAdapter<BookInfo> {
		private ViewHolder holder;
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;
		private boolean[] isCheckedConfrim;
		private ArrayList<BookInfo> list;

		public DataAdapter(Context context, ArrayList<BookInfo> object) {

			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			list = object;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.isCheckedConfrim = new boolean[bkList.size()];
			//bkList = object;

		}

		// CheckBox를 모두 선택하는 메서드
		public void setAllChecked(boolean ischeked) {
			int tempSize = isCheckedConfrim.length;
			for (int a = 0; a < tempSize; a++) {
				isCheckedConfrim[a] = ischeked;
			}
		}

		public void setChecked(int position) {
			isCheckedConfrim[position] = !isCheckedConfrim[position];
			notifyDataSetChanged();
		}

		public ArrayList<Integer> getChecked() {
			int tempSize = isCheckedConfrim.length;
			ArrayList<Integer> mArrayList = new ArrayList<Integer>();
			for (int b = 0; b < tempSize; b++) {
				if (isCheckedConfrim[b]) {
					mArrayList.add(b);
				}
			}
			return mArrayList;
		}

		@Override
		public int getCount() {
			return list.size();
		}

//		@Override
//		public BookInfo getItem(int position) {
//			return bkList.get(position);
//		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(final int position, View v, ViewGroup parent) {

			View view = null;

			// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

			if (v == null) {

				// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
				view = mInflater.inflate(R.layout.activity_bookdata, null);

			} else {

				view = v;
			}

			// 자료를 받는다.
			final BookInfo data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				holder = new ViewHolder();
				holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
				view.setTag(holder);

				holder.number = (TextView) view.findViewById(R.id.number);
				holder.chung = (TextView) view.findViewById(R.id.chung);
				holder.location = (TextView) view.findViewById(R.id.location);
				holder.reservation = (TextView) view
						.findViewById(R.id.reservation);
				view.setTag(holder);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값

				holder.number.setText("1");
				holder.chung.setText(data.getCategory());		
				holder.location.setText(data.getLocation());
				
				Log.d("kh","예약 값 "+ data.getReservation());
				state=data.getReservation();
				if(data.getReservation().equals("0")){
					holder.reservation.setText("대출가능");
				}
				else if(data.getReservation().equals("1"))
				{
					holder.reservation.setText("대출중");
				}
				else if(data.getReservation().equals("2"))
				{
					holder.reservation.setText("관내대여");				
				}
				else if(data.getReservation().equals("3"))
				{
					holder.reservation.setText("예약중");
				}
			
				//setChecked(position);
				// holder.checkbox.setChecked(isCheckedConfrim[position]);
				//holder.checkbox.setTag(position);
				holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					  
		            @Override
		            public void onCheckedChanged(CompoundButton buttonView,
		                    boolean isChecked) {
		                if (buttonView.getId() == R.id.checkbox) {
		                    if (isChecked) {
		                    	selectIsbn = list.get((int) getItemId(position)).getIsbn();
		        				selectcard = list.get(position).getCard();
		                        Toast.makeText(getApplicationContext(), "눌림", 1).show();
		                    } else {
		                        Toast.makeText(getApplicationContext(), "안눌림", 1).show();
		                    }
		                }
		            }
		        });
				
				
				Log.d("kh", "checked T,.T");
				// holder.checkbox.setChecked(((ListView)
				// parent).isItemChecked(position));

			}

			return view;

		}

	}

	class ViewHolder {
		TextView number;
		TextView chung;
		TextView location;
		TextView reservation;

		CheckBox checkbox;
	}

}
