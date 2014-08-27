package com.example.smartlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class BarcodeScan extends Activity{
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_capture);
        
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage("com.google.zxing.client.android");
        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");

        try{
        startActivityForResult(intent, 0);
        }
        catch(ActivityNotFoundException e){
        	Log.d("___", e.toString());
        	// downloadFromMarket();

        }

        
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
        	 Log.d("kh", "0");
            if (resultCode == RESULT_OK) {
 //               Log.d("kh", "1");
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                Log.d("kh", "isbn: "+contents);


                Intent intent_search = new Intent();
				intent_search.setClass(BarcodeScan.this, ActivityChunggu.class);
				intent_search.putExtra("isbn", contents);
				startActivity(intent_search);
				Log.d("kh", "값얻음?");
                
                
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                
                Intent intent_person = new Intent();
    			intent_person.setClass(BarcodeScan.this,
    					SettingActivity.class); 
    			startActivity(intent_person);
    		finish();   
            
                
            }
        }
    }
    public void downloadFromMarket() {  
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);  
        downloadDialog.setTitle("Warning");  
        downloadDialog.setMessage("Barcode app not found. Download?");  
        downloadDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
             public void onClick(DialogInterface dialogInterface, int i) {  
                  Uri uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android");  
                  Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
                  startActivity(intent);  
             }  
        });  

        downloadDialog.show();  
   }  

}
