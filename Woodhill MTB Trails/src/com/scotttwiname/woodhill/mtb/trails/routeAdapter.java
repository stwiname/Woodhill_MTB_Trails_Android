package com.scotttwiname.woodhill.mtb.trails;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class routeAdapter extends ArrayAdapter<Trail>{
	private TypedArray imageNames;
	int selectedRoute = -1;
	public routeAdapter(Context context, int textViewResourceId ,ArrayList<Trail> routes) {
		super(context, textViewResourceId, routes);
		imageNames = context.getResources().obtainTypedArray(R.array.route_icons);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		if (v == null){
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.route_list_item, null);
		}
		Trail t = this.getItem(position);
		if(t != null){
			ImageView icons = (ImageView)v.findViewById(R.id.icon);
			TextView details = (TextView)v.findViewById(R.id.details);
			if(icons != null){
				icons.setImageDrawable(imageNames.getDrawable(position));
			}
			if(details != null){
				details.setText(t.getDist() + "km - " + t.getDiff());
			}
		}
		RadioButton radio = (RadioButton)v.findViewById(R.id.radioButton1);
		if(position == selectedRoute){
			radio.setChecked(true);
		}
		else radio.setChecked(false);
		return v;
	}
	public void setPositionSelected(int position){
		selectedRoute = position;
	}
	public long getRouteSelected(){
		Trail t = this.getItem(selectedRoute);
		return t.getId();
	}

}