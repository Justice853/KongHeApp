package com.example.kongheapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.Controller.ActivityCollector;
import com.example.kongheapp.Util.CheckPremissionUtil;
import com.example.kongheapp.activity.LgsuccessActivity;
import com.example.kongheapp.activity.LoginActivity;
import com.example.kongheapp.activity.QuanBuActivity;
import com.example.kongheapp.activity.SetActivity;
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
    int zt=0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        final boolean isDark = readMode();
        if(isDark){
            //设置主题
            zt=1;
            setTheme(R.style.AppDarkTheme);

        }else{
            zt=0;
            setTheme(R.style.AppLightTheme);

    }
        setContentView(R.layout.fragment_menu);
        if(Build.VERSION.SDK_INT>=21){
//检查权限
            String[] permissions = CheckPremissionUtil.checkPermission(this);
            if (permissions.length == 0) {
                //权限都申请了

            } else {
                //申请权限 ,参数2:权限数组,参数3:请求码code
                ActivityCompat.requestPermissions(this, permissions, 100);
                //把执行结果的操作给EasyPermissions的onRequestPermissionsResult
            }
        }
        ActivityCollector.addActivity(this);
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
                "主题切换",
                "设置",
                "关于",
                "退出",
        };
        int[] actionImg = new int[]{
                R.drawable.geren,
                R.drawable.suoyouwenjian,
                R.drawable.zhutiqiehuan,
                R.drawable.shezhi,
                R.drawable.guanyu,
                R.drawable.tuichu,
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
                        sharedPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
                        if(sharedPreferences.getString("login","").equals("1")){
                            Intent intent4 = new Intent(MainActivity.this, LgsuccessActivity.class);
                            intent4.putExtra("zt",zt);
                            startActivity(intent4);
                        }else {
                            Intent intent4 = new Intent(MainActivity.this, LoginActivity.class);
                            intent4.putExtra("zt",zt);
                            startActivity(intent4);
                        }

                        break;
                    case 1:
                        Intent intent3 = new Intent(MainActivity.this, QuanBuActivity.class);
                        intent3.putExtra("zt",zt);
                        startActivity(intent3);
                        break;

                    case 2:
                        boolean isDark = readMode();
                        saveMode(!isDark);
                        finish();
                        Intent intent2 = getIntent();
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;
                    case 3:
//                        Intent intent1 = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
//                        startActivity(intent1);
                        Intent intent1 = new Intent(MainActivity.this, SetActivity.class);
                        intent1.putExtra("zt",zt);
                        startActivity(intent1);
                        break;
                    case 4:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent, new Fragment_home()).commit();
//                        tv_title.setText("关于");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://www.cxp853.top/"));
                        startActivity(intent);
                        break;
                    case 5:
                        ActivityCollector.finishAll();
                        break;
                }
            }
        });
        setHome();
    }
    public void setHome(){
        getSupportFragmentManager().beginTransaction().replace(R.id.pcontent,new Fragment_home()).commit();
        tv_title.setText("空盒");
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

    private void saveMode(boolean isDark) {
        SharedPreferences sp = getSharedPreferences("setting", 0);
        sp.edit().putBoolean("dark_mode", isDark).commit();
    }
    private boolean readMode() {
        SharedPreferences sp = getSharedPreferences("setting", 0);
        boolean isDark = sp.getBoolean("dark_mode", false);
        return isDark;
    }

}
