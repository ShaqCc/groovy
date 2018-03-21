package com.bunny.groovy.presenter;

import android.text.TextUtils;

import com.bunny.groovy.api.ApiConstants;
import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.GoogleMapLoc;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.ui.MainActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IMeView;
import com.bunny.groovy.weidget.ProgressHUD;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * 个人中心 控制器
 * Created by Administrator on 2017/12/21.
 */

public class MePresenter extends BasePresenter<IMeView> {
    public MePresenter(IMeView view) {
        super(view);
    }

    /**
     * 获取用户数据
     */
    public void requestUserData() {
        addSubscription(apiService.getPerformerInfo(),
                new SubscriberCallBack<PerformerUserModel>(mView.get()) {
                    @Override
                    protected void onSuccess(PerformerUserModel response) {
                        mView.setUserView(response);
                        Utils.initLoginData(mView.get(),response);
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {

                    }
                });
    }

    /**
     * 获取表演style
     */
    public void requestStyle() {
        addSubscription(apiService.getPerformStyle(), new SubscriberCallBack<List<StyleModel>>(mView.get()) {
            @Override
            protected void onSuccess(List<StyleModel> response) {
                if (response != null && response.size() > 0) {
                    mView.showStylePop(response);
                } else {
                    UIUtils.showBaseToast("获取style失败，稍后再试");
                }
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
    public void updatePerformerData(Map<String, String> fileMap) {
        fileMap.put("userID",AppCacheData.getPerformerUserModel().getUserID());
        //头像图片
        String imagePath = fileMap.get("imgfile");
        //音频文件
        String musicPath = fileMap.get("music");
        //构建body
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set<Map.Entry<String, String>> entries = fileMap.entrySet();
        for (Map.Entry<String, String> entry :
                entries) {
            //文本参数
            if (!TextUtils.isEmpty(entry.getValue()) && (!entry.getValue().equals("imgfile") || !entry.getValue().equals("music"))) {
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

        if (!TextUtils.isEmpty(musicPath)) {
            File music = new File(musicPath);
            if (music.isFile()) {
                builder.addFormDataPart("music", music.getName(), RequestBody.create(MultipartBody.FORM, music));
            }
        } else {
            builder.addFormDataPart("music", "music.mp3", RequestBody.create(MultipartBody.FORM, ""));
        }


        RequestBody build = builder.build();

        addSubscription(apiService.updateUserProfile(build), new SubscriberCallBack<Object>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                MainActivity.launch(mView.get());
            }

            @Override
            protected void onFailure(ResultResponse response) {
            }
        });
    }


    /**
     * 查询地址经纬度
     *
     * @param zipCode
     */
    public void searchLocation(String zipCode, final HashMap<String, String> params) {
        addSubscription(apiService.getLocation(zipCode, ApiConstants.GoogleMapAppKey), new Subscriber<GoogleMapLoc>() {
            ProgressHUD mProgressHUD;

            @Override
            public void onStart() {
                super.onStart();
                mProgressHUD = ProgressHUD.show(mView.get(), "", true, true, null);
                mProgressHUD.setCancelable(false);
                mProgressHUD.setMessage("");
                mProgressHUD.show();
            }

            @Override
            public void onCompleted() {
                if (mProgressHUD != null) mProgressHUD.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                if (mProgressHUD != null) mProgressHUD.dismiss();
                UIUtils.showBaseToast(e.toString());
            }

            /**
             * @param loc
             */
            @Override
            public void onNext(GoogleMapLoc loc) {
                try {
                    if ("OK".equals(loc.getStatus())) {
                        //上传信息
                        params.put("longitude", loc.getResults().get(0).getGeometry().getLocation().getLng());
                        params.put("latitude", loc.getResults().get(0).getGeometry().getLocation().getLat());
                        updatePerformerData(params);
                    } else {
                        UIUtils.showBaseToast("邮编错误" + loc.getError_message());
                    }
                } catch (Exception e) {
                    UIUtils.showBaseToast(e.toString());
                }
            }
        });
    }
}
