package com.grr.sunweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangjie on 2017/3/20.
 */

public class DailyForecast {
    /**
     * "daily_forecast": [
     * {
     * "astro": {
     * "mr": "03:09",
     * "ms": "17:06",
     * "sr": "05:28",
     * "ss": "18:29"
     * },
     * "cond": {
     * "code_d": "100",
     * "code_n": "100",
     * "txt_d": "晴",
     * "txt_n": "晴"
     * },
     * "date": "2016-08-30",
     * "hum": "45",
     * "pcpn": "0.0",
     * "pop": "8",
     * "pres": "1005",
     * "tmp": {
     * "max": "29",
     * "min": "22"
     * },
     * "vis": "10",
     * "wind": {
     * "deg": "339",
     * "dir": "北风",
     * "sc": "4-5",
     * "spd": "24"
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

    //降水量（mm）
    @SerializedName("pcpn")
    public String pcpn;//能见度（km）

    //降水概率
    @SerializedName("pop")
    public String pop;

    //气压
    @SerializedName("pres")
    public String pres;

    //能见度（km）
    @SerializedName("vis")
    public String vis;

    //天文数值
    @SerializedName("astro")
    public Astronomical astronomical;

    //天气状况
    @SerializedName("cond")
    public Condition condition;

    //温度
    @SerializedName("tmp")
    public Temperature temperature;

    //风向
    @SerializedName("wind")
    public Wind wind;

    public class Astronomical {
        //月生时间
        @SerializedName("mr")
        public String moonrise;

        //月落时间
        @SerializedName("ms")
        public String moonset;

        //日出时间
        @SerializedName("sr")
        public String sunrise;

        //日落时间
        @SerializedName("ss")
        public String sunset;
    }

    public class Condition {
        //白天天气状况代码
        @SerializedName("code_d")
        public String codeDay;

        //夜间天气状况代码
        @SerializedName("code_n")
        public String codeNight;

        //白天天气状况描述
        @SerializedName("txt_d")
        public String txtDay;

        //夜间天气状况描述
        @SerializedName("txtNight")
        public String sunset;
    }

    public class Temperature {
        //最高温度
        @SerializedName("max")
        public String max;

        //最低温度
        @SerializedName("min")
        public String min;
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
