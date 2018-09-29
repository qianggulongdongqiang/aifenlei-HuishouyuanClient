package com.arcfun.aifenx.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.OwnerInfo;
import com.arcfun.aifenx.data.RawOwnerInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class LoginActivity extends Activity implements OnClickListener,
        TextWatcher {
    private static final String TAG = "Login";
    private EditText mAccount, mPassword;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    private void initView() {
        mAccount = (EditText) findViewById(R.id.login_account);
        mPassword = (EditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);
        mAccount.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
        String name = SharedPreferencesUtils.getOwnerName(this);
        if (name != null) {
            mAccount.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_btn:
            requestLogin(Utils.buildAccountJson(mAccount.getText().toString(),
                    mPassword.getText().toString()));
            break;

        default:
            break;
        }
    }

    private void requestLogin(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.USER_LOGIN;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                LogUtils.d(TAG, "requestLogin data =" + data);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestLogin:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                RawOwnerInfo raw = null;
                if (result != null) {
                    raw = Utils.parseOwnerInfo(result);
                    if (raw.getCode() == Utils.RESULT_OK
                            && raw.getInfo() != null) {
                        insertInfoToDB(raw.getInfo());
                        enter();
                        return;
                    }
                }
                if (raw != null) {
                    Utils.showMsg(LoginActivity.this, raw.getMsg());
                } else {
                    Utils.showMsg(LoginActivity.this, R.string.login_error);
                }
            };
        }.execute(url);
    }

    private void insertInfoToDB(OwnerInfo info) {
        SharedPreferencesUtils.setOwner(this, info, System.currentTimeMillis());
    }

    private void enter() {
        startActivity(new Intent(this, MenuActivity.class));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
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
        boolean able = (mPassword.getText().toString().length() > 0)
                && (mAccount.toString().length() > 0);
        mLoginBtn.setEnabled(able);
    }
}