package com.example.kongheapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.Controller.ActivityCollector;
import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.fragment.Fragment_home;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Looper.loop;
import static android.os.Looper.prepare;

public class SetActivity extends BaseActivity {
    TextView title;
    ImageView fanhui;
    private ListView listView;
    ArrayList<HashMap<String,Object>> setitems;
    SimpleAdapter simpleAdapter;
    private static boolean flag=false;
    int panduan1=1;
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
        setContentView(R.layout.set_layout);
        title=findViewById(R.id.rt_title);
        fanhui=findViewById(R.id.returnimage);

        listView = findViewById(R.id.setmenu);
        //添加下划线
        listView.addFooterView(new TextView(getBaseContext()));

        title.setText("设置");
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] actionTexts = new String[]{
                "关于",
                "检查更新",
                "清除缓存",
                "意见反馈",
        };
       setitems = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i< actionTexts.length;++i){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("action_name", actionTexts[i]);
            setitems.add(map);
        }
        simpleAdapter = new SimpleAdapter(getApplicationContext(), setitems, R.layout.set_item,
                new String[]{"action_name"},
                new int[]{R.id.set_wenzi});
        listView.setAdapter(simpleAdapter);
        listView.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.cxp853.top/"));
                        startActivity(intent);
                        break;
                    case 1:
                        GengxinDialog();
                        break;
                    case 2:
                        QingchuDialog();
                        break;
                    case 3:
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("http://www.cxp853.top/"));
                        startActivity(intent1);
                        break;


                }
            }
        });

    }

    private void QingchuDialog(){
        final ProgressDialog dialog = new ProgressDialog(SetActivity.this);
        dialog.setTitle("清除APP缓存中");
        dialog.setMessage("等待");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        t.start();

    }
    private void GengxinDialog(){
        final ProgressDialog dialog = new ProgressDialog(SetActivity.this);
        dialog.setTitle("检查是否存在更新");
        dialog.setMessage("正在检查");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               dialog.dismiss();
                prepare();
                tishiDialog();
                loop();
            }
        });
        t.start();

    }
    private void tishiDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
        builder.setTitle("暂时没有新的版本");
        builder.setMessage("请耐心等待新的功能");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        builder.create().show();
    }



}
