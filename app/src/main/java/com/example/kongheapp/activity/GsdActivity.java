package com.example.kongheapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.Controller.Consts;
import com.example.kongheapp.Data.Phone;
import com.example.kongheapp.R;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class GsdActivity extends Activity implements View.OnClickListener {
    private EditText et_phone;
    private Button btn_search;
    private TextView tv_phone;
    private TextView tv_province;
    private TextView tv_catName;
    private TextView tv_carrier;
    private TextView title;
    private ImageView fanhui;
    private GsdHandler gsdHandler = new GsdHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int zt =intent.getIntExtra("zt",0);
        if(zt==1){
            //设置主题
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.AppLightTheme);
        }
        setContentView(R.layout.gsd_layout);
        title=findViewById(R.id.rt_title);
        title.setText("归属地查询");
        fanhui=findViewById(R.id.returnimage);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_phone=findViewById(R.id.et_phone);
        btn_search=findViewById(R.id.btn_search);
        tv_phone=findViewById(R.id.tv_phone);
        tv_carrier=findViewById(R.id.tv_carrier);
        tv_province=findViewById(R.id.tv_province);
        tv_catName=findViewById(R.id.tv_catName);
        btn_search.setOnClickListener(this);
    }
    //隐藏键盘
    private void hideKeyboard(View view) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im.isActive(view)) {
            im.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.et_phone_anim);
                et_phone.startAnimation(animation);
                String url;
                String phoneNumber = et_phone.getText().toString().trim();
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    hideKeyboard(et_phone);
                    url = Consts.BAIDU_PHONE_URL + phoneNumber;
                    new GsdThread(url,phoneNumber).start();
        }
                break;
            default:
                break;

        }
    }
    class GsdThread extends Thread{
        private String url;
        private String number;
        public GsdThread(String url,String number){
            this.url=url;
            this.number=number;
        }
        public void run(){
            String result =getJsonFromURL(url);
            if(TextUtils.isEmpty(result)){
                Looper.prepare();
                Toast.makeText(GsdActivity.this,"返回数据为空",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            else {
                Phone phone = parseJsonToPhone(result,number);
                Message msg = Message.obtain();
                msg.obj=phone;
                gsdHandler.sendMessage(msg);
            }
        }


    }
    @SuppressLint("Handlerleak")
    class GsdHandler extends Handler{
        public void handleMessage(Message msg){
            Phone phone = (Phone)msg.obj;
            showInfo(phone);
        }
    }
    private String getJsonFromURL(String urlStr){
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = new URL(urlStr).openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine())!=null){
                sb.append(line);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("yh", "getJsonFromURL: "+sb.toString());
        return sb.toString();
    }
    private Phone parseJsonToPhone(String url,String number){
        Phone phone = new Phone();
        try {
            JSONObject object =new JSONObject(url);
            JSONObject numberObj = object.getJSONObject("response").getJSONObject(number);
            JSONObject detailObj =numberObj.getJSONObject("detail");
            phone.setNumber(number);
            phone.setProvince(detailObj.getString("province"));
            phone.setCarrier(detailObj.getString("operator"));
            phone.setcatName(numberObj.getString("location"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }
    @SuppressLint("SetTextI18n")
    private void showInfo(Phone phone){
        tv_phone.setText(phone.getNumber());
        tv_catName.setText(phone.getcatName());
        tv_province.setText(phone.getProvince());
        tv_carrier.setText(phone.getCarrier());
    }
    private Phone parseJsonByFastJson(String t) {
        return new Phone();
    }

    private Phone parseJsonByGson(String t) {
        return new Phone();
    }


}
