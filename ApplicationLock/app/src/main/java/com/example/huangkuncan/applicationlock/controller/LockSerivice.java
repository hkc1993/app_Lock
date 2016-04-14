package com.example.huangkuncan.applicationlock.controller;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.DivideEditText;
import com.example.huangkuncan.applicationlock.View.View.LockView;
import com.example.huangkuncan.applicationlock.View.View.TopView;

import java.io.IOException;
import java.util.List;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能： 后台服务，监测软件的启动和安装
 */
public class LockSerivice extends Service {
	private Handler handler = new Handler();
	// 轮询的runnable
	private Runnable pollingRunnable;
	//轮询的间隔
	private static final long interval = 1000;
	private ActivityManager mActivityManager;
	//栈顶的acitivity
	private ComponentName topActivity;
	private String topPackageName;
	private static final String TAG = "hkc";
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	//是否已经弹出了悬浮窗
	private boolean mHaveShowWindow;
	//将自己的app设置为白名单
	private static final String WhiteApp = "com.example.huangkuncan.applicationlock";
	//正在被解锁的app
	private String unLockingApp;
	//解锁界面
	LockView mLockView;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
		initPopView();
		handler.post(pollingRunnable);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public static void startSerivice(Context context) {
		Intent intent = new Intent(context, LockSerivice.class);
		context.startService(intent);
	}

	public static void stopSerivice(Context context) {
		Intent intent = new Intent(context, LockSerivice.class);
		context.stopService(intent);
	}

	private void init() {
		pollingRunnable = new Runnable() {
			@Override
			public void run() {
				getActivity();
				handler.postDelayed(this, interval);
			}
		};
	}

	private void initPopView() {
		mLockView = new LockView(this);
		mLockView.addTextInputConpleteListener(new LockView.LockViewListener() {
			@Override
			public void textInputComplete(String text) {
				if (text.equals(LockStore.getInstance().getPassword())) {
					removePopView();
				} else {
					mLockView.setTopText(getString(R.string.lock_view_remind_input_again));
				}
			}
		});
		mLockView.setTopText(getString(R.string.lock_view_remind_input_password));
		mLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
				2002, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSPARENT);
		mLayoutParams.gravity = Gravity.CENTER;
	}


	/**
	 * 关闭输入密码的悬浮窗
	 */
	private void removePopView() {
		if (mLockView != null && mHaveShowWindow) {
			mWindowManager.removeView(mLockView);
			mHaveShowWindow = false;
		}
	}

	/**
	 * 弹出输入密码的悬浮窗
	 */
	private void showPopView() {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		}
		if (!mHaveShowWindow) {
			mWindowManager.addView(mLockView, mLayoutParams);
			mHaveShowWindow = true;
		}

	}

	/**
	 * 设置弹出的文案与图标
	 */
	private void setViewText() {
		List<PackageInfo> list;
		list = this.getPackageManager().getInstalledPackages(0);
		int length = list.size();
		for (int i = 0; i < length; i++) {
			if (list.get(i).packageName.equals(topPackageName)) {
				//图标
				mLockView.setAppDrawable(list.get(i).applicationInfo.loadIcon(getPackageManager()));
				//app的名字
				mLockView.setAppName(list.get(i).applicationInfo.loadLabel(getPackageManager()) + "");
			}
		}
	}

	/**
	 * 获取栈顶的activity
	 */
	private void getActivity() {
		mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
		ActivityManager.RunningTaskInfo info = mActivityManager.getRunningTasks(1).get(0);
		topPackageName = topActivity.getPackageName();
		Log.d(TAG, "getActivity: " + topPackageName);
		//跳过白名单
		if (!topPackageName.equals(WhiteApp)) {
			if (LockAppManager.getInstance().isChoosed(topPackageName)) {
				if (unLockingApp == null) {
					setViewText();
					//弹出
					showPopView();
					unLockingApp = topPackageName;
				} else if (topPackageName.equals(unLockingApp)) {
					//已经弹出解锁页面，不需要继续弹
//                    showPopView();
				} else {
					//待解app已经关闭，关闭解锁界面
					removePopView();
					unLockingApp = null;
				}
			}
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
