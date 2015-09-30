package org.caas.datascan;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TemplateActivity extends Activity {

	private File[] dataFiles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.template_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.template_titlebar);
		
		reload();
	}

	private void reload() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// not mounted
			System.out.println("sdcard not mounted");
			return;
		}
		File sdcardRoot = Environment.getExternalStorageDirectory();
		File dataPath = new File(sdcardRoot.getAbsolutePath() + "/data/datascan");
		dataPath.mkdirs();
		dataFiles = dataPath.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".csv") || filename.endsWith(".CSV") || filename.endsWith(".txt") || filename.endsWith(".TXT");
			}
		});
		String[] fileNames = new String[dataFiles.length];
		for (int i = 0; i < dataFiles.length; i++) {
			fileNames[i] = dataFiles[i].getName();
		}
		
		ListView lv = (ListView)findViewById(R.id.listViewTemplate);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, fileNames));
		lv.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				Global.templateFile = TemplateActivity.this.dataFiles[arg2];
				Intent intent =  new Intent(TemplateActivity.this, TemplateColumnActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public void clickRefresh(View v) {
		reload();
		Toast.makeText(this, "列表已刷新", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template, menu);
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
