package com.example.kongheapp.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;
    @SerializedName("tmp_max")
    public String max;

    @SerializedName("tmp_min")
    public String min;
//    public Temperature temperature;

    @SerializedName("cond_txt_d")
    public String info;


//    public More more;

//    public class Temperature{
//        public String max;
//        public String min;
//    }
//    public class More{
//        @SerializedName("txt_d")
//        public String info;
//    }
}
