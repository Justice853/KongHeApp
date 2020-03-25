package com.example.kongheapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kongheapp.R;
import com.example.kongheapp.db.App;

import java.util.List;

public class GridShouCangAdapter extends BaseAdapter {
    private Context context;
    private List<App> data;
    private LayoutInflater inflater = null;
    public GridShouCangAdapter(Context context, List<App> data) {
        super();
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.quanbu_item,null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView
                    .findViewById(R.id.qb_wenzi);
            convertView.setTag(holder);// 如果convertView为空就 把holder赋值进去
        } else {
            holder = (ViewHolder) convertView.getTag();// 如果convertView不为空，那么就在convertView中getTag()拿出来
        }
        holder.tv.setText(data.get(position).getName());
        return convertView;
    }



}
