package com.bunny.groovy.utils;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bunny.groovy.weidget.BottomBarItem;
import com.bunny.groovy.weidget.BottomBarLayout;
import com.bunny.groovy.weidget.NoScrollViewPager;
import com.yan.inflaterauto.AutoConvert;
import com.yan.inflaterauto.annotation.Convert;

import java.util.HashMap;

/**
 * Created by Administrator on 2017\12\24 0024.
 */

@Convert({LinearLayout.class
        , RelativeLayout.class
        , FrameLayout.class
        , RecyclerView.class
        , ListView.class
        , ScrollView.class
        , ViewPager.class
        , NoScrollViewPager.class
        , BottomBarItem.class
        , BottomBarLayout.class
})
public class InflaterConvert implements AutoConvert {
    @Override
    public HashMap<String, String> getConvertMap() {
       return null;
    }

}