package com.iot.ecjtu.airquality;

import org.json.JSONObject;

/**
 * Created by 497908738@qq.com on 2016/7/1.
 */
public interface OnCityListener {
    void success(JSONObject jsonObject);
    void error();
}
