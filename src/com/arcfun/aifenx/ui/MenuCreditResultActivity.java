package com.arcfun.aifenx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.ScoreInfo;
import com.arcfun.aifenx.utils.Utils;

public class MenuCreditResultActivity extends BaseActivity implements
        OnClickListener {
    private TextView mAccount, mScore;
    private Button mReturnBtn;
    private ImageView mHomeView;
    private ScoreInfo mScoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_credit);
        ((AFXApplication) getApplication()).addActivity(this);

        initView();
    }

    private void initView() {
        bindView(R.string.credit_finish, null, 0);

        mAccount = (TextView) findViewById(R.id.score_result_account);
        mScore = (TextView) findViewById(R.id.score_result_score);
        mReturnBtn = (Button) findViewById(R.id.score_back);
        mHomeView = (ImageView) findViewById(R.id.score_result_home);
        mReturnBtn.setOnClickListener(this);
        mHomeView.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mScoreInfo = getIntent().getParcelableExtra("score_result");
        if (mScoreInfo != null) {
            mAccount.setText(mScoreInfo.getName() + "("
                    + Utils.formatPhoneNumber(mScoreInfo.getMobile()) + ")");
            mScore.setText(String.valueOf(mScoreInfo.getScore()));
        } else {
            mAccount.setText("test" + "("
                    + Utils.formatPhoneNumber("18717728257") + ")");
            mScore.setText(String.valueOf(200));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.score_back:
            enterCredit();
            break;
        case R.id.score_result_home:
            launchHome();
            break;
        default:
            super.onClick(v);
            break;
        }
    }

    private void enterCredit() {
        Intent intent = new Intent(this, MenuCreditNewActivity.class);
        intent.putExtra("score_info", mScoreInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }

}