package com.arcfun.aifenx.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.AddOwnerInfo;
import com.arcfun.aifenx.data.ResultInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.Utils;

public class MenuCreateEnsureActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "Menu";
    private TextView mAccount, mName, mSex, mNumber, mAddr;
    private Button mEnsure;
    private AddOwnerInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_owner);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    private void initView() {
        mInfo = getIntent().getParcelableExtra("add_owner");
        bindView(R.string.ensure_new, R.string.last);
        mAccount = (TextView) findViewById(R.id.ensure_account);
        mName = (TextView) findViewById(R.id.ensure_name);
        mSex = (TextView) findViewById(R.id.ensure_sex);
        mNumber = (TextView) findViewById(R.id.ensure_number);
        mAddr =  (TextView) findViewById(R.id.ensure_addr);
        mEnsure =  (Button) findViewById(R.id.ensure_next);
        mEnsure.setOnClickListener(this);

        mAccount.setText(mInfo.getAccount());
        mName.setText(mInfo.getName());
        mSex.setText(getSexRes(mInfo.getSex()));
        mNumber.setText(mInfo.getMobile());
        mAddr.setText(mInfo.getAddr());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ensure_next:
            if (mEnsure != null) {
                mEnsure.setEnabled(false);
            }
            requestBuildOwner();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void requestBuildOwner() {
        requestAddOwner(Utils.buildAddOwnerJson(mInfo));
    }

    private void requestAddOwner(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.USER_ADD;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestAddOwner:" + result);
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                if (mEnsure != null) {
                    mEnsure.setEnabled(true);
                }
                ResultInfo add = null;
                if (result != null) {
                    add = Utils.parseResultInfo(result);
                }
                if (add != null) {
                    showResult(add.getCode() == Utils.RESULT_OK);
                    Utils.showMsg(MenuCreateEnsureActivity.this, add.getMsg());
                } else {
                    Utils.showMsg(MenuCreateEnsureActivity.this, R.string.login_error);
                }
            }
        }.execute(url);
    }

    private void showResult(boolean success) {
        if (!success) return;
        Intent intent = new Intent(this, MenuCreateResultActivity.class);
        intent.putExtra("add_result", success);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    private int getSexRes(String sex) {
        if (sex.equals("2")) {
            return R.string.sex_female;
        }
        return R.string.sex_male;
    }
}