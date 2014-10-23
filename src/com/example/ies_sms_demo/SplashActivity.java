package com.example.ies_sms_demo;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;


public class SplashActivity extends ActionBarActivity {

	 private boolean mIsBackButtonPressed;
	    private static final int SPLASH_DURATION = 3000; //6 seconds
	    private Handler myhandler;
	  
	    public void onCreate(Bundle savedInstanceState) 
	    {
	    	
	    	 super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash_screen);
	        ActionBar actionBar = getActionBar();
	        actionBar.hide();
	        myhandler = new Handler();
	          // run a thread to start the home screen
	        myhandler.postDelayed(new Runnable()
	        {
	            @Override
	            public void run() 
	            {
	            	SharedPreferences sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
	            	String username = sharedPref.getString(getString(R.string.user_name), "");
     	    
	               finish();
	                 
	               if (!mIsBackButtonPressed)
	               {
	            	   if(username.equals("")){
	            		   	Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		                    SplashActivity.this.startActivity(intent);
	            	   }else{
	                    // start the home activity 
		                    Intent intent = new Intent(SplashActivity.this, EquipementListActivity.class);
		                    intent.putExtra("isRefresh", true);
		                    SplashActivity.this.startActivity(intent);
		               
	            	   }
	            	}
	                  
	            }
	  
	        }, SPLASH_DURATION); 
	    }
	     
	    
	    //handle back button press
	    @Override
	    public void onBackPressed() 
	    {
	        mIsBackButtonPressed = true;
	        super.onBackPressed();
	    }

}
