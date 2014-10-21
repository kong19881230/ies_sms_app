package com.example.ies_sms_demo.observe;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import com.example.ies_sms_demo.PopSMSActivity;
import com.example.ies_sms_demo.R;
import com.example.ies_sms_demo.R.id;
import com.example.ies_sms_demo.R.layout;
import com.example.ies_sms_demo.R.string;
import com.example.ies_sms_demo.receiver.PopMessage;

import android.app.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteTabFragment extends Fragment {
	private Context rootContext;
	private View rootView;

	public RemoteTabFragment() {
	}

	public RemoteTabFragment(Context context) {
		this.rootContext = context;
	}

	@Override
	public void onResume() {
		super.onResume();

		updateView();

	}

	public void updateView() {
		SharedPreferences sharedPref = rootContext.getSharedPreferences(
				"share_data", Context.MODE_PRIVATE);
		String chStateStr = sharedPref.getString(getString(R.string.ch_state),
				"");
		String ipamStateStr = sharedPref.getString(
				getString(R.string.ipam_state), "");
		String amStateStr = sharedPref.getString(getString(R.string.am_state),
				"");
		TextView textCH = (TextView) rootView.findViewById(R.id.text_ch_state);
		textCH.setText("Channel State:\n" + chStateStr);
		TextView textIP = (TextView) rootView.findViewById(R.id.text_ip_state);
		textIP.setText("IPAM State:\n" + ipamStateStr);
		TextView textSYS = (TextView) rootView
				.findViewById(R.id.text_sys_state);
		textSYS.setText("System State:\n" + amStateStr);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_remote, container, false);
		updateView();
		Button testSysMsg = (Button) rootView.findViewById(R.id.testStateMsg);
		testSysMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				testStateMsg(v);

			}/* Some Code */
		});
		Button button = (Button) rootView.findViewById(R.id.testMsg);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				testCHAMMsg(v);

			}/* Some Code */
		});
		Button getch = (Button) rootView.findViewById(R.id.getch);
		getch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCH(v);

			}/* Some Code */
		});
		Button getstate = (Button) rootView.findViewById(R.id.getstate);
		getstate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getState(v);

			}/* Some Code */
		});
		Button setchalarm = (Button) rootView.findViewById(R.id.setchalarm);
		setchalarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setChAlarm(v);

			}/* Some Code */
		});
		Button setdipalarm = (Button) rootView.findViewById(R.id.setdipalarm);
		setdipalarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setDipAlarm(v);

			}/* Some Code */
		});
		Button setdop = (Button) rootView.findViewById(R.id.setdop);
		setdop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setDop(v);

			}/* Some Code */
		});
		return rootView;
	}

	public void setDop(View v) {
		sendRequest("SET-DOP 11000000");

	}

	public void setDipAlarm(View v) {
		sendRequest("SET-DIP-ALARM 11000000");

	}

	public void setChAlarm(View v) {
		sendRequest("SET-CH-ALARM 11000000");

	}

	public void getCH(View v) {
		sendRequest("GET-CH");
	}

	public void getState(View v) {
		sendRequest("GET-STATE");

	}

	private void sendRequest(String instruction) {
		SharedPreferences sharedPref = rootContext.getSharedPreferences(
				"share_data", Context.MODE_PRIVATE);
		String phoneNum = sharedPref.getString(getString(R.string.phone_num),
				"");
		Context context = rootContext.getApplicationContext();
		int duration = Toast.LENGTH_LONG;
		CharSequence text;
		if (!phoneNum.equals("")) {
			sendSMS(phoneNum, instruction);

			text = instruction + "has sent";

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			text = "Please set phone number first!!!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

	public void testCHAMMsg(View v) {
		PopMessage pop_msg = new PopMessage();

		pop_msg.sender = "66350168";
		// pop_msg.body =
		// "OP(1-8)=HHHHHHHL IP(1-8)=OOOOOOOO CHAM(1-8)=00000000 IPAM(1-8)=00000000";
		pop_msg.body = "CHS=NLLLLLLL\r\t C1=60\n C2=-24 C C3=24 F C4=24 % C5=25 %RH\f C6=25 pH\f C7=29 %O2 C8=23 mg/L";
		pop_msg.timestamp = 1234;
		pop_msg.msgType = PopMessage.CH_ALARM;
		Intent intent = new Intent(rootContext, PopSMSActivity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// pass Serializable object
		intent.putExtra("msg", pop_msg);
		Context context = rootContext.getApplicationContext();
		CharSequence text = pop_msg.body + "has sent";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		// start UI
		startActivity(intent);
	}

	public void testStateMsg(View v) {
		PopMessage pop_msg = new PopMessage();

		pop_msg.sender = "66350168";
		// pop_msg.body =
		// "OP(1-8)=\nHHHHHHHL\nIP(1-8)=\nOOOOOOOO\nCHAM(1-8)=\n00000000\nIPAM(1-8)=\n00000000\n";
		// pop_msg.body =
		// "OP(1-8)= HHHHHHHL IP(1-8)= OCOCOOOO CHAM(1-8)= 10000000 IPAM(1-8)= 00000000 ";
		pop_msg.body = "Switch input 1-8 (1:Close) 2:Open 3:Open 4:Open 5:Open 6:Open 7:Open 8:Open";
		pop_msg.timestamp = 1234;
		pop_msg.msgType = PopMessage.IP_ALARM;
		Intent intent = new Intent(rootContext, PopSMSActivity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// pass Serializable object
		intent.putExtra("msg", pop_msg);
		Context context = rootContext.getApplicationContext();
		CharSequence text = pop_msg.body + "has sent";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		// start UI
		startActivity(intent);
	}

	public void saveData(View v) {
		String state = "CHS=NLLLLLLL\r\t C1=60\n C2=-24 C C3=24 F C4=24 % C5=25 %RH\f C6=25 pH\f C7=29 %O2 C8=23 mg/L";
		SharedPreferences sharedPref = rootContext.getSharedPreferences(
				"share_data", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.ch_state), state);
		editor.commit();
	}

}
