package com.bunny.groovy.ui.setfile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.listener.PermissionListener;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.SetFilePresenter;
import com.bunny.groovy.ui.RoleChooseActivity;
import com.bunny.groovy.ui.login.LoginActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.ISetFileView;
import com.xw.repo.XEditText;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/****************************************
 * 功能说明:  完善资料第一步
 userName
 zipCode
 phoneNumber
 stageName
 webSiteAddress
 imgfile
 *
 * Author: Created by bayin on 2017/12/12.
 ****************************************/

public class SetFile1Activity extends BaseActivity<SetFilePresenter> implements ISetFileView {
    @Bind(R.id.perfect_info_et_fullname)
    XEditText etFullName;
    @Bind(R.id.perfect_info_et_artistname)
    XEditText etArtistName;
    @Bind(R.id.perfect_info_et_zipcode)
    XEditText etZipcode;
    @Bind(R.id.perfect_info_et_website)
    XEditText etWebsite;
    @Bind(R.id.perfect_info_iv_headpic)
    CircleImageView headView;

    @OnClick(R.id.zipcode_info)
    void showZipCodeInfo() {
        UIUtils.showBaseToast("Set the zip code for current location so that the performance hall can find you.");
    }

    @OnClick(R.id.perfect_info_iv_select_pic)
    void selectPic() {
        requestRuntimePermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                //选择图片
                choosePic(SetFile1Activity.this);
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                //拒绝
                UIUtils.showBaseToast("Select picture denied.");
            }
        });
    }

    @OnClick(R.id.iv_logout)
    void logout() {
        Utils.clearLoginData(get());
        //跳转到角色选择页面
        RoleChooseActivity.launch(this);
//        //退出setting页面
        finish();
//        //退出MainActivity
        EventBus.getDefault().post(AppConstants.EVENT_LOGIN_OUT);
    }

    @OnClick(R.id.perfect_info_tv_login)
    void login() {
        LoginActivity.launch(this, AppConstants.USER_TYPE_MUSICIAN);
    }

    @OnClick(R.id.perfect_info_tv_next)
    void next() {
        //拦截
        if (TextUtils.isEmpty(etFullName.getTrimmedString())) {
            UIUtils.showBaseToast("请输入名字");
            return;
        }
        if (TextUtils.isEmpty(etArtistName.getTrimmedString())) {
            UIUtils.showBaseToast("请输入昵称");
            return;
        }
        if (TextUtils.isEmpty(etZipcode.getTrimmedString())) {
            UIUtils.showBaseToast("请输入邮编");
            return;
        }
        if (TextUtils.isEmpty(etWebsite.getTrimmedString())) {
            UIUtils.showBaseToast("请输入网站");
            return;
        }
        //保存数据
        AppCacheData.getFileMap().put("userName", etFullName.getTrimmedString());
        AppCacheData.getFileMap().put("zipCode", etZipcode.getTrimmedString());
        AppCacheData.getFileMap().put("phoneNumber", AppCacheData.getPerformerUserModel().getTelephone());
        AppCacheData.getFileMap().put("stageName", etArtistName.getTrimmedString());
        AppCacheData.getFileMap().put("webSiteAddress", etWebsite.getTrimmedString());
        AppCacheData.getFileMap().put("imgfile", headImagePath);
//        AppCacheData.getFileMap().put("userID", AppCacheData.getPerformerUserModel().getUserID());

        mPresenter.searchLocation(etZipcode.getTrimmedString());
    }

    private String headImagePath = "";//头像文件路径


    @Override
    public Activity get() {
        return getCurrentActivity();
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {

    }

    @Override
    protected SetFilePresenter createPresenter() {
        return new SetFilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.first_perfect_file_layout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUESTCODE_SELECT_PIC && resultCode == RESULT_OK) {
            List<String> mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            assert mResults != null;
            headImagePath = mResults.get(0);
            Bitmap bitmap = BitmapFactory.decodeFile(headImagePath);
            headView.setImageBitmap(bitmap);
        } else if (requestCode == 2 && resultCode == AppConstants.ACTIVITY_FINISH) {
            setResult(AppConstants.ACTIVITY_FINISH);
            finish();
        }
    }
}
