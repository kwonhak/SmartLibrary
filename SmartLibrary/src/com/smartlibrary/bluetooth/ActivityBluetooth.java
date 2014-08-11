package com.smartlibrary.bluetooth;

import com.example.smartlibrary.R;
import com.example.smartlibrary.R.id;
import com.example.smartlibrary.R.layout;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ActivityBluetooth  {
	  private static final String TAG = "kh";
	     
	    // Intent request code
	    private static final int REQUEST_CONNECT_DEVICE = 1;
	    private static final int REQUEST_ENABLE_BT = 2;
	     
	    // Layout
	    private Button btn_Connect;
	    private TextView txt_Result;
	     
	    private BluetoothService btService = null;
	    
	    private String mConnectedDeviceName = null;
	    // Array adapter for the conversation thread
	    private ArrayAdapter<String> mConversationArrayAdapter;
	    // String buffer for outgoing messages
	    private StringBuffer mOutStringBuffer;
	    // Local Bluetooth adapter
	    private BluetoothAdapter mBluetoothAdapter = null;
	     
	     
	    private final Handler mHandler = new Handler() {
	 
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	        }
	         
	    };
	 
	   
	    
	    
	 
}
