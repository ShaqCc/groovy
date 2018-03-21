package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.presenter.VenueRegisterPresenter;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISingUpView;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by mysty on 2018/2/26.
 */

public class VenueRegister2Activity extends BaseActivity<VenueRegisterPresenter> implements ISingUpView {

    public static String KEY_ACCOUNT = "key_account";
    private String mAccount;
    public static String KEY_TYPE = "key_type";
    private int mType;
    public static String KEY_PASSWORD = "key_password";
    private String mPassword;
    public static String KEY_PUBLIC_NAME = "key_public_name";
    private String mPublicName;
    public static String KEY_ADDRESS = "key_address";
    private String mAddress;
    public static String KEY_HEAD_URL = "key_head_url";
    private String mHeadUrl;
    public static String KEY_LONGITUDE = "key_longitude";
    private String mLongitude;
    public static String KEY_LATITUDE = "key_latitude";
    private String mLatitude;
    public static String KEY_PLACE_ID = "key_place_id";
    private String mPlaceId;
    @Bind(R.id.et_venue_code)
    XEditText mEditCode;
    @Bind(R.id.et_venue_booking_phone)
    XEditText mEditPhone;
    @Bind(R.id.et_venue_booking_email)
    XEditText mEditEmail;
    @Bind(R.id.et_venue_service)
    XEditText mVenueService;
    @Bind(R.id.et_venue_phone)
    XEditText mVenuePhone;
    @Bind(R.id.et_venue_website)
    XEditText mVenueWebsite;
    @Bind(R.id.et_venue_facebook)
    XEditText mVenueFacebook;
    @Bind(R.id.et_venue_twitter)
    XEditText mVenueTwitter;
    private PopupWindow popupWindow;

    private WeakReference<Activity> mWeakReference = new WeakReference<Activity>(this);

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        if (intent != null) {
            mAccount = intent.getStringExtra(KEY_ACCOUNT);
            mType = intent.getIntExtra(KEY_TYPE, 0);
            mPassword = intent.getStringExtra(KEY_PASSWORD);
            mPublicName = intent.getStringExtra(KEY_PUBLIC_NAME);
            mAddress = intent.getStringExtra(KEY_ADDRESS);
            mHeadUrl = intent.getStringExtra(KEY_HEAD_URL);
            mLongitude = intent.getStringExtra(KEY_LONGITUDE);
            mLatitude = intent.getStringExtra(KEY_LATITUDE);
            mPlaceId = intent.getStringExtra(KEY_PLACE_ID);
        } else finish();

