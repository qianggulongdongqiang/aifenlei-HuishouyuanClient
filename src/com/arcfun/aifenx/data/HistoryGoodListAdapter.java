package com.arcfun.aifenx.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcfun.aifenx.R;

public class HistoryGoodListAdapter extends BaseAdapter {
    private HistoryGoodInfo[] mItems;

    public HistoryGoodListAdapter(HistoryGoodInfo[] data) {
        this.mItems = data;
    }

    @Override
    public int getCount() {
        return (mItems != null) ? mItems.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
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
                    R.layout.item_history_goods, parent, false);
            holder.goodName = (TextView) convertView
                    .findViewById(R.id.item_history_good_name);
            holder.goodNum = (TextView) convertView
                    .findViewById(R.id.item_history_good_num);
            holder.goodUnit = (TextView) convertView
                    .findViewById(R.id.item_history_good_unit);
            holder.goodPoint = (TextView) convertView
                    .findViewById(R.id.item_history_good_credit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.goodName.setText(mItems[position].getName());
        holder.goodNum
                .setText(String.valueOf(mItems[position].getNum()));
        holder.goodUnit.setText(mItems[position].getUnit());
        holder.goodPoint.setText(String
                .valueOf(mItems[position].getPoints()));
        return convertView;
    }

    static class ViewHolder {
        TextView goodName;
        TextView goodNum;
        TextView goodUnit;
        TextView goodPoint;
    }

}