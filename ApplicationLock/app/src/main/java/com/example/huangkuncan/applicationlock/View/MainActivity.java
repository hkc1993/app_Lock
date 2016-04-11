package com.example.huangkuncan.applicationlock.View;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.huangkuncan.applicationlock.R;
import com.example.huangkuncan.applicationlock.controller.LockAppManager;
import com.example.huangkuncan.applicationlock.controller.LockGridViewAdapter;
import com.example.huangkuncan.applicationlock.controller.LockSerivice;
import com.example.huangkuncan.applicationlock.controller.LockStore;
import com.example.huangkuncan.applicationlock.controller.LockViewPagerAdapter;
import com.example.huangkuncan.applicationlock.database.LockDataBase;
import com.example.huangkuncan.applicationlock.model.LockAppInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "hkc";
    private ViewPager mViewpager;
    private RelativeLayout rl;
    private LinearLayout ll;
    private int mPageNum = 2;
    private int mGridHaveAppNum = 15;
    private LockViewPagerAdapter mViewPagerAdapter;
    private LockGridViewAdapter mGridViewAdapter;
    private List<View> mGridViews = new ArrayList<View>();
    private TextView mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setGridView();
        setmViewpagerAdapter();
        setIndicator();
        //配置信息
        LockStore.getInstance().setGetApplication(getPackageManager());
        //开启后台服务
        LockSerivice.startSerivice(this);
    }

    @Override
    protected void onDestroy() {
        //关闭后台
//        LockSerivice.stopSerivice(this);
        super.onDestroy();
    }

    /**
     * 设置gridview的数据信息
     */
    private void setGridView() {
        //获取所有的应用信息
        List<LockAppInfo> list = LockAppManager.getInstance().getPackageInfos();
        int num = list.size();
        int start = 0;
        mPageNum = num % mGridHaveAppNum == 0 ? num / mGridHaveAppNum : num / mGridHaveAppNum + 1;
        for (int i = 0; i < mPageNum; i++) {
            GridView gridView = new GridView(this);
            gridView.setNumColumns(5);
            LockGridViewAdapter adapter = new LockGridViewAdapter(this, generateList(list, start, start + 15 <= num ? start + 15 : num));
            gridView.setAdapter(adapter);
            mGridViews.add(gridView);
            start += 15;
        }

    }

    /**
     * 现在采用的手段是对list进行截取
     *
     * @param list
     * @param start 截取的起点
     * @param stop  截取的终点（此点不截取）
     * @return
     */
    private List<LockAppInfo> generateList(List<LockAppInfo> list, int start, int stop) {
        List<LockAppInfo> newList = new ArrayList<LockAppInfo>();
        for (int i = start; i < stop; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

    /**
     * 小圆点相关的代码
     */
    private void setIndicator() {
        ImageView[] views = new ImageView[mPageNum];
        for (int i = 0; i < mPageNum; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.mipmap.indicator_unchoosed);
            ll.addView(iv);
        }
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mPageNum; i++) {
                    if (position == i) {
                        ((ImageView) ll.getChildAt(i)).setBackgroundResource(R.mipmap.indicator_choosed);
                    } else {
                        ((ImageView) ll.getChildAt(i)).setBackgroundResource(R.mipmap.indicator_unchoosed);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setmViewpagerAdapter() {
        LockViewPagerAdapter viewPagerAdapter = new LockViewPagerAdapter(mGridViews);
        mViewpager.setAdapter(viewPagerAdapter);
    }

    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.main_activity_viewpager);
        rl = (RelativeLayout) findViewById(R.id.main_activity_rl);
        ll = (LinearLayout) findViewById(R.id.main_activity_ll);
        mNextBtn = (TextView) findViewById(R.id.main_activity_btn_next);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockAppManager.getInstance().lock(LockStore.getInstance().getListChoosed());
                LockAppManager.getInstance().unlock(LockStore.getInstance().getListUnlock());
            }
        });
    }
}
