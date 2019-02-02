package br.com.utilities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberTextWatcher implements TextWatcher{
	EditText et=null;
	
	public NumberTextWatcher(EditText editText){
		et=editText;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {


	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if(!et.getText().toString().contains("R$")){
			et.removeTextChangedListener(this);
			String value= s.toString().replace('.', ',');
			et.setText(value);
			et.setSelection(value.length());
			
			et.addTextChangedListener(this);
		}

	}

}
