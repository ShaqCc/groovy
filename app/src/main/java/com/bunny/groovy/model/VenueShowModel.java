package com.bunny.groovy.model;

import android.os.Parcel;
import android.os.Parcelable;

/****************************************
 * 功能说明:  日程中表演者信息
 *
 ****************************************/

public class VenueShowModel implements Parcelable {

    /**
     "createDate": "2018-02-08 20:51",
     "performID": "1234",
     "venueID": "32",
     "performStartDate": "2018-03-08 13:00",
     "performEndDate": "2018-03-08 21:00",
     "performType": "FOLK",
     "performerID": "29",
     "performState": "0",
     "isOpportunity": null,
     "performerName": "熊孩子",
     "performDesc": "流行851",
     "venueName": "龙东大道金科路",
     "venueAddress": "中国上海市浦东新区龙东大道 邮政编码: 201203",
     "publishType": "0",
     "venueLongitude": "121.6000",
     "venueLatitude": "31.2200",
     "performDate": "Mar 08 2018",
     "performTime": "1:00PM-9:00PM",
     "performerImg": "http://47.100.104.82:8083/upload/headImgFile/headImg_1514280946972.jpg",
     "performerMusic": "http://47.100.104.82:8083/upload/musicFile/music_1514280946971.m4a",
     "performerScore": "3.0000",
     "performerFacebook": "ffghj",
     "performerTwitter": "ccbhj",
     "performerSoundcloud": "ffghh",
     "performerSignature": "hhjjj",
     "venueScore": "0.00",
     "venueFacebook": "246781",
     "venueTwitter": "356889",
     "venueTypeName": "Exclude 21+",
     "isHaveCharges": "1",
     "venueBookingPhone": "13761434342",
     "venueWebSite": "tx-46789",
     "venueImg": "http://47.100.104.82:8083/upload/headImgFile/headImg_1516593311012.jpg",
     "venueEmail": null,
     "performList": null,
     "newPerformInfo": null,
     "isEvaluate": null,
     "performerStarLevel": null,
     "evaluateDate": null,
     "evaluateContent": null,
     "distance": null
     */

    private String createDate;
    private String performID;
    private String venueID;
    private String performStartDate;
    private String performEndDate;
    private String performType;
    private String performerID;
    private String performState;
    private String isOpportunity;
    private String performerName;
    private String performDesc;
    private String venueName;
    private String venueAddress;
    private String publishType;
    private String venueLongitude;
    private String venueLatitude;
    private String performDate;
    private String performTime;
    private String performerImg;
    private String performerMusic;
    private String performerScore;
    private String performerFacebook;
    private String performerTwitter;
    private String performerSoundcloud;
    private String performerSignature;
    private String venueScore;
    private String venueFacebook;
    private String venueTwitter;
    private String venueTypeName;
    private String isHaveCharges;
    private String venueBookingPhone;
    private String venueWebSite;
    private String venueImg;
    private String venueEmail;
    private String performList;
    private String newPerformInfo;
    private String isEvaluate;
    private String performerStarLevel;
    private String evaluateDate;
    private String evaluateContent;
    private String distance;


    protected VenueShowModel(Parcel in) {
        createDate = in.readString();
        performID = in.readString();
        venueID = in.readString();
        performStartDate = in.readString();
        performEndDate = in.readString();
        performType = in.readString();
        performerID = in.readString();
        performState = in.readString();
        isOpportunity = in.readString();
        performerName = in.readString();
        performDesc = in.readString();
        venueName = in.readString();
        venueAddress = in.readString();
        publishType = in.readString();
        venueLongitude = in.readString();
        venueLatitude = in.readString();
        performDate = in.readString();
        performTime = in.readString();
        performerImg = in.readString();
        performerMusic = in.readString();
        performerScore = in.readString();
        performerFacebook = in.readString();
        performerTwitter = in.readString();
        performerSoundcloud = in.readString();
        performerSignature = in.readString();
        venueScore = in.readString();
        venueFacebook = in.readString();
        venueTwitter = in.readString();
        venueTypeName = in.readString();
        isHaveCharges = in.readString();
        venueBookingPhone = in.readString();
        venueWebSite = in.readString();
        venueImg = in.readString();
        venueEmail = in.readString();
        performList = in.readString();
        newPerformInfo = in.readString();
        isEvaluate = in.readString();
        performerStarLevel = in.readString();
        evaluateDate = in.readString();
        evaluateContent = in.readString();
        distance = in.readString();
    }

