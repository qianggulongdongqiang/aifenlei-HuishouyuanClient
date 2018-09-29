package com.arcfun.aifenx.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.HistoryListAdapter;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.Utils;
import com.arcfun.aifenx.view.HistoryFragment;
import com.arcfun.aifenx.view.HistoryFragment.OnInfoCallBack;
import com.arcfun.aifenx.view.ViewPagerScroller;
import com.arcfun.aifenx.view.ViewPagerTabs;

public class MenuHistoryActivity extends BaseFragmentActivity implements
        OnClickListener, OnInfoCallBack, OnPageChangeListener {
    private static final String TAG = "History";
    private TextView mTotal;
    private ViewPager mViewPager;
    private ImageView mHome;
    private int mCurrentPage = 0;
    private HistoryFragment mTodayFragment, mWeekFragment, mMonthFragment;
    private ViewPagerTabs mViewPagerTabs;
    private HistoryListAdapter mAdapter;
    private final List<OnPageChangeListener> mOnPageChangeListeners = new ArrayList<OnPageChangeListener>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_main);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        bindView(R.string.menu_history, null, 0);

        mHome = (ImageView) findViewById(R.id.history_home);
        mTotal = (TextView) findViewById(R.id.history_total);

        mHome.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        List<Fragment> fragments = new ArrayList<Fragment>(3);
        mTodayFragment = new HistoryFragment(Utils.getTodayDate(), this, 0);
        mWeekFragment = new HistoryFragment(Utils.getWeekFirstDate(), this, 1);
        mMonthFragment = new HistoryFragment(Utils.getMonthFirstDate(), this, 2);
        fragments.add(mTodayFragment);
        fragments.add(mWeekFragment);
        fragments.add(mMonthFragment);

        String[] tabTitles = new String[3];
        tabTitles[0] = getResources().getString(R.string.history_title_today);
        tabTitles[1] = getResources().getString(R.string.history_title_week);
        tabTitles[2] = getResources().getString(R.string.history_title_month);
        mAdapter = new HistoryListAdapter(getSupportFragmentManager(),
                fragments, tabTitles);
        mViewPager.setAdapter(mAdapter);
        ViewPagerScroller scroller = new ViewPagerScroller(this);
        mViewPager.setOffscreenPageLimit(getTabCount() - 1);
        scroller.initViewPagerScroll(mViewPager);
        mViewPager.setOnPageChangeListener(this);
        mViewPagerTabs = (ViewPagerTabs) findViewById(R.id.history_pager_header);
        mViewPagerTabs.setViewPager(mViewPager);
        addOnPageChangeListener(mViewPagerTabs);
    }

    public int getTabCount() {
        return mAdapter.getCount();
    }

    public void addOnPageChangeListener(
            OnPageChangeListener onPageChangeListener) {
        if (!mOnPageChangeListeners.contains(onPageChangeListener)) {
            mOnPageChangeListeners.add(onPageChangeListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.history_home:
            enterHome();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void enterHome() {
        startActivity(new Intent(this, MenuActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    private String getFragementCredit(int id) {
        String total = "";
        switch (id) {
        case 0:
            total = mTodayFragment.getCredit();
            break;
        case 1:
            total = mWeekFragment.getCredit();
            break;
        case 2:
            total = mMonthFragment.getCredit();
            break;
        }
        return total;
    }

    @Override
    public void onUpdate(int index, String sum) {
        LogUtils.d(TAG, "onUpdate = " + index + "," + sum);
        if (mCurrentPage == index) {
            mTotal.setText(sum);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        final int count = mOnPageChangeListeners.size();
        for (int i = 0; i < count; i++) {
            mOnPageChangeListeners.get(i).onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
        final int count = mOnPageChangeListeners.size();
        for (int i = 0; i < count; i++) {
            mOnPageChangeListeners.get(i).onPageScrolled(pos, posOffset,
                    posOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int id) {
        mCurrentPage = id;
        mTotal.setText(getFragementCredit(id));
    }

}