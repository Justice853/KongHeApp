package com.example.kongheapp.constant;

public class NetConstant {
    //我自己的网站接口
    private static String getOtpCodeURL = "http://www.cxp853.top:8090" + "/user/getotp";
    private static String loginURL = "http://www.cxp853.top:8090" + "/user/login";
    private static String registerURL = "http://www.cxp853.top:8090" + "/user/register";
    private static String createItemURL = "http://10.0.3.2:8090/item/create";
    private static String getItemListURL = "http://10.0.3.2:8090/item/list";
    private static String submitOrderURL = "http://10.0.3.2:8090/order/createorder";

    public static String getGetOtpCodeURL() {
        return getOtpCodeURL;
    }

    public static String getLoginURL() {
        return loginURL;
    }

    public static String getRegisterURL() {
        return registerURL;
    }
}
