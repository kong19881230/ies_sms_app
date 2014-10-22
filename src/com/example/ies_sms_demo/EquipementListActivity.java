package com.example.ies_sms_demo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.ies_sms_demo.model.Equipement;
import com.example.ies_sms_demo.model.EquipementHelper;


public class EquipementListActivity extends ListActivity {
	
	private static final String LOGIN_URL = "http://uniquecode.net/job/ms/get_user_equipement.php";
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
           SharedPreferences sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
			String username = sharedPref.getString(getString(R.string.user_name), "");
			String token = sharedPref.getString(getString(R.string.token), "");
           try {
               // Building Parameters
               List params = new ArrayList();
               params.add(new BasicNameValuePair("txtUName", username));
               params.add(new BasicNameValuePair("txtToken", token));

               Log.d("request!", "starting");
               // getting product details by making HTTP request
               JSONObject json = jsonParser.makeHttpRequest(
                      LOGIN_URL, "POST", params);

               // check your log for json response
               Log.d("Login attempt", json.toString());

      	    	
      	    	SharedPreferences.Editor editor = sharedPref.edit();
               // json success tag
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
           } catch (JSONException e) {
               e.printStackTrace();
           }
          return null;  
       }
       /**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
           // dismiss the dialog once product deleted
           pDialog.dismiss();
           if (file_url != null){
               Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
           }
       }

	    
}
   
}
