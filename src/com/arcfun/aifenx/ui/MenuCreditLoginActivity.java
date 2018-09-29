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
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.CreditOwnerInfo;
import com.arcfun.aifenx.data.ScoreInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.CountDownTimerUtils;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class MenuCreditLoginActivity extends BaseActivity implements
        OnClickListener, TextWatcher {
    private static final String TAG = "Credit|Login";
    private static final int SMS_CODE_LENGTH = 6;
    private ImageView mHomeView;
    private Button mLoginBtn, mCodeBtn;
    private TextView mPhone;
    private EditText mCode;
    private ScoreInfo mScoreInfo = null;
    private CountDownTimerUtils mCountHelper;
    public static final int PERIOD = 60 * 1000;
    public static final int INTERVAL = 1 * 1000;
    private CreditOwnerInfo mInfo;
    private ScoreInfo mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_login);
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
        mInfo = getIntent().getParcelableExtra("score_info");
        if (mInfo != null) {
            LogUtils.d(TAG, "onResume " + mInfo.toString());
            mPhone.setText(Utils.formatPhoneNumber(mInfo.getMobile()));
        }
        mCountHelper = new CountDownTimerUtils(mCodeBtn, PERIOD, INTERVAL,
                R.string.credit_token_again, getResources().getString(
                        R.string.credit_token_suffix));
        mCode.setText("");
    }

    private void initView() {
        bindView(R.string.menu_credit, null, 0);

        mHomeView = (ImageView) findViewById(R.id.cost_home);
        mCodeBtn = (Button) findViewById(R.id.cost_code_btn);
        mLoginBtn = (Button) findViewById(R.id.cost_login);
        mPhone = (TextView) findViewById(R.id.cost_phone);
        mCode = (EditText) findViewById(R.id.cost_code);

        mCodeBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mHomeView.setOnClickListener(this);
        mCode.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.cost_home:
            enterHome();
            break;
        case R.id.cost_code_btn:
            requestCode();
            break;
        case R.id.cost_login:
            requestAddScore(Utils.buildAddScoreJson(
                    SharedPreferencesUtils.getOwnerToken(this), mInfo.getId(),
                    mCode.getText().toString(), mInfo.getCost(), mInfo.getWay()
                            + "+" + mInfo.getMore()));
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void loginCredit() {
        if (mScoreInfo == null || mScoreInfo.getCode() != Utils.RESULT_OK) {
            Utils.showMsg(this, R.string.invalid_code);
            return;
        }
        if (!TextUtils.isEmpty(mCode.getText().toString())) {
            mCountHelper.stop();
            enterResult();
        }
    }

    private void requestCode() {
        if (mInfo == null || !Utils.isMobile(mInfo.getMobile())) {
            Utils.showMsg(this, R.string.credit_code_error);
            return;
        }
        mCountHelper.start();
        mCode.requestFocus();
        requestCreditCode(Utils.buildScoreCodeJson(mInfo.getMobile()));
    }

    private void enterHome() {
        startActivity(new Intent(this, MenuActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    private void requestAddScore(final String json) {
        if (json == null)
            return;
        String url = HttpRequest.URL_HEAD + HttpRequest.SCORE_CONSUME;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestAddScore: " + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    mResult = Utils.parseAddScore(result);
                }
                if (mResult != null) {
                    if (mResult.getCode() == Utils.RESULT_OK) {
                        LogUtils.d(TAG, "requestAddScore " + mResult.getScore());
                        loginCredit();
                    } else {
                        Utils.showMsg(MenuCreditLoginActivity.this,
                                mResult.getMsg());
                    }
                }
            }
        }.execute(url);
    }

    private void enterResult() {
        Intent intent = new Intent(this, MenuCreditResultActivity.class);
        intent.putExtra("score_result", mResult);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    private void requestCreditCode(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.SCORE_SEND_SMS_CODE;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestCreditCode:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    mScoreInfo = Utils.parseScoreCode(result);
                    Utils.showMsg(MenuCreditLoginActivity.this,
                            mScoreInfo.getMsg());
                    return;
                }
                mCountHelper.stop();
                Utils.showMsg(MenuCreditLoginActivity.this,
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
        boolean able = mCode.getText().toString().length() >= SMS_CODE_LENGTH;
        mLoginBtn.setEnabled(able);
    }
}