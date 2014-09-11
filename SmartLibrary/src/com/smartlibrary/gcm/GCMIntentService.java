package com.smartlibrary.gcm;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.smartlibrary.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {

	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;
	
	public static final int NOTIFICATION_ID = 1;
	private static final String TAG = GCMIntentService.class.getSimpleName();

	public static final String PROJECT_ID = "940516598130";

	public GCMIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		sharedPref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		
		  Bundle extras = intent.getExtras();
		    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		    // The getMessageType() intent parameter must be the intent you received
		    // in your BroadcastReceiver.
		    String messageType = gcm.getMessageType(intent);
		    
		    if (!extras.isEmpty())
		    { // has effect of unparcelling Bundle
		      /*
		       * Filter messages based on message type. Since it is likely that GCM will
		       * be extended in the future with new message types, just ignore any
		       * message types you're not interested in, or that you don't recognize.
		       */
		      if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
		      {
		        sendNotification("Send error: " + extras.toString());
		      }
		      else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
		      {
		        sendNotification("Deleted messages on server: " + extras.toString());
		        // If it's a regular GCM message, do some work.
		      }
		      else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
		      {
		    	 String[] arrays;
		        String msg = intent.getStringExtra("msg");
		        arrays=msg.split("@");
		   
		        Log.d("kh","받은메시지확인 : "+arrays[1]);
		        sharedEditor.putString("msgsender",arrays[1]);
				sharedEditor.commit();
		         
		        // Post notification of received message.
//		            sendNotification("Received: " + extras.toString());
		        sendNotification("받은 메시지: " + arrays[0]);
		        Log.i("GcmIntentService.java | onHandleIntent", "Received: " + extras.toString());
		      }
		    }
		    // Release the wake lock provided by the WakefulBroadcastReceiver.
		    GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(getApplicationContext(), ActivityMessage.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("msg", msg);

		startActivity(intent);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.appicon)
				.setContentTitle("SmartLibrary")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setAutoCancel(true)
				.setVibrate(new long[] { 0, 500 });

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	

}
