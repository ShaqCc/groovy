package com.bunny.groovy.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****************************************
 * 功能说明:  演出厅-日程设置日期对应列表
 *
 ****************************************/

public class VenueScheduleModel {
    private Map<String, List<VenueShowModel>> mMap = new HashMap<>();

    public void setMap(String key, List<VenueShowModel> value) {
        mMap.put(key, value);
    }

    public List<VenueShowModel> getShowModelList(String key) {
        return mMap.get(key);
    }
}
