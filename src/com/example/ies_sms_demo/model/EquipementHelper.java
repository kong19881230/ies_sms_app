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

public class EquipementHelper {
	public static int[] photos={R.drawable.tank1,R.drawable.tank2,R.drawable.tank3,R.drawable.tank4,R.drawable.tank5,R.drawable.tank6};
	public final static String TAG_EQUIPEMENTS="equipments";
	  public static ArrayList<Equipement> getEquipementList(Resources resources){
		   InputStream is = resources.openRawResource(R.raw.data);
	       Writer writer = new StringWriter();
	       char[] buffer = new char[1024];
	       try {
	           Reader reader;
					reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				
	           int n;
	           while ((n = reader.read(buffer)) != -1) {
	               writer.write(buffer, 0, n);
	           }
	       } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
	           try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       }

	       String jsonString = writer.toString();
	       JSONObject jObj = null;
	       ArrayList<Equipement> equiments=new ArrayList<Equipement>();
	       try {
	           jObj = new JSONObject(jsonString);
	           JSONArray eList=jObj.getJSONArray(TAG_EQUIPEMENTS);
	          
	           for (int i = 0; i < eList.length(); i++) {
	        	   
	               JSONObject c = eList.getJSONObject(i);
	               Equipement e=new Equipement(c);
	               e.machine.photoId=photos[i];
	               equiments.add(e);


	           }
	          
	       } catch (JSONException e) {
	           Log.e("Equipement", "Error parsing data " + e.toString());
	       }
	       return equiments;
	   }

}
