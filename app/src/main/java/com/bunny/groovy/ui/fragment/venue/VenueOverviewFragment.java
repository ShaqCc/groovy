package com.bunny.groovy.ui.fragment.venue;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.presenter.VenueOverviewPresenter;
import com.bunny.groovy.ui.fragment.apply.EditPerformFragment;
import com.bunny.groovy.ui.fragment.notify.NotificationFragment;
import com.bunny.groovy.ui.fragment.releaseshow.DiscoverMusicianFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ReleaseShowFragment;
import com.bunny.groovy.ui.fragment.releaseshow.ReleaseShowOpportunityFragment;
import com.bunny.groovy.ui.fragment.releaseshow.VenueShowDetailFragment;
import com.bunny.groovy.ui.fragment.spotlight.SpotlightFragment;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IVenueOverView;
import com.bunny.groovy.weidget.HeightLightTextView;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/****************************************
 * 功能说明: 演播厅主页
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class VenueOverviewFragment extends BaseFragment<VenueOverviewPresenter> implements IVenueOverView {
    @Bind(R.id.nextshow_layout)
    View nextShowLayout;
    @Bind(R.id.nextshow_iv_head)
    CircleImageView ivHead;
    @Bind(R.id.nextshow_tv_performerName)
    HeightLightTextView tvName;
    @Bind(R.id.nextshow_tv_performerStar)
    TextView tvStar;
    @Bind(R.id.nextshow_tv_address)
    TextView tvPerformType;
    @Bind(R.id.nextshow_tv_time)
    TextView tvTime;
    @Bind(R.id.nextshow_iv_edit)
    ImageView ivEdit;
    private VenueShowModel model;

    @OnClick(R.id.nextshow_layout)
    public void showDetail() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(VenueShowDetailFragment.KEY_SHOW_BEAN, model);
        VenueShowDetailFragment.launch(mActivity, bundle);
    }

    @OnClick(R.id.tv_release_show)
    void releaseShow() {
        ReleaseShowFragment.launch(getActivity());
    }

    @OnClick(R.id.tv_notifications)
    public void notifications() {
        NotificationFragment.launch(mActivity);
    }

    @OnClick(R.id.tv_discover_musician)
    void discoverMusician() {
        DiscoverMusicianFragment.launch(getActivity());
    }

    @OnClick(R.id.tv_release_opportunity)
    void releaseOpportunity() {
        ReleaseShowOpportunityFragment.launch(getActivity());
    }

    @OnClick(R.id.nextshow_iv_edit)
    void showPopupWindow() {
        //获取view位置
        final PopupWindow popupWindow = new PopupWindow(mActivity);
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.pop_edit, null);
        popupWindow.setContentView(inflate);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        inflate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });


        inflate.findViewById(R.id.edit_spotlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //推广
                if (TextUtils.equals(model.getIsHaveCharges(), "1")) {//已收费
                    mPresenter.spotlightPerform(model.getPerformID(), model.getVenueID());
                } else {
                    SpotlightFragment.launch(mActivity);
                }
                popupWindow.dismiss();
            }
        });

        inflate.findViewById(R.id.eidt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //编辑
                Bundle bundle = new Bundle();
                bundle.putParcelable(EditPerformFragment.KEY_VENUE_SHOW, model);
                EditPerformFragment.launch(mActivity, bundle);
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(ivEdit, 0, -ivEdit.getHeight() * 5);
    }

    @Override
    protected VenueOverviewPresenter createPresenter() {
        return new VenueOverviewPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_venue_overview_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void loadData() {
        //请求用户数据
        mPresenter.requestUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        //请求最近一场演出
        mPresenter.requestNextShow(AppCacheData.getPerformerUserModel().getUserID());
    }

    @Override
    public void refreshUI() {
        mPresenter.requestUserData();
        mPresenter.requestNextShow(AppCacheData.getPerformerUserModel().getUserID());
    }


    @Override
    public void initNextView(VenueShowModel showModel) {
        ivEdit.setVisibility(View.VISIBLE);
        model = showModel;
        nextShowLayout.setVisibility(View.VISIBLE);
        Glide.with(this).load(showModel.getPerformerImg())
                .placeholder(R.drawable.venue_instead_pic).error(R.drawable.venue_instead_pic).into(ivHead);
        tvName.setText(showModel.getVenueName());
        tvStar.setText(Utils.getStar(showModel.getVenueScore()));
        tvPerformType.setText(showModel.getPerformType());
        tvTime.setText(showModel.getPerformDate() + " " + showModel.getPerformTime());
    }

    @Override
    public void showEmptyNextShow() {
        nextShowLayout.setVisibility(View.GONE);
    }

    @Override
    public FragmentActivity get() {
        return getActivity();
    }

    @Override
    public void setView(PerformerUserModel userModel) {
    }
}
