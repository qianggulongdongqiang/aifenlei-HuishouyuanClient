package com.arcfun.aifenx.data;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcfun.aifenx.R;

public class PaperListAdapter extends BaseAdapter implements OnClickListener {
    private List<String> mPapers = new ArrayList<String>();

    public PaperListAdapter() {
    }

    public List<String> getData() {
        return mPapers;
    }

    public void clear(int index) {
        mPapers.remove(index);
        notifyDataSetChanged();
    }

    public void add(String id) {
        mPapers.add(0, id);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPapers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPapers.get(position);
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
                    R.layout.item_paper, parent, false);
            holder.delete = (TextView) convertView
                    .findViewById(R.id.item_paper_delete);
            holder.paper = (TextView) convertView
                    .findViewById(R.id.item_paper_msg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.delete.setTag(position);
        holder.delete.setOnClickListener(this);
        holder.paper.setText(mPapers.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView delete;
        TextView paper;
    }

    @Override
    public void onClick(View v) {
        clear((Integer) v.getTag());
    }

}