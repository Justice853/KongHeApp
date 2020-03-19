package com.example.kongheapp.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;//id
    private String countyName;//县的名字
    private String weatherId;//县对应的天气
    private int cityId;//当前县对应的市的id值

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

}
