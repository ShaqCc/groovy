package com.bunny.groovy.model;

import java.util.List;

/**
 * Created by wjy on 2018/4/8.
 */

public class LocationModel {

    public List<LocationDetail> results;

    public final class LocationDetail{
        // 评价信息
        public LocationGeometry geometry; // 地址
        public String icon;// 图标
        public String id;
        public String name;// 名字
        public String place_id;//地址di
        public String rating;//评分
        public String vicinity;//附近

        final class LocationGeometry{
            public Location location;
            final class Location{
                public String lat;
                public String lng;
            }
        }
    }
}
