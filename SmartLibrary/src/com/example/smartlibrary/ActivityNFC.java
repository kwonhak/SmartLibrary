package com.example.smartlibrary;

import com.smartlibrary.book.GetBookdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;



public class ActivityNFC<NFCReaderActivity> extends Activity{

	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	
	private TextView tagDesc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        
        tagDesc = (TextView)findViewById(R.id.tagDesc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        
        //nfcAdapter = NfcAdapter.getDefaultAdapter(this);        
        if (!nfcAdapter.isEnabled())     {   
         
         //NFC Setting UI
          AlertDialog.Builder ad=new AlertDialog.Builder(ActivityNFC.this);
          ad.setTitle("Connection Error");
          ad.setMessage("설정에서 NFC을 ON 해주세요.");
          ad.setPositiveButton("OK",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,int whichButton) {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));    
            
           }
          });
          ad.create();
          ad.show();
         }
        //nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    protected void startNfcSettingsActivity() {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
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
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) {
			byte[] tagId = tag.getId();
			tagDesc.setText("TagID: " + toHexString(tagId));
			String strCut1 = toHexString(tagId).substring(0,1);
			String strCut2 = toHexString(tagId).substring(1,2);

			int hexToint = Integer.parseInt(strCut1, 16);
			int hexToint2 = Integer.parseInt(strCut2, 16);

			Log.d("kh","binary : "+Integer.toBinaryString(hexToint)+" "+Integer.toBinaryString(hexToint2));
			
			
//			Intent intent_person = new Intent();
//			intent_person.setClass(ActivityNFC.this, GetBookdata.class);
//			//intent_person.putExtra("chung", chung.getText().toString());
//			startActivity(intent_person);
		}
	}
	
	
	public static final String CHARS = "0123456789ABCDEF";
	
	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
				.append(CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
	}
}
