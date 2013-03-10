package com.scotttwiname.woodhill.mtb.trails;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper{

	//Trails Table
	public static final String TABLE_TRAIL = "trails";
	public static final String TRAIL_COLUMN_ID = "_id";
	public static final String TRAIL_COLUMN_NAME = "name";
	public static final String TRAIL_COLUMN_DIST = "dist";
	public static final String TRAIL_COLUMN_DIFF = "diff";
	public static final String TRAIL_COLUMN_JUMPERS = "jumpers";
	public static final String TRAIL_COLUMN_FILE = "file";
	
	//Routes Table
	public static final String TABLE_ROUTE = "routes";
	public static final String ROUTE_COLUMN_ID = "_id";
	public static final String ROUTE_COLUMN_NAME = "name";
	public static final String ROUTE_COLUMN_DIST = "dist";
	public static final String ROUTE_COLUMN_DIFF = "diff";
	
	//Join Table
	public static final String TABLE_JOIN = "joiner";
	public static final String JOIN_COLUMN_TRAIL = "trail";
	public static final String JOIN_COLUMN_ROUTE = "route";
	
	private static final String DATABASE_NAME = "woodhill.db";
	public static final String DATABASE_PATH = "/data/data/com.scotttwiname.woodhill.mtb.trails/databases/";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase db;
	private Context context;

	//Adds database from assets if it doesn't exist
	public dbHelper(Context context){
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		if(checkDB())
			openDB();
		else{
			try{
				this.getReadableDatabase();
				copyDB();
				this.close();
				openDB();
			}
			catch(IOException e){
				throw new Error("Error copying DB");
			}
		}
	}
	
	private void copyDB() throws IOException{
	    InputStream myInput = context.getAssets().open(DATABASE_NAME);
	    String outFileName = DATABASE_PATH + DATABASE_NAME;
	    OutputStream myOutput = new FileOutputStream(outFileName);

	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = myInput.read(buffer))>0){
	        myOutput.write(buffer, 0, length);
	    }

	    myOutput.flush();
	    myOutput.close();
	    myInput.close();
	}
	
	public void openDB() throws SQLException {
	    String dbPath = DATABASE_PATH + DATABASE_NAME;
	    db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	//See if database exists
	private boolean checkDB(){
		SQLiteDatabase checkDB = null;
		boolean exist = false;
		try{
			String dbPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch(SQLiteException e){
			Log.v("db log", "database doesnt exist");
		}
		if(checkDB!=null){
			exist = false; //CHANGE TO TRUE FOR PUBLISHING
			checkDB.close();
		}
		return exist;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w(dbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_TRAIL + "," + TABLE_ROUTE + "," + TABLE_JOIN);
		onCreate(db);
	}


}
