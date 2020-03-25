package com.example.kongheapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kongheapp.Adapter.GridViewAdapter;
import com.example.kongheapp.Adapter.ViewHolder;
import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.db.App;
import com.example.kongheapp.db.Zt;
import com.example.kongheapp.fragment.Fragment_home;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TianJiaActivity extends BaseActivity {
    TextView title;
    ImageView fanhui;
    private GridView gd;
    private GridViewAdapter mAdapter;
    private ArrayList<App> apps;
    private int checkNum; // 记录选中的条目数量
    List<String> selectID = new ArrayList<String>(); //选中的ID
    FloatingActionButton floatingActionButton;
    String[] quanbuApp = {"天气查询","指南针","快速翻译","二维码制作","图片压缩","归属地查询"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int zt =intent.getIntExtra("zt",0);
        if(zt==1){
            //设置主题
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.AppLightTheme);
        }
        setContentView(R.layout.fragment_grid);
        title=findViewById(R.id.rt_title);
        fanhui=findViewById(R.id.returnimage);
        floatingActionButton=findViewById(R.id.floatbutton);
        apps=new ArrayList<App>();
        title.setText("收藏页面");
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gd=findViewById(R.id.grid_check);
        initData();

        gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//                System.out.println("1234");
////                ViewHolder holder = (ViewHolder)view.getTag();
////                holder.cb.toggle();
////                GridViewAdapter.getIsSelected().put(position,holder.cb.isChecked());
                ViewHolder holder = (ViewHolder) arg1.getTag();
                holder.cb.toggle();
                GridViewAdapter.getIsSelected().put(arg2, holder.cb.isChecked());
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectID.clear();
                System.out.println(mAdapter.getIsSelected().size());
                for (int i = 0; i < mAdapter.getIsSelected().size(); i++) {
                    if (mAdapter.getIsSelected().get(i)) {

                        selectID.add(apps.get(i).getId());
                    }
                    System.out.println(mAdapter.getIsSelected().get(i));
                }

                if (selectID.size() == 0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                            TianJiaActivity.this);
                    builder1.setMessage("没有选中任何记录");
                    builder1.show();
                } else {
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < selectID.size(); i++) {
                        sb.append("ID=" + selectID.get(i) + "  ");
                        System.out.println(selectID.get(i));
                    }
//                    AlertDialog.Builder builder2 = new AlertDialog.Builder(TianJiaActivity.this);
////                    builder2.setMessage(sb.toString());
////                    builder2.show();
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(TianJiaActivity.this).edit();
                    Set set = new HashSet<String>(selectID);
                    editor.putStringSet("tjid",set);
                    editor.apply();
                    editor.commit();
                    Intent intent = new Intent(TianJiaActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void initData() {
        App mapps;
        for(int i=0;i<quanbuApp.length;i++){
            mapps=new App();
            mapps.setName(quanbuApp[i]);
            mapps.setId(i+"");
            apps.add(mapps);
        }
        mAdapter= new GridViewAdapter(apps,this);
        gd.setAdapter(mAdapter);
    }
}
