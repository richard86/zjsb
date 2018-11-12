package com.kcmap.frame.work;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.core.symbol.PictureMarkerSymbol;

public class SymobelUtils {
	public static PictureMarkerSymbol pictureSymobel(Context context, int layoutID)
	  {
	    LayoutInflater inflater = LayoutInflater.from(context);
	    View vw = inflater.inflate(layoutID, null);
	    Bitmap bt = ArcGISUtils.convertViewToBitmap(vw);
	    BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

	    return new PictureMarkerSymbol(temp);
	  }

	  public static PictureMarkerSymbol pictureSymobel(Context context, View view)
	  {
	    Bitmap bt = ArcGISUtils.convertViewToBitmap(view);
	    BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

	    return new PictureMarkerSymbol(temp);
	  }

	  public static PictureMarkerSymbol TextPicSymobel(Context context, CharSequence label, int color, float size, int imgInt, MODE mode)
	  {
	    LinearLayout layout = new LinearLayout(context);
	    layout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));

	    ImageView imgView = new ImageView(context);
	    imgView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
	    Drawable db = context.getResources().getDrawable(imgInt);
	    imgView.setImageDrawable(db);

	    TextView txtView = new TextView(context);
	    txtView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
	    txtView.setText(label);
	    txtView.setTextColor(color);
	    txtView.setTextSize(size);

	    switch (mode)
	    {
		    case BOTTOM:
		      layout.addView(txtView);
		      layout.addView(imgView);
		      layout.setOrientation(LinearLayout.HORIZONTAL);
		      break;
		    case LEFT:
		      layout.addView(imgView);
		      layout.addView(txtView);
		      layout.setOrientation(LinearLayout.HORIZONTAL);
		      break;
		    case RIGHT:
		      layout.addView(txtView);
		      layout.addView(imgView);
		      layout.setOrientation(LinearLayout.VERTICAL);
		      break;
		    case TOP:
		      layout.addView(imgView);
		      layout.addView(txtView);
		      layout.setOrientation(LinearLayout.VERTICAL);
	    }

	    Bitmap bt = ArcGISUtils.convertViewToBitmap(layout);
	    BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

	    return new PictureMarkerSymbol(temp);
	  }

	  public static PictureMarkerSymbol TextPicSymobel(Context context, CharSequence label, int color, float size)
	  {
	    LinearLayout layout = new LinearLayout(context);
	    layout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));

	    TextView txtView = new TextView(context);
	    txtView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
	    txtView.setText(label);
	    txtView.setTextColor(color);
	    txtView.setTextSize(size);

	    layout.addView(txtView);

	    Bitmap bt = ArcGISUtils.convertViewToBitmap(layout);
	    BitmapDrawable temp = new BitmapDrawable(context.getResources(), bt);

	    return new PictureMarkerSymbol(temp);
	  }
	  
//	  public static PictureMarkerSymbol TextPicSymobel(Context context, CharSequence label)
//	  {
//		  return TextPicSymobel(context, label, Color.BLUE, 12);
//	  }
	  
//	  public static PictureMarkerSymbol TextPicSymobel(Context context, CharSequence label, int imgInt, MODE mode)
//	  {
////	    return TextPicSymobel(context, label, -16777216, 15.0F, imgInt, mode);
//		  return TextPicSymobel(context, label, Color.BLUE, 12, imgInt, mode);
//	  }

	  public static enum MODE
	  {
	    RIGHT, 
	    LEFT, 
	    TOP, 
	    BOTTOM;
	  }
}
