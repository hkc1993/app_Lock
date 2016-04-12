package com.example.huangkuncan.applicationlock.controller;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.DivideEditText;
import com.example.huangkuncan.applicationlock.View.View.TopView;

import java.io.IOException;

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
    private View mLockView;
    private TopView mTopView;
    private WindowManager.LayoutParams mLayoutParams;
    //是否已经弹出了悬浮窗
    private boolean mHaveShowWindow;
    //密码输入框
    private DivideEditText mEditText;
    //将自己的app设置为白名单
    private static final String WhiteApp = "com.example.huangkuncan.applicationlock";
    //正在被解锁的app
    private String unLockingApp;

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
        mLockView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.full_screen_input_password, null);
        mLockView.requestFocus();
        mLockView.requestFocusFromTouch();
        mTopView = (TopView) mLockView.findViewById(R.id.full_screen_input_password_topview);
        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
                Log.d(TAG, "onClick: ");
            }
        });
        mLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800,
                2002, WindowManager.LayoutParams.FLAG_SCALED, PixelFormat.TRANSPARENT);
        mEditText = (DivideEditText) mLockView.findViewById(R.id.full_screen_input_password_et);
        mLayoutParams.gravity = Gravity.CENTER;
    }

    /**
     * 模拟返回键
     */
    private void back() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void getActivity() {
        mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
        ActivityManager.RunningTaskInfo info = mActivityManager.getRunningTasks(1).get(0);
        topPackageName = topActivity.getPackageName();
        //跳过白名单
        if (!topPackageName.equals(WhiteApp)) {
            if (LockAppManager.getInstance().isChoosed(topPackageName)) {
                if (unLockingApp == null) {
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
