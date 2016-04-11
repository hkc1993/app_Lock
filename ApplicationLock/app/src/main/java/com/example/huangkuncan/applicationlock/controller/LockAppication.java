package com.example.huangkuncan.applicationlock.controller;

import android.app.Application;

import com.example.huangkuncan.applicationlock.database.LockDataBase;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能：
 */
public class LockAppication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化配置
        LockDataBase.initConfig(getApplicationContext());
        LockAppManager.initConfig(getApplicationContext());

    }
}
