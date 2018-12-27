package com.kcmap.frame.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;
import com.kcmap.frame.ui.CustomDialog;
import com.kcmap.frame.ui.CustomListAdapter;
import com.kcmap.frame.utils.AppManager;
import com.kcmap.frame.work.DBHelper;
import com.kcmap.frame.work.DrawTool;
import com.kcmap.frame.work.GPSModel;
import com.kcmap.frame.work.AnyResponse;
import com.kcmap.frame.work.MeasureTool;
import com.kcmap.frame.work.UtilTool;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements AnyResponse {

    AppData appData;
    File workingDirectory;
    //样本tpk
    File[] tpkFileList;
    String[] tpkName;
    String[] tfName;
    String[] checkPerson;
    MapView mMapView;

    TextView txt_XMH;
    TextView txt_PCH;
    TextView txt_YBH;
    TextView txt_JCR;

    String[] measureTypes;

    String dbPath;
    DBHelper dbHelper;

    MeasureTool measureTool;
    DrawTool drawTool;
    GPSModel gpsModel;
    int gpsGraphicID=0;
    Point myPoint = null;
    SimpleMarkerSymbol smsMarkerSymbol = null;
    Boolean gzBoolean = false;

    GraphicsLayer graphicsLayer;
    GraphicsLayer linegraphicsLayer;
    GraphicsLayer tempGraphicLayer;
    GraphicsLayer gpsLayer;

    GraphicsLayer ctGraphicLayer;


    MultiPath poly;
    SimpleLineSymbol sls;
    SimpleMarkerSymbol sms;
    Graphic graphicLine;
    int graphicID;

    ListView xcinfolistView;
    LinearLayout xcinfolist;

    ListView bcinfolistView;
    LinearLayout bcinfolist;

    ImageView btnCollect;
    ImageView btnDistance;
    ImageView btnLocate;
    ImageView btnSwitchTable;
    ImageView btnOpenTable;

    ImageView i_draw_point;
    ImageView i_draw_line;
    ImageView i_draw_freeline;
    ImageView i_draw_poly;
    ImageView i_draw_freepoly;
    ImageView i_draw_text;

    ImageView i_pan;
    ImageView i_ok;
    ImageView i_cancel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);

        appData = new AppData();
        // --------------------------------------初始化工程目录
        //------获取工程目录
        String workDirectory = appData.getAppData("workPath",MainActivity.this);
        //------获取项目信息
        String XMH=appData.getAppData("XMH",MainActivity.this);
        String PCH = appData.getAppData("PCH",MainActivity.this);

        checkPerson = new String[]{"张三", "李四", "王五"};//检查人员
        //--------------初始化控件
        txt_XMH = (TextView) findViewById(R.id.xmnum);
        txt_XMH.setText("项目号："+XMH);
        txt_PCH = (TextView) findViewById(R.id.pcnum);
        txt_PCH.setText("当前批次："+PCH);
        txt_YBH = (TextView) findViewById(R.id.ybnum);
        String YBH=appData.getAppData("YBH",MainActivity.this);
        if(YBH.length()>0){
            txt_YBH.setText("当前样本："+YBH);
        }
        txt_JCR = (TextView) findViewById(R.id.jcry);

        File pcFolder=new File(workDirectory+File.separator+PCH);

        if (pcFolder != null) {
            tpkFileList = pcFolder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".tpk");
                }
            });
        }

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.WHITE, 0, 0);
        mMapView.setOnStatusChangedListener(mapStatusChangedEvent);

        //----------设置infotab透明
        LinearLayout lllLayout = (LinearLayout) findViewById(R.id.infotab);
        lllLayout.getBackground().mutate().setAlpha(180);

        btnCollect = (ImageView) findViewById(R.id.btncollect);
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,String.valueOf(mMapView.getScale()),Toast.LENGTH_SHORT).show();
                Point centerPoint = mMapView.getCenter();
                Graphic graphic = new Graphic(centerPoint, new SimpleMarkerSymbol(Color.RED, 9, STYLE.CIRCLE));
                graphicsLayer.addGraphic(graphic);

                Intent intent = new Intent(MainActivity.this, XcInfoMainActivity.class);

                appData.setAppData("TAG","POINT",MainActivity.this);
                appData.setAppData("X", String.valueOf(centerPoint.getX()),MainActivity.this);
                appData.setAppData("Y", String.valueOf(centerPoint.getY()),MainActivity.this);
                startActivity(intent);

            }
        });

        btnDistance = (ImageView) findViewById(R.id.btndistance);
        btnDistance.setOnClickListener(lineBtnOnClickListener);

        if (tpkFileList != null) {
            tpkName = new String[tpkFileList.length];
            for (int i = 0; i < tpkFileList.length; i++) {
                tpkName[i] = tpkFileList[i].getName().substring(0, tpkFileList[i].getName().indexOf(".tpk"));
            }

            dbPath=UtilTool.getDBPath(MainActivity.this);
            dbHelper=new DBHelper(MainActivity.this,dbPath);

            tfName=new String[tpkName.length];
            dbHelper.open();
            Cursor returnCursor = dbHelper.findList("XMInfo", new String[] { "rowid","YBH","TFH"}, null,
                    null, null, null, null);
            int i=0;
            while (returnCursor.moveToNext()){
                tfName[i]=tpkName[i]+" ("+returnCursor.getString(returnCursor.getColumnIndex("TFH"))+")";
                i++;
            }
            dbHelper.close();

            if(YBH.length()>0){

                for(int k=0;k<tfName.length;k++){
                    if(tfName[k].startsWith(YBH)){
                        LoadLocalTiledMapService(mMapView, "file://" + pcFolder.getAbsolutePath()+File.separator+YBH+".tpk", 0);
                        txt_YBH.setText("当前样本：" + tfName[k]);
                        break;
                    }
                }
            }else{
                LoadLocalTiledMapService(mMapView, "file://" + tpkFileList[0].getAbsolutePath(), 0);
                txt_YBH.setText("当前样本：" + tfName[0]);
            }
        }

        xcinfolistView=(ListView)findViewById(R.id.xcinfolistview);
        xcinfolist=(LinearLayout)findViewById(R.id.xcinfolist);

        bcinfolistView=(ListView)findViewById(R.id.bcinfolistview);
        bcinfolist=(LinearLayout)findViewById(R.id.bcinfolist);

        btnSwitchTable=(ImageView)findViewById(R.id.switchtable);
        btnSwitchTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xcinfolist.getVisibility()==View.VISIBLE){
                    btnSwitchTable.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.btnbcinfo));
                    xcinfolist.setVisibility(View.GONE);
                    bcinfolist.setVisibility(View.VISIBLE);
                    tempGraphicLayer.removeAll();
                    QueryBCInfo();
                }
                else{
                    btnSwitchTable.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.btnxcinfo));
                    xcinfolist.setVisibility(View.VISIBLE);
                    bcinfolist.setVisibility(View.GONE);
                    tempGraphicLayer.removeAll();
                    QueryXCInfo();
                }
            }
        });

        btnOpenTable=(ImageView)findViewById(R.id.btntable);
        btnOpenTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnSwitchTable.getVisibility()==View.VISIBLE){
                    btnOpenTable.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.btn_table));
                    btnSwitchTable.setVisibility(View.GONE);
                    xcinfolist.setVisibility(View.GONE);
                    bcinfolist.setVisibility(View.GONE);
                    tempGraphicLayer.removeAll();
                }else{
                    btnSwitchTable.setVisibility(View.VISIBLE);
                    xcinfolist.setVisibility(View.VISIBLE);
                    btnOpenTable.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.btn_table_pressed));
                    btnSwitchTable.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.btnbcinfo));
                    QueryXCInfo();
                }
            }
        });



        btnLocate=(ImageView)findViewById(R.id.btnlocate);
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gpsModel==null){
                    Toast.makeText(MainActivity.this,"请先打开GPS定位功能！",Toast.LENGTH_SHORT).show();
                }else {
                    gzBoolean=!gzBoolean;
                    if(gzBoolean){
                        btnLocate.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.btn_locate_pressed));
                    }else {
                        btnLocate.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.btn_locate));
                        gpsModel.CloseGPS();
                        gpsModel=null;
                    }
                }

            }
        });

        sms = new SimpleMarkerSymbol(Color.RED, 9, STYLE.CIRCLE);
        sls = new SimpleLineSymbol(Color.BLUE, 2);

        //------------------草图工具
        i_draw_point=(ImageView)findViewById(R.id.i_draw_point);
        i_draw_point.setOnClickListener(drawToolClickListener);

        i_draw_line=(ImageView)findViewById(R.id.i_draw_line);
        i_draw_line.setOnClickListener(drawToolClickListener);

        i_draw_freeline=(ImageView)findViewById(R.id.i_draw_freeline);
        i_draw_freeline.setOnClickListener(drawToolClickListener);

        i_draw_poly=(ImageView)findViewById(R.id.i_draw_poly);
        i_draw_poly.setOnClickListener(drawToolClickListener);

        i_draw_freepoly=(ImageView)findViewById(R.id.i_draw_freepoly);
        i_draw_freepoly.setOnClickListener(drawToolClickListener);

        i_draw_text=(ImageView)findViewById(R.id.i_draw_text);
        i_draw_text.setOnClickListener(drawToolClickListener);

        i_ok=(ImageView)findViewById(R.id.i_ok);
        i_ok.setOnClickListener(drawToolClickListener);

        i_cancel=(ImageView)findViewById(R.id.i_cancel);
        i_cancel.setOnClickListener(drawToolClickListener);

        i_pan=(ImageView)findViewById(R.id.i_pan);
        i_pan.setOnClickListener(drawToolClickListener);

    }

    View.OnClickListener drawToolClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RestoreDrawTool();
            if(drawTool==null){
                drawTool=new DrawTool(MainActivity.this,mMapView,ctGraphicLayer,dbPath);
            }

            if(view.getId()==R.id.i_draw_point){
                drawTool.drawPoint();
                i_draw_point.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_point_open));
            }
            if(view.getId()==R.id.i_draw_line){
                drawTool.drawLine();
                i_draw_line.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_line_open));
            }
            if(view.getId()==R.id.i_draw_freeline){

                drawTool.drawFreeLine();
                i_draw_freeline.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_freeline_open));
            }
            if(view.getId()==R.id.i_draw_poly){

                drawTool.drawPolygon();
                i_draw_poly.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_poly_open));
            }
            if(view.getId()==R.id.i_draw_freepoly){

                drawTool.drawFreePolygon();
                i_draw_freepoly.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_freepoly_open));
            }
            if(view.getId()==R.id.i_pan){
                i_pan.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_pan_open));
                if(drawTool!=null){
                    drawTool.cancelDraw();
                    drawTool=null;
                }
            }
            if(view.getId()==R.id.i_ok){
                if(drawTool!=null){
                    drawTool.cancelDraw();
                    String UID=java.util.UUID.randomUUID().toString();
                    appData.setAppData("TAG","POLY",MainActivity.this);
                    appData.setAppData("UID",UID,MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, XcInfoMainActivity.class);
                    startActivity(intent);
                }
            }
            if(view.getId()==R.id.i_cancel){
                if(drawTool!=null) {
                    drawTool.cancelDraw();
                    DeleteUIDNullGraphics();
                }

            }
        }
    };

    private void DeleteUIDNullGraphics(){
        dbHelper.open();
        dbHelper.execSQL("delete from graphics where uid is null");
        dbHelper.close();
        int[] gs=ctGraphicLayer.getGraphicIDs();
        for(int i=0;i<gs.length;i++){
            if(ctGraphicLayer.getGraphic(gs[i]).getAttributeValue("uid")==null){
                ctGraphicLayer.removeGraphic(gs[i]);
            }
        }
    }

    private void SetUIDNullGraphics(String UID){
        HashMap<String, Object> attribute = new HashMap<String, Object>();
        attribute.put("uid", UID);
        int[] gs=ctGraphicLayer.getGraphicIDs();
        for(int i=0;i<gs.length;i++){
            if(ctGraphicLayer.getGraphic(gs[i]).getAttributeValue("uid")==null){
                ctGraphicLayer.updateGraphic(gs[i],attribute);
            }
        }
    }

    private boolean CheckUIDGraphicExits(String UID){
        boolean isExits=false;
        if(ctGraphicLayer.getNumberOfGraphics()>0){
            int[] gs=ctGraphicLayer.getGraphicIDs();
            for(int i=0;i<gs.length;i++){
                if(ctGraphicLayer.getGraphic(gs[i]).getAttributeValue("uid").toString().equalsIgnoreCase(UID)){
                    isExits=true;
                    break;
                }
            }
        }
        return isExits;
    }

    private void RestoreDrawTool(){
        i_draw_point.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_point));
        i_draw_line.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_line));
        i_draw_freeline.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_freeline));
        i_draw_poly.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_poly));
        i_draw_freepoly.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_freepoly));
        i_draw_text.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_draw_text));
        i_pan.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.i_pan));
    }

    //-------------初始化草图
    private void InitDrawLayer(){
        ctGraphicLayer.removeAll();
        dbHelper.open();
        Cursor returnCursor=dbHelper.queryList("select id,geometry,uid from graphics where uid is not null order by rowid desc");

        while(returnCursor.moveToNext()){
            String geoString=returnCursor.getString(1);//geoType:x,y|x,y|x,y
            String geoType=geoString.split(":")[0];//geoType
            String geoPointsString=geoString.split(":")[1];//x,y|x,y|x,y
            String UID=returnCursor.getString(2);

            HashMap<String, Object> attribute = new HashMap<String, Object>();
            attribute.put("uid", UID);
            if(geoType.equalsIgnoreCase("POINT")){
                SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 12, STYLE.CIRCLE);
                String xString=geoPointsString.split(",")[0];//x
                String yString=geoPointsString.split(",")[1];//y
                Point point=new Point(Double.valueOf(xString), Double.valueOf(yString));
                Graphic pGraphic=new Graphic(point, sms,attribute);
                ctGraphicLayer.addGraphic(pGraphic);
            }else if(geoType.equalsIgnoreCase("POLYLINE") || geoType.equalsIgnoreCase("POLYGON") || geoType.equalsIgnoreCase("FREEPOLYLINE") || geoType.equalsIgnoreCase("FREEPOLYGON")){
                String[] pointString=geoPointsString.split(";");//x,y
                MultiPath poly = (geoType.equalsIgnoreCase("POLYLINE") || geoType.equalsIgnoreCase("FREEPOLYLINE")) ? new Polyline() : new Polygon();
                poly.startPath(Double.valueOf(pointString[0].split(",")[0]), Double.valueOf(pointString[0].split(",")[1]));
                for(int i=0;i<pointString.length;i++){
                    String xString=pointString[i].split(",")[0];//x
                    String yString=pointString[i].split(",")[1];//y
                    Point point=new Point(Double.valueOf(xString), Double.valueOf(yString));
                    poly.lineTo(point);
                }
                if(geoType.equalsIgnoreCase("POLYLINE") || geoType.equalsIgnoreCase("FREEPOLYLINE")){
                    SimpleLineSymbol sls = new SimpleLineSymbol(Color.BLUE, 2);
                    Graphic pGraphic=new Graphic(poly, sls,attribute);
                    ctGraphicLayer.addGraphic(pGraphic);
                }
                if(geoType.equalsIgnoreCase("POLYGON") || geoType.equalsIgnoreCase("FREEPOLYGON")){
                    SimpleFillSymbol sfs = new SimpleFillSymbol(Color.RED);
                    sfs.setOutline(new SimpleLineSymbol(Color.BLUE, 2));
                    sfs.setAlpha(50);
                    Graphic pGraphic=new Graphic(poly, sfs,attribute);
                    ctGraphicLayer.addGraphic(pGraphic);
                }
            }
        }
        dbHelper.close();
    }

    private void QueryBCInfo(){
        List<HashMap<String, Object>> dataMap = new ArrayList<HashMap<String, Object>>();

        dbHelper.open();
        String YBH=UtilTool.getYBH(MainActivity.this);
        Cursor returnCursor = dbHelper.findList("N7_Record", new String[] { "rowid","Length"}, "YBH = ?",
                new String[] { YBH }, null, null, "rowid desc");
        while (returnCursor.moveToNext()){
            String nrowid=returnCursor.getString(returnCursor.getColumnIndex("rowid"));
            String nlength=returnCursor.getString(returnCursor.getColumnIndex("Length"));


            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("rowid", nrowid);
            map.put("length", nlength);

            dataMap.add(map);
        }

        dbHelper.close();

        CustomListAdapter adapter=new CustomListAdapter(MainActivity.this,dataMap,R.layout.item_activity_bcinfo,
                new String[]{"rowid","length"},new int[]{R.id.nrowid,R.id.nlength});

        bcinfolistView.setAdapter(adapter);

        bcinfolistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ListView lv = (ListView) parent;

                if (lv.getTag() != null){
                    ((View)lv.getTag()).setBackgroundDrawable(null);
                }
                lv.setTag(view);
                view.setBackgroundColor(Color.RED);

                HashMap<String, Object> hashMap = (HashMap<String, Object>) lv.getItemAtPosition(position);// SimpleAdapter返回Map

                dbHelper.open();

                Cursor returnCursor = dbHelper.findList("N7_Record", new String[] { "rowid","Length","X0","Y0","X1","Y1"}, "rowid = ?",
                        new String[] { hashMap.get("rowid").toString() }, null, null, "rowid desc");
                if (returnCursor.moveToNext()){
                    double X0=returnCursor.getDouble(returnCursor.getColumnIndex("X0"));
                    double Y0=returnCursor.getDouble(returnCursor.getColumnIndex("Y0"));
                    double X1=returnCursor.getDouble(returnCursor.getColumnIndex("X1"));
                    double Y1=returnCursor.getDouble(returnCursor.getColumnIndex("Y1"));
                    MultiPath poly =new Polyline();
                    poly.startPath(new Point(X0,Y0));
                    poly.lineTo(new Point(X1,Y1));
                    Graphic graphic=new Graphic(poly,sls);
                    tempGraphicLayer.addGraphic(graphic);
                    mMapView.centerAt(new Point((X1+X0)/2,(Y1+Y0)/2),false);
                    mMapView.setScale(300);
                }

                dbHelper.close();

            }
        });
    }

    private void QueryXCInfo(){

        List<HashMap<String, Object>> dataMap = new ArrayList<HashMap<String, Object>>();

        dbHelper.open();
        String YBH=UtilTool.getYBH(MainActivity.this);
        Cursor returnCursor = dbHelper.findList("N5_Record", new String[] { "rowid","WTDM","WTMS","UID"}, "YBH = ?",
                new String[] { YBH }, null, null, "rowid desc");

//        Cursor returnCursor=dbHelper.queryList(sql);
        while (returnCursor.moveToNext()){
            String rowid=returnCursor.getString(returnCursor.getColumnIndex("rowid"));
            String wtdm=returnCursor.getString(returnCursor.getColumnIndex("WTDM"));
            String wtms=returnCursor.getString(returnCursor.getColumnIndex("WTMS"));
            String uid=returnCursor.getString(returnCursor.getColumnIndex("UID"));

            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("rowid", rowid);
            map.put("wtdm", wtdm);
            map.put("wtms", wtms);
            map.put("uid", uid);
            dataMap.add(map);
        }

        dbHelper.close();

        CustomListAdapter adapter=new CustomListAdapter(MainActivity.this,dataMap,R.layout.item_activity_xcinfo,
                new String[]{"rowid","wtdm","wtms","uid"},new int[]{R.id.lrowid,R.id.lwtdm,R.id.lwtms,R.id.luid});

        xcinfolistView.setAdapter(adapter);

        xcinfolistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ListView lv = (ListView) parent;

                if (lv.getTag() != null){
                    ((View)lv.getTag()).setBackgroundDrawable(null);
                }
                lv.setTag(view);
                view.setBackgroundColor(Color.RED);

                boolean isGraphicExits=false;
                HashMap<String, Object> hashMap = (HashMap<String, Object>) lv.getItemAtPosition(position);// SimpleAdapter返回Map
                String UID=hashMap.get("uid").toString();
                if(CheckUIDGraphicExits(UID)){
                    isGraphicExits=true;
                }
                //ctGraphicLayer.removeAll();
                dbHelper.open();
                Cursor c=dbHelper.queryList("select geometry from graphics where uid='"+UID+"'");
                if(c.getCount()>0){
                    Geometry[] geometries=new Geometry[c.getCount()];
                    Graphic pGraphic=null;
                    HashMap<String, Object> attribute = new HashMap<String, Object>();
                    attribute.put("uid", UID);
                    int k=0;
                    while (c.moveToNext()){
                        String geoString=c.getString(0);
                        String geoType=geoString.split(":")[0];//geoType
                        String geoPointsString=geoString.split(":")[1];//x,y|x,y|x,y

                        if(geoType.equalsIgnoreCase("POINT")){
                            SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 12, STYLE.CIRCLE);
                            String xString=geoPointsString.split(",")[0];//x
                            String yString=geoPointsString.split(",")[1];//y
                            Point point=new Point(Double.valueOf(xString), Double.valueOf(yString));
                            pGraphic=new Graphic(point, sms,attribute);
                            if(!isGraphicExits){
                                ctGraphicLayer.addGraphic(pGraphic);
                            }

                        }else if(geoType.equalsIgnoreCase("POLYLINE") || geoType.equalsIgnoreCase("POLYGON") || geoType.equalsIgnoreCase("FREEPOLYLINE") || geoType.equalsIgnoreCase("FREEPOLYGON")){
                            String[] pointString=geoPointsString.split(";");//x,y
                            MultiPath poly = (geoType.equalsIgnoreCase("POLYLINE") || geoType.equalsIgnoreCase("FREEPOLYLINE")) ? new Polyline() : new Polygon();
                            poly.startPath(Double.valueOf(pointString[0].split(",")[0]), Double.valueOf(pointString[0].split(",")[1]));
                            for(int i=0;i<pointString.length;i++){
                                String xString=pointString[i].split(",")[0];//x
                                String yString=pointString[i].split(",")[1];//y
                                Point point=new Point(Double.valueOf(xString), Double.valueOf(yString));
                                poly.lineTo(point);
                            }
                            if(geoType.equalsIgnoreCase("POLYLINE") || geoType.equalsIgnoreCase("FREEPOLYLINE")){
                                SimpleLineSymbol sls = new SimpleLineSymbol(Color.BLUE, 2);
                                pGraphic=new Graphic(poly, sls,attribute);
                                if(!isGraphicExits){
                                    ctGraphicLayer.addGraphic(pGraphic);
                                }
                            }
                            if(geoType.equalsIgnoreCase("POLYGON") || geoType.equalsIgnoreCase("FREEPOLYGON")){
                                SimpleFillSymbol sfs = new SimpleFillSymbol(Color.RED);
                                sfs.setOutline(new SimpleLineSymbol(Color.BLUE, 2));
                                sfs.setAlpha(50);
                                pGraphic=new Graphic(poly, sfs,attribute);

                                if(!isGraphicExits){
                                    ctGraphicLayer.addGraphic(pGraphic);
                                }
                            }
                        }
                        geometries[k]=pGraphic.getGeometry();
                        k++;
                    }
                    if(geometries.length>0){
                        Geometry geo=GeometryEngine.union(geometries,mMapView.getSpatialReference());
                        mMapView.setExtent(geo);
                    }

                }

                dbHelper.close();
            }
        });

    }

    OnStatusChangedListener mapStatusChangedEvent=new OnStatusChangedListener() {
        @Override
        public void onStatusChanged(Object source, STATUS status) {
            if(source instanceof ArcGISLocalTiledLayer && status == STATUS.LAYER_LOADED){
                removeGraphicLayer(mMapView);
                graphicsLayer = new GraphicsLayer();
                mMapView.addLayer(graphicsLayer);
                linegraphicsLayer = new GraphicsLayer();
                mMapView.addLayer(linegraphicsLayer);
                tempGraphicLayer = new GraphicsLayer();
                mMapView.addLayer(tempGraphicLayer);
                gpsLayer=new GraphicsLayer();
                mMapView.addLayer(gpsLayer);
                ctGraphicLayer=new GraphicsLayer();
                mMapView.addLayer(ctGraphicLayer);
            }

        }
    };

    //---------------------划线测距

    private View.OnClickListener lineBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(poly!=null){
                if (poly.getPointCount() == 2) {
                    poly = null;
                    tempGraphicLayer.removeAll();
                }
            }

            Point centerPoint = mMapView.getCenter();

            Graphic graphicPoint = new Graphic(centerPoint, sms);


            tempGraphicLayer.addGraphic(graphicPoint);
            if (poly == null) {
                poly = new Polyline();
                poly.startPath(centerPoint);

            } else {
                poly.lineTo(centerPoint);
                graphicLine = new Graphic(poly, sls);
                graphicID = linegraphicsLayer.addGraphic(graphicLine);
            }

            if (poly.getPointCount() == 2) {
                double distance=0;

                try{
                    distance=GeometryEngine.distance(poly.getPoint(0),poly.getPoint(1),mMapView.getSpatialReference());
                }
                catch(Exception e){
                    distance=0;
                }

                showLineDistanceDialog(MainActivity.this,distance);
            }
        }
    };
