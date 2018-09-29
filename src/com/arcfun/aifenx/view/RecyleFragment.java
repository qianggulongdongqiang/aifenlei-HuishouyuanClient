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
import com.arcfun.aifenx.data.PreOrderInfo;
import com.arcfun.aifenx.data.PreOrderListAdapter;
import com.arcfun.aifenx.net.HttpRequest;
import com.arcfun.aifenx.ui.MenuRecyleDetailActivity;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.utils.SharedPreferencesUtils;
import com.arcfun.aifenx.utils.Utils;

public class RecyleFragment extends Fragment implements OnItemClickListener,
        OnScrollListener {
    private static final String TAG = "RecyleFragment";
    private List<PreOrderInfo> mPreInfos = new ArrayList<PreOrderInfo>();
    private ListView mListView;
    private PreOrderListAdapter mAdapter;
    private boolean isLoadFinished = true;
    private static final int MAX_PAGE_COUNT = 10;
    private OnActionCallBack mListener;
    private int mIndex;

    public RecyleFragment() {}

    public RecyleFragment(OnActionCallBack listener, int index) {
        this.mListener = listener;
        this.mIndex = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history_item, container,
                false);
        initView(view);
        LogUtils.d(TAG, "onCreateView " + "zxz");
        return view;
    }

    public void reload(boolean refresh) {
        mPreInfos.clear();
        if (refresh) {
            mAdapter.notifyDataSetChanged();
        }
        requestPreOrder(Utils.buildPreOrderJson(
                SharedPreferencesUtils.getOwnerToken(getActivity()), 0,
                MAX_PAGE_COUNT, getType(mIndex)));
    }

    private void initView(View view) {
        if (mPreInfos == null || mPreInfos.size() == 0) {
            requestPreOrder(Utils.buildPreOrderJson(
                    SharedPreferencesUtils.getOwnerToken(getActivity()), 0,
                    MAX_PAGE_COUNT, getType(mIndex)));
        }

        mListView = (ListView) view.findViewById(R.id.history_list);
        mAdapter = new PreOrderListAdapter(getActivity(), mPreInfos, mIndex,
                mListener);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
    }

    private int getType(int index) {
        switch (index) {
            case 0:
                return 2;
            case 1:
                return 1;
            case 2:
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        enterDetail(mPreInfos.get(position));
    }

    private void enterDetail(PreOrderInfo info) {
        Intent intent = new Intent(getActivity(),
                MenuRecyleDetailActivity.class);
        intent.putExtra("pre_order", info);
        intent.putExtra("is_New", mIndex <= 0);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_enter,
                R.anim.anim_exit);
    }

    private void requestPreOrder(final String json) {
        String url = HttpRequest.URL_HEAD + HttpRequest.USER_GET_PRE;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                byte[] data = HttpRequest.sendPost(params[0], json);
                if (data == null) {
                    return null;
                }
                String result = new String(data);
                LogUtils.d(TAG, "requestPreOrder:" + result);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                List<PreOrderInfo> preInfos = new ArrayList<PreOrderInfo>();
                if (result != null) {
                    preInfos = Utils.parsePreOrder(result);
                }
                if (preInfos != null && preInfos.size() > 0) {
                    for (PreOrderInfo preInfo : preInfos) {
                        mPreInfos.add(preInfo);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                isLoadFinished = true;
            }
        }.execute(url);
    }

    public interface OnActionCallBack {
        void onUpdate(int index);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == (view.getCount() - 1)
                    && isLoadFinished) {
                isLoadFinished = false;
                LogUtils.d(TAG, "onScrollStateChanged " + view.getCount());
                requestPreOrder(Utils.buildPreOrderJson(
                        SharedPreferencesUtils.getOwnerToken(getActivity()), 1,
                        view.getCount(), getType(mIndex)));
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
    }
}