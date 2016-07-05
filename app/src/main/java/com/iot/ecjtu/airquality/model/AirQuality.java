package com.iot.ecjtu.airquality.model;

import java.io.Serializable;

/**
 * Created by 497908738@qq.com on 2016/7/1.
 */
public class AirQuality implements Serializable{
    private String cityName;
    private String quality;
    private int AQI;
    private String date;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getAQI() {
        return AQI;
    }

    public void setAQI(int AQI) {
        this.AQI = AQI;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
