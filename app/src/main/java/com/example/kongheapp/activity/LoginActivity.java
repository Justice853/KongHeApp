package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity implements View.OnClickListener {
    Button bt_login;
    EditText et_account;
    EditText et_password;
    TextView tv_to_register;
    TextView tv_forget_password;
    TextView tv_service_agreement;
    ImageView iv_third_method1;
    ImageView iv_third_method2;
    ImageView iv_third_method3;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String token;
    private String token_telphone;
    private String token_password;
    int zt;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
         zt =intent.getIntExtra("zt",0);
        setContentView(R.layout.activity_login);
        initView();
        setOnClickListener();
        setOnFocusChangeErrMsg(et_account,"phone","手机号格式不正确");
        setOnFocusChangeErrMsg(et_password,"password","密码必须不少于6位");
    }



    private void initView() {
        bt_login = findViewById(R.id.bt_login); // 登录按钮
        et_account = findViewById(R.id.et_account); // 输入账号
        et_password = findViewById(R.id.et_password); // 输入密码
        tv_to_register = findViewById(R.id.tv_to_register); // 注册
        tv_forget_password = findViewById(R.id.tv_forget_password); // 忘记密码
        tv_service_agreement = findViewById(R.id.tv_service_agreement); // 同意协议
        iv_third_method1 = findViewById(R.id.iv_third_method1); // 第三方登录方式1
        iv_third_method2 = findViewById(R.id.iv_third_method2); // 第三方登录方式2
        iv_third_method3 = findViewById(R.id.iv_third_method3); // 第三方登录方式3
    }

    private void setOnClickListener(){
        bt_login.setOnClickListener(this);
        tv_to_register.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_service_agreement.setOnClickListener(this);
        iv_third_method1.setOnClickListener(this);
        iv_third_method2.setOnClickListener(this);
        iv_third_method3.setOnClickListener(this);
    }
    public void onClick(View v){
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        switch (v.getId()){
            case R.id.bt_login:
                et_password.clearFocus();
                if(!(isTelphoneValid(account)&&(isPasswordValid(password)))){
                    Toast.makeText(this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    break;
                }
                sharedPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("token", "token_value");
                editor.putString("telphone", account);
                editor.putString("login","1");
                editor.putString("password", password);
                if (editor.commit()) {
                   Intent intent = new Intent(this,LgsuccessActivity.class);
                    intent.putExtra("zt",zt);
                   startActivity(intent);

                    finish();
                } else {
                    showToastInThread(LoginActivity.this, "token保存失败，请重新登录");
                }

                break;
            case R.id.tv_to_register://注册页面
                Intent register = new Intent(this, RegisterActivity.class);
                register.putExtra("account", account);
                register.putExtra("zt",zt);
                startActivity(register);
                break;
            case R.id.tv_forget_password://忘记密码
                break;
            case R.id.tv_service_agreement://服务协议
                break;
            case R.id.iv_third_method1://以下是第三方登录
                break;
            case R.id.iv_third_method2:
                break;
            case R.id.iv_third_method3:
                break;

        }

    }
    private void setOnFocusChangeErrMsg(final EditText et, final String type, final String errmsg) {
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputStr = et.getText().toString();
                if(!hasFocus){
                    if(type == "phone"){
                        if (isTelphoneValid(inputStr)){
                            et.setError(null);
                        }else {
                            et.setError(errmsg);
                        }
                    }
                    if(type == "password"){
                        if(isPasswordValid(inputStr)){
                            et.setError(null);
                        }else {
                            et.setError(errmsg);
                        }
                    }
                }
            }
        });
    }

    private boolean isPasswordValid(String inputStr) {
        return inputStr !=null && inputStr.trim().length()>5;
    }

    private boolean isTelphoneValid(String inputStr) {
        if(inputStr == null){
            return false;
        }
        String pattern = "[1]([3-9])[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(inputStr);
        return m.matches();
    }
    private void showToastInThread(final Context context, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
