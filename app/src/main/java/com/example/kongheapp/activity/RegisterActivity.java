package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.constant.NetConstant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    Button bt_get_otp = null;
    Button bt_submit_register = null;
    EditText et_telphone = null;
    EditText et_otpCode = null;
    EditText et_username = null;
    EditText et_password1 = null;
    EditText et_password2 = null;
    RadioButton man = null;
    RadioGroup sexgroup;
    RadioButton woman = null;
    EditText et_age = null;
    int gender = 2;
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
            et_age=findViewById(R.id.et_age);
            woman=findViewById(R.id.woman);
            man=findViewById(R.id.man);
            sexgroup=findViewById(R.id.sexgroup);
            sexgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==man.getId()){
                        gender=1;
                    }
                    if(checkedId==woman.getId()){
                        gender=2;
                    }

                }
            });
    }

    @Override
    public void onClick(View v) {
        String telphone = et_telphone.getText().toString();
        String otpCode = et_otpCode.getText().toString();
        String username = et_username.getText().toString();
        String password1 = et_password1.getText().toString();
        String password2 = et_password2.getText().toString();
        String age = et_age.getText().toString();
        switch (v.getId()) {
            case R.id.bt_get_otp:
                // 点击获取验证码按钮响应事件
                if (TextUtils.isEmpty(telphone)) {
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(RegisterActivity.this,"验证成功，当前版本验证码暂时用不到",Toast.LENGTH_SHORT).show();
                    asyncGetOtpCode(telphone);
                }
                break;
            case R.id.bt_submit_register:
//                sp = getSharedPreferences("login_info", MODE_PRIVATE);
//                editor = sp.edit();
//                editor.putString("token", "token_value");
//                editor.putString("login","1");
//                editor.putString("telphone", telphone);
//                editor.putString("password", password1); // 注意这里是password1
//                if (editor.commit()) {
//                    Intent intent = new Intent(this,LgsuccessActivity.class);
//                    intent.putExtra("zt",zt);
//                    startActivity(intent);
//                    finish();
//                }
                asyncRegister(telphone, otpCode, username, gender, age, password1, password2);
                // 点击提交注册按钮响应事件
                // 尽管后端进行了判空，但Android端依然需要判空
                break;
        }
    }

    private void asyncGetOtpCode(final String telphone) {
        if (TextUtils.isEmpty(telphone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // okhttp的使用，POST，异步； 总共5步
                // 1、初始化okhttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(60000, TimeUnit.SECONDS)
                        .readTimeout(60000,TimeUnit.SECONDS)
                        .writeTimeout(60000, TimeUnit.SECONDS).build();

                //2.构建请求体
                RequestBody requestBody = new FormBody.Builder()
                        .add("telphone",telphone)
                        .build();
                //3.发送请求，POST
                Request request = new Request.Builder()
                        .url(NetConstant.getGetOtpCodeURL())
                        .post(requestBody).build();
                //4.使用okhttpclient对象获取请求的回调方法，enqueue方法代表异步执行
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("RegisterActivity", "onFailure: "+e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //判断服务器状态
                        String responeseStr = response.toString();
                        if(responeseStr.contains("200")){
                            String responseData = response.body().string();
                            JsonObject responseBodyJSONObject = (JsonObject) new JsonParser().parse(responseData);
                            //如果返回的status为success，则说明成功
                            if(getResponseStatus(responseBodyJSONObject).equals("successGetOtpCode")){
                               JsonObject dataObject = responseBodyJSONObject.get("data").getAsJsonObject();
                               if(!dataObject.isJsonNull()){
                                   String telphone = dataObject.get("telphone").getAsString();
                                   String otpCode = dataObject.get("otpCode").getAsString();
                                   //自动填写验证码
                                   setTextInThread(et_otpCode,otpCode);
                                   //子线程中显示TOAST
                                   showToastInThread(RegisterActivity.this, "验证码：" + otpCode);
                                   Log.d("RegisterActivity", "telphone: " + telphone + " otpCode: " + otpCode);
                               }
                                Log.d("RegisterActivity", "验证码已发送 ");
                            }else {
                                getResponseErrMsg(RegisterActivity.this, responseBodyJSONObject);
                            }
                        }else {
                            Log.d("RegisterActivity", "服务器异常");
                            showToastInThread(RegisterActivity.this, responeseStr);
                        }
                    }
                });
            }
        }).start();

    }
    private void asyncRegister(final String telphone, final String otpCode,
                               final String username, final int gender,
                               final String age, final String password1, final String password2){
        if (TextUtils.isEmpty(telphone) || TextUtils.isEmpty(otpCode) || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(String.valueOf(gender)) || TextUtils.isEmpty(age)
                || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            Toast.makeText(RegisterActivity.this, "存在输入为空，注册失败", Toast.LENGTH_SHORT).show();
        }else if(password1.equals(password2)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 2、构建请求体
                    //                    // 注意这里的name 要和后端接收的参数名一一对应，否则无法传递过去

                    RequestBody requestBody = new FormBody.Builder()
                            .add("telphone",telphone)
                            .add("otpCode",otpCode)
                            .add("name",username)
                            .add("gender",String.valueOf(gender))
                            .add("age",age)
                            .add("password",password1).build();
                    //3.发送请求，POST
                    Request request = new Request.Builder()
                            .url(NetConstant.getRegisterURL())
                            .post(requestBody)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("RegisterActivity", "onFailure: "+e.getMessage());
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseStr = response.toString();
                            if(responseStr.contains("200")){
                                String responseBodyStr = response.body().string();
                                JsonObject responseBodyJSONObject = (JsonObject)new JsonParser().parse(responseBodyStr);
                                //如果返回的status为success，代表通过
                                if(getResponseStatus(responseBodyJSONObject).equals("success")){
                                    sp =  getSharedPreferences("login_info", MODE_PRIVATE);
//                                    editor.putString("token", "token_value");
//                                    editor.putString("login","1");
//                                    editor.putString("telphone", telphone);
//                                    editor.putString("password", password1);
                                    editor = sp.edit();
                                    editor.putString("token", "token_value");
                                    editor.putString("telphone", telphone);
                                    editor.putString("password", password1);
                                    editor.putString("login","1");
                                    if (editor.commit()){
                                        Intent intent = new Intent(RegisterActivity.this,LgsuccessActivity.class);
                                        intent.putExtra("zt",zt);
                                        startActivity(intent);
                                        finish();
                                    }
                                }else {
                                    getResponseErrMsg(RegisterActivity.this, responseBodyJSONObject);
                                }
                            }else {
                                Log.d("RegisterActivity", "服务器异常");
                                showToastInThread(RegisterActivity.this, responseStr);
                            }
                        }
                    });
                }
            }).start();
        }else {
            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }

    }

    private String getResponseStatus(JsonObject responseBodyJSONObject) {
        // Gson解析JSON，总共3步
        // 1、获取response对象的字符串序列化
        // String responseData = response.body().string();
        // 2、通过JSON解析器JsonParser()把字符串解析为JSON对象，
        //
        // *****前两步抽写方法外面了*****
        //
        // JsonObject jsonObject = (JsonObject) new JsonParser().parse(responseBodyStr);
        // 3、通过JSON对象获取对应的属性值
        String status = responseBodyJSONObject.get("status").getAsString();
        return status;
    }
    private void setTextInThread(final EditText editText, final String otpCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(otpCode);
            }
        });
    }

    private void showToastInThread(final Context context, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getResponseErrMsg(Context context, JsonObject responseBodyJSONObject) {
        JsonObject dataObject = responseBodyJSONObject.get("data").getAsJsonObject();
        String errorCode = dataObject.get("errCode").getAsString();
        String errorMsg = dataObject.get("errMsg").getAsString();
        Log.d("RegisterActivity", "errorCode: " + errorCode + " errorMsg: " + errorMsg);
        // 在子线程中显示Toast
        showToastInThread(context, errorMsg);
    }

}
