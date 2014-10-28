package com.example.ies_sms_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.Machine;


public class MachineInfoActivity extends ActionBarActivity {
	
	public final String TAG_EQUIPEMENTS="equipments";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Equipment equipment= (Equipment) getIntent().getSerializableExtra("Equipement");
        Machine machine=equipment.machine;
        setContentView(R.layout.machine_info);
        setTitle(machine.modelId);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView img = (ImageView) findViewById(R.id.photo);
	   
        TextView type = (TextView) findViewById(R.id.type);
		TextView material = (TextView) findViewById(R.id.material);
		TextView capacity = (TextView) findViewById(R.id.capacity);
		TextView pump = (TextView) findViewById(R.id.pump);
		TextView heat_exchanger = (TextView) findViewById(R.id.heat_exchanger);
		TextView back_up_heater = (TextView) findViewById(R.id.back_up_heater);
		TextView insulation = (TextView) findViewById(R.id.insulation);
		TextView pressure = (TextView) findViewById(R.id.pressure);
		// check to see if each individual textview is null.
		// if not, assign some text!
		if (type != null){
			type.setText(machine.type);
		}
	    if(img!=null){
		      
	        img.setImageDrawable(getResources().getDrawable(
					machine.photoId));
	       }
		if (material != null){
			material.setText(machine.material);
		}
		if (capacity != null){
			capacity.setText(machine.capacity);
		}
		if (pump != null){
			pump.setText(machine.pump);
		}
		if (heat_exchanger != null){
			heat_exchanger.setText(machine.heatExchanger);
		}
		if (back_up_heater != null){
			back_up_heater.setText(machine.backUpHeater);
		}
		if (insulation != null){
			insulation.setText(machine.insulation);
		}
		if (pressure != null){
			pressure.setText(machine.pressure);
		}
    }

   


   
}
