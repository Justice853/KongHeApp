package com.example.kongheapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.kongheapp.Adapter.FenLeiListViewAdapter;
import com.example.kongheapp.Adapter.GridViewAdapter;
import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.Util.NoScrollGridView;
import com.example.kongheapp.activity.FanyiActivity;
import com.example.kongheapp.activity.GsdActivity;
import com.example.kongheapp.activity.ImageYsActivity;
import com.example.kongheapp.activity.QrcodeActivity;
import com.example.kongheapp.activity.QuanBuActivity;
import com.example.kongheapp.activity.TianQiActivity;
import com.example.kongheapp.db.App;
import com.example.kongheapp.huodong.CompassActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_fenlei extends Fragment {


    ListView fenleilist;
    NoScrollGridView fenleigrid;
    TextView fenleiyingyongname;
    ArrayList<App> yingyongitems;
    FenLeiListViewAdapter fenLeiListViewAdapter;
    LinearLayout fenleiquanbu;
    String[] quanbuApp = {"天气查询","指南针","快速翻译"};
    String[] quanbuApp1 = {"二维码制作","图片压缩"};
    String[] quanbuApp2 = {"归属地查询"};
    int zt;
    private FenLeiListViewAdapter mListViewAdapter;
    private ArrayList<ArrayList<HashMap<String,Object>>> mArrayList;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fenlei,container,false);
        fenleiquanbu = view.findViewById(R.id.fenleiquanbu);
        fenleilist=view.findViewById(R.id.yingyongfenlei);
         zt = ((MainActivity)getActivity()).getZt();
        fenleigrid=view.findViewById(R.id.fenleigrid);
        fenleiyingyongname=view.findViewById(R.id.fenleiyingyongname);
        initData();
        mListViewAdapter=new FenLeiListViewAdapter(mArrayList, getContext());
        fenleilist.setAdapter(mListViewAdapter);

        return view;
    }

    private void initData() {
        mArrayList=new ArrayList<ArrayList<HashMap<String,Object>>>();
        HashMap<String, Object> hashMap=null;
        ArrayList<HashMap<String,Object>> arrayListForEveryGridView;
        arrayListForEveryGridView = new ArrayList<HashMap<String, Object>>();
        for (int j = 0; j < quanbuApp.length; j++) {
                hashMap = new HashMap<String, Object>();
                hashMap.put("content", quanbuApp[j]);

                arrayListForEveryGridView.add(hashMap);

            }
        mArrayList.add(arrayListForEveryGridView);

        ArrayList<HashMap<String,Object>> arrayListForEveryGridView1;
        arrayListForEveryGridView1 = new ArrayList<HashMap<String, Object>>();
        for (int j = 0; j < quanbuApp1.length; j++) {
            hashMap = new HashMap<String, Object>();
            hashMap.put("content", quanbuApp1[j]);

            arrayListForEveryGridView1.add(hashMap);

        }
        mArrayList.add(arrayListForEveryGridView1);

        ArrayList<HashMap<String,Object>> arrayListForEveryGridView2;
        arrayListForEveryGridView2 = new ArrayList<HashMap<String, Object>>();
        for (int j = 0; j < quanbuApp2.length; j++) {
            hashMap = new HashMap<String, Object>();
            hashMap.put("content", quanbuApp2[j]);

            arrayListForEveryGridView2.add(hashMap);

        }
        mArrayList.add(arrayListForEveryGridView2);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fenleiquanbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuanBuActivity.class);
                startActivity(intent);
            }
        });


    }


}
