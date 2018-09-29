package com.arcfun.aifenx.data;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.ui.MenuRecyleDetailActivity;
import com.arcfun.aifenx.ui.PopOrderDialog;
import com.arcfun.aifenx.view.RecyleFragment.OnActionCallBack;

public class PreOrderListAdapter extends BaseAdapter {
    private List<PreOrderInfo> mItems;
    private Activity activity;
    private boolean isNew = true;
    private int mIndex;
    private OnActionCallBack mCallBack;

    public PreOrderListAdapter(Activity activity, List<PreOrderInfo> data,
            int index, OnActionCallBack callback) {
        this.activity = activity;
        this.mItems = data;
        this.mIndex = index;
        this.isNew = index <= 0;
        this.mCallBack = callback;
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
                    R.layout.item_pre_order, parent, false);
            holder.orderRfid = (ImageView) convertView
                    .findViewById(R.id.pre_order_rfid);
            holder.orderSn = (TextView) convertView
                    .findViewById(R.id.pre_order_sn);
            holder.orderTime = (TextView) convertView
                    .findViewById(R.id.pre_order_time);
            holder.orderCategory = (TextView) convertView
                    .findViewById(R.id.pre_order_category);
            holder.orderNumber = (TextView) convertView
                    .findViewById(R.id.pre_order_number);
            holder.orderManagerTitle = (TextView) convertView
                    .findViewById(R.id.pre_order_manage_title);
            holder.orderManager = (TextView) convertView
                    .findViewById(R.id.pre_order_manager);
            holder.orderAction = (Button) convertView
                    .findViewById(R.id.pre_order_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;
        holder.orderAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNew) {
                    enterDetail(mItems.get(pos));
                } else {
                    createDialog(mItems.get(pos));
                }
            }
        });

        holder.orderRfid.setVisibility(mItems.get(position).getRfidCount()
                < 1 ? View.VISIBLE : View.GONE);
        holder.orderSn.setText(mItems.get(position).getSn());
        holder.orderTime.setText(mItems.get(position).getFinishedTime());
        holder.orderCategory.setText(mItems.get(position).getGoodsName());
        holder.orderNumber.setText(mItems.get(position).getBuyerPhone());
        holder.orderManager.setText(mItems.get(position).getCollecterName());
        updateAction(holder, isNew);
        return convertView;
    }

    private void createDialog(PreOrderInfo info) {
        PopOrderDialog dialog = new PopOrderDialog(activity, mIndex,
                info.getId(), mCallBack);
        dialog.show();
    }

    private void updateAction(ViewHolder holder, boolean isNew) {
        holder.orderManagerTitle.setVisibility(isNew ? View.GONE : View.VISIBLE);
        holder.orderManager.setVisibility(isNew ? View.GONE : View.VISIBLE);
        holder.orderAction.setText(activity.getResources().getString(
                isNew ? R.string.manage_receive : R.string.manage_refuse));
        holder.orderAction.setBackground(activity.getResources().getDrawable(
                isNew ? R.drawable.dialog_button_dark
                        : R.drawable.dialog_button_light));
    }

    private void enterDetail(PreOrderInfo info) {
        Intent intent = new Intent(activity, MenuRecyleDetailActivity.class);
        intent.putExtra("pre_order", info);
        intent.putExtra("is_New", isNew);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }

    static class ViewHolder {
        ImageView orderRfid;
        TextView orderSn;
        TextView orderTime;
        TextView orderCategory;
        TextView orderNumber;
        TextView orderManagerTitle;
        TextView orderManager;
        Button orderAction;
    }

}