package com.example.ies_sms_demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Machine  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String TAG_TYPE="type";
	public final String TAG_MODEL_ID="model_id";
	public final String TAG_MACHINE_ATTRIBUTES="machine_attributes";
	
//	public final String TAG_MATERIAL="material";
//	public final String TAG_CAPACITY="capacity";
//	public final String TAG_PUMP="pump";
//	public final String TAG_HEAT_EXCHANGER="heat_exchanger";
//	public final String TAG_BACK_UP_HEATER="back_up_heater";
//	public final String TAG_INSULATION="insulation";
//	public final String TAG_PRESSURE="pressure";
	public Machine(JSONObject json){
	  try {
		type=json.getString(TAG_TYPE);
		modelId=json.getString(TAG_MODEL_ID);
		JSONArray attributes=json.getJSONArray(TAG_MACHINE_ATTRIBUTES);
		machineAttributes=new ArrayList<MachineAttribute>();
		for(int i=0;i<attributes.length();i++){
			machineAttributes.add(new MachineAttribute(attributes.getJSONObject(i)));
		}
		
	  } catch (JSONException e) {
          e.printStackTrace();
      }
	}
	public String modelId;
	public String type;
	public List<MachineAttribute> machineAttributes;
}
