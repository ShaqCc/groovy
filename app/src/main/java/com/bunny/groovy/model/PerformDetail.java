package com.bunny.groovy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mysty on 2018/2/13.
 */

public class PerformDetail implements Parcelable {

    // 表演信息
    private String performID; // 演出编号
    private String venueID; // 演出厅编号【用户】
    private String performStartDate; // 开始时间
    private String performEndDate; // 结束时间
    private String performType; // 表演类型
    private String performerID; // 表演者编号【用户】
    private String performState; // 演出状态（0-待验证1-已发布 2-已取消）
    private String isOpportunity; // 是否是推广演出（0-否 1-是）
    private String performerName;// 表演者名称（艺名）
    private String performDesc;// 表演描述
    private String venueName;// 演出厅名称
    private String venueAddress;// 演出厅地址
    private String publishType;// 0-申请 1-主动发布的
    private String venueLongitude;// 演出厅经度（°）
    private String venueLatitude;// 演出厅纬度（°）
    // 表演时间
    private String performDate;// 日期
    private String performTime;// 时间

    // 表演者信息
    private String performerImg;// 图片
    private String performerMusic;// 音频文件
    private String performerScore;// 平均评分
    private String performerFacebook;// facebook
    private String performerTwitter;// twitter
    private String performerSoundcloud;// soundcloud
    private String performerSignature;// 个性签名

    // 演出厅信息
    private String venueScore;// 评分数
    private String venueFacebook;// facebook
    private String venueTwitter;// twitter
    private String venueTypeName;// 演出厅类型
    private String isHaveCharges;// 当天是否付费
    private String venueBookingPhone;// 预定电话
    private String venueWebSite;// 网址
    private String venueImg;// 图片
    private String venueEmail; // 邮箱（账户）

    private List<PerformDetail> performList;// 演出厅演出list
    private PerformDetail newPerformInfo;// 演出厅最近一场演出信息【用于普通用户端地图展示】

    // 评价信息
    private String isEvaluate; // 是否评价（0-否 1-是）
    private String performerStarLevel;// 星级
    private String evaluateDate;// 评价时间
    private String evaluateContent;// 评论信息

    private String distance;//距离

    public String getPerformID() {
        return performID;
    }

