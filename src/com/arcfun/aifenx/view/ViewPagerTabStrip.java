package com.arcfun.aifenx.view;

import com.arcfun.aifenx.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ViewPagerTabStrip extends LinearLayout {

    private final Paint mSelectedUnderlinePaint;
    private int mSelectedUnderlineThickness = 5;
    private int mIndexForSelection;
    private float mSelectionOffset;

    public ViewPagerTabStrip(Context context) {
        this(context, null);
    }

    public ViewPagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        final Resources res = context.getResources();

        int underlineColor = res.getColor(R.color.login_button_enable);
        int backgroundColor = res.getColor(R.color.white);

        mSelectedUnderlinePaint = new Paint();
        mSelectedUnderlinePaint.setColor(underlineColor);

        setBackgroundColor(backgroundColor);
        setWillNotDraw(false);
    }

    /**
     * Notifies this view that view pager has been scrolled. We save the tab
     * index and selection offset for interpolating the position and width of
     * selection underline.
     */
    void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        mIndexForSelection = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int childCount = getChildCount();

        // Thick colored underline below the current selection
        if (childCount > 0) {
            View selectedTitle = getChildAt(mIndexForSelection);

            if (selectedTitle == null) {
                // The view pager's tab count changed but we weren't notified yet.
                // Ignore this draw pass, when we get a new selection
                // we will update and draw the selection strip in the correct place.
                return;
            }
            int selectedLeft = selectedTitle.getLeft();
            int selectedRight = selectedTitle.getRight();
            final boolean isRtl = isRtl();
            final boolean hasNextTab = isRtl ? mIndexForSelection > 0
                    : (mIndexForSelection < (getChildCount() - 1));
            if ((mSelectionOffset > 0.0f) && hasNextTab) {
                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mIndexForSelection
                        + (isRtl ? -1 : 1));
                int nextLeft = nextTitle.getLeft();
                int nextRight = nextTitle.getRight();

                selectedLeft = (int) (mSelectionOffset * nextLeft + (1.0f - mSelectionOffset)
                        * selectedLeft);
                selectedRight = (int) (mSelectionOffset * nextRight + (1.0f - mSelectionOffset)
                        * selectedRight);
            }

            int height = getHeight();
            canvas.drawRect(selectedLeft, height - mSelectedUnderlineThickness,
                    selectedRight, height, mSelectedUnderlinePaint);
        }
    }

    private boolean isRtl() {
        return getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
}