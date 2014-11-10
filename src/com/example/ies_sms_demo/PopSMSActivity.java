package com.example.ies_sms_demo;


import com.example.ies_sms_demo.model.UpdateState;
import com.example.ies_sms_demo.receiver.PopMessage;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class PopSMSActivity extends Activity {
	  public static final int NOTIFICATION_ID = 1;
	  /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_pop_sms);
	       ActionBar actionBar = getActionBar();
	        actionBar.hide();
	       // no need for XML layouts right now
	       // we will use a dialog instead
	       //setContentView(R.layout.main); 
	       
	       // retrieve Serializable sms message object
	       // by the key "msg" used to pass it
	       Intent in = this.getIntent();
	       PopMessage msg = (PopMessage) in.getSerializableExtra("msg");
	       
//	        Case where we launch the app to test the UI
//	        i.e. no incoming SMS
	       if(msg != null){
	    	   saveData(msg);
	    	   showDialog(msg);
		       sendNotification(msg);
	       }
	      
	   }
	   private void showDialog(PopMessage msg){
		   
		    final String sender = msg.sender;
		    final String body = msg.msgType+" Received";
		 
		    final String display = sender + "\n"
		            + msg.getShortDate( msg.timestamp )+ "\n"
		            + body + "\n";
		 
		    // Display in Alert Dialog
		    final PopMessage clone=msg;
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage(display)
		    .setCancelable(false)
		    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		        	goMain(clone);
		        }
		    })
		    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		                  // go back to the phone home screen
		                  goHome();
		        }
		    });
		    AlertDialog alert = builder.create();
		    alert.show();
		    try {
		        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		        r.play();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	   private void goMain(PopMessage msg){
		   Intent intent = this.getIntent();
			  intent.setClass(this, EquipementSlideActivity.class);
			  intent.putExtra("eIndex", msg.equipementIndex);
			  intent.putExtra("pIndex", msg.projectIndex);
			  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       	startActivity(intent);
       	this.finish();
		}
	   private void goHome(){
		    Intent intent = new Intent(Intent.ACTION_MAIN);
		    intent.addCategory(Intent.CATEGORY_HOME);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startActivity(intent);
		    this.finish();
		}
	   public void sendNotification(PopMessage msg) {

	        // BEGIN_INCLUDE(build_action)
	        /** Create an intent that will be fired when the user clicks the notification.
	         * The intent needs to be packaged into a {@link android.app.PendingIntent} so that the
	         * notification service can fire it on our behalf.
	         */
//	        Intent intent = new Intent(Intent.ACTION_VIEW,
//	                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
//	        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//	        
	    	
	    	Intent resultIntent = new Intent(this, EquipementSlideActivity.class);
	    	resultIntent.putExtra("eIndex", msg.equipementIndex);
	    	resultIntent.putExtra("pIndex", msg.projectIndex);
	    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	    	// Adds the back stack
	    	stackBuilder.addParentStack(EquipementSlideActivity.class);
	    	// Adds the Intent to the top of the stack
	    	stackBuilder.addNextIntent(resultIntent);
	    	// Gets a PendingIntent containing the entire back stack
	    	PendingIntent pendingIntent =
	    	        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	        // END_INCLUDE(build_action)

	        // BEGIN_INCLUDE (build_notification)
	        /**
	         * Use NotificationCompat.Builder to set up our notification.
	         */
	        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

	        /** Set the icon that will appear in the notification bar. This icon also appears
	         * in the lower right hand corner of the notification itself.
	         *
	         * Important note: although you can use any drawable as the small icon, Android
	         * design guidelines state that the icon should be simple and monochrome. Full-color
	         * bitmaps or busy images don't render well on smaller screens and can end up
	         * confusing the user.
	         */
	        builder.setSmallIcon(R.drawable.ic_launcher);

	        // Set the intent that will fire when the user taps the notification.
	        builder.setContentIntent(pendingIntent);

	        // Set the notification to auto-cancel. This means that the notification will disappear
	        // after the user taps it, rather than remaining until it's explicitly dismissed.
	        builder.setAutoCancel(true);

	        /**
	         *Build the notification's appearance.
	         * Set the large icon, which appears on the left of the notification. In this
	         * sample we'll set the large icon to be the same as our app icon. The app icon is a
	         * reasonable default if you don't have anything more compelling to use as an icon.
	         */
	        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

	        /**
	         * Set the text of the notification. This sample sets the three most commononly used
	         * text areas:
	         * 1. The content title, which appears in large type at the top of the notification
	         * 2. The content text, which appears in smaller text below the title
	         * 3. The subtext, which appears under the text on newer devices. Devices running
	         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
	         *    anything vital!
	         */
	        builder.setContentTitle(msg.msgType+" Received");
	        builder.setContentText(msg.sender);
	        builder.setSubText(msg.getShortDate(msg.timestamp));

	       
	        NotificationManager notificationManager = (NotificationManager) getSystemService(
	                NOTIFICATION_SERVICE);
	        notificationManager.notify(NOTIFICATION_ID, builder.build());
	        // END_INCLUDE(send_notification)
	    }
	   
	   public void saveData(PopMessage msg){
	    	String state=msg.body;
	    	SharedPreferences sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
	    	  String updateState = sharedPref.getString(getString(R.string.update_status)+"_"+msg.phoneNum, "");
	    	SharedPreferences.Editor editor = sharedPref.edit();
	    	if(msg!=null&&msg.msgType!=null&&editor!=null){
		    	if(msg.msgType.equals(PopMessage.CH_ALARM)){
		    		editor.putString(getString(R.string.ch_state)+"_"+msg.phoneNum, state);
		    		if(updateState.equals(UpdateState.GET_CH)){
		    			editor.putString(getString(R.string.update_status)+"_"+msg.phoneNum,UpdateState.HAS_CH);
		    		}
		    	}else if(msg.msgType.equals(PopMessage.SYS_STATE)){
		    		editor.putString(getString(R.string.am_state)+"_"+msg.phoneNum, state);
		    		editor.putString(getString(R.string.ipam_state)+"_"+msg.phoneNum, "");
		    		if(updateState.equals(UpdateState.GET_STATE)){
		    			editor.putString(getString(R.string.update_status)+"_"+msg.phoneNum,UpdateState.HAS_STATE);
		    		}
		    	}else if(msg.msgType.equals(PopMessage.IP_ALARM)){
		    		editor.putString(getString(R.string.ipam_state)+"_"+msg.phoneNum, state);
		    	}
	    	}
	    	editor.commit();
	    }
	   
	   
}
