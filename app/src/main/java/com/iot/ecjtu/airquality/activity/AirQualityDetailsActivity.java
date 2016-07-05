package com.iot.ecjtu.airquality.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iot.ecjtu.airquality.model.AirQuality;
import com.iot.ecjtu.airquality.db.AirQualityDBHelper;
import com.iot.ecjtu.airquality.R;
import com.iot.ecjtu.airquality.db.SQLOperateImpl;
import com.iot.ecjtu.airquality.adapter.SavedDataAdapter;

import java.util.List;

public class AirQualityDetailsActivity extends AppCompatActivity {

    private AirQualityDBHelper mDBHelper;
    private AirQuality mAirQuality;
    private List<AirQuality> mAirQualities;
    private Button btnSave;
    private TextView tvSaved;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
        setContentView(R.layout.activity_air_quality_details);

        initView();
        mDBHelper = new AirQualityDBHelper(this, "myDB.db", null, 1);
        displaySavedData();
        checkSavedData();
    }


    private void checkSavedData() {
        boolean result = new SQLOperateImpl(mDBHelper).query(mAirQuality.getCityName(), mAirQuality.getDate());
        if (result) {
            btnSave.setVisibility(View.GONE);
            tvSaved.setVisibility(View.VISIBLE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
            tvSaved.setVisibility(View.GONE);
        }
    }

    private void displaySavedData() {
        mAirQualities = new SQLOperateImpl(mDBHelper).query(mAirQuality.getCityName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView rvSavedData = (RecyclerView) findViewById(R.id.rv_saved_data);
        rvSavedData.setLayoutManager(linearLayoutManager);
        rvSavedData.setAdapter(new SavedDataAdapter(mAirQualities));
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAirQuality = (AirQuality) getIntent().getSerializableExtra(MainActivity.AIR_QUALITY);
        ((TextView) findViewById(R.id.tv_AQI)).setText(mAirQuality.getAQI() + "");
        ((TextView) findViewById(R.id.tv_quality)).setText(mAirQuality.getQuality());
        ((TextView) findViewById(R.id.tv_date)).setText(mAirQuality.getDate());
        tvSaved = (TextView) findViewById(R.id.tv_saved);
        btnSave = (Button) findViewById(R.id.btn_save);
        toolbar.setTitle(mAirQuality.getCityName());
        Log.d("titttttttttttttttle",mAirQuality.getCityName());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("+++++++++++++++++","click");
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("cityname", mAirQuality.getCityName());
                values.put("AQI", mAirQuality.getAQI());
                values.put("air_quality", mAirQuality.getQuality());
                values.put("time", mAirQuality.getDate());
                db.insert(AirQualityDBHelper.TABLE_AIR, null, values);
//                Log.d("======================",db.toString());

                btnSave.setVisibility(View.GONE);
                tvSaved.setVisibility(View.VISIBLE);
                displaySavedData();

            }
        });
    }
}
