package com.example.ies_sms_demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Indicator  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String TAG_TYPE="type";
	public final String TAG_CHANNEL="channel";
	public final String TAG_NAME_EN="name_en";
	public final String TAG_NAME_CN="name_cn";
	public final String TAG_UNIT="unit";
	public final String TAG_NORMAL_STATUS="normal_status";
	public final String TAG_STYLE_TYPE="style_type";
	public final String TAG_IS_SHOW_TEXT="is_show_text";
	public static final String TYPE_CH="CH";
	public static final String TYPE_IP="IP";
	
	public static final String STYLE_TYPE_NS="NS";
	public static final String STYLE_TYPE_OS="OS";
	public static final String STYLE_TYPE_NB="NB";
	public Indicator(JSONObject json){
	  try {
		type=json.getString(TAG_TYPE);
		channel=json.getString(TAG_CHANNEL);
		name_en=json.getString(TAG_NAME_EN);
		name_cn=json.getString(TAG_NAME_CN);
		unit=json.getString(TAG_UNIT);
		normal_status=json.getString(TAG_NORMAL_STATUS);
		style_type=json.getString(TAG_STYLE_TYPE);
		is_show_text=json.getBoolean(TAG_IS_SHOW_TEXT);
	  } catch (JSONException e) {
          e.printStackTrace();
      }
	}
	public String state;
	public String text="";
	public String type;
	public String channel;
	public String name_en;
	public String name_cn;
	public String unit;
	public String normal_status;
	public boolean is_show_text;
	public String style_type;
}