    public void setPerformID(String performID) {
        this.performID = performID;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public String getPerformStartDate() {
        return performStartDate;
    }

    public void setPerformStartDate(String performStartDate) {
        this.performStartDate = performStartDate;
    }

    public String getPerformEndDate() {
        return performEndDate;
    }

    public void setPerformEndDate(String performEndDate) {
        this.performEndDate = performEndDate;
    }

    public String getPerformType() {
        return performType;
    }

    public void setPerformType(String performType) {
        this.performType = performType;
    }

    public String getPerformerID() {
        return performerID;
    }

    public void setPerformerID(String performerID) {
        this.performerID = performerID;
    }

    public String getPerformState() {
        return performState;
    }

    public void setPerformState(String performState) {
        this.performState = performState;
    }

    public String getIsOpportunity() {
        return isOpportunity;
    }

    public void setIsOpportunity(String isOpportunity) {
        this.isOpportunity = isOpportunity;
    }

    public String getPerformerName() {
        return performerName;
    }

    public void setPerformerName(String performerName) {
        this.performerName = performerName;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public String getVenueLongitude() {
        return venueLongitude;
    }

    public void setVenueLongitude(String venueLongitude) {
        this.venueLongitude = venueLongitude;
    }

    public String getVenueLatitude() {
        return venueLatitude;
    }

    public void setVenueLatitude(String venueLatitude) {
        this.venueLatitude = venueLatitude;
    }

    public String getPerformDate() {
        return performDate;
    }

    public void setPerformDate(String performDate) {
        this.performDate = performDate;
    }

    public String getPerformTime() {
        return performTime;
    }

    public void setPerformTime(String performTime) {
        this.performTime = performTime;
    }

    public String getPerformerImg() {
        return performerImg;
    }

    public void setPerformerImg(String performerImg) {
        this.performerImg = performerImg;
    }

    public String getPerformerMusic() {
        return performerMusic;
    }

    public void setPerformerMusic(String performerMusic) {
        this.performerMusic = performerMusic;
    }

    public String getPerformerScore() {
        return performerScore;
    }

    public void setPerformerScore(String performerScore) {
        this.performerScore = performerScore;
    }

    public String getPerformerFacebook() {
        return performerFacebook;
    }

    public void setPerformerFacebook(String performerFacebook) {
        this.performerFacebook = performerFacebook;
    }

    public String getPerformerTwitter() {
        return performerTwitter;
    }

    public void setPerformerTwitter(String performerTwitter) {
        this.performerTwitter = performerTwitter;
    }

    public String getPerformerSoundcloud() {
        return performerSoundcloud;
    }

    public void setPerformerSoundcloud(String performerSoundcloud) {
        this.performerSoundcloud = performerSoundcloud;
    }

    public String getPerformerSignature() {
        return performerSignature;
    }

    public void setPerformerSignature(String performerSignature) {
        this.performerSignature = performerSignature;
    }

    public String getVenueScore() {
        return venueScore;
    }

    public void setVenueScore(String venueScore) {
        this.venueScore = venueScore;
    }

    public String getVenueFacebook() {
        return venueFacebook;
    }

    public void setVenueFacebook(String venueFacebook) {
        this.venueFacebook = venueFacebook;
    }

    public String getVenueTwitter() {
        return venueTwitter;
    }

    public void setVenueTwitter(String venueTwitter) {
        this.venueTwitter = venueTwitter;
    }

    public String getVenueTypeName() {
        return venueTypeName;
    }

    public void setVenueTypeName(String venueTypeName) {
        this.venueTypeName = venueTypeName;
    }

    public String getIsHaveCharges() {
        return isHaveCharges;
    }

    public void setIsHaveCharges(String isHaveCharges) {
        this.isHaveCharges = isHaveCharges;
    }

    public String getVenueBookingPhone() {
        return venueBookingPhone;
    }

    public void setVenueBookingPhone(String venueBookingPhone) {
        this.venueBookingPhone = venueBookingPhone;
    }

    public String getVenueWebSite() {
        return venueWebSite;
    }

    public void setVenueWebSite(String venueWebSite) {
        this.venueWebSite = venueWebSite;
    }

    public String getVenueImg() {
        return venueImg;
    }

    public void setVenueImg(String venueImg) {
        this.venueImg = venueImg;
    }

    public String getVenueEmail() {
        return venueEmail;
    }

    public void setVenueEmail(String venueEmail) {
        this.venueEmail = venueEmail;
    }

    public List<PerformDetail> getPerformList() {
        return performList;
    }

    public void setPerformList(List<PerformDetail> performList) {
        this.performList = performList;
    }

    public PerformDetail getNewPerformInfo() {
        return newPerformInfo;
    }

    public void setNewPerformInfo(PerformDetail newPerformInfo) {
        this.newPerformInfo = newPerformInfo;
    }

    public String getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(String isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public String getPerformerStarLevel() {
        return performerStarLevel;
    }

    public void setPerformerStarLevel(String performerStarLevel) {
        this.performerStarLevel = performerStarLevel;
    }

    public String getEvaluateDate() {
        return evaluateDate;
    }

    public void setEvaluateDate(String evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.performID);
        dest.writeString(this.venueID);
        dest.writeString(this.performStartDate);
        dest.writeString(this.performEndDate);
        dest.writeString(this.performType);
        dest.writeString(this.performerID);
        dest.writeString(this.performState);
        dest.writeString(this.isOpportunity);
        dest.writeString(this.performerName);
        dest.writeString(this.performDesc);
        dest.writeString(this.venueName);
        dest.writeString(this.venueAddress);
        dest.writeString(this.publishType);
        dest.writeString(this.venueLongitude);
        dest.writeString(this.venueLatitude);
        dest.writeString(this.performDate);
        dest.writeString(this.performTime);
        dest.writeString(this.performerImg);
        dest.writeString(this.performerMusic);
        dest.writeString(this.performerScore);
        dest.writeString(this.performerFacebook);
        dest.writeString(this.performerTwitter);
        dest.writeString(this.performerSoundcloud);
        dest.writeString(this.performerSignature);
        dest.writeString(this.venueScore);
        dest.writeString(this.venueFacebook);
        dest.writeString(this.venueTwitter);
        dest.writeString(this.venueTypeName);
        dest.writeString(this.isHaveCharges);
        dest.writeString(this.venueBookingPhone);
        dest.writeString(this.venueWebSite);
        dest.writeString(this.venueImg);
        dest.writeString(this.venueEmail);
        dest.writeList(this.performList);
        dest.writeParcelable(this.newPerformInfo, flags);
        dest.writeString(this.isEvaluate);
        dest.writeString(this.performerStarLevel);
        dest.writeString(this.evaluateDate);
        dest.writeString(this.evaluateContent);
        dest.writeString(this.distance);
    }

    public PerformDetail() {
    }

    protected PerformDetail(Parcel in) {
        this.performID = in.readString();
        this.venueID = in.readString();
        this.performStartDate = in.readString();
        this.performEndDate = in.readString();
        this.performType = in.readString();
        this.performerID = in.readString();
        this.performState = in.readString();
        this.isOpportunity = in.readString();
        this.performerName = in.readString();
        this.performDesc = in.readString();
        this.venueName = in.readString();
        this.venueAddress = in.readString();
        this.publishType = in.readString();
        this.venueLongitude = in.readString();
        this.venueLatitude = in.readString();
        this.performDate = in.readString();
        this.performTime = in.readString();
        this.performerImg = in.readString();
        this.performerMusic = in.readString();
        this.performerScore = in.readString();
        this.performerFacebook = in.readString();
        this.performerTwitter = in.readString();
        this.performerSoundcloud = in.readString();
        this.performerSignature = in.readString();
        this.venueScore = in.readString();
        this.venueFacebook = in.readString();
        this.venueTwitter = in.readString();
        this.venueTypeName = in.readString();
        this.isHaveCharges = in.readString();
        this.venueBookingPhone = in.readString();
        this.venueWebSite = in.readString();
        this.venueImg = in.readString();
        this.venueEmail = in.readString();
        this.performList = new ArrayList<PerformDetail>();
        in.readList(this.performList, PerformDetail.class.getClassLoader());
        this.newPerformInfo = in.readParcelable(PerformDetail.class.getClassLoader());
        this.isEvaluate = in.readString();
        this.performerStarLevel = in.readString();
        this.evaluateDate = in.readString();
        this.evaluateContent = in.readString();
        this.distance = in.readString();
    }

    public static final Creator<PerformDetail> CREATOR = new Creator<PerformDetail>() {
        @Override
        public PerformDetail createFromParcel(Parcel source) {
            return new PerformDetail(source);
        }

        @Override
        public PerformDetail[] newArray(int size) {
            return new PerformDetail[size];
        }
    };
}
