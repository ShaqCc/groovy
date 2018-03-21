package com.bunny.groovy.model;

import java.util.List;
import java.util.Map;

/****************************************
 * 功能说明:  用户首页请求返回模型
 *
 * Author: Created by bayin on 2017/12/13.
 ****************************************/

public class UserMainModel {

    public List<PerformDetail> venuePerformList;
    public List<PerformDetail> allPerformList;
    public Map<String, List<PerformDetail>> resultMap;//地图展示

}
