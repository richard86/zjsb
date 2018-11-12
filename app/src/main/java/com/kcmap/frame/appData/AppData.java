package com.kcmap.frame.appData;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class AppData  {

	public void setAppData(String dataName, String data,Activity activity)
	{
		setData(dataName, data,activity);
	}
	
	public String getAppData(String dataName,Activity activity)
	{
		return getData(dataName,activity);
	}
	
	
	private void setData(String dataName, String data,Activity activity) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences("share", activity.MODE_PRIVATE);
		//String isFirstRun = sharedPreferences.getBoolean(dataName, true);
		Editor editor = sharedPreferences.edit();

		editor.putString(dataName, data);
		editor.commit();
	}
	
	private String getData(String dataName,Activity activity)
	{
		SharedPreferences sharedPreferences = activity.getSharedPreferences("share",activity.MODE_PRIVATE);
		String data = sharedPreferences.getString(dataName, "");
		return data;
	}
}







