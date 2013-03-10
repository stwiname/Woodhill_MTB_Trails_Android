package com.scotttwiname.woodhill.mtb.trails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
	

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    CustomViewPager mViewPager;
    private databaseIO datasource;
    private long routeID = -1L;
    private int routeElement = -1;
    private boolean update = false;
    public ArrayList<Trail> selectedTrails = new ArrayList<Trail>();
    public ArrayList<Trail> allTrails = new ArrayList<Trail>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Create database connection
        datasource = new databaseIO(this);
        datasource.open();
        allTrails = datasource.getAllTrails();
        
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding tab.
        // We can also use ActionBar.Tab#select() to do this if we have a reference to the
        // Tab.
        mViewPager.setOnPageChangeListener(new CustomViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case R.id.about:
    		Intent intent = new Intent(this, About.class);
    		return true;
    	default:
            return super.onOptionsItemSelected(item);
    	}    	
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	datasource.close();
    }

    public ArrayList<Trail>  getRoutes(){
    	return datasource.getAllRoutes();
    }
    public ArrayList<Trail> getAllRouteTrails(long id){
    	return datasource.getAllRouteTrails(id);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
    public long getRoute(){
    	return routeID;
    }
    public void setRoute(long id){
    	routeID = id;
    }
    
    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            switch(i){
            case 0:
            	fragment = new RouteFragment();
            	break;
            case 1:
            	fragment = new TrailFragment();
            	break;
            case 2:
            	fragment = new MapsFragment();//If MapsFragment changes look in CustomViewPager.canScroll()
            	break;
            default:
            	fragment = new RouteFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_section1).toUpperCase();
                case 1: return getString(R.string.title_section2).toUpperCase();
                case 2: return getString(R.string.title_section3).toUpperCase();
            }
            return null;
        }
        
        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }
        
    }
    
    public static class RouteFragment extends ListFragment {
        View view;  
        private ArrayList<Trail> routeList = new ArrayList<Trail>();
        routeAdapter adapter;
        
        
        @Override
        public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
        	routeList = ((MainActivity)getActivity()).getRoutes();	
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.route_fragment, container, false);
            ListView routeView = (ListView) view.findViewById(android.R.id.list); 
            adapter = new routeAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, routeList);
            adapter.setPositionSelected(((MainActivity)getActivity()).routeElement);
            routeView.setAdapter(adapter);
            return view;
        }
        //Selected and deselect items and edit appropriate fragments
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            RadioButton routeSelected = (RadioButton)v.findViewById(R.id.radioButton1);
            ((MainActivity)getActivity()).routeElement = position;
    		if(!routeSelected.isChecked()){
    			routeSelected.setChecked(true);
    			adapter.setPositionSelected(((MainActivity)getActivity()).routeElement);
    			((MainActivity) getActivity()).setRoute(adapter.getRouteSelected());
    			((MainActivity)getActivity()).selectedTrails = ((MainActivity)getActivity()).getAllRouteTrails(adapter.getRouteSelected());
    		}
    		mSectionsPagerAdapter.notifyDataSetChanged();
        }
    }

    public static class TrailFragment extends ListFragment {
    	View view;
        private ArrayList<Trail> trailList = new ArrayList<Trail>();
        private ArrayList<Trail> checkedTrails = new ArrayList<Trail>();
        private long routeId = -1L;
        trailAdapter adapter;
        
        @Override
        public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
        	//Get data for trail list
        	trailList = ((MainActivity)getActivity()).allTrails;  
        }
        
        @Override
        public void onAttach(Activity activity){
        	super.onAttach(activity);
        	routeId = ((MainActivity) getActivity()).getRoute();
        	if(routeId != -1L && !((MainActivity)getActivity()).update){
        		checkedTrails = ((MainActivity)getActivity()).getAllRouteTrails(routeId);
        		((MainActivity)getActivity()).selectedTrails = checkedTrails;
        	}
        	if(((MainActivity)getActivity()).update){
        		checkedTrails = ((MainActivity)getActivity()).selectedTrails;
        		((MainActivity)getActivity()).update = false;
        	}
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.trail_fragment, container, false);
            ListView trailView = (ListView) view.findViewById(android.R.id.list);
            adapter = new trailAdapter(getActivity(), android.R.layout.simple_list_item_1, trailList, checkedTrails);  
            trailView.setAdapter(adapter);
            return view;
        }
        
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            CheckBox trailCheck = (CheckBox)v.findViewById(R.id.checkBox1);
            ((MainActivity)getActivity()).routeElement = -1;
    		if(!trailCheck.isChecked()){
    			trailCheck.setChecked(true);
    			if(!((MainActivity)getActivity()).selectedTrails.contains(adapter.getSelected(position))){
    				((MainActivity)getActivity()).selectedTrails.add(adapter.getSelected(position));
    			}
    			Log.v("check", Integer.toString(((MainActivity)getActivity()).selectedTrails.size()));    			
    		}
    		else {    			
    			trailCheck.setChecked(false);
    			remove(adapter.getSelected(position));
    			Log.v("uncheck", Integer.toString(((MainActivity)getActivity()).selectedTrails.size()));
    		}
    		((MainActivity)getActivity()).update = true;
    		mSectionsPagerAdapter.notifyDataSetChanged();
        }
        
        public void remove(Trail t){
        	for(int i = 0; i < ((MainActivity)getActivity()).selectedTrails.size(); i ++){
        		if(t.getId() == ((MainActivity)getActivity()).selectedTrails.get(i).getId()){
        			((MainActivity)getActivity()).selectedTrails.remove(i);
        		}
        	}        		
        }
    }
    
    public static class MapsFragment extends SupportMapFragment{
    	private static final LatLng START_POINT = new LatLng(-36.74023,174.42255);
    	private static final int SELECTED_COLOUR = 0xaa3366cc;
    	private static final int UNSELECTED_COLOUR = 0x28000000;
    	GoogleMap map;
    	ArrayList<PolylineOptions> selectedTrailPoints;
    	ArrayList<PolylineOptions> unselectedTrailPoints;
    	ArrayList<String> selectedFileNames = new ArrayList<String>();
    	
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            UiSettings settings = getMap().getUiSettings();
            //settings.setAllGesturesEnabled(false);
            settings.setMyLocationButtonEnabled(true);
            settings.setZoomControlsEnabled(false);
            
            setUpMapIfNeeded();
            
            return view;
        }
        
        private void setUpMapIfNeeded() {
            // Do a null check to confirm that we have not already instantiated the map.
            if (map == null) {
                map = getMap();                
                // Check if we were successful in obtaining the map.
                if (map != null) {
                	map.moveCamera(CameraUpdateFactory.newLatLngZoom(START_POINT, 16));
                	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                	map.setMyLocationEnabled(true);
                	
                	try{
                    	populateTrailPoints();
                    }
                    catch(Exception e){}
                	drawTrails();
                }
            }
        }
        
        private void populateTrailPoints() throws NotFoundException, XmlPullParserException, IOException{
        	Resources res = this.getResources();
        	int resourceID;
        	selectedTrailPoints = new ArrayList<PolylineOptions>();
        	unselectedTrailPoints = new ArrayList<PolylineOptions>();
        	//get data for selected trails
        	for(int i = 0; i < ((MainActivity)getActivity()).selectedTrails.size(); i++){
        		if(((MainActivity)getActivity()).selectedTrails.get(i).getFile() != ""){
        			resourceID = getResourceString(((MainActivity)getActivity()).selectedTrails.get(i).getFile(), getActivity());
        			selectedTrailPoints.add(gpxReader.parse(res.getXml(resourceID)));
        			selectedFileNames.add(((MainActivity)getActivity()).selectedTrails.get(i).getFile());
        		}
        	}
        	//get data for all trails
        	for(int i = 0; i < ((MainActivity)getActivity()).allTrails.size(); i++){
        		if(((MainActivity)getActivity()).allTrails.get(i).getFile() != ""){        			
    				resourceID = getResourceString(((MainActivity)getActivity()).allTrails.get(i).getFile(), getActivity());
    				unselectedTrailPoints.add(gpxReader.parse(res.getXml(resourceID)));
        		}
        	}
        	
        }
        
        private void drawTrails(){
        	//Draw unselected Trails
        	for(int i = 0; i < unselectedTrailPoints.size(); i++){
        		map.addPolyline(unselectedTrailPoints.get(i).color(UNSELECTED_COLOUR));
        	}
        	//Draw selected Trails
        	for(int i = 0; i < selectedTrailPoints.size(); i++){
        		map.addPolyline(selectedTrailPoints.get(i).color(SELECTED_COLOUR));
        	}        	
        }
        //Get location data for a trail if it exists
        public static int getResourceString(String name, Context context) {
            int nameResourceID = context.getResources().getIdentifier(name, "xml", context.getApplicationInfo().packageName);
            if (nameResourceID == 0) {
                throw new IllegalArgumentException("No resource string found with name " + name);
            } else {
                return nameResourceID;
            }
        }
    }    
}
