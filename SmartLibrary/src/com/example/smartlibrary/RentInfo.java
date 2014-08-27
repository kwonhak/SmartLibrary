package com.example.smartlibrary;

import android.content.Context;

public class RentInfo {


	private String isbn;
	private String title;
	private String card;
	private String student;
	private String startdate;


	public RentInfo(Context context, String b_card, String b_student, String b_isbn, String b_startdate, String b_title ) {
		isbn = b_isbn;
		title = b_title;
		card = b_card;
		student = b_student;
		startdate = b_startdate;

	}

	public String getIsbn() {
		return isbn;
	}

	public String getCard() {
		return card;
	}

	public String getTitle() {
		return title;
	}

	public String getStudent() {
		return student;
	}

	public String getStartdate() {
		return startdate;
	}


}
