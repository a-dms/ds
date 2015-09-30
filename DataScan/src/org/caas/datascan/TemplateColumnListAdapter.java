package org.caas.datascan;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TemplateColumnListAdapter extends BaseAdapter {

	private Activity mActivity;

	private List<String> mItemList = null;



	public TemplateColumnListAdapter(Activity activity, List<String> list) {
		this.mActivity = activity;
		this.mItemList = list;
	}

	@Override
	public int getCount() {
		if (mItemList == null) {
			return 0;
		}
		return Global.originalFieldCount;// mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mItemList == null) {
			return null;
		} else {
			return mItemList.get(position);
		}
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	private int checkedCount = 0;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mItemList == null || getCount() == 0) {
			return null;
		}

		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.template_column_listitem, null);
		}
		
		TextView text = (TextView) view.findViewById(R.id.txtColumnName);
		text.setText(mItemList.get(position));

		final int pos = position;
		final ViewGroup parantView = parent;
		
		RadioButton radio = (RadioButton) view.findViewById(R.id.rdoColumnScan);
		if (position != Global.scanFieldIndex) {
			radio.setChecked(false);
		} else {
			radio.setChecked(true);
		}
		radio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Global.scanFieldIndex = pos;
				for (int i = 0; i < parantView.getChildCount(); i++) {
					RadioButton radio = (RadioButton) ((ViewGroup) parantView.getChildAt(i)).getChildAt(1);
					radio.setChecked(false);
				}
				((RadioButton) v).setChecked(true);
			}
		});

		CheckBox check = (CheckBox) view.findViewById(R.id.chkColumnChoose);
		check.setChecked(Global.displayFields[pos]);
		if (Global.displayFields[pos]) {
			checkedCount++;
		}
		check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked && checkedCount >= 2) {
					buttonView.setChecked(false);
					Toast.makeText(TemplateColumnListAdapter.this.mActivity, "最多选择2列显示", Toast.LENGTH_SHORT).show();
					return;
				}
				buttonView.setChecked(isChecked);
				Global.displayFields[pos] = isChecked;
				if (isChecked) {
					checkedCount++;
				} else {
					checkedCount--;
				}
			}
		});
		return view;
	}

}
