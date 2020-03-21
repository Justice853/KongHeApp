package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.kongheapp.R;
import com.example.kongheapp.gson.Weather;

public class TianQiActivity extends FragmentActivity {
    int zt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
         zt =intent.getIntExtra("zt",0);
        if(zt==1){
            //设置主题
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.AppLightTheme);
        }
        setContentView(R.layout.tianqi_layout);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getString("weather",null)!=null){
            Intent intent1  = new Intent(this,WeatherActivity.class);
            intent1.putExtra("zt",zt);
            startActivity(intent1);
            finish();
        }
    }
}
