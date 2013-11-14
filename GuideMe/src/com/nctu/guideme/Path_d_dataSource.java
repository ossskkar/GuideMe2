package com.nctu.guideme;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Path_d_dataSource {

	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns={SQLiteHelper.PATH_D_COLUMN_ID,
			SQLiteHelper.PATH_D_COLUMN_PATH_H,
			SQLiteHelper.PATH_D_COLUMN_STEPS,
			SQLiteHelper.PATH_D_COLUMN_DIRECTION_X,
			SQLiteHelper.PATH_D_COLUMN_DIRECTION_Y,
			SQLiteHelper.PATH_D_COLUMN_DIRECTION_Z,};
	
	public Path_d_dataSource(Context context){
		dbHelper=new SQLiteHelper(context);
	}
	
	public void open()throws SQLException{
		database=dbHelper.getWritableDatabase();
	}
	
	public void close()throws SQLException{
		dbHelper.close();
	}
	
	public Path_d createPath_d(long path_h, 
			long steps,
			float directionX, 
			float directionY, 
			float directionZ){
		ContentValues values=new ContentValues();
		values.put(SQLiteHelper.PATH_D_COLUMN_PATH_H, path_h);
		values.put(SQLiteHelper.PATH_D_COLUMN_STEPS, steps);
		values.put(SQLiteHelper.PATH_D_COLUMN_DIRECTION_X, directionX);
		values.put(SQLiteHelper.PATH_D_COLUMN_DIRECTION_Y, directionY);
		values.put(SQLiteHelper.PATH_D_COLUMN_DIRECTION_Z, directionZ);
		
		long insertId=database.insert(SQLiteHelper.TABLE_PATH_D, null, values);
		Cursor cursor=database.query(SQLiteHelper.TABLE_PATH_D, 
				allColumns, 
				SQLiteHelper.PATH_D_COLUMN_ID+"="+insertId, 
				null, null, null, null);
		cursor.moveToFirst();
		Path_d newPath_d=cursorToPath_d(cursor);
		cursor.close();
		return newPath_d;
	}
	
	public void deletePath_d(Path_d path_d){
		long id=path_d.getId();
		System.out.println("path_d deleted with id: "+id);
		database.delete(SQLiteHelper.TABLE_PATH_D, 
				SQLiteHelper.PATH_D_COLUMN_ID+"="+id, null);
	}
	
	public List<Path_d> getAllPath_d(long path_h){
		List<Path_d> paths_d=new ArrayList<Path_d>();
		Cursor cursor=database.query(SQLiteHelper.TABLE_PATH_D, 
				allColumns, 
				SQLiteHelper.PATH_D_COLUMN_PATH_H+"="+path_h, 
				null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Path_d path_d=cursorToPath_d(cursor);
			paths_d.add(path_d);
			cursor.moveToNext();
		}
		cursor.close();
		return paths_d;
	}
	
	private Path_d cursorToPath_d(Cursor cursor){
		Path_d path_d=new Path_d();
		path_d.setId(cursor.getLong(0));
		path_d.setPath_h(cursor.getInt(1));
		path_d.setSteps(cursor.getInt(2));
		path_d.setDirectionX(cursor.getFloat(3));
		path_d.setDirectionY(cursor.getFloat(4));
		path_d.setDirectionZ(cursor.getFloat(5));
		return path_d;
	}
}
