package com.example.ies_sms_demo;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ies_sms_demo.ItemAdapter.EquipementItemAdapter;
import com.example.ies_sms_demo.ItemAdapter.MachineAttributeAdapter;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.Machine;


public class MachineInfoActivity extends ListActivity {
	
	public final String TAG_EQUIPEMENTS="equipments";
	public int pIndex;
	public int eIndex;
	private MachineAttributeAdapter m_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eIndex=getIntent().getIntExtra("eIndex", 0);
	    pIndex=getIntent().getIntExtra("pIndex", 0);
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Equipment equipment= (Equipment) getIntent().getSerializableExtra("Equipement");
        Machine machine=equipment.machine;
        setContentView(R.layout.machine_info);
        setTitle(machine.modelId);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(
     		   new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
        ImageView img = (ImageView) findViewById(R.id.photo);
	   
        TextView type = (TextView) findViewById(R.id.type);
        m_adapter = new MachineAttributeAdapter(this, R.id.text_view, machine.machineAttributes);
        setListAdapter(m_adapter);
		// check to see if each individual textview is null.
		// if not, assign some text!
		if (type != null){
			type.setText(machine.type);
		}
	    if(img!=null){
	    	 ImageLoader imgLoader = new ImageLoader(getApplicationContext());
			  
		        if(equipment.photo!=null &&!equipment.photo.isEmpty()){
		        	imgLoader.DisplayImage(EquipementItemAdapter.EQUIPMENT_PHOTO_URL+equipment.photo, R.drawable.ic_action_refresh, img);
		        }
	       
	     }
		
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
            	Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
              intent.putExtra("eIndex", eIndex);
              intent.putExtra("pIndex", pIndex);
              startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    @Override
//    public void onBackPressed() {
//    	 Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
//         intent.putExtra("eIndex", eIndex);
//         intent.putExtra("pIndex", pIndex);
//         startActivity(intent);
//    }


   
}
