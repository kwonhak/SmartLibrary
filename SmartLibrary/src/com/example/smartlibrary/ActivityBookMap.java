package com.example.smartlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityBookMap extends Activity {

	String location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booktable);
		Intent intenta = getIntent();
		location = intenta.getStringExtra("location");
		int booklo = Integer.parseInt(location);
		Button btnhome = (Button) findViewById(R.id.home);
		Button btnsearch = (Button) findViewById(R.id.search);
		ImageView images = (ImageView) findViewById(R.id.image);
		

		//사진
		for(int i=100;i<1200;i+=100)
		{
			//사진을 바꾸는 코드 
			if(booklo>=i&&booklo<200)
			{
			
				images.setImageResource(R.drawable.site100);
				break;
			}
			else if(booklo>=i&&booklo<300)
			{
				images.setImageResource(R.drawable.site200);
				break;
			}
			else if(booklo>=i&&booklo<400)
			{
				images.setImageResource(R.drawable.site300);
				break;
			}
			else if(booklo>=i&&booklo<500)
			{
				images.setImageResource(R.drawable.site400);
				break;
			}
			else if(booklo>=i&&booklo<600)
			{
				images.setImageResource(R.drawable.site500);
				break;
			}
			else if(booklo>=i&&booklo<700)
			{
				images.setImageResource(R.drawable.site600);
				break;
			}
			else if(booklo>=i&&booklo<800)
			{
				images.setImageResource(R.drawable.site700);
				break;
			}
			else if(booklo>=i&&booklo<900)
			{
				images.setImageResource(R.drawable.site800);
				break;
			}
			else if(booklo>=i&&booklo<1000)
			{
				images.setImageResource(R.drawable.site900);
				break;
			}
			
		
		}
//			for(int j=Integer.parseInt(location)+1;j<Integer.parseInt(location)+7;j++)
//			{
//				//사진을 바꾸자
//				findViewById(getResources().getIdentifier("Text"+j, "id", getPackageName())).setBackgroundColor(Color.BLUE);
//			}
		
		

			btnhome.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent_home = new Intent();
					intent_home.setClass(ActivityBookMap.this, MainActivity.class);

					intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Log.d("kh", "list home button ");
					startActivity(intent_home);
					finish();
					
				}
			});
			

			btnsearch.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent_setting = new Intent();
					intent_setting.setClass(ActivityBookMap.this, ActivitySearch.class);

					intent_setting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Log.d("kh", "search button ");
					startActivity(intent_setting);
					finish();
					
				}
			});
			
	}
	
	
	
}
