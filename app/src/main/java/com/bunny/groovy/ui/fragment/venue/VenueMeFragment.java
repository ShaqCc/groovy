package com.bunny.groovy.ui.fragment.venue;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.UserCenterAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BaseListFragment;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.VenueMePresenter;
import com.bunny.groovy.ui.fragment.spotlight.SpotlightFragment;
import com.bunny.groovy.ui.fragment.usercenter.FavoriteMusicianFragment;
import com.bunny.groovy.ui.fragment.usercenter.HistoryMusicianFragment;
import com.bunny.groovy.ui.fragment.usercenter.MeTimeFragment;
import com.bunny.groovy.ui.fragment.usercenter.SettingsFragment;
import com.bunny.groovy.ui.fragment.usercenter.VenueDataFragment;
import com.bunny.groovy.ui.fragment.wallet.WalletFragment;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IMeView;
import com.bunny.groovy.weidget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/****************************************
 * 功能说明: 演播厅个人中心
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class VenueMeFragment extends BaseFragment<VenueMePresenter> implements IMeView {

    @Bind(R.id.user_center_iv_header)
    CircleImageView ivHeader;

    @Bind(R.id.user_center_tv_username)
    TextView tvName;

    @Bind(R.id.user_center_tv_style)
    TextView tvStyle;

    @Bind(R.id.user_center_tv_score)
    TextView tvScore;

    @Bind(R.id.user_center_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.user_center_pagetabstrip)
    SlidingTabLayout pagerTabStrip;

    private List<BaseListFragment> mFragments = new ArrayList<>();

    private String[] titleArray = new String[]{"MY FAVORITE", "SHOW HISTORY"};


    private PerformerUserModel mModel;

    @OnClick(R.id.user_center_tv_settings)
    public void settings() {
        SettingsFragment.launch(getActivity());
    }

    @OnClick(R.id.me_tv_time)
    public void times() {
        MeTimeFragment.launch(getActivity());
    }

    @OnClick(R.id.me_tv_facebook)
    public void facebook() {
        if (!TextUtils.isEmpty(AppCacheData.getPerformerUserModel().getFacebookAccount()))
            Utils.openFacebook(mActivity, AppCacheData.getPerformerUserModel().getFacebookAccount());
    }

    @OnClick(R.id.me_tv_twitter)
    public void twitter() {
        if (!TextUtils.isEmpty(AppCacheData.getPerformerUserModel().getTwitterAccount()))
            Utils.openTwitter(mActivity, AppCacheData.getPerformerUserModel().getTwitterAccount());
    }

    @OnClick(R.id.me_tv_wallet)
    public void wallet() {
        WalletFragment.launch(mActivity);
    }

    @OnClick(R.id.me_tv_spotlight)
    public void spotlight() {
        SpotlightFragment.launch(mActivity);
    }

    @OnClick(R.id.user_center_iv_header)
    public void updateUserInfo() {
        VenueDataFragment.launch(mActivity);
    }


    @Override
    protected VenueMePresenter createPresenter() {
        return new VenueMePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_venue_center_layout;
    }

    @Override
    protected void loadData() {
        //获取用户数据
        mPresenter.requestUserData();
        //设置viewpager
        mFragments.add(new FavoriteMusicianFragment());
        mFragments.add(new HistoryMusicianFragment());
        mViewPager.setAdapter(new UserCenterAdapter(mFragments, titleArray, getActivity().getSupportFragmentManager()));
        pagerTabStrip.setDistributeEvenly(true);
        pagerTabStrip.setCustomTabView(R.layout.custorm_tab_layout, R.id.tv_tab);
        pagerTabStrip.setSelectedIndicatorColors(getActivity().getResources().getColor(R.color.white));
        pagerTabStrip.setTitleTextColor(getResources().getColor(R.color.white), getResources().getColor(R.color.white));
        pagerTabStrip.setTabStripWidth(UIUtils.getScreenWidth() / 2);
        pagerTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFragments.get(position).loadAgain();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerTabStrip.setViewPager(mViewPager);
    }

    @Override
    public Activity get() {
        return mActivity;
    }


    /**
     * 设置用户数据
     *
     * @param model
     */
    @Override
    public void setUserView(PerformerUserModel model) {
        mModel = model;
        tvName.setText(mModel.getUserName());
        tvStyle.setText(model.getVenueAddress());
        if (TextUtils.isEmpty(model.getVenueScore()))
            tvScore.setText("0.0");
        else {
            if (model.getVenueScore().contains(".")) {
                model.setVenueScore(model.getVenueScore().substring(0, model.getVenueScore().indexOf(".") + 2));
            }
            tvScore.setText(model.getVenueScore());
        }
        Glide.with(getActivity()).load(model.getHeadImg()).placeholder(R.drawable.icon_default_photo).into(ivHeader);
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
        //do nothing
    }

    @Override
    public void refreshUI() {
        //获取用户数据
        mPresenter.requestUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstEnter())
            mPresenter.requestUserData();
    }
}
