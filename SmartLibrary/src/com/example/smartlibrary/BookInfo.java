package com.example.smartlibrary;

import android.content.Context;


public class BookInfo {
	private String isbn;
	private String num;
	private String category;
	private String author;
	private String publisher;
	private String pubdate;
	private String bookname;
	private String reservation;

	public BookInfo(Context context, String b_isbn, String b_num,
			String b_category, String b_author, String b_publisher,
			String b_pubdate, String b_bookname, String b_reservation) {
		isbn = b_isbn;
		num = b_num;
		category = b_category;
		author = b_author;
		publisher = b_publisher;
		pubdate = b_pubdate;
		bookname = b_bookname;
		reservation = b_reservation;

	}

	public String getIsbn() {
		return isbn;
	}

	public String getNum() {
		return num;
	}

	public String getCategory() {
		return category;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getPubdate() {
		return pubdate;
	}

	public String getBookname() {
		return bookname;
	}

	public String getReservation() {
		return reservation;
	}

}