package com.example.huangkuncan.applicationlock.View.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangkuncan.applicationlock.R;

/**
 * Created by huangkuncan on 2016/4/12.
 * 邮箱：673391138@qq.com
 * 功能：界面顶部的View
 */
public class TopView extends LinearLayout{
    private Context context;
    private LayoutInflater inflater;
    private ImageView mIconIv;
    private TextView mTextView;

    public TopView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public TopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("hkc", "onKeyDown: "+(keyCode==KeyEvent.KEYCODE_BACK));
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER);
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.common_top_view, this);
        mIconIv = (ImageView) view.findViewById(R.id.common_top_view_iv);
        mTextView = (TextView) view.findViewById(R.id.common_top_view_tv);
    }

    public void setImage(Drawable drawable) {
        mIconIv.setImageDrawable(drawable);
    }

    public void setText(String s) {
        mTextView.setText(s);
    }
}
