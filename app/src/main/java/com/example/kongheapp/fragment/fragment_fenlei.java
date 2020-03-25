package com.example.kongheapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.kongheapp.R;
import com.example.kongheapp.activity.QuanBuActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_fenlei extends Fragment {


    ListView fenleilist;
    GridView fenleigrid;
    TextView fenleiyingyongname;
    ArrayList<HashMap<String,Object>> yingyongitems;
    SimpleAdapter simpleAdapter;
    LinearLayout fenleiquanbu;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fenlei,container,false);
        fenleiquanbu = view.findViewById(R.id.fenleiquanbu);
        fenleilist=view.findViewById(R.id.yingyongfenlei);
        fenleigrid=view.findViewById(R.id.fenleigrid);
        fenleiyingyongname=view.findViewById(R.id.fenleiyingyongname);
        return view;
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
        String[] actionTexts = new String[]{
                "日常应用",
                "第三方应用",
                "其他应用",
        };
        yingyongitems = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i< actionTexts.length;++i){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("action_name", actionTexts[i]);
            yingyongitems.add(map);
        }
        simpleAdapter = new SimpleAdapter(getActivity(), yingyongitems, R.layout.fenleiyingong_item,
                new String[]{"action_name"},
                new int[]{R.id.fenleiyingyongname});
        fenleilist.setAdapter(simpleAdapter);


    }
}
