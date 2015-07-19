package com.example.ies_sms_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.ies_sms_demo.ItemAdapter.EquipementItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.GalleryItemAdapter;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.model.Equipment;

public class EGalleryFragment extends ListFragment {
	private Context rootContext;
	private View rootView;
	public Equipment equipment;

	private GalleryItemAdapter m_adapter;
	public EGalleryFragment() {
	}

	public EGalleryFragment(Context context,Equipment e) {
		this.rootContext = context;
		equipment=e;
	}

	@Override
	public void onResume() {
		super.onResume();

        ViewGroup.LayoutParams params1 = getListView().getLayoutParams();
	     params1.height =Dp2Px(rootContext, equipment.gallery.size()*240);
	     getListView().setLayoutParams(params1);
	     getListView().setFocusable(false);

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
		rootView = inflater.inflate(R.layout.e_tab_gallery, container, false);
	    ImageView img = (ImageView) rootView.findViewById(R.id.photo);
    	ImageLoader imgLoader = new ImageLoader(rootContext);
		  
	    if(img!=null){
	    	 
		        if(equipment.photo!=null &&!equipment.photo.isEmpty()){
		        	imgLoader.DisplayImage(EquipementItemAdapter.EQUIPMENT_PHOTO_URL+equipment.photo, R.drawable.ic_action_refresh, img);
		        }
		       
		        img.setOnClickListener(new OnClickListener() {
		            public void onClick(View v) {
		            	Intent intent = new Intent(rootContext, PhotoActivity.class);
		            	intent.putExtra("photo", equipment.photo);
		            	startActivity(intent);
		            }
		        });
	     }
	    
	    m_adapter=new GalleryItemAdapter(rootContext, R.id.text_view, equipment.gallery);
        setListAdapter(m_adapter);
		
		return rootView;
	}

	@Override
	public void onListItemClick (ListView l, View v, int position, long id) {
    	
		
          Intent intent = new Intent(rootContext, PhotoActivity.class);
// 
          intent.putExtra("photo", equipment.gallery.get(position));
//          intent.putExtra("pIndex", 0);
          startActivity(intent);
    }	

}
