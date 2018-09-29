package com.arcfun.aifenx.ui;

import android.app.Dialog;
import android.content.Context;
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
import com.arcfun.aifenx.view.RecyleFragment.OnActionCallBack;

public class PopOrderDialog extends Dialog implements OnClickListener {
    private static final String TAG = "PopOrder";
    private Button mQuit, mBack;
    private Context mContext;
    private int mPreOrderId;
    private int mPageIndex;
    private OnActionCallBack mCallBack;

    public PopOrderDialog(Context context, int index, int id, OnActionCallBack callback) {
        this(context, index, id, R.style.PopDialog);
        this.mCallBack = callback;
    }

    private PopOrderDialog(Context context, int index, int id, int themeResId) {
        super(context, themeResId);
        mContext = context.getApplicationContext();
        mPreOrderId = id;
        mPageIndex = index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_layout);
        mQuit = (Button) findViewById(R.id.dialog_order_ok);
        mBack = (Button) findViewById(R.id.dialog_order_back);
        mQuit.setOnClickListener(this);
        mBack.setOnClickListener(this);

        initView();
    }

    private void initView() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_order_ok:
                String token = SharedPreferencesUtils.getOwnerToken(mContext);
                requestUnbindPreOrder(Utils.buildTokenIdJson(token, mPreOrderId));
                break;
            case R.id.dialog_order_back:
                break;
            default:
                break;
        }
        dismiss();
    }

    private void requestUnbindPreOrder(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.UNBIND_PRE_ORDER;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestUnbindPreOrder:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                ResultInfo info = null;
                if (result != null) {
                    info = Utils.parseResultInfo(result);
                }
                if (info != null) {
                    if (info.getCode() == Utils.RESULT_OK) {
                        reloadOrder();
                    }
                    Utils.showMsg(mContext, info.getMsg());
                } else {
                    Utils.showMsg(mContext, R.string.login_error);
                }
            }
        }.execute(url);
    }

    protected void reloadOrder() {
        LogUtils.d(TAG, "reloadOrder success");
        if (mCallBack != null) {
            mCallBack.onUpdate(mPageIndex);
        }
    }
}