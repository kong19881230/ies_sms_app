package com.example.ies_sms_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.ies_sms_demo.PopSMSActivity;
import com.example.ies_sms_demo.R;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		 
		// get the SMS received
		Object[] pdus = (Object[]) bundle.get("pdus");
		SmsMessage[] msgs = new SmsMessage[pdus.length];
		
		PopMessage pop_msg = new PopMessage();
		for (int i=0; i<msgs.length; i++){
		      msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
		      pop_msg.sender = msgs[i].getOriginatingAddress();
		      pop_msg.body = msgs[i].getMessageBody().toString();
		      pop_msg.timestamp = msgs[i].getTimestampMillis();
		}
        SharedPreferences sharedPref = context.getSharedPreferences("share_data",Context.MODE_PRIVATE);
       
        String phoneNum = sharedPref.getString(context.getString(R.string.phone_num), "");
		if(pop_msg.sender.contains(phoneNum)){
			if(pop_msg.body.contains("CHS")){
				pop_msg.msgType=PopMessage.CH_ALARM;
	    	}else if(pop_msg.body.contains("IPAM")){
	    		pop_msg.msgType=PopMessage.SYS_STATE;
	    	}else if(pop_msg.body.contains("Switch")){
	    		pop_msg.msgType=PopMessage.IP_ALARM;
	    	}
			intent.setClass(context, PopSMSActivity.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 
		    // pass Serializable object
		    intent.putExtra("msg", pop_msg);
		 
		    // start UI
		    context.startActivity(intent);
		}
	}

}
