package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.kongheapp.Controller.ActivityCollector;
import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.fragment.Fragment_home;

import java.util.ArrayList;
import java.util.HashMap;

public class SetActivity extends Activity {
    TextView title;
    ImageView fanhui;
    private ListView listView;
    ArrayList<HashMap<String,Object>> setitems;
    SimpleAdapter simpleAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_layout);
        title=findViewById(R.id.rt_title);
        fanhui=findViewById(R.id.returnimage);
        listView = findViewById(R.id.setmenu);

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
        simpleAdapter = new SimpleAdapter(getApplicationContext(), setitems, R.layout.fragment_zuoceitem,
                new String[]{ "action_name"},
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
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;

                }
            }
        });

    }
}
