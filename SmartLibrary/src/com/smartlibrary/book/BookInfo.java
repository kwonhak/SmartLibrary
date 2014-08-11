package com.smartlibrary.book;

import android.content.Context;


public class BookInfo {
	private String isbn;
	private String num;
	private String category;
	private String author;
	private String publisher;
	private String pubdate;
	private String title;
	private String location;
	private String reservation;
	private String card;

	public BookInfo(Context context, String b_isbn, String b_num,
			String b_category, String b_author, String b_publisher,
			String b_pubdate, String b_title,String b_location, String b_reservation,String b_card) {
		isbn = b_isbn;
		num = b_num;
		category = b_category;
		author = b_author;
		publisher = b_publisher;
		pubdate = b_pubdate;
		title = b_title;
		location=b_location;
		reservation = b_reservation;
		card = b_card;

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
	
	public String getCard() {
		return card;
	}

	public String getAuthor() {
		return author;
	}
	public String getLocation() {
		return location;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getPubdate() {
		return pubdate;
	}

	public String gettitle() {
		return title;
	}

	public String getReservation() {
		return reservation;
	}

}