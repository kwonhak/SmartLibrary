package com.smartlibrary.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.example.smartlibrary.R;

public class CommonUtilities {


    /**
     * Google API project id registered to use GCM.
     */
    static final String SENDER_ID = "940516598130";  // Google API console ?먯꽌 ?살? Project ID 媛믪쓣 ?ㅼ젙 
    static final String SERVER_URL = "http://112.108.40.87/msg_receiver.php";  // 데이터베이스를 등록하는 서버
  
    /**
     * Intent used to display a message in the screen.
     */
    static final String DISPLAY_MESSAGE_ACTION="com.google.android.gcm.pushservertest.app.DISPLAY_MESSAGE";
 
    /**
     * Intent's extra that contains the message to be displayed.
     */
    static final String EXTRA_MESSAGE = "message";
 
    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static String PROPERTY_REG_ID = "registration_id";
    
    /**
     * Notification's Icon
     */
    public static int noti_icon = R.drawable.ic_launcher;
    
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    
    /**
     * Message to server to request
     *
     */
    public static String register = "register";
    public static String delete = "delete";
    
    
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
     static String JsonParser(String Jsondata)
     {
         String resultStr = "";
         try {
            
             JSONObject jObj = new JSONObject(Jsondata);
             resultStr += jObj.getString("msg");
             
             
        } catch (JSONException e) {
            // TODO: handle exception
        }
         
         return resultStr;
     }
}
