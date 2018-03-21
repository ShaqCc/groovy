package com.bunny.groovy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mysty on 2018/2/26.
 */

public class LoginRequest implements Parcelable {

    private String userAccount;//账号
    private String userPwd;//密码
    private String userType;//0普通用户, 1表演者, 2演出厅
    private String userZone;//用户时区

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserZone() {
        return userZone;
    }

    public void setUserZone(String userZone) {
        this.userZone = userZone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userAccount);
        dest.writeString(this.userPwd);
        dest.writeString(this.userType);
        dest.writeString(this.userZone);
    }

    public LoginRequest() {
    }

    protected LoginRequest(Parcel in) {
        this.userAccount = in.readString();
        this.userPwd = in.readString();
        this.userType = in.readString();
        this.userZone = in.readString();
    }

    public static final Creator<LoginRequest> CREATOR = new Creator<LoginRequest>() {
        @Override
        public LoginRequest createFromParcel(Parcel source) {
            return new LoginRequest(source);
        }

        @Override
        public LoginRequest[] newArray(int size) {
            return new LoginRequest[size];
        }
    };
}
