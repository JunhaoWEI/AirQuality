package com.iot.ecjtu.airquality.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.iot.ecjtu.airquality.model.AirQuality;
import com.iot.ecjtu.airquality.Constants;
import com.iot.ecjtu.airquality.OnCityListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 497908738@qq.com on 2016/6/29.
 */
public class CityInformation implements City {

    private static final int REQUEST_NOT_FINISH = 0;
    private static final int REQUEST_FINISHED = 1;
    public static int requestState = REQUEST_NOT_FINISH;
    private RequestQueue mQueue;
    private Context mContext;
    public static AirQuality mAirQuality;


    public CityInformation(Context context) {
        mContext = context;
    }

    @Override
    public void getCurrentAirQuality(String cityName, final OnCityListener listener) {
        mQueue = Volley.newRequestQueue(mContext);
        String url = Constants.URL + "?city=" + cityName + "&key=" + Constants.APPKEY;
        Log.d("+++++++++++++++++++++++",url);
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void getCityIntroduction(String cityName) {

    }

    public void getAirQuality(String cityName){
        mAirQuality = new AirQuality();
        mQueue = Volley.newRequestQueue(mContext);
        String url = Constants.URL + "?city=" + cityName + "&key=" + Constants.APPKEY;
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = null;
                try {
                    object = response.getJSONArray(Constants.RESULT).getJSONObject(0).getJSONObject(Constants.CITY_NOW);
                    mAirQuality.setAQI(Integer.valueOf(object.getString(Constants.AQI)));
                    mAirQuality.setQuality(object.getString(Constants.QUALITY));
                    mAirQuality.setCityName(object.getString(Constants.CITY));
                    mAirQuality.setDate(object.getString(Constants.DATE));

                    requestState = REQUEST_FINISHED;
                    Log.d("=============",REQUEST_FINISHED+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }





}
