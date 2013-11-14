package com.nctu.guideme;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Path_h_dataSource {
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns={SQLiteHelper.PATH_H_COLUMN_ID,
			SQLiteHelper.PATH_H_COLUMN_FILE_NAME};
	
	public Path_h_dataSource(Context context){
		dbHelper=new SQLiteHelper(context);
	}
	
	public void open()throws SQLException{
		database=dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Path_h createPath_h(String fileName){
		ContentValues values=new ContentValues();
		values.put(SQLiteHelper.PATH_H_COLUMN_FILE_NAME, fileName);
		long insertId=database.insert(SQLiteHelper.TABLE_PATH_H, null, values);
		Cursor cursor=database.query(SQLiteHelper.TABLE_PATH_H, allColumns, SQLiteHelper.PATH_H_COLUMN_ID+"="+insertId, null, null, null, null);
		cursor.moveToFirst();
		Path_h newPath_h=cursorToPath_h(cursor);
		cursor.close();
		return newPath_h;
	}
	
	public void deletePath_h(Path_h path_h){
		long id=path_h.getId();
		System.out.println("Comment deleted with id: "+id);
		database.delete(SQLiteHelper.TABLE_PATH_H, SQLiteHelper.PATH_H_COLUMN_ID+"="+id, null);
	}
	
	public List<Path_h> getAllPath_h(){
		List<Path_h> paths_h=new ArrayList<Path_h>();
		Cursor cursor=database.query(SQLiteHelper.TABLE_PATH_H, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Path_h path_h=cursorToPath_h(cursor);
			paths_h.add(path_h);
			cursor.moveToNext();
		}
		cursor.close();
		return paths_h;
	}
	
	private Path_h cursorToPath_h(Cursor cursor){
		Path_h path_h=new Path_h();
		path_h.setId(cursor.getLong(0));
		path_h.setFileName(cursor.getString(1));
		return path_h;
	}
}
