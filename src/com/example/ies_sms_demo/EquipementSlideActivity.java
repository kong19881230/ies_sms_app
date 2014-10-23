/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ies_sms_demo;


import java.util.ArrayList;

import com.example.ies_sms_demo.model.Equipment;
import com.example.ies_sms_demo.model.EquipmentHelper;
import com.example.ies_sms_demo.model.Project;
import com.example.ies_sms_demo.observe.SettingActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see EquipementStateFragment
 */
public class EquipementSlideActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */public SharedPreferences sharedPref;
    private ViewPager mPager;
    private ImageView next;
    private ImageView previous;
    private ArrayList<Equipment> equipements = new ArrayList<Equipment>();
    private ArrayList<Project> projects=new ArrayList<Project>();
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipement_slide);
        sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
        String projectListStr = sharedPref.getString(getString(R.string.project_list), "");
        projects= EquipmentHelper.getProjectList(projectListStr);
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(projects.size()>0){
	        setTitle(projects.get(0).name_en);
	        equipements=(ArrayList<Equipment>) projects.get(0).equipments;
       
	        int index=getIntent().getIntExtra("index", 0);
	        // Instantiate a ViewPager and a PagerAdapter.
	        setTitle(equipements.get(index).refNo);
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        mPager = (ViewPager) findViewById(R.id.pager);
	        next=(ImageView)findViewById(R.id.next);
	        previous=(ImageView)findViewById(R.id.previous);
	        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
	        if(previous!=null){
	        	previous.setVisibility(getVisibility(index > 0));
	        	previous.setOnClickListener(new OnClickListener() {
	
	     			@Override
	     			public void onClick(View v) {
	     				 mPager.setCurrentItem(mPager.getCurrentItem() - 1);
	     			}
	        	});
	        }
	        if(next!=null){
	        	next.setVisibility(getVisibility(index < mPagerAdapter.getCount() - 1));
	        	next.setOnClickListener(new OnClickListener() {
	
	     			@Override
	     			public void onClick(View v) {
	     				 mPager.setCurrentItem(mPager.getCurrentItem() + 1);
	     				
	     			}
	        	});
	        }
	        mPager.setAdapter(mPagerAdapter);
	        mPager.setCurrentItem(index);
	        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	            @Override
	            public void onPageSelected(int position) {
	                // When changing pages, reset the action bar actions since they are dependent
	                // on which page is currently active. An alternative approach is to have each
	                // fragment expose actions itself (rather than the activity exposing actions),
	                // but for simplicity, the activity provides the actions in this sample.
	            	setTitle(equipements.get(position).refNo);
	            	if(previous!=null)previous.setVisibility(getVisibility(mPager.getCurrentItem() > 0));
	            	if(next!=null)next.setVisibility(getVisibility(mPager.getCurrentItem() < mPagerAdapter.getCount() - 1));
	                invalidateOptionsMenu();
	            }
	        });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_setting:
            	Intent intent = new Intent(getApplicationContext(), SettingActivity.class);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
public int getVisibility(boolean visible){
	if(visible){
		return View.VISIBLE;
	}else{
		return View.GONE;
	}
}

    /**
     * A simple pager adapter that represents 5 {@link EquipementStateFragment} objects, in
     * sequence.
     */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return EquipementStateFragment.create(position,getApplicationContext(),equipements.get(position));
    }

    @Override
    public int getCount() {
        return equipements.size();
    }
}
}
