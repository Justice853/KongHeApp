package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    Button bt_get_otp = null;
    Button bt_submit_register = null;
    EditText et_telphone = null;
    EditText et_otpCode = null;
    EditText et_username = null;
    EditText et_password1 = null;
    EditText et_password2 = null;

    String account = "";
    String password = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    int zt;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setOnClickListener();
        Intent intent = getIntent();
        zt =intent.getIntExtra("zt",0);
        setOnFocusChangeErrMsg(et_telphone, "phone", "手机号格式不正确");
        setOnFocusChangeErrMsg(et_password1, "password", "密码必须不少于6位");

        Intent it_from_login = getIntent();
        account = it_from_login.getStringExtra("account");
        // 把对应的account设置到telphone输入框
        et_telphone.setText(account);
    }

    private void setOnFocusChangeErrMsg(final EditText editText, final String inputType, final String errMsg) {
        editText.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String inputStr = editText.getText().toString();
                        if (!hasFocus) {
                            if (inputType == "phone") {
                                if (isTelphoneValid(inputStr)) {
                                    editText.setError(null);
                                } else {
                                    editText.setError(errMsg);
                                }
                            }
                            if (inputType == "password") {
                                if (isPasswordValid(inputStr)) {
                                    editText.setError(null);
                                } else {
                                    editText.setError(errMsg);
                                }
                            }

                        }
                    }
                }
        );
    }

    private boolean isTelphoneValid(String account) {
        if (account == null) {
            return false;
        }
        // 首位为1, 第二位为3-9, 剩下九位为 0-9, 共11位数字
        String pattern = "^[1]([3-9])[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(account);
        return m.matches();
    }

    // 校验密码不少于6位
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


    private void setOnClickListener() {
        bt_get_otp.setOnClickListener(this);
        bt_submit_register.setOnClickListener(this);
    }

    private void initView() {
            bt_get_otp = findViewById(R.id.bt_get_otp);
            bt_submit_register = findViewById(R.id.bt_submit_register);
            et_telphone = findViewById(R.id.et_telphone);
            et_otpCode = findViewById(R.id.et_otpCode);
            et_username = findViewById(R.id.et_username);
            et_password1 = findViewById(R.id.et_password);
            et_password2 = findViewById(R.id.et_password2);
    }

    @Override
    public void onClick(View v) {
        String telphone = et_telphone.getText().toString();
        String otpCode = et_otpCode.getText().toString();
        String username = et_username.getText().toString();
        String password1 = et_password1.getText().toString();
        String password2 = et_password2.getText().toString();
        switch (v.getId()) {
            case R.id.bt_get_otp:
                // 点击获取验证码按钮响应事件
                if (TextUtils.isEmpty(telphone)) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this,"验证成功，当前版本验证码暂时用不到",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_submit_register:
                sp = getSharedPreferences("login_info", MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("token", "token_value");
                editor.putString("login","1");
                editor.putString("telphone", telphone);
                editor.putString("password", password1); // 注意这里是password1
                if (editor.commit()) {
                    Intent intent = new Intent(this,LgsuccessActivity.class);
                    intent.putExtra("zt",zt);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
}
