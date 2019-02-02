/***
  Copyright (c) 2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.

  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package com.mobive.dealme;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.humby.dealular.R;

public class EditorFragment extends SherlockFragment {
	private static final String KEY_POSITION="position";

	static EditorFragment newInstance(int position) {
		EditorFragment frag=new EditorFragment();
		Bundle args=new Bundle();

		args.putInt(KEY_POSITION, position);
		frag.setArguments(args);

		return(frag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		int position=getArguments().getInt(KEY_POSITION, -1);
		
		View result=null;
	
		if(position==0){
			MyDealsSelectorView selector=new MyDealsSelectorView();
			result=selector.getDealsSelectorView();
			DataUtility.selector=selector;
			
		} 
		if(position==1){

			AccountsEditor accountsSettings=new AccountsEditor();
			result=accountsSettings.getAccountsScreenView();

		} 
		if(position==2)
		{  
			result=inflater.inflate(R.layout.about_settings, container, false);
			Button checkOutApps=(Button)result.findViewById(R.id.CheckAppsButton);
			checkOutApps.setOnClickListener(checkMoreAppsListener);
		} 


		return(result);
	}
	
	OnClickListener checkMoreAppsListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			String link="https://play.google.com/store/apps/developer?id=Humby+Apps";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(link));
			startActivity(i);
		}
	};
}