package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.MainActivity;
import com.example.kongheapp.R;
import com.example.kongheapp.constant.NetConstant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
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
//                sharedPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
//                editor = sharedPreferences.edit();
//                editor.putString("token", "token_value");
//                editor.putString("telphone", account);
//                editor.putString("login","1");
//                editor.putString("password", password);
//                if (editor.commit()) {
//                   Intent intent = new Intent(this,LgsuccessActivity.class);
//                    intent.putExtra("zt",zt);
//                   startActivity(intent);
//
//                    finish();
//                } else {
//                    showToastInThread(LoginActivity.this, "token保存失败，请重新登录");
//                }
                asyncValidate(account,password);

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

    private void asyncValidate(final String account, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // okhttp异步POST请求； 总共5步
                // 1、初始化okhttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();
                final String telphone = account;
                // 2、构建请求体requestBody
                RequestBody requestBody = new FormBody.Builder()
                        .add("telphone",telphone).add("password",password).build();
                // 3、发送请求，因为要传密码，所以用POST方式
                Request request = new Request.Builder()
                        .url(NetConstant.getLoginURL())
                        .post(requestBody).build();
                // 4、使用okhttpClient对象获取请求的回调方法，enqueue()方法代表异步执行
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    // 5、重写两个回调方法
                    public void onFailure(Call call, IOException e) {
                        Log.d("LoginActivity", "请求URL失败： " + e.getMessage());
                        showToastInThread(LoginActivity.this, "请求URL失败, 请重试！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.toString();
                        // 先判断一下服务器是否异常
                        if(responseStr.contains("200")){
                            /* 使用Gson解析response的JSON数据的第一步 */
                            String responseBodyStr = response.body().string();
                            /* 使用Gson解析response的JSON数据的第二步 */
                            JsonObject responseBodyJSONObject = (JsonObject) new JsonParser().parse(responseBodyStr);
                            // 如果返回的status为success，则getStatus返回true，登录验证通过
                            if(getStatus(responseBodyJSONObject).equals("success")){
                                sharedPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putString("token", "token_value");
                                editor.putString("telphone", telphone);
                                editor.putString("password", password);
                                editor.putString("login","1");
                                if(editor.commit()){
                                    Intent intent = new Intent(LoginActivity.this,LgsuccessActivity.class);
                                    intent.putExtra("zt",zt);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    showToastInThread(LoginActivity.this, "token保存失败，请重新登录");
                                }
                            }else {
                                getResponseErrMsg(LoginActivity.this, responseBodyJSONObject);
                                Log.d("LoginActivity", "账号或密码验证失败");
                            }
                        }else{
                            Log.d("LoginActivity", "服务器异常");
                            showToastInThread(LoginActivity.this, responseStr);
                        }

                    }
                });
            }
        }).start();
    }

    /*
     使用Gson解析response返回异常信息的JSON中的data对象
     这也属于第三步，一、二步在方法调用之前执行了
    */
    private void getResponseErrMsg(Context context, JsonObject responseBodyJSONObject) {
        JsonObject dataObject = responseBodyJSONObject.get("data").getAsJsonObject();
        String errorCode = dataObject.get("errCode").getAsString();
        String errorMsg = dataObject.get("errMsg").getAsString();
        Log.d("LoginActivity", "errorCode: " + errorCode + " errorMsg: " + errorMsg);
        // 在子线程中显示Toast
        showToastInThread(context, errorMsg);
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

    private String getStatus(JsonObject responseBodyJSONObject) {
        /* 使用Gson解析response的JSON数据的第三步
           通过JSON对象获取对应的属性值 */
        String status = responseBodyJSONObject.get("status").getAsString();
        // 登录成功返回的json为{ "status":"success", "data":null }
        // 只获取status即可，data为null
        return status;
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
