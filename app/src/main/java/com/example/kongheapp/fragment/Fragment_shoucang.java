package com.example.kongheapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.kongheapp.Adapter.GridShouCangAdapter;
import com.example.kongheapp.Adapter.GridViewAdapter;
import com.example.kongheapp.Adapter.ViewHolder;
import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.activity.FanyiActivity;
import com.example.kongheapp.activity.GsdActivity;
import com.example.kongheapp.activity.ImageYsActivity;
import com.example.kongheapp.activity.QrcodeActivity;
import com.example.kongheapp.activity.QuanBuActivity;
import com.example.kongheapp.activity.TianJiaActivity;
import com.example.kongheapp.activity.TianQiActivity;
import com.example.kongheapp.db.App;
import com.example.kongheapp.huodong.CompassActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Fragment_shoucang extends Fragment {
    GridView gridView;
    int zt;
    private GridShouCangAdapter mAdapter;
    FloatingActionButton floatingActionButton;
    private ArrayList<App> apps;
    ViewHolder holder;
    String[] quanbuApp = {"天气查询","指南针","快速翻译","二维码制作","图片压缩","归属地查询"};
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_shoucang,container,false);
        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        zt = ((MainActivity)getActivity()).getZt();
        gridView=(GridView)view.findViewById(R.id.appshoucang);
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set set=sharedPreferences.getStringSet("tjid",null);
        List list = new ArrayList(set);
        App mapps;

        apps=new ArrayList<App>();
        for(int i=0;i<list.size();i++){
            mapps=new App();
            int lg = Integer.parseInt(list.get(i).toString());
            mapps.setName(quanbuApp[lg]);
            apps.add(mapps);
        }
        mAdapter= new GridShouCangAdapter(getActivity(),apps);
        gridView.setAdapter(mAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TianJiaActivity.class);
                intent.putExtra("zt",zt);
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView view1=view.findViewById(R.id.qb_wenzi);
                if(view1.getText()=="天气查询"){
                    Intent intent5 = new Intent(getActivity(), TianQiActivity.class);
                    intent5.putExtra("zt",zt);
                    startActivity(intent5);
                }
                else if (view1.getText()=="指南针"){
                    Intent intent = new Intent(getActivity(), CompassActivity.class);
                    startActivity(intent);
                }else if(view1.getText()=="快速翻译"){
                    Intent intent6 = new Intent(getActivity(),FanyiActivity.class);
                    intent6.putExtra("zt",zt);
                    startActivity(intent6);
                }else if (view1.getText()=="二维码制作"){
                    Intent intent2 = new Intent(getActivity(), QrcodeActivity.class);
                    intent2.putExtra("zt",zt);
                    startActivity(intent2);
                }else if(view1.getText()=="图片压缩"){
                    Intent intent3 = new Intent(getActivity(), ImageYsActivity.class);
                    intent3.putExtra("zt",zt);
                    startActivity(intent3);
                }
                else if(view1.getText()=="归属地查询"){
                    Intent intent1 = new Intent(getActivity(), GsdActivity.class);
                    intent1.putExtra("zt",zt);
                    startActivity(intent1);
                }

            }
        });
    }

}
