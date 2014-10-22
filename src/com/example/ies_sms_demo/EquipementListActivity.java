package com.example.ies_sms_demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ies_sms_demo.LoginActivity.AttemptLogin;
import com.example.ies_sms_demo.http.JSONParser;
import com.example.ies_sms_demo.model.Equipement;
import com.example.ies_sms_demo.model.EquipementHelper;


public class EquipementListActivity extends ListActivity {
	JSONParser jsonParser = new JSONParser();
	public SharedPreferences sharedPref;
	  private ProgressDialog pDialog;
	    private static final String TAG_SUCCESS = "status";
	    private static final String TAG_TOKEN = "token";
	    private static final String TAG_MESSAGE = "message";
	private static final String GET_EQUIPEMENT_URL = "http://uniquecode.net/job/ms/get_user_equipement.php";
	private EquipementItemAdapter e_adapter;
	private ArrayList<Equipement> equipements = new ArrayList<Equipement>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        equipements= EquipementHelper.getEquipementList( getResources());
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Royal garden");
        setContentView(R.layout.equipements);
        getActionBar().setIcon(
        		   new ColorDrawable(getResources().getColor(android.R.color.transparent)));    
        e_adapter = new EquipementItemAdapter(this, R.id.text_view, equipements);
        setListAdapter(e_adapter);
        sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
        new GetUserEquipement().execute();
    }
    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	
      
          Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
  
          intent.putExtra("index", position);
          startActivity(intent);
    }
   
  class GetUserEquipement extends AsyncTask {

        /**
        * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;
       
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(EquipementListActivity.this);
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
           
			String username = sharedPref.getString(getString(R.string.user_name), "");
			String token = sharedPref.getString(getString(R.string.token), "");
			if(username==""||token==""){
				 Intent i = new Intent(EquipementListActivity.this, LoginActivity.class);
                 finish();
                 startActivity(i);
			}else{
	           try {
	               // Building Parameters
	               List params = new ArrayList();
	               params.add(new BasicNameValuePair("txtUName", username));
	               params.add(new BasicNameValuePair("txtToken", token));
	
	               Log.d("request!", "starting");
	               // getting product details by making HTTP request
	               JSONObject json = jsonParser.makeHttpRequest(
	                      GET_EQUIPEMENT_URL, "POST", params);
	
	               // check your log for json response
	               Log.d("Login attempt", json.toString());
	
	      	    	
	      	    	SharedPreferences.Editor editor = sharedPref.edit();
	               // json success tag
	      	    	if(json!=null){
			               success = json.getInt(TAG_SUCCESS);
			               if (success == 200) {
			                   Log.d("Login Successful!", json.toString());
			                   
					    	    	editor.putString(getString(R.string.user_name), username);
					    	    	editor.putString(getString(R.string.token), json.getString(TAG_TOKEN));
					    	    	editor.commit();
			                   
			                   
			                 
			                   return json.getString(TAG_MESSAGE);
			               }else{
			            
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
               Toast.makeText(EquipementListActivity.this, result.toString(), Toast.LENGTH_LONG).show();
           }
       }

	    
}
   
}
