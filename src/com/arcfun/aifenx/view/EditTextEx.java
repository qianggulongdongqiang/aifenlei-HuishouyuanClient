package com.arcfun.aifenx.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.arcfun.aifenx.R;

public class EditTextEx extends EditText implements TextWatcher {

    private Drawable draw;

    public EditTextEx(Context context) {
        this(context, null);
    }

    public EditTextEx(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initClearDrawable();
        this.addTextChangedListener(this);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && !this.getText().toString().equals("")) {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private void initClearDrawable() {
        draw = getCompoundDrawables()[2];

        if (draw == null) {
            draw = getResources().getDrawable(R.drawable.delete);
        }

        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore,
            int lengthAfter) {
        if (text.length() > 0) {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() > (getWidth() - getTotalPaddingRight())
                    && event.getX() < (getWidth() - getPaddingRight())) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

}