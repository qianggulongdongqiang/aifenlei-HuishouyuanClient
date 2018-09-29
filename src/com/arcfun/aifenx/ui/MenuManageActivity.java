package com.arcfun.aifenx.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.PreOrderInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.Utils;

public class MenuManageActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "Manager";
    private Button mFailBtn, mPassBtn;
    private TextView mAccountTitle, mAccount, mManager, mCategory, mAction,
            mUnit, mPoint;
    private int mType = 1;
    private int mOwnerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_main);
        ((AFXApplication) getApplication()).addActivity(this);
        mInfos = ((AFXApplication) getApplication()).getInfo();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestManageOrder("");
        Intent data = getIntent();
        if (data != null) {
            mOwnerId = data.getIntExtra("ownerId", 0);
            mType = data.getIntExtra("type", 1);
            mAccount.setText(data.getStringExtra("account"));
            mManager.setText(getResources().getString(
                    R.string.manage_owner_info)
                    + data.getStringExtra("owner"));
        }
        if (mType > 1) {
            mAccountTitle.setText(getString(R.string.manage_title));
            mUnit.setText(getString(R.string.ensure_good_weight));
            mAction.setVisibility(View.VISIBLE);
        }
        LogUtils.d(TAG, "" + mOwnerId + ", point " + mPoint + ","
                + mCategory.getText().toString());
    }

    private void initView() {
        bindView(R.string.menu_ensure, null, 1);

        mFailBtn = (Button) findViewById(R.id.manager_fail);
        mPassBtn = (Button) findViewById(R.id.manager_ok);
        mAccountTitle = (TextView) findViewById(R.id.manager_account_title);
        mAccount = (TextView) findViewById(R.id.manage_account);
        mManager = (TextView) findViewById(R.id.manage_owner);
        mCategory = (TextView) findViewById(R.id.manage_category);
        mAction = (TextView) findViewById(R.id.manage_action);
        mUnit = (TextView) findViewById(R.id.manage_unit);
        mPoint = (TextView) findViewById(R.id.manage_point);
        mAction.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        mFailBtn.setOnClickListener(this);
        mPassBtn.setOnClickListener(this);
        mAction.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.manager_fail:
            enterHome();
            break;
        case R.id.manager_ok:
            enterHome();
            break;
        case R.id.manage_action:
            enterDetail(null);
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void requestManageOrder(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.USER_GET_PRE;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestManageOrder:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                Utils.parsePreOrder(result);
            }
        }.execute(url);
    }

    private void enterHome() {
        startActivity(new Intent(this, MenuActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    private void enterDetail(PreOrderInfo info) {
        Intent intent = new Intent(this, MenuRecyleDetailActivity.class);
        intent.putExtra("pre_order", info);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

}