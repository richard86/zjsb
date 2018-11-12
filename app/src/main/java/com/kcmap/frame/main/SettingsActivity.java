package com.kcmap.frame.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;


public class SettingsActivity extends Activity{
	AppData appData;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		appData=new AppData();

		final CheckBox chBox=(CheckBox)findViewById(R.id.checkbox_remember);

		if (appData.getAppData("RemPassWord", this).equalsIgnoreCase("true")) {
			chBox.setChecked(Boolean.parseBoolean(appData.getAppData("RemPassWord", this)));
		}else {
			chBox.setChecked(false);
		}
		
		
		ImageButton btnBack = (ImageButton) findViewById(R.id.btnback);
		btnBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SettingsActivity.this.finish();
			}
		});
		
		Button btn_setting=(Button)findViewById(R.id.btn_setting);
		btn_setting.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				
				if (chBox.isChecked()) {
					appData.setAppData("RemPassWord", "true", SettingsActivity.this);
				}else {
					appData.setAppData("RemPassWord", "false", SettingsActivity.this);
					appData.setAppData("userPassword", null, SettingsActivity.this);
					appData.setAppData("userName", null, SettingsActivity.this);
				}
				SettingsActivity.this.finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}
}
