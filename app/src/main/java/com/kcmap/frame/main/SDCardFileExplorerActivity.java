package com.kcmap.frame.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kcmap.frame.R;
import com.kcmap.frame.appData.AppData;
import com.kcmap.frame.utils.AppManager;

public class SDCardFileExplorerActivity extends Activity {

	private TextView tvpath;
	private ListView lvFiles;

	// 记录当前的父文件夹
	File currentParent;

	// 记录当前路径下的所有文件夹的文件数组
	File[] currentFiles;
	AppData appData;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_dir);
        AppManager.getAppManager().addActivity(this);
		appData=new AppData();
		lvFiles = (ListView) this.findViewById(R.id.files);

		tvpath = (TextView) this.findViewById(R.id.tvpath);

		// 获取系统的SDCard的目录
		Boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if(!sdCardExist){
			Toast.makeText(this, "请保证已插上手持设备的SD卡!", Toast.LENGTH_LONG).show();
			System.exit(0);
		}

		File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		// 如果SD卡存在的话
		if (root.exists() && root.canRead() && root.isDirectory()) {
			if(root.listFiles()!=null){
				currentParent = root;
				currentFiles = root.listFiles();
			}else {
				currentParent = new File(root.getParent());
				currentFiles = currentParent.listFiles();
			}

			//		if(root.getParent()!=null){
			//			currentParent = new File(root.getParent());
			//			currentFiles = currentParent.listFiles();
			//		}else{
			//			if(root.listFiles()!=null){
			//				currentParent = root;
			//				currentFiles = root.listFiles();
			//			}
			//		}

			if(currentFiles==null){
				currentParent = new File(currentParent.getParent());
				currentFiles = currentParent.listFiles();
			}

			// 使用当前目录下的全部文件、文件夹来填充ListView
			inflateListView(currentFiles);

		}

		lvFiles.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
									int position, long id) {
				// 如果用户单击了文件，直接返回，不做任何处理
				if (currentFiles[position].isFile()) {
					// 也可自定义扩展打开这个文件等
					return;
				}
				// 获取用户点击的文件夹 下的所有文件
				File[] tem = currentFiles[position].listFiles();
				if (tem == null || tem.length == 0) {

					Toast.makeText(SDCardFileExplorerActivity.this,
							"当前路径不可访问或者该路径下没有文件", Toast.LENGTH_LONG).show();

				} else {
					// 获取用户单击的列表项对应的文件夹，设为当前的父文件夹
					currentParent = currentFiles[position];
					// 保存当前的父文件夹内的全部文件和文件夹
					currentFiles = tem;
					// 再次更新ListView
					inflateListView(currentFiles);
				}

			}
		});
	}

	/**
	 * 根据文件夹填充ListView
	 *
	 * @param files
	 */
	private void inflateListView(File[] files) {

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < files.length; i++) {

			Map<String, Object> listItem = new HashMap<String, Object>();

			if (files[i].isDirectory()) {
				listItem.put("icon", R.drawable.folder);
			} else {
				listItem.put("icon", R.drawable.file);
			}

			listItem.put("filename", files[i].getName());
			listItems.add(listItem);

		}

		SimpleAdapter adapter = new SimpleAdapter(
				SDCardFileExplorerActivity.this, listItems, R.layout.list_item_dir,
				new String[] { "filename", "icon"}, new int[] {
				R.id.file_name, R.id.icon});

		// 填充数据集
		lvFiles.setAdapter(adapter);

		try {
			tvpath.setText("当前路径为:" + currentParent.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_dir, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//标题栏工具条Menu
		if(item.getTitle().equals("设置工程目录")){
			try {
				String workPathString=currentParent.getCanonicalPath();
				String tagFileString=workPathString+"/工程目录.prj";
				if((new File(tagFileString)).exists()){
					appData.setAppData("workPath", workPathString, this);
					setResult(0);
					finish();
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(SDCardFileExplorerActivity.this);
					builder.setTitle("提示")
							.setMessage("工程目录设置出错，请重新选择工程目录！")
							.setIcon(R.drawable.icon)
							.setPositiveButton("确定",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									return;
								}
							});
					Dialog alert = builder.create();
					alert.show();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (currentParent.getParentFile() != null) {
				// 获取上一级目录
				currentParent = currentParent.getParentFile();
				// 列出当前目录下的所有文件
				currentFiles = currentParent.listFiles();
				// 再次更新ListView
				inflateListView(currentFiles);
			}else{
				Alertdialog();
			}

		}
		return false;
	}

	//============================================退出程序提醒
	private void Alertdialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(SDCardFileExplorerActivity.this);
		builder.setTitle("提示")
				.setMessage("确定要退出程序？")
				.setIcon(R.drawable.icon)
				.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						AppManager.getAppManager().exitApp();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		});
		Dialog alert = builder.create();
		alert.show();
	}
}