package com.kcmap.frame.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;
import com.kcmap.frame.ui.CustomDialog;
import com.kcmap.frame.utils.AppManager;
import com.kcmap.frame.utils.PermissionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class LoginActivity extends Activity {

	EditText txt_user;
	EditText txt_pwd;
	Button btn_login;
	AppData appData;

	public static final String EXIST = "exist";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		AppManager.getAppManager().addActivity(this);

		//--------------------安卓6.0以上需代码授权
		PermissionUtils.verifyStoragePermissions(LoginActivity.this);

		//----------------------始终显示Menu
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}

		appData = new AppData();

		String workPathString = appData.getAppData("workPath", this);
		if (!(workPathString.length() > 1)) {// 路径未设置
			Intent mainIntent = new Intent(LoginActivity.this, SDCardFileExplorerActivity.class);
			startActivityForResult(mainIntent, 0);
		} else {//

		}

		txt_user = (EditText) findViewById(R.id.edittext_username);
		txt_pwd = (EditText) findViewById(R.id.edittext_password);

		txt_user.setText(appData.getAppData("userName", this));
		txt_pwd.setText(appData.getAppData("userPassword", this));

		btn_login = (Button) findViewById(R.id.button_login);
		btn_login.setOnClickListener(new btnLoginLintener());


		//显示系统版本号
		String versionName=getAppInfo();
		TextView vTextView=(TextView)findViewById(R.id.versionName);
		vTextView.setText(versionName);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == 0) {


			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {//判断其他Activity启动本Activity时传递来的intent是否为空
			//获取intent中对应Tag的布尔值
			boolean isExist = intent.getBooleanExtra(EXIST, false);
			//如果为真则退出本Activity
			if (isExist) {
				AppManager.getAppManager().exitApp();
			}
		}
	}

	private String getAppInfo() {
		try {
			String pkName = this.getPackageName();
			String versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
			// 			int versionCode = this.getPackageManager().getPackageInfo(pkName, 0).versionCode;
			return "v" + versionName;
		} catch (Exception e) {
		}
		return null;
	}

	private class btnLoginLintener implements OnClickListener {
		public void onClick(View v) {

			if (txt_user.getText().toString().equals("") || txt_user.getText().toString() == null) {
				Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
				return;
			}


			if (appData.getAppData("RemPassWord", LoginActivity.this).equalsIgnoreCase("true")) {
				appData.setAppData("userPassword", txt_pwd.getText().toString(), LoginActivity.this);
				appData.setAppData("userName", txt_user.getText().toString(), LoginActivity.this);
			}else {
				appData.setAppData("userPassword", null, LoginActivity.this);
				appData.setAppData("userName", null, LoginActivity.this);
			}

			final String userName = txt_user.getText().toString();
			final String userPwd = txt_pwd.getText().toString();


			//---------------------------------调试用，跳过服务
			String workPathString = appData.getAppData("workPath", LoginActivity.this);
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			Bundle bundleLogin = new Bundle();
			bundleLogin.putString("userName", userName);
			bundleLogin.putString("workPathString",workPathString);

			intent.putExtras(bundleLogin);
			startActivity(intent);

		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
				startActivity(intent);
				break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		new CustomDialog.Builder(this).setTitle("提示").setMessage("确认退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						AppManager.getAppManager().exitApp();
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
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