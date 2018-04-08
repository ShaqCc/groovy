package com.bunny.groovy.view;

import android.app.Activity;

import com.bunny.groovy.model.LocationModel;

/**
 * 用户首页界面
 * Created by Administrator on 2018/4/8.
 */

public interface IUserMainView<T> {
    Activity get();

    void setView(T t);

    void setSearchView(LocationModel model);

    void setNormalView();

    void setNodata();

    void setError();
}
