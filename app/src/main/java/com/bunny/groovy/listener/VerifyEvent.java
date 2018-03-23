package com.bunny.groovy.listener;

import android.app.Activity;

import com.bunny.groovy.base.BaseApp;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.sinch.verification.Config;
import com.sinch.verification.PhoneNumberUtils;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.socks.library.KLog;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/19.
 ****************************************/

public class VerifyEvent {
    private static Verification mVerification;
    private static MyVerificationListener sListener;

    /**
     * 发送验证码
     *
     * @param activity
     * @param phone
     */
    public static void initSinch(Activity activity, String phone) {
        Config config = SinchVerification.config().applicationKey(AppConstants.SINCH_APPKEY).context(BaseApp.getContext()).build();
        sListener = new MyVerificationListener();
        String defaultRegion = PhoneNumberUtils.getDefaultCountryIso(BaseApp.getContext());
        KLog.d("地区", defaultRegion);
        String phoneNumberInE164 = PhoneNumberUtils.formatNumberToE164(phone, defaultRegion);
        KLog.d("格式化的号码", phoneNumberInE164);
        mVerification = SinchVerification.createSmsVerification(config, phoneNumberInE164, sListener);
        mVerification.initiate();
    }

    /**
     * 验证code
     *
     * @param code
     */
    public static void verifyCode(String code) {
        if (mVerification == null) {
            UIUtils.showBaseToast("Failed!Please send code again.");
        } else mVerification.verify(code);
    }
}
