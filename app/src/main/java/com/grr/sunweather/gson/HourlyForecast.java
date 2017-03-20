package com.grr.sunweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangjie on 2017/3/20.
 */

public class HourlyForecast {
    /**
     * "hourly_forecast": [ //当天每小时天气预报
     * {
     * "cond": { //天气状况
     * "code": "100",  //天气状况代码
     * "txt": "晴"  //天气状况描述
     * },
     * "date": "2016-08-31 12:00",  //时间
     * "hum": "21",  //相对湿度（%）
     * "pop": "0",  //降水概率
     * "pres": "998",  //气压
     * "tmp": "33",  //温度
     * "wind": {  //风力风向
     * "deg": "40",  //风向（360度）
     * "dir": "东北风",  //风向
     * "sc": "4-5",  //风力
     * "spd": "24"  //风速（kmph）
     * }
     * }
     * ],
     */

    //预报日期
    @SerializedName("date")
    public String date;

    //相对湿度（%）
    @SerializedName("hum")
    public String hum;

    //降水概率
    @SerializedName("pop")
    public String pop;

    //气压
    @SerializedName("pres")
    public String pres;

    //温度
    @SerializedName("tmp")
    public String tmp;

    //天气状况
    @SerializedName("cond")
    public Condition condition;

    //风向
    @SerializedName("wind")
    public Wind wind;

    public class Condition {
        //天气状况代码
        @SerializedName("code")
        public String code;

        //天气状况描述
        @SerializedName("txt")
        public String txt;
    }

    public class Wind {
        //风向（360度）
        @SerializedName("deg")
        public String deg;

        //风向
        @SerializedName("dir")
        public String dir;

        //风力等级
        @SerializedName("sc")
        public String sc;

        //风速（kmph）
        @SerializedName("spd")
        public String spd;
    }
}
