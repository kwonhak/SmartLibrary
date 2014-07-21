package com.example.smartlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityBluetooth extends Activity implements OnClickListener {
	  private static final String TAG = "kh";
	     
	    // Intent request code
	    private static final int REQUEST_CONNECT_DEVICE = 1;
	    private static final int REQUEST_ENABLE_BT = 2;
	     
	    // Layout
	    private Button btn_Connect;
	    private TextView txt_Result;
	     
	    private BluetoothService btService = null;
	     
	     
	    private final Handler mHandler = new Handler() {
	 
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	        }
	         
	    };
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.e(TAG, "onCreate");
	         
	        setContentView(R.layout.bluetooth);
	         
	        /** Main Layout **/
	        btn_Connect = (Button) findViewById(R.id.btn_connect);
	        txt_Result = (TextView) findViewById(R.id.txt_result);
	         
	        btn_Connect.setOnClickListener(this);
	         
	        // BluetoothService 클래스 생성
	        if(btService == null) {
	            btService = new BluetoothService(this, mHandler);
	        }
	    }
	 
	    @Override
	    public void onClick(View v) {
	        if(btService.getDeviceState()) {
	            // 블루투스가 지원 가능한 기기일 때
	            btService.enableBluetooth();
	        } else {
	            finish();
	        }
	    }
	     
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        Log.d(TAG, "onActivityResult " + resultCode);
	         
	        switch (requestCode) {
	 
	        case REQUEST_ENABLE_BT:
	            // When the request to enable Bluetooth returns
	            if (resultCode == Activity.RESULT_OK) {
	                 
	            } else {
	 
	                Log.d(TAG, "Bluetooth is not enabled");
	            }
	            break;
	        }
	    }
}
