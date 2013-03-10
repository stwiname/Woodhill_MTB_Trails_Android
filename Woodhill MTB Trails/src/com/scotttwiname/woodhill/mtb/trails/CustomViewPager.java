package com.scotttwiname.woodhill.mtb.trails;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomViewPager extends ViewPager{

	public CustomViewPager(Context context) {
		super(context);
	}
	public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev){
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y){
		//Disable scrolling on map view
		if(getCurrentItem() == 2){
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

}
