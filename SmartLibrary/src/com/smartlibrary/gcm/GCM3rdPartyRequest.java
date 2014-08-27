package com.smartlibrary.gcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class GCM3rdPartyRequest extends Thread {


    public final String TAG = "GCM3rdPartyRequest";
    private String mAddr;
    private String mResult;
    private String reg_id;
    private String msg;
 
    public GCM3rdPartyRequest(String addr, String reg_id, String msg)
    {
        mAddr=addr;
        mResult="";
        this.reg_id=reg_id;
        this.msg = msg;
    }
    
    @Override
    public void run() {
        Log.i(TAG, "In Run");
        // Thread를 싱행한다.
        super.run();
        
        StringBuilder html=new StringBuilder();
        
        // 서버를  연다. 
        URL url;
        try {
            // 서버를  연다.
            Log.i(TAG, "서버 연결");
            url = new URL(mAddr);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();    
            
            Log.i(TAG, "서버세팅");
            // 서버를 세팅한다. 서버 전송 방식은 POST를 사용한다.
            conn.setDefaultUseCaches(false); 
            conn.setDoInput(true); // 서버에서 읽기 모드 지정
            conn.setDoOutput(true); // 서버로 쓰기 모드 지정 
            conn.setRequestMethod("POST");
            
            // 데이터 전송양식은 x-www-form-urlencoded 로 사용한다. 필요하다면 , json을 사용해도 된다. 
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
 
            StringBuffer buffer = new StringBuffer();
            buffer.append("reg_id").append("=").append(reg_id).append("&");
            buffer.append("msg").append("=").append(msg);
            
            Log.i(TAG, "데이터 전송");
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            pw.write(buffer.toString());        //write하는순간 서버인(php)로  buffer 데이터 넘어감.
            pw.flush();
 
            Log.i(TAG, "데이터 읽어오기");
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
 
            while(true){
            String line=br.readLine();
            if(line==null) break;
            html.append(line);
            }
            br.close();
            mResult=html.toString();
            Log.i(TAG,mResult);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
    }
}
