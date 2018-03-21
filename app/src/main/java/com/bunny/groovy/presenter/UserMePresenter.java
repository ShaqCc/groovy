package com.bunny.groovy.presenter;

import android.text.TextUtils;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IMeView;

import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 普通用户个人中心 控制器
 * Created by Administrator on 2017/12/21.
 */

public class UserMePresenter extends BasePresenter<IMeView> {
    public UserMePresenter(IMeView view) {
        super(view);
    }

    /**
     * 获取用户数据
     */
    public void requestUserData() {
        addSubscription(apiService.getUserInfo(),
                new SubscriberCallBack<PerformerUserModel>(mView.get()) {
                    @Override
                    protected void onSuccess(PerformerUserModel response) {
                        response.setUserType(String.valueOf(AppConstants.USER_TYPE_NORMAL));
                        mView.setUserView(response);
                        Utils.initLoginData(mView.get(), response);
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }

    /**
     * 评价表演者
     */
    public void evaluatePerformer(String performerID, String performerStarLevel, String evaluateContent) {
        addSubscription(apiService.evaluatePerformer(performerID, performerStarLevel, evaluateContent),
                new SubscriberCallBack<Object>(mView.get()) {
                    @Override
                    protected void onSuccess(Object response) {
                        mView.get().finish();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }

    /**
     * 上传信息
     *
     * @param fileMap
     */
    public void updateUserInfo(Map<String, String> fileMap) {
        final String userName = fileMap.get("userName");
        fileMap.put("userID", AppCacheData.getPerformerUserModel().getUserID());
        //头像图片
        String imagePath = fileMap.get("imgfile");
        //构建body
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set<Map.Entry<String, String>> entries = fileMap.entrySet();
        for (Map.Entry<String, String> entry :
                entries) {
            //文本参数
            if (!TextUtils.isEmpty(entry.getValue()) && (!entry.getValue().equals("imgfile"))) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //文件参数
        if (!TextUtils.isEmpty(imagePath)) {
            File img = new File(imagePath);
            if (img.isFile()) {
                builder.addFormDataPart("imgfile", img.getName(), RequestBody.create(MultipartBody.FORM, img));
            }
        } else {
            builder.addFormDataPart("imgfile", "image.jpg", RequestBody.create(MultipartBody.FORM, ""));
        }
        RequestBody build = builder.build();

        addSubscription(apiService.updateUserInfo(build), new SubscriberCallBack<Object>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                requestNewUserData();
            }

            @Override
            protected void onFailure(ResultResponse response) {
            }
        });
    }

    /**
     * 获取用户数据
     */
    public void requestNewUserData() {
        addSubscription(apiService.getUserInfo(),
                new SubscriberCallBack<PerformerUserModel>(mView.get()) {
                    @Override
                    protected boolean isShowProgress() {
                        return true;
                    }

                    @Override
                    protected void onSuccess(PerformerUserModel response) {
                        response.setUserType(String.valueOf(AppConstants.USER_TYPE_NORMAL));
                        Utils.initLoginData(mView.get(), response);
                        mView.get().finish();
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }

}
