package com.bunny.groovy.ui.fragment;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.ScheduleAdapter;
import com.bunny.groovy.adapter.ScheduleVenueAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.model.ScheduleModel;
import com.bunny.groovy.model.ShowModel;
import com.bunny.groovy.presenter.SchedulePresenter;
import com.bunny.groovy.ui.fragment.releaseshow.ReleaseShowFragment;
import com.bunny.groovy.utils.DateUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IScheduleView;
import com.bunny.groovy.weidget.MoveLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明: 表演者时间计划
 *
 * Author: Created by bayin on 2017/12/15.
 ****************************************/

public class ScheduleFragment extends BaseFragment<SchedulePresenter> implements IScheduleView,ScheduleVenueAdapter.OnSpotlightListener {

    private ScheduleModel mScheduleModel;
    private Calendar todayCal = Calendar.getInstance();
    private ArrayList<TextView> mTextList;
    private ScheduleAdapter mAdapter;

    @Bind(R.id.moveLayout)
    MoveLayout mMoveLayout;
    @Bind(R.id.recyclerview)
    RecyclerView mRcyvlerview;
    @Bind(R.id.schedule_tv_empty)
    View mEmptyView;

    @Bind(R.id.schedule_tv_time)
    TextView mTvTime;
    @Bind(R.id.schedule_tv_show_number_1)
    TextView mTvNumber_1;
    @Bind(R.id.schedule_tv_show_number_2)
    TextView mTvNumber_2;
    @Bind(R.id.schedule_tv_show_number_3)
    TextView mTvNumber_3;
    @Bind(R.id.schedule_tv_show_number_4)
    TextView mTvNumber_4;
    @Bind(R.id.schedule_tv_show_number_5)
    TextView mTvNumber_5;
    @Bind(R.id.schedule_tv_show_number_6)
    TextView mTvNumber_6;
    @Bind(R.id.schedule_tv_show_number_7)
    TextView mTvNumber_7;
    @Bind(R.id.schedule_tv_list_title)
    TextView mTvListTitle;

    @OnClick(R.id.schedule_bt_last_week)
    public void lastWeek() {
        List<Date> dates = DateUtils.lastWeek(monDate, sunDate);
        monDate = dates.get(0);
        sunDate = dates.get(1);
        mTvTime.setText(String.format(timeStr, generateTime(monDate, sunDate)));
        mPresenter.requestWeekList(DateUtils.getFormatTime(monDate), DateUtils.getFormatTime(sunDate));
    }

    @OnClick(R.id.schedule_bt_next_week)
    public void nextWeek() {
        List<Date> dates = DateUtils.nextWeek(monDate, sunDate);
        monDate = dates.get(0);
        sunDate = dates.get(1);
        mTvTime.setText(String.format(timeStr, generateTime(monDate, sunDate)));
        mPresenter.requestWeekList(DateUtils.getFormatTime(monDate), DateUtils.getFormatTime(sunDate));
    }

    @OnClick(R.id.schedule_rl_1)
    public void mondayList() {
        setWeekListData(1);
    }

    @OnClick(R.id.schedule_rl_2)
    public void tuesdayList() {
        setWeekListData(2);
    }

    @OnClick(R.id.schedule_rl_3)
    public void wensdayList() {
        setWeekListData(3);
    }

    @OnClick(R.id.schedule_rl_4)
    public void thursdayList() {
        setWeekListData(4);
    }

    @OnClick(R.id.schedule_rl_5)
    public void fridayList() {
        setWeekListData(5);
    }

    @OnClick(R.id.schedule_rl_6)
    public void statdayList() {
        setWeekListData(6);
    }

    @OnClick(R.id.schedule_rl_7)
    public void sundayList() {
        setWeekListData(7);
    }

    @OnClick(R.id.schedule_iv_add_1)
    public void addShow_1() {
        releaseShow(1);
    }

    @OnClick(R.id.schedule_iv_add_2)
    public void addShow_2() {
        releaseShow(2);
    }

    @OnClick(R.id.schedule_iv_add_3)
    public void addShow_3() {
        releaseShow(3);
    }

    @OnClick(R.id.schedule_iv_add_4)
    public void addShow_4() {
        releaseShow(4);
    }

    @OnClick(R.id.schedule_iv_add_5)
    public void addShow_5() {
        releaseShow(5);
    }

