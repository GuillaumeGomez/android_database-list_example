package com.dbexample;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddElem extends Activity {
	public static final String EXTRA_RETURN = "extra_ret";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_elem);
	}

	public void onClickOnAdd(View v){
		String tmp = ((TextView)findViewById(R.id.elem_title)).getText().toString();

		if (tmp.equals("")){
			displayPopup("Please fill title field", "Error");
			return;
		}

		Intent intent = new Intent();
		ArrayList<String>	ret = new ArrayList<String>();

		ret.add(tmp);
		ret.add(String.valueOf(((TextView)findViewById(R.id.elem_description)).getText().toString()));
		intent.putStringArrayListExtra(EXTRA_RETURN, ret);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void displayPopup(String text, String title){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(text);
		builder.setTitle(title);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		builder.create().show();
	}
}

