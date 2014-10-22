package com.example.ies_sms_demo;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ies_sms_demo.http.JSONParser;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {
	  JSONParser jsonParser = new JSONParser();
	  private EditText user, pass;
	  private ProgressDialog pDialog;
	    private static final String LOGIN_URL = "http://uniquecode.net/job/ms/authentication.php";
	    private static final String TAG_SUCCESS = "status";
	    private static final String TAG_TOKEN = "token";
	    private static final String TAG_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        user=(EditText)findViewById(R.id.username); 
    	pass=(EditText)findViewById(R.id.password);
        
    }

    public void login(View view){
    	 
    	  new AttemptLogin().execute();

	    
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
    
    class AttemptLogin extends AsyncTask {

        /**
        * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;
       
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(LoginActivity.this);
           pDialog.setMessage("Attempting login...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }
       
       @Override
       protected String doInBackground(Object... args) {
           // TODO Auto-generated method stub
            // Check for success tag
           int success;
           String username = user.getText().toString();
           String password = pass.getText().toString();
           try {
               // Building Parameters
               List params = new ArrayList();
               params.add(new BasicNameValuePair("txtUName", username));
               params.add(new BasicNameValuePair("txtPass", password));

               Log.d("request!", "starting");
               // getting product details by making HTTP request
               JSONObject json = jsonParser.makeHttpRequest(
                      LOGIN_URL, "POST", params);
               if(json!=null){
               // check your log for json response
	               Log.d("Login attempt", json.toString());
	
	      	    	SharedPreferences sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
	      	    	SharedPreferences.Editor editor = sharedPref.edit();
	               // json success tag
	               success = json.getInt(TAG_SUCCESS);
	               if (success == 200) {
	                   Log.d("Login Successful!", json.toString());
	                   
			    	    	editor.putString(getString(R.string.user_name), username);
			    	    	editor.putString(getString(R.string.token), json.getString(TAG_TOKEN));
			    	    	editor.commit();
	                   
	                   
	                   Intent i = new Intent(LoginActivity.this, EquipementListActivity.class);
	                   
	                   finish();
	                   startActivity(i);
	                   return json.getString(TAG_MESSAGE);
	               }else{
	            	   editor.putString(getString(R.string.user_name), "");
		    	    	editor.putString(getString(R.string.token), "");
		    	    	editor.commit();
	                   Log.d("Login Failure!", json.getString(TAG_MESSAGE));
	                   return json.getString(TAG_MESSAGE);      
	               }
               }else{
            	   return "No Network Connection"; 
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
          return null;  
       }
       /**
        * After completing background task Dismiss the progress dialog
        * **/
       @Override
       protected void onPostExecute(Object result) {
           // dismiss the dialog once product deleted
          pDialog.dismiss();
           if (result != null){
               Toast.makeText(LoginActivity.this, result.toString(), Toast.LENGTH_LONG).show();
           }
       }

	    
}
}