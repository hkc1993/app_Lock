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
public class SetSuccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_success_activity);
		((TopView) findViewById(R.id.set_success_topview)).setStateText(getString(R.string.set_success));
		findViewById(R.id.set_success_password).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetPassWordActivity.startActivity(SetSuccessActivity.this);
			}
		});
		findViewById(R.id.set_success_app).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.startActivity(SetSuccessActivity.this);
			}
		});
	}
	public static void startActivity(Context context) {
		Intent intent = new Intent(context, SetSuccessActivity.class);
		context.startActivity(intent);
	}
}
