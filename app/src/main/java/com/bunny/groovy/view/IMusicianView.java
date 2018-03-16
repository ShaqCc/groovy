package com.bunny.groovy.view;

import android.app.Activity;

import com.bunny.groovy.model.MusicianDetailModel;

/****************************************
 * 功能说明:  表演者详情页，接口
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public interface IMusicianView {
    Activity get();
    void setView(MusicianDetailModel model);
    void cancelFavorite();
    void favorite();
}
