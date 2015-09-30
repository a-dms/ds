package org.caas.datascan;

import java.text.ParseException;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		Global.dbconn = new Sqlite(getBaseContext());
		
		if (expires()) {
			Intent intent = new Intent(this, ExpireActivity.class) ;
			startActivity(intent);
			finish();
			return;
		}
		
		if (Global.getCurrentTemplateFileFromSqlite()) {
			Global.readTemplateFile();
			Global.getSettingsFromDB();
			Intent intent = new Intent(this, HomeInputActivity.class) ;
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, TemplateActivity.class);
			startActivity(intent);
		}
		
	}

	private boolean expires() {
		try {
			Date date = Global.dateFormat.parse("2015-12-31 23:59:59");
			return System.currentTimeMillis() > date.getTime(); 	
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void clickChooseTemplate(View v){
		Intent intent = new Intent(this, TemplateActivity.class);
		startActivity(intent);
	}
}
