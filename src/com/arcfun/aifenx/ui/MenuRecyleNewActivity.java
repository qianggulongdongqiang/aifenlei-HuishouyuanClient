package com.arcfun.aifenx.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.CategoryListAdapter;
import com.arcfun.aifenx.data.CategoryListAdapter.Callback;
import com.arcfun.aifenx.data.GoodInfo;
import com.arcfun.aifenx.data.PaperListAdapter;
import com.arcfun.aifenx.data.PhoneInfo;
import com.arcfun.aifenx.data.PhoneInfoListAdapter;
import com.arcfun.aifenx.data.PreGoodInfo;
import com.arcfun.aifenx.data.PreOrderInfo;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;
import com.arcfun.aifenx.zxing.client.CaptureActivity;

public class MenuRecyleNewActivity extends BaseActivity implements
        OnClickListener, TextWatcher, Callback {
    private static final String TAG = "Recyle|New";
    private ImageView mAddView;
    private Button mNextBtn;
    private ListView mListView, mPaperList;
    private EditText mInputPaper;
    private Button mScanBtn;
    private TextView mEnsureBtn; 
    private CategoryListAdapter mAdapter;
    private List<GoodInfo> mItems;
    private AutoCompleteTextView mInput;
    private boolean isPreOrder;
    private PreOrderInfo mInfo;
    private List<PhoneInfo> mPhoneNumbers = new ArrayList<PhoneInfo>();
    private PhoneInfoListAdapter mPhoneNumberAdapter = null;
    private PaperListAdapter mPaperAdapter = null;
    private boolean isLoaded = false;
    private static final int REQUEST_SCAN_PAPER = 203;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        ((AFXApplication) getApplication()).addActivity(this);
        mInfos = ((AFXApplication) getApplication()).getInfo();

        initView();
    }

    private void initView() {
        bindView(R.string.new_order, null, 3);
        isPreOrder = getIntent().getBooleanExtra("is_pre_order", false);

        mInputPaper = (EditText) findViewById(R.id.input_rfid);
        mScanBtn = (Button) findViewById(R.id.recyle_scan);
        mEnsureBtn = (TextView) findViewById(R.id.recyle_input_ok);
        mInput = (AutoCompleteTextView) findViewById(R.id.input_number);
        mPhoneNumberAdapter = new PhoneInfoListAdapter(this,
                R.layout.phone_spinner_item, mPhoneNumbers);
        mInput.setAdapter(mPhoneNumberAdapter);
        mInput.addTextChangedListener(this);
        mListView = (ListView) findViewById(R.id.create_list);
        mPaperList = (ListView) findViewById(R.id.recyle_rfid_list);
        mAddView = (ImageView) LayoutInflater.from(this).inflate(
                R.layout.activity_create_order_foot, mListView, false);
        mNextBtn = (Button) findViewById(R.id.create_next);
        mScanBtn.setOnClickListener(this);
        mEnsureBtn.setOnClickListener(this);
        mAddView.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mPaperAdapter = new PaperListAdapter();
        mPaperList.setAdapter(mPaperAdapter);

        mItems = new ArrayList<GoodInfo>();
        if (!isPreOrder) {
            mItems.add(mInfos.get(0));
        } else {
            mInfo = getIntent().getParcelableExtra("pre_order");
            mInput.setText(mInfo.getMobile());
            PreGoodInfo[] preGoods = mInfo.getPreGood();
            for (PreGoodInfo info : preGoods) {
                LogUtils.d(TAG, "initView num:" + info.getNum());
                mItems.add(getGoodFromId(Integer.valueOf(info.getId()),
                        info.getNum()));
            }
            if (mInfo.getMobile().length() >= 6 && !isLoaded) {
                requestAddMobiles(Utils.buildAllMobile(
                        SharedPreferencesUtils.getOwnerToken(this),
                        Utils.normalizeNumber(mInfo.getMobile().substring(0, 6))));
            }
        }
        mListView.addFooterView(mAddView);
        mAdapter = new CategoryListAdapter(mInfos, mItems, this);
        mListView.setAdapter(mAdapter);
    }

    private GoodInfo getGoodFromId(int id, float num) {
        for (GoodInfo goodInfo : mInfos) {
            if (goodInfo.getId() == id) {
                goodInfo.setVendor(num);
                LogUtils.d(TAG, "GoodInfo:" + goodInfo.toString() + ", num"
                        + num);
                return goodInfo;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.create_add:
            addItem();
            break;
        case R.id.create_next:
            enterNext();
            break;
        case R.id.recyle_scan:
            startScan();
            break;
        case R.id.recyle_input_ok:
            addPaper();
            break;

        default:
            super.onClick(v);
            break;
        }
    }

    private void addPaper() {
        String paper = mInputPaper.getText().toString().trim();
        if (paper.length() <= 0) {
            Utils.showMsg(this, R.string.invalid_msg);
            return;
        }
        mInputPaper.setText("");
        mPaperAdapter.add(paper);
    }

    private void startScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_PAPER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SCAN_PAPER) {
                String result = data.getStringExtra("SCAN_RESULT");
                mPaperAdapter.add(result);
            }
        }
    }

    private void addItem() {
        GoodInfo one = new GoodInfo(mInfos.get(0));
        LogUtils.d(TAG, "add " + one.toString());
        mItems.add(one);
        mAdapter.notifyDataSetChanged();
    }

    private void enterNext() {
        ArrayList<GoodInfo> goods = new ArrayList<GoodInfo>();
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            GoodInfo info = (GoodInfo) mAdapter.getItem(i);
            goods.add(info);
        }
        if (isOrderValid(mItems)) {
            Intent intent = new Intent(this, MenuRecyleNewEnsureActivity.class);
            intent.putExtra("is_pre_order", isPreOrder);
            intent.putExtra("pre_order", mInfo);
            intent.putExtra("new_number", mInput.getText().toString());
            intent.putParcelableArrayListExtra("new_goods", goods);
            String code = Utils.formatArrayList(mPaperAdapter.getData());
            if (!code.isEmpty()) {
                intent.putExtra("pre_code", code);
            }
            startActivity(intent);
        } else {
            Utils.showMsg(this, R.string.invalid_msg);
        }
    }

    private boolean isOrderValid(final List<GoodInfo> items) {
        if (TextUtils.isEmpty(mInput.getText().toString()) ||
                mInput.getText().length() <= 6) {
            return false;
        }
        for (GoodInfo item : items) {
            if (item.getVendor() <= 0) {
                LogUtils.d(TAG, "empty item: " + item.getName());
                return false;
            }
        }
        return true;
    }

    private void requestAddMobiles(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.USER_GET_ALL_MOBILE;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestAddMobiles:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                List<PhoneInfo> addResult = null;
                if (result != null) {
                    addResult = Utils.parseAllMobile(result);
                }
                if (addResult != null && addResult.size() > 0) {
                    LogUtils.d(TAG, "requestAddMobiles onPostExecute:"
                            + addResult.size() + "," + mInput.getThreshold());
                    isLoaded = true;
                    mPhoneNumberAdapter.clear();
                    for (PhoneInfo number : addResult) {
                        mPhoneNumberAdapter.add(number);
                    }
                }
                mInput.setThreshold(7);
            };
        }.execute(url);
    }

    @Override
    public void onUpdate(View v) {
        int index = (Integer) v.getTag();
        mItems.remove(index);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 1) {
            isLoaded = false;
            mInput.setThreshold(15);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 6 || (s.toString().length() > 6 && !isLoaded)) {
            requestAddMobiles(Utils.buildAllMobile(
                    SharedPreferencesUtils.getOwnerToken(this),
                    Utils.normalizeNumber(s.toString().substring(0, 6))));
        }
    }
}