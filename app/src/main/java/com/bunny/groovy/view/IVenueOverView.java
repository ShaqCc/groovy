package com.bunny.groovy.view;

import android.support.v4.app.FragmentActivity;

import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.VenueShowModel;

/****************************************
 * 功能说明:  首页接口
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public interface IVenueOverView {
    void initNextView(VenueShowModel showModel);
    void showEmptyNextShow();
    FragmentActivity get();
    void setView(PerformerUserModel userModel);
}
