package com.bunny.groovy.presenter;

import com.bunny.groovy.api.SubscriberCallBack;
import com.bunny.groovy.base.BaseApp;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.listener.MyVerificationListener;
import com.bunny.groovy.listener.VerifyEvent;
import com.bunny.groovy.model.ResultResponse;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.PatternUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISingUpView;
import com.bunny.groovy.weidget.ProgressHUD;
import com.sinch.verification.Config;
import com.sinch.verification.PhoneNumberUtils;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * 注册控制器
 * <p>
 * Created by Administrator on 2017/12/9.
 */

public class SingUpPresenter extends BasePresenter<ISingUpView> {

    private int type = 0;
    private VerificationListener mListener;
    private Verification mVerification;

    public SingUpPresenter(ISingUpView view) {
        super(view);
    }

    /**
     * 检查账户是否可用，和类型
     */
    public void checkAccount(final String account, final boolean next) {
        if (PatternUtils.isUSphonenumber(account) || PatternUtils.isCNPhone(account)) {
            //手机号
            type = AppConstants.ACCOUNT_TYPE_PHONE;
        } else if (PatternUtils.isValidEmail(account)) {
            //邮箱
            type = AppConstants.ACCOUNT_TYPE_EMAIL;
        } else {
            //不合法，提示用户
            mView.showCheckResult(false, type, "Invalid phone or email!");
            return;
        }

        //检测账户是否可用
        addSubscription(apiService.checkAccountUsed(account), new SubscriberCallBack<Object>(mView.get()) {
            @Override
            protected boolean isShowProgress() {
                return true;
            }

            @Override
            protected void onSuccess(Object response) {
                mView.showCheckResult(true, type, "");
                if (next) {
                    //根据类型发送验证码
                    //邮箱账户
                    if (type == AppConstants.ACCOUNT_TYPE_EMAIL)
                        addSubscription(apiService.getEmailCheckCode(account, String.valueOf(AppConstants.USER_TYPE_MUSICIAN)),
                                new SubscriberCallBack<ResultResponse>(mView.get()) {
                                    @Override
                                    protected void onSuccess(ResultResponse response) {
                                        UIUtils.showBaseToast("Code send to your E-mail successfully.");
                                        mView.nextStep();
                                    }

                                    @Override
                                    protected void onFailure(ResultResponse response) {

                                    }

                                    @Override
                                    protected boolean isShowProgress() {
                                        return true;
                                    }
                                });

                        //手机账户
                    else if (type == AppConstants.ACCOUNT_TYPE_PHONE) {
//                        sendPhoneCheckCode(account);
                        VerifyEvent.initSinch(mView.get(),account);
                    }
                }
            }

            @Override
            protected void onFailure(ResultResponse response) {
                mView.showCheckResult(false, type, response.errorMsg);
            }
        });

    }

    /**
     * 检验邮箱验证码
     *
     * @param code
     */
    public void checkEmailCode(String code, final String userAccount) {
        addSubscription(apiService.chekEmailCodeRegister(code, userAccount), new SubscriberCallBack<ResultResponse>(mView.get()) {
            @Override
            protected void onSuccess(ResultResponse response) {
                EventBus.getDefault().post(AppConstants.Code_Verify_Correct);
            }

            @Override
            protected void onFailure(ResultResponse response) {
                EventBus.getDefault().post(AppConstants.Code_Verify_Invalid);
            }
        });
    }

    //发送手机验证码
    private void sendPhoneCheckCode(String account) {
        ProgressHUD show = ProgressHUD.show(mView.get(), "Send code...", true, true, null);
        if (mVerification == null) {
            Config config = SinchVerification.config().applicationKey(AppConstants.SINCH_APPKEY).context(BaseApp.getContext()).build();
            mListener = new MyVerificationListener();
            String defaultRegion = PhoneNumberUtils.getDefaultCountryIso(BaseApp.getContext());
            KLog.d("地区", defaultRegion);
            String phoneNumberInE164 = PhoneNumberUtils.formatNumberToE164(account, defaultRegion);
            KLog.d("格式化的号码", phoneNumberInE164);
            mVerification = SinchVerification.createSmsVerification(config, phoneNumberInE164, mListener);
        }
        mVerification.initiate();
        show.dismiss();
    }

    /**
     * 验证手机验证码
     *
     * @param code
     */
    public void checkPhoneCode(Verification verification,String code) {
        verification.verify(code);
    }


    /**
     * 表演者注册
     *
     * @param account
     * @param pwd
     * @param phone
     * @param email
     */
    public void register(String account, String pwd, String phone, String email) {

        addSubscription(apiService.performerRegister(account, pwd, phone, email), new SubscriberCallBack<ResultResponse>(mView.get()) {
            @Override
            protected void onSuccess(ResultResponse response) {
                UIUtils.showBaseToast("Register successfully！");
                mView.registerSuccess();
            }

            @Override
            protected void onFailure(ResultResponse response) {
            }
        });
    }

    /**
     * 表演者注册
     *
     */
    public void registerUser(HashMap<String,String> map) {

        addSubscription(apiService.ordinaryFrontUserRegister(map), new SubscriberCallBack<ResultResponse>(mView.get()) {
            @Override
            protected void onSuccess(ResultResponse response) {
                UIUtils.showBaseToast("Register successfully！");
                mView.registerSuccess();
            }

            @Override
            protected void onFailure(ResultResponse response) {
            }
        });
    }

}
