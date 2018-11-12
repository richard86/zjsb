package com.kcmap.frame.utils;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewUtil {

	public static TextView getTextViewByID(Activity ctx,int id) {
		return (TextView)ctx.findViewById(id);
	}
	
	public static String getTextByID(Activity ctx,int id) {
		View view=ctx.findViewById(id);
		if (view instanceof TextView) {
			return getTextViewByID(ctx, id).getText().toString();
		}else if(view instanceof EditText){
			return getEditTextByID(ctx, id).getText().toString();
		}

		return null;
		
	}
	
	public static void setTextVisible(Activity ctx,int id,int visible) {
		getTextViewByID(ctx,id).setVisibility(visible);
	}
	
	public static void setTextByID(Activity ctx,int id,String txt) {
		getTextViewByID(ctx,id).setText(txt);
	}
	
	public static ImageView getImagViewByID(Activity ctx,int id) {
		return (ImageView)ctx.findViewById(id);
	}
	
	public static EditText getEditTextByID(Activity ctx,int id) {
		return (EditText)ctx.findViewById(id);
	}
	
	public static void setEditTextByID(Activity ctx,int id,String txt) {
		getEditTextByID(ctx,id).setText(txt);
	}
	
	public static ImageView getImageViewByID(Activity ctx,int id) {
		return (ImageView)ctx.findViewById(id);
	}
}
