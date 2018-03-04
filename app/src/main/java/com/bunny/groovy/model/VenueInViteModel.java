package com.bunny.groovy.model;

import android.os.Parcel;
import android.os.Parcelable;

/****************************************
 * 功能说明:  演出厅用户的Notification中的Invite-获取邀请列表及详情信息的model
 * 具体的按实际返回数据为准，缺的后续再加
 ****************************************/

public class VenueInViteModel implements Parcelable {


    /**
     * createDate : 2018-03-02 20:16
     * inviteID : 60
     * venueID : 32
     * performStartDate : 2018-04-08 01:00
     * performEndDate : 2018-04-08 03:00
     * performType : null
     * performDesc : null
     * performerID : 29
     * invitationState : 0-未处理，1-同意，2-拒绝
     * handlerDate :
     * invitationDesc : 流行
     * userName : 熊孩子
     * userEmail : null
     * telephone : null
     * headImg : http://47.100.104.82:8083/upload/headImgFile/headImg_1514280946972.jpg
     * performTypeName : null
     * webSiteAddress : null
     * longitude : -71.7000
     * latitude : 43.7200
     * venueScore : null
     * signature : hhjjj
     * musicFile : http://47.100.104.82:8083/upload/musicFile/music_1514280946971.m4a
     * paypalAccount : null
     * twitterAccount : ccbhj
     * soundcloudAccount : ffghh
     * facebookAccount : ffghj
     * stageName : 熊豆豆
     * distance : null
     * starLevel : 3
     * performDate : Apr 08 2018
     * performTime : 1:00AM-3:00AM
     */


    private String createDate;
    private String inviteID;
    private String venueID;
    private String performStartDate;
    private String performEndDate;
    private String performType;
    private String performDesc;
    private String performerID;
    private String invitationState;
    private String handlerDate;
    private String invitationDesc;
    private String userName;
    private String userEmail;
    private String headImg;
    private String performTypeName;
    private String webSiteAddress;
    private String longitude;
    private String latitude;
    private String venueScore;
    private String signature;
    private String musicFile;
    private String paypalAccount;
    private String twitterAccount;
    private String soundcloudAccount;
    private String facebookAccount;
    private String stageName;
    private String distance;
    private String starLevel;
    private String performDate;
    private String performTime;


    protected VenueInViteModel(Parcel in) {
        createDate = in.readString();
        inviteID = in.readString();
        venueID = in.readString();
        performStartDate = in.readString();
        performEndDate = in.readString();
        performType = in.readString();
        performDesc = in.readString();
        performerID = in.readString();
        invitationState = in.readString();
        handlerDate = in.readString();
        invitationDesc = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        headImg = in.readString();
        performTypeName = in.readString();
        webSiteAddress = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        venueScore = in.readString();
        signature = in.readString();
        musicFile = in.readString();
        paypalAccount = in.readString();
        twitterAccount = in.readString();
        soundcloudAccount = in.readString();
        facebookAccount = in.readString();
        stageName = in.readString();
        distance = in.readString();
        starLevel = in.readString();
        performDate = in.readString();
        performTime = in.readString();

    }

    public static final Creator<VenueInViteModel> CREATOR = new Creator<VenueInViteModel>() {
        @Override
        public VenueInViteModel createFromParcel(Parcel in) {
            return new VenueInViteModel(in);
        }

        @Override
        public VenueInViteModel[] newArray(int size) {
            return new VenueInViteModel[size];
        }
    };

    public String getCreateDate() {
        return createDate;
    }

    public String getInviteID() {
        return inviteID;
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

    public String getPerformDesc() {
        return performDesc;
    }

    public String getPerformerID() {
        return performerID;
    }

    public String getInvitationState() {
        return invitationState;
    }

    public String getHandlerDate() {
        return handlerDate;
    }

    public String getInvitationDesc() {
        return invitationDesc;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getHeadImg() {
        return headImg;
    }

    public String getPerformTypeName() {
        return performTypeName;
    }

    public String getWebSiteAddress() {
        return webSiteAddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getVenueScore() {
        return venueScore;
    }

    public String getSignature() {
        return signature;
    }

    public String getMusicFile() {
        return musicFile;
    }

    public String getPaypalAccount() {
        return paypalAccount;
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

    public String getStageName() {
        return stageName;
    }

    public String getDistance() {
        return distance;
    }

    public String getStarLevel() {
        return starLevel;
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

    public void setInviteID(String inviteID) {
        this.inviteID = inviteID;
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

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    public void setPerformerID(String performerID) {
        this.performerID = performerID;
    }

    public void setInvitationState(String invitationState) {
        this.invitationState = invitationState;
    }

    public void setHandlerDate(String handlerDate) {
        this.handlerDate = handlerDate;
    }

    public void setInvitationDesc(String invitationDesc) {
        this.invitationDesc = invitationDesc;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void setPerformTypeName(String performTypeName) {
        this.performTypeName = performTypeName;
    }

    public void setWebSiteAddress(String webSiteAddress) {
        this.webSiteAddress = webSiteAddress;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setVenueScore(String venueScore) {
        this.venueScore = venueScore;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setMusicFile(String musicFile) {
        this.musicFile = musicFile;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
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

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
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
        dest.writeString(inviteID);
        dest.writeString(venueID);
        dest.writeString(performStartDate);
        dest.writeString(performEndDate);
        dest.writeString(performType);
        dest.writeString(performDesc);
        dest.writeString(performerID);
        dest.writeString(invitationState);
        dest.writeString(handlerDate);
        dest.writeString(invitationDesc);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(headImg);
        dest.writeString(performTypeName);
        dest.writeString(webSiteAddress);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(venueScore);
        dest.writeString(signature);
        dest.writeString(musicFile);
        dest.writeString(paypalAccount);
        dest.writeString(twitterAccount);
        dest.writeString(soundcloudAccount);
        dest.writeString(facebookAccount);
        dest.writeString(stageName);
        dest.writeString(distance);
        dest.writeString(starLevel);
        dest.writeString(performDate);
        dest.writeString(performTime);
    }
}
