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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.ies_sms_demo.ItemAdapter.AlarmIndicatorItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.EquipementItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.TextIndicatorItemAdapter;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.Indicator;
import com.example.ies_sms_demo.model.UpdateState;
import com.example.ies_sms_demo.receiver.PopMessage;

public class EquipementStateFragment extends Fragment {

	public static final String ARG_PAGE = "page";
	private ProgressDialog pDialog;
	private Context rootContext;
	private ViewGroup rootView;
	public String updateState;
	public Hashtable<String, String> chStateHash;
	public Hashtable<String, String> amStateHash;
	public String[] ipamStates;
	public Timer myTimer;
	public Button update;
	public Equipment equipement;
	public ArrayList<Indicator> textIndicator=new ArrayList<Indicator>();
	public ArrayList<Indicator> amIndicator=new ArrayList<Indicator>();
	public ListView textList;
	public TextIndicatorItemAdapter text_adapter;
	public ListView amList;
	public AlarmIndicatorItemAdapter am_adapter;
	public ImageView img;
	private int mPageNumber;
	public int pIndex;
	public static EquipementStateFragment create(int pageNumber,int pIndex, Context context,Equipment equipement) {

		EquipementStateFragment fragment = new EquipementStateFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		fragment.setRootContext(context,pIndex);
		fragment.setEquipement(equipement);
		return fragment;
	}

	private void setEquipement(Equipment equipement2) {
		equipement=equipement2;
		for(Indicator i :equipement.indicators){
			if(i.style_type.equals(Indicator.STYLE_TYPE_NB)){
				textIndicator.add(i);
			}else if(i.style_type.equals(Indicator.STYLE_TYPE_NS)||i.style_type.equals(Indicator.STYLE_TYPE_OS)){
				amIndicator.add(i);
			}
		}
	}

	public void setRootContext(Context context,int pIndex) {
		rootContext = context;
		this.pIndex=pIndex;
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
		amList=(ListView)rootView.findViewById(R.id.ip_list);
		textList=(ListView)rootView.findViewById(R.id.text_list);

		parseState();

		// TextView t=(TextView)rootView.findViewById(R.id.msg);
		// t.setText(stateStr);
		 img = (ImageView) rootView.findViewById(R.id.photo);
	       if(img!=null&&equipement!=null){
	        img.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                  Intent intent = new Intent(rootContext, MachineInfoActivity.class);
	                  intent.putExtra("Equipement", equipement);
	                  intent.putExtra("eIndex",mPageNumber );
	                  intent.putExtra("pIndex", pIndex);
	                  startActivity(intent);
	            }
	        });
	        ImageLoader imgLoader = new ImageLoader(rootContext);
			  
	        if(equipement.photo!=null &&!equipement.photo.isEmpty()){
	        	imgLoader.DisplayImage(EquipementItemAdapter.EQUIPMENT_PHOTO_URL+equipement.photo, R.drawable.ic_action_refresh, img);
	        }

	      
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
						if (equipement.phoneNumber!=null&&!equipement.phoneNumber.equals("")) {
	
							getState(v);
						
							showLoadingBar();
							updateState = UpdateState.GET_STATE;
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString(parseStrByID(R.string.update_status),
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
					}
				}
			});
			if(equipement.com_method.equals(Equipment.WEB)){
				update.setEnabled(false);
			}
			if (updateState == null) {
				updateState = UpdateState.IDLE;
			}

			if (updateState.equals(UpdateState.IDLE)) {
			
				hideLoadingBar();

			} else {
				showLoadingBar();
				
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
				
				hideLoadingBar();

			} else {
				showLoadingBar();
				
			}
		}
		if(equipement.com_method.equals(Equipment.WEB)){
			update.setEnabled(false);
		}
		// TextView t=(TextView)rootView.findViewById(R.id.msg);
		// t.setText(stateStr);

	}
	private void showLoadingBar() {
		
		if(pDialog==null||!pDialog.isShowing()){
		pDialog = new ProgressDialog(rootView.getContext());
         pDialog.setMessage("Loading...");
         pDialog.setIndeterminate(false);
         pDialog.setCancelable(true);
         pDialog.setOnCancelListener( new OnCancelListener(){

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				cancelMessage();
				 hideLoadingBar();
			}});
         pDialog.show();
		}
	}
	
	private void hideLoadingBar() {
		if(pDialog!=null){
		 pDialog.dismiss();
		}
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
					getUpdateStatusStr(),
					UpdateState.IDLE);
			editor.commit();
			updateState = UpdateState.IDLE;
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

		if (rootContext != null&&equipement.phoneNumber!=null&&!equipement.phoneNumber.isEmpty()) {
			SharedPreferences sharedPref = rootContext.getSharedPreferences(
					"share_data", Context.MODE_PRIVATE);
			updateState = sharedPref.getString(
					getUpdateStatusStr(), "");
			String chStateStr = sharedPref.getString(
					getCHStateStr(), "");

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
			}
			String amStateStr = sharedPref.getString(
					getAmStateStr(), "");
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
			}

			String ipamStateStr = sharedPref.getString(
					getIPAMStr(), "");
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

			}
			updateState();
			if (updateState.equals(UpdateState.HAS_STATE)) {

				updateState = UpdateState.GET_CH;
				getCH(null);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getUpdateStatusStr(),
						UpdateState.GET_CH);
				editor.commit();
			} else if (updateState.equals(UpdateState.HAS_CH)) {

				updateState = UpdateState.IDLE;
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getUpdateStatusStr(),
						UpdateState.IDLE);
				editor.commit();
			} else if (updateState.equals("")) {
				updateState = UpdateState.IDLE;
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getUpdateStatusStr(),
						UpdateState.IDLE);
				editor.commit();
			}
			 am_adapter = new AlarmIndicatorItemAdapter(rootContext, R.id.text_view, amIndicator);
		     amList.setAdapter(am_adapter);
		     ViewGroup.LayoutParams params = amList.getLayoutParams();
		     params.height =Dp2Px(rootContext, amIndicator.size()*36);
		     amList.setLayoutParams(params);
		     text_adapter=new TextIndicatorItemAdapter(rootContext, R.id.text_view,	 textIndicator);
		     textList.setAdapter(text_adapter);
		     ViewGroup.LayoutParams params1 = textList.getLayoutParams();
		     params1.height =Dp2Px(rootContext, textIndicator.size()*36);
		     textList.setLayoutParams(params1);
		     
		}
	}
	public int Dp2Px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	}

	private String getIPAMStr() {
		return parseStrByID(R.string.ipam_state);
	}
	private String parseStrByID(int id){
		return getString(id)+"_"+equipement.phoneNumber;
	}
	private String getCHStateStr() {
		return parseStrByID(R.string.ch_state);
	}

	private String getAmStateStr() {
		return parseStrByID(R.string.am_state);
	}

	private String getUpdateStatusStr() {
		return parseStrByID(R.string.update_status);
	}
//	public int[] alarms={R.id.heaterStep1,R.id.heaterStep2,R.id.heaterStep3,R.id.pumpRunning,R.id.lowLevelAlarm,R.id.highTempAlarm,R.id.pumpFaultAlarm,R.id.lossOfPhase};
	public void updateIPAM() {
		if(ipamStates!=null &&ipamStates.length==8){
			for(int i=0;i<amIndicator.size();i++){
				char state=ipamStates[Integer.parseInt(amIndicator.get(i).channel)-1].charAt(0);
				amIndicator.get(i).state=""+state;
				
			}
		}
	}
	
	public void updateState() {
		String ips =null;
		String chs=null;
		if(amStateHash!=null){
			ips = amStateHash.get("IP\\(1\\-8\\)");
		}
		if(chStateHash!=null){
			 chs =chStateHash.get("CHS");
		}
		
//		TextView t = (TextView) rootView.findViewById(R.id.temperature);
//		if(t!=null &&c1!=""){
//			t.setText(c1);
//		}
		for(int i=0;i<textIndicator.size();i++){
			int currentChannel=Integer.parseInt(textIndicator.get(i).channel);
			if(textIndicator.get(i).type.equals(Indicator.TYPE_CH)&&chStateHash!=null){
				String text =chStateHash.get("C"+currentChannel);
				textIndicator.get(i).text=text;
			}
		}
		
		for(int i=0;i<amIndicator.size();i++){
			int currentChannel=Integer.parseInt(amIndicator.get(i).channel)-1;
			if(amIndicator.get(i).type.equals(Indicator.TYPE_CH)&&chs!=null&& chs.length()==8){
					
					char state=chs.charAt(currentChannel);
					amIndicator.get(i).state=""+state;
				
			}
			if(amIndicator.get(i).type.equals(Indicator.TYPE_IP)&&ips!=null&& ips.length()==8){
				
					char state=ips.charAt(currentChannel);
					amIndicator.get(i).state=""+state;
				
			}
			if(amIndicator.get(i).type.equals(Indicator.TYPE_IP)&&ipamStates!=null  &&ipamStates.length==8){
				
				String state=ipamStates[currentChannel]!=null&&!ipamStates[currentChannel].isEmpty()?(""+ipamStates[currentChannel].charAt(0)):"";
				if(!state.isEmpty()){
					amIndicator.get(i).state=state;
				}
			
			}
			
		}
	}

	public void updateCH() {
		
	}

	public void getCH(View v) {
		
		sendRequest("GET-CH");

	}

	public void getState(View v) {
		sendRequest("GET-STATE");

	}

	private void sendRequest(String instruction) {
		if(equipement.com_method.equals(Equipment.SMS)){
		String phoneNum = equipement.phoneNumber;
		Context context = rootContext.getApplicationContext();
		int duration = Toast.LENGTH_LONG;
		CharSequence text;
		

		sendSMS(phoneNum, instruction);

		text = instruction + " has sent";

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		}else if(equipement.com_method.equals(Equipment.WEB)){
			
		}
//		if(instruction=="GET-STATE"){
//		Handler myhandler = new Handler();
//        // run a thread to start the home screen
//	      myhandler.postDelayed(new Runnable()
//	      {
//	          @Override
//	          public void run() 
//	          {
//	          
//	        	  testStateMsg();
//	        	  
//	          
//	          }
//	
//	      }, 5000); 
//		
//	}else{
//		Handler myhandler = new Handler();
//        // run a thread to start the home screen
//	      myhandler.postDelayed(new Runnable()
//	      {
//	          @Override
//	          public void run() 
//	          {
//	          
//	        	  testCHAMMsg();
//	        	  
//	          
//	          }
//	
//	      }, 5000); 
//		
//	}

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
	public void testCHAMMsg() {
		PopMessage pop_msg = new PopMessage();
		pop_msg.projectIndex=pIndex;
		pop_msg.equipementIndex=mPageNumber;
		pop_msg.phoneNum= equipement.phoneNumber;
		pop_msg.sender = equipement.phoneNumber;
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

		

		// start UI
		
	      startActivity(intent);
	}

	public void testStateMsg() {
		PopMessage pop_msg = new PopMessage();
		pop_msg.projectIndex=pIndex;
		pop_msg.equipementIndex=mPageNumber;
		pop_msg.sender  = equipement.phoneNumber;
		pop_msg.phoneNum= equipement.phoneNumber;
		// pop_msg.body =
		// "OP(1-8)=\nHHHHHHHL\nIP(1-8)=\nOOOOOOOO\nCHAM(1-8)=\n00000000\nIPAM(1-8)=\n00000000\n";
		// pop_msg.body =
		//"Switch input 1-8 (1:Close) 2:Open 3:Open 4:Open 5:Open 6:Open 7:Open 8:Open"
		// "OP(1-8)= HHHHHHHL IP(1-8)= OCOCOOOO CHAM(1-8)= 10000000 IPAM(1-8)= 00000000 ";
		pop_msg.body = "OP(1-8)= HHHHHHHL IP(1-8)= OCOCOCOC CHAM(1-8)= 10000000 IPAM(1-8)= 00000000 ";
		pop_msg.timestamp = 1234;
		pop_msg.msgType = PopMessage.SYS_STATE;
		Intent intent = new Intent(rootContext, PopSMSActivity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// pass Serializable object
		intent.putExtra("msg", pop_msg);
		Context context = rootContext.getApplicationContext();
		CharSequence text = pop_msg.body + "has sent";
		int duration = Toast.LENGTH_SHORT;

		
		// start UI
	      startActivity(intent);
	}
}
