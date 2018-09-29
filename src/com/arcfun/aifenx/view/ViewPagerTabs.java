package com.arcfun.aifenx.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcfun.aifenx.utils.Utils;

public class ViewPagerTabs extends HorizontalScrollView implements
        ViewPager.OnPageChangeListener {

    private static final int TAB_SIDE_PADDING_IN_DPS = 10;
    private static final int[] ATTRS = new int[] { android.R.attr.textSize,
            android.R.attr.textStyle, android.R.attr.textColor };

    final int mTextStyle;
    final ColorStateList mTextColor;
    final int mTextSize;
    ViewPager mPager;
    int mPrevSelected = -1;
    int mSidePadding;
    private ViewPagerTabStrip mTabStrip;

    public ViewPagerTabs(Context context) {
        this(context, null);
    }

    public ViewPagerTabs(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTabs(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);

        mSidePadding = (int) (getResources().getDisplayMetrics().density * TAB_SIDE_PADDING_IN_DPS);

        final TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        mTextSize = a.getDimensionPixelSize(0, 0);
        mTextStyle = a.getInt(1, 0);
        mTextColor = a.getColorStateList(2);
        mTabStrip = new ViewPagerTabStrip(context);
        addView(mTabStrip, new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        a.recycle();
    }

    public void setViewPager(ViewPager viewPager) {
        mPager = viewPager;
        addTabs(mPager.getAdapter());
    }

    private void addTabs(PagerAdapter adapter) {
        mTabStrip.removeAllViews();

        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            addTab(adapter.getPageTitle(i), i);
        }
    }

    private void addTab(CharSequence tabTitle, final int position) {
        View tabView;
        final TextView textView = new TextView(getContext());
        textView.setText(tabTitle);

        // Assign various text appearance related attributes to child views.
        if (mTextStyle > 0) {
            textView.setTypeface(textView.getTypeface(), mTextStyle);
        }
        if (mTextSize > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        if (mTextColor != null) {
            textView.setTextColor(mTextColor);
        }
        textView.setGravity(Gravity.CENTER);
        tabView = textView;
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(getRtlPosition(position));
            }
        });

        tabView.setOnLongClickListener(new OnTabLongClickListener(position));
        tabView.setPadding(mSidePadding, 0, mSidePadding, 0);
        mTabStrip.addView(tabView, position, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1));

        // Default to the first child being selected
        if (position == 0) {
            mPrevSelected = 0;
            tabView.setSelected(true);
        }
    }

    /**
     * Remove a tab at a certain index.
     * 
     * @param index
     *            The index of the tab view we wish to remove.
     */
    public void removeTab(int index) {
        View view = mTabStrip.getChildAt(index);
        if (view != null) {
            mTabStrip.removeView(view);
        }
    }

    /**
     * Refresh a tab at a certain index by removing it and reconstructing it.
     * 
     * @param index
     *            The index of the tab view we wish to update.
     */
    public void updateTab(int index) {
        removeTab(index);

        if (index < mPager.getAdapter().getCount()) {
            addTab(mPager.getAdapter().getPageTitle(index), index);
        }
    }

    @Override
    public void onPageScrolled(int position, float posOffset,
            int posOffsetPixels) {
        position = getRtlPosition(position);
        int tabStripChildCount = mTabStrip.getChildCount();
        if ((tabStripChildCount == 0) || (position < 0)
                || (position >= tabStripChildCount)) {
            return;
        }

        mTabStrip.onPageScrolled(position, posOffset, posOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        position = getRtlPosition(position);
        int tabStripChildCount = mTabStrip.getChildCount();
        if ((tabStripChildCount == 0) || (position < 0)
                || (position >= tabStripChildCount)) {
            return;
        }

        if (mPrevSelected >= 0 && mPrevSelected < tabStripChildCount) {
            mTabStrip.getChildAt(mPrevSelected).setSelected(false);
        }
        final View selectedChild = mTabStrip.getChildAt(position);
        selectedChild.setSelected(true);

        // Update scroll position
        final int scrollPos = selectedChild.getLeft()
                - (getWidth() - selectedChild.getWidth()) / 2;
        smoothScrollTo(scrollPos, 0);
        mPrevSelected = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private int getRtlPosition(int position) {
        if (getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            return mTabStrip.getChildCount() - 1 - position;
        }
        return position;
    }

    /**
     * Simulates action bar tab behavior by showing a toast with the tab title
     * when long clicked.
     */
    private class OnTabLongClickListener implements OnLongClickListener {

        final int mPosition;

        public OnTabLongClickListener(int position) {
            mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            Utils.showMsg(getContext(),
                    mPager.getAdapter().getPageTitle(mPosition));
            return true;
        }
    }

}