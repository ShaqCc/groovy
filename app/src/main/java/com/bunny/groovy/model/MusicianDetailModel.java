package com.bunny.groovy.model;

import java.util.List;

/****************************************
 * 功能说明:  表演者详情
 *
 * Author: Created by bayin on 2017/12/13.
 ****************************************/

public class MusicianDetailModel {

    public String userID; // 用户编号
    public String userName; // 姓名
    public String userEmail; // 邮箱（账户）
    public String telephone; // 手机号（账户）
    public String headImg;// 头像【头像/演出厅图片/表演者图片】
    public String performTypeName;// 【表演者】演出类别（多选）
    public String webSiteAddress; // 【演出厅/表演者】网站地址
    public String longitude;// 经度（°）
    public String latitude;// 纬度（°）
    public String signature; // 【表演者】个人描述
    public String zipCode; // 【表演者】邮编号
    public String musicFile; // 【表演者】音乐文件名称
    public String paypalAccount;// paypal 账户名称
    public String twitterAccount;// twitter 账户名称
    public String soundcloudAccount;// soundcloud 账户名称
    public String facebookAccount;// facebook 账户名称
    public String stageName;// 表演者的艺名

    public String starLevel;// 平均星级
    public String isBeCollection;// 是否被已前用户收藏 0-否 1-是
    // 2.评论列表：评分数、评论时间、评论内容、评论人名称
    // 3.我打赏该表演者历史记录：打赏金额、打赏时间
    public List<PerformViewer> evaluateList;
    public List<TransactionRecord> rewardList;

    public class PerformViewer {
        public String viewerID; // 订单编号
        public String performID; // 演出编号
        public String userID; // 普通用户编号
        public String performerStarLevel; // 表演者星级（1-5）
        public String evaluateContent; // 评价内容
        public String evaluateDate; // 评价时间
        public String userName; // 评论人姓名

    }
    public class TransactionRecord {

        public String cost; // 费用
        public String dealDate; // 交易时间
    }

}
