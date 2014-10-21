package com.example.ies_sms_demo.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Machine  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String TAG_PHOTO="photo";
	public final String TAG_TYPE="type";
	public final String TAG_MATERIAL="material";
	public final String TAG_CAPACITY="capacity";
	public final String TAG_PUMP="pump";
	public final String TAG_HEAT_EXCHANGER="heat_exchanger";
	public final String TAG_BACK_UP_HEATER="back_up_heater";
	public final String TAG_INSULATION="insulation";
	public final String TAG_PRESSURE="pressure";
	public Machine(JSONObject json){
	  try {
		photo=json.getString(TAG_PHOTO);
		type=json.getString(TAG_TYPE);
		material=json.getString(TAG_MATERIAL);
		capacity=json.getString(TAG_CAPACITY);
		pump=json.getString(TAG_PUMP);
		heatExchanger=json.getString(TAG_HEAT_EXCHANGER);
		backUpHeater=json.getString(TAG_BACK_UP_HEATER);
		insulation=json.getString(TAG_INSULATION);
		pressure=json.getString(TAG_PRESSURE);
	  } catch (JSONException e) {
          e.printStackTrace();
      }
	}
	public int photoId;
	public String photo;
	public String type;
	public String material;
	public String capacity;
	public String pump;
	public String heatExchanger;
	public String backUpHeater;
	public String insulation;
	public String pressure;
}
