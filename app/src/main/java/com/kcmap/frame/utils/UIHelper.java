package com.kcmap.frame.utils;

import com.kcmap.frame.R;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 界面工具???
 * 
 * @author zhangxinhe
 * @date 2013-05-10
 */
public class UIHelper {

	/**
	 * show DialogLoading
	 * 
	 * @param mProgressDialog
	 */
	public static void showDialogLoading(ProgressDialog mProgressDialog) {
		if (mProgressDialog != null) {
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// spinner 自旋??? 像螺旋桨那样
			mProgressDialog.setMessage("请稍等...");
			mProgressDialog.setIndeterminate(false);// 设置进度条是否为不明???
			mProgressDialog.setCancelable(true);// 设置进度条是否可以按???回健取消
			mProgressDialog.show();
			mProgressDialog.setContentView(R.layout.layout_progress);
		}

	}

	/**
	 * show DialogLoading
	 * 
	 * @param mProgressDialog
	 */
	public static void showDialogLoading(ProgressDialog mProgressDialog, String msg) {
		if (mProgressDialog != null) {
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// spinner 自旋??? 像螺旋桨那样
			mProgressDialog.setMessage(msg);
			mProgressDialog.setIndeterminate(false);// 设置进度条是否为不明???
			mProgressDialog.setCancelable(true);// 设置进度条是否可以按???回健取消
			mProgressDialog.show();
			mProgressDialog.setContentView(R.layout.layout_progress);
		}

	}

	/**
	 * 关闭进度???
	 */
	public static void unShowDialogLoading(ProgressDialog mProgressDialog) {
		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	}

}
