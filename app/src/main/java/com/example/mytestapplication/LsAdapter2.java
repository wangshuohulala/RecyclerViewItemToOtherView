package com.example.mytestapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LsAdapter2 extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater mInflater = null;
    private boolean isVisible = true;
    /**
     * 要删除的position
     */
    public int remove_position = -1;
    private int[] bg = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7};

    public LsAdapter2(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList != null)
            return mList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_item, null);
        ImageView tv = view.findViewById(R.id.item_tv);
        tv.setBackgroundResource(bg[position]);
//        tv.setText(mList.get(position));
//        if (!isVisible && (position == -1 + mList.size())) {
//            tv.setText("");
//        }
//        if (remove_position == position) {
//            tv.setText("");
//        }

        return view;
    }

}
