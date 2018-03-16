package com.bunny.groovy.ui.fragment.usercenter;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.StyleAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.listener.PermissionListener;
import com.bunny.groovy.model.MusicBean;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.MePresenter;
import com.bunny.groovy.service.MusicService;
import com.bunny.groovy.ui.setfile.MusicListActivity;
import com.bunny.groovy.utils.AppCacheData;
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
 * 个人信息设置页面
 * <p>
 * Created by Administrator on 2017/12/24.
 */

public class PersonalDataFragment extends BaseFragment<MePresenter> implements IMeView {

    @Bind(R.id.personal_headview)
    CircleImageView mHeadView;

    @Bind(R.id.personal_tv_name)
    TextView mTvName;

    @Bind(R.id.personal_et_artist_name)
    EditText mEtArtistName;

    @Bind(R.id.personal_et_website)
    EditText mEtWebsite;

    @Bind(R.id.personal_et_facebook)
    EditText mEtFaceBook;

    @Bind(R.id.personal_et_twitter)
    EditText mEtTwitter;

    @Bind(R.id.personal_et_soundcloud)
    EditText mEtSoundClound;

    @Bind(R.id.personal_et_zipcode)
    EditText mEtZipcode;

    @Bind(R.id.personal_et_bio)
    EditText mEtBio;

    @Bind(R.id.personal_iv_play)
    ImageView mIvPlay;

    @Bind(R.id.personal_et_style)
    EditText mEtStyle;
    private String headImagePath;

    @OnClick(R.id.personal_et_style)
    public void selectStyle() {
        mPresenter.requestStyle();
    }

    @OnClick(R.id.personal_tv_replace)
    public void replaceMusic() {
        startActivityForResult(new Intent(getActivity(), MusicListActivity.class), AppConstants.REQUESTCODE_SELECT_MUSIC);
    }

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

    @OnClick(R.id.personal_iv_play)
    public void playMusic() {
        if (TextUtils.isEmpty(AppCacheData.getPerformerUserModel().getMusicFile()) ||
                mMusic_file == null || TextUtils.isEmpty(mMusic_file.getMusicPath())) {
            UIUtils.showBaseToast("Choose music.");
            return;
        }
        handleMusic();
    }

    /**
     * 控制音乐播放
     */
    private void handleMusic() {
        if (callBack != null) {
            boolean isPlay = callBack.isPlayerMusic();
            if (isPlay) {
                mIvPlay.setImageResource(R.drawable.login_stop);
            } else {
                mIvPlay.setImageResource(R.drawable.login_play);
            }
        }
    }

    public static void launch(Activity from) {
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "PERSONAL DATA");
        FragmentContainerActivity.launch(from, PersonalDataFragment.class, bundle);
    }

    private PopupWindow popupWindow;
    private StyleAdapter adapter;

    private void initPopWindow(Context context, List<StyleModel> dataList, String selectStyle) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.pop_performer_style_layout, null, false);
            ListView listView = inflate.findViewById(R.id.style_listview);
            adapter = new StyleAdapter(dataList);
            listView.setAdapter(adapter);
            popupWindow.setContentView(inflate);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setWidth(UIUtils.getScreenWidth() - UIUtils.dip2Px(130));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mEtStyle.setText(adapter.getSelectStyle());
                }
            });
        } else {
            adapter.refresh(dataList, selectStyle);
        }
    }

    public void showPopWindow(Context context, List<StyleModel> dataList) {
        UIUtils.hideSoftInput(mEtStyle);
        initPopWindow(context, dataList, mEtStyle.getText().toString());
        if (popupWindow.isShowing()) popupWindow.dismiss();
        else popupWindow.showAsDropDown(mEtStyle, 0, 0, Gravity.CENTER);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.personal_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.personal_item_save) {
            //判断空，拦截
            //1.昵称
            if (TextUtils.isEmpty(mEtArtistName.getText().toString())) {
                UIUtils.showBaseToast("Please input ArtistName.");
                return super.onOptionsItemSelected(item);
            }
            //2.zipcode
            if (TextUtils.isEmpty(mEtZipcode.getText().toString())) {
                UIUtils.showBaseToast("Please input ZipCode.");
                return super.onOptionsItemSelected(item);
            }

            HashMap<String, String> ma = new HashMap<>();
            ma.put("performTypeName", mEtStyle.getText().toString());
            ma.put("signature", mEtBio.getText().toString());
            ma.put("zipCode", mEtZipcode.getText().toString());
            ma.put("stageName", mEtArtistName.getText().toString());
            ma.put("webSiteAddress", mEtWebsite.getText().toString());
            ma.put("twitterAccount", mEtTwitter.getText().toString());
            ma.put("facebookAccount", mEtFaceBook.getText().toString());
            ma.put("soundcloudAccount", mEtSoundClound.getText().toString());
            //无法维护的参数
            PerformerUserModel model = AppCacheData.getPerformerUserModel();
            ma.put("userName", model.getUserName());
            ma.put("phoneNumber", model.getTelephone());

            if (!TextUtils.isEmpty(headImagePath))
                ma.put("imgfile", headImagePath);
            if (mMusic_file != null)
                ma.put("music", mMusic_file.getMusicPath());
            //3.检查zipcode是否正确
            mPresenter.searchLocation(mEtZipcode.getText().toString(), ma);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setUserView(PerformerUserModel model) {
        mTvName.setText(model.getUserName());
        Glide.with(getActivity()).load(model.getHeadImg())
                .placeholder(R.drawable.head).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mHeadView.setImageDrawable(resource.getCurrent());
            }
        });
        mEtArtistName.setText(model.getStageName());
        mEtZipcode.setText(model.getZipCode());
        mEtWebsite.setText(model.getWebSiteAddress());
        mEtStyle.setText(model.getPerformTypeName());
        mEtBio.setText(model.getSignature());
        mEtFaceBook.setText(model.getFacebookAccount());
        mEtTwitter.setText(model.getTwitterAccount());
        mEtSoundClound.setText(model.getSoundcloudAccount());
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
        showPopWindow(getActivity(), modelList);
    }

    @Override
    protected MePresenter createPresenter() {
        return new MePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_personal_data_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mEtStyle.setFocusable(false);
    }

    @Override
    protected void loadData() {
        mPresenter.requestUserData();
    }

    private MusicBean mMusic_file;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUESTCODE_SELECT_MUSIC && resultCode == RESULT_OK) {
            if (data != null) {
                mMusic_file = data.getParcelableExtra("music_file");
                initMusicService();
            }
        } else if (requestCode == AppConstants.REQUESTCODE_SELECT_PIC && resultCode == RESULT_OK) {
            List<String> mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            assert mResults != null;
            headImagePath = mResults.get(0);
            Bitmap bitmap = BitmapFactory.decodeFile(headImagePath);
            mHeadView.setImageBitmap(bitmap);
        }
    }

    private MusicService.CallBack callBack;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            callBack = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            callBack = null;
        }
    };

    private void initMusicService() {
        /** 构造启动音乐播放服务的Intent，设置音乐资源 */
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.putExtra("music_path", mMusic_file.getMusicPath());
        getActivity().startService(intent);
        getActivity().bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callBack != null && callBack.isPlaying()) {
            callBack.isPlayerMusic();
            mIvPlay.setBackgroundResource(R.drawable.login_play);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callBack != null && callBack.isPlaying()) {
            callBack.isPlayerMusic();
            mIvPlay.setBackgroundResource(R.drawable.login_play);
        }
    }
}
