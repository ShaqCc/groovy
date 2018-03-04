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
        if (mAdapter == null) {
            mAdapter = new VenueNotify1ListAdapter(o);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(o);
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
