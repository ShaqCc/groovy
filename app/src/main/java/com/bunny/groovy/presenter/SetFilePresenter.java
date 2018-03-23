package com.bunny.groovy.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.bunny.groovy.api.ApiConstants;
import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.GoogleMapLoc;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.ui.MainActivity;
import com.bunny.groovy.ui.setfile.SetFile2Activity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;
import com.bunny.groovy.weidget.ProgressHUD;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/12.
 ****************************************/

public class SetFilePresenter extends BasePresenter<ISetFileView> {
    public SetFilePresenter(ISetFileView view) {
        super(view);
    }

    /**
     * 查询地址经纬度
     *
     * @param zipCode
     */
    public void searchLocation(String zipCode) {
        addSubscription(apiService.getLocation(zipCode, ApiConstants.GOOGLE_MAP_APP_KEY), new Subscriber<GoogleMapLoc>() {
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
                        AppCacheData.getFileMap().put("longitude", loc.getResults().get(0).getGeometry().getLocation().getLng());
                        AppCacheData.getFileMap().put("latitude", loc.getResults().get(0).getGeometry().getLocation().getLat());
                        //下一页
                        mView.get().startActivityForResult(new Intent(mView.get(), SetFile2Activity.class), 2);
                    } else {
                        UIUtils.showBaseToast("The code is wrong.");
                    }
                } catch (Exception e) {
                    UIUtils.showBaseToast(e.toString());
                }
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
                    UIUtils.showBaseToast("Get Style Failed");
                }
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onFailure(ResultResponse response) {
                UIUtils.showBaseToast("Get Style Failed");
            }
        });
    }

    /**
     * 上次用户资料
     *
     * @param fileMap
     */
    public void updateUserInfo(Map<String, String> fileMap) {
        fileMap.put("userID", AppCacheData.getPerformerUserModel().getUserID());
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
                builder.addFormDataPart("imgfile", img.getName(), RequestBody.create(MediaType.parse("image/*"), img));
            }
        } else {
            builder.addFormDataPart("imgfile", "image.jpg", RequestBody.create(MediaType.parse("image/*"), ""));
        }

        if (!TextUtils.isEmpty(musicPath)) {
            File music = new File(musicPath);
            if (music.isFile()) {
                builder.addFormDataPart("music", music.getName(), RequestBody.create(MediaType.parse("*/*"), music));
            }
        } else {
            builder.addFormDataPart("music", "music.mp3", RequestBody.create(MediaType.parse("*/*"), ""));
        }


        RequestBody build = builder.build();

        addSubscription(apiService.updatePerformerInfo(build), new SubscriberCallBack<Object>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                MainActivity.launch(mView.get());
                mView.get().setResult(AppConstants.ACTIVITY_FINISH);
                mView.get().finish();
            }

            @Override
            protected void onFailure(ResultResponse response) {
            }
        });

    }
}
