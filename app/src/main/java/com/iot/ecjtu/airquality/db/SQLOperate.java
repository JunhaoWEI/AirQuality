package com.iot.ecjtu.airquality.db;

import com.iot.ecjtu.airquality.model.AirQuality;

import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/6/29.
 */
public interface SQLOperate {
    void add(AirQuality airQuality);
    List<AirQuality> query();
    List<AirQuality> query(String cityName);
    boolean query(String cityName, String time);
}
