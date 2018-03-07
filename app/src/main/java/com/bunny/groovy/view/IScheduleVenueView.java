package com.bunny.groovy.view;

import android.app.Activity;

import com.bunny.groovy.model.ScheduleModel;
import com.bunny.groovy.model.VenueScheduleModel;

/**
 * Created by Administrator on 2017/12/28.
 */
public interface IScheduleVenueView {
    Activity get();
    void setFailureView();
    void setVenueSuccessView(VenueScheduleModel model);
}
