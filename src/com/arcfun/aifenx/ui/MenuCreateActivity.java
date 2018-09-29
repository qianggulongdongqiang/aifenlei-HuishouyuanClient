package com.arcfun.aifenx.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.AddOwnerInfo;
import com.arcfun.aifenx.data.AreaInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;
import com.arcfun.aifenx.view.SpinnerView;
import com.arcfun.aifenx.view.SpinnerView.onSpinnerSelectedListener;
import com.arcfun.aifenx.zxing.client.CaptureActivity;

public class MenuCreateActivity extends BaseActivity implements
        OnClickListener, onSpinnerSelectedListener {
    private EditText mAccount, mName, mNumber, mAddr;
    private RadioGroup mRadioGroup;
    private Button mNextBtn, mScanBtn;
    private SpinnerView mCitySpinner, mDisSpinner;
    ArrayList<String> mCity = new ArrayList<String>();
    SparseArray<ArrayList<String>> mDistricts = new SparseArray<ArrayList<String>>();

    private static final int REQUEST_SCAN_EPC = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_owner);
        ((AFXApplication) getApplication()).addActivity(this);

        initData();
        initView();
    }

    private void initData() {
        requestArea();
    }

    private void initView() {
        bindView(R.string.menu_new, null, 3);

        mAccount = (EditText) findViewById(R.id.input_account);
        mName = (EditText) findViewById(R.id.input_name);
        mNumber = (EditText) findViewById(R.id.input_number);
        mAddr = (EditText) findViewById(R.id.input_addr);
        mNextBtn = (Button) findViewById(R.id.create_next);
        mScanBtn = (Button) findViewById(R.id.scan);
        mNextBtn.setOnClickListener(this);
        mScanBtn.setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mCitySpinner = (SpinnerView) findViewById(R.id.spinner_city);
        mDisSpinner = (SpinnerView) findViewById(R.id.spinner_district);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.create_next:
            if (isEmpty(mName) || isEmpty(mNumber) || isEmpty(mAddr)) {
                Utils.showMsg(this, R.string.invalid_msg);
                return;
            }
            requestBuildOwner();
            break;
        case R.id.scan:
            startScan();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void startScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_EPC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SCAN_EPC) {
                String result = data.getStringExtra("SCAN_RESULT");
                mAccount.setText(result);
                mAccount.setSelection(mAccount.getText().length());
            }
        }
    }

    private String getSexType() {
        int id = mRadioGroup.getCheckedRadioButtonId();
        switch (id) {
        case R.id.male:
            return "1";
        case R.id.female:
            return "2";
        default:
            break;
        }
        return "1";
    }

    private boolean isEmpty(EditText view) {
        return TextUtils.isEmpty(view.getText().toString().trim());
    }

    private void requestBuildOwner() {
        String rfid = mAccount.getText().toString();
        String name = mName.getText().toString();
        String number = mNumber.getText().toString();
        String addr = mCitySpinner.getText() + mDisSpinner.getText()
                + mAddr.getText().toString();
        String token = SharedPreferencesUtils.getOwnerToken(this);
        requestAddOwner(new AddOwnerInfo(rfid, name, getSexType(), number,
                addr, token));
    }

    private void requestAddOwner(AddOwnerInfo info) {
        Intent intent = new Intent(this, MenuCreateEnsureActivity.class);
        intent.putExtra("add_owner", info);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    private void requestArea() {
        String url = HttpRequest.URL_HEAD + HttpRequest.USER_GET_AREA;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], "");
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                List<AreaInfo> areaInfos = null;
                if (result != null) {
                    areaInfos = Utils.parseArea(result);
                    /*areaInfos = new ArrayList<AreaInfo>();
                    AreaInfo area = null;
                    for (int i = 0; i < 3; i++) {
                        List<AreaDetailInfo> detailList = new ArrayList<AreaDetailInfo>();
                        for (int j = 0; j < 3; j++) {
                            AreaDetailInfo info = new AreaDetailInfo(1, i
                                    + "testa" + j);
                            detailList.add(info);
                        }
                        area = new AreaInfo(i, "area" + i, detailList);
                        areaInfos.add(area);
                    }*/
                }
                if (areaInfos != null) {
                    int index = 0;
                    for (AreaInfo areaInfo : areaInfos) {
                        mCity.add(areaInfo.getName());
                        ArrayList<String> district = new ArrayList<String>();
                        for (int i = 0; i < areaInfo.getInfo().size(); i++) {
                            district.add(areaInfo.getInfo().get(i).getName());
                        }
                        mDistricts.put(index, district);
                        index++;
                    }
                    mCitySpinner.setData(mCity, null, MenuCreateActivity.this);
                    mDisSpinner.setData(mDistricts.get(0), null, null);

                } else {
                    Utils.showMsg(MenuCreateActivity.this, R.string.login_error);
                }
            };
        }.execute(url);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onSelected(Object obj, int pos) {
        mDisSpinner.setData(mDistricts.get(pos), null, null);
    }
}