package com.example.huangkuncan.applicationlock.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.View.View.TopView;

/**
 * Created by huangkuncan on 2016/4/14.
 * 邮箱：673391138@qq.com
 * 功能：密码设置成功界面
 */
public class SetSuccessActivity extends LockBaseActivity {
	public final static int AFTER_SET_PASSWORD = 0;
	public final static int AFTER_SET_APP = 1;
	private int flag;
	private boolean mNeedCheckPasswod;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_success_activity);
		flag = getIntent().getIntExtra("flag", 0);
		initView();
	}

	@Override
	protected void onStart() {
//按返回键或者home键退出后进入也需要输入密码
		if (mNeedCheckPasswod) {
			mNeedCheckPasswod = false;
			SetPassWordActivity.startActivity(this, SetPassWordActivity.CHECK_PASSWORD);
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		mNeedCheckPasswod = true;
		super.onStop();
	}

	private void initView() {
		if (flag == AFTER_SET_PASSWORD) {
			((TopView) findViewById(R.id.set_success_topview)).setStateText(getString(R.string.set_success));
		} else if (flag == AFTER_SET_APP) {
			((TopView) findViewById(R.id.set_success_topview)).setStateText(getString(R.string.normal_operation));
		}
		findViewById(R.id.set_success_password).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetPassWordActivity.startActivity(SetSuccessActivity.this, SetPassWordActivity.SET_PASSWORD);
				finish();
			}
		});
		findViewById(R.id.set_success_app).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.startActivity(SetSuccessActivity.this);
				finish();
			}
		});
	}

	public static void startActivity(Context context, int flag) {
		Intent intent = new Intent(context, SetSuccessActivity.class);
		intent.putExtra("flag", flag);
		context.startActivity(intent);
	}
}
