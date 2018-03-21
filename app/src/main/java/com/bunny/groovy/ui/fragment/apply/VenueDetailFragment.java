package com.bunny.groovy.ui.fragment.apply;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.VenueScheduleAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.presenter.VenueDetailPresenter;
import com.bunny.groovy.ui.fragment.notify.ReportFragment;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IVenueView;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  音乐厅详情页面，申请演出
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class VenueDetailFragment extends BaseFragment<VenueDetailPresenter> implements IVenueView {

    @Bind(R.id.venue_detail_iv_fav)
    ImageView mIvFavourite;
    @Bind(R.id.include_detail_tv_venueName)
    TextView mTvVenueName;
    @Bind(R.id.include_detail_tv_venueStar)
    TextView mTvScore;
    @Bind(R.id.include_detail_tv_venueAddress)
    TextView mTvAddress;
    @Bind(R.id.include_detail_tv_tel)
    TextView mTvTel;
    @Bind(R.id.include_detail_tv_email)
    TextView mTvEmail;
    @Bind(R.id.include_detail_iv_head)
    ImageView mHead;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @Bind(R.id.include_detail_tv_21plus)
    TextView tv21Plus;
    @Bind(R.id.include_detail_tv_Alcohol)
    TextView tvAlcohol;
    @Bind(R.id.include_detail_tv_food)
    TextView tvFood;
    @Bind(R.id.include_detail_tv_Cover_Charge)
    TextView tvCoverCharge;


    private VenueScheduleAdapter mAdapter;
    private VenueModel venueModel;

    @OnClick(R.id.venue_detail_tv_apply)
    public void apply() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ApplyVenueFragment.KEY_VENUE_BEAN, venueModel);
        ApplyVenueFragment.launch(mActivity, bundle);
    }

    @OnClick(R.id.include_detail_tv_tel)
    public void call() {
        Utils.CallPhone(mActivity, venueModel.getPhoneNumber());
    }

    @OnClick(R.id.facebook_page)
    public void facebook() {
        Utils.openFacebook(mActivity, venueModel.getFacebookAccount());
    }

    @OnClick(R.id.twitter_page)
    public void twitter() {
        Utils.openTwitter(mActivity, venueModel.getTwitterAccount());
    }

    @OnClick(R.id.venue_detail_iv_fav)
    public void setFavourite() {
        if (isFavorite) {
            mPresenter.cancleCollectionVenue(venueID);
        } else {
            mPresenter.collectionVenue(venueID);
        }
    }

    public static String KEY_VENUE_ID = "key_venue_id";
    private static String venueID;
    private boolean isFavorite = false;

    public static void launch(Activity from, Bundle bundle) {
        venueID = bundle.getString(KEY_VENUE_ID);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "VENUE DETAILS");
        FragmentContainerActivity.launch(from, VenueDetailFragment.class, bundle);
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
    public void setView(VenueModel model) {
        venueModel = model;
        mTvVenueName.setText(model.getVenueName());
        mTvScore.setText(Utils.getStar(model.getVenueScore()));
        mTvTel.setText(model.getPhoneNumber());
        mTvAddress.setText(model.getVenueAddress());
        mTvEmail.setText(model.getWebSiteAddress());
        if ("1".equals(model.getIsVenueCollection())) {
            isFavorite = true;
            mIvFavourite.setImageResource(R.drawable.nav_collection_selected);
        } else {
            isFavorite = false;
            mIvFavourite.setImageResource(R.drawable.nav_collection);
        }
        //设置演出厅提供服务
        String venueTypeName = model.getVenueTypeName();
        if (!TextUtils.isEmpty(venueTypeName)) {
            tv21Plus.setEnabled(venueTypeName.contains("21"));
            tvFood.setEnabled(venueTypeName.contains("Food"));
            tvAlcohol.setEnabled(venueTypeName.contains("Alcohol"));
        } else {
            tv21Plus.setEnabled(false);
            tvFood.setEnabled(false);
            tvAlcohol.setEnabled(false);
        }


        Glide.with(mActivity).load(model.getHeadImg()).error(R.drawable.venue_instead_pic).into(mHead);
        //set list
        if (mAdapter == null) {
            mAdapter = new VenueScheduleAdapter(model.getScheduleList());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(model.getScheduleList());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.venue_details_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.tipoff_venue){
            //举报演出厅
            ReportVenueFragment.launch(get(),venueID);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void cancleFavorite() {
        mIvFavourite.setImageResource(R.drawable.nav_collection);
        isFavorite = false;
    }

    @Override
    public void favorite() {
        mIvFavourite.setImageResource(R.drawable.nav_collection_selected);
        isFavorite = true;
    }

    @Override
    protected VenueDetailPresenter createPresenter() {
        return new VenueDetailPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_venue_detail_layout;
    }

    @Override
    protected void loadData() {
        mPresenter.getVenueDetail(venueID);
    }
}
