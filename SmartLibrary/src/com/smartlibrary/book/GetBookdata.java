package com.smartlibrary.book;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smartlibrary.R;
import com.example.smartlibrary.SettingActivity;


public class GetBookdata extends Activity {

	String id;
	InputStream is = null;
	String result = null;
	String querytxt;
	String line = null;
	String searchIsbn = null;
	TextView txtTitle;
	TextView txtAuthor;
	TextView txtPublisher;
	TextView txtIsbn;
	TextView txtLocation;
	TextView txtReservation;
	String title;
	String card;
	String category;
	String author;
	String isbn;
	String publisher;
	String location = "1";
	String reservation = "0";
	String pubdate;
	String sign;
	Document doc;
	EditText chung;

	private static final char[] CHO =
	/* ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ */
	{ 0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143,

	0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d,

	0x314e };
	private static final char[] JUN =
	/* ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ */
	{ 0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157,

	0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160,

	0x3161, 0x3162, 0x3163 };
	/* X ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ */
	private static final char[] JON = { 0x0000, 0x3131, 0x3132, 0x3133, 0x3134,
			0x3135, 0x3136, 0x3137, 0x3139,

			0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141,
			0x3142,

			0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c,
			0x314d, 0x314e };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enrollbook);

		
		// public GetBookInfo throws ParserConfigurationException,
		// MalformedURLException, IOException, AXException{
		 
		//showAlertChung();
		Intent intent = getIntent();
		sign = intent.getStringExtra("sign");
		card = intent.getStringExtra("nfc");
		searchIsbn = intent.getStringExtra("isbn");
		Log.d("kh", "넘어온값 : " + sign + card);
		location =   "책장/"+sign;
	
		txtTitle = (TextView)findViewById(R.id.title);
		txtIsbn =  (TextView)findViewById(R.id.isbn);
		txtAuthor =  (TextView)findViewById(R.id.author);
		txtPublisher =  (TextView)findViewById(R.id.publisher);
		
		select();

	
		
		

	}


	public String send() {

		try {
			return (new AsyncTask<String, String, String>() {

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					// 여기서 데이터 전송
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("title", title));
					nameValuePairs
							.add(new BasicNameValuePair("author", author));
					nameValuePairs.add(new BasicNameValuePair("category",
							category));
					nameValuePairs.add(new BasicNameValuePair("isbn", isbn));
					nameValuePairs.add(new BasicNameValuePair("publisher",
							publisher));
					nameValuePairs.add(new BasicNameValuePair("location",
							location));
					nameValuePairs.add(new BasicNameValuePair("pubdate",
							pubdate));
					nameValuePairs.add(new BasicNameValuePair("reservation",
							reservation));
					nameValuePairs.add(new BasicNameValuePair("card",
							card));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/enrollbook.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "sending connection success");

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "UTF_8"), 8);
						StringBuilder sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						Log.d("kh", "result");
						result = sb.toString();
						Log.d("kh", result);
					} catch (Exception e) {
						Log.e("Fail 2", e.toString());

					}
				
					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					
				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	public String select() {

		try {
			return (new AsyncTask<String, String, String>() {

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					// 여기서 데이터 전송
					try {
						DocumentBuilder docBuilder;
						String xmlData;

						docBuilder = DocumentBuilderFactory.newInstance()
								.newDocumentBuilder();

						URL url = new URL(
								"http://book.interpark.com/api/search.api?"
										+ "key=BC35EF5561A23FF8286B4F06137D0161AA3F1C9637E1A1A7FBD31F12643FA7CF"
										+ "&query=" + searchIsbn // 여기는 쿼리를
																	// 넣으세요(검색어)
										+ "&queryType=isbn");
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();

						byte[] bytes = new byte[4096];
						InputStream in = conn.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						while (true) {
							int red = in.read(bytes);
							if (red < 0)
								break;
							baos.write(bytes, 0, red);
						}
						xmlData = baos.toString("utf-8"); // 구글 날씨 API 는 현재
															// euc-kr 이다.
						baos.close();
						in.close();
						doc = docBuilder.parse(new InputSource(
								new StringReader(xmlData)));
						// System.out.println(xmlData);
						// 여기까지 진행이 되면 xml 파싱은 끝이 나며 doc에는 DOM 형태의 문서 구조를 갖는다.
						// 이제 DOM 을 파싱해보자.
						//Log.d("kh", xmlData);
						
						

						return xmlData;

					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;

					NodeList titleNodeList = doc.getElementsByTagName("title");
					Element titleElement = (Element) titleNodeList.item(1);
					NodeList childTitleNodeList = titleElement.getChildNodes();
					title = ((Node) childTitleNodeList.item(0)).getNodeValue();
					Log.d("kh", "이거슨 타이틀 : " + title);

					NodeList publisherNodeList = doc
							.getElementsByTagName("publisher");
					Element publisherElement = (Element) publisherNodeList
							.item(0);
					NodeList childPublisherNodeList = publisherElement
							.getChildNodes();
					publisher = ((Node) childPublisherNodeList.item(0))
							.getNodeValue();
					Log.d("kh", "이거슨 출판사 : " + publisher);

					NodeList authorNodeList = doc
							.getElementsByTagName("author");
					Element authorElement = (Element) authorNodeList.item(0);
					NodeList childAuthorNodeList = authorElement
							.getChildNodes();
					author = ((Node) childAuthorNodeList.item(0))
							.getNodeValue();
					Log.d("kh", "이거슨 저자 : " + author);

					NodeList isbnNodeList = doc.getElementsByTagName("isbn");
					Element isbnElement = (Element) isbnNodeList.item(0);
					NodeList childIsbnNodeList = isbnElement.getChildNodes();
					isbn = ((Node) childIsbnNodeList.item(0)).getNodeValue();
					Log.d("kh", "이거슨 isbn : " + isbn);

					NodeList pubdateNodeList = doc
							.getElementsByTagName("pubDate");
					Element pubdateElement = (Element) pubdateNodeList.item(1);
					NodeList childPubdateNodeList = pubdateElement
							.getChildNodes();
					pubdate = ((Node) childPubdateNodeList.item(0))
							.getNodeValue();
					Log.d("kh", "이거슨 출판일 : " + pubdate);
					
					
					changeChar();
					
					
					Log.d("kh", "이름기호 : " + category);
					send();
					txtAuthor.setText(author);
					txtIsbn.setText(isbn);
					txtTitle.setText(title);
					txtPublisher.setText(publisher);
					 alert();
					
					
				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}
	public void alert()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("도서 등록이 완료되었습니다.")        // 제목 설정
		.setMessage("확인을 누르시면 Setting화면으로 넘어갑니다.")        // 메세지 설정
		.setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
		.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
		 // 확인 버튼 클릭시 설정
		public void onClick(DialogInterface dialog, int whichButton){
			Intent intent_person = new Intent();
			intent_person.setClass(GetBookdata.this,
					SettingActivity.class);
			startActivity(intent_person);
		finish();   

		}
		});

		AlertDialog dialog = builder.create();    // 알림창 객체 생성
		dialog.show();    // 알림창 띄우기
	}


	public void changeChar() {

		String lastname; // 성
		String firstname_1 = null; // 이름
		String firstname_2 = null; // 이름
		char titleVal = (char) (publisher.charAt(0) - 0xAC00);
		char titlename = (char) (((titleVal - (titleVal % 28)) / 28) / 21);
		//System.out.println(CHO[titlename]);

		List<Map<String, Integer>> list = new ArrayList<Map<String,

		Integer>>();
		String tempStr = author;
//		String lastStr = "";

		System.out.println(tempStr);
		for (int i = 1; i < 2; i++) {
//			Map<String, Integer> map = new HashMap<String, Integer>();
			char test = tempStr.charAt(i);

			if (test >= 0xAC00) {
				char uniVal = (char) (test - 0xAC00);

				char cho = (char) (((uniVal - (uniVal % 28)) / 28) / 21);
				char jun = (char) (((uniVal - (uniVal % 28)) / 28) % 21);
				char jon = (char) (uniVal % 28);

				// 이건 성부분
				System.out.println("성이다");
				System.out.println("" + test + "// 0x"
						+ Integer.toHexString((char) test));

//				Character cr = new Character(test); // char을 Object로 랩
//				lastname = cr.toString();

				System.out.println("이름이다");
				System.out.println("" + CHO[cho] + "// 0x"
						+ Integer.toHexString((char) cho));
				// Character firstn = new Character(CHO[cho]);
				// firstname_1 = firstn.toString();

				switch (CHO[cho]) {
				case 0x3131:
					firstname_1 = "1";
					break;
				case 0x3132:
					firstname_1 = "1";
					break;
				case 0x3134:// ㄴ
					firstname_1 = "19";
					break;
				case 0x3137:// ㄷ
					firstname_1 = "2";
					break;
				case 0x3138:// ㄸ
					firstname_1 = "2";
					break;
				case 0x3139:// ㄹ
					firstname_1 = "29";
					break;
				case 0x3141:// ㅁ
					firstname_1 = "3";
					break;
				case 0x3142:// ㅂ
					firstname_1 = "4";
					break;
				case 0x3143:// ㅃ
					firstname_1 = "4";
					break;
				case 0x3145:// ㅅ
					firstname_1 = "5";
					break;
				case 0x3146:// ㅆ
					firstname_1 = "5";
					break;
				case 0x3147:// ㅇ
					firstname_1 = "6";
					break;
				case 0x3148:// ㅈ
					firstname_1 = "7";
					break;
				case 0x3149:// ㅉ
					firstname_1 = "7";
					break;
				case 0x314a:// ㅊ
					firstname_1 = "8";
					break;
				case 0x314b:// ㅋ
					firstname_1 = "87";
					break;
				case 0x314c:// ㅌ
					firstname_1 = "88";
					break;
				case 0x314d:// ㅍ
					firstname_1 = "89";
					break;
				case 0x314e:// ㅎ
					firstname_1 = "9";
					break;
				}
				System.out.println("첫번째 초성 " + firstname_1);

				System.out.println("" + JUN[jun] + "// 0x"
						+ Integer.toHexString((char) jun));
				// Character secn = new Character(JUN[jun]);
				// firstname_2 = secn.toString();

				if (CHO[cho] == 0x314a) {
					switch (JUN[jun]) {
					case 0x314f:// ㅏ
						firstname_2 = "2";
						break;
					case 0x3150:// ㅐ
						firstname_2 = "2";
						break;
					case 0x3151:// ㅑ
						firstname_2 = "2";
						break;
					case 0x3152:// ㅒ
						firstname_2 = "2";
						break;
					case 0x3153:// ㅓ
						firstname_2 = "3";
						break;
					case 0x3154:// ㅔ
						firstname_2 = "3";
						break;
					case 0x3155:// ㅕ
						firstname_2 = "3";
						break;
					case 0x3156:// ㅖ
						firstname_2 = "3";
						break;
					case 0x3157:// ㅗ
						firstname_2 = "4";
						break;
					case 0x3158:// ㅘ
						firstname_2 = "4";
						break;
					case 0x3159:// ㅙ
						firstname_2 = "4";
						break;
					case 0x315a:// ㅚ
						firstname_2 = "4";
						break;
					case 0x315b:// ㅛ
						firstname_2 = "4";
						break;
					case 0x315c:// ㅜ
						firstname_2 = "5";
						break;
					case 0x315d:// ㅝ
						firstname_2 = "5";
						break;
					case 0x315e:// ㅞ
						firstname_2 = "5";
						break;
					case 0x315f:// ㅟ
						firstname_2 = "5";
						break;
					case 0x3160:// ㅠ
						firstname_2 = "5";
						break;
					case 0x3161:// ㅡ
						firstname_2 = "5";
						break;
					case 0x3162:// ㅢ
						firstname_2 = "5";
						break;
					case 0x3163:// ㅣ
						firstname_2 = "6";
						break;
					}

				} else {
					switch (JUN[jun]) {
					case 0x314f:// ㅏ
						firstname_2 = "2";
						break;
					case 0x3150:// ㅐ
						firstname_2 = "3";
						break;
					case 0x3151:// ㅑ
						firstname_2 = "3";
						break;
					case 0x3152:// ㅒ
						firstname_2 = "3";
						break;
					case 0x3153:// ㅓ
						firstname_2 = "4";
						break;
					case 0x3154:// ㅔ
						firstname_2 = "4";
						break;
					case 0x3155:// ㅕ
						firstname_2 = "4";
						break;
					case 0x3156:// ㅖ
						firstname_2 = "4";
						break;
					case 0x3157:// ㅗ
						firstname_2 = "5";
						break;
					case 0x3158:// ㅘ
						firstname_2 = "5";
						break;
					case 0x3159:// ㅙ
						firstname_2 = "5";
						break;
					case 0x315a:// ㅚ
						firstname_2 = "5";
						break;
					case 0x315b:// ㅛ
						firstname_2 = "5";
						break;
					case 0x315c:// ㅜ
						firstname_2 = "6";
						break;
					case 0x315d:// ㅝ
						firstname_2 = "6";
						break;
					case 0x315e:// ㅞ
						firstname_2 = "6";
						break;
					case 0x315f:// ㅟ
						firstname_2 = "6";
						break;
					case 0x3160:// b
						firstname_2 = "6";
						break;
					case 0x3161:// ㅡ
						firstname_2 = "7";
						break;
					case 0x3162:// ㅢ
						firstname_2 = "7";
						break;
					case 0x3163:// ㅣ
						firstname_2 = "8";
						break;
					}
					System.out.println("두번째 초성 " + firstname_2);

				}
//				if ((char) jon != 0x0000)
//					System.out.println("" + JON[jon] + "// 0x"
//							+ Integer.toHexString((char) jon));
//
//				map.put("cho", (int) cho);
//				map.put("jun", (int) jun);
//				map.put("jon", (int) jon);
//				list.add(map);
				
			}
		}

		category = sign + author.charAt(0) + firstname_1 + firstname_2
				+ CHO[titlename];

	}

}
