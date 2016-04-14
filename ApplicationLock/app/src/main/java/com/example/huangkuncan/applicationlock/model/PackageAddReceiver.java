package com.example.huangkuncan.applicationlock.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.controller.LockAppication;

import java.util.List;

/**
 * Created by huangkuncan on 2016/4/14.
 * 邮箱：673391138@qq.com
 * 功能：监听软件的安装
 */
public class PackageAddReceiver extends BroadcastReceiver {
	private WindowManager mWindowManger;
	private WindowManager.LayoutParams mLayoutParams;
	private View mDialogView;
	private TextView mTextAdd;
	private TextView mTextCancel;
	private TextView mTextAppName;
	private static final String TAG = "hkc";
	//先安装的app的包名
	private String mNewAddAppName;

	public PackageAddReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("hkc", "onReceive: 得到的intent " + intent);
		if (intent.getAction() == "android.intent.action.PACKAGE_ADDED") {
			mNewAddAppName = intent.getDataString();
			initView();
			setViewText();
			show();
		}
	}

	private void initView() {
		mWindowManger = (WindowManager) LockAppication.context.getSystemService(Context.WINDOW_SERVICE);
		mDialogView = LayoutInflater.from(LockAppication.context).inflate(R.layout.dialog_add_lock, null);
		mTextAdd = (TextView) mDialogView.findViewById(R.id.dialog_add_lock_add);
		mTextCancel = (TextView) mDialogView.findViewById(R.id.dialog_add_lock_cancel);
		mTextAppName = (TextView) mDialogView.findViewById(R.id.dialog_add_lock_appName);
		mLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300,
				2002, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSPARENT);
		mLayoutParams.gravity = Gravity.CENTER;
		setOnClickListener();
	}

	/**
	 * 设置弹出的文案与图标
	 */
	private void setViewText() {
		List<PackageInfo> list;
		list = LockAppication.context.getPackageManager().getInstalledPackages(0);
		int length = list.size();
		for (int i = 0; i < length; i++) {
			if (list.get(i).packageName.equals(mNewAddAppName)) {
				//app的名字
				mTextAppName.setText(list.get(i).applicationInfo.loadLabel(LockAppication.context.getPackageManager()) + "");
			}
		}
	}

	private void setOnClickListener() {
		mTextAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismisss();
			}
		});
		mTextCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismisss();
			}
		});
	}

	private void show() {
		Log.d("hkc", "show: ");
		mWindowManger.addView(mDialogView, mLayoutParams);
	}

	private void dismisss() {
		mWindowManger.removeView(mDialogView);
	}
}