    public static final Creator<VenueShowModel> CREATOR = new Creator<VenueShowModel>() {
        @Override
        public VenueShowModel createFromParcel(Parcel in) {
            return new VenueShowModel(in);
        }

        @Override
        public VenueShowModel[] newArray(int size) {
            return new VenueShowModel[size];
        }
    };


    public String getCreateDate() {
        return createDate;
    }

    public String getPerformID() {
        return performID;
    }

    public String getVenueID() {
        return venueID;
    }

    public String getPerformStartDate() {
        return performStartDate;
    }

    public String getPerformEndDate() {
        return performEndDate;
    }

    public String getPerformType() {
        return performType;
    }

    public String getPerformerID() {
        return performerID;
    }

    public String getPerformState() {
        return performState;
    }

    public String getIsOpportunity() {
        return isOpportunity;
    }

    public String getPerformerName() {
        return performerName;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public String getPublishType() {
        return publishType;
    }

    public String getVenueLongitude() {
        return venueLongitude;
    }

    public String getVenueLatitude() {
        return venueLatitude;
    }

    public String getPerformDate() {
        return performDate;
    }

    public String getPerformTime() {
        return performTime;
    }

    public String getPerformerImg() {
        return performerImg;
    }

    public String getPerformerMusic() {
        return performerMusic;
    }

    public String getPerformerScore() {
        return performerScore;
    }

    public String getPerformerFacebook() {
        return performerFacebook;
    }

    public String getPerformerTwitter() {
        return performerTwitter;
    }

    public String getPerformerSoundcloud() {
        return performerSoundcloud;
    }

    public String getPerformerSignature() {
        return performerSignature;
    }

    public String getVenueScore() {
        return venueScore;
    }

    public String getVenueFacebook() {
        return venueFacebook;
    }

    public String getVenueTwitter() {
        return venueTwitter;
    }

    public String getVenueTypeName() {
        return venueTypeName;
    }

    public String getIsHaveCharges() {
        return isHaveCharges;
    }

    public String getVenueBookingPhone() {
        return venueBookingPhone;
    }

    public String getVenueWebSite() {
        return venueWebSite;
    }

    public String getVenueImg() {
        return venueImg;
    }

    public String getVenueEmail() {
        return venueEmail;
    }

    public String getPerformList() {
        return performList;
    }

    public String getNewPerformInfo() {
        return newPerformInfo;
    }

    public String getIsEvaluate() {
        return isEvaluate;
    }

    public String getPerformerStarLevel() {
        return performerStarLevel;
    }

    public String getEvaluateDate() {
        return evaluateDate;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public String getDistance() {
        return distance;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setPerformID(String performID) {
        this.performID = performID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public void setPerformStartDate(String performStartDate) {
        this.performStartDate = performStartDate;
    }

    public void setPerformEndDate(String performEndDate) {
        this.performEndDate = performEndDate;
    }

    public void setPerformType(String performType) {
        this.performType = performType;
    }

    public void setPerformerID(String performerID) {
        this.performerID = performerID;
    }

    public void setPerformState(String performState) {
        this.performState = performState;
    }

    public void setIsOpportunity(String isOpportunity) {
        this.isOpportunity = isOpportunity;
    }

    public void setPerformerName(String performerName) {
        this.performerName = performerName;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public void setVenueLongitude(String venueLongitude) {
        this.venueLongitude = venueLongitude;
    }

    public void setVenueLatitude(String venueLatitude) {
        this.venueLatitude = venueLatitude;
    }

    public void setPerformDate(String performDate) {
        this.performDate = performDate;
    }

    public void setPerformTime(String performTime) {
        this.performTime = performTime;
    }

    public void setPerformerImg(String performerImg) {
        this.performerImg = performerImg;
    }

    public void setPerformerMusic(String performerMusic) {
        this.performerMusic = performerMusic;
    }

    public void setPerformerScore(String performerScore) {
        this.performerScore = performerScore;
    }

    public void setPerformerFacebook(String performerFacebook) {
        this.performerFacebook = performerFacebook;
    }

    public void setPerformerTwitter(String performerTwitter) {
        this.performerTwitter = performerTwitter;
    }

    public void setPerformerSoundcloud(String performerSoundcloud) {
        this.performerSoundcloud = performerSoundcloud;
    }

    public void setPerformerSignature(String performerSignature) {
        this.performerSignature = performerSignature;
    }

    public void setVenueScore(String venueScore) {
        this.venueScore = venueScore;
    }

    public void setVenueFacebook(String venueFacebook) {
        this.venueFacebook = venueFacebook;
    }

    public void setVenueTwitter(String venueTwitter) {
        this.venueTwitter = venueTwitter;
    }

    public void setVenueTypeName(String venueTypeName) {
        this.venueTypeName = venueTypeName;
    }

    public void setIsHaveCharges(String isHaveCharges) {
        this.isHaveCharges = isHaveCharges;
    }

    public void setVenueBookingPhone(String venueBookingPhone) {
        this.venueBookingPhone = venueBookingPhone;
    }

    public void setVenueWebSite(String venueWebSite) {
        this.venueWebSite = venueWebSite;
    }

    public void setVenueImg(String venueImg) {
        this.venueImg = venueImg;
    }

    public void setVenueEmail(String venueEmail) {
        this.venueEmail = venueEmail;
    }

    public void setPerformList(String performList) {
        this.performList = performList;
    }

    public void setNewPerformInfo(String newPerformInfo) {
        this.newPerformInfo = newPerformInfo;
    }

    public void setIsEvaluate(String isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public void setPerformerStarLevel(String performerStarLevel) {
        this.performerStarLevel = performerStarLevel;
    }

    public void setEvaluateDate(String evaluateDate) {
        this.evaluateDate = evaluateDate;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
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
        dest.writeString(createDate);
        dest.writeString(performID);
        dest.writeString(venueID);
        dest.writeString(performStartDate);
        dest.writeString(performEndDate);
        dest.writeString(performType);
        dest.writeString(performerID);
        dest.writeString(performState);
        dest.writeString(isOpportunity);
        dest.writeString(performerName);
        dest.writeString(performDesc);
        dest.writeString(venueName);
        dest.writeString(venueAddress);
        dest.writeString(publishType);
        dest.writeString(venueLongitude);
        dest.writeString(venueLatitude);
        dest.writeString(performDate);
        dest.writeString(performTime);
        dest.writeString(performerImg);
        dest.writeString(performerMusic);
        dest.writeString(performerScore);
        dest.writeString(performerFacebook);
        dest.writeString(performerTwitter);
        dest.writeString(performerSoundcloud);
        dest.writeString(performerSignature);
        dest.writeString(venueScore);
        dest.writeString(venueFacebook);
        dest.writeString(venueTwitter);
        dest.writeString(venueTypeName);
        dest.writeString(isHaveCharges);
        dest.writeString(venueBookingPhone);
        dest.writeString(venueWebSite);
        dest.writeString(venueImg);
        dest.writeString(venueEmail);
        dest.writeString(performList);
        dest.writeString(newPerformInfo);
        dest.writeString(isEvaluate);
        dest.writeString(performerStarLevel);
        dest.writeString(evaluateDate);
        dest.writeString(evaluateContent);
        dest.writeString(distance);
    }
}
