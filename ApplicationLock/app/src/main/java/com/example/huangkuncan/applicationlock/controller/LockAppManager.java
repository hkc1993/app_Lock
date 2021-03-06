package com.example.huangkuncan.applicationlock.controller;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.example.huangkuncan.applicationlock.database.LockDataBase;
import com.example.huangkuncan.applicationlock.model.LockAppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkuncan on 2016/4/11.
 * 邮箱：673391138@qq.com
 * 功能：所有app的管理
 */
public class LockAppManager {
    private static Context mContext;
    //从系统获取的所有的app信息
    private List<PackageInfo> list;
    //从数据库获取的已经被加锁的app
    private List<String> listPackages=new ArrayList<String>();

    private LockAppManager() {
        list = mContext.getPackageManager().getInstalledPackages(0);
        listPackages = LockDataBase.getInstance().getAll();
    }

    public static void initConfig(Context context) {
        mContext = context;
    }

    private static LockAppManager Instance;

    public static LockAppManager getInstance() {
        if (Instance == null) {
            Instance = new LockAppManager();
        }
        return Instance;
    }

    /**
     * 获取所有的app信息，包括app是否已经被加锁的信息
     */
    public List<LockAppInfo> getPackageInfos() {
        List<LockAppInfo> lockList = new ArrayList<LockAppInfo>();
        int length = list.size();
        LockAppInfo info;
        for (int i = 0; i < length; i++) {
            info = new LockAppInfo();
            if (isChoosed(list.get(i))) {
                info.isChoosed = true;
            } else {
                info.isChoosed = false;
            }
            info.packageInfo = list.get(i);
            lockList.add(info);
        }
        return lockList;
    }

    /**
     * @param packageName 将一个app变成加锁状态
     */
    public void lock(String packageName) {
        LockDataBase.getInstance().add(packageName);
    }

    /**
     * @param packageName 将多个app变成加锁状态
     */
    public void lock(List<String> packageName) {
        if (packageName == null || packageName.size() == 0) return;
        LockDataBase.getInstance().add(packageName);
    }

    /**
     * @param packageName 将一个app从加锁状态改成不加锁状态
     */
    public void unlock(String packageName) {
        LockDataBase.getInstance().delete(packageName);
    }

    /**
     * @param packageName 将多个app从加锁状态改成不加锁状态
     */
    public void unlock(List<String> packageName) {
        if (packageName == null || packageName.size() == 0) return;
        LockDataBase.getInstance().delete(packageName);
    }

    /**
     * @param info
     * @return 判断某个app包是否已经被加锁了
     */
    private boolean isChoosed(PackageInfo info) {
        if (listPackages == null && listPackages.size() == 0) {
            return false;
        }
        int length = listPackages.size();
        for (int i = 0; i < length; i++) {
            if (info.packageName.equals(listPackages.get(i)))
                return true;
        }
        return false;
    }
    /**
     *
     * @return 判断某个app包是否已经被加锁了
     */
    public boolean isChoosed(String packageName) {
        if (listPackages == null && listPackages.size() == 0) {
            return false;
        }
        int length = listPackages.size();
        for (int i = 0; i < length; i++) {
            if (packageName.equals(listPackages.get(i)))
                return true;
        }
        return false;
    }

}
