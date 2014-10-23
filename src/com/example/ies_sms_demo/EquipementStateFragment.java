/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ies_sms_demo;

import java.util.Hashtable;
import java.util.Timer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.Machine;

public class EquipementStateFragment extends Fragment {

	public static final String ARG_PAGE = "page";

	private Context rootContext;
	private ViewGroup rootView;
	public String updateState;
	public Hashtable<String, String> chStateHash;
	public Hashtable<String, String> amStateHash;
	public String[] ipamStates;
	public Timer myTimer;
	public Button update;
	public Equipment equipement;
	public ImageView img;
	private int mPageNumber;
	
	public static EquipementStateFragment create(int pageNumber, Context context,Equipment equipement) {

		EquipementStateFragment fragment = new EquipementStateFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		fragment.setRootContext(context);
		fragment.setEquipement(equipement);
		return fragment;
	}

	private void setEquipement(Equipment equipement2) {
		equipement=equipement2;
		
	}

	public void setRootContext(Context context) {
		rootContext = context;

	}

	public EquipementStateFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		rootView = (ViewGroup) inflater.inflate(R.layout.tab_state, container,
				false);
		parseState();

		// TextView t=(TextView)rootView.findViewById(R.id.msg);
		// t.setText(stateStr);
		 img = (ImageView) rootView.findViewById(R.id.photo);
	       if(img!=null&&equipement!=null &&equipement.machine!=null){
	        img.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	  Machine m=equipement.machine;
	                  Intent intent = new Intent(rootContext, MachineInfoActivity.class);
	                  intent.putExtra("Machine", m);
	                  startActivity(intent);
	            }
	        });
	        
	        img.setImageDrawable(getResources().getDrawable(
					equipement.machine.photoId));
	       }
	       
	     
			
		
		update = (Button) rootView.findViewById(R.id.update);
		if (update != null) {
			update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(updateState
					.equals(UpdateState.IDLE)){
					SharedPreferences sharedPref = rootContext
							.getSharedPreferences("share_data",
									Context.MODE_PRIVATE);
					String phoneNum = sharedPref.getString(
							getString(R.string.phone_num), "");
					if (!phoneNum.equals("")) {

						getState(v);
					
						update.setText("Loading...\nCancel");
						updateState = UpdateState.GET_STATE;
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putString(getString(R.string.update_status),
								UpdateState.GET_STATE);
						editor.commit();
					} else {
						Context context = rootContext.getApplicationContext();
						int duration = Toast.LENGTH_LONG;
						CharSequence text;
						text = "Please set phone number first!!!";
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();

					}
					}else{
						cancelMessage();
					}
				}
			});

			if (updateState == null) {
				updateState = UpdateState.IDLE;
			}

			if (updateState.equals(UpdateState.IDLE)) {
			
				update.setText("Update");

			} else {
				update.setText("Loading...\nCancel");

			}
			
		}
		return rootView;
		// Set the title view to show the page number.
		// ((TextView) rootView.findViewById(android.R.id.text1)).setText(
		// getString(R.string.title_template_step, mPageNumber + 1));

	}

	@Override
	public void onResume() {
		super.onResume();

		parseState();
		if (update != null) {
			if (updateState.equals(UpdateState.IDLE)) {
			
				update.setText("Update");

			} else {
				
				update.setText("Loading...\nClick to cancel");
			}
		}
		// TextView t=(TextView)rootView.findViewById(R.id.msg);
		// t.setText(stateStr);

	}
	

	
	    public void cancelMessage() {

	    	if (!updateState
					.equals(UpdateState.IDLE)) {
				showTimerOutMsg();
			}

			SharedPreferences sharedPref = rootContext
					.getSharedPreferences(
							"share_data",
							Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref
					.edit();
			editor.putString(
					getString(R.string.update_status),
					UpdateState.IDLE);
			editor.commit();
			updateState = UpdateState.IDLE;
			update.setText("Update");
	    }

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
		return mPageNumber;
	}
	public String[] chStateKeys={"C8","C7","C6","C5","C4","C3","C2","C1","CHS"};
	public String[] amStateKeys={"IPAM\\(1\\-8\\)","CHAM\\(1\\-8\\)","IP\\(1\\-8\\)","OP\\(1\\-8\\)"};
	public String[] ipStateKeys={"8","7","6","5","4","3","2","1","0"};
	public void parseState() {

		if (rootContext != null) {
			SharedPreferences sharedPref = rootContext.getSharedPreferences(
					"share_data", Context.MODE_PRIVATE);
			updateState = sharedPref.getString(
					getString(R.string.update_status), "");
			String chStateStr = sharedPref.getString(
					getString(R.string.ch_state), "");

			if (chStateStr != "") {
				chStateHash = new Hashtable<String, String>();
				chStateStr=chStateStr.replaceAll("\\s+","");
				for (int i = 0; i < chStateKeys.length; i++) {
					String[] states = chStateStr.split(chStateKeys[i]+"=");
					if(states.length>1){
						String value = states[1];
						chStateStr=states[0];
						if (value!=null&&value.length() > 1) {
							chStateHash.put(chStateKeys[i], value);
						}
					}
				}
				updateCH();
			}
			String amStateStr = sharedPref.getString(
					getString(R.string.am_state), "");
			if (amStateStr != "") {
				amStateHash = new Hashtable<String, String>();
				amStateStr=amStateStr.replaceAll("\\s+","");
				for (int i = 0; i < amStateKeys.length; i++) {
					String[] states = amStateStr.split(amStateKeys[i]+"=");
					if(states.length>1){
						String value = states[1];
						amStateStr=states[0];
						if (value!=null&&value.length() > 1) {
							amStateHash.put(amStateKeys[i], value);
						}
					}
				}
				updateAM();
			}

			String ipamStateStr = sharedPref.getString(
					getString(R.string.ipam_state), "");
			ipamStates=new String[8];
			if (ipamStateStr != "") {
				ipamStateStr=ipamStateStr.replaceAll("\\s+","");
				for (int i = 0; i < ipStateKeys.length; i++) {
					String[] states = ipamStateStr.split(ipStateKeys[i]+":");
					if(states.length>1){
						String value = states[1].replace(")", "");
						ipamStateStr=states[0];
						if (value!=null&&value.length() > 1) {
							ipamStates[Integer.parseInt(ipStateKeys[i])-1]= value;
						}
					}
				}

				updateIPAM();
			}

			if (updateState.equals(UpdateState.HAS_STATE)) {

				updateState = UpdateState.GET_CH;
				getCH(null);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.update_status),
						UpdateState.GET_CH);
				editor.commit();
			} else if (updateState.equals(UpdateState.HAS_CH)) {

				updateState = UpdateState.IDLE;
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.update_status),
						UpdateState.IDLE);
				editor.commit();
			} else if (updateState.equals("")) {
				updateState = UpdateState.IDLE;
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.update_status),
						UpdateState.IDLE);
				editor.commit();
			}
		}
	}
	public int[] alarms={R.id.heaterStep1,R.id.heaterStep2,R.id.heaterStep3,R.id.pumpRunning,R.id.lowLevelAlarm,R.id.highTempAlarm,R.id.pumpFaultAlarm,R.id.lossOfPhase};
	public void updateIPAM() {
		if(ipamStates!=null &&ipamStates.length==alarms.length){
		for(int i=0;i<4;i++){
		if (ipamStates[i].contains("Close")) {

			ImageView tempAlarm = (ImageView) rootView
					.findViewById(alarms[i]);
			tempAlarm.setImageDrawable(getResources().getDrawable(
					R.drawable.on));
		} else {
			ImageView tempAlarm = (ImageView) rootView
					.findViewById(alarms[i]);
			tempAlarm.setImageDrawable(getResources().getDrawable(
					R.drawable.off));
		}
		}
		for(int i=4;i<alarms.length;i++){
			if (ipamStates[i].contains("Close")) {

				ImageView tempAlarm = (ImageView) rootView
						.findViewById(alarms[i]);
				tempAlarm.setImageDrawable(getResources().getDrawable(
						R.drawable.danger_icon));
			} else {
				ImageView tempAlarm = (ImageView) rootView
						.findViewById(alarms[i]);
				tempAlarm.setImageDrawable(getResources().getDrawable(
						R.drawable.safe_icon));
			}
		}
		}
	}
	
	public void updateAM() {
		String chs = amStateHash.get("IP\\(1\\-8\\)");
		if(chs!=null&& chs.length()==alarms.length){
		for(int i=0;i<4;i++){
			if (chs.charAt(i) == 'O') {
				ImageView tempAlarm = (ImageView) rootView
						.findViewById(alarms[i]);
				tempAlarm.setImageDrawable(getResources().getDrawable(
						R.drawable.off));
			} else {
				ImageView tempAlarm = (ImageView) rootView
						.findViewById(alarms[i]);
				tempAlarm.setImageDrawable(getResources().getDrawable(
						R.drawable.on));
			}
		}
		for(int i=4;i<alarms.length;i++){
			if (chs.charAt(i) == 'O') {
				ImageView tempAlarm = (ImageView) rootView
						.findViewById(alarms[i]);
				tempAlarm.setImageDrawable(getResources().getDrawable(
						R.drawable.safe_icon));
			} else {
				ImageView tempAlarm = (ImageView) rootView
						.findViewById(alarms[i]);
				tempAlarm.setImageDrawable(getResources().getDrawable(
						R.drawable.danger_icon));
			}
		}
		}
	}

	public void updateCH() {
		String c1 = chStateHash.get("C1");
		TextView t = (TextView) rootView.findViewById(R.id.temperature);
		if(t!=null &&c1!=""){
			t.setText(c1);
		}
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

		sendSMS(phoneNum, instruction);

		text = instruction + "has sent";

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}

	public void showTimerOutMsg() {
		Context context = rootContext.getApplicationContext();
		CharSequence text = "Update Cancelled";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	private void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
}
