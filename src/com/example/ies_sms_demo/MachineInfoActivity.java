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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ies_sms_demo.EquipementListActivity.GetUserEquipement;
import com.example.ies_sms_demo.ItemAdapter.EquipementItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.GalleryItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.MachineAttributeAdapter;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.http.JSONParser;
import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.EquipmentHelper;


public class MachineInfoActivity extends ListActivity {
	JSONParser jsonParser = new JSONParser();
	public final String TAG_EQUIPEMENT_NOTE="equipment_note";
	
	public SharedPreferences sharedPref;
	  private ProgressDialog pDialog;
	    private static final String TAG_SUCCESS = "status";
	    private static final String TAG_TOKEN = "token";
	    private static final String TAG_MESSAGE = "message";

		private static final String GET_EQUIPEMENT_URL = "http://uniquecode.net/job/ms/get_user_equipement.php";
	private static final String ADD_NOTE_URL = "http://uniquecode.net/job/ms/add_note.php";
	public int pIndex;
	public int eIndex;
	public EditText newNote;
	public TextView note;
	private GalleryItemAdapter m_adapter;
	public ListView photoList;
	public MachineAttributeAdapter attributeAdapter;
	public Equipment equipment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eIndex=getIntent().getIntExtra("eIndex", 0);
	    pIndex=getIntent().getIntExtra("pIndex", 0);
	    sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        equipment= (Equipment) getIntent().getSerializableExtra("Equipement");
        ScrollView  mainScrollView = (ScrollView)findViewById(R.id.scrollView1);
        setContentView(R.layout.machine_info);
        setTitle(equipment.modelId);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(
     		   new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
        ImageView img = (ImageView) findViewById(R.id.photo);
	   
        TextView type = (TextView) findViewById(R.id.type);
        m_adapter=new GalleryItemAdapter(this, R.id.photo_list_text_view, equipment.gallery);
        setListAdapter(m_adapter);
        ViewGroup.LayoutParams params1 = getListView().getLayoutParams();
	     params1.height =Dp2Px(getApplicationContext(), equipment.gallery.size()*200);
	     getListView().setLayoutParams(params1);
	     getListView().setFocusable(false);
		// check to see if each individual textview is null.
		// if not, assign some text!
		if (type != null){
			type.setText(equipment.type);
		}
		ImageLoader imgLoader = new ImageLoader(getApplicationContext());
		  
	    if(img!=null){
	    	 
		        if(equipment.photo!=null &&!equipment.photo.isEmpty()){
		        	imgLoader.DisplayImage(EquipementItemAdapter.EQUIPMENT_PHOTO_URL+equipment.photo, R.drawable.ic_action_refresh, img);
		        }
	       
	     }
	    photoList=(ListView)findViewById(R.id.photo_list);
	    
	    attributeAdapter= new MachineAttributeAdapter(getApplicationContext(), R.id.text_view, equipment.equipmentAttributes);
	    photoList.setAdapter(attributeAdapter);
	    ViewGroup.LayoutParams params = photoList.getLayoutParams();
	     params.height =Dp2Px(getApplicationContext(), equipment.equipmentAttributes.size()*87);
	     photoList.setLayoutParams(params);
	    note = (TextView) findViewById(R.id.note);
	    newNote=(EditText)findViewById(R.id.new_note); 
	    if (note != null){
	    	note.setText(equipment.note);
		}
	    photoList.setFocusable(false);
	  //  mainScrollView.fullScroll(ScrollView.FOCUS_UP);
    }
	public int Dp2Px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	}

    public void addNote(View view){
   	 if(newNote!=null&&!newNote.getText().toString().isEmpty()){
    	new AddNoteEquipement().execute();
   	 }else{
   		Toast.makeText(MachineInfoActivity.this, "New Note is empty", Toast.LENGTH_LONG).show();
   	 }
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
//    @Override
//    public void onBackPressed() {
//    	 Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
//         intent.putExtra("eIndex", eIndex);
//         intent.putExtra("pIndex", pIndex);
//         startActivity(intent);
//    }

    class AddNoteEquipement extends AsyncTask {

        /**
        * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;
       
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(MachineInfoActivity.this);
           pDialog.setMessage("Adding Note...");
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
				 Intent i = new Intent(MachineInfoActivity.this, LoginActivity.class);
                 finish();
                 startActivity(i);
			}else{
	           try {
	               // Building Parameters
	               List params = new ArrayList();
	               params.add(new BasicNameValuePair("txtUName", username));
	               params.add(new BasicNameValuePair("txtToken", token));
	              
	               params.add(new BasicNameValuePair("note", newNote.getText().toString()));
	               params.add(new BasicNameValuePair("equipment_id", ""+equipment.id));
	               Log.d("request!", "starting");
	               // getting product details by making HTTP request
	               JSONObject json = jsonParser.makeHttpRequest(
	                      ADD_NOTE_URL, "POST", params);
	
	             
	
	      	    	
	      	    	SharedPreferences.Editor editor = sharedPref.edit();
	               // json success tag
	      	    	if(json!=null){
		      	    	  // check your log for json response
		 	               Log.d("Login attempt", json.toString());
			               success = json.getInt(TAG_SUCCESS);
			               if (success == 200) {
			                   Log.d("Login Successful!", json.toString());
			                   
					    	    	
					    	    	
					    	    	equipment.note=json.getString(TAG_EQUIPEMENT_NOTE);

					     		  
					    	      
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
    	   if(!failure&&note!=null){
	    	  if(newNote!=null){
	    		  note.setText(equipment.note);
	    		  newNote.setText("");
	    	  }
	    	  
    	   }
    	   new GetUserEquipement().execute();
           pDialog.dismiss();
           if (result != null){
               Toast.makeText(MachineInfoActivity.this, result.toString(), Toast.LENGTH_LONG).show();
           }
       }

	    
}
    class GetUserEquipement extends AsyncTask {

        /**
        * Before starting background thread Show Progress Dialog
        * */
       boolean failure = false;
       
      
       
       @Override
       protected String doInBackground(Object... args) {
           // TODO Auto-generated method stub
            // Check for success tag
           int success;
           
			String username = sharedPref.getString(getString(R.string.user_name), "");
			String token = sharedPref.getString(getString(R.string.token), "");
			if(username==""||token==""){
				 Intent i = new Intent(MachineInfoActivity.this, LoginActivity.class);
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
			                   
					    	    	
					    	      
			                   return json.getString(TAG_MESSAGE);
			               }else if(success==400){
			            	   Intent i = new Intent(MachineInfoActivity.this, LoginActivity.class);
			                   finish();
			                   startActivity(i);
			               }
			               else{
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
         
       }

	    
}
}
