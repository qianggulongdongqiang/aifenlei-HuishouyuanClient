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

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.HistoryListAdapter;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.view.RecyleFragment;
import com.arcfun.aifenx.view.RecyleFragment.OnActionCallBack;
import com.arcfun.aifenx.view.ViewPagerScroller;
import com.arcfun.aifenx.view.ViewPagerTabs;

public class MenuRecyleActivity extends BaseFragmentActivity implements
        OnClickListener, OnActionCallBack, OnPageChangeListener {
    private static final String TAG = "Recyle";
    private ViewPager mViewPager;
    private ImageView mHomeBtn, mNewBtn;
    private RecyleFragment mUnkownFragment, mSelectedFragment, mAllFragment;
    private ViewPagerTabs mViewPagerTabs;
    private HistoryListAdapter mAdapter;
    private final List<OnPageChangeListener> mOnPageChangeListeners =
            new ArrayList<OnPageChangeListener>();
    private boolean isMaster = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyle_main);
        ((AFXApplication) getApplication()).addActivity(this);
        mInfos = ((AFXApplication) getApplication()).getInfo();
        isMaster = SharedPreferencesUtils.getOwnerType(this);

        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getBooleanExtra("force_reload", false)) {
            refresh(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        bindView(R.string.menu_order, null, 0);

        mHomeBtn = (ImageView) findViewById(R.id.recyle_home);
        mNewBtn = (ImageView) findViewById(R.id.recyle_new);
        mHomeBtn.setOnClickListener(this);
        mNewBtn.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        List<Fragment> fragments = new ArrayList<Fragment>(3);
        mUnkownFragment = new RecyleFragment(this, 0);
        mSelectedFragment = new RecyleFragment(this, 1);
        fragments.add(mUnkownFragment);
        fragments.add(mSelectedFragment);
        if (isMaster) {
            mAllFragment = new RecyleFragment(this, 2);
            fragments.add(mAllFragment);
        }

        String[] tabTitles = new String[3];
        tabTitles[0] = getResources().getString(R.string.recyle_type_none);
        tabTitles[1] = getResources().getString(R.string.recyle_type_mine);
        tabTitles[2] = getResources().getString(R.string.recyle_type_all);
        mAdapter = new HistoryListAdapter(getSupportFragmentManager(),
                fragments, tabTitles);
        mViewPager.setAdapter(mAdapter);
        ViewPagerScroller scroller = new ViewPagerScroller(this);
        mViewPager.setOffscreenPageLimit(getTabCount() - 1);
        scroller.initViewPagerScroll(mViewPager);
        mViewPager.setOnPageChangeListener(this);
        mViewPagerTabs = (ViewPagerTabs) findViewById(R.id.recyle_pager_header);
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
        case R.id.recyle_home:
            enterHome();
            break;
        case R.id.recyle_new:
            if (mInfos != null && mInfos.size() > 0) {
                enterNew();
            }

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

    private void enterNew() {
        startActivity(new Intent(this, MenuRecyleNewActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    @Override
    public void onUpdate(int index) {
        LogUtils.d(TAG, "onUpdate " + index);
        refresh(index);
    }

    /**
     * refresh the RecyleFragment
     * @param index
     */
    private void refresh(int index) {
        LogUtils.d(TAG, "refresh " + index);
        if (mUnkownFragment != null) {
            mUnkownFragment.reload(true);
        }
        if (mSelectedFragment != null) {
            mSelectedFragment.reload(index != 1);
        }
        if (mAllFragment != null) {
            mAllFragment.reload(index != 2);
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
    }

}