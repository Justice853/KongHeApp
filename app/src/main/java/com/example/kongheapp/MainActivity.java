package com.example.kongheapp;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.fragment.Fragment_home;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends FragmentActivity {
    private SlidingPaneLayout slidinglayout;
    private TextView mTextMessage;
    TextView tv_title;
    ImageView ihome;
    ImageView iSliding;
    ArrayList<HashMap<String,Object>>actionItems,guanlist;
    SimpleAdapter simpleAdapter,guansimpleAdapte;
    private ListView listView,guanlistview;
    private static boolean flag=false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                    tv_title.setText("主页");
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                    tv_title.setText("所有应用");
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                    tv_title.setText("我的");
                    return true;
            }
            return false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

        ihome= findViewById(R.id.homeimage);
        tv_title=findViewById(R.id.tv_title);
        iSliding=findViewById(R.id.menuimage);
        slidinglayout=findViewById(R.id.sliding);
        listView=findViewById(R.id.leftmenu);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ihome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHome();
            }
        });
        iSliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidinglayout.isOpen()) {
                    slidinglayout.closePane();
                } else {
                    slidinglayout.openPane();
                }
            }
        });


        String[] actionTexts = new String[]{
                "我的账户",
                "全部应用",
                "添加快捷方式",
                "主题管理",
                "设置",
                "关于",
                "退出",
        };
        int[] actionImg = new int[]{
                R.drawable.btn_l_star,
                R.drawable.btn_l_download,
                R.drawable.btn_l_book,
                R.drawable.btn_l_download,
                R.drawable.btn_l_download,
                R.drawable.btn_l_book,
                R.drawable.btn_l_download,
        };
        actionItems = new ArrayList<HashMap<String, Object>>();

        for(int i=0;i< actionImg.length;++i){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("action_icon", actionImg[i]);
            map.put("action_name", actionTexts[i]);
            actionItems.add(map);
        }
        simpleAdapter = new SimpleAdapter(getApplicationContext(), actionItems, R.layout.fragment_zuoceitem,
                new String[]{"action_icon", "action_name"},
                new int[]{R.id.zuotu, R.id.zuozi});
        listView.setAdapter(simpleAdapter);
        listView.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                        tv_title.setText("我的账户");
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                        tv_title.setText("全部应用");
                        break;

                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                        tv_title.setText("添加快捷方式");
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                        tv_title.setText("主题管理");
                        break;
                    case 4:
                        Intent intent1 = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                        startActivity(intent1);
                        break;
                    case 5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
                        tv_title.setText("关于");
                        break;
                    case 6:
                        tuichu();
                        break;
                }
            }
        });
        setHome();
    }
    public void setHome(){
        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent,new Fragment_home()).commit();
        tv_title.setText("空盒App");
    }
    public boolean onKeyDown(int KeyCode, KeyEvent event) {

        if (KeyCode == KeyEvent.KEYCODE_BACK) {
            if (event.isLongPress()) {
                Toast.makeText(this, "changan", Toast.LENGTH_LONG).show();
                finish();
                return super.onKeyDown(KeyCode, event);
            } else {
                tuichu();
                return true;
            }

        } else {
            return super.onKeyDown(KeyCode, event);
        }
    }
    public void tuichu() {
        if (!flag) {
            flag = true;
            Toast toast = Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag = false;
                }
            }, 2000);

        } else {
            flag = false;
            finish();
        }
    }
}
