package org.caas.datascan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeInputActivity extends Activity {

	private EditText edtScanColumn;
	
	private TextView txtTemplateName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home_input_activity);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.home_input_titlebar);
		
		edtScanColumn = (EditText) findViewById(R.id.edtScanColumn);
		txtTemplateName = (TextView) findViewById(R.id.txtTemplateName);
		txtTemplateName.setText("当前模板: " + Global.templateFile.getName());
	}

	public void clickChooseTemplate(View v) {
		Intent intent = new Intent(this, TemplateActivity.class);
		startActivity(intent);
	}
	
	public void clickFindInTemplate(View v) {
		if (edtScanColumn.getText() == null) {
			Toast.makeText(this, "请输入", Toast.LENGTH_SHORT).show();
			return;
		}
		String s = edtScanColumn.getText().toString();
		boolean found = false;
			// search in file
		for (int i = 0; i < Global.csvData.size(); i++) {
			if (Global.csvData.get(i).get(Global.scanFieldIndex).equals(s)) {
				Global.scanRecordIndex = i;
				found = true;
				break;
			}
		}
		if (found) {
			// goto and display
			Intent intent = new Intent(this, ResultActivity.class);
			startActivity(intent);
			//Toast.makeText(this, "找到啦", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "没有找到", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void clickScan(View v) {
		Intent intent = new Intent(this, CaptureActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_input, menu);
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
