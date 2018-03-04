package com.bunny.groovy.view;

import android.support.v4.app.FragmentActivity;

import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.StyleModel;

import java.util.List;

/**
 * 发现表演者
 */
public interface IDiscoverSearchMusicianList {
    FragmentActivity get();

    void setListView(List<PerformerUserModel> list);
    void showStylePop(List<StyleModel> modelList);
}
