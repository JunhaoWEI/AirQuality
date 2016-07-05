package com.iot.ecjtu.airquality.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iot.ecjtu.airquality.model.AirQuality;
import com.iot.ecjtu.airquality.R;

import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/6/30.
 */
public class SavedDataAdapter extends RecyclerView.Adapter<SavedDataAdapter.ViewHolder> {

    private List<AirQuality> mAirQualities;

    public SavedDataAdapter(List<AirQuality> airQualities){
        mAirQualities = airQualities;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_air_quality,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
          holder.bindAirQuality(mAirQualities.get(position));
    }

    @Override
    public int getItemCount() {
        return mAirQualities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAQI,tvDate,tvQuality;
        public ViewHolder(View itemView) {
            super(itemView);
            tvAQI = (TextView) itemView.findViewById(R.id.tv_AQI);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvQuality = (TextView) itemView.findViewById(R.id.tv_quality);
        }
        public void bindAirQuality(AirQuality airQuality){
            tvAQI.setText(airQuality.getAQI()+"");
            tvQuality.setText(airQuality.getQuality());
            tvDate.setText(airQuality.getDate());
        }
    }
}
