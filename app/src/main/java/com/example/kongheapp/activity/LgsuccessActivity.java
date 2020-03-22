package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;

public class LgsuccessActivity extends Activity {
    TextView title;
    ImageView fanhui;
    TextView tv_account;
    Button bt_exit_login;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.person_layout);
        title=findViewById(R.id.rt_title);
        fanhui=findViewById(R.id.returnimage);
        tv_account=findViewById(R.id.account_name);
        bt_exit_login=findViewById(R.id.bt_exit_login);
        sharedPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
        String accountname=sharedPreferences.getString("telphone","");
        tv_account.setText(accountname);
        bt_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
                editor=sharedPreferences.edit();
                editor.putString("login","0");
                editor.commit();
                finish();
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LgsuccessActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        title.setText("用户界面");



    }
}
