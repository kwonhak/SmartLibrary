package com.example.smartlibrary;

import com.smartlibrary.book.GetBookdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ActivityChunggu extends Activity{

	EditText chung;
	String isbn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example);
		Intent intent = getIntent();
		isbn = intent.getStringExtra("isbn");
		showAlertChung();
		
		
	}
	private void showAlertChung() {
		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout loginLayout = (LinearLayout) vi.inflate(
				R.layout.category_input, null);
		chung = (EditText) loginLayout.findViewById(R.id.category);
//		//final EditText e_id = (EditText) findViewById(R.id.book_input);
//		chung.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//		chung.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);

		new AlertDialog.Builder(this).setTitle("청구기호").setView(loginLayout)
				.setNeutralButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Log.d("kh", "청구기호 : " + chung.getText().toString());

						//sign=chung.getText().toString();
						Intent intent_person = new Intent();
						intent_person.setClass(ActivityChunggu.this, ActivityNFC.class);
						intent_person.putExtra("chung", chung.getText().toString());
						intent_person.putExtra("isbn", isbn);
						intent_person.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent_person);
					}
				}).show();
	}
	


}
