package com.scotttwiname.woodhill.mtb.trails;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class trailAdapter extends ArrayAdapter<Trail>{
	ArrayList<Trail> checkedTrails;
	
	public trailAdapter(Context context, int textViewResourceId, ArrayList<Trail> trails, ArrayList<Trail> checkedTrails) {
		super(context, textViewResourceId, trails);
		this.checkedTrails = checkedTrails;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		
		if (v == null){
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item, null);
		}
		Trail t = this.getItem(position);
		TextView trailname = (TextView)v.findViewById(R.id.trailname);
		TextView details = (TextView)v.findViewById(R.id.details);
		CheckBox trailCheck = (CheckBox)v.findViewById(R.id.checkBox1);
		
		//Sets init values to nothing
		trailname.setText("");
		details.setText("");
		trailCheck.setChecked(false);
		if(t != null){
			
			String jumpers = "JUMPERS ONLY";
			if(trailname != null){
				trailname.setText(t.getName());
			}
			if(details != null){
				if(t.getJumpers() == 1){
					details.setText(t.getDist() + "km - " + t.getDiff() + " - " + jumpers);
				}
				else details.setText(t.getDist() + "km - " + t.getDiff());
			}
			if(checkedTrails != null && checkedTrails.size() > 0){
				for(Iterator<Trail> i = checkedTrails.iterator(); i.hasNext();){
					Trail tIterator = i.next();
					if(tIterator.getId() == t.getId()){
						trailCheck.setChecked(true);
					}
				}
			}
		}
		return v;
	}
	public Trail getSelected(int position){
		return this.getItem(position);
	}
}
