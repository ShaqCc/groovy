package com.bunny.groovy.model;

import android.os.Parcel;
import android.os.Parcelable;

/****************************************
 * 功能说明:  演出厅用户的Notification中的Apply-获取申请列表的model
 * 具体的按实际返回数据为准，缺的后续再加
 ****************************************/

public class VenueApplyModel implements Parcelable {

    /**
     * "createDate": "2018-02-28 16:08",
     * "performID": "1582",
     * "venueID": "32",
     * "performStartDate": "2018-03-22 01:00",
     * "performEndDate": "2018-03-22 02:00",
     * "performType": "JAZZ",
     * "performerID": "29",
     * "performState": "0",
     * "isOpportunity": "0",
     * "performerName": "熊豆豆",
     * "performDesc": "流行408",
     * "venueName": "龙东大道金科路",
     * "venueAddress": "中国上海市浦东新区龙东大道 邮政编码: 201203",
     * "publishType": "0",
     * "venueLongitude": "121.6000",
     * "venueLatitude": "31.2200",
     * "updateType": null,
     * "distance": null,
     * "starLevel": "3",
     * "userName": "熊孩子",
     * "stageName": "熊豆豆",
     * "longitude": "-71.7000",
     * "latitude": "43.7200",
     * "signature": "hhjjj",
     * "musicFile": "http://47.100.104.82:8083/upload/musicFile/music_1514280946971.m4a",
     * "twitterAccount": "ccbhj",
     * "soundcloudAccount": "ffghh",
     * "facebookAccount": "ffghj",
     * "headImg": "http://47.100.104.82:8083/upload/headImgFile/headImg_1514280946972.jpg",
     * "performDate": "Mar 22 2018",
     * "performTime": "1:00AM-2:00AM"
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
    private String updateType;
    private String distance;
    private String starLevel;
    private String userName;
    private String stageName;
    private String longitude;
    private String latitude;
    private String signature;
    private String musicFile;
    private String twitterAccount;
    private String soundcloudAccount;
    private String facebookAccount;
    private String headImg;
    private String performDate;
    private String performTime;

    protected VenueApplyModel(Parcel in) {
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
        updateType = in.readString();
        distance = in.readString();
        starLevel = in.readString();
        userName = in.readString();
        stageName = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        signature = in.readString();
        musicFile = in.readString();
        twitterAccount = in.readString();
        soundcloudAccount = in.readString();
        facebookAccount = in.readString();
        headImg = in.readString();
        performDate = in.readString();
        performTime = in.readString();

    }

    public static final Creator<VenueApplyModel> CREATOR = new Creator<VenueApplyModel>() {
        @Override
        public VenueApplyModel createFromParcel(Parcel in) {
            return new VenueApplyModel(in);
        }

        @Override
        public VenueApplyModel[] newArray(int size) {
            return new VenueApplyModel[size];
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

    public String getUpdateType() {
        return updateType;
    }

    public String getDistance() {
        return distance;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public String getUserName() {
        return userName;
    }

    public String getStageName() {
        return stageName;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getSignature() {
        return signature;
    }

    public String getMusicFile() {
        return musicFile;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public String getSoundcloudAccount() {
        return soundcloudAccount;
    }

    public String getFacebookAccount() {
        return facebookAccount;
    }

    public String getHeadImg() {
        return headImg;
    }

    public String getPerformDate() {
        return performDate;
    }

    public String getPerformTime() {
        return performTime;
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

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setMusicFile(String musicFile) {
        this.musicFile = musicFile;
    }

    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public void setSoundcloudAccount(String soundcloudAccount) {
        this.soundcloudAccount = soundcloudAccount;
    }

    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void setPerformDate(String performDate) {
        this.performDate = performDate;
    }

    public void setPerformTime(String performTime) {
        this.performTime = performTime;
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
        dest.writeString(updateType);
        dest.writeString(distance);
        dest.writeString(starLevel);
        dest.writeString(userName);
        dest.writeString(stageName);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(signature);
        dest.writeString(musicFile);
        dest.writeString(twitterAccount);
        dest.writeString(soundcloudAccount);
        dest.writeString(facebookAccount);
        dest.writeString(headImg);
        dest.writeString(performDate);
        dest.writeString(performTime);
    }
}
