package com.bunny.groovy.view;

import android.app.Activity;

import com.bunny.groovy.model.PerformerUserModel;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/13.
 ****************************************/

public interface ILoginView {
    void loginSuccess(PerformerUserModel userModel);

    Activity get();
    /**
     * 跳转至主页
     */
    void launchMainPage(int type);

    void launchToSetFile();
}
