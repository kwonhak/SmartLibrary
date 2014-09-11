package com.smartlibrary.bluetooth;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.smartlibrary.R;

public class ActivityReservationAlert extends Activity {
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;
	String messagesender;
	String message;

	InputStream is = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gcmmessage);
		alert();

	}

	public void alert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 여기서
																		// this는
																		// Activity의
																		// this

		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("message")
				// 제목 설정
				.setMessage("이미 예약된 도서입니다")
				// 메세지 설정
				.setCancelable(false)
				// 뒤로 버튼 클릭시 취소 가능 설정
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					// 확인 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton) {
						

					}
				});

		AlertDialog dialog = builder.create(); // 알림창 객체 생성
		dialog.show(); // 알림창 띄우기
	}
}
