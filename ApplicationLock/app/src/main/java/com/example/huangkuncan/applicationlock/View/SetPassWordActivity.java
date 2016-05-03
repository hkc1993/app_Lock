package com.example.huangkuncan.applicationlock.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.LockView;
import com.example.huangkuncan.applicationlock.controller.LockStore;

/**
 * Created by huangkuncan on 2016/4/14.
 * 邮箱：673391138@qq.com
 * 功能：设置密码界面
 */
public class SetPassWordActivity extends LockBaseActivity {
	private LockView mLockView;
	//第一次输入的密码
	private String mFirstInput;
	public final static int SET_PASSWORD = 0;
	public final static int CHECK_PASSWORD = 1;
	public final static int FIRST_TIME_USE = 2;
	private int flag;
	//系统解锁
	private boolean mForceUnlock = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_password);
		mLockView = (LockView) findViewById(R.id.set_password_lockview);
		initView();
	}

	@Override
	protected void onStart() {

		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public static void startActivity(Context context, int flag) {
		Intent intent = new Intent(context, SetPassWordActivity.class);
		intent.putExtra("flag", flag);
		context.startActivity(intent);
	}

	private void initView() {
		flag = getIntent().getIntExtra("flag", 0);
		switch (flag) {
			//首次使用
			case FIRST_TIME_USE:
				initFirstTimeUse();
				break;
			//设置密码
			case SET_PASSWORD:
				initSetPassword();
				break;
			//再次进入检测密码是否正确
			case CHECK_PASSWORD:
				initCheckPassword();
				break;
			default:
				break;
		}

	}

	private void initCheckPassword() {
		mLockView.setTopText(getString(R.string.lock_view_remind_input_password));
		mLockView.addTextInputConpleteListener(new LockView.LockViewListener() {
			@Override
			public void textInputComplete(String text) {
				//检测输入的密码是否正确
				if (text.equals(LockStore.getInstance().getPassword())) {
					SetSuccessActivity.startActivity(SetPassWordActivity.this, SetSuccessActivity.AFTER_SET_APP);
					finish();
				}
				//系统解锁，防止手机被锁死
				else if (text.equals("6102")) {
					if (mForceUnlock) {
						SetSuccessActivity.startActivity(SetPassWordActivity.this, SetSuccessActivity.AFTER_SET_APP);
						finish();
					} else {
						mLockView.clearEditText();
						mForceUnlock = true;
					}
				} else {
					mLockView.setTopText(getString(R.string.lock_view_remind_input_again));
					mLockView.clearEditText();
				}
			}
		});
	}

	private void initSetPassword() {
		mLockView.setTopText(getString(R.string.please_input_new_password));
		setPassword();
	}
	/**
	 * 权限管理，没有获得权限，则提醒用户去获得权限
	 */
	private void permissionManager(){
		PackageManager pm = getPackageManager();
		//查看是否具有获取应用运行的权限
		boolean permission = (PackageManager.PERMISSION_GRANTED ==
				pm.checkPermission("android.permission.PACKAGE_USAGE_STATS", getPackageName()));
//		Log.d("hkc", "permissionManager: "+permission);
		//没有获取则提醒用户获取
//		if (!permission) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
			startActivity(intent);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Intent intent = new Intent(Settings.);
			startActivity(intent);
		}
//		}
	}
	private void initFirstTimeUse() {
		permissionManager();
		mLockView.setTopText(getString(R.string.lock_view_remind_set_password));
		setPassword();

	}

	private void setPassword() {
		mLockView.addTextInputConpleteListener(new LockView.LockViewListener() {
			@Override
			public void textInputComplete(String text) {
				//接受第一次输入
				if (mFirstInput == null) {
					mFirstInput = text;
					mLockView.setTopText(getString(R.string.lock_view_remind_set_again));
					mLockView.clearEditText();
				}
				//接受第二次输入
				else if (mFirstInput.equals(text)) {
					//两次密码输入相同
					LockStore.getInstance().setPassword(text);
					//开启新的界面
					SetSuccessActivity.startActivity(SetPassWordActivity.this, SetSuccessActivity.AFTER_SET_PASSWORD);
					finish();
				} else {
					//两次密码输入不同
					mLockView.clearEditText();
					mLockView.setTopText(getString(R.string.lock_view_remind_set_wrong));
					mFirstInput = null;
				}
			}
		});
	}
}
