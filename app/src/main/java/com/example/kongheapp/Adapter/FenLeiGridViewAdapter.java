package com.example.kongheapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.R;
import com.example.kongheapp.activity.FanyiActivity;
import com.example.kongheapp.activity.GsdActivity;
import com.example.kongheapp.activity.ImageYsActivity;
import com.example.kongheapp.activity.QrcodeActivity;
import com.example.kongheapp.activity.TianQiActivity;
import com.example.kongheapp.db.App;
import com.example.kongheapp.huodong.CompassActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class FenLeiGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> mList;

    public FenLeiGridViewAdapter(Context mContext,ArrayList<HashMap<String, Object>> mList) {
        super();
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return this.mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.fenleigrid_layout, null, false);
            holder.textView = (TextView) convertView.findViewById(R.id.fenleitest);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (this.mList != null) {
            HashMap<String, Object> hashMap = this.mList.get(position);
            if (holder.textView != null) {
                holder.textView.setText(hashMap.get("content").toString());

            }
        }


        return convertView;

    }


    private class ViewHolder {
       TextView textView;
    }
}
