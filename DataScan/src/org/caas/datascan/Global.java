package org.caas.datascan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Global {
	public static File templateFile = null;
	
	public static List<String> templateColumnNames;
	
	// 每行数据存成一个list。每个field都为String类型
	public static List<List<String>> csvData;
	public static int scanRecordIndex = -1;
	
	public static int scanFieldIndex = -1;
	
	public static int originalFieldCount = -1;
	
	public static boolean[] displayFields = null;

	public static List<String> getSelectedRecord() {
		return csvData.get(scanRecordIndex);
	}
	
	public static Sqlite dbconn;
	
	public static SQLiteDatabase getDB() {
		return dbconn.getDB();
	}
	
	public static String getParam(String paramName) {
		SQLiteDatabase db = getDB();
		String s = null;
		Cursor c = db.rawQuery("select param_value FROM t_config WHERE param_name= ? ", new String[]{paramName});
		if (c.moveToNext()) {
			s = c.getString(0);
		}
		c.close();
		return s;
	}
	
	public static void insertOrUpdateParam(String paramName, String paramValue) {
		SQLiteDatabase db = getDB();
		boolean found = false;
		Cursor c = db.rawQuery("select count(*) FROM t_config WHERE param_name= ? ", new String[]{paramName});
		if (c.moveToNext()) {
			found = (c.getInt(0) > 0);
		}
		c.close();
		
		///////////////////
		/*c = db.rawQuery("select * FROM t_config ", new String[]{});
		while (c.moveToNext()) {
			System.out.println(c.getString(0) + ":" + c.getString(1));
		}
		c.close();*/
		//////////////
		 
		if (found) {
			ContentValues cv = new ContentValues();
			cv.put("param_value", paramValue);
			db.update("t_config", cv, "param_name = ?", new String[]{paramName});
		} else {
			db.execSQL("insert into t_config values (?, ? )", new String[]{paramName, paramValue});
		}
	}
	
	public static boolean getCurrentTemplateFileFromSqlite() {
		String fileName = getParam("currentTemplateFile");
		if (fileName == null) {
			return false;
		}
		File file = new File(fileName);
		if (file.exists()) {
			templateFile = file;
		}
		return templateFile != null;
	}
	
	public static void getSettingsFromDB() {
		SQLiteDatabase db = getDB();
		
		Cursor c = db.rawQuery("select * from t_file where file_name = ?", new String[]{templateFile.getName()});
		if (c.moveToNext()) {
			scanFieldIndex = c.getInt(3);
			String displayItem = c.getString(4);
			originalFieldCount = c.getInt(2);
			for (int i = 0; i < displayItem.length(); i++) {
				displayFields[i] = (displayItem.charAt(i) == 'T');				
			}
		} else {
			char [] a = new char[templateColumnNames.size()];
			originalFieldCount = templateColumnNames.size();
			Arrays.fill(a, 'F');
			db.execSQL("INSERT INTO t_file VALUES (?, ?, ?, 0, ?)", new Object[]{templateFile.getName(), templateFile.lastModified(), originalFieldCount, String.valueOf(a)});  
		}
		c.close();
		
	}
	
	public static void readTemplateFile() {
		if (templateFile == null) {
			return;
		}
		BufferedReader br = null;
		try {
			templateColumnNames = new ArrayList<String>();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
			String str = br.readLine();
			String[] strCols = csvSplit(str);
			for (int i = 0; i < strCols.length; i++) {
				templateColumnNames.add(strCols[i]);
			}
			
			displayFields = new boolean[templateColumnNames.size()];
			scanFieldIndex = 0;
			
			csvData = new ArrayList<List<String>>();
			while ((str = br.readLine()) != null) {
				String[] strVals = csvSplit(str); 
				List<String> vals = new ArrayList<String>();
				for (int i = 0; i < templateColumnNames.size(); i++) {
					if (i <  strVals.length) {
						vals.add(strVals[i]);
					} else {
						vals.add("");
					}
				}

				csvData.add(vals);
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String [] csvSplit(String line) {
		return line.split(",");
	}
	
	public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
}
