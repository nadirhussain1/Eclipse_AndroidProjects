package br.com.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.data.model.DinamoObject;
import br.com.dinamo.R;
import br.com.global.DinamoConstants;
import br.com.global.SharedData;
import br.com.utilities.ScalingUtility;

public class DropDownAdapter extends BaseAdapter{
	ArrayList<DinamoObject> list=null;
	TextView selectedText=null;


	public DropDownAdapter(ArrayList<DinamoObject> names){
		list=names;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)SharedData.getInstance().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drop_down_row, null, false);
			ScalingUtility.getInstance().scaleView(convertView);
		}
		TextView unitNameTextView=(TextView)convertView.findViewById(R.id.dropdownRowTextView);
		SharedData.getInstance().applyFontToTextView(unitNameTextView, DinamoConstants.HELVETICA_NEUE_CONDENSED);
		unitNameTextView.setText(list.get(position).getName());

		convertView.setTag(position);
		return convertView;
	}

}
