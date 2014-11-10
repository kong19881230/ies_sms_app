package com.example.ies_sms_demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ies_sms_demo.LoginActivity;
import com.example.ies_sms_demo.R;
import com.example.ies_sms_demo.model.Equipment;

public class SettingActivity extends ActionBarActivity {

	public int pIndex;
	public int eIndex;
	public Equipment equipment;
	private String parseStrByID(int id){
		return getString(id)+"_"+equipment.phoneNumber;
	}
	public void updateView() {
		SharedPreferences sharedPref = getSharedPreferences(
				"share_data", Context.MODE_PRIVATE);
		String chStateStr = sharedPref.getString(parseStrByID(R.string.ch_state),
				"");
		String ipamStateStr = sharedPref.getString(
				parseStrByID(R.string.ipam_state), "");
		String amStateStr = sharedPref.getString(parseStrByID(R.string.am_state),
				"");
		TextView textPhone = (TextView) findViewById(R.id.text_phone);
		textPhone.setText("Phone:\n" + equipment.phoneNumber);
		TextView textCH = (TextView) findViewById(R.id.text_ch_state);
		textCH.setText("Channel State:\n" + chStateStr);
		TextView textIP = (TextView) findViewById(R.id.text_ip_state);
		textIP.setText("IPAM State:\n" + ipamStateStr);
		TextView textSYS = (TextView)findViewById(R.id.text_sys_state);
		textSYS.setText("System State:\n" + amStateStr);

	}
	 @Override
	 public void onCreate(Bundle savedInstanceState) 
	    {
	    	
	    	 super.onCreate(savedInstanceState);
	        setContentView(R.layout.setting);
         SharedPreferences sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
         String username = sharedPref.getString(getString(R.string.user_name), "");
         eIndex=getIntent().getIntExtra("eIndex", 0);
 	     pIndex=getIntent().getIntExtra("pIndex", 0);
         equipment= (Equipment) getIntent().getSerializableExtra("Equipement");
         setTitle(equipment.refNo);
		updateView();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(
     		   new ColorDrawable(getResources().getColor(android.R.color.transparent))); 

     }

	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:
	                // app icon in action bar clicked; goto parent activity.
	            	Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
	              intent.putExtra("eIndex", eIndex);
	              intent.putExtra("pIndex", pIndex);
	              startActivity(intent);
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
    
}
