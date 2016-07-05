package com.iot.ecjtu.airquality.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 497908738@qq.com on 2016/6/29.
 */
public class AirQualityDBHelper extends SQLiteOpenHelper {

    public static final String CITY_NAME = "cityname";
    public static final String AQI = "AQI";
    public static final String AIR_QUALITY = "air_quality";
    public static final String TIME = "time";
    public static final String TABLE_AIR = "air_quality";
    private static final String CREATE_AIR_QUALITY = "create table "+TABLE_AIR+"("
            + CITY_NAME + " varchar(10),"
            + AQI + " integer,"
            + AIR_QUALITY + " varchar(10),"
            + TIME + " datetime)";

    private Context mContext;

    public AirQualityDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_AIR_QUALITY);
        Toast.makeText(mContext, "create succeeded",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
