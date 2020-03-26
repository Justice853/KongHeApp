package com.example.kongheapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.activity.FanyiActivity;
import com.example.kongheapp.activity.GsdActivity;
import com.example.kongheapp.activity.ImageYsActivity;
import com.example.kongheapp.activity.QrcodeActivity;
import com.example.kongheapp.activity.TianQiActivity;
import com.example.kongheapp.db.App;
import com.example.kongheapp.fragment.fragment_fenlei;
import com.example.kongheapp.huodong.CompassActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FenLeiListViewAdapter extends BaseAdapter {
    private ArrayList<ArrayList<HashMap<String, Object>>> mList;
    private Context mContext;
    String[] quanbuApp = {"日常应用","第三方应用","其他应用"};
    int zt;
    public FenLeiListViewAdapter(ArrayList<ArrayList<HashMap<String, Object>>> mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        zt = ((MainActivity)mContext).getZt();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.fenleiyingong_item, null, false);
            holder.textView = (TextView) convertView.findViewById(R.id.fenleiyingyongname);
            holder.gridView = (GridView) convertView.findViewById(R.id.fenleigrid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (this.mList != null) {
            if (holder.textView != null) {
                holder.textView.setText(quanbuApp[position]);
            }
            if (holder.gridView != null) {
                ArrayList<HashMap<String, Object>> arrayListForEveryGridView = this.mList.get(position);
                FenLeiGridViewAdapter gridViewAdapter=new FenLeiGridViewAdapter(mContext,arrayListForEveryGridView);
                holder.gridView.setAdapter(gridViewAdapter);
                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView view1=view.findViewById(R.id.fenleitest);
                        if(view1.getText()=="天气查询"){
                            Intent intent5 = new Intent(mContext,TianQiActivity.class);
                            intent5.putExtra("zt",zt);
                            mContext.startActivity(intent5);
                        }
                        else if (view1.getText()=="指南针"){
                            Intent intent = new Intent(mContext, CompassActivity.class);
                           mContext.startActivity(intent);
                        }else if(view1.getText()=="快速翻译"){
                            Intent intent6 = new Intent(mContext,FanyiActivity.class);
                            intent6.putExtra("zt",zt);
                            mContext.startActivity(intent6);
                        }else if (view1.getText()=="二维码制作"){
                            Intent intent2 = new Intent(mContext, QrcodeActivity.class);
                            intent2.putExtra("zt",zt);
                            mContext.startActivity(intent2);
                        }else if(view1.getText()=="图片压缩"){
                            Intent intent3 = new Intent(mContext, ImageYsActivity.class);
                            intent3.putExtra("zt",zt);
                            mContext.startActivity(intent3);
                        }
                        else if(view1.getText()=="归属地查询"){
                            Intent intent1 = new Intent(mContext, GsdActivity.class);
                            intent1.putExtra("zt",zt);
                            mContext.startActivity(intent1);
                        }
                    }
                });
            }

        }


        return convertView;

    }


    private class ViewHolder {
        TextView textView;
        GridView gridView;
    }

}

