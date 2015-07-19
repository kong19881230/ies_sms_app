package com.example.ies_sms_demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ies_sms_demo.ItemAdapter.EquipementItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.GalleryItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.MachineAttributeAdapter;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.EquipmentAttribute;

public class EInfoFragment extends ListFragment {
	private Context rootContext;
	private View rootView;
	public Equipment equipment;

	private MachineAttributeAdapter m_adapter;
	public EInfoFragment() {
	}

	public EInfoFragment(Context context,Equipment e) {
		this.rootContext = context;
		equipment=e;
	}

	@Override
	public void onResume() {
		super.onResume();



	}

	public void updateView() {
	

	   

	}
	public int Dp2Px(Context context, float dp) { 
	    final float scale = context.getResources().getDisplayMetrics().density; 
	    return (int) (dp * scale + 0.5f); 
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.e_tab_info, container, false);
		if(equipment!=null){
			if(equipment.modelId!=null){
				equipment.equipmentAttributes.add(0, new EquipmentAttribute("Model No.",equipment.modelId));
			}
		    m_adapter=new MachineAttributeAdapter(rootContext, R.id.text_view, equipment.equipmentAttributes);
	        setListAdapter(m_adapter);
		}
		return rootView;
	}

	

}
