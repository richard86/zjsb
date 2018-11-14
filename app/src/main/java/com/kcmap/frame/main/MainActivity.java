package com.kcmap.frame.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.kcmap.frame.R;
import com.kcmap.frame.utils.AppManager;

import java.io.File;
import java.io.FileFilter;


public class MainActivity extends AppCompatActivity {

    String userName;
    File workingDirectory;
    //样本tpk
    File[] tpkFileList;
    String[] tpkName;
    String[] checkPerson;
    MapView mMapView;

    TextView pcTextView;
    TextView ybTextView;
    TextView jcrTextView;


    GraphicsLayer graphicsLayer;
    GraphicsLayer linegraphicsLayer;
    GraphicsLayer tempGraphicLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);

        // --------------------------------------初始化工程目录
        final Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userName = bundle.getString("userName");

        checkPerson = new String[]{"张三", "李四", "王五"};//检查人员


        String workDirectory = bundle.getString("workPathString");
        workingDirectory = new File(workDirectory);// 工程目录

        //------获取批次目录路径
        String[] fileList = workingDirectory.list();
        File pcFolder = null;
        for (int f = 0; f < fileList.length; f++) {
            if (fileList[f].endsWith("批次")) {
                pcFolder = new File(workDirectory + File.separator + fileList[f]);
                break;
            }
        }

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

        //----------设置infotab透明
        LinearLayout lllLayout = (LinearLayout) findViewById(R.id.infotab);
        lllLayout.getBackground().mutate().setAlpha(180);

        //--------------初始化控件
        pcTextView = (TextView) findViewById(R.id.pcnum);
        ybTextView = (TextView) findViewById(R.id.ybnum);
        jcrTextView = (TextView) findViewById(R.id.jcry);

        ImageView collectButton = (ImageView) findViewById(R.id.collect);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Point centerPoint = mMapView.getCenter();
                Graphic graphic = new Graphic(centerPoint, new SimpleMarkerSymbol(Color.RED, 9, STYLE.CIRCLE));
                graphicsLayer.addGraphic(graphic);

                Intent intent1 = new Intent(MainActivity.this, XcInfoMainActivity.class);
                startActivity(intent1);

            }
        });

        ImageView lineButton = (ImageView) findViewById(R.id.line);
        lineButton.setOnClickListener(lineBtnOnClickListener);

        if (tpkFileList != null) {
            tpkName = new String[tpkFileList.length];
            for (int i = 0; i < tpkFileList.length; i++) {
                tpkName[i] = tpkFileList[i].getName().substring(0, tpkFileList[i].getName().indexOf(".tpk"));
            }
            LoadLocalTiledMapService(mMapView, "file://" + tpkFileList[0].getAbsolutePath(), 0);
            graphicsLayer = new GraphicsLayer();
            mMapView.addLayer(graphicsLayer);
            linegraphicsLayer = new GraphicsLayer();
            mMapView.addLayer(linegraphicsLayer);
            tempGraphicLayer = new GraphicsLayer();
            mMapView.addLayer(tempGraphicLayer);
            ybTextView.setText("当前样本：" + tpkName[0]);
        }
    }

    //---------------------划线测距
    private MultiPath poly;
    private SimpleLineSymbol sls;
    private SimpleMarkerSymbol sms;
    private Graphic graphicLine;
    private int graphicID;
    private View.OnClickListener lineBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Point centerPoint = mMapView.getCenter();
            sms = new SimpleMarkerSymbol(Color.RED, 9, STYLE.CIRCLE);
            Graphic graphicPoint = new Graphic(centerPoint, sms);


            tempGraphicLayer.addGraphic(graphicPoint);
            if (poly == null) {
                poly = new Polyline();
                poly.startPath(centerPoint);
                sls = new SimpleLineSymbol(Color.BLUE, 2);


            } else {
                poly.lineTo(centerPoint);
                graphicLine = new Graphic(poly, sls);
                graphicID = linegraphicsLayer.addGraphic(graphicLine);
            }

            if (poly.getPointCount() == 2) {
                poly = null;
                tempGraphicLayer.removeAll();
                showLineDistanceDialog(MainActivity.this);
            }
        }
    };
//----------------给线赋值
    private void showLineDistanceDialog(Context ctx) {
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

        EditText et;
        et = new EditText(ctx);
        // et.setLayoutParams(lp);
        et.setSingleLine(true);
        et.setTextAppearance(ctx, android.R.style.TextAppearance_Medium);
        // et.setTextColor(0xFFFFFFFF);
        et.setHint("请输入实测距离");
        layout.addView(et);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(sv).setTitle("记录实测距离（米）").setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                linegraphicsLayer.removeGraphic(graphicID);
            }
        }).create().show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenus, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//标题栏工具条Menu
        if (item.getTitle().equals("切换样本")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择外业核查样本");
            builder.setSingleChoiceItems(tpkName, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                    removeBaseMap(mMapView);
                    String baseMapUrl = "file://" + tpkFileList[which].getAbsolutePath();
                    ybTextView.setText("当前样本：" + tpkName[which]);
                    LoadLocalTiledMapService(mMapView, baseMapUrl, 0);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (item.getTitle().equals("检查人员")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择外业核查人员");
            builder.setSingleChoiceItems(checkPerson, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                    jcrTextView.setText("检查人：" + checkPerson[which]);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

    //=========================================加载SD卡中的切片服务
    private void LoadLocalTiledMapService(MapView mapView, String mapDataUrl, int index) {
        ArcGISLocalTiledLayer pArcGISLocalTiledLayer = new ArcGISLocalTiledLayer(mapDataUrl);
        mapView.addLayer(pArcGISLocalTiledLayer, index);
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
}
