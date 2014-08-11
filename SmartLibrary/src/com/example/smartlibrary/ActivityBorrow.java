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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityBorrow extends Activity {

	String id;
	InputStream is = null;
	String result = null;
	String line = null;
	String bookname;
	String querytxt;
	String card;
	String student;
	String isbn;
	String startdate;
	String enddate;
	String extension;
	String title;
	
	String selectCard;
	
	ListAdapter adapter;
	private ListView mListView = null;
	EditText e_id;

	private ArrayList<BorrowInfo> brList;
	// ArrayAdapter<String> adapter;

	private ArrayAdapter<String> mAaBooklist;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_borrow);

		Button btnrent = (Button) findViewById(R.id.rent);
		Button btnborrow = (Button) findViewById(R.id.borrow);
		Button btnhome = (Button) findViewById(R.id.home);
		Button btnsetting = (Button) findViewById(R.id.setting);
		btnhome.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_home = new Intent();
				intent_home.setClass(ActivityBorrow.this, TabMenuActivity.class);

				Log.d("kh", "list home button ");
				startActivity(intent_home);
			}
		});
		
		btnsetting.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_setting = new Intent();
				intent_setting.setClass(ActivityBorrow.this, SettingActivity.class);

				Log.d("kh", "list setting button ");
				startActivity(intent_setting);
			}
		});
		btnrent.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
//				adapter.clear();
//				bkList = new ArrayList<BookInfo>();
				// TODO Auto-generated method stub
				Intent intent_search = new Intent();
				intent_search.setClass(ActivityBorrow.this, ActivityRent.class);

				Log.d("kh", "list home button ");
				startActivity(intent_search);
			}
		});

		
		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		String userid = sharedPref.getString("id", "");
		Log.d("kh", "아이디네 : "+userid);
		select(userid);
		
		
		mListView = (ListView) findViewById(R.id.book_list);
		brList = new ArrayList<BorrowInfo>();
		adapter = new ListAdapter(this, brList);

		mListView.setAdapter(adapter);
		
		
	}
	

	public String select(final String qtx) {

		try {
			return (new AsyncTask<String, String, String>() {

				ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();
				
				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("student", qtx));
					

					
					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/selectborrow.php");
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
							student = jo.getString("student");
							title = (jo.getString("title"));
							enddate = (jo.getString("enddate"));
							startdate = jo.getString("startdate");
							extension = jo.getString("extension");
							card = jo.getString("card");
							Log.d("kh", "ok");
							
							dataList.add(new BorrowInfo(getApplicationContext(),
								card, student, isbn, startdate, enddate,
									extension, title));
							
//							adapter.add(new BorrowInfo(getApplicationContext(),
//									card, student, isbn, startdate, enddate,
//									extension, title));
//							mListView.setAdapter(adapter);
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
//					txtIsbn.setText(isbn);
//					txtTitle.setText(title);
//					txtAuthor.setText(author);
//					txtPublisher.setText(publisher);
					adapter.clear();
					adapter.addAll(dataList);
					adapter.notifyDataSetChanged();

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}
	
	private class ListAdapter extends ArrayAdapter<BorrowInfo> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;
		private ArrayList<BorrowInfo> list;

	

		public ListAdapter(Context context, ArrayList<BorrowInfo> object) {

			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			list = object;
			

		}
		
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
				view = mInflater.inflate(R.layout.activity_borrowlist, null);

			} else {

				view = v;
			}

			// 자료를 받는다.
			final BorrowInfo data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				TextView bktitle = (TextView) view.findViewById(R.id.title);
				TextView bkenddate = (TextView) view.findViewById(R.id.enddate);
				CheckBox chbox = (CheckBox) view.findViewById(R.id.checkbox);
				chbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					  
		            @Override
		            public void onCheckedChanged(CompoundButton buttonView,
		                    boolean isChecked) {
		                if (buttonView.getId() == R.id.checkbox) {
		                    if (isChecked) {
		                    	//selectIsbn = list.get((int) getItemId(position)).getIsbn();
		        				selectCard = list.get(position).getCard();
		                        Toast.makeText(getApplicationContext(), "눌림", 1).show();
		                    } else {
		                        Toast.makeText(getApplicationContext(), "안눌림", 1).show();
		                    }
		                }
		            }
		        });

				bktitle.setText(data.getTitle());
				bkenddate.setText(data.getEnddate());

			}

			return view;

		}

	}
	
//	class ViewHolder {
//		
//
//		CheckBox checkbox;
//	}
	
	
}
