package com.scotttwiname.woodhill.mtb.trails;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.res.XmlResourceParser;

public class gpxReader {
	
	public gpxReader(){
		
	}
	public static PolylineOptions parse(XmlResourceParser in) throws XmlPullParserException, IOException{
		try{
			XmlPullParser parser = in;
			return readData(parser);
		}
		finally{
			in.close();
		}
	}
	private static PolylineOptions readData(XmlPullParser parser) throws XmlPullParserException, IOException{
		PolylineOptions points = new PolylineOptions();

		while (parser.next() != XmlPullParser.END_DOCUMENT){
			if(parser.getEventType() != XmlPullParser.START_TAG)
				continue;
			String name = parser.getName();
			if(name.equals("trkpt")){
				points.add(new LatLng(Double.parseDouble(parser.getAttributeValue(null, "lat")), 
						Double.parseDouble(parser.getAttributeValue(null, "lon"))));
			}
		}
		return points;
	}
}