//----------------给线赋值
    private void showLineDistanceDialog(Context ctx,double distance) {
        LinearLayout.LayoutParams LP_FF = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ScrollView sv = new ScrollView(ctx);
        sv.setLayoutParams(LP_FF);

        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);
        sv.addView(layout);

        TextView tv;
        tv = new TextView(ctx);// TextView
        tv.setText("实测距离:");
        tv.setTextAppearance(ctx, android.R.style.TextAppearance_Medium);
        //layout.addView(tv);

        final EditText et = new EditText(ctx);
        // et.setLayoutParams(lp);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);//只能输入数字和小数点
        et.setSingleLine(true);
        et.setTextAppearance(ctx, android.R.style.TextAppearance_Medium);
        // et.setTextColor(0xFFFFFFFF);
        et.setText(new java.text.DecimalFormat("0.00").format(distance));
        et.setHint("请输入实测距离(米)");
        layout.addView(et);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setView(sv).setTitle("记录实测距离（米）").setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if(et.getText().toString().length()>0){
                    dbHelper.open();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("XMH", UtilTool.getXMH(MainActivity.this));
                    contentValues.put("PCH", UtilTool.getPCH(MainActivity.this));
                    contentValues.put("YBH", UtilTool.getYBH(MainActivity.this));
                    contentValues.put("X0",poly.getPoint(0).getX());
                    contentValues.put("Y0",poly.getPoint(0).getY());
                    contentValues.put("X1",poly.getPoint(1).getX());
                    contentValues.put("Y1",poly.getPoint(1).getY());
                    contentValues.put("Length",et.getText().toString());
                    dbHelper.insert("N7_Record", contentValues);

                    dbHelper.close();

                    poly = null;
                    tempGraphicLayer.removeAll();
                    // ------------------------------------
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, true); // true -
                        // 使之可以关闭(此为机关所在，其它语句相同)
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"请输入实测距离！",Toast.LENGTH_SHORT).show();
                    // 条件不成立不能关闭 AlertDialog 窗口
                    Toast.makeText(MainActivity.this, "类型编码不能为空！", Toast.LENGTH_LONG).show();
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, false); // false -
                        // 使之不能关闭(此为机关所在，其它语句相同)
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                linegraphicsLayer.removeGraphic(graphicID);
                poly = null;
                tempGraphicLayer.removeAll();
                dialog.cancel();
            }
        }).setCancelable(false).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenus, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//标题栏工具条Menu
        if (item.getItemId()==R.id.action_layers_bar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择外业核查样本");
            builder.setSingleChoiceItems(tfName, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                    removeBaseMap(mMapView);
                    String baseMapUrl = "file://" + tpkFileList[which].getAbsolutePath();
                    txt_YBH.setText("当前样本：" + tfName[which]);
                    appData.setAppData("YBH",tpkName[which],MainActivity.this);

                    LoadLocalTiledMapService(mMapView, baseMapUrl, 0);
                    String dbPath=UtilTool.getDBPath(MainActivity.this);
                    dbHelper=new DBHelper(MainActivity.this,dbPath);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (item.getItemId()==R.id.action_feature_bar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择外业核查人员");
            builder.setSingleChoiceItems(checkPerson, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                    txt_JCR.setText("检查人：" + checkPerson[which]);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        //------------量测

        if (item.getItemId()==R.id.action_measure_bar) {
            measureTypes = new String[] { "距离", "面积", "退出" };// "坐标"
            if (measureTool == null) {
                measureTool = new MeasureTool(getBaseContext(), mMapView);
            }
            showDialog(1);
        }
        //-------------GPS
        if (item.getItemId()==R.id.action_location_bar) {
            if(gpsModel==null){
                gpsModel=new GPSModel(MainActivity.this,3);//每3秒更新一次位置
                try {
                    gpsModel.OpenGPS();
                    btnLocate.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        //-------------Clear
        if (item.getItemId()==R.id.action_clear_bar) {
            if(measureTool!=null){
                measureTool.cancelDraw();
                measureTool.removeAll();
                measureTool=null;
            }
        }
        //------------草图菜单
        if (item.getItemId()==R.id.action_ct_bar){
            LinearLayout drawtools=(LinearLayout)this.findViewById(R.id.drawtools);
            if(drawtools.getVisibility()==View.VISIBLE){
                if(drawTool!=null){
                    drawTool.cancelDraw();
                }
                RestoreDrawTool();
                drawtools.setVisibility(View.GONE);
                ctGraphicLayer.removeAll();
            }else{
                drawtools.setVisibility(View.VISIBLE);
                InitDrawLayer();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 2) {// GPS
            try {
                if (gpsModel != null) {
                    gpsModel.OpenGPS();
                    btnLocate.setVisibility(View.VISIBLE);

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    protected Dialog onCreateDialog(int id){
        if(id==1){//----量测
            return new AlertDialog.Builder(MainActivity.this).setTitle("选择量测类型").setItems(measureTypes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
                    String measureType = measureTypes[which];
                    if (measureType.equalsIgnoreCase("坐标")) {
                        measureTool.drawPoint();
                        toast.setText("您选择了坐标工具 \n 点击屏幕显示坐标");
                    } else if (measureType.equalsIgnoreCase("距离")) {
                        measureTool.drawLine();
                        toast.setText("您选择了距离工具 \n 点击屏幕开始量测");
                    } else if (measureType.equalsIgnoreCase("面积")) {
                        measureTool.drawPolygon();
                        toast.setText("您选择了面积工具 \n 点击屏幕开始量测");
                    } else if (measureType.equalsIgnoreCase("退出")) {
                        measureTool.cancelDraw();
                        toast.setText("量测工具已关闭");
                    }
                    toast.show();
                }
            }).create();
        }
        return null;
    }

    //=========================================加载SD卡中的切片服务
    private void LoadLocalTiledMapService(MapView mapView, String mapDataUrl, int index) {
        ArcGISLocalTiledLayer pArcGISLocalTiledLayer = new ArcGISLocalTiledLayer(mapDataUrl);
        //pArcGISLocalTiledLayer.reinitializeLayer(mapDataUrl);
        Envelope ev=pArcGISLocalTiledLayer.getFullExtent();
        mapView.setMaxExtent(ev);
        mapView.setExtent(ev);
        Point pCenter=new Point();
        pCenter.setXY(ev.getCenterX(),ev.getCenterY());
        mapView.addLayer(pArcGISLocalTiledLayer, index);
        mapView.centerAt(pCenter,false);
    }

    //========================清空底图
    private void removeBaseMap(MapView mapView) {
        for (int k = 0; k < mapView.getLayers().length; k++) {
            Layer mLayer = mapView.getLayer(k);
            if (!(mLayer instanceof GraphicsLayer)) {
                if (mLayer.getUrl().indexOf(".tpk") > 0) {
                    mapView.removeLayer(k);
                    k--;
                }
            }
        }
    }

    private void removeGraphicLayer(MapView mapView) {
        for (int k = 0; k < mapView.getLayers().length; k++) {
            Layer mLayer = mapView.getLayer(k);
            if (mLayer instanceof GraphicsLayer) {
                mapView.removeLayer(k);
            }
        }
    }

    //-----------删除选中行
    @Override
    public void ResponseSelectRow(final int id) {
        new CustomDialog.Builder(this).setTitle("提示").setMessage("确认删除序号为 "+id+" 的记录吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if(xcinfolist.getVisibility()==View.VISIBLE){
                            dbHelper.open();
                            Cursor c=dbHelper.queryList("Select uid from N5_Record where rowid ="+String.valueOf(id));
                            String uid="";
                            if(c.moveToFirst()){
                                uid=c.getString(0);
                                dbHelper.execSQL("delete from graphics where uid='"+uid+"'");
                            }
                            dbHelper.execSQL("delete from N5_Record where rowid ="+String.valueOf(id));
                            dbHelper.close();

                            int[] gs=ctGraphicLayer.getGraphicIDs();
                            for(int i=0;i<gs.length;i++){
                                Graphic g=ctGraphicLayer.getGraphic(gs[i]);
                                if(g.getAttributeValue("uid").toString().equalsIgnoreCase(uid)){
                                    ctGraphicLayer.removeGraphic(gs[i]);
                                }
                            }

                            QueryXCInfo();
                        }else{
                            dbHelper.open();
                            dbHelper.execSQL("delete from N7_Record where rowid ="+String.valueOf(id));
                            dbHelper.close();
                            QueryBCInfo();
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void LocateFinish(Location location) {

        try{
            myPoint = new Point(location.getLongitude(), location.getLatitude());
            myPoint=(Point)GeometryEngine.project(myPoint,SpatialReference.create(4326),mMapView.getSpatialReference());
            if(mMapView.getMaxExtent().contains(myPoint)){
                if (smsMarkerSymbol == null) {
                    SimpleLineSymbol slsLineSymbol = new SimpleLineSymbol(Color.BLACK, Float.parseFloat("1.5"));
                    Random random = new Random();
                    int[] ranColor = {Color.RED};
                    smsMarkerSymbol = new SimpleMarkerSymbol(ranColor[random.nextInt(ranColor.length)], 15, SimpleMarkerSymbol.STYLE.CIRCLE);
                    smsMarkerSymbol.setOutline(slsLineSymbol);
                }

                Graphic graphic = new Graphic(myPoint, smsMarkerSymbol);
                if (gpsGraphicID == 0) {
                    gpsGraphicID = gpsLayer.addGraphic(graphic);
                } else {
                    gpsLayer.updateGraphic(gpsGraphicID, myPoint);
                }

                if (gzBoolean) {
                    mMapView.centerAt(myPoint, true);
                    mMapView.setScale(500);
                }
            }else{
                Toast.makeText(getApplicationContext(), "您的位置不在地图范围内！", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "当前地图无法转换GPS坐标！", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void ResponseUID(String uid) {
        SetUIDNullGraphics(uid);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (gpsModel != null) {
                gpsModel.CloseGPS();
                btnLocate.setVisibility(View.GONE);
                gpsModel=null;
                gpsLayer.removeAll();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onBackPressed() {
//
//    }

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
