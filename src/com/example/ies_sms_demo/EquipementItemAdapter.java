package com.example.ies_sms_demo;

import java.util.ArrayList;
import java.util.List;

import com.example.ies_sms_demo.model.Equipement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EquipementItemAdapter extends ArrayAdapter<Equipement> {
	private ArrayList<Equipement> objects;
	public EquipementItemAdapter(Context context, int textViewResourceId,
			ArrayList<Equipement> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

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
		Equipement i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView refNo = (TextView) v.findViewById(R.id.refNo);
			TextView type = (TextView) v.findViewById(R.id.type);
			ImageView photo = (ImageView) v.findViewById(R.id.photo);
			
			if(photo!=null){
			photo.setImageDrawable(v.getResources().getDrawable(
					i.machine.photoId));
			}
			// check to see if each individual textview is null.
			// if not, assign some text!
			if (refNo != null){
				refNo.setText("Ref No: "+i.refNo);
			}
			if (type != null){
				type.setText("Equipement: "+i.machine.type);
			}
		
		}

		// the view must be returned to our activity
		return v;

	}
}
