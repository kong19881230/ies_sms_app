package com.example.ies_sms_demo;

import java.util.Locale;

import com.example.ies_sms_demo.R;
import com.example.ies_sms_demo.R.drawable;
import com.example.ies_sms_demo.R.id;
import com.example.ies_sms_demo.R.layout;
import com.example.ies_sms_demo.R.string;
import com.example.ies_sms_demo.downloader.ImageLoader;
import com.example.ies_sms_demo.model.Equipment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


public class EquipementInfoAcitivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
	public int pIndex;
	public int eIndex;
	public Equipment equipment;
	public SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(
        		   new ColorDrawable(getResources().getColor(android.R.color.transparent)));    
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBar().setDisplayHomeAsUpEnabled(true);
     

        eIndex=getIntent().getIntExtra("eIndex", 0);
	    pIndex=getIntent().getIntExtra("pIndex", 0);
	    sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        equipment= (Equipment) getIntent().getSerializableExtra("Equipement");
//        setContentView(R.layout.machine_info);
        setTitle(equipment.refNo);
	   
        
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab().setIcon(mSectionsPagerAdapter.getPageIcon(i))
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
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

            case R.id.action_logout:
            	Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
		    	SharedPreferences sharedPref = getSharedPreferences("share_data",Context.MODE_PRIVATE);
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putString(getString(R.string.user_name), "");
		    	editor.putString(getString(R.string.project_list), "");
		    	ImageLoader image=new ImageLoader(getApplicationContext());
		    	image.clearCache();
		    	editor.commit();
		        startActivity(intent1);
                return true;
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
  @Override
  public void onBackPressed() {
  	 Intent intent = new Intent(getApplicationContext(), EquipementSlideActivity.class);
       intent.putExtra("eIndex", eIndex);
       intent.putExtra("pIndex", pIndex);
       startActivity(intent);
  }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	public Context context;
        public SectionsPagerAdapter(FragmentManager fm,Context context) {
           
        	super(fm);
        	this.context=context;
        }
      
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        	 switch (position) {
            
             case 0:
             
            	 EGalleryFragment fragment1 = new EGalleryFragment(context,equipment);
                 Bundle args1 = new Bundle();
                 args1.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position+1);
                 fragment1.setArguments(args1);
                 return (Fragment)(fragment1) ;
             case 1:
            
            	 EInfoFragment fragment2 = new EInfoFragment(context,equipment);
                 Bundle args2 = new Bundle();
                 args2.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position+1);
                 fragment2.setArguments(args2);
                 return (Fragment)(fragment2) ;
             case 2:
            	 ENoteFragment fragment3 = new ENoteFragment(context,equipment);
                 Bundle args3 = new Bundle();
                 args3.putInt(PlaceholderFragment.ARG_SECTION_NUMBER, position+1);
                 fragment3.setArguments(args3);
                 return (Fragment)(fragment3) ;
         }
        	 return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

       
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
              
                case 0:
                    return getString(R.string.etitle_1).toUpperCase(l);
                case 1:
                    return getString(R.string.etitle_2).toUpperCase(l);
                case 2:
                    return getString(R.string.etitle_3).toUpperCase(l);
            }
            return null;
        }
        
     
        public int getPageIcon(int position) {
           
            switch (position) {
           
                case 0:
                    return R.drawable.ic_action_picture;
                case 1:
                    return R.drawable.ic_action_view_as_list;
                case 2:
                    return R.drawable.ic_action_edit;
            }
            return R.drawable.ic_action_phone;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
