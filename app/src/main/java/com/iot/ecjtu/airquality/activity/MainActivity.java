package com.iot.ecjtu.airquality.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.iot.ecjtu.airquality.model.AirQuality;
import com.iot.ecjtu.airquality.utils.City;
import com.iot.ecjtu.airquality.utils.CityInformation;
import com.iot.ecjtu.airquality.Constants;
import com.iot.ecjtu.airquality.OnCityListener;
import com.iot.ecjtu.airquality.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    MapView mMapView = null;
    public static final String AIR_QUALITY = "AirQuality";
    private TextView tvAQI, tvQuality, tvCity;
    private LinearLayout llContent;
    private ProgressBar mProgressBar;
    private AirQuality mAirQuality;
    private BaiduMap mBaiduMap;
    private GeoCoder mGeoCoder;
    private BitmapDescriptor bitmap;
    private int i = 0;


    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:// 如果城市列表加载完毕，就发送此消息
                    mAirQuality = CityInformation.mAirQuality;
                    Log.d("+++++++++", mAirQuality.getCityName());
                    i = 1;
                    tvCity.setText(mAirQuality.getCityName());
                    tvAQI.setText(mAirQuality.getAQI() + "");
                    tvQuality.setText(mAirQuality.getQuality());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

new Thread(){
    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                Thread.sleep(100);
                if (CityInformation.requestState == 1) {
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}.start();

       // Log.d("=======================", mAirQuality.getCityName());
        initView();
        getLocation();
    }

    private void getLocation() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                Log.d("TAG", "经纬度" + "latitude=" + latitude + ",longitude=" + longitude);

                llContent.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

                //先清除图层
                mBaiduMap.clear();
                // 定义Maker坐标点
                LatLng point = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point).icon(bitmap);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);

                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(latLng);
                //实例化一个地理编码查询对象
                mGeoCoder = GeoCoder.newInstance();
                //发起反地理编码请求(经纬度->地址信息)
                mGeoCoder.reverseGeoCode(op);
                mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {


                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        String result = reverseGeoCodeResult.getAddressDetail().city;
                        if (result!=null) {
                            String city = result.substring(0, result.length() - 1);
                            Log.d("city", city);
                           mAirQuality.setCityName(city);
                           getAirQuality(mAirQuality.getCityName());
                        }

                    }
                });
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void getAirQuality(final String cityName) {
        City city = new CityInformation(this);
        mAirQuality = new AirQuality();
        city.getCurrentAirQuality(cityName, new OnCityListener() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    JSONObject object = jsonObject.getJSONArray(Constants.RESULT).getJSONObject(0).getJSONObject(Constants.CITY_NOW);
                    mAirQuality.setAQI(Integer.valueOf(object.getString(Constants.AQI)));
                    mAirQuality.setQuality(object.getString(Constants.QUALITY));
                    mAirQuality.setCityName(object.getString(Constants.CITY));
                    mAirQuality.setDate(object.getString(Constants.DATE));
                    tvAQI.setText(object.getString(Constants.AQI));
                    tvQuality.setText(object.getString(Constants.QUALITY));
                    tvCity.setText(cityName);
                    llContent.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    private void initView() {
        //mAirQuality = (AirQuality) getIntent().getSerializableExtra(MainActivity.AIR_QUALITY);
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 删除百度地图LoGo
        mMapView.removeViewAt(1);
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark);
        tvAQI = (TextView) findViewById(R.id.tv_AQI);
        tvQuality = (TextView) findViewById(R.id.tv_quality);
        tvCity = (TextView) findViewById(R.id.btn_city_name);
        llContent = (LinearLayout) findViewById(R.id.ll_air_data);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(AIR_QUALITY, mAirQuality);
                intent.setClass(MainActivity.this, AirQualityDetailsActivity.class);
                startActivity(intent);
            }
        });


    }
}
