package com.arcfun.aifenx.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;

public class PopLoginDialog extends Dialog implements OnClickListener {
    private Button mOk, mCancel;
    private Activity mContext;

    public PopLoginDialog(Activity activity) {
        this(activity, R.style.PopDialog);
    }

    private PopLoginDialog(Activity context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_layout);
        mOk = (Button) findViewById(R.id.dialog_login_ok);
        mCancel = (Button) findViewById(R.id.dialog_login_cancel);
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        initView();
    }

    private void initView() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dialog_login_ok:
            launchLogin();
            dismiss();
            break;
        case R.id.dialog_login_cancel:
            dismiss();
            break;
        default:
            break;
        }
    }

    protected void launchLogin() {
        SharedPreferencesUtils.resetOwnerToken(mContext, "");
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        mContext.overridePendingTransition(R.anim.anim_enter,
                R.anim.anim_exit);
        mContext.finish();
    }
}