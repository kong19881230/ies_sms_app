//package com.example.ies_sms_demo.observe;
//
//import java.util.Hashtable;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.telephony.SmsManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.ies_sms_demo.R;
//import com.example.ies_sms_demo.UpdateState;
//
//public class StateTabFragment extends Fragment {
//	private Context rootContext; 
//	private View rootView;
//	public Hashtable<String, String> chStateHash;
//	public Hashtable<String, String> amStateHash;
//	public String[] ipamStates;
//	public Timer myTimer;
//	public StateTabFragment(Context context){
//		this.rootContext=context;
//	}
//	public String updateState;
//	public void parseState(){
//		  
//		
//		SharedPreferences sharedPref = rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
//		updateState = sharedPref.getString(getString(R.string.update_status), "");
//		String chStateStr = sharedPref.getString(getString(R.string.ch_state), "");
//		
//		
//		
//		if(chStateStr!=""){
//		    chStateHash=new Hashtable<String,String>();
//		    String[] states=chStateStr.split("\n");
//		    for(int i=0;i<states.length;i++ ){
//		    	String[] keyvalepair=states[i].split("=");
//		    	chStateHash.put(keyvalepair[0], keyvalepair[1]);
//		    }
//		    updateCH();
//		}
//		String amStateStr = sharedPref.getString(getString(R.string.am_state), "");
//		if(amStateStr!=""){
//		    amStateHash=new Hashtable<String,String>();
//		    String[] states=amStateStr.split("\n|=\n");
//		    for(int i=0;i<states.length;i+=2 ){
//		    	
//		    	amStateHash.put(states[i], states[i+1]);
//		    }
//		    updateAM();
//		}
//		
//		String ipamStateStr = sharedPref.getString(getString(R.string.ipam_state), "");
//		if(ipamStateStr!=""){
//		    
//		    ipamStates=ipamStateStr.split("\n");
//		    
//		    updateIPAM();
//		}
//		
//		if(updateState.equals(UpdateState.HAS_STATE)){
//			
//			updateState=UpdateState.GET_CH;
//			getCH(null);
//	    	SharedPreferences.Editor editor = sharedPref.edit();
//	    	editor.putString(getString(R.string.update_status), UpdateState.GET_CH);
//	    	editor.commit();
//		}else if(updateState.equals(UpdateState.HAS_CH)) {
//			
//			updateState=UpdateState.IDLE;
//			SharedPreferences.Editor editor = sharedPref.edit();
//	    	editor.putString(getString(R.string.update_status), UpdateState.IDLE);
//	    	editor.commit();
//		}else if(updateState.equals("")){
//			updateState=UpdateState.IDLE;
//			SharedPreferences.Editor editor = sharedPref.edit();
//	    	editor.putString(getString(R.string.update_status), UpdateState.IDLE);
//	    	editor.commit();
//		}
//		
//	}
//	@Override
//	public void onResume() {
//	    super.onResume();
//	   
//        parseState();
//        if(update!=null){
//            if(updateState.equals(UpdateState.IDLE)){
//           	 	update.setEnabled(true);
//           	 	update.setText("Update");
//	           	
//            }else{
//            	update.setEnabled(false);
//            	update.setText("Loading...");
//            }
//        }
////        TextView t=(TextView)rootView.findViewById(R.id.msg); 
////        t.setText(stateStr);
//	    
//	}
//	public void updateIPAM(){
//		
//		if(ipamStates[1].contains("Close")){
//			
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.failure_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.danger_icon));
//		}else{
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.failure_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.safe_icon));
//		}
//		if(ipamStates[2].contains("Close")){
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.low_water_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.danger_icon));
//		}else{
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.low_water_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.safe_icon));
//		}
//	}
//	public void updateAM(){
//		String chs=amStateHash.get("IP(1-8)").replaceAll("\\s","");
//		if(chs.charAt(0)=='O'){
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.failure_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.safe_icon));
//		}else{
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.failure_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.danger_icon));
//		}
//		if(chs.charAt(1)=='O'){
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.low_water_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.safe_icon));
//		}else{
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.low_water_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.danger_icon));
//		}
//	}
//	public void updateCH(){
//		String chs=chStateHash.get("CHS");
//		if(chs.charAt(0)=='N'){
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.temp_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.safe_icon));
//		}else{
//			ImageView tempAlarm=(ImageView)rootView.findViewById(R.id.temp_alarm); 
//			tempAlarm.setImageDrawable(getResources().getDrawable(R.drawable.danger_icon));
//		}
//		
//		String c1=chStateHash.get("C1");
//		 TextView t=(TextView)rootView.findViewById(R.id.temperature); 
//		 t.setText(c1);
//	}
//	public Button update;
//	 @Override
//     public View onCreateView(LayoutInflater inflater, ViewGroup container,
//             Bundle savedInstanceState) {
//         rootView = inflater.inflate(R.layout.tab_state, container, false);
//         
//         parseState();
//         
////         TextView t=(TextView)rootView.findViewById(R.id.msg); 
////         t.setText(stateStr);
//         update = (Button) rootView.findViewById(R.id.update);
//         update.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				  SharedPreferences sharedPref =  rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
//			        String phoneNum = sharedPref.getString(getString(R.string.phone_num), "");
//				   if(!phoneNum.equals("")){
//			    	
//				getState(v);
//				update.setEnabled(false);
//				update.setText("Loading...");
//				updateState=UpdateState.GET_STATE;
//				SharedPreferences.Editor editor = sharedPref.edit();
//		    	editor.putString(getString(R.string.update_status), UpdateState.GET_STATE);
//		    	editor.commit();
//		    	 myTimer = new Timer();
//		    	 TimerTask task = new TimerTask() {  
//		    		  
//		    		   @Override  
//		    		   public void run() {  
//		    			   ((Activity) rootContext).runOnUiThread(new Runnable() {  
//		    		  
//		    		     @Override  
//		    		     public void run() {  
//		    		    	 if(!updateState.equals(UpdateState.IDLE)){
//		    		    		 showTimerOutMsg();
//		    		    	 }
//		    		    	 
//		    		    	 SharedPreferences sharedPref = rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
//		    			    	SharedPreferences.Editor editor = sharedPref.edit();
//		    			    	editor.putString(getString(R.string.update_status), UpdateState.IDLE);
//		    			    	editor.commit();
//		    			    	updateState=UpdateState.IDLE;
//		    			    	update.setEnabled(true);
//		                	 	update.setText("Update");
//		    			    	
//		    		     }  
//		    		    });  
//		    		   }  
//		    		  };  
//		    		  
//		    		  myTimer.schedule(task,60000);
//				   }else{
//					   	Context context = rootContext.getApplicationContext();
//				        int duration = Toast.LENGTH_LONG;
//				        CharSequence text;
//				        text = "Please set phone number first!!!";
//			    		Toast toast = Toast.makeText(context, text, duration);
//				    	toast.show();
//				    	
//			    	}
//				   
//			} });
//         
//         if(update!=null){
//             if(updateState.equals(UpdateState.IDLE)){
//            	 	update.setEnabled(true);
//            	 	update.setText("Update");
//            	 	
//             }else{
//             	update.setEnabled(false);
//             	update.setText("Loading...");
//             	
//             }
//         }
//         return rootView;
//     }
//
//    
//    
//    
//    public void getCH(View v)
//    {
//    	 sendRequest("GET-CH") ;
//           
//     }
//    
//    public void getState(View v)
//    {
//    	 sendRequest("GET-STATE") ;
//           
//     }
//    private void sendRequest(String instruction){
//	    SharedPreferences sharedPref =  rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
//        String phoneNum = sharedPref.getString(getString(R.string.phone_num), "");
//        Context context = rootContext.getApplicationContext();
//        int duration = Toast.LENGTH_LONG;
//        CharSequence text;
//       
//	        sendSMS(phoneNum, instruction);
//	    	
//	    	 text = instruction +"has sent";
//	    	
//	
//	    	Toast toast = Toast.makeText(context, text, duration);
//	    	toast.show();
//	 
//    	
//    	
//    }
//    
//    public void showTimerOutMsg(){
//    	Context context = rootContext.getApplicationContext();
//    	CharSequence text = "Request Timeout";
//    	int duration = Toast.LENGTH_LONG;
//    	Toast toast = Toast.makeText(context, text, duration);
//    	toast.show();
//    }
//    private void sendSMS(String phoneNumber, String message)
//    {
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, null, null);
//     }
//    
//   
//}
