package com.example.smartlibrary;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
		
		
		// ArrayAdapter ��ü�� �����Ѵ�.
		ArrayAdapter<String> adapter;

		// ����Ʈ�䰡 ������ ������ ���ҽ��� ���ڿ� ������ �����Ѵ�
		// ��ü�� = new ArrayAdapter<��������>(�����Ҹ޼ҵ�, �����������۸��ҽ�, ���������ڿ�);
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, book_list_item);


		return v;
	}
	
	
	

	
	
}
