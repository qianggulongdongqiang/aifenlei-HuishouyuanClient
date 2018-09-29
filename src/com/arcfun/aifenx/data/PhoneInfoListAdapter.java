package com.arcfun.aifenx.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.arcfun.aifenx.R;
import com.arcfun.aifenx.utils.Utils;

public class PhoneInfoListAdapter extends ArrayAdapter<PhoneInfo> {
    private List<PhoneInfo> mItems;
    private ArrayList<PhoneInfo> mOriginalValues;
    private final Object mLock = new Object();
    private String mErrorMsg;

    public PhoneInfoListAdapter(Context context, int resource,
            List<PhoneInfo> list) {
        super(context, resource, R.id.user_number, list);
        this.mItems = list;
        this.mErrorMsg = context.getResources().getString(R.string.account_no_found);
    }

    public interface Callback {
        void onUpdate(View v);
    }

    @Override
    public int getCount() {
        return (mItems != null) ? mItems.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PhoneInfo getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.phone_spinner_item, parent, false);
            holder.number = (TextView) convertView
                    .findViewById(R.id.user_number);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.number.setText(getItem(position).getNumber());
        holder.name.setText(getItem(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView name;
    }

    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mItems.clear();
            }
        }
        notifyDataSetChanged();
    }

    public void add(PhoneInfo object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mItems.add(object);
            }
        }
        notifyDataSetChanged();
    }

    public void sort(Comparator<? super PhoneInfo> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mItems, comparator);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                    FilterResults results) {
                mItems = (List<PhoneInfo>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                if (mOriginalValues == null) {
                    synchronized (mLock) {
                        mOriginalValues = new ArrayList<PhoneInfo>(mItems);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    ArrayList<PhoneInfo> list;
                    synchronized (mLock) {
                        list = new ArrayList<PhoneInfo>(mOriginalValues);
                    }
                    results.values = list;
                    results.count = list.size();
                } else {
                    String prefixString = Utils.normalizeNumber(prefix.toString().toLowerCase());

                    ArrayList<PhoneInfo> values;
                    synchronized (mLock) {
                        values = new ArrayList<PhoneInfo>(mOriginalValues);
                    }

                    final int count = values.size();
                    final ArrayList<PhoneInfo> newValues = new ArrayList<PhoneInfo>();

                    for (int i = 0; i < count; i++) {
                        final PhoneInfo value = values.get(i);
                        final String valueText = value.getNumber().toString()
                                .toLowerCase();

                        // First match against the whole
                        if (valueText.startsWith(prefixString)) {
                            newValues.add(value);
                        } else {
                            final String[] words = valueText.split(" ");
                            final int wordCount = words.length;

                            // Start at index 0, in case valueText starts with
                            // space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }

                    results.values = newValues;
                    results.count = newValues.size();
                    if (results.count == 0) {
                        newValues.add(new PhoneInfo(mErrorMsg, ""));
                        results.values = newValues;
                        results.count = newValues.size();
                    }
                }

                return results;
            }
        };
        return filter;
    }
}