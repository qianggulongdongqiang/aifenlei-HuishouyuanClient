package com.arcfun.aifenx.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.CreditOwnerInfo;

public class PopCreditDialog extends Dialog implements OnClickListener {
    private Activity mContext;
    private Button mOk, mCancel;
    private CreditOwnerInfo mInfo;
    private TextView mAll, mWays, mCost, mMore;

    public PopCreditDialog(Activity context, CreditOwnerInfo info) {
        this(context, R.style.PopDialog);
        this.mInfo = info;
        this.mContext = context;
    }

    private PopCreditDialog(Activity context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_credit);

        initView();
    }

    private void initView() {
        mOk = (Button) findViewById(R.id.dialog_left);
        mCancel = (Button) findViewById(R.id.dialog_right);
        mAll = (TextView) findViewById(R.id.dialog_all);
        mWays = (TextView) findViewById(R.id.dialog_way);
        mMore = (TextView) findViewById(R.id.dialog_more);
        mCost = (TextView) findViewById(R.id.dialog_cost);
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        if (mInfo != null) {
            mAll.setText(String.valueOf(mInfo.getScore()));
            mWays.setText(mInfo.getWay());
            mMore.setText(mInfo.getMore());
            mCost.setText(String.valueOf(mInfo.getCost()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.dialog_left:
            dismiss();
            break;
        case R.id.dialog_right:
            enterLogin();
            dismiss();
            break;
        default:
            break;
        }
    }

    private void enterLogin() {
        Intent intent = new Intent(mContext, MenuCreditLoginActivity.class);
        intent.putExtra("score_info", mInfo);
        mContext.startActivity(intent);
        mContext.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        mContext.finish();
    }

}