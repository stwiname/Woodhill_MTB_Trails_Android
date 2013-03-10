package com.scotttwiname.woodhill.mtb.trails;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class databaseIO {
	
	private SQLiteDatabase database;
	private dbHelper mydbHelper;
	private String[] allTrailColumns = { dbHelper.TRAIL_COLUMN_ID, dbHelper.TRAIL_COLUMN_NAME, dbHelper.TRAIL_COLUMN_DIST,
			dbHelper.TRAIL_COLUMN_DIFF, dbHelper.TRAIL_COLUMN_JUMPERS, dbHelper.TRAIL_COLUMN_FILE };
	private String[] allRouteColumns = { dbHelper.ROUTE_COLUMN_ID, dbHelper.ROUTE_COLUMN_NAME, dbHelper.ROUTE_COLUMN_DIST,
			dbHelper.ROUTE_COLUMN_DIFF };
	
	public databaseIO(Context context){
		mydbHelper = new dbHelper(context);		
	}
	
	public void open() throws SQLException{
		database = mydbHelper.getReadableDatabase();
	}
	
	public void close(){
		database.close();
	}
	
	public ArrayList<Trail> getAllTrails(){
		ArrayList<Trail> Trails = new ArrayList<Trail>();
		Cursor cursor = database.query(dbHelper.TABLE_TRAIL, allTrailColumns, null, null, null, null, dbHelper.TRAIL_COLUMN_NAME);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Trail t = cursorToTrail(cursor);
			
			Trails.add(t);
			cursor.moveToNext();
		}
		cursor.close();
		return Trails;
	}
	
	public ArrayList<Trail> getAllRoutes(){
		ArrayList<Trail> routes = new ArrayList<Trail>();
		Cursor cursor = database.query(dbHelper.TABLE_ROUTE, allRouteColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Trail t = cursorToRoute(cursor);
			routes.add(t);
			cursor.moveToNext();
		}
		cursor.close();			
		return routes;
		
	}
	
	public ArrayList<Trail> getAllRouteTrails(long id){
		ArrayList<Trail> trails = new ArrayList<Trail>();
		String query = "SELECT t._id, t.name, t.dist, t.diff, t.jumpers FROM trails t, joiner j WHERE j.route = " +
				id + " AND j.trail = t._id ";
		Log.v("All routes query", query);
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Trail newTrail = cursorToTrail(cursor);
			trails.add(newTrail);
			cursor.moveToNext();			
		}
		cursor.close();
		return trails;
	}

	
	public Trail cursorToTrail(Cursor cursor){
		Trail t = new Trail();
		t.setID(cursor.getLong(0));
		t.setName(cursor.getString(1));
		t.setDist(cursor.getDouble(2));
		t.setDiff(cursor.getInt(3));
		t.setJumpers(cursor.getInt(4));
		if(!cursor.isNull(5)){
			t.setFile(cursor.getString(5));
		}
		else{
			t.setFile("");
		}
		return t;
	}
	public Trail cursorToRoute(Cursor cursor){
		Trail t = new Trail();
		t.setID(cursor.getLong(0));
		t.setName(cursor.getString(1));
		t.setDist(cursor.getDouble(2));
		t.setDiff(cursor.getInt(3));
		t.setJumpers(0);
		t.setFile("");
		return t;
	}
	
}
