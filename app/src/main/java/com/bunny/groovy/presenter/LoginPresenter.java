package com.bunny.groovy.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.model.GlobalModel;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.ui.MainActivity;
import com.bunny.groovy.ui.login.BindAccountFragment;
import com.bunny.groovy.ui.login.VenueRegister1Activity;
import com.bunny.groovy.ui.setfile.SetFile1Activity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.ILoginView;

/****************************************
 * 功能说明:  登录控制器
 *
 * Author: Created by bayin on 2017/12/13.
 ****************************************/

public class LoginPresenter extends BasePresenter<ILoginView> {
    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void login(String account, String password, final int type) {
        addSubscription(apiService.performerLogin(account, password, String.valueOf(type), Utils.getTimeZone()),
                new SubscriberCallBack<PerformerUserModel>(mView.get()) {
                    /**
                     * @param response
                     */
                    @Override
                    protected void onSuccess(PerformerUserModel response) {
                        //缓存到本地
                        Utils.initLoginData(mView.get(), response);
                        if (response != null) {
                            //获取全局参数
                            getGlobParam();
                            //判断资料是否完善
                            int userType = Utils.parseInt(response.getUserType());
                            //需要完善信息
                            if (userType == AppConstants.USER_TYPE_MUSICIAN
                                    && TextUtils.isEmpty(response.getZipCode())) {
                                mView.get().startActivityForResult(new Intent(mView.get(), SetFile1Activity.class), AppConstants.REQUESTCODE_SETFILE);
                            } else if (userType == AppConstants.USER_TYPE_VENUE
                                    && TextUtils.isEmpty(response.getVenueTypeName())) {
                                mView.get().startActivityForResult(new Intent(mView.get(), VenueRegister1Activity.class), AppConstants.REQUESTCODE_SETFILE);
                            } else {
                                //进入主页
                                mView.launchMainPage();
                            }
                        }
                    }

                    @Override
                    protected void onFailure(ResultResponse response) {
                    }

                    @Override
                    protected boolean isShowProgress() {
                        return true;
                    }
                });
    }

    public void getGlobParam() {
        //获取全局参数
        addSubscription(apiService.getGlobalParam(), new SubscriberCallBack<GlobalModel>(null) {
            @Override
            protected void onSuccess(GlobalModel response) {
                AppCacheData.setGlobalModel(response);
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }

    /**
     * 检查是否绑定该id
     *
     * @param loginType 类型
     * @param uid       账户
     */
    public void checkHadBindUid(final String loginType, final String uid, final String username, final String userType) {
        addSubscription(apiService.checkUidNotLogin(loginType, userType, uid, Utils.getTimeZone()), new SubscriberCallBack<PerformerUserModel>(mView.get()) {
            @Override
            protected void onSuccess(PerformerUserModel response) {
                //已经绑定，直接登录
                if (response != null) {
                    //获取全局参数
                    getGlobParam();
                    //判断资料是否完善
                    int type = Utils.parseInt(userType);
                    if (type == AppConstants.USER_TYPE_MUSICIAN
                            && TextUtils.isEmpty(response.getZipCode())) {
                        mView.get().startActivityForResult(new Intent(mView.get(), SetFile1Activity.class), AppConstants.REQUESTCODE_SETFILE);
                    } else if (type == AppConstants.USER_TYPE_VENUE
                            && TextUtils.isEmpty(response.getVenueTypeName())) {
                        mView.get().startActivityForResult(new Intent(mView.get(), VenueRegister1Activity.class), AppConstants.REQUESTCODE_SETFILE);
                    } else {
                        //进入主页
                        MainActivity.launch(mView.get());
                    }
                } else {
                    //未绑定任何账户，跳转到绑定账户页面
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("logintype", loginType);
                    bundle.putString("uid", uid);
                    bundle.putString("userType", userType);
                    BindAccountFragment.launch(mView.get(), bundle);
                }
            }

            @Override
            protected void onFailure(ResultResponse response) {
                //未绑定任何账户，跳转到绑定账户页面
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("logintype", loginType);
                bundle.putString("uid", uid);
                bundle.putString("userType", userType);
                BindAccountFragment.launch(mView.get(), bundle);
            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }
        });
    }

    /**
     * 发送邮件验证码
     *
     * @param email
     */
    public void socialSendEmailCode(String email) {
        addSubscription(apiService.socialSendEmailCode(email), new SubscriberCallBack(mView.get()) {
            @Override
            protected void onSuccess(Object response) {
                UIUtils.showBaseToast("Send Success.");
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }

            @Override
            protected boolean isShowProgress() {
                return true;
            }
        });
    }

    /**
     * loginType
     * uid
     * userName
     * userAccount
     * userType
     * userZone
     */
    public void socialLogin(String logintype, String uid, String username, String useraccount, final String userType) {
        addSubscription(apiService.socialAccountLogin(logintype, uid, username,
                useraccount, userType, Utils.getTimeZone()), new SubscriberCallBack<PerformerUserModel>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(PerformerUserModel response) {
                //登录，判断是否完善了资料
                Utils.initLoginData(mView.get(), response);
                switch (Utils.parseInt(userType)) {
                    case AppConstants.USER_TYPE_VENUE:
                        if (response == null || TextUtils.isEmpty(response.getStageName()) ||
                                TextUtils.isEmpty(response.getVenueTypeName())) {
                            mView.launchToSetFile();
                            return;
                        }
                        break;
                    case AppConstants.USER_TYPE_MUSICIAN:
                        if (response == null || TextUtils.isEmpty(response.getVenueTypeName()) ||
                                TextUtils.isEmpty(response.getUserType())) {
                            mView.launchToSetFile();
                            return;
                        }
                        break;
                }
                mView.launchMainPage();
            }

            @Override
            protected void onFailure(ResultResponse response) {

            }
        });
    }


}
