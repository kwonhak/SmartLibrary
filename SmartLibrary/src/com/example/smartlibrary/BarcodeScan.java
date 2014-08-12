package com.example.smartlibrary;

import com.smartlibrary.book.GetBookdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BarcodeScan extends Activity{
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        
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
//        try {
//            Button scanner = (Button)findViewById(R.id.scanner);
//            scanner.setOnClickListener(new OnClickListener() {
//                
//                public void onClick(View v) {
//                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                    
//                    intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
//                    startActivityForResult(intent, 0);
//                }
// 
//            });
//            
//            Button scanner2 = (Button)findViewById(R.id.scanner2);
//            scanner2.setOnClickListener(new OnClickListener() {
//                
//                public void onClick(View v) {
//                   
//                }
// 
//            });
//            
//        } catch (ActivityNotFoundException anfe) {
//            Log.e("onCreate", "Scanner Not Found", anfe);
//                   }
        
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
//                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format , Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP, 25, 400);
//                toast.show();
                Log.d("kh", "액티비티변경");
                Intent intent_search = new Intent();
				intent_search.setClass(BarcodeScan.this, ActivityChunggu.class);
				intent_search.putExtra("isbn", contents);
				startActivity(intent_search);
				Log.d("kh", "액티비티변경되냐?");
                
                
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            
                
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
