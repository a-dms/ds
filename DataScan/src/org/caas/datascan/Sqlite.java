package org.caas.datascan;

import java.io.File;
import java.text.ParseException;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class Sqlite {
	private Context context;
	private SQLiteDatabase db;
	
	public SQLiteDatabase getDB() {
		if (db == null) {
			db = openDatabase();
		}
		return db;
	}

	public Sqlite(Context context) {
		this.context = context;
	}
	
	public SQLiteDatabase openDatabase() {
		db = context.openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);  
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdcardRoot = Environment.getExternalStorageDirectory();
			File dataPath = new File(sdcardRoot.getAbsolutePath() + "/data/datascan/RESET");
			if (dataPath.exists()) {
				clearDB4test();
			}
		}
		Cursor c = db.rawQuery("select count(*) FROM sqlite_master WHERE type='table' AND name='t_file'", new String[]{});
		if (c.moveToNext()) {
			if (c.getInt(0) == 0) {
				db.execSQL("create table t_file(file_name varchar, last_modified varchar, original_col_count int, scan_col_index int, disp_cols varchar)");
			}
		}
		c.close();
		
		c = db.rawQuery("select count(*) FROM sqlite_master WHERE type='table' AND name='t_config'", new String[]{});
		if (c.moveToNext()) {
			if (c.getInt(0) == 0) {
				db.execSQL("create table t_config(param_name varchar, param_value varchar)");
			}
		}
		c.close();
		
		return db;
	}
	
	public boolean getCurrentTemplateFileFromSqlite() {
		Cursor c = db.rawQuery("select param_value FROM t_config WHERE param_name= ? ", new String[]{"currentTemplateFile"});
		if (c.moveToNext()) {
			File file = new File(c.getString(0));
			if (file.exists()) {
				Global.templateFile = file;
			}
		}
		c.close();
		
		return Global.templateFile != null;
	}
	
	private void clearDB4test() {
		db.execSQL("drop table if exists t_file");
		db.execSQL("drop table if exists t_config");
	}

	public static String toString(Date date) {
		return Global.dateFormat.format(date);
	}
	
	public static Date toDate(String dateString) {
		try {
			return Global.dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
