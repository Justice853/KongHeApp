package com.example.kongheapp.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.R;
import com.example.kongheapp.Util.HttpUtil;
import com.example.kongheapp.Util.Utillity;
import com.example.kongheapp.gson.Trans;
import com.example.kongheapp.gson.Word;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.view.View.inflate;

public class FanyiActivity extends BaseActivity  {
    private TextView title;
    private ImageView fanhui;
    Spinner sp_fylanguage;
    Spinner sp_bsylanguage;
    EditText et_fy;
    TextView tv_bfy;
    Button bt_qrfy;
    String selectTranslatedCode ;
    String targetTranslatedCode ;
    private String mwordinfo;
    final String[] languageArr = {"自动检测", "中文", "英文", "粤语", "文言文", "日语", "韩语", "法语", "西班牙语", "泰语", "阿拉伯语", "俄语", "葡萄牙语", "德语", "葡萄牙语", "德语", "意大利语", "希腊语", "荷兰语", "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语", "捷克语", "罗马尼亚语", "斯洛文尼亚语", "瑞典语", "匈牙利语", "繁体中文", "越南语"
    };

    final String[] languageCode = {"auto", "zh", "en", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom", "slo", "swe", "hu", "cht", "vie"};
    final String[] bfylanguageArr = {"中文", "英文", "粤语", "文言文", "日语", "韩语", "法语", "西班牙语", "泰语", "阿拉伯语", "俄语", "葡萄牙语", "德语", "葡萄牙语", "德语", "意大利语", "希腊语", "荷兰语", "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语", "捷克语", "罗马尼亚语", "斯洛文尼亚语", "瑞典语", "匈牙利语", "繁体中文", "越南语"
    };

    final String[] bfylanguageCode = { "zh", "en", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it", "el", "nl", "pl", "bul", "est", "dan", "fin", "cs", "rom", "slo", "swe", "hu", "cht", "vie"};

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
        setContentView(R.layout.fanyi_layout);
        title=findViewById(R.id.rt_title);
        title.setText("翻译工具");
        fanhui=findViewById(R.id.returnimage);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sp_fylanguage=findViewById(R.id.sp_fylanguage);
        sp_bsylanguage=findViewById(R.id.sp_bfylanguage);;
        et_fy = findViewById(R.id.et_fy);
        tv_bfy = findViewById(R.id.tv_bfy);
        bt_qrfy =findViewById(R.id.bt_qrfy);
        setAdapter();
       bt_qrfy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String word=et_fy.getText().toString();
               queryword(word);
           }
       });
    }

    private void queryword(final String word) {
        String appleid ="20200322000402862";
        String salt = "12345678";
        String miyao ="v2ywmq_sroL8zRcG9MCg";
        String msign = appleid+word+salt+miyao;
        String sign = md5Decode32(msign);
        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q="+word+"&from="+selectTranslatedCode+"&to="+targetTranslatedCode+"&appid="+appleid+"&salt="+salt+"&sign="+sign;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Trans mword = Utillity.handleWordResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mword!=null){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(FanyiActivity.this).edit();
                            editor.putString("word",responseText);
                            editor.apply();
                            mwordinfo = mword.info;
                            tv_bfy.setText(mwordinfo);
                        }
                        else {
                            Log.d("3691", "run: ");
                            Toast.makeText(FanyiActivity.this,"获取翻译信息失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }



    private void setAdapter() {
        ArrayAdapter fyadapter = new ArrayAdapter<String>(this,
                R.layout.item_select, languageArr){
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = inflate(getContext(), R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);
                label.setText(languageArr[position]);
                return view;
            }
        };
        ArrayAdapter bfyadapter = new ArrayAdapter<String>(this,
                R.layout.item_select, bfylanguageArr){
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = inflate(getContext(), R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);
                label.setText(bfylanguageArr[position]);
                return view;
            }
        };
        fyadapter.setDropDownViewResource(R.layout.spinner_item_layout);
        sp_fylanguage.setAdapter(fyadapter);
        sp_bsylanguage.setAdapter(bfyadapter);
        sp_fylanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (languageCode[position] != null) {
                    selectTranslatedCode = languageCode[position];//翻译的输入语言，定义的全局变量
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_bsylanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bfylanguageCode[position] != null) {
                    targetTranslatedCode = bfylanguageCode[position];//翻译的目标语言，定义的全局变量
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    public String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }



}
