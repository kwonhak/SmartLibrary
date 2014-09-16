package com.example.smartlibrary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

import com.smartlibrary.book.GetBookdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.RadioButton;
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
	private boolean mCheckBoxState = true;
	String selectcard = "";
	String userid = "";
	String selectIsbn = "";
	String selectExtension = "";
	// boolean isChecked;

	ListAdapter adapter;
	private ListView mListView = null;
	EditText e_id;

	private ArrayList<BorrowInfo> brList;
	// ArrayAdapter<String> adapter;

	private ArrayAdapter<String> mAaBooklist;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_borrow);

		Button btnrent = (Button) findViewById(R.id.rent);
		Button btnborrow = (Button) findViewById(R.id.borrow);
		Button btnhome = (Button) findViewById(R.id.home);
		Button btnsearch = (Button) findViewById(R.id.search);
		Button btnextension = (Button) findViewById(R.id.extension);
		// btnborrow.setBackgroundColor(R.drawable.borrowbutton_change);

		btnextension.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userid = sharedPref.getString("id", "");
				if (userid == "") {
					Toast toast = Toast.makeText(getApplicationContext(),
							"로그인을 해주세요.", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 25, 400);
					toast.show();
				} else {
					if (selectcard.equals("")) {

					} else {
						Log.d("kh", "selectextension : " + selectExtension);

						if (selectExtension.equals("1")) {
							Log.d("kh", "selectExtension : " + selectExtension);

							alreadyextension();

						} else {
							SimpleDateFormat dateformat = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date date = null;
							try {
								date = dateformat.parse(enddate);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							cal.add(Calendar.DATE, 7);
							String strDate = dateformat.format(cal.getTime());
							Log.d("kh", "date extension : " + strDate);
							extension(strDate);
							extcomplete();
						}
					}
				}
			}
		});

		btnhome.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_home = new Intent();
				intent_home.setClass(ActivityBorrow.this, MainActivity.class);

				Log.d("kh", "list home button ");
				startActivity(intent_home);
				finish();
			}
		});

		btnrent.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent_search = new Intent();
				intent_search.setClass(ActivityBorrow.this, ActivityRent.class);

				Log.d("kh", "list home button ");
				startActivity(intent_search);
				finish();
			}
		});

		btnsearch.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_setting = new Intent();
				intent_setting.setClass(ActivityBorrow.this,
						ActivitySearch.class);

				Log.d("kh", "search button ");
				startActivity(intent_setting);
				finish();

			}
		});

		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		String userid = sharedPref.getString("id", "");
		Log.d("kh", "아이디네 : " + userid);
		select(userid);

		mListView = (ListView) findViewById(R.id.book_list);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
							startdate = jo.getString("startdate");
							card = jo.getString("card");
							enddate = jo.getString("enddate");

							extension = jo.getString("extension");
							Log.d("kh", "ok");

							
							dataList.add(new BorrowInfo(
									getApplicationContext(), card, student,
									isbn, startdate, enddate, extension, title));

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
			//ViewHolder vholder = null;
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
				//CheckBox chbox = (CheckBox) view.findViewById(R.id.checkbox);
				CheckBox chbox = (CheckBox) view.findViewById(R.id.checkbox);
				
				
				
				chbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {						
							if (buttonView.getId() == R.id.checkbox) {
								if (isChecked) {						
									
									selectcard = list.get(position).getCard();
									selectIsbn = list.get(position).getIsbn();
									selectExtension = list.get(position)
											.getExtension();
									Log.d("kh", "extension" + selectExtension);
									
								} else {
									selectcard = "";
									selectIsbn = "";
									selectExtension = "";								
								}							
						}
					}
				});
				
				
//				
//				chbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//						// TODO Auto-generated method stub
//						if (buttonView.getId() == R.id.checkbox) {
//							if (isChecked) {									
//								selectcard = list.get(position).getCard();
//								selectIsbn = list.get(position).getIsbn();
//								selectExtension = list.get(position)
//										.getExtension();
//								Log.d("kh", "extension" + selectExtension);
//								
//							} else {
//								selectcard = "";
//								selectIsbn = "";
//								selectExtension = "";								
//							}							
//						}
//					}
//				});
				

				bktitle.setText(data.getTitle());
				bkenddate.setText(data.getEnddate());

			}

			return view;

		}

	}

	public String extension(final String strdate) {

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

					// nameValuePairs.add(new BasicNameValuePair("student",
					// userid));
					nameValuePairs.add(new BasicNameValuePair("card",
							selectcard));
					// nameValuePairs.add(new BasicNameValuePair("isbn",
					// selectIsbn));
					nameValuePairs.add(new BasicNameValuePair("enddate",
							strdate));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/extension.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");

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

	public void alreadyextension() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 여기서
																		// this는
																		// Activity의
																		// this

		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("이미 연장한 도서입니다.") // 제목 설정
				.setMessage("확인 버튼을 눌러주세요.") // 메세지 설정
				.setCancelable(false) // 뒤로 버튼 클릭시 취소 가능 설정
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					// 확인 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton) {

						// finish();

					}
				});

		AlertDialog dialog = builder.create(); // 알림창 객체 생성
		dialog.show(); // 알림창 띄우기
	}

	public void extcomplete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 여기서
																		// this는
																		// Activity의
																		// this

		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("연장되었습니다.") // 제목 설정
				.setMessage("확인 버튼을 눌러주세요.") // 메세지 설정
				.setCancelable(false) // 뒤로 버튼 클릭시 취소 가능 설정
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					// 확인 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton) {

						// finish();
						adapter.clear();
						Intent intent_person = new Intent();
						intent_person.setClass(ActivityBorrow.this,
								ActivityBorrow.class);
						startActivity(intent_person);

					}
				});

		AlertDialog dialog = builder.create(); // 알림창 객체 생성
		dialog.show(); // 알림창 띄우기
	}
	


}
