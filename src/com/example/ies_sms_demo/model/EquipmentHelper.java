package com.example.ies_sms_demo.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.util.Log;

import com.example.ies_sms_demo.R;

public class EquipmentHelper {
	public static int[] photos={R.drawable.tank1,R.drawable.tank2,R.drawable.tank3,R.drawable.tank4,R.drawable.tank5,R.drawable.tank6};
	public final static String TAG_PROJECT_LIST="project_list";
//	  public static ArrayList<Equipment> getEquipementList(Resources resources){
//		   InputStream is = resources.openRawResource(R.raw.data);
//	       Writer writer = new StringWriter();
//	       char[] buffer = new char[1024];
//	       try {
//	           Reader reader;
//					reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//				
//	           int n;
//	           while ((n = reader.read(buffer)) != -1) {
//	               writer.write(buffer, 0, n);
//	           }
//	       } catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}finally {
//	           try {
//					is.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	       }
//
//	       String jsonString = writer.toString();
//	       JSONObject jObj = null;
//	       ArrayList<Equipment> equiments=new ArrayList<Equipment>();
//	       try {
//	           jObj = new JSONObject(jsonString);
//	           JSONArray eList=jObj.getJSONArray(TAG_PROJECT_LIST);
//	          
//	           for (int i = 0; i < eList.length(); i++) {
//	        	   
//	               JSONObject c = eList.getJSONObject(i);
//	               Equipment e=new Equipment(c);
//	               e.machine.photoId=photos[i];
//	               equiments.add(e);
//
//
//	           }
//	          
//	       } catch (JSONException e) {
//	           Log.e("Equipement", "Error parsing data " + e.toString());
//	       }
//	       return equiments;
//	   }
	  public static ArrayList<Project> getProjectList(String jsonString){
		
	       JSONObject jObj = null;
	       ArrayList<Project> projects=new ArrayList<Project>();
	       if(!jsonString.isEmpty()){
		       try {
		           jObj = new JSONObject(jsonString);
		           JSONArray eList=jObj.getJSONArray(TAG_PROJECT_LIST);
		          
		           for (int i = 0; i < eList.length(); i++) {
		        	   
		               JSONObject c = eList.getJSONObject(i);
		               Project p=new Project(c);
		               projects.add(p);
	
	
		           }
		          
		       } catch (JSONException e) {
		           Log.e("Equipement", "Error parsing data " + e.toString());
		       }
	       }
	       return projects;
	       }
	
}
