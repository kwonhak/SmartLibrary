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
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class SearchViewerActivity extends Fragment  {
	final String TAG = "SearchViewActivity";
	String[] book_list_item = new String[5] ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_search, container, false);

		ListView list = (ListView) v.findViewById(R.id.book_list);

	   final EditText e_id = (EditText) v.findViewById(R.id.book_input);
		Button button = (Button) v.findViewById(R.id.searchButton);
	
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {

				case R.id.book_list:
					Toast.makeText(getActivity(), "OneFragment", Toast.LENGTH_SHORT)
							.show();
					break;
				case R.id.searchButton:
					

					Intent intent_search = new Intent();
					intent_search.setClass(getActivity(), SearchBookList.class);
					intent_search.putExtra("text", e_id.getText().toString());
					Log.d(TAG, "searchButton " + intent_search);
					startActivity(intent_search);
					break;
				}
			}
		});
		
		
		// ArrayAdapter 객체를 생성한다.
		ArrayAdapter<String> adapter;

		// 리스트뷰가 보여질 아이템 리소스와 문자열 정보를 저장한다
		// 객체명 = new ArrayAdapter<데이터형>(참조할메소드, 보여질아이템리소스, 보여질문자열);
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, book_list_item);


		return v;
	}
	
	
	

	
	
}
