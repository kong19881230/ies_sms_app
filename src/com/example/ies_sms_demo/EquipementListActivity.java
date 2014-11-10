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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ies_sms_demo.ItemAdapter.EquipementItemAdapter;
import com.example.ies_sms_demo.LoginActivity.AttemptLogin;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.http.JSONParser;
import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.EquipmentHelper;
import com.example.ies_sms_demo.model.Project;


public class EquipementListActivity extends ListActivity {
	JSONParser jsonParser = new JSONParser();
	public SharedPreferences sharedPref;
	  private ProgressDialog pDialog;
	    private static final String TAG_SUCCESS = "status";
	    private static final String TAG_TOKEN = "token";
	    private static final String TAG_MESSAGE = "message";
	private static final String GET_EQUIPEMENT_URL = "http://uniquecode.net/job/ms/get_user_equipement.php";
	private static final String PROJECT_PHOTO_URL="http://uniquecode.net/job/ms/photo/project/";
	private EquipementItemAdapter e_adapter;
	private ArrayList<Project> projects=new ArrayList<Project>();
	private ImageLoader imgLoader;
	private ImageView photo;
	private ArrayList<Equipment> equipements = new ArrayList<Equipment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
        String projectListStr = sharedPref.getString(getString(R.string.project_list), "");
        projects= EquipmentHelper.getProjectList( projectListStr);
        setContentView(R.layout.equipements);
        photo =(ImageView)findViewById(R.id.photo);
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(projects.size()>0){
	        setTitle(projects.get(0).name_en);
	        equipements=(ArrayList<Equipment>) projects.get(0).equipments;
	        imgLoader = new ImageLoader(getApplicationContext());
	  
	        if(projects.get(0).photo!=null &&!projects.get(0).photo.isEmpty()){
	        	imgLoader.DisplayImage(PROJECT_PHOTO_URL+projects.get(0).photo, R.drawable.ic_action_refresh, photo);
	        }
	    }
       
        getActionBar().setIcon(
        		   new ColorDrawable(getResources().getColor(android.R.color.transparent)));    
        
        e_adapter = new EquipementItemAdapter(this, R.id.text_view, equipements);
        setListAdapter(e_adapter);
        boolean isRefresh=getIntent().getBooleanExtra("isRefresh", false);
        if(isRefresh){
        	new GetUserEquipement().execute();
        }
    }
    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	
      
          Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
 
          intent.putExtra("eIndex", position);
          intent.putExtra("pIndex", 0);
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
           pDialog.setCancelable(false);
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
	
	             
	
	      	    	
	      	    	SharedPreferences.Editor editor = sharedPref.edit();
	               // json success tag
	      	    	if(json!=null){
		      	    	  // check your log for json response
		 	               Log.d("Login attempt", json.toString());
			               success = json.getInt(TAG_SUCCESS);
			               if (success == 200) {
			                   Log.d("Login Successful!", json.toString());
			                   
					    	    	editor.putString(getString(R.string.project_list), json.toString());			
					    	    	editor.commit();
			                   
					    	    	projects= EquipmentHelper.getProjectList( json.toString());
					    	        //Remove notification bar
//					    	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
					    	        if(projects.size()>0){
					    		        //setTitle(projects.get(0).name_en);
					    		        equipements=(ArrayList<Equipment>) projects.get(0).equipments;
					    		        
						    	        
					    	        }
					    	      
			                   return json.getString(TAG_MESSAGE);
			               }else{
			            	   failure=true;
				    	    	editor.commit();
			                   Log.d("Login Failure!", json.getString(TAG_MESSAGE));
			                   return json.getString(TAG_MESSAGE);      
			               }
	      	    	}else{
	      	    		failure=true;
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
    	   if(!failure){
	    	   e_adapter.clear();
	    	   e_adapter.addAll(equipements);
	    	   e_adapter.notifyDataSetChanged();
	    	   if(projects.size()>0){
	    		   imgLoader = new ImageLoader(getApplicationContext());
	    		
		   	        if(projects.get(0).photo!=null &&!projects.get(0).photo.isEmpty()){
		   	        	imgLoader.DisplayImage(PROJECT_PHOTO_URL+projects.get(0).photo, R.drawable.ic_action_refresh, photo);
		   	        }
	    	   }
    	   }
           pDialog.dismiss();
           if (result != null){
               Toast.makeText(EquipementListActivity.this, result.toString(), Toast.LENGTH_LONG).show();
           }
       }

	    
}
   
}
