package com.scotttwiname.woodhill.mtb.trails;

import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class About extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		FragmentManager fragmentManager = getFragmentManager();
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case android.R.id.home:
    		Intent intent = new Intent(this, MainActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(intent);
    		finish();
    		return true;
    	default:
            return super.onOptionsItemSelected(item);
    	}
    }
	
	public class frag extends Fragment{
		PackageInfo pInfo = null;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
		        Bundle savedInstanceState){
			View v = inflater.inflate(R.layout.activity_about, null);
			TextView version = (TextView) v.findViewById(R.id.version);
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			version.setText(pInfo.versionCode);
			EditText mapsInfo = (EditText)findViewById(R.id.mapsInfo);
			mapsInfo.setEnabled(false);
			mapsInfo.setText("i hope this works");
			return v;
		}
	}
}
