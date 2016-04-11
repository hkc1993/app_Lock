package com.example.huangkuncan.applicationlock.controller;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.huangkuncan.applicationlock.View.LockActivity;

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
    private ComponentName topActivity;
    private String topPackageName;
    private static final String TAG="hkc";
   //将自己的app设置为白名单
    private static final String WhiteApp="com.example.huangkuncan.applicationlock";
    @Override
    public void onCreate() {
        super.onCreate();
        init();
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
   public static void stopSerivice(Context context){
       Intent intent=new Intent(context,LockSerivice.class);
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

    private void getActivity() {
        mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
        topPackageName = topActivity.getPackageName();
        Log.d("hkc", "getActivity: 报名  " + topPackageName);
        LockActivity.startActivity(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
