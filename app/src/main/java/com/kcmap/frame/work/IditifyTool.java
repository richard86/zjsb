package com.kcmap.frame.work;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.kcmap.frame.R;

public class IditifyTool {

	public MyTouchListener myTouch;
	private MapView map;
	private Context context;
	private GraphicsLayer glayer;
	// 构造函数
	public IditifyTool(Context context, MapView map, GraphicsLayer glayer) {
		this.context = context;
		this.map = map;
		this.glayer=glayer;
	}


	public void OpenIditify() {
		// 实例化监听器
		myTouch = new MyTouchListener(context, map,glayer);
		// 设置绘画监听
		map.setOnTouchListener(myTouch);
	}

	public void CancelIditify() {
		map.setOnTouchListener(null);
	}

	/**
	 *
	 * 触摸监听类
	 *
	 * @author Lzw
	 *
	 *
	 */
	public class MyTouchListener extends MapOnTouchListener {
		Context context;
		GraphicsLayer graphicsLayer;
		MapView mapView;
		Callout callout;


		public MyTouchListener(Context context, MapView view, GraphicsLayer glayer) {
			super(context, view);
			// TODO Auto-generated constructor stub
			this.context=context;
			this.graphicsLayer = glayer;
			this.mapView = view;

			this.callout=mapView.getCallout();
			this.callout.setStyle(R.xml.calloutstyle);
		}

		public void showCallOut(Point point) {
			Point e = mapView.toScreenPoint(point);
			int[] uids = graphicsLayer.getGraphicIDs((float)e.getX(), (float)e.getY(), 10);
			if (uids != null && uids.length > 0) {
				for (int i = 0; i < uids.length; i++) {
					int targetId = uids[i];
					Graphic selection = graphicsLayer.getGraphic(targetId);
					if (selection.getGeometry() instanceof Point) {
						if (selection.getAttributeValue("info")!=null) {
							String idString=selection.getAttributeValue("info").toString();
							callout.setContent(loadView(idString));
							callout.show(point);

						}
						break;
					}

				}

			} else {
				if (callout != null && callout.isShowing()) {
					callout.hide();
				}
			}

		}

		public void removeCallOut(){
			if (callout != null && callout.isShowing()) {
				callout.hide();
			}
		}

		/**
		 *
		 * 点击地图时的操作
		 */
		public boolean onSingleTap(MotionEvent e) {
			int[] uids = graphicsLayer.getGraphicIDs(e.getX(), e.getY(), 10);
			if (uids != null && uids.length > 0) {
				for (int i = 0; i < uids.length; i++) {
					int targetId = uids[i];
					Graphic selection = graphicsLayer.getGraphic(targetId);
					if (selection.getGeometry() instanceof Point) {
						if (selection.getAttributeValue("info")!=null) {
							String idString=selection.getAttributeValue("info").toString();
							callout.setContent(loadView(idString));
							callout.show(mapView.toMapPoint(new Point(e.getX(), e.getY())));

							return true;
						}
						break;
					}

				}

			} else {
				if (callout != null && callout.isShowing()) {
					callout.hide();
					return true;
				}
			}
			return false;
		}

		//======================================================================popUp属性模板
		private LinearLayout.LayoutParams LP_FF = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		private View loadView(String text) {
			ScrollView sv = new ScrollView(context);
			sv.setLayoutParams(LP_FF);

			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			sv.addView(layout);

			TextView tv;//----------------------------属性
			tv=new TextView(context);
			tv.setTextColor(Color.BLACK);
			tv.setText(text);
			layout.addView(tv);

			return sv;
		}

	}
}
