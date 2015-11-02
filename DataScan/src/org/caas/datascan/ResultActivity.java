package org.caas.datascan;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ResultActivity extends Activity {

	EditText edtData = null;
	EditText edtComment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.result_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.result_titlebar);
		
		edtData = (EditText) findViewById(R.id.edtData);
		edtComment = (EditText) findViewById(R.id.edtComment);
		
		List<Map<String, String>> cols = new ArrayList<Map<String,String>>();
		
		Map<String, String> scanMap = new HashMap<String, String>();
		scanMap.put("columnName", Global.templateColumnNames.get(Global.scanFieldIndex));
		scanMap.put("columnValue", Global.getSelectedRecord().get(Global.scanFieldIndex));
		cols.add(scanMap);
		
		for (int i = 0; i < Global.displayFields.length; i++) {
			if (Global.displayFields[i]) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("columnName", Global.templateColumnNames.get(i));
				map.put("columnValue", Global.getSelectedRecord().get(i));
				cols.add(map);
			}
		}
		
		ListView view = (ListView) findViewById(R.id.listViewColumnData);
		SimpleAdapter adapter = new SimpleAdapter(this, cols, R.layout.result_listitem, new String[]{"columnName", "columnValue"}, new int[]{R.id.txtColumnName, R.id.txtColumnValue});
		view.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
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
	
	public void clickBack(View v) {
		finish();
	}
	
	public void clickSave(View v)  {
		try {
			String data = edtData.getText().toString();
			String comment = edtComment.getText().toString();
			List<String> selectedRecord = Global.getSelectedRecord();
			
			if (selectedRecord.size() <= Global.originalFieldCount) {
				selectedRecord.add(Global.dateFormat.format(new Date()));
				selectedRecord.add("1");
				selectedRecord.add(data);
				selectedRecord.add(comment);
			} else {
				selectedRecord.set(selectedRecord.size() - 4, Global.dateFormat.format(new Date()));
				try {
					selectedRecord.set(selectedRecord.size() - 3, "" + (Integer.parseInt(selectedRecord.get(selectedRecord.size() - 3)) + 1));
				} catch (NumberFormatException e) {
					selectedRecord.set(selectedRecord.size() - 3, "1");
				}
				selectedRecord.set(selectedRecord.size() - 2, data);
				selectedRecord.set(selectedRecord.size() - 1, comment);
			}
			
			if (Global.templateColumnNames.size() <= Global.originalFieldCount) {
				Global.templateColumnNames.add(Global.ADD_DATE);
				Global.templateColumnNames.add(Global.ADD_COUNT);
				Global.templateColumnNames.add(Global.ADD_DATA);
				Global.templateColumnNames.add(Global.ADD_COMMENT);
			}
			
			Writer writer = new OutputStreamWriter(new FileOutputStream(Global.templateFile));
			for (int i = 0; i < Global.templateColumnNames.size(); i++) {
				writer.write(Global.templateColumnNames.get(i));
				if (i < Global.templateColumnNames.size() - 1) {
					writer.write(",");
				} else {
					writer.write("\n");
				}
			}
			for (int i = 0; i < Global.csvData.size(); i++) {
				List<String> rec = Global.csvData.get(i);
				for (int j = 0; j < rec.size(); j++) {
					writer.write(rec.get(j));
					if (j < rec.size() - 1) {
						writer.write(",");
					} else {
						writer.write("\n");
					}
				}
			}
			writer.close();
	
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					finish();
				}
			}, Toast.LENGTH_SHORT);
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
		}
	}
}
