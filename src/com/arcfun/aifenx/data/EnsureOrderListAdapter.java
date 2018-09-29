package com.arcfun.aifenx.data;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.utils.Utils;

public class EnsureOrderListAdapter extends BaseAdapter {
    private List<GoodInfo> mGoodInfos;

    public EnsureOrderListAdapter(List<GoodInfo> data) {
        this.mGoodInfos = data;
    }

    @Override
    public int getCount() {
        return (mGoodInfos != null) ? mGoodInfos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mGoodInfos.get(position);
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
                    R.layout.item_ensure_order, parent, false);
            holder.goodCategory = (TextView) convertView.findViewById(R.id.item_ensure_category);
            holder.good_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.good_num = (TextView) convertView.findViewById(R.id.item_ensure_price);
            holder.good_unit = (TextView) convertView.findViewById(R.id.item_unit);
            holder.good_point = (TextView) convertView.findViewById(R.id.item_ensure_credit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.goodCategory.setText(mGoodInfos.get(position).getName());
        holder.good_title.setText(mGoodInfos.get(position).getUnit_name());
        holder.good_num.setText(String.valueOf(mGoodInfos.get(position).getVendor()));
        holder.good_unit.setText(mGoodInfos.get(position).getUnit());
        holder.good_point.setText(String.valueOf(Utils.formatInt(
                mGoodInfos.get(position).getVendor() * mGoodInfos.get(position).getPoint())));
        return convertView;
    }

    static class ViewHolder {
        TextView goodCategory;
        TextView good_title;
        TextView good_num;
        TextView good_unit;
        TextView good_point;
    }
}