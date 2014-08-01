package com.example.smartlibrary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Sharedpreferences extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
         
    }
     
    // 값 불러오기
     String getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString("id", "");
    }
     
    // 값 저장하기
    public void savePreferences(String id){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id", id);
        editor.commit();
    }
     
    // 값(Key Data) 삭제하기
    public void removePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("id");
        editor.commit();
    }
     
    // 값(ALL Data) 삭제하기
    public void removeAllPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
