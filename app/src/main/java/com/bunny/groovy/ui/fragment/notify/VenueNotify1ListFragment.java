package com.bunny.groovy.ui.fragment.notify;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.VenueNotify1ListAdapter;
import com.bunny.groovy.base.BaseListFragment;
import com.bunny.groovy.divider.HLineDecoration;
import com.bunny.groovy.model.VenueOpportunityModel;
import com.bunny.groovy.presenter.ListPresenter;
import com.bunny.groovy.view.IListPageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueNotify1ListFragment extends BaseListFragment<ListPresenter, VenueNotify1ListAdapter> implements IListPageView<List<VenueOpportunityModel>> {

    private int mTYPE;


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mRecyclerView.addItemDecoration(new HLineDecoration(mActivity, LinearLayoutManager.VERTICAL, R.drawable.shape_item_divider_line));
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getVenueOpportunityList();
    }

    @Override
    protected ListPresenter createPresenter() {
        return new ListPresenter(this);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setView(List<VenueOpportunityModel> o) {
        setNormalView();
        List<VenueOpportunityModel> models = new ArrayList<>();
        for (int i = 0; i < o.size(); i++){
            if(o.get(i).getApplyList().size() > 4){
                o.get(i).setApplyList(o.get(i).getApplyList().subList(0, 4));
            }
            models.add(o.get(i));
        }
        if (mAdapter == null) {
            //去除ApplyList为空情况(最多为4个)
            mAdapter = new VenueNotify1ListAdapter(models);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(models);
        }
    }

    @Override
    public void setNormalView() {
        setPageState(PageState.NORMAL);
    }

    @Override
    public void setNodata() {
        setPageState(PageState.NODATA);
    }

    @Override
    public void setError() {
        setPageState(PageState.ERROR);
    }
}