    @OnClick(R.id.schedule_iv_add_6)
    public void addShow_6() {
        releaseShow(6);
    }

    @OnClick(R.id.schedule_iv_add_7)
    public void addShow_7() {
        releaseShow(7);
    }

    /**
     * 发布表演，指定日期
     *
     * @param i
     */
    private void releaseShow(int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(monDate);
        instance.add(Calendar.DATE,i-1);
        ReleaseShowFragment.launch(mActivity);
    }

    /**
     * 周几的点击事件
     *
     * @param i 周几
     */
    private void setWeekListData(int i) {
        mTvListTitle.setText(String.format(listTitleStr, DateUtils.CN_FULL_WEEKS[i - 1]));
        if (mScheduleModel.getShowModelList(String.valueOf(i)) != null && mScheduleModel.getShowModelList(String.valueOf(i)).size() > 0) {
            mAdapter.refresh(mScheduleModel.getShowModelList(String.valueOf(i)));
            mRcyvlerview.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRcyvlerview.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }


    private Date monDate;//周一日期
    private Date sunDate;//周日日期
    private String timeStr = "WEEK: %s";
    private String sameMonthTimeStr = "%s %s-%s";
    private String diffMonthTimeStr = "%s %s-%s %s";
    private String listTitleStr = "SHOW LIST ON %s";

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mMoveLayout.setLoaction(UIUtils.getScreenHeight() / 2);
        //setTime
        List<Date> weekStartEndDate = DateUtils.getWeekStartEndDate();
        monDate = weekStartEndDate.get(0);
        sunDate = weekStartEndDate.get(1);
        mTvTime.setText(String.format(timeStr, generateTime(monDate, sunDate)));
        //init list
        mTextList = new ArrayList<>();
        mTextList.add(mTvNumber_1);
        mTextList.add(mTvNumber_2);
        mTextList.add(mTvNumber_3);
        mTextList.add(mTvNumber_4);
        mTextList.add(mTvNumber_5);
        mTextList.add(mTvNumber_6);
        mTextList.add(mTvNumber_7);
    }

    private String generateTime(Date from, Date end) {
        if (from.getMonth() == end.getMonth()) {
            String month = DateUtils.getMonthEn(from);
            return String.format(sameMonthTimeStr, month, from.getDate(), end.getDate());
        } else {
            return String.format(diffMonthTimeStr, DateUtils.getMonthEn(from), from.getDate(),
                    DateUtils.getMonthEn(end), end.getDate());
        }
    }

    @Override
    protected SchedulePresenter createPresenter() {
        return new SchedulePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_schedule_layout;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void refreshUI() {
        mPresenter.requestWeekList(DateUtils.getFormatTime(monDate), DateUtils.getFormatTime(sunDate));
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setFailureView() {

    }

    @Override
    public void setSuccessView(ScheduleModel model) {
        mScheduleModel = model;
        //设置每天的数据
        for (int i = 1; i < 8; i++) {
            List<ShowModel> showModelList = mScheduleModel.getShowModelList(String.valueOf(i));
            if (showModelList != null) {
                mTextList.get(i - 1).setText(String.valueOf(showModelList.size()));
                mTextList.get(i - 1).setBackgroundResource(R.drawable.shape_blue_circle_solid_bg);
            } else {
                mTextList.get(i - 1).setText("0");
                mTextList.get(i - 1).setBackgroundResource(R.drawable.shape_circle_grey_solid_bg);
            }
        }
        //title
        mTvListTitle.setText(String.format(listTitleStr, DateUtils.CN_FULL_WEEKS[todayCal.get(Calendar.DAY_OF_WEEK) - 1]));

        //list
        int day = todayCal.get(Calendar.DAY_OF_WEEK);
        List<ShowModel> showModelList;
        if (day != 1) {//不是周日
            showModelList = mScheduleModel.getShowModelList(String.valueOf(day - 1));
        } else {//周日
            showModelList = mScheduleModel.getShowModelList("7");
        }

        if (showModelList != null && showModelList.size() > 0) {
            mRcyvlerview.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRcyvlerview.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new ScheduleAdapter(showModelList);
            mRcyvlerview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            mRcyvlerview.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(showModelList);
        }
        mAdapter.setOnSpotlightListener(this);
    }

    @Override
    public void spotlight(String performID, String userID) {
        mPresenter.spotlightPerform(performID, userID);
    }
}
