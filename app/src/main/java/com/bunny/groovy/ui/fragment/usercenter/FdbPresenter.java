package com.bunny.groovy.ui.fragment.usercenter;

import android.text.TextUtils;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;

import java.util.HashMap;

import retrofit2.http.FieldMap;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/12.
 ****************************************/

public class FdbPresenter extends BasePresenter<IFView> {
    public FdbPresenter(IFView view) {
        super(view);
    }

    public void feedback(String content) {
        HashMap<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("deviceType", "1");
        if (AppConstants.USER_TYPE_NORMAL != Utils.parseInt(AppCacheData.getPerformerUserModel().getUserType())) {
            map.put("userID", AppCacheData.getPerformerUserModel().getUserID());
        }
        addSubscription(apiService.addVenueFeesback(map)
                , new SubscriberCallBack(mView.get()) {
                    @Override
                    protected void onSuccess(Object response) {
                        UIUtils.showBaseToast("Success");
                        mView.get().finish();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }

                });
    }
}
