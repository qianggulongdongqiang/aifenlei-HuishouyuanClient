package com.arcfun.aifenx.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.HistoryOrderInfo;
import com.arcfun.aifenx.data.PreOrderInfo;
import com.arcfun.aifenx.data.ResultInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class MenuRecyleDetailActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "Recyle|Detail";
    private Button mStartBtn;
    private ImageView mRrids;
    private TextView mTime, mCategory, mAccount, mNumber, mAddr, mManager, mSn;
    private LinearLayout mManagerLayout, mCommentLayout;
    private PreOrderInfo mInfo;
    private HistoryOrderInfo mHistoryInfo;
    private boolean mIsHistory = false;
    private boolean isNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyle_detail);
        ((AFXApplication) getApplication()).addActivity(this);
        mInfos = ((AFXApplication) getApplication()).getInfo();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsHistory) {
            mTime.setText(mInfo.getFinishedTime());
            mCategory.setText(mInfo.getGoodsName());
            mAccount.setText(mInfo.getBuyerName() + "("
                    + Utils.formatPhoneNumber(mInfo.getMobile()) + ")");
            mNumber.setText(mInfo.getBuyerPhone());
            mAddr.setText(mInfo.getBuyerAddr());
            mManagerLayout.setVisibility(View.GONE);
            mSn.setText(mInfo.getSn());
            mCommentLayout .setVisibility((mInfo.getAddition() == 1) ?
                    View.VISIBLE : View.GONE);
            mRrids.setVisibility(mInfo.getRfidCount() < 1 ? View.VISIBLE
                    : View.GONE);
            mStartBtn.setOnClickListener(this);
            mStartBtn.setText(isNew ? R.string.manage_receive
                    : R.string.pre_to_recyle);
            if (isNew) {
                mStartBtn.setEnabled(mInfo.getCollecterId() == 0);
            }
        } else {
            mTime.setText(mHistoryInfo.getFinishedTime());
            mCategory.setText(mHistoryInfo.getGoodsName());
            mAccount.setText(mHistoryInfo.getBuyerName() + "("
                    + Utils.formatPhoneNumber(mHistoryInfo.getMobile()) + ")");
            mNumber.setText(mHistoryInfo.getMobile());
            mAddr.setText(mHistoryInfo.getBuyerAddr());
            mManagerLayout.setVisibility(View.VISIBLE);
            mManager.setText(mHistoryInfo.getCollecterName());
            mSn.setText(mHistoryInfo.getSn());
            mCommentLayout .setVisibility((mHistoryInfo.getAddition() == 1) ?
                    View.VISIBLE : View.GONE);
            mRrids.setVisibility(View.GONE);
            mStartBtn.setVisibility(View.GONE);
        }

    }

    private void initView() {
        bindView(R.string.title_order_detail, null, 1);
        mInfo = getIntent().getParcelableExtra("pre_order");
        mIsHistory = getIntent().getBooleanExtra("is_history", false);
        isNew = getIntent().getBooleanExtra("is_New", false);
        mHistoryInfo = getIntent().getParcelableExtra("history_order");

        mTime = (TextView) findViewById(R.id.pre_order_time);
        mRrids = (ImageView) findViewById(R.id.pre_order_rfid);
        mCategory = (TextView) findViewById(R.id.pre_order_category);
        mAccount = (TextView) findViewById(R.id.pre_order_account);
        mNumber = (TextView) findViewById(R.id.pre_order_number);
        mAddr = (TextView) findViewById(R.id.pre_order_addr);
        mManager = (TextView) findViewById(R.id.pre_order_manager);
        mSn = (TextView) findViewById(R.id.pre_order_sn);
        mManagerLayout = (LinearLayout) findViewById(R.id.ll_order_manager);
        mCommentLayout = (LinearLayout) findViewById(R.id.pre_order_comments);
        mStartBtn = (Button) findViewById(R.id.recyle_done);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.recyle_done:
            if (!isNew) {
                enterNew();
            } else if (mInfo != null && !mIsHistory) {
                if (mStartBtn != null) {
                    mStartBtn.setEnabled(false);
                }
                String token = SharedPreferencesUtils.getOwnerToken(this);
                requestBindPreOrder(Utils.buildTokenIdJson(token, mInfo.getId()));
            }
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void enterNew() {
        LogUtils.d(TAG, "enterNew");
        Intent intent = new Intent(this, MenuRecyleNewActivity.class);
        intent.putExtra("is_pre_order", true);
        intent.putExtra("pre_order", mInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    private void ensureOrder() {
        LogUtils.d(TAG, "ensureOrder");
        Intent intent = new Intent(this, MenuRecyleActivity.class);
        intent.putExtra("force_reload", true);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    private void requestBindPreOrder(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.BIND_PRE_ORDER;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestBindPreOrder:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (mStartBtn != null) {
                    mStartBtn.setEnabled(true);
                }
                ResultInfo info = null;
                if (result != null) {
                    info = Utils.parseResultInfo(result);
                }
                if (info != null) {
                    if (info.getCode() == Utils.RESULT_OK) {
                        ensureOrder();
                    }
                    Utils.showMsg(MenuRecyleDetailActivity.this, info.getMsg());
                } else {
                    Utils.showMsg(MenuRecyleDetailActivity.this,
                            R.string.login_error);
                }
            }
        }.execute(url);
    }
}