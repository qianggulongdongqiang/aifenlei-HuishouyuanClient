package com.arcfun.aifenx.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.CreditOwnerInfo;
import com.arcfun.aifenx.data.ScoreInfo;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.Utils;
import com.arcfun.aifenx.view.SpinnerView;

public class MenuCreditNewActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "Recyle|New";
    private Button mNextBtn;
    private EditText mInputScore, mInputDetail;
    private TextView mAccount, mScore;
    private SpinnerView mScoreSpinner;
    private ArrayList<String> mType = new ArrayList<String>();
    private ScoreInfo mScoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_main);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    private void initView() {
        bindView(R.string.credit_new_title, null, 1);
        mAccount = (TextView) findViewById(R.id.score_detail_account);
        mScore = (TextView) findViewById(R.id.score_detail_score);
        mScoreSpinner = (SpinnerView) findViewById(R.id.spinner_ways);
        mInputScore = (EditText) findViewById(R.id.score_detail_input);
        mInputDetail = (EditText) findViewById(R.id.score_name_input);
        mNextBtn = (Button) findViewById(R.id.score_next);
        mNextBtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mScoreInfo = getIntent().getParcelableExtra("score_info");

        String[] type = getResources().getStringArray(R.array.score_list);
        for (String item : type) {
            mType.add(item);
        }
        mScoreSpinner.setData(mType, null, null);
        if (mScoreInfo == null) {
            mScoreInfo = new ScoreInfo(0, "", 0, "", "0", 0);
        }
        mAccount.setText(mScoreInfo.getName() + "(" +
                Utils.formatPhoneNumber(mScoreInfo.getMobile()) + ")");
        mScore.setText(String.valueOf(mScoreInfo.getScore()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.score_next:
            createDialog();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void createDialog() {
        LogUtils.d(TAG, "createDialog score= " + mScoreInfo.getScore());
        if (mInputScore.getText().toString().length() <= 0) {
            Utils.showMsg(this, R.string.invalid_msg);
            return;
        }
        if (Integer.valueOf(mInputScore.getText().toString()) > mScoreInfo.getScore()) {
            Utils.showMsg(this, R.string.invalid_credit);
            return;
        }
        CreditOwnerInfo info = new CreditOwnerInfo(mScoreInfo.getId(),
                mScoreInfo.getMobile(), mScoreInfo.getScore(),
                Integer.valueOf(mInputScore.getText().toString()),
                mScoreSpinner.getText(), mInputDetail.getText().toString());
        PopCreditDialog dialog = new PopCreditDialog(this, info);
        dialog.show();
    }

}