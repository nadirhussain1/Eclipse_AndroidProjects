package com.mobive.dealme.deal_items;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.humby.dealular.R;
import com.mobive.dealme.DealMePreferences;

public class EntryAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> items;
	private LayoutInflater vi;
	HashMap<Integer, Integer> enteryAdapterStates=new HashMap<Integer, Integer>();
	private int noOfSelection=0;
	private boolean hasUserChangedSettings=false;
	
	public HashMap<Integer, Integer> getEnteryAdapterStates() {
		return enteryAdapterStates;
	}

	public void setEnteryAdapterStates(HashMap<Integer, Integer> enteryAdapterStates) {
		this.enteryAdapterStates = enteryAdapterStates;
	}
    public int getNoOfSelection(){
    	return noOfSelection;
    }
    public boolean getHasUserChangedSettings(){
    	return hasUserChangedSettings;
    }
    public void setHasUserChangedSettings(boolean changed){
    	 hasUserChangedSettings=changed;
    }
	public EntryAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		this.items = items;
		vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		checkMarkAlreadyEntries();
	}
    private void checkMarkAlreadyEntries(){
    	String selectedDeals=DealMePreferences.getInstance().getSelectedDeals();
    	for(int count=0;count<items.size();count++){
    		if(selectedDeals.contains(items.get(count).getSlug())){
    			enteryAdapterStates.put(count, 1);
    			noOfSelection++;
    		}
    	}
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		final Item i = items.get(position);
		if (i != null) {
			if (i.isSection()) {
				SectionItem si = (SectionItem) i;
				v = vi.inflate(R.layout.list_item_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);
				
				final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());
			} else {
				EntryItem ei = (EntryItem) i;
				v = vi.inflate(R.layout.list_item_entry, null);
				final CheckBox title = (CheckBox) v.findViewById(R.id.list_item_entry_checkbox);
				title.setOnClickListener(new ChangeListener(position));
				if (title != null) {
					title.setText(ei.title);
					
					
				}
				if (enteryAdapterStates.containsKey(position)) {
					if (enteryAdapterStates.get(position) == 0) {
						title.setChecked(false);
					} else if (enteryAdapterStates.get(position) == 1) {
						title.setChecked(true);
					}
				}
			}
		}
		return v;
	}
	class ChangeListener implements OnClickListener
	{
		int position=-1;
		public ChangeListener(int position)
		{
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			hasUserChangedSettings=true;
			CheckBox boxView=(CheckBox)v;
			if(boxView.isChecked()){
				enteryAdapterStates.put(position, 1);
				noOfSelection++;
			}else{
				enteryAdapterStates.put(position, 0);
				noOfSelection--;
			}
			
		}
		
	}
	public void clearHashMap()
	{
		enteryAdapterStates.clear();
	}


}
