package com.example.kongheapp.fragment;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kongheapp.R;
import com.example.kongheapp.Util.HttpUtil;
import com.example.kongheapp.Util.Utillity;
import com.example.kongheapp.activity.TianQiActivity;
import com.example.kongheapp.activity.WeatherActivity;
import com.example.kongheapp.db.City;
import com.example.kongheapp.db.County;
import com.example.kongheapp.db.Province;
import com.example.kongheapp.gson.Weather;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    public static final  int LEVEL_PROVINCE = 0;
    public static final  int LEVEL_CITY = 1;
    public static final  int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ImageView backimage;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County>countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLeavl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.choose_area,container,false);
            titleText = (TextView)view.findViewById(R.id.rt_title2);
        backimage = (ImageView) view.findViewById(R.id.returnimage2);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLeavl == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);

                    queryCities();
                }
                else if(currentLeavl == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
                else if (currentLeavl==LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    if(getActivity() instanceof TianQiActivity){
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefreshLayout.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }

                }
            }
        });
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLeavl == LEVEL_COUNTY){
                    queryCities();
                }
                else if (currentLeavl == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        titleText.setText("中国");
        backimage.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeavl = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryFromServer(String address, final String type) {

        showProgerssDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result= Utillity.handleProvinceResponse(responseText) ;
                  }else if ("city".equals(type)){
                    result = Utillity.handleCityResponse(responseText,selectedProvince.getId());
                 }else if ("county".equals(type)){
                    result = Utillity.handleCountyResponse(responseText,selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void closeProgressDialog() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void showProgerssDialog() {
        if(progressDialog==null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }




    private void queryCounties() {

        titleText.setText(selectedCity.getCityName());
        backimage.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county :countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeavl = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvincceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }

    }

    private void queryCities() {

        titleText.setText(selectedProvince.getProvinceName());
        backimage.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeavl = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvincceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

}
