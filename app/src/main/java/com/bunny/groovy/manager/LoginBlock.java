package com.bunny.groovy.manager;

import com.bunny.groovy.base.BaseApp;
import com.bunny.groovy.model.LoginRequest;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.ui.MainActivity;
import com.bunny.groovy.ui.UserMainActivity;
import com.bunny.groovy.ui.VenueMainActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.SharedPreferencesUtils;
import com.bunny.groovy.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mysty on 2018/2/26.
 */

public class LoginBlock {

    private final String SP_FILE_LOGIN = "login";
    private final String KEY_LOGIN_REQUEST = "login_request";


    //单例
    private static LoginBlock instance;

    //单例
    private LoginBlock() {
    }

    //单例
    public static synchronized LoginBlock getInstance() {
        if (instance == null) {
            instance = new LoginBlock();
        }
        return instance;
    }

    public void saveLoginRequest(LoginRequest loginRequest) {
        String jsonString = new Gson().toJson(loginRequest);
        SharedPreferencesUtils.setUserParam(BaseApp.getContext(), KEY_LOGIN_REQUEST, jsonString);
    }

    public void clearLoginRequest() {
        SharedPreferencesUtils.setUserParam(BaseApp.getContext(), KEY_LOGIN_REQUEST, "");
    }

    public LoginRequest getLoginRequest() {
        String jsonString = (String) SharedPreferencesUtils.getUserParam(BaseApp.getContext(), KEY_LOGIN_REQUEST,"");
        if (jsonString != null) {
            return new Gson().fromJson(jsonString, LoginRequest.class);
        }
        return null;
    }

    public void handleLoginSuccess(LoginRequest loginRequest, PerformerUserModel userInfo) {
        saveLoginRequest(loginRequest);
        AppCacheData.setPerformerUserModel(userInfo);
        handleCheckSuccess(userInfo.getUserType());
    }

    public void handleCheckSuccess(String type){
        int userType = Utils.parseInt(type);
        switch (userType) {
            case AppConstants.USER_TYPE_NORMAL:
                UserMainActivity.start(BaseApp.getContext());
                break;
            case AppConstants.USER_TYPE_MUSICIAN:
                MainActivity.launch(BaseApp.getContext());
                break;
            case AppConstants.USER_TYPE_VENUE:
                VenueMainActivity.start(BaseApp.getContext());
                break;
        }
        EventBus.getDefault().post(AppConstants.EVENT_LOGIN_SUCCESS);
    }

}
