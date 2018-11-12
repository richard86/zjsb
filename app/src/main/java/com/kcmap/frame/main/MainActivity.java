package com.kcmap.frame.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.runtime.ArcGISRuntime;
import com.kcmap.frame.R;
import com.kcmap.frame.utils.AppManager;

import java.io.File;



public class MainActivity extends AppCompatActivity {

    String userName;
    File workingDirectory;

    MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArcGISRuntime.setClientId("uK0DxqYT0om1UXa9");
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);

        // --------------------------------------初始化工作目录
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userName = bundle.getString("userName");
        String workDirectory = bundle.getString("workPathString");
        workingDirectory = new File(workDirectory);// 工作目录

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setMapBackground(Color.WHITE, Color.WHITE, 0, 0);

        // 基础数据
        String baseMapUrl = "file://" + workingDirectory.getPath() + "/map/影像图";
        ArcGISLocalTiledLayer baseMapLayer = new ArcGISLocalTiledLayer(baseMapUrl);
        mMapView.addLayer(baseMapLayer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenus, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//标题栏工具条Menu
        if(item.getTitle().equals("位置")) {

        }
        if(item.getTitle().equals("清空")) {

        }
        if(item.getTitle().equals("量测")) {

        }
        if(item.getTitle().equals("草图")) {

        }
        if(item.getTitle().equals("编辑")) {

        }
        if(item.getTitle().equals("图层")) {

        }
        return super.onOptionsItemSelected(item);
    }
}
