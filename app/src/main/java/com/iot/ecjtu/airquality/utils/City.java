package com.iot.ecjtu.airquality.utils;

import com.iot.ecjtu.airquality.OnCityListener;

/**
 * Created by 497908738@qq.com on 2016/7/1.
 */
public interface City {
    void getCurrentAirQuality(String cityName,OnCityListener listener);
    void getCityIntroduction(String cityName);
}
