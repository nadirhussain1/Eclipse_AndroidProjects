package com.mobive.alert_keywords;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.humby.dealular.R;
import com.mobive.dealme.Screen4_Activity;

public class KeywordItemAdapter extends ArrayAdapter<KeywordItem> {

	private Context context;
	private ArrayList<KeywordItem> items;
	private LayoutInflater vi;

	public KeywordItemAdapter(Context context, ArrayList<KeywordItem> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, final View convertView, ViewGroup parent) {
		View v = convertView;

		final KeywordItem i = items.get(position);
		if (i != null) {
			
			v = vi.inflate(R.layout.list_item_keyword, null);
			v.setOnClickListener(null);
			v.setOnLongClickListener(null);
			v.setLongClickable(false);
			
			final TextView sectionView = (TextView) v.findViewById(R.id.list_item_entry_text);
			sectionView.setText(i.title);
			
			//set remove button click listener
			((ImageView)v.findViewById(R.id.item_delete)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					items.remove(position);
					Screen4_Activity.updateAdapter();
				}
			});
			
		}
		return v;
	}

}
