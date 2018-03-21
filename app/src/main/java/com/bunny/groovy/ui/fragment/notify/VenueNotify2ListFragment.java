package com.bunny.groovy.ui.fragment.notify;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.VenueNotify2ListAdapter;
import com.bunny.groovy.base.BaseListFragment;
import com.bunny.groovy.divider.HLineDecoration;
import com.bunny.groovy.model.VenueApplyModel;
import com.bunny.groovy.presenter.ListPresenter;
import com.bunny.groovy.view.IListPageView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class VenueNotify2ListFragment extends BaseListFragment<ListPresenter, VenueNotify2ListAdapter> implements IListPageView<List<VenueApplyModel>> {

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
        mPresenter.getVenueReleaseList();
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
    public void setView(List<VenueApplyModel> o) {
        setNormalView();
        if (mAdapter == null) {
            mAdapter = new VenueNotify2ListAdapter(o);
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
