package com.example.kongheapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kongheapp.QRcode.QRcodeUtil;
import com.example.kongheapp.R;

public class QrcodeActivity extends Activity {
    TextView title;
    ImageView fanhui;
    EditText et_ewm;
    Button bt_ewm;
    Button bt_img;
    ImageView code_image;

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
        setContentView(R.layout.set_layout);
        title=findViewById(R.id.rt_title);
        fanhui=findViewById(R.id.returnimage);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_ewm=findViewById(R.id.input_ewm);
        bt_ewm=findViewById(R.id.bt_ewm);
        bt_img=findViewById(R.id.bt_image);
        code_image=findViewById(R.id.code_image);
        bt_ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = QRcodeUtil.createQRcodeBitmap(et_ewm.getText().toString(),480,480);
                code_image.setImageBitmap(bitmap);
            }
        });
    }
}
