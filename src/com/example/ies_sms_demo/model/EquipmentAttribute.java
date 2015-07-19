package com.example.ies_sms_demo.model;


import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class EquipmentAttribute implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String TAG_VALUE="value";
	public final String TAG_NAME_EN="name_en";
	public final String TAG_NAME_CN="name_cn";
	
	public String name_cn;
	public String name_en;
	public String value;
	public EquipmentAttribute(JSONObject json){
		try {
			name_cn=json.getString(TAG_NAME_CN);
			name_en=json.getString(TAG_NAME_EN);
			value=json.getString(TAG_VALUE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public EquipmentAttribute(String name,String value){
		name_cn=name;
			name_en=name;
			this.value=value;
		
	}
}
