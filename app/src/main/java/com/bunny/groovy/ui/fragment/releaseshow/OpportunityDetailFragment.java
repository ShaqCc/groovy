package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.adapter.OpportunityAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.OpportunityModel;
import com.bunny.groovy.ui.fragment.apply.ApplyOppFragment;
import com.bunny.groovy.ui.fragment.apply.VenueDetailFragment;
import com.bunny.groovy.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  演出机会详情页
 *
 * Author: Created by bayin on 2017/12/26.
 ****************************************/

public class OpportunityDetailFragment extends BaseFragment {

    public static String KEY_OPPORTUNITY_BEAN = "key_opportunity_bean";
    private static OpportunityModel sParcelable;

    @Bind(R.id.opp_detail_tv_name)
    TextView mTvName;
    @Bind(R.id.opp_detail_tv_time)
    TextView mTvTime;
    @Bind(R.id.opp_detail_tv_desc)
    TextView mTvDesc;
    @Bind(R.id.opp_detail_tv_empty_info)
    TextView mTvEmpty;
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

    @OnClick(R.id.opp_detail_tv_apply)
    public void apply() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ApplyOppFragment.KEY_OPP_BEAN, sParcelable);
        ApplyOppFragment.launch(mActivity, bundle);
    }

    @OnClick(R.id.opp_detail_include_venue_card)
    public void venueDetail(){
        Bundle bundle = new Bundle();
        bundle.putString(VenueDetailFragment.KEY_VENUE_ID,sParcelable.getVenueID());
        VenueDetailFragment.launch(mActivity,bundle);
    }

    @OnClick({R.id.opp_detail_iv_phone,R.id.include_detail_tv_tel})
    public void call() {
        Utils.CallPhone(mActivity, sParcelable.getPhoneNumber());
    }
    @OnClick(R.id.opp_detail_iv_email)
    public void email() {
        Utils.sendEmail(mActivity, sParcelable.getVenueEmail());
    }

    @OnClick(R.id.facebook_page)
    public void facebook(){
        Utils.openFacebook(mActivity,sParcelable.getFacebookAccount());
    }

    @OnClick(R.id.twitter_page)
    public void twitter(){
        Utils.openTwitter(mActivity,sParcelable.getTwitterAccount());
    }

    public static void launch(Activity activity, Bundle bundle) {
        sParcelable = bundle.getParcelable(KEY_OPPORTUNITY_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "SHOW OPPORTUNITY");
        FragmentContainerActivity.launch(activity, OpportunityDetailFragment.class, bundle);
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_opportunity_detail_layout;
    }

    @Override
    protected void loadData() {
        if (sParcelable != null) {
            mTvTime.setText(sParcelable.getPerformDate() + " " + sParcelable.getPerformTime());
            mTvName.setText(sParcelable.getVenueName());
            mTvDesc.setText(sParcelable.getPerformDesc());
            mTvVenueName.setText(sParcelable.getVenueName());
            mTvScore.setText(Utils.getStar(sParcelable.getVenueScore()));
            mTvAddress.setText(sParcelable.getVenueAddress());
            mTvTel.setText(sParcelable.getPhoneNumber());
            mTvEmail.setText(sParcelable.getWebSiteAddress());
            Glide.with(mActivity).load(sParcelable.getHeadImg()).placeholder(R.drawable.venue_instead_pic).error(R.drawable.venue_instead_pic)
                    .into(mHead);
            //设置演出厅提供服务
            String venueTypeName = sParcelable.getVenueTypeName();
            if (!TextUtils.isEmpty(venueTypeName)) {
                tv21Plus.setEnabled(!venueTypeName.contains("21"));
                tvFood.setEnabled(venueTypeName.contains("Food"));
                tvAlcohol.setEnabled(venueTypeName.contains("Alcohol"));
            } else {
                tv21Plus.setEnabled(true);
                tvFood.setEnabled(false);
                tvAlcohol.setEnabled(false);
            }
            tv21Plus.setEnabled(Utils.is21Enabled(sParcelable.getPerformDesc()));
            List<OpportunityModel.PerformerOpportunityBean> OpportunityList = sParcelable.getPerformerOpportunity();
            if (OpportunityList != null && OpportunityList.size() > 0) {
                //set adapter
                mTvEmpty.setVisibility(View.GONE);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(new OpportunityAdapter(OpportunityList));
            } else {
                mTvEmpty.setVisibility(View.VISIBLE);
            }
        }
    }
}
