package com.arcfun.aifenx.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.data.HistoryOrderInfo;
import com.arcfun.aifenx.data.HistoryOrderListAdapter;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.ui.MenuRecyleDetailActivity;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class HistoryFragment extends Fragment implements OnItemClickListener,
        OnScrollListener {
    private static final String TAG = "HistoryFragment";
    private List<HistoryOrderInfo> mHistoryInfos = new ArrayList<HistoryOrderInfo>();
    private ListView mListView;
    private HistoryOrderListAdapter mAdapter;
    private boolean isLoadFinished = true;
    private static final int MAX_PAGE_COUNT = 10;
    private String mFrom, mTo;
    private int mTotalCredit = 0;
    private OnInfoCallBack mListener;
    private int mIndex;

    public HistoryFragment() {}

    public HistoryFragment(String from, OnInfoCallBack listener, int index) {
        this.mListener = listener;
        this.mIndex = index;
        this.mFrom = from;
        this.mTo = Utils.getTodayDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history_item, container,
                false);
        initView(view);
        LogUtils.d(TAG, "onCreateView " + mFrom);
        return view;
    }

    private void initView(View view) {
        if (mHistoryInfos == null || mHistoryInfos.size() == 0) {
            requestAllOrder(Utils.buildAllOrderJson(
                    SharedPreferencesUtils.getOwnerToken(getActivity()), 0,
                    MAX_PAGE_COUNT, mFrom, mTo));
        }

        mListView = (ListView) view.findViewById(R.id.history_list);
        mAdapter = new HistoryOrderListAdapter(mHistoryInfos);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        LogUtils.d(TAG, "onItemClick" + pos);
        enterDetail(mHistoryInfos.get(pos));
    }

    private void enterDetail(HistoryOrderInfo info) {
        Intent intent = new Intent(getActivity(),
                MenuRecyleDetailActivity.class);
        intent.putExtra("history_order", info);
        intent.putExtra("is_history", true);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_enter,
                R.anim.anim_exit);
    }

    private void requestAllOrder(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.USER_GET_ORDER;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestAllOrder:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                List<HistoryOrderInfo> preInfos = new ArrayList<HistoryOrderInfo>();
                if (result != null) {
                    preInfos = Utils.parseAllOrder(getActivity(), result);
                }
                if (preInfos != null && preInfos.size() > 0) {
                    setCredit(preInfos.get(0));
                    for (HistoryOrderInfo preInfo : preInfos) {
                        mHistoryInfos.add(preInfo);
                    }
                    mAdapter.notifyDataSetChanged();
                    if (mListener != null) {
                        mListener
                                .onUpdate(mIndex, String.valueOf(mTotalCredit));
                    }
                }
                isLoadFinished = true;
            }
        }.execute(url);
    }

    protected void setCredit(HistoryOrderInfo orderInfo) {
        mTotalCredit = orderInfo.getPoint();
    }

    public String getCredit() {
        return String.valueOf(mTotalCredit);
    }

    public interface OnInfoCallBack {
        void onUpdate(int index, String sum);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == (view.getCount() - 1)
                    && isLoadFinished) {
                isLoadFinished = false;
                LogUtils.d(TAG, "onScrollStateChanged " + view.getCount());
                requestAllOrder(Utils.buildAllOrderJson(
                        SharedPreferencesUtils.getOwnerToken(getActivity()), 1,
                        view.getCount(), mFrom, mTo));
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
    }
}