package com.iot.ecjtu.airquality.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iot.ecjtu.airquality.model.AirQuality;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/6/29.
 */
public class SQLOperateImpl implements SQLOperate {

    private AirQualityDBHelper mDBHelper;
    public SQLOperateImpl(AirQualityDBHelper airQualityDBHelper){
        mDBHelper = airQualityDBHelper;
    }


    @Override
    public void add(AirQuality airQuality) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AirQualityDBHelper.CITY_NAME,airQuality.getCityName());
        values.put(AirQualityDBHelper.AQI,airQuality.getAQI());
        values.put(AirQualityDBHelper.AIR_QUALITY,airQuality.getQuality());
        values.put(AirQualityDBHelper.TIME,airQuality.getDate());
        db.insert(AirQualityDBHelper.TABLE_AIR,null,values);
        db.close();
    }

    @Override
    public List<AirQuality> query() {
        List<AirQuality> airQualities = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(AirQualityDBHelper.TABLE_AIR,null,null,null,null,null,null);
        if (cursor!=null){
            airQualities = new ArrayList<AirQuality>();
            while (cursor.moveToNext()){
                AirQuality airQuality = new AirQuality();
            }
        }
        cursor.close();
        db.close();
        return null;
    }

    @Override
    public List<AirQuality> query(String cityName) {
        List<AirQuality> airQualities = null;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(AirQualityDBHelper.TABLE_AIR,null,AirQualityDBHelper.CITY_NAME+"=?",new String[]{cityName},null,null,AirQualityDBHelper.TIME+" DESC");
        AirQuality airQuality = null;
        if(cursor!=null){
            airQualities = new ArrayList<AirQuality>();
            while (cursor.moveToNext()){
                airQuality = new AirQuality();
                airQuality.setCityName(cursor.getString(cursor.getColumnIndex(AirQualityDBHelper.CITY_NAME)));
                airQuality.setAQI(cursor.getInt(cursor.getColumnIndex(AirQualityDBHelper.AQI)));
                airQuality.setQuality(cursor.getString(cursor.getColumnIndex(AirQualityDBHelper.AIR_QUALITY)));
                airQuality.setDate(cursor.getString(cursor.getColumnIndex(AirQualityDBHelper.TIME)));
                airQualities.add(airQuality);
            }
        }
        cursor.close();
        db.close();
        return airQualities;

    }

    @Override
    public boolean query(String cityName, String time) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(AirQualityDBHelper.TABLE_AIR,null,AirQualityDBHelper.CITY_NAME+"=? and "+AirQualityDBHelper.TIME+"=?",new String[]{cityName,time},null,null,null);
        int i = 0;
        while (cursor.moveToNext()){
            i++;
        }
        db.close();
        cursor.close();
        if (i==0){
            return false;
        }else {
            return true;
        }
    }
}
