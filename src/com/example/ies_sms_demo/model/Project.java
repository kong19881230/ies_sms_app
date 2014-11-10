package com.example.ies_sms_demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Project implements Serializable {

	/**
	 * 
	 */
	 public final String TAG_NAME_CN="name_cn";
    public final String TAG_NAME_EN="name_en";
    public final String TAG_PHOTO="photo";
    public final String TAG_EQUIPMENTS="equipments";
	public Project(JSONObject json){
		try {
			name_cn=json.getString(TAG_NAME_CN);
			name_en=json.getString(TAG_NAME_EN);
			
			 JSONArray eList=json.getJSONArray(TAG_EQUIPMENTS);
			 equipments=new ArrayList<Equipment>();
	           for (int i = 0; i < eList.length(); i++) {
	        	   
	               JSONObject c = eList.getJSONObject(i);
	               Equipment e=new Equipment(c);
	               e.photoId=0;
	               equipments.add(e);
	           }
	           if(json.has(TAG_PHOTO)){
	        	   photo=json.getString(TAG_PHOTO);
	           }
	          
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static final long serialVersionUID = 1L;
	public String name_cn;
	public String name_en;
	public String photo;
	public ArrayList<Equipment> equipments;
}
