package com.arcfun.aifenx.data;

import java.util.ArrayList;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.utils.LogUtils;
import com.arcfun.aifenx.view.SpinnerView;
import com.arcfun.aifenx.view.SpinnerView.onSpinnerSelectedListener;

public class CategoryListAdapter extends BaseAdapter implements
        OnClickListener, onSpinnerSelectedListener {
    private static final String TAG = "Adapter";
    private List<GoodInfo> raw;
    private List<GoodInfo> mItems;
    private List<String> mSpinnerRes;
    private Callback mListerner;

    public CategoryListAdapter(List<GoodInfo> data, List<GoodInfo> items,
            Callback callback) {
        this.raw = data;
        this.mItems = items;
        this.mListerner = callback;
        initRes(data);
    }

    public interface Callback {
        void onUpdate(View v);
    }

    private void initRes(List<GoodInfo> data) {
        mSpinnerRes = new ArrayList<String>();
        if (data == null || data.size() == 0)
            return;

        for (GoodInfo info : data) {
            mSpinnerRes.add(info.getName());
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
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
                    R.layout.item_new_order, parent, false);
            holder.delete = (ImageView) convertView
                    .findViewById(R.id.create_del);
            holder.spinner = (SpinnerView) convertView
                    .findViewById(R.id.item_category);
            holder.price_title = (TextView) convertView
                    .findViewById(R.id.item_title);
            holder.price = (EditText) convertView.findViewById(R.id.item_price);
            holder.price_unit = (TextView) convertView
                    .findViewById(R.id.item_unit);
            holder.spinner.setData(mSpinnerRes, holder, this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.price.setTag(position);
        holder.price.addTextChangedListener(new CreditTextWatcher(holder));
        float vendor = mItems.get(position).getVendor();
        if (vendor > 0) {
            holder.price.setText(String.valueOf(vendor));
        } else {
            holder.price.setText("");
        }
        holder.delete.setTag(position);
        holder.delete.setOnClickListener(this);
        holder.spinner.setText(mItems.get(position).getName());
        holder.price_title.setText(mItems.get(position).getUnit_name());
        holder.price_unit.setText(mItems.get(position).getUnit());
        float num = mItems.get(position).getVendor();
        if (num > 0) {
            holder.price.setText(String.valueOf(num));
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView delete;
        SpinnerView spinner;
        TextView price_title;
        EditText price;
        TextView price_unit;
    }

    class CreditTextWatcher implements TextWatcher {
        private ViewHolder viewHolder;

        public CreditTextWatcher(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int a, int b, int c) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                int pos = (Integer) viewHolder.price.getTag();
                mItems.get(pos).setVendor(Float.valueOf(s.toString()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mListerner != null) {
            mListerner.onUpdate(v);
        }
    }

    @Override
    public void onSelected(Object obj, int pos) {
        ViewHolder holder = (ViewHolder) obj;
        GoodInfo select = new GoodInfo(raw.get(pos));
        int index = (Integer) holder.delete.getTag();
        LogUtils.d(TAG, "onSelected " + pos + ", index " + index);
        mItems.set(index, select);
        mItems.get(index).setVendor(Float.valueOf(0));
        holder.price_title.setText(mItems.get(index).getUnit_name());
        holder.price_unit.setText(mItems.get(index).getUnit());
        holder.price.setText("");
    }

}