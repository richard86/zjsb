package com.kcmap.frame.work;

import android.graphics.Bitmap;
import android.view.View;

public class ArcGISUtils {
	public static Bitmap convertViewToBitmap(View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.getMode(0)), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.getMode(0)));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}
}
