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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlibrary.ActivityBookMap;
import com.example.smartlibrary.ActivitySearch;
import com.example.smartlibrary.MainActivity;
import com.example.smartlibrary.R;

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
	String selectReservation;
	String userid;
	String state;
	String GcmId;
	JSONArray bkName;
	int cnt = 0;
	private ListView mListView = null;
	DataAdapter adapter;
	private ArrayList<BookInfo> bkList;
	private boolean mCheckBoxState = false;
	String selectIsbn = null;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;

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
		//Button btnsetting = (Button) findViewById(R.id.setting);

		btnhome.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_home = new Intent();
				intent_home.setClass(BookInfoActivity.this,
						MainActivity.class);

				Log.d("kh", "list home button ");
				startActivity(intent_home);
			}
		});

		
		btnsearch.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// adapter.clear();
				// bkList = new ArrayList<BookInfo>();
				// TODO Auto-generated method stub
				Intent intent_search = new Intent();
				intent_search.setClass(BookInfoActivity.this,
						ActivitySearch.class);

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
				if (userid == "") {
					
					alert("미인증회원.","로그인을 해주세요");
				} else {

					if (selectIsbn != null) {
						Log.d("kh", "selectIbsn : " + selectIsbn);

						Log.d("kh", "userid : " + userid);
						if (state.equals("0")) {
							// select(selectIsbn,"reservation");
							reservation();
							//alert("예약완료.","확인버튼을 눌러주세요");
							
							
							// select(searchIsbn);
						} else if (state.equals("1")) {
							alert("예약오류.","대출중인 도서입니다");
						} else if (state.equals("2")) {
							alert("예약오류.","관내 대여중인 도서입니다");
						} else if (state.equals("3")) {
							alert("예약오류.","이미 예약된 도서입니다");
						}

					} else {
						Toast toast = Toast.makeText(getApplicationContext(),
								"도서를 선택해 주세요.", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP, 25, 400);
						toast.show();
					}
				}
			}
		});

		btnmessage.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				userid = sharedPref.getString("id", "");
				if (userid == "") {
					alert("미인증회원.","로그인을 해주세요");
				} else {

					if (selectReservation != null) {
						if (state.equals("0")) {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"관내 대여중인 도서만 메시지를 보낼 수 있습니다.",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();
						}
						
						else if (state.equals("1")) {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"관내 대여중인 도서만 메시지를 보낼 수 있습니다.",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();
						} else if (state.equals("2")) {
							showAlertLogin();

						} else if (state.equals("3")) {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"관내 대여중인 도서만 메시지를 보낼 수 있습니다.",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP, 25, 400);
							toast.show();
						}

					} else {
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
	public void alert(String message1,String message2)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle(message1)        // 제목 설정
		.setMessage(message2)        // 메세지 설정
		.setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
		.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
		 // 확인 버튼 클릭시 설정
		public void onClick(DialogInterface dialog, int whichButton){
			
   

		}
		}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		});

		AlertDialog dialog = builder.create();    // 알림창 객체 생성
		dialog.show();    // 알림창 띄우기
	}

	private void showAlertLogin() {
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout loginLayout = (LinearLayout) vi.inflate(
				R.layout.activity_message, null);
		final EditText message = (EditText) loginLayout.findViewById(R.id.message);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("보낼 메시지").setView(loginLayout)
				.setNeutralButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						//여기서 메시지랑 gcm아이디 보냄
						Log.d("kh", "확인버튼누름");
						if(message.getText().toString().length()!=0)
						{
							
							sendmessage(message.getText().toString());
						}
						else
						{
							Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", 1).show();
						}
						
					
						
					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
		
		AlertDialog dialog = builder.create();
		dialog.show();
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
						bkName = json_data.getJSONArray("results");
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
	
	public String getgcmid( final String qtx) {

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
					nameValuePairs.add(new BasicNameValuePair("card", qtx));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/selectrent_gcmid.php");
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
						bkName = json_data.getJSONArray("results");
						for (int i = 0; i < bkName.length(); i++) {
							Log.d("kh", "i " + i);
							JSONObject jo = bkName.getJSONObject(i);
							GcmId = (jo.getString("gcmid"));
						
							Log.d("kh", "ok gcmid : "+GcmId);

							
						}

						return GcmId;

					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}

					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;

				

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

					String gcmid = sharedPref.getString("gcmid", "");
					nameValuePairs
							.add(new BasicNameValuePair("student", userid));
					nameValuePairs.add(new BasicNameValuePair("card",
							selectcard));
					nameValuePairs.add(new BasicNameValuePair("isbn",
							selectIsbn));
					

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

					return gcmid;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					alert("예약완료.","확인버튼을 눌러주세요");
					select(searchIsbn);
					
			
				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	public String sendmessage(final String qtx) {

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

					String gcmmyid = sharedPref.getString("gcmid", "");
					nameValuePairs.add(new BasicNameValuePair("gcmid", GcmId));
					nameValuePairs.add(new BasicNameValuePair("msg", qtx+"@"+gcmmyid));
					Log.d("kh", "message : "+qtx);
					Log.d("kh", "GcmId : "+GcmId);
					
					//nameValuePairs.add(new BasicNameValuePair("sender", gcmid));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/gcmsender.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "message sending success");

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}


					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;

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
			// bkList = object;

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

		// @Override
		// public BookInfo getItem(int position) {
		// return bkList.get(position);
		// }

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			cnt=0;
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

				

				holder.number.setText(String.valueOf(++cnt));
				holder.chung.setText(data.getCategory());
				holder.location.setText(data.getLocation());

				holder.location.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String strcut = location.substring(3, location.length());
						Log.d("kh", "length : " + strcut);
						Intent intent_location = new Intent();
						intent_location.setClass(BookInfoActivity.this,
								ActivityBookMap.class);
						intent_location.putExtra("location", strcut);
						Log.d("kh", "booklocation text ");
						startActivity(intent_location);

					}
				});

				Log.d("kh", "예약 값 " + data.getReservation());
				state = data.getReservation();
				if (data.getReservation().equals("0")) {
					holder.reservation.setText("대출가능");
				} else if (data.getReservation().equals("1")) {
					holder.reservation.setText("대출중");
				} else if (data.getReservation().equals("2")) {
					holder.reservation.setText("관내대여");
				} else if (data.getReservation().equals("3")) {
					holder.reservation.setText("예약중");
				}

				// setChecked(position);
				// holder.checkbox.setChecked(isCheckedConfrim[position]);
				// holder.checkbox.setTag(position);
				holder.checkbox
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (buttonView.getId() == R.id.checkbox) {
									if (isChecked) {
										selectIsbn = list.get(
												(int) getItemId(position))
												.getIsbn();
										selectReservation = list.get(
												(int) getItemId(position))
												.getReservation();
										selectcard = list.get(position)
												.getCard();
										
										getgcmid(selectcard);
									} else {
										selectIsbn = null;
										selectReservation = null;
										selectcard = null;
										
										
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
