package com.arcfun.aifenx.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.arcfun.aifenx.R;

public class MenuCreateResultActivity extends BaseActivity implements OnClickListener {
    private TextView mResult;
    private Button mReturnBtn;
    private boolean isSuccess;
    private int titleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_owner);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    private void initView() {
        isSuccess = getIntent().getBooleanExtra("add_result", true);
        titleId = getIntent().getIntExtra("id_success", -1);
        bindView(R.string.ensure_new, null, 0);
        mResult = (TextView) findViewById(R.id.result_msg);
        mReturnBtn =  (Button) findViewById(R.id.result_back);
        mReturnBtn.setOnClickListener(this);
        if (!isSuccess) {
            mResult.setText(R.string.ensure_fail_detail);
        }
        if (titleId > 0) {
            mResult.setText(titleId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.result_back:
            launchHome();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

}