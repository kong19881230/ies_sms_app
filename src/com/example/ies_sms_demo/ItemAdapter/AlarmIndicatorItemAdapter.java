package com.example.ies_sms_demo.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.ies_sms_demo.R;
import com.example.ies_sms_demo.R.drawable;
import com.example.ies_sms_demo.R.id;
import com.example.ies_sms_demo.R.layout;
import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.Indicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmIndicatorItemAdapter extends ArrayAdapter<Indicator> {
	private ArrayList<Indicator> objects;
	public AlarmIndicatorItemAdapter(Context context, int textViewResourceId,
			ArrayList<Indicator> objects) {
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
			v = inflater.inflate(R.layout.ip_indicator_item, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Indicator i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView text = (TextView) v.findViewById(R.id.text);
			ImageView state = (ImageView) v.findViewById(R.id.logo);
			if(state!=null){
				if(i.style_type.equals(Indicator.STYLE_TYPE_NS)){
					if(i.state!=null&&i.state.equals(i.normal_status)){
						state.setImageDrawable(v.getResources().getDrawable(
								R.drawable.safe_icon));
					}else{
						state.setImageDrawable(v.getResources().getDrawable(
								R.drawable.danger_icon));
					}
				}else if (i.style_type.equals(Indicator.STYLE_TYPE_OS)) {
					if(i.state!=null&&i.state.equals(i.normal_status)){
						state.setImageDrawable(v.getResources().getDrawable(
								R.drawable.on));
					}else{
						state.setImageDrawable(v.getResources().getDrawable(
								R.drawable.off));
					}
				}
			}
			// check to see if each individual textview is null.
			// if not, assign some text!
			if (text != null){
				text.setText(i.name_en);
			}
		
		
		}

		// the view must be returned to our activity
		return v;

	}
}
