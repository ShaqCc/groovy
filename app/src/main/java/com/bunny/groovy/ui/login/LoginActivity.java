package com.bunny.groovy.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.bunny.groovy.BuildConfig;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.manager.LoginBlock;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.presenter.LoginPresenter;
import com.bunny.groovy.ui.setfile.SetFile1Activity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.PatternUtils;
import com.bunny.groovy.utils.SharedPreferencesUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ILoginView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.socks.library.KLog;
import com.xw.repo.XEditText;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 表演者登陆页面
 * <p>
 * Created by Administrator on 2017/12/5.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {
    private static final int RC_SIGN_IN = 123;
    private int mUserType;
    @Bind(R.id.login_et_account)
    XEditText etPhoneOrEmail;
    @Bind(R.id.login_et_password)
    XEditText etPassword;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

    @OnClick(R.id.actionbar_iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_sign_up)
    void signUp() {
        switch (mUserType) {
            case AppConstants.USER_TYPE_MUSICIAN:
                startActivityForResult(new Intent(this, SignUpActivity.class), 2);
                break;
            case AppConstants.USER_TYPE_VENUE:
                startActivityForResult(new Intent(this, VenueRegister1Activity.class), 2);
                break;
            case AppConstants.USER_TYPE_NORMAL:
                startActivityForResult(new Intent(this, UserRegister1Activity.class), 2);
                break;
        }
    }

    @OnClick(R.id.tv_musician_login)
    void login() {
        //拦截非法输入
        if (TextUtils.isEmpty(etPhoneOrEmail.getTrimmedString())) {
            UIUtils.showBaseToast(getString(R.string.account_not_be_null));
            return;
        }
        if (!(PatternUtils.isUSphonenumber(etPhoneOrEmail.getTrimmedString())
                || PatternUtils.isCNPhone(etPhoneOrEmail.getTrimmedString())
                || PatternUtils.isValidEmail(etPhoneOrEmail.getTrimmedString()))) {
            //不是email也不是手机
            UIUtils.showBaseToast(getString(R.string.invalid_account));
            return;
        }
        if (TextUtils.isEmpty(etPassword.getTrimmedString())) {
            UIUtils.showBaseToast(getString(R.string.account_not_be_null));
            return;
        } else if (etPassword.getTrimmedString().length() < 8) {
            UIUtils.showBaseToast(getString(R.string.invalid_password));
            return;
        }

        //通过验证,登录
        mPresenter.login(etPhoneOrEmail.getTrimmedString(), etPassword.getTrimmedString(), mUserType);
    }

    @OnClick(R.id.tv_forget_password)
    void forgetPassword() {
        Intent intent = new Intent();
        intent.setClass(this, ForgetActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_login_google)
    public void googleLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.iv_login_facebook)
    public void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    public static void launch(Context activity, int type) {
        activity.startActivity(new Intent(activity, LoginActivity.class).putExtra("type", type));
    }

    @Override
    public void initData() {
        super.initData();
        int type = getIntent().getIntExtra("switch_type", -1);
        if (type > -1) {
            String userID = AppCacheData.getPerformerUserModel().getUserID();
            String account = (String) SharedPreferencesUtils.getUserParam(this, AppConstants.KEY_ACCOUNT + userID, "");
            String password = (String) SharedPreferencesUtils.getUserParam(this, AppConstants.KEY_PASSWORD + userID, "");
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                mUserType = type;
                etPhoneOrEmail.setText(account);
                etPassword.setText(password);
                login();
                return;
            }
        } else {
            mUserType = getIntent().getIntExtra("type", AppConstants.USER_TYPE_NORMAL);
        }
        //just for test
        if (BuildConfig.DEBUG) {
            switch (mUserType) {
                case AppConstants.USER_TYPE_NORMAL:
                    etPhoneOrEmail.setText("18601794067");
                    break;
                case AppConstants.USER_TYPE_MUSICIAN:
                    etPhoneOrEmail.setText("13564521320");
                    break;
                case AppConstants.USER_TYPE_VENUE:
                    etPhoneOrEmail.setText("13761434342");
//                    etPhoneOrEmail.setText("15021370938");
//                    etPhoneOrEmail.setText("13476027261");
            }
            etPassword.setText("123456789");
        }
        try {
            String account = (String) SharedPreferencesUtils.getAppParam(this
                    , AppConstants.KEY_HISTORY_ACCOUNT_BY_TYPE + mUserType, "");
            if (!TextUtils.isEmpty(account)) etPhoneOrEmail.setText(account);
        } catch (Exception e) {
        }


    }

    @Override
    public void initListener() {
        super.initListener();
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        KLog.a("facebook success :" + loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        exception.printStackTrace();
                        KLog.a("facebook onError :" + exception.toString());
                    }
                });
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login_layout;
    }

    @Override
    public void loginSuccess(PerformerUserModel userModel) {

    }

    @Override
    public Activity get() {
        return getCurrentActivity();
    }


    @Override
    public void launchMainPage(int type) {
        LoginBlock.getInstance().handleCheckSuccess(String.valueOf(type));
        finish();
    }

    /**
     * 跳转到设置资料页面
     */
    @Override
    public void launchToSetFile() {
        switch (mUserType) {
            case AppConstants.USER_TYPE_MUSICIAN:
                startActivityForResult(new Intent(this, SetFile1Activity.class), AppConstants.REQUESTCODE_SETFILE);
                break;
            case AppConstants.USER_TYPE_VENUE:
                startActivityForResult(new Intent(this, VenueFile1Activity.class), AppConstants.REQUESTCODE_SETFILE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //facebook登录回调
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        //设置资料结束，结束本页面,跳转至首页
        if (requestCode == AppConstants.REQUESTCODE_SETFILE && resultCode == AppConstants.ACTIVITY_FINISH) {
            launchMainPage(mUserType);
        } else if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * loginType
     * userName
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            KLog.d(account.toString());
            //检查是否绑定了账户
            mPresenter.checkHadBindUid("0", account.getId(), account.getDisplayName(), String.valueOf(mUserType));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            syso("异常：" + e.getMessage());
            KLog.w("signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void syso(String tag) {
        System.out.println(tag);
    }
}