        //set view
        if (AppConstants.ACCOUNT_TYPE_EMAIL == mType) {
            mEditEmail.setText(mAccount);
            mEditEmail.setFocusable(false);
            mEditEmail.setTextColor(Color.GRAY);
        } else if (AppConstants.ACCOUNT_TYPE_PHONE == mType) {
            mEditPhone.setText(mAccount);
            mEditPhone.setFocusable(false);
            mEditPhone.setTextColor(Color.GRAY);
        }
    }

    @OnClick(R.id.et_venue_service)
    void selectService() {
        UIUtils.hideSoftInput(mVenueService);
        showPopWindow(this, mVenueService);
    }

    @OnClick(R.id.bt_sign_up)
    void signUp() {
        //验证码为空
        if (TextUtils.isEmpty(mEditCode.getTrimmedString())) {
            UIUtils.showBaseToast("Code must not be null.");
            return;
        }
        if (TextUtils.isEmpty(mVenueService.getTrimmedString())) {
            UIUtils.showBaseToast("Venue service must not be null.");
            return;
        }
        if (TextUtils.isEmpty(mVenuePhone.getTrimmedString())) {
            UIUtils.showBaseToast("Booking telphone must not be null.");
            return;
        }
        if (TextUtils.isEmpty(mVenueWebsite.getTrimmedString())) {
            UIUtils.showBaseToast("Website telphone must not be null.");
            return;
        }
        if (TextUtils.isEmpty(mVenueFacebook.getTrimmedString())) {
            UIUtils.showBaseToast("Facebook must not be null.");
            return;
        }
        if (TextUtils.isEmpty(mVenueTwitter.getTrimmedString())) {
            UIUtils.showBaseToast("Twitter must not be null.");
            return;
        }
//        if (mType == AppConstants.ACCOUNT_TYPE_PHONE) {
//            if (TextUtils.isEmpty(mEditEmail.getTrimmedString())) {
//                //邮箱为空
//                UIUtils.showBaseToast("E-mail must not be null.");
//                return;
//            } else if (!PatternUtils.isValidEmail(mEditEmail.getTrimmedString())) {
//                //邮箱不合法
//                UIUtils.showBaseToast("E-mail invalid.");
//                return;
//            }
//            VerifyEvent.verifyCode(mEditCode.getTrimmedString());
//        } else if (mType == AppConstants.ACCOUNT_TYPE_EMAIL) {
//            if (TextUtils.isEmpty(mEditPhone.getTrimmedString()))
//            //手机号为空
//            {
//                UIUtils.showBaseToast("Phone must not be null.");
//                return;
//            } else if (!PatternUtils.isUSphonenumber(mEditPhone.getTrimmedString()) && !PatternUtils.isCNPhone(mEditPhone.getTrimmedString())) {
//                UIUtils.showBaseToast("Phone invalid.");
//                return;
//            }
        mPresenter.registerVenue(mPublicName, mPassword, mEditPhone.getTrimmedString(), mEditEmail.getTrimmedString(),
                mEditCode.getTrimmedString(), mVenueService.getTrimmedString(), mAddress, mVenuePhone.getTrimmedString(),
                mVenueWebsite.getTrimmedString(), mLongitude, mLatitude, mPlaceId, mVenueTwitter.getTrimmedString()
                , mVenueFacebook.getTrimmedString(), mHeadUrl);
//            mPresenter.checkEmailCode(mEditCode.getTrimmedString(), URLEncoder.encode(mAccount));
//        }
    }

    @Override
    protected VenueRegisterPresenter createPresenter() {
        return new VenueRegisterPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_venue_register_second;
    }

    @Override
    public void initListener() {
        super.initListener();
        mVenueService.setCursorVisible(false);
        mVenueService.setFocusable(false);
        mVenueService.setFocusableInTouchMode(false);
        registerEventBus(this);
    }


    @Override
    public Activity get() {
        return mWeakReference.get();
    }

    /**
     * 注册成功，返回登录页面
     */
    @Override
    public void registerSuccess() {
        setResult(AppConstants.ACTIVITY_FINISH);
        finish();
        //完善资料
        startActivity(new Intent(this, VenueFile1Activity.class));
    }

    @Override
    public void showCheckResult(boolean invalid, int accountType, String msg) {

    }

    @Override
    public void nextStep() {

    }


    /**
     * 验证码结果回调
     *
     * @param result 结果
     */
    @Subscribe
    public void onVerifyEvent(String result) {
        switch (result) {
            case AppConstants.Code_Verify_Correct:
                mPresenter.registerVenue(mAccount, mPassword, mEditPhone.getTrimmedString(), mEditEmail.getTrimmedString(),
                        mEditCode.getTrimmedString(), mVenueService.getTrimmedString(), mAddress, mVenuePhone.getTrimmedString(),
                        mVenueWebsite.getTrimmedString(), mLongitude, mLatitude, mPlaceId, mVenueTwitter.getTrimmedString()
                        , mVenueFacebook.getTrimmedString(), mHeadUrl);
                break;
            case AppConstants.Code_Verify_Invalid:
                UIUtils.showBaseToast("验证码不正确");
                break;
            case AppConstants.Code_Send_ServerError:
            default:
                UIUtils.showBaseToast("服务器出错");
                break;
        }
    }

    private void initPopWindow(Context context, String value) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.pop_venue_service_choose_layout, null, false);
            final CheckBox checkBox1 = inflate.findViewById(R.id.service_choose_1);
            final CheckBox checkBox2 = inflate.findViewById(R.id.service_choose_2);
            final CheckBox checkBox3 = inflate.findViewById(R.id.service_choose_3);
            if (!TextUtils.isEmpty(value)) {
                String data[] = value.split(",");
                for (String v : data) {
                    if (TextUtils.equals(checkBox1.getText().toString(), v)) {
                        checkBox1.setChecked(true);
                    } else if (TextUtils.equals(checkBox2.getText().toString(), v)) {
                        checkBox2.setChecked(true);
                    } else if (TextUtils.equals(checkBox3.getText().toString(), v)) {
                        checkBox3.setChecked(true);
                    }
                }
            }
            popupWindow.setContentView(inflate);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setWidth(mVenueService.getWidth());
            popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (checkBox1.isChecked()) {
                        stringBuilder.append(checkBox1.getText().toString().trim());
                    }
                    if (checkBox2.isChecked()) {
                        if (stringBuilder.length() > 0) stringBuilder.append(",");
                        stringBuilder.append(checkBox2.getText().toString().trim());
                    }
                    if (checkBox3.isChecked()) {
                        if (stringBuilder.length() > 0) stringBuilder.append(",");
                        stringBuilder.append(checkBox3.getText().toString().trim());
                    }
                    mVenueService.setText(stringBuilder.toString());
                    mVenueService.setCheckStatus(XEditText.CheckStatus.NONE);
                }
            });
        }
    }

    public void showPopWindow(Context context, View view) {
        UIUtils.hideSoftInput(mVenueService);
        initPopWindow(context, mVenueService.getTrimmedString());
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            mVenueService.setCheckStatus(XEditText.CheckStatus.NONE);
        } else {
            popupWindow.showAsDropDown(mVenueService, 0, 0);
            mVenueService.setCheckStatus(XEditText.CheckStatus.INVALID);
        }
    }
}
