package com.bunny.groovy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/****************************************
 * 功能说明:  演出厅用户的Notification中的opportunity列表的model
 * 具体的按实际返回数据为准，缺的后续再加(接口文档里面一些字段有误)
 ****************************************/

public class VenueOpportunityModel implements Parcelable {

    /**
     "createDate": "2018-03-02 20:13",
     "opportunityID": "55",
     "venueID": "32",
     "startDate": "2018-03-06 01:00",
     "endDate": "2018-03-06 08:00",
     "performDesc": "流行813",
     "opportunityState": "0",
     "applyID": null,
     "operationDate": "",
     "performDate": "Mar 06 2018",
     "performTime": "1:00AM-8:00AM",
     "applyList": [{
         "createDate": "2018-03-02 20:14",
         "applyID": "68",
         "opportunityID": "55",
         "venueID": "32",
         "performStartDate": "2018-03-06 01:00",
         "performEndDate": "2018-03-06 08:00",
         "performType": "POP",
         "performDesc": "流行814",
         "performerID": "29",
         "applyState": "0",
         "handlerDate": null,
         "headImg": "http://47.100.104.82:8083/upload/headImgFile/headImg_1514280946972.jpg",
         "userName": "熊孩子",
         "stageName": "熊豆豆",
         "signature": null,
         "starLevel": "3",
         "twitterAccount": null,
         "soundcloudAccount": null,
         "facebookAccount": null,
         "musicFile": null,
         "distance": null,
         "longitude": null,
         "latitude": null,
         "venueName": null
     }]
     */


    private String createDate;
    private String opportunityID;
    private String venueID;
    private String startDate;
    private String endDate;
    private String performDesc;
    private String opportunityState;
    private String applyID;
    private String operationDate;
    private String performDate;
    private String performTime;

    private List<ApplyList> applyList;

    protected VenueOpportunityModel(Parcel in) {

        createDate = in.readString();
        opportunityID = in.readString();
        venueID = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        performDesc = in.readString();
        opportunityState = in.readString();
        applyID = in.readString();
        operationDate = in.readString();
        performDate = in.readString();
        performTime = in.readString();

    }

    public static final Creator<VenueOpportunityModel> CREATOR = new Creator<VenueOpportunityModel>() {
        @Override
        public VenueOpportunityModel createFromParcel(Parcel in) {
            return new VenueOpportunityModel(in);
        }

        @Override
        public VenueOpportunityModel[] newArray(int size) {
            return new VenueOpportunityModel[size];
        }
    };




    public String getCreateDate() {
        return createDate;
    }

    public String getOpportunityID() {
        return opportunityID;
    }

    public String getVenueID() {
        return venueID;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getPerformDesc() {
        return performDesc;
    }

    public String getOpportunityState() {
        return opportunityState;
    }

    public String getApplyID() {
        return applyID;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public String getPerformDate() {
        return performDate;
    }

    public String getPerformTime() {
        return performTime;
    }

    public List<ApplyList> getApplyList() {
        return applyList;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setOpportunityID(String opportunityID) {
        this.opportunityID = opportunityID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPerformDesc(String performDesc) {
        this.performDesc = performDesc;
    }

    public void setOpportunityState(String opportunityState) {
        this.opportunityState = opportunityState;
    }

    public void setApplyID(String applyID) {
        this.applyID = applyID;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public void setPerformDate(String performDate) {
        this.performDate = performDate;
    }

    public void setPerformTime(String performTime) {
        this.performTime = performTime;
    }

    public void setApplyList(List<ApplyList> applyList) {
        this.applyList = applyList;
    }

    public static class ApplyList {

        private String createDate;
        private String applyID;
        private String opportunityID;
        private String venueID;
        private String performStartDate;
        private String performEndDate;
        private String performType;
        private String performDesc;
        private String performerID;
        private String applyState;
        private String handlerDate;
        private String headImg;
        private String userName;
        private String stageName;
        private String signature;
        private String starLevel;
        private String twitterAccount;
        private String soundcloudAccount;
        private String facebookAccount;
        private String musicFile;
        private String distance;
        private String longitude;
        private String latitude;
        private String venueName;

        public String getCreateDate() {
            return createDate;
        }

        public String getApplyID() {
            return applyID;
        }

        public String getOpportunityID() {
            return opportunityID;
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

        public String getApplyState() {
            return applyState;
        }

        public String getHandlerDate() {
            return handlerDate;
        }

        public String getHeadImg() {
            return headImg;
        }

        public String getUserName() {
            return userName;
        }

        public String getStageName() {
            return stageName;
        }

        public String getSignature() {
            return signature;
        }

        public String getStarLevel() {
            return starLevel;
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

        public String getMusicFile() {
            return musicFile;
        }

        public String getDistance() {
            return distance;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getVenueName() {
            return venueName;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public void setApplyID(String applyID) {
            this.applyID = applyID;
        }

        public void setOpportunityID(String opportunityID) {
            this.opportunityID = opportunityID;
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

        public void setApplyState(String applyState) {
            this.applyState = applyState;
        }

        public void setHandlerDate(String handlerDate) {
            this.handlerDate = handlerDate;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setStageName(String stageName) {
            this.stageName = stageName;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public void setStarLevel(String starLevel) {
            this.starLevel = starLevel;
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

        public void setMusicFile(String musicFile) {
            this.musicFile = musicFile;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setVenueName(String venueName) {
            this.venueName = venueName;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(createDate);
        dest.writeString(opportunityID);
        dest.writeString(venueID);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(performDesc);
        dest.writeString(opportunityState);
        dest.writeString(applyID);
        dest.writeString(operationDate);
        dest.writeString(performDate);
        dest.writeString(performTime);
    }
}
