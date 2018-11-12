package com.kcmap.frame.work;

import java.text.DecimalFormat;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import com.kcmap.frame.R;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class MeasureTool {
	// map属性
	private MapView map;
	// 标绘图层
	private GraphicsLayer glayer;
	private MyTouchListener myTouch;
	// 符号样式
	private SimpleMarkerSymbol sms;
	private SimpleLineSymbol sls;
	private SimpleFillSymbol sfs;
	public SimpleMarkerSymbol getSms() {
		return sms;
	}
	public void setSms(SimpleMarkerSymbol sms) {
		this.sms = sms;
	}
	public SimpleLineSymbol getSls() {
		return sls;
	}
	public void setSls(SimpleLineSymbol sls) {
		this.sls = sls;
	}
	public SimpleFillSymbol getSfs() {
		return sfs;
	}
	public void setSfs(SimpleFillSymbol sfs) {
		this.sfs = sfs;
	}
	// 构造函数
	public MeasureTool(Context context, MapView map) {
		this.map = map;
		this.glayer = new GraphicsLayer();
		this.map.addLayer(glayer);
		// 实例化监听器
		myTouch = new MyTouchListener(context, map, glayer);
		// 设置绘画监听
		this.map.setOnTouchListener(myTouch);
	}
	// 标绘点操作
	public void drawPoint() {
		this.myTouch.setType("POINT");
		myTouch.startPoint=null;
	}
	// 标绘线操作
	public void drawLine() {
		this.myTouch.setType("POLYLINE");
		myTouch.startPoint=null;
	}
	// 标绘面操作
	public void drawPolygon() {
		this.myTouch.setType("POLYGON");
		myTouch.startPoint=null;
	}

	// 取消标绘
	public void cancelDraw() {
		this.myTouch.setType("");
		myTouch.startPoint=null;
	}
	// 清除图层信息
	public void removeAll() {
		glayer.removeAll();
		map.removeLayer(glayer);
		this.myTouch.setType("");
		myTouch.startPoint=null;
	}
	/**
	 *
	 * 触摸监听类
	 *
	 * @author Lzw
	 *
	 *
	 */
	class MyTouchListener extends MapOnTouchListener {
		MultiPath poly;
		String type = "";
		Point startPoint = null;
		int uid = 0;
		int aid=0;
		GraphicsLayer graphicsLayer;
		MapView mapView;
		Context ctxContext;
		public MyTouchListener(Context context, MapView view,
							   GraphicsLayer glayer) {
			super(context, view);
			this.graphicsLayer = glayer;
			this.mapView = view;
			this.ctxContext=context;
		}
		public void setType(String geometryType) {
			this.type = geometryType;
		}
		public String getType() {
			return this.type;
		}
		/**
		 *
		 * 点击地图时的操作
		 */
		public boolean onSingleTap(MotionEvent e) {
			DecimalFormat m_df = new DecimalFormat(".########");
			if (type.length() > 1 && type.equalsIgnoreCase("POINT")) {
				if (sms == null)
					sms = new SimpleMarkerSymbol(Color.RED, 12, STYLE.CIRCLE);
				Point pPoint = mapView.toMapPoint(e.getX(), e.getY());
				Graphic graphic = new Graphic(pPoint, sms);
				graphicsLayer.addGraphic(graphic);

				//===============显示坐标
				Point reslutPoint=(Point)GeometryEngine.project(pPoint, mapView.getSpatialReference(), SpatialReference.create(4326));
				String cor="经度:"+m_df.format(reslutPoint.getX())+"\n纬度:"+m_df.format(reslutPoint.getY());
				PictureMarkerSymbol pMarkerSymbol=SymobelUtils.TextPicSymobel(ctxContext, cor,Color.BLUE, 5);
				pMarkerSymbol.setOffsetY(12);
				pMarkerSymbol.setOffsetX(-12);
				Graphic txtGraphic = new Graphic(mapView.toMapPoint(new Point(e.getX(), e.getY())), pMarkerSymbol);
				graphicsLayer.addGraphic(txtGraphic);
				return true;
			}else if(type.length() > 1 && (type.equalsIgnoreCase("POLYLINE") || type.equalsIgnoreCase("POLYGON"))){
				Point mapPt = mapView.toMapPoint(e.getX(), e.getY());
				if (startPoint == null) {
					poly = type.equalsIgnoreCase("POLYLINE") ? new Polyline() : new Polygon();
					startPoint = mapPt;
					poly.startPath((float) startPoint.getX(),(float) startPoint.getY());
					/*
					 * 创建也该Graphic对象，并将他添加到图层中去
					 */
					Graphic graphic;
					if (type.equalsIgnoreCase("POLYLINE")) {
						// 设置线型样式，如果为空者创建他------------------POLYLINE
						if (sls == null)
							sls = new SimpleLineSymbol(Color.BLUE, 2);
						graphic = new Graphic(poly, sls);
					} else {
						if (sfs == null) {
							// 设置面状样式，如果为空者创建他---------------POLYGON
							sfs = new SimpleFillSymbol(Color.RED);
							sfs.setOutline(new SimpleLineSymbol(Color.BLUE, 2));
							sfs.setAlpha(50);
						}
						graphic = new Graphic(poly, sfs);
					}
					// 建图形添加到图层中
					uid = graphicsLayer.addGraphic(graphic);
				}
				poly.lineTo((float) mapPt.getX(), (float) mapPt.getY());
				int count=poly.getPointCount();
				m_df = new DecimalFormat("#.##");
				if(type.equalsIgnoreCase("POLYLINE")) {
					//=================================显示距离
					if(count>2){
						Point sPoint=poly.getPoint(count-2);
						Point ePoint=poly.getPoint(count-1);
						Double lenthDouble=GeometryEngine.distance(sPoint,ePoint, mapView.getSpatialReference());
						String jl;
						if(map.getSpatialReference().isWGS84()){//wgs84
							jl=m_df.format(lenthDouble*100000)+"米";
						}else if (map.getSpatialReference().getID()==4490) {
							jl=m_df.format(lenthDouble*100000)+"米";
						}else {//xian80-120
							jl=m_df.format(lenthDouble)+"米";
						}
						PictureMarkerSymbol pMarkerSymbol=SymobelUtils.TextPicSymobel(ctxContext, jl,Color.BLUE, 5);
						pMarkerSymbol.setOffsetY(12);
						pMarkerSymbol.setOffsetX(-12);
						Graphic txtGraphic = new Graphic(ePoint, pMarkerSymbol);
						graphicsLayer.addGraphic(txtGraphic);
					}
				}
				if(type.equalsIgnoreCase("POLYGON")) {
					//==================================显示面积
					if(count>3){
						Polygon polygon=new Polygon();
						polygon.add(poly, false);
						Geometry geometry=(Geometry)polygon;
						Double area;
						if(map.getSpatialReference().isWGS84()){//wgs84
							area=Math.abs(geometry.calculateArea2D())*10000*1000000;	//area*km2*m2
						}else if (map.getSpatialReference().getID()==4490) {
							area=Math.abs(geometry.calculateArea2D())*10000*1000000;	//area*km2*m2
						}
						else {//xian80-120
							area=Math.abs(geometry.calculateArea2D());	//area m2
						}
						String areaString=m_df.format(area)+"平方米";//m_df.format(area/666.67)+"亩\r\n"+m_df.format(area/10000)+"公顷\r\n"+
						PictureMarkerSymbol pMarkerSymbol=SymobelUtils.TextPicSymobel(ctxContext, areaString,Color.BLUE, 5);
						if(aid==0){
							aid=graphicsLayer.addGraphic(new Graphic(getCenterPoint(poly), pMarkerSymbol));
						}else{
							graphicsLayer.updateGraphic(aid, new Graphic(getCenterPoint(poly), pMarkerSymbol));
						}

					}
				}
				// 更新图形
				graphicsLayer.addGraphic(new Graphic(mapView.toMapPoint(new Point(e.getX(), e.getY())), new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE)));
				graphicsLayer.updateGraphic(uid, poly);
				return true;
			}

			return false;
		}

		private Point getCenterPoint(MultiPath polyPath){
			double xmin=polyPath.getPoint(0).getX();
			double ymin=polyPath.getPoint(0).getY();
			double xmax=polyPath.getPoint(0).getX();
			double ymax=polyPath.getPoint(0).getY();

			for(int i=0;i<polyPath.getPointCount();i++){
				if(xmin>polyPath.getPoint(i).getX()){
					xmin=polyPath.getPoint(i).getX();
				}
				//				else {
				//					xmax=polyPath.getPoint(i).getX();
				//				}
				if(ymin>polyPath.getPoint(i).getY()){
					ymin=polyPath.getPoint(i).getY();
				}
				//				else {
				//					ymax=polyPath.getPoint(i).getY();
				//				}
				if(xmax<polyPath.getPoint(i).getX()){
					xmax=polyPath.getPoint(i).getX();
				}
				if(ymax<polyPath.getPoint(i).getY()){
					ymax=polyPath.getPoint(i).getY();
				}
			}
			return new Point((xmin+xmax)/2, (ymin+ymax)/2);
		}
	}
}
