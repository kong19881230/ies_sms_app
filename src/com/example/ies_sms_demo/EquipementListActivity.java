package com.example.ies_sms_demo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.ies_sms_demo.model.Equipement;
import com.example.ies_sms_demo.model.EquipementHelper;


public class EquipementListActivity extends ListActivity {
	
	
	private EquipementItemAdapter e_adapter;
	private ArrayList<Equipement> equipements = new ArrayList<Equipement>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        equipements= EquipementHelper.getEquipementList( getResources());
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Royal garden");
        setContentView(R.layout.equipements);
        getActionBar().setIcon(
        		   new ColorDrawable(getResources().getColor(android.R.color.transparent)));    
        e_adapter = new EquipementItemAdapter(this, R.id.text_view, equipements);
        setListAdapter(e_adapter);

    }
    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	
      
          Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
  
          intent.putExtra("index", position);
          startActivity(intent);
    }
   
 
   
}
