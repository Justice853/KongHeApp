package com.example.kongheapp.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kongheapp.R;
import com.example.kongheapp.Util.HttpUtil;
import com.example.kongheapp.Util.Utillity;
import com.example.kongheapp.gson.Forecast;
import com.example.kongheapp.gson.Weather;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public SwipeRefreshLayout swipeRefreshLayout;
    private String mWeatherId;
    public DrawerLayout drawerLayout;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private  TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private Button nav_button;
    private ImageView bingpicImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent = getIntent();

        int zt =intent.getIntExtra("zt",0);
        if(zt==1){
            //设置主题
            setTheme(R.style.AppDarkTheme);
        }else{
            setTheme(R.style.AppLightTheme);
        }
        setContentView(R.layout.weather_layout);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        weatherLayout =(ScrollView)findViewById(R.id.weather_layout);
        titleCity =(TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
        degreeText= (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText= (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_button=(Button)findViewById(R.id.nav_button);
        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        bingpicImg = (ImageView)findViewById(R.id.bing_pic_img);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingpicImg);
        }
        else {
            loadBingPic();
        }
        String weatherString = prefs.getString("weather",null);
        if(weatherString != null){
            Weather weather = Utillity.handleWeatherResponse(weatherString);
            mWeatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
        }else {
            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingpicImg);
                    }
                });
            }
        });
    }

    public void requestWeather(final  String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               e.printStackTrace();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                   }
               });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utillity.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId=weather.basic.weatherId;
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }
   private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String updateName = weather.basic.update.updateTime.split("")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateName);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);;
            TextView dateText =(TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度:"+weather.suggestion.comfort.info;
        String carWash = "洗车指数:"+weather.suggestion.carWash.info;
        String sport = "运动指数:"+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
