package com.arcfun.aifenx.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.ScoreInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class MenuCreditQueryActivity extends BaseActivity implements
        OnClickListener, TextWatcher {
    private static final String TAG = "Credit|Query";
    private ImageView mHomeView;
    private Button mQueryBtn;
    private EditText mPhone;
    private ScoreInfo mScoreInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_query);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mScoreInfo = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String mobile = getIntent().getStringExtra("credit_mobile");
        if (mobile != null) {
            mPhone.setText(mobile);
            mPhone.setSelection(mPhone.getText().length());
        }
    }

    private void initView() {
        bindView(R.string.credit_query_title, null, 0);

        mHomeView = (ImageView) findViewById(R.id.query_home);
        mQueryBtn = (Button) findViewById(R.id.query_btn);
        mPhone = (EditText) findViewById(R.id.query_phone);

        mHomeView.setOnClickListener(this);
        mQueryBtn.setOnClickListener(this);
        mPhone.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.query_home:
            enterHome();
            break;
        case R.id.query_btn:
            queryCredit();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void queryCredit() {
        if (TextUtils.isEmpty(mPhone.getText().toString())) {
            Utils.showMsg(this, R.string.invalid_msg);
            return;
        }
        requestQueryCredit(Utils.buildQueryScoreJson(
                SharedPreferencesUtils.getOwnerToken(this),
                Utils.normalizeNumber(mPhone.getText().toString())));
    }

    private void enterCredit() {
        Intent intent = new Intent(this, MenuCreditNewActivity.class);
        intent.putExtra("score_info", mScoreInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    private void enterHome() {
        startActivity(new Intent(this, MenuActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    private void requestQueryCredit(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.SCORE_QUERY;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestQueryCredit:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    mScoreInfo = Utils.parseScoreCode(result);
                    if (mScoreInfo.getCode() == Utils.RESULT_OK) {
                        enterCredit();
                    } else {
                        Utils.showMsg(MenuCreditQueryActivity.this,
                                mScoreInfo.getMsg());
                    }
                    return;
                }
                Utils.showMsg(MenuCreditQueryActivity.this,
                        R.string.login_error);
            };
        }.execute(url);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean able = (Utils.isMobile(mPhone.getText().toString()));
        mQueryBtn.setEnabled(able);
    }
}