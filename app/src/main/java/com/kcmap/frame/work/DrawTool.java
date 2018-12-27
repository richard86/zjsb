package com.kcmap.frame.work;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class DrawTool {
	// map属性
	private MapView map;
	// 标绘图层
	private GraphicsLayer graphicsLayer;
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
	public DrawTool(Context context, MapView map, GraphicsLayer glayer, String dbPath) {
		this.map = map;
		this.graphicsLayer = glayer;
		// 实例化监听器
		myTouch = new MyTouchListener(context, map, glayer,dbPath);
		
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
	//自由线
	public void drawFreeLine() {
		this.myTouch.setType("FREEPOLYLINE");
		myTouch.startPoint=null;
	}
	//自由面
	public void drawFreePolygon() {
		this.myTouch.setType("FREEPOLYGON");
		myTouch.startPoint=null;
	}
	// 取消标绘
	public void cancelDraw() {
		this.myTouch.setType("");
		myTouch.startPoint=null;
	}
	// 清除图层信息
	public void removeAll() {
		graphicsLayer.removeAll();
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
		Context ctx;
		int uid = 0;
		GraphicsLayer graphicsLayer;
		MapView mapView;

		String dbName;//数据库路径
		String geoString="";//几何信息 geoType:x,y;x,y;x,y
		int id=-1;//新增记录id,用于更新
		
		public MyTouchListener(Context context, MapView view,
                               GraphicsLayer glayer, String dataBaseName) {
			super(context, view);
			this.graphicsLayer = glayer;
			this.mapView = view;
			this.ctx=context;
			this.dbName=dataBaseName;
		}
		public void setType(String geometryType) {
			this.type = geometryType;
			this.geoString=type;
		}
		public String getType() {
			return this.type;
		}
		/**
		 * 
		 * 点击地图时的操作
		 */
		public boolean onSingleTap(MotionEvent e) {
			if(type.length()>1){
				DBHelper dbHelper=new DBHelper(ctx, dbName);
				dbHelper.open();
				ContentValues values = new ContentValues();
				if (type.equalsIgnoreCase("POINT")) {
					if (sms == null)
						sms = new SimpleMarkerSymbol(Color.RED, 12, STYLE.CIRCLE);
					Point pPoint = mapView.toMapPoint(e.getX(), e.getY());
					Graphic graphic = new Graphic(pPoint, sms);
					graphicsLayer.addGraphic(graphic);
					geoString=type+":"+pPoint.getX()+","+pPoint.getY();

					values.put("geometry", geoString);
					values.put("color1", Color.RED);
					id=(int) dbHelper.insert("graphics", values);
					dbHelper.close();
					return true;
				}else if(type.equalsIgnoreCase("POLYLINE") || type.equalsIgnoreCase("POLYGON")){
					Point mapPt = mapView.toMapPoint(e.getX(), e.getY());
					if (startPoint == null) {
						poly = type.equalsIgnoreCase("POLYLINE") ? new Polyline() : new Polygon();
						startPoint = mapPt;
						poly.startPath((float) startPoint.getX(),(float) startPoint.getY());
						//----创建也该Graphic对象，并将他添加到图层中去
						Graphic graphic;
						if (type.equalsIgnoreCase("POLYLINE")) {
							// 设置线型样式，如果为空者创建他------------------POLYLINE
							if (sls == null)
								sls = new SimpleLineSymbol(Color.BLUE, 3);
							graphic = new Graphic(poly, sls);

							geoString+=":"+startPoint.getX()+","+startPoint.getY();
							values.put("geometry", geoString);
							values.put("color1", Color.BLUE);
							id=(int) dbHelper.insert("graphics", values);

						} else {
							if (sfs == null) {
								// 设置面状样式，如果为空者创建他---------------POLYGON
								sfs = new SimpleFillSymbol(Color.RED);
								sfs.setOutline(new SimpleLineSymbol(Color.BLUE, 3));
								sfs.setAlpha(50);
							}
							graphic = new Graphic(poly, sfs);

							geoString+=":"+startPoint.getX()+","+startPoint.getY();
							values.put("geometry", geoString);
							values.put("color1", Color.BLUE);
							values.put("color2", Color.RED);
							id=(int) dbHelper.insert("graphics", values);
						}
						// 建图形添加到图层中
						uid = graphicsLayer.addGraphic(graphic);
					}
					poly.lineTo((float) mapPt.getX(), (float) mapPt.getY());


					// 更新图形
					graphicsLayer.addGraphic(new Graphic(mapView.toMapPoint(new Point(e.getX(), e.getY())), new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE)));
					graphicsLayer.updateGraphic(uid, poly);
					//更新库
					geoString+=";"+mapPt.getX()+","+mapPt.getY();
					values.put("geometry", geoString);
					dbHelper.update("graphics", values, "id = ?", new String[]{String.valueOf(id)});
					dbHelper.close();
					return true;
				}
			}
			return false;
		}
		/**
		 * 
		 * 点击地图并在屏幕上滑动的操作
		 * 
		 * 
		 */
		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			if (type.length() > 1 && (type.equalsIgnoreCase("FREEPOLYLINE") || type.equalsIgnoreCase("FREEPOLYGON"))) {
				Point mapPt = mapView.toMapPoint(to.getX(), to.getY());
				//如果startPoint为空，者创建它并将设置为poly的起始点
				if (startPoint == null) {
					poly = type.equalsIgnoreCase("FREEPOLYLINE") ? new Polyline() : new Polygon();
					startPoint = mapView.toMapPoint(from.getX(), from.getY());
					poly.startPath((float) startPoint.getX(), (float) startPoint.getY());
					//创建也该Graphic对象，并将他添加到图层中去
					Graphic graphic;
					if (type.equalsIgnoreCase("FREEPOLYLINE")) {
						// 设置线型样式，如果为空者创建他						
						if (sls == null)
							sls = new SimpleLineSymbol(Color.RED, 2);
						graphic = new Graphic(poly, sls);
					} else {
						if (sfs == null) {
							// 设置面状样式，如果为空者创建他
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
				// 更新图形
				graphicsLayer.updateGraphic(uid, poly);

				return true;
			}

			return super.onDragPointerMove(from, to);
		}
		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			if (type.length() > 1 && (type.equalsIgnoreCase("FREEPOLYLINE") || type.equalsIgnoreCase("FREEPOLYGON"))) {
                DBHelper dbHelper=new DBHelper(ctx, dbName);
                dbHelper.open();
                int count=poly.getPointCount();
                //ContentValues values = new ContentValues();
                geoString=type+":"+poly.getPoint(0).getX()+","+poly.getPoint(0).getY();
                if (type.equalsIgnoreCase("FREEPOLYLINE")) {

                    for(int i=1;i<count;i++){
                        geoString+=";"+poly.getPoint(i).getX()+","+poly.getPoint(i).getY();
                    }
                    //values.put("geometry", geoString);
                    //values.put("color1", Color.BLUE);

                    //id=(int) dbHelper.insert("graphics", values);

                }
                //判断当是面状要素时添加第一个点到poly上.
				if (type.equalsIgnoreCase("FREEPOLYGON")) {
					poly.lineTo((float) startPoint.getX(), (float) startPoint.getY());
                    for(int i=1;i<count+1;i++){
                        geoString+=";"+poly.getPoint(i).getX()+","+poly.getPoint(i).getY();
                    }
                    //values.put("geometry", geoString);
                    //values.put("color1", Color.BLUE);
                    //values.put("color2", Color.RED);
                    //id=(int) dbHelper.insert("graphics", values);
				}
                //geoString="FREEPOLYGON:541289.8125,3673222.25;541289.75,3673222.25;541289.625,3673222.25;541289.3125,3673222.0;541289.0,3673222.0;541288.75,3673222.0;541288.4375,3673222.0;541288.1875,3673222.25;541287.9375,3673222.25;541287.6875,3673222.5;541287.375,3673222.75;541287.0625,3673223.0;541286.8125,3673223.5";
                Envelope ev=new Envelope();
                poly.queryEnvelope(ev);

                String insertSql="insert into graphics (geometry,xmin,ymin,xmax,ymax) values ('"+geoString+"',"+ev.getXMin()+","+ev.getYMin()+","+ev.getXMax()+","+ev.getYMax()+")";
                dbHelper.execSQL(insertSql);
                dbHelper.close();
				startPoint = null;
				return true;
			}
			return super.onDragPointerUp(from, to);
		}
	}
}

