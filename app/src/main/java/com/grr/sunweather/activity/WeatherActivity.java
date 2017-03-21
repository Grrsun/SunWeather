
package com.grr.sunweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grr.sunweather.R;
import com.grr.sunweather.fragment.ChooseFragment;
import com.grr.sunweather.gson.DailyForecast;
import com.grr.sunweather.gson.Weather;
import com.grr.sunweather.service.AutoUpdateservice;
import com.grr.sunweather.util.HttpUtil;
import com.grr.sunweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private Button navButton;
    public DrawerLayout drawerLayout;
    public SwipeRefreshLayout swipeRefresh;
    private ImageView bingPicImg;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout dailyForecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //初始化控件
        initView();
        String weatherString = prefs.getString("weather", null);
        final String countyName;
        if (weatherString != null) {
            //有缓存时直接解析天气
            Weather weather = Utility.handleWeatherResponse(weatherString);
            countyName = weather.basic.cityName;
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            countyName = getIntent().getStringExtra("county_name");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(countyName);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ChooseFragment chooseFragment = (ChooseFragment) getSupportFragmentManager().findFragmentById(R.id.choose_area_fragment);
                if (TextUtils.isEmpty(chooseFragment.countyName)) {
                    requestWeather(countyName);
                } else {
                    requestWeather(chooseFragment.countyName);
                }
            }
        });
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        dailyForecastLayout = (LinearLayout) findViewById(R.id.daily_forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }

    /**
     * 处理并展示Weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.tmp + "℃";
        String weatherInfo = weather.now.more.txt;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        dailyForecastLayout.removeAllViews();
        for (DailyForecast dailyForecast : weather.dailyForecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.daily_forecast_item, dailyForecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(dailyForecast.date);
            infoText.setText(dailyForecast.condition.txtDay);
            maxText.setText(dailyForecast.temperature.max);
            minText.setText(dailyForecast.temperature.min);
            dailyForecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.aqiCity.aqi);
            pm25Text.setText(weather.aqi.aqiCity.pm25);
        }
        String comfort = "";
        String carWash = "";
        String sport = "";
        if (weather.suggestion.comfort != null) {
            if (!TextUtils.isEmpty(weather.suggestion.comfort.info)) {
                comfort = "舒适度:" + weather.suggestion.comfort.info;
            }
            if (!TextUtils.isEmpty(weather.suggestion.comfort.info)) {
                carWash = "洗车指数:" + weather.suggestion.carWash.info;
            }
            if (!TextUtils.isEmpty(weather.suggestion.comfort.info)) {
                sport = "运动建议:" + weather.suggestion.sport.info;
            }
        }
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param countyName
     */
    public void requestWeather(final String countyName) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather/?city=" + countyName + "&key=852391803c4944869c8ae79d0b748d6d";
        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            Intent intent = new Intent(WeatherActivity.this, AutoUpdateservice.class);
                            startService(intent);
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

    }

    /**
     * 请求必应壁纸
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
