package com.example.ies_sms_demo.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Equipment  implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public final String TAG_REF_NO="ref_no";
	public final String TAG_PHONE_NUMBER="phone_num";
	public final String TAG_MACHINE="machine";
	public final String TAG_PHOTO="photo";
	public final String TAG_INDICATORS="indicators";
	public Equipment(JSONObject json){
		  try {
			  refNo=json.getString(TAG_REF_NO);
			  phoneNumber=json.getString(TAG_PHONE_NUMBER);
			  machine=new Machine(json);
			  JSONArray indicatorArr=json.getJSONArray(TAG_INDICATORS);
			  indicators=new ArrayList<Indicator>();
				for(int i=0;i<indicatorArr.length();i++){
					indicators.add(new Indicator(indicatorArr.getJSONObject(i)));
				}
				if(json.has(TAG_PHOTO)){
					photo=json.getString(TAG_PHOTO);
				}
		  } catch (JSONException e) {
	          e.printStackTrace();
	      }
		}
	public String refNo;
	public String phoneNumber;
	public int photoId;
	public String photo;
	public Machine machine;
	public ArrayList<Indicator> indicators;
}
