package com.example.ies_sms_demo.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.ies_sms_demo.R;
import com.example.ies_sms_demo.R.drawable;
import com.example.ies_sms_demo.R.id;
import com.example.ies_sms_demo.R.layout;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.model.Equipment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EquipementItemAdapter extends ArrayAdapter<Equipment> {
	private ArrayList<Equipment> objects;
	public EquipementItemAdapter(Context context, int textViewResourceId,
			ArrayList<Equipment> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}
	public static final String EQUIPMENT_PHOTO_URL="http://uniquecode.net/job/ms/photo/equipment/";
	
	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.equipement_item, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Equipment i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.
			
			TextView refNo = (TextView) v.findViewById(R.id.refNo);
			TextView type = (TextView) v.findViewById(R.id.type);
			ImageView photo = (ImageView) v.findViewById(R.id.photo);
			
			if(photo!=null){
				ImageLoader imgLoader = new ImageLoader(v.getContext());
				  
		        if(i.photo!=null &&!i.photo.isEmpty()){
		        	imgLoader.DisplayImage(EQUIPMENT_PHOTO_URL+i.photo, R.drawable.ic_action_refresh, photo);
		        }
	
			}
			// check to see if each individual textview is null.
			// if not, assign some text!
			if (refNo != null){
				refNo.setText("Ref No: "+i.refNo);
			}
			if (type != null){
				type.setText(i.modelId);
			}
		
		}

		// the view must be returned to our activity
		return v;

	}
}
