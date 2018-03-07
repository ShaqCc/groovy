package com.bunny.groovy.ui.fragment.apply;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.MusicianScheduleAdapter;
import com.bunny.groovy.adapter.VenueScheduleAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.MusicianDetailModel;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.presenter.MusicianDetailPresenter;
import com.bunny.groovy.ui.fragment.releaseshow.InviteMusicianFragment;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IMusicianView;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/****************************************
 * 功能说明:  表演者详情页面，邀请
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class MusicianDetailFragment extends BaseFragment<MusicianDetailPresenter> implements IMusicianView {

    @Bind(R.id.user_header)
    CircleImageView mUserHeader;
    @Bind(R.id.user_score)
    TextView mUserScore;
    @Bind(R.id.user_name)
    TextView mUserName;
    @Bind(R.id.user_style)
    TextView mUserStyle;
    @Bind(R.id.user_phone)
    TextView mUserPhone;
    @Bind(R.id.user_desc)
    TextView mUserDesc;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @Bind(R.id.user_iv_fav)
    ImageView mIvFavouriteView;
    @Bind(R.id.user_iv_money)
    ImageView mMoneyView;

    private static String mPerformerId;


    private MusicianScheduleAdapter mAdapter;
    private MusicianDetailModel musicianDetailModel;

    @OnClick(R.id.user_invite)
    public void invite() {
        mPresenter.invite(musicianDetailModel);
    }

    @OnClick(R.id.user_phone)
    public void call() {
        Utils.CallPhone(mActivity, musicianDetailModel.telephone);
    }

    @OnClick(R.id.me_tv_facebook)
    public void facebook() {
        Utils.openFacebook(mActivity, musicianDetailModel.facebookAccount);
    }

    @OnClick(R.id.me_tv_twitter)
    public void twitter() {
        Utils.openTwitter(mActivity, musicianDetailModel.twitterAccount);
    }

    @OnClick(R.id.user_iv_fav)
    public void setFavourite() {
        if (isFavorite) {
            mPresenter.cancelCollectionPerformer(mPerformerId, AppCacheData.getPerformerUserModel().getUserID());
        } else {
            mPresenter.collectionPerformer(mPerformerId, AppCacheData.getPerformerUserModel().getUserID());
        }
    }

    public static String KEY_VENUE_ID = "key_venue_id";
    private boolean isFavorite = false;

    public static void launch(Activity from, String performerId) {
        mPerformerId = performerId;
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "MUSICIAN DETAILS");
        FragmentContainerActivity.launch(from, MusicianDetailFragment.class, bundle);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setView(MusicianDetailModel model) {
        musicianDetailModel = model;
        mUserName.setText(model.userName);
        if (model.starLevel.contains(".")) {
            String start = model.starLevel.substring(0, model.starLevel.lastIndexOf(".") + 2);
            mUserScore.setText(start);
        } else {
            mUserScore.setText(model.starLevel);
        }

        mUserPhone.setText(model.telephone);
        mUserStyle.setText(model.performTypeName);
        mUserDesc.setText(model.signature);
        if ("1".equals(model.isBeCollection)) {
            isFavorite = true;
            mIvFavouriteView.setImageResource(R.drawable.nav_collection_selected);
        } else {
            isFavorite = false;
            mIvFavouriteView.setImageResource(R.drawable.nav_collection);
        }

        Glide.with(mActivity).load(model.headImg).error(R.mipmap.venue_instead_pic).into(mUserHeader);
        //set list
        if (mAdapter == null) {
            mAdapter = new MusicianScheduleAdapter(model.evaluateList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(model.evaluateList);
        }
    }

    @Override
    public void cancelFavorite() {
        mIvFavouriteView.setImageResource(R.drawable.nav_collection);
        isFavorite = false;
    }

    @Override
    public void favorite() {
        mIvFavouriteView.setImageResource(R.drawable.nav_collection_selected);
        isFavorite = true;
    }

    @Override
    protected MusicianDetailPresenter createPresenter() {
        return new MusicianDetailPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_musician_detail_layout;
    }

    @Override
    protected void loadData() {
        mPresenter.getSingPerformerDetail(mPerformerId, AppCacheData.getPerformerUserModel().getUserID());
    }
}
