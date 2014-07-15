package com.example.smartlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchViewerActivity extends Fragment {
	final String TAG = "SearchViewActivity";
	String[] book_list_item = new String[5];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_search, container, false);

		ListView list = (ListView) v.findViewById(R.id.book_list);

		final EditText e_id = (EditText) v.findViewById(R.id.book_input);
		e_id.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		e_id.setInputType(InputType.TYPE_CLASS_TEXT);
		Button button = (Button) v.findViewById(R.id.searchButton);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// switch (v.getId()) {
				//
				// case R.id.book_list:
				// Toast.makeText(getActivity(), "OneFragment",
				// Toast.LENGTH_SHORT)
				// .show();
				// break;
				// case R.id.searchButton:

				String encode_str = null;
				encode_str = e_id.getText().toString();
				Intent intent_search = new Intent();
				intent_search.setClass(getActivity(), SearchBookList.class);
				// intent_search.putExtra("text",encode_str);
				intent_search.putExtra("text", e_id.getText().toString());
				Log.d("kh", "searchButton " + encode_str);
				startActivity(intent_search);
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
					Intent intent_search = new Intent();
					intent_search.setClass(getActivity(), SearchBookList.class);
					intent_search.putExtra("text", e_id.getText().toString());
					startActivity(intent_search);
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
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, book_list_item);

		return v;
	}

}
