package com.example.ies_sms_demo;

import com.example.ies_sms_demo.ItemAdapter.EquipementItemAdapter;
import com.example.ies_sms_demo.downloader.ImageLoader;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;


public class PhotoActivity extends ActionBarActivity {

	 private boolean mIsBackButtonPressed;
	  
	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	    	
	    	 super.onCreate(savedInstanceState);
	        setContentView(R.layout.photo);
	        ActionBar actionBar = getActionBar();
	        getActionBar().setIcon(
	     		   new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
	        actionBar.hide();
	        String i =getIntent().getStringExtra("photo");
	        ImageView img = (ImageView)findViewById(R.id.photo);
			ImageLoader imgLoader = new ImageLoader(getApplicationContext());
			if (img != null) {
				
			        if(i!=null &&!i.isEmpty()){
			        	imgLoader.DisplayImage(EquipementItemAdapter.EQUIPMENT_PHOTO_URL+i, R.drawable.ic_action_refresh, img);
			        }
			}
	    }
	     
	    
	    //handle back button press
	    @Override
	    public void onBackPressed() 
	    {
	        mIsBackButtonPressed = true;
	        super.onBackPressed();
	    }

}
