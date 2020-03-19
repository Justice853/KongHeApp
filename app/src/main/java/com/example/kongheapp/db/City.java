package com.example.kongheapp.db;

import org.litepal.crud.DataSupport;

public class City extends DataSupport {
    private int id;//id
    private String cityName;//城市名字字
    private int cityCode;//城市代号
    private int provinceId;//当前省的id值

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
