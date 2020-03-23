package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.kongheapp.MainActivity;

public class StartPageActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(1000);//使程序休眠一秒
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
