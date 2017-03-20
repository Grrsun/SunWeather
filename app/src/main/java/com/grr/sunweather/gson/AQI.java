package com.grr.sunweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangjie on 2017/3/20.
 */

public class AQI {
    /**
     * "aqi": {
     * "city": {
     * "aqi": "60",
     * "co": "0",
     * "no2": "14",
     * "o3": "95",
     * "pm10": "67",
     * "pm25": "15",
     * "qlty": "良",  //共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染
     * "so2": "10"
     * }
     * },
     */
    @SerializedName("city")
    public AQICity aqiCity;

    public class AQICity {
        @SerializedName("aqi")
        public String aqi;

        @SerializedName("co")
        public String co;

        @SerializedName("no2")
        public String no2;

        @SerializedName("o3")
        public String o3;

        @SerializedName("pm10")
        public String pm10;

        @SerializedName("pm25")
        public String pm25;

        @SerializedName("qlty")
        public String qlty;

        @SerializedName("so2")
        public String so2;
    }
}
