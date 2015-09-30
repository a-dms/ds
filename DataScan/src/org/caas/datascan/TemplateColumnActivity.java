package org.caas.datascan;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class TemplateColumnActivity extends Activity {
	
	TemplateColumnListAdapter columnAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.template_column_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.template_column_titlebar);

		ListView lv = (ListView)findViewById(R.id.listViewTemplateColumn);
		
		Global.readTemplateFile();
		Global.getSettingsFromDB();
		
		columnAdapter = new TemplateColumnListAdapter(this, Global.templateColumnNames);
		lv.setAdapter(columnAdapter);
	}

	public void clickSave(View v) {
		SQLiteDatabase db = Global.getDB();
		char [] a = new char[Global.displayFields.length];
		for (int i = 0; i < a.length; i++) {
			a[i] = Global.displayFields[i] ? 'T' : 'F';
		}
		ContentValues cv = new ContentValues();
		cv.put("scan_col_index", Global.scanFieldIndex);
		cv.put("disp_cols", String.valueOf(a));
		db.update("t_file", cv, "file_name = ?", new String[]{Global.templateFile.getName()});
		
		Global.insertOrUpdateParam("currentTemplateFile", Global.templateFile.getAbsolutePath());
		
		Intent intent = new Intent(this, HomeInputActivity.class);
		startActivity(intent);
	}
	
	public void clickChooseTemplate(View v) {
		Intent intent = new Intent(this, TemplateActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_column, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
