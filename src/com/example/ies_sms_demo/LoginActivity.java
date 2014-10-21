package com.example.ies_sms_demo;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        
    }

    public void login(View view){
    	EditText usernameText=(EditText)findViewById(R.id.username); 
    	String username =usernameText.getText().toString();
    	EditText passwordText=(EditText)findViewById(R.id.password); 
    	String password =passwordText.getText().toString();
    	if(!username.isEmpty()&&!password.isEmpty()){
    		Intent intent = new Intent(this, EquipementListActivity.class);
	    	SharedPreferences sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
	    	SharedPreferences.Editor editor = sharedPref.edit();
	    	editor.putString(getString(R.string.user_name), username);
	    	editor.commit();
	//        EditText editText = (EditText) findViewById(R.id.edit_message);
	//        String message = editText.getText().toString();
	//        intent.putExtra(EXTRA_MESSAGE, message);
	        startActivity(intent);
    	}
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

  
    if (keyCode == KeyEvent.KEYCODE_MENU) {
    //Do Code Here
    // If want to block just return false
    return false;
    }
   

    return super.onKeyDown(keyCode, event);
    }
}
