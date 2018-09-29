package com.arcfun.aifenx.data;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.utils.Utils;
import com.arcfun.aifenx.view.BlockedListView;

public class HistoryOrderListAdapter extends BaseAdapter {
    private List<HistoryOrderInfo> mItems;

    public HistoryOrderListAdapter(List<HistoryOrderInfo> data) {
        this.mItems = data;
    }

    @Override
    public int getCount() {
        return (mItems != null) ? mItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_history_order, parent, false);
            holder.orderTime = (TextView) convertView
                    .findViewById(R.id.history_order_time);
            holder.orderGood = (BlockedListView) convertView
                    .findViewById(R.id.block_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.orderTime.setText(mItems.get(position).getFinishedTime());
        HistoryGoodListAdapter adapter = new HistoryGoodListAdapter(mItems.get(
                position).getHistoryGood());
        holder.orderGood.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(holder.orderGood);
        return convertView;
    }

    static class ViewHolder {
        TextView orderTime;
        BlockedListView orderGood;
    }

}