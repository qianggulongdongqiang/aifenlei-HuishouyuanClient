package com.arcfun.aifenx.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.GoodInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class BaseFragmentActivity extends FragmentActivity
        implements OnClickListener {
    private static final String TAG = "BaseActivity";
    protected List<GoodInfo> mInfos = new ArrayList<GoodInfo>();
    private TextView mBackTitle, mActionBarTitle;
    private ImageView mBack, mExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor();
    }

    // type = 0 none; 1 left; 2 right; 3 all
    protected void bindView(int title, String back, int type) {
        mBackTitle = (TextView) findViewById(R.id.actionbar_left);
        mActionBarTitle = (TextView) findViewById(R.id.actionbar_title);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mExit = (ImageView) findViewById(R.id.actionbar_close);
        mActionBarTitle.setText(title);

        switch (type) {
        case 0:
            mBack.setVisibility(View.GONE);
            mExit.setVisibility(View.GONE);
            break;
        case 1:
            mBack.setOnClickListener(this);
            mExit.setVisibility(View.GONE);
            break;
        case 2:
            mBack.setVisibility(View.GONE);
            mExit.setImageResource(R.drawable.out);
            mExit.setOnClickListener(this);
            break;
        case 3:
            mBack.setOnClickListener(this);
            mExit.setOnClickListener(this);
            break;

        default:
            break;
        }
    }

    protected void bindView(String title, String back, int type) {
        mBackTitle = (TextView) findViewById(R.id.actionbar_left);
        mActionBarTitle = (TextView) findViewById(R.id.actionbar_title);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mExit = (ImageView) findViewById(R.id.actionbar_close);
        mActionBarTitle.setText(title);

        switch (type) {
        case 0:
            mBack.setVisibility(View.GONE);
            mExit.setVisibility(View.GONE);
            break;
        case 1:
            mBack.setOnClickListener(this);
            mExit.setVisibility(View.GONE);
            break;
        case 2:
            mBack.setVisibility(View.GONE);
            mExit.setImageResource(R.drawable.out);
            mExit.setOnClickListener(this);
            break;
        case 3:
            mBack.setOnClickListener(this);
            mExit.setOnClickListener(this);
            break;

        default:
            break;
        }
    }

    protected void bindView(int title, int back) {
        mBackTitle = (TextView) findViewById(R.id.actionbar_left);
        mActionBarTitle = (TextView) findViewById(R.id.actionbar_title);
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mExit = (ImageView) findViewById(R.id.actionbar_close);
        mExit.setOnClickListener(this);
        mActionBarTitle.setText(title);

        mBackTitle.setVisibility(View.VISIBLE);
        mBackTitle.setText(back);
        mBackTitle.setOnClickListener(this);
        mBack.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        LogUtils.d(TAG, "onClick" + v.getClass().getSimpleName());
        switch (v.getId()) {
        case R.id.actionbar_back:
        case R.id.actionbar_left:
            finish();
            overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            break;

        default:
            break;
        }
    }

    public void setWindowStatusBarColor() {
        /*int colorResId = R.color.white;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(colorResId));
                window.setNavigationBarColor(getResources()
                        .getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    protected void launchHome() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    protected void launchLogin() {
        SharedPreferencesUtils.resetOwnerToken(this, "");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

    protected void requestSecGoods() {
        String url = HttpRequest.URL_HEAD + HttpRequest.GET_SEC_GOOD;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], "");
                if (data == null || data.length == 0) return "";

                Utils.dumpJson2Db(BaseFragmentActivity.this, data, "usergoods");
                String result = new String(data);
                LogUtils.d(TAG, "SecGoods:" + result);
                mInfos = Utils.parseJsonData(result);
                ((AFXApplication) getApplication()).setInfo(mInfos);
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result == null || result.isEmpty()) {
                    byte[] raw = Utils.getdumpFromDb(BaseFragmentActivity.this,
                            "usergoods");
                    if (raw != null) {
                        LogUtils.d(TAG, "onPostExecute goods from Db");
                        String dump = new String(raw);
                        mInfos = Utils.parseJsonData(dump);
                        ((AFXApplication) getApplication()).setInfo(mInfos);
                    }
                    Utils.showMsg(BaseFragmentActivity.this, R.string.login_error);
                    return;
                }
            }
        }.execute(url);
    }

}