package com.bunny.groovy.view;

import android.app.Activity;

import com.bunny.groovy.model.VenueModel;

import java.util.List;

/****************************************
 * 功能说明:  
 ****************************************/

public interface ITimeView {
    Activity get();
    void setView(List<VenueModel.ScheduleListBean> model);
    void updateScheduleView(String scheduleID, String startDate, String endDate);
    void updateScheduleIsHaveCharges(String scheduleID, String isHaveCharges);
}
