package com.example.ies_sms_demo.observe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ies_sms_demo.LoginActivity;
import com.example.ies_sms_demo.R;

public class SettingTabFragment extends Fragment {
	private Context rootContext; 
	private View rootView;
	public SettingTabFragment(){
	}
	public SettingTabFragment(Context context){
		this.rootContext=context;
	}
	@Override
	public void onResume() {
	    super.onResume();
	    SharedPreferences sharedPref =  rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);

        String stateStr = sharedPref.getString(getString(R.string.ch_state), "");

//        TextView t=(TextView)rootView.findViewById(R.id.msg); 
//        t.setText(stateStr);
	    
	}
	
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.tab_setting, container, false);
         SharedPreferences sharedPref = rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
         String username = sharedPref.getString(getString(R.string.user_name), "");

         String phoneNum = sharedPref.getString(getString(R.string.phone_num), "");
         
         
         EditText  et=(EditText)rootView.findViewById(R.id.phone_num);
         et.setText(phoneNum);
         TextView t=(TextView)rootView.findViewById(R.id.username);
         t.setText(username);

         Button logout = (Button) rootView.findViewById(R.id.logout);
         logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(rootContext, LoginActivity.class);
		    	SharedPreferences sharedPref = rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putString(getString(R.string.user_name), "");
		    	editor.commit();
		        startActivity(intent);
				
			} });
         
         Button changePhoneNum = (Button) rootView.findViewById(R.id.change_phone_num);
         changePhoneNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText  et1=(EditText)rootView.findViewById(R.id.phone_num);
				SharedPreferences sharedPref = rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putString(getString(R.string.phone_num), et1.getText().toString());
		    	editor.commit();
			}});
         return rootView;
     }

    
    
}
