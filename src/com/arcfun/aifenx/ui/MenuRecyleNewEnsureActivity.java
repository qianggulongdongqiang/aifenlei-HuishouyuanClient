package com.arcfun.aifenx.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.AllOrderInfo;
import com.arcfun.aifenx.data.EnsureOrderListAdapter;
import com.arcfun.aifenx.data.GoodInfo;
import com.arcfun.aifenx.data.PreOrderInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class MenuRecyleNewEnsureActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "Recyle|Ensure";
    private TextView mAccount, mTotalCredit;
    private Button mEnsureBtn;
    private ListView mGoodList;
    private boolean isPreOrd;
    private PreOrderInfo mPreOrder;
    private String mNumber;
    private ArrayList<GoodInfo> mGoods = new ArrayList<GoodInfo>();
    private EnsureOrderListAdapter mAdapter;
    private String mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_order);
        ((AFXApplication) getApplication()).addActivity(this);
        mInfos = ((AFXApplication) getApplication()).getInfo();

        initView();
    }

    private void initView() {
        bindView(R.string.title_ensure_order, R.string.last);

        mAccount = (TextView) findViewById(R.id.ensure_number);
        mTotalCredit = (TextView) findViewById(R.id.ensure_total_credit);
        mEnsureBtn = (Button) findViewById(R.id.create_next);
        mEnsureBtn.setOnClickListener(this);
        mGoodList = (ListView) findViewById(R.id.ensure_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isPreOrd = getIntent().getBooleanExtra("is_pre_order", false);
        mPreOrder = getIntent().getParcelableExtra("pre_order");
        mNumber = getIntent().getStringExtra("new_number");
        mGoods = getIntent().getParcelableArrayListExtra("new_goods");
        mCode = getIntent().getStringExtra("pre_code");
        if (isPreOrd && mPreOrder != null) {
            mNumber = mPreOrder.getMobile();
            mAccount.setText(mPreOrder.getBuyerName() + "("
                    + Utils.formatPhoneNumber(mPreOrder.getMobile()) + ")");
        } else {
            mAccount.setText(mNumber);
        }
        mTotalCredit.setText(getTotalCredit(mGoods));
        mAdapter = new EnsureOrderListAdapter(mGoods);
        mGoodList.setAdapter(mAdapter);
    }

    private String getTotalCredit(ArrayList<GoodInfo> goods) {
        int credit = 0;
        for (GoodInfo good : goods) {
            credit += Utils.formatInt(good.getVendor() * good.getPoint());
        }
        return String.valueOf(credit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.create_next:
            if (mEnsureBtn != null) {
                mEnsureBtn.setEnabled(false);
            }
            enterNext();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void enterNext() {
        requestAddOrder(Utils.buildAddOrderJson(
                SharedPreferencesUtils.getOwnerToken(this), mNumber, mGoods,
                (mPreOrder != null) ? mPreOrder.getSn() : null, mCode));
    }

    private void requestAddOrder(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.ADD_ORDER;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestAddOrder:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (mEnsureBtn != null) {
                    mEnsureBtn.setEnabled(true);
                }
                AllOrderInfo orderInfo = null;
                if (result != null) {
                    orderInfo = Utils.parseAddOrder(result);
                }
                if (orderInfo != null) {
                    if (orderInfo.getCode() == Utils.RESULT_OK) {
                        showResult(orderInfo);
                    } else {
                        Utils.showMsg(MenuRecyleNewEnsureActivity.this,
                                orderInfo.getMsg());
                    }
                }
            }
        }.execute(url);
    }

    private void showResult(AllOrderInfo info) {
        Intent intent = new Intent(this, MenuRecyleFinishActivity.class);
        intent.putExtra("all_order", info);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

}