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
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.smartlibrary.book.GetBookdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityNFC<NFCReaderActivity> extends Activity {

	String sign;
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	String isbn;
	String card;
	int hexToint;
	int hexToint2;
	private TextView tagDesc;
	String bin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);

		tagDesc = (TextView) findViewById(R.id.tagDesc);
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		Intent intenta = getIntent();
		sign = intenta.getStringExtra("chung");
		isbn = intenta.getStringExtra("isbn");

		// nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (!nfcAdapter.isEnabled()) {

			// NFC Setting UI
			AlertDialog.Builder ad = new AlertDialog.Builder(ActivityNFC.this);
			ad.setTitle("Connection Error");
			ad.setMessage("설정에서 NFC을 ON 해주세요.");
			ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					startActivity(new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS));

				}
			});
			ad.create();
			ad.show();
		}

		showIntent();
	}

	public void showIntent() {
		Intent intent = new Intent(this, getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
	}

	protected void startNfcSettingsActivity() {
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			startActivity(new Intent(
					android.provider.Settings.ACTION_NFC_SETTINGS));
		} else {
			startActivity(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	@Override
	protected void onPause() {
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(this);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (nfcAdapter != null) {
			nfcAdapter
					.enableForegroundDispatch(this, pendingIntent, null, null);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) {
			byte[] tagId = tag.getId();
			tagDesc.setText("TagID: " + toHexString(tagId));
			String test = toHexString(tagId).substring(4,6);
			String strCut1 = toHexString(tagId).substring(4, 5);
			String strCut2 = toHexString(tagId).substring(5, 6);

			hexToint = Integer.parseInt(strCut1, 16);
			hexToint2 = Integer.parseInt(strCut2, 16);

			Log.d("kh", "binary : " + changebin(hexToint)+ " "
			+ changebin(hexToint2));
	bin = changebin(hexToint)
			+ changebin(hexToint2);
	Log.d("kh","NFC bin : "+bin);
			

			select(bin);
		}
	}

	public String changebin( int a)
	{
		String c="";
		if(a==1)
			return "0001";
		else if(a==2)
			return "0010";
		else if(a==3)
			return "0011";
		else if(a==4)
			return "0100";
		else if(a==5)
			return "0101";
		else if(a==6)
			return "0110";
		else if(a==7)
			return "0111";
		else if(a==8)
			return "1000";
		else if(a==9)
			return "1001";
		else if(a==10)
			return "1010";
		else if(a==11)
			return "1011";
		else if(a==12)
			return "1100";
		else if(a==13)
			return "1101";
		else if(a==14)
			return "1110";
		else if(a==15)
			return "1111";
		else if(a==0)
			return "0000";
		else return c;
	}
	public static final String CHARS = "0123456789ABCDEF";

	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
					CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
	}

	public String select(final String qtx) {

		try {
			return (new AsyncTask<String, String, String>() {

				ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();
				private InputStream is;
				private String line;
				private String result;

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("card", qtx));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/nfcfinder.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");

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

					try {
						Log.d("kh", "1");
						JSONObject json_data = null;
						json_data = new JSONObject(result);
						Log.d("kh", "1.5"); // 여기는 됨
						JSONArray bkName = json_data.getJSONArray("results");

						for (int i = 0; i < bkName.length(); i++) {
							Log.d("kh", "i " + i);
							JSONObject jo = bkName.getJSONObject(i);

							card = jo.getString("card");
							Log.d("kh", "ok");
						}

						return isbn;

					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}

					return null;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					if (card.equals(bin)) {
						Toast.makeText(ActivityNFC.this, "이미 등록된 카드입니다.",
								Toast.LENGTH_LONG).show();

					} else {
						
						Intent intent_person = new Intent();
						intent_person.setClass(ActivityNFC.this,
								GetBookdata.class);
						intent_person.putExtra(
								"nfc",bin);
						intent_person.putExtra("isbn", isbn);

						intent_person.putExtra("sign", sign);

						startActivity(intent_person);
					}

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

}
