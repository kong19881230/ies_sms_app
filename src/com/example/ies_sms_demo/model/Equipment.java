package com.example.ies_sms_demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Equipment  implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public final static String SMS="sms";
	public final static String WEB="web";

	public final String TAG_TYPE="type";
	public final String TAG_MODEL_ID="model_id";
	public final String TAG_EQUIPMENT_ATTRIBUTES="equipment_attributes";
	public final String TAG_REF_NO="ref_no";
	public final String TAG_PHONE_NUMBER="phone_num";
	public final String TAG_PHOTO="photo";
	public final String TAG_GALLERY="gallery";
	public final String TAG_INDICATORS="indicators";
	public final String TAG_COM_METHOD="com_method";
	public Equipment(JSONObject json){
		  try {
			  refNo=json.getString(TAG_REF_NO);
			  phoneNumber=json.getString(TAG_PHONE_NUMBER);
			 
			  type=json.getString(TAG_TYPE);
				modelId=json.getString(TAG_MODEL_ID);
				com_method=json.getString(TAG_COM_METHOD);
				String galleryStr=json.getString(TAG_GALLERY);
				gallery=new ArrayList<String>();
			       if(!galleryStr.isEmpty()){
			    	   	   JSONArray gList = new JSONArray(galleryStr);
				           for (int i = 0; i < gList.length(); i++) {
				               String c = gList.getString(i);
				               gallery.add(c);
				           }
				     
			       }
				JSONArray attributes=json.getJSONArray(TAG_EQUIPMENT_ATTRIBUTES);
				equipmentAttributes=new ArrayList<EquipmentAttribute>();
				for(int i=0;i<attributes.length();i++){
					equipmentAttributes.add(new EquipmentAttribute(attributes.getJSONObject(i)));
				}
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
	
	public String com_method;
	public ArrayList<String> gallery;
	public String modelId;
	public String type;
	public ArrayList<EquipmentAttribute> equipmentAttributes;
	public String refNo;
	public String phoneNumber;
	public int photoId;
	public String photo;
	public String note;
	public ArrayList<Indicator> indicators;
}
