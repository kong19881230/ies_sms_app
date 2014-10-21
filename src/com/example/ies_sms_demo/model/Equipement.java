package com.example.ies_sms_demo.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Equipement  implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public final String TAG_REF_NO="ref_no";
	public final String TAG_PHONE_NUMBER="phone_number";
	public final String TAG_MACHINE="machine";
	public Equipement(JSONObject json){
		  try {
			  refNo=json.getString(TAG_REF_NO);
			  phoneNumber=json.getString(TAG_PHONE_NUMBER);
			  machine=new Machine(json.getJSONObject(TAG_MACHINE));
		
		  } catch (JSONException e) {
	          e.printStackTrace();
	      }
		}
	public String refNo;
	public String phoneNumber;
	public Machine machine;
}
