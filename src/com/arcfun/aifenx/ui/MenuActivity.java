package com.arcfun.aifenx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.ResultInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;
import com.arcfun.aifenx.zxing.client.CaptureActivity;

public class MenuActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "Menu";
    private Button mNewOwner, mRecyleOrder, mCreditExchange, mHistory, mManage;
    private int mOwnerId;
    private String mName = "";
    private static final int REQUEST_SCAN_GOOD = 201;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = SharedPreferencesUtils.getOwnerToken(this);
        if (mToken == null || mToken.isEmpty()) {
            login();
            return;
        }
        setContentView(R.layout.activity_menu);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPreferencesUtils.getRegister(this)) {
            requestSetPushCode(this, Utils.buildPushCode(mToken,
                    Utils.getRegistrationID(this)));
        }
        if (mInfos == null || mInfos.size() == 0) {
            requestSecGoods();
        }
    }

    private void initView() {
        mOwnerId = SharedPreferencesUtils.getOwnerId(this);
        mName = SharedPreferencesUtils.getOwnerName(this);
        bindView(mName, null, 2);

        mNewOwner = (Button) findViewById(R.id.new_owner);
        mRecyleOrder = (Button) findViewById(R.id.recyle_order);
        mCreditExchange = (Button) findViewById(R.id.credit_exchange);
        mHistory = (Button) findViewById(R.id.history_list);
        mManage = (Button) findViewById(R.id.owner_ensure);

        mNewOwner.setOnClickListener(this);
        mRecyleOrder.setOnClickListener(this);
        mCreditExchange.setOnClickListener(this);
        mHistory.setOnClickListener(this);
        mManage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.new_owner:
            startActivity(new Intent(this, MenuCreateActivity.class));
            break;
        case R.id.recyle_order:
            startActivity(new Intent(this, MenuRecyleActivity.class));
            break;
        case R.id.credit_exchange:
            startActivity(new Intent(this, MenuCreditQueryActivity.class));
            break;
        case R.id.history_list:
            startActivity(new Intent(this, MenuHistoryActivity.class));
            break;
        case R.id.owner_ensure:
            startScan();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void startScan() {
        Utils.showMsg(MenuActivity.this,
                getResources().getString(R.string.menu_credit_not_online));
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_GOOD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SCAN_GOOD) {
                String result = data.getStringExtra("SCAN_RESULT");
                Utils.showMsg(MenuActivity.this, result);
                Intent intent = new Intent(this, MenuManageActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("account", result);
                intent.putExtra("owner", mName);
                intent.putExtra("ownerId", mOwnerId);
                startActivity(intent);
            }
        }
    }

    private void requestSetPushCode(final Context c, final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.SET_PUSH_CODE;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestSetPushCode:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                ResultInfo resultInfo = null;
                if (result != null) {
                    resultInfo = Utils.parsePushCode(result);
                }
                if (resultInfo != null) {
                    LogUtils.d(TAG, "requestSetPushCode Registration Id sucess.");
                    SharedPreferencesUtils.setRegister(c,
                            resultInfo.getCode() == Utils.RESULT_OK);
                }
            }
        }.execute(url);
    }

    private void login() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }
}