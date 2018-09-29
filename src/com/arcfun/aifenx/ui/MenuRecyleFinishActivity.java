package com.arcfun.aifenx.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.AllOrderInfo;
import com.arcfun.aifenx.data.EnsureOrderListAdapter;
import com.arcfun.aifenx.data.GoodInfo;
import com.arcfun.aifenx.utils.Utils;

public class MenuRecyleFinishActivity extends BaseActivity implements
        OnClickListener {
    private TextView mSumCredit, mAccount, mCreditStart, mCreditEnd;
    private ImageView mHomeBtn, mNextBtn;
    private ListView mListView;
    private EnsureOrderListAdapter mAdapter;
    private ArrayList<GoodInfo> mGoods = new ArrayList<GoodInfo>();
    private AllOrderInfo mAllOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < mAllOrder.getGood().length; i++) {
            mGoods.add(mAllOrder.getGood()[i]);
        }
        mAdapter = new EnsureOrderListAdapter(mGoods);
        mListView.setAdapter(mAdapter);
        mSumCredit.setText(String.valueOf(mAllOrder.getPointsNumber()));
        mAccount.setText(mAllOrder.getBuyerName() + "(" +
                Utils.formatPhoneNumber(mAllOrder.getBuyerPhone()) + ")");
        mCreditStart.setText(String.valueOf(mAllOrder.getUserScore() - 
                mAllOrder.getPointsNumber()));
        mCreditEnd.setText(String.valueOf(mAllOrder.getUserScore()));
    }

    private void initView() {
        bindView(R.string.finishe_order, null, 0);

        mAllOrder = getIntent().getParcelableExtra("all_order");
        mHomeBtn = (ImageView) findViewById(R.id.finish_home);
        mNextBtn = (ImageView) findViewById(R.id.finish_credit);
        mListView = (ListView) findViewById(R.id.finish_list);
        mSumCredit = (TextView) findViewById(R.id.finish_sum);
        mAccount = (TextView) findViewById(R.id.finish_account);
        mCreditStart = (TextView) findViewById(R.id.finish_before);
        mCreditEnd = (TextView) findViewById(R.id.finish_end);
        mHomeBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.finish_home:
            enterHome();
            break;
        case R.id.finish_credit:
            enterNext();
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

    private void enterNext() {
        Intent intent = new Intent(this, MenuCreditQueryActivity.class);
        if (mAllOrder != null) {
            intent.putExtra("credit_mobile", mAllOrder.getBuyerPhone());
        }
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

}