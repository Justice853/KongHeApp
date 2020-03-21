package com.example.kongheapp.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond_txt")
    public String info;

    @SerializedName("wind_sc")
    public String wind_sc;

    @SerializedName("hum")
    public String hum;
//    public More more;

//    public class More{
//        @SerializedName("txt")
//        public String info;
//    }
}
