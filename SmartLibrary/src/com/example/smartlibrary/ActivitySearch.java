package com.example.smartlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class ActivitySearch extends Activity {
	final String TAG = "SearchViewActivity";
	String[] book_list_item = new String[5];
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search);

		ListView list = (ListView) findViewById(R.id.book_list);
		Button btnhome = (Button) findViewById(R.id.home);

		final EditText e_id = (EditText) findViewById(R.id.book_input);
		e_id.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		e_id.setInputType(InputType.TYPE_CLASS_TEXT);
		Button button = (Button) findViewById(R.id.searchButton);
		
		btnhome.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_home = new Intent();
				intent_home
						.setClass(ActivitySearch.this, MainActivity.class);

				Log.d("kh", "list home button ");
				startActivity(intent_home);
				finish();
			}
		});

//		button.setOnEditorActionListener( new OnEditorActionListener() {
//			
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				// TODO Auto-generated method stub
//				 switch (actionId) {
//		            case EditorInfo.IME_ACTION_SEARCH:
//		            	String encode_str = null;
//		            	Log.d("kh", "키보드다");
//						encode_str = e_id.getText().toString();
//						if (encode_str.length() != 0) {
//							Intent intent_search = new Intent();
//							intent_search.setClass(ActivitySearch.this, SearchBookList.class);
//							// intent_search.putExtra("text",encode_str);
//							intent_search.putExtra("text", e_id.getText().toString());
//							Log.d("kh", "searchButton " + encode_str);
//							startActivity(intent_search);
//						} else {
//							Toast.makeText(getApplicationContext(), "검색어를 입력해주세요", 1).show();
//						}
//						
//		                return true;
//		 
//		            default:
//		            	String input = null;
//		            	input = e_id.getText().toString();
//						if (input.length() != 0) {
//							Intent intent_search = new Intent();
//							intent_search.setClass(ActivitySearch.this, SearchBookList.class);
//							// intent_search.putExtra("text",encode_str);
//							intent_search.putExtra("text", e_id.getText().toString());
//							Log.d("kh", "searchButton " + input);
//							startActivity(intent_search);
//						} else {
//							Toast.makeText(getApplicationContext(), "검색어를 입력해주세요", 1).show();
//						}
//						
//		                return false;
//		        }
//			}
//		});
		
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String encode_str = null;
				encode_str = e_id.getText().toString();
				if (encode_str.length() != 0) {
					Intent intent_search = new Intent();
					intent_search.setClass(ActivitySearch.this, SearchBookList.class);
					// intent_search.putExtra("text",encode_str);
					intent_search.putExtra("text", e_id.getText().toString());
					Log.d("kh", "searchButton " + encode_str);
					startActivity(intent_search);
				} else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"검색어를 입력해주세요.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 25, 400);
					toast.show();
				}

				// break;
				// }
			}
		});

		e_id.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent arg2) {
				// TODO Auto-generated method stub
				switch (actionId) {
				case EditorInfo.IME_ACTION_SEARCH:
					String encode_str = null;
	            	Log.d("kh", "키보드다");
					encode_str = e_id.getText().toString();
					if (encode_str.length() != 0) {
						Intent intent_search = new Intent();
						intent_search.setClass(ActivitySearch.this, SearchBookList.class);
						// intent_search.putExtra("text",encode_str);
						intent_search.putExtra("text", e_id.getText().toString());
						Log.d("kh", "searchButton " + encode_str);
						startActivity(intent_search);
					} else {
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

		// ArrayAdapter 객체를 생성한다.
		ArrayAdapter<String> adapter;

		// 리스트뷰가 보여질 아이템 리소스와 문자열 정보를 저장한다
		// 객체명 = new ArrayAdapter<데이터형>(참조할메소드, 보여질아이템리소스, 보여질문자열);
		adapter = new ArrayAdapter<String>(ActivitySearch.this,
				android.R.layout.simple_list_item_1, book_list_item);

		

	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final View view = findViewById(R.id.book_input);
		view.postDelayed(new Runnable() {
			public void run() {
				InputMethodManager manager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
				manager.showSoftInput(view, 0);
			}
		}, 100);
	}

}
