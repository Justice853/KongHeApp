package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.kongheapp.R;
import com.example.kongheapp.huodong.CompassActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class QuanBuActivity extends Activity {
    TextView title;
    ImageView fanhui;
    GridView application;
    SimpleAdapter adapter;
    ArrayList<HashMap<String,Object>> appitems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final int zt =intent.getIntExtra("zt",0);
        if(zt==1){
            //设置主题
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.AppLightTheme);
        }
        setContentView(R.layout.quanbu_layout);
        title = findViewById(R.id.rt_title);
        fanhui = findViewById(R.id.returnimage);
        application = findViewById(R.id.application);
        title.setText("全部应用");

        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String[] quanbuApp = {"天气查询","指南针","快递查询","二维码制作","图片压缩","归属地查询"};
        appitems = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i< quanbuApp.length;++i){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("action_name", quanbuApp[i]);
            appitems.add(map);
        }
        adapter  = new SimpleAdapter(getApplicationContext(),appitems,R.layout.quanbu_item,new String[]{"action_name"},new int[]{R.id.qb_wenzi});

        application.setAdapter(adapter);
        application.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        Intent intent = new Intent(QuanBuActivity.this, CompassActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        Intent intent1 = new Intent(QuanBuActivity.this, GsdActivity.class);
                        intent1.putExtra("zt",zt);
                        startActivity(intent1);
                        break;
                }
            }
        });
    }
}
