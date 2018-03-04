package com.bunny.groovy.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.listener.PermissionListener;
import com.bunny.groovy.presenter.SingUpPresenter;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISingUpView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.xw.repo.XEditText;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mysty on 2018/2/26.
 */

public class VenueRegister1Activity extends BaseActivity<SingUpPresenter> implements ISingUpView {

    private final static int PLACE_PICKER_REQUEST = 1;
    private String mLongitude, mLatitude, mPlaceId;

    @Bind(R.id.iv_venue_head_pic)
    CircleImageView mHeadPic;
    @Bind(R.id.et_venue_phone_or_email)
    XEditText mPhoneEmail;
    @Bind(R.id.et_venue_password)
    XEditText mPassword;
    @Bind(R.id.et_venue_password_again)
    XEditText mPasswordAgain;
    @Bind(R.id.et_venue_public_name)
    XEditText mPublicName;
    @Bind(R.id.et_venue_address)
    XEditText mAddress;
    @Bind(R.id.tv_venue_protocol)
    TextView mAddressText;

    private String headImagePath = "";//头像文件路径


    private int mAccountType = 0;//账号类型
    private WeakReference<Activity> mWeakReference = new WeakReference<Activity>(this);


    public static void start(Context outerContext, int userType) {
        Intent intent = new Intent(outerContext, VenueRegister1Activity.class);
        intent.putExtra("userType", userType);
        outerContext.startActivity(intent);
    }

    @Override
    protected SingUpPresenter createPresenter() {
        return new SingUpPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_venue_register_first;
    }

    @Override
    public void initListener() {
        super.initListener();
        //账户输入框的监听
        mPhoneEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String account = mPhoneEmail.getText().toString();
                if (!hasFocus && !TextUtils.isEmpty(account)) {
                    //检查是否合法
                    mPresenter.checkAccount(account, false);
                }
            }
        });
        //event bus
        registerEventBus(this);
    }


    @Override
    public Activity get() {
        return mWeakReference.get();
    }

    @Override
    public void showCheckResult(boolean invalid, int AccountType, String msg) {
        mAccountType = AccountType;
        if (invalid) mPhoneEmail.setCheckStatus(XEditText.CheckStatus.CORRECT);
        else {
            mPhoneEmail.setCheckStatus(XEditText.CheckStatus.INVALID);
            UIUtils.showBaseToast(msg);
        }
    }

    @Override
    public void registerSuccess() {

    }

    @Override
    public void nextStep() {
        Intent intent = new Intent();
        intent.putExtra(VenueRegister2Activity.KEY_ACCOUNT, mPhoneEmail.getTrimmedString());
        intent.putExtra(VenueRegister2Activity.KEY_TYPE, mAccountType);
        intent.putExtra(VenueRegister2Activity.KEY_PASSWORD, mPassword.getTrimmedString());
        intent.putExtra(VenueRegister2Activity.KEY_PUBLIC_NAME, mPublicName.getTrimmedString());
        intent.putExtra(VenueRegister2Activity.KEY_ADDRESS, mAddress.getText().toString().trim());
        intent.putExtra(VenueRegister2Activity.KEY_HEAD_URL, headImagePath);
        intent.putExtra(VenueRegister2Activity.KEY_LONGITUDE, mLongitude);
        intent.putExtra(VenueRegister2Activity.KEY_LATITUDE, mLatitude);
        intent.putExtra(VenueRegister2Activity.KEY_PLACE_ID, mPlaceId);
        intent.setClass(this, VenueRegister2Activity.class);
        startActivityForResult(intent, 2);
    }

    //下一步
    @OnClick(R.id.tv_venue_next)
    void next() {
        nextStep();
        String pwd = mPassword.getTrimmedString();
        String pwdAgain = mPasswordAgain.getTrimmedString();
        String publicName = mPublicName.getTrimmedString();
        String address = mAddress.getText().toString().trim();
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
            UIUtils.showBaseToast("请输入密码");
        } else if (pwd.length() < 8 || pwdAgain.length() < 8) {
            UIUtils.showBaseToast("密码至少8位");
        } else if (!pwd.equals(pwdAgain)) {
            UIUtils.showBaseToast("密码输入不一致");
        } else if (TextUtils.isEmpty(publicName)) {
            UIUtils.showBaseToast("名称不能为空");
        } else if (TextUtils.isEmpty(address)) {
            UIUtils.showBaseToast("地址不能为空");
        } else {
            //检查账户
            String account = mPhoneEmail.getTrimmedString();
            if (TextUtils.isEmpty(account)) {
                UIUtils.showBaseToast("Please input account!");
                return;
            }
            mPresenter.checkAccount(account, true);
        }
    }

    @OnClick(R.id.et_venue_address)
    void chooseAddress() {
        UIUtils.hideSoftInput(mAddress);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            UIUtils.showBaseToast("GooglePlay services not available!");

        }
    }
    //登陆
    @OnClick(R.id.tv_signup_login)
    void login() {
        finish();
    }

    @OnClick(R.id.iv_venue_select_pic)
    void selectPic() {
        requestRuntimePermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                //选择图片
                choosePic(VenueRegister1Activity.this);
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                //拒绝
                UIUtils.showBaseToast("Select picture denied.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUESTCODE_SELECT_PIC && resultCode == RESULT_OK) {
            List<String> mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            assert mResults != null;
            headImagePath = mResults.get(0);
            Bitmap bitmap = BitmapFactory.decodeFile(headImagePath);
            mHeadPic.setImageBitmap(bitmap);
        } else if (requestCode == 2 && resultCode == AppConstants.ACTIVITY_FINISH) {
            setResult(AppConstants.ACTIVITY_FINISH);
            finish();
        }else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                mAddress.setText(place.getAddress());
                mLongitude = String.valueOf(place.getLatLng().longitude);
                mLatitude = String.valueOf(place.getLatLng().latitude);
                mPlaceId = String.valueOf(place.getId());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Subscribe
    public void onRecevieSms(String code) {
        switch (code) {
            case AppConstants.Code_Send_Success://发送成功，跳转到下一个页面
                nextStep();
                break;
            case AppConstants.Code_Send_InvalidPhone://发送失败
                UIUtils.showBaseToast("手机号码不正确");
                break;
            default:
            case "5000"://网络错误
                UIUtils.showBaseToast("服务器出错");
                break;
        }
    }
}
