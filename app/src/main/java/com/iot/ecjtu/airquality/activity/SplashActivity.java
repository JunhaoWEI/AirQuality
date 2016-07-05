package com.iot.ecjtu.airquality.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iot.ecjtu.airquality.model.AirQuality;
import com.iot.ecjtu.airquality.utils.City;
import com.iot.ecjtu.airquality.utils.CityInformation;
import com.iot.ecjtu.airquality.Constants;
import com.iot.ecjtu.airquality.OnCityListener;
import com.iot.ecjtu.airquality.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    private static final String SHANGHAI = "上海";
    private AirQuality mAirQuality;
    public static final String AIR_QUALITY = "AirQuality";


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:// 如果城市列表加载完毕，就发送此消息
                        mHandler.postDelayed(goToMainActivity, 300);
                    break;
                default:
                    break;
            }
        }
    };
    //进入下一个Activity
    Runnable goToMainActivity = new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent();
//            intent.putExtra(AIR_QUALITY, mAirQuality);
            intent.setClass(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);
        new CityInformation(this).getAirQuality(SHANGHAI);

    }

    private void getDefaultCity(final Handler handler) {
        City city = new CityInformation(this);
        mAirQuality = new AirQuality();
        city.getCurrentAirQuality("上海", new OnCityListener() {
            @Override
            public void success(JSONObject jsonObject) {
                JSONObject object = null;
                try {
                    object = jsonObject.getJSONArray(Constants.RESULT).getJSONObject(0).getJSONObject(Constants.CITY_NOW);
                    mAirQuality.setAQI(Integer.valueOf(object.getString(Constants.AQI)));
                    mAirQuality.setQuality(object.getString(Constants.QUALITY));
                    mAirQuality.setCityName(object.getString(Constants.CITY));
                    mAirQuality.setDate(object.getString(Constants.DATE));

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error() {

            }
        });
    }
}
