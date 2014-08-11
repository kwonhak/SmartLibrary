package com.example.smartlibrary;

import android.content.Context;

public class BorrowInfo {

	private String isbn;
	private String title;
	private String card;
	private String student;
	private String startdate;
	private String enddate;
	private String extension;

	public BorrowInfo(Context context, String b_card, String b_student, String b_isbn, String b_startdate, String b_enddate, String b_extension, String b_title ) {
		isbn = b_isbn;
		title = b_title;
		card = b_card;
		student = b_student;
		startdate = b_startdate;
		enddate = b_enddate;
		extension = b_extension;
		

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

	public String getEnddate() {
		return enddate;
	}
	public String getExtension() {
		return extension;
	}

	

}
