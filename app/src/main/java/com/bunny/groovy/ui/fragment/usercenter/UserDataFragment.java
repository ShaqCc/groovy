package com.bunny.groovy.ui.fragment.usercenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.listener.PermissionListener;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.UserMePresenter;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IMeView;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.bunny.groovy.utils.AppConstants.REQUESTCODE_SELECT_PIC;

/**
 * 普通用户编辑设置页面
 * <p>
 * Created by Administrator on 2017/12/24.
 */

public class UserDataFragment extends BaseFragment<UserMePresenter> implements IMeView {

    @Bind(R.id.personal_headview)
    CircleImageView mHeadView;

    @Bind(R.id.user_name)
    EditText mEtName;

    private String headImagePath;

    @OnClick(R.id.personal_iv_select_pic)
    public void selectHeaderView() {
        mActivity.requestRuntimePermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                //选择图片
                Intent intent = new Intent(mActivity, ImagesSelectorActivity.class);
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                startActivityForResult(intent, REQUESTCODE_SELECT_PIC);
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                //拒绝
                UIUtils.showBaseToast("Select picture denied.");
            }
        });
    }

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "PERSONAL DATA");
        FragmentContainerActivity.launch(from, UserDataFragment.class, bundle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.personal_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.personal_item_save) {
            //判断空，拦截
            if (TextUtils.isEmpty(mEtName.getText().toString())) {
                UIUtils.showBaseToast("Please input name.");
            } else {
                HashMap<String, String> ma = new HashMap<>();
                ma.put("userName", mEtName.getText().toString());
                if (!TextUtils.isEmpty(headImagePath))
                    ma.put("imgfile", headImagePath);
                mPresenter.updateUserInfo(ma);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setUserView(PerformerUserModel model) {
        mEtName.setText(model.getUserName());
        Glide.with(getActivity()).load(model.getHeadImg())
                .placeholder(R.drawable.head).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mHeadView.setImageDrawable(resource.getCurrent());
            }
        });
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {

    }

    @Override
    protected UserMePresenter createPresenter() {
        return new UserMePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_user_data_layout;
    }

    @Override
    protected void loadData() {
        mPresenter.requestUserData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUESTCODE_SELECT_PIC && resultCode == RESULT_OK) {
            List<String> mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            assert mResults != null;
            headImagePath = mResults.get(0);
            Bitmap bitmap = BitmapFactory.decodeFile(headImagePath);
            mHeadView.setImageBitmap(bitmap);
        }
    }
}
