package com.bunny.groovy.view;

import android.support.v4.app.FragmentActivity;

import com.bunny.groovy.model.PerformerUserModel;

import java.util.List;

/**
 * 搜索表演者
 */
public interface ISearchMusicianList {
    FragmentActivity get();

    void setListView(List<PerformerUserModel> list);
}
