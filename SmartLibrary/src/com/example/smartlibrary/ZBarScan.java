package com.example.smartlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

public class ZBarScan extends Activity {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_zbar);
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.CODABAR,Symbol.EAN13,Symbol.ISBN13});
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }

    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ZBAR_SCANNER_REQUEST:
                if (resultCode == RESULT_OK) {
                    
                	//Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                	 Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                     Toast.makeText(this, "Scan Result Type = " + data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0), Toast.LENGTH_SHORT).show();

                	String contents = data.getStringExtra(ZBarConstants.SCAN_RESULT);
    				//String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
    				// Handle successful scan
    				Log.d("kh", "isbn: " + contents);

    				Intent intent_search = new Intent();
    				intent_search.setClass(ZBarScan.this, ActivityChunggu.class);
    				intent_search.putExtra("isbn", contents);
    				startActivity(intent_search);
    				Log.d("kh", "값얻음?");
                
                } else if(resultCode == RESULT_CANCELED && data != null) {
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    if(!TextUtils.isEmpty(error)) {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                    
//                    Toast toast = Toast.makeText(this, "Scan was Cancelled!",
//    						Toast.LENGTH_LONG);
//    				toast.setGravity(Gravity.TOP, 25, 400);
//    				toast.show();

    				Intent intent_person = new Intent();
    				intent_person.setClass(ZBarScan.this, MainActivity.class);
    				startActivity(intent_person);
    				finish();
                }
                break;
        }
    }
}
