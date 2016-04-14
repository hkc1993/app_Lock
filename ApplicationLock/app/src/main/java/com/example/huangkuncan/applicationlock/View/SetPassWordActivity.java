package com.example.huangkuncan.applicationlock.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.LockView;
import com.example.huangkuncan.applicationlock.controller.LockStore;

/**
 * Created by huangkuncan on 2016/4/14.
 * 邮箱：673391138@qq.com
 * 功能：设置密码界面
 */
public class SetPassWordActivity extends Activity {
	private LockView mLockView;
	//第一次输入的密码
	private String mFirstInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_password);
		mLockView = (LockView) findViewById(R.id.set_password_lockview);
		initView();
	}

	public static void startActivity(Context context) {
		Intent intent = new Intent(context, SetPassWordActivity.class);
		context.startActivity(intent);
	}

	private void initView() {
		mLockView.setTopText(getString(R.string.lock_view_remind_set_password));
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
					MainActivity.startActivity(SetPassWordActivity.this);
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
