package com.bunny.groovy.ui.fragment.wallet;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.ScheduleVenueAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.model.VenueScheduleModel;
import com.bunny.groovy.model.VenueShowModel;
import com.bunny.groovy.presenter.VenueSchedulePresenter;
import com.bunny.groovy.ui.fragment.releaseshow.ReleaseShowFragment;
import com.bunny.groovy.utils.DateUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.IScheduleVenueView;
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

public class VenueScheduleFragment extends BaseFragment<VenueSchedulePresenter> implements IScheduleVenueView, ScheduleVenueAdapter.OnSpotlightListener {

    private VenueScheduleModel mVenueScheduleModel;
    private Calendar todayCal = Calendar.getInstance();
    private ArrayList<TextView> mTextList;
    private ArrayList<ImageView> mImgAddList;
    private ScheduleVenueAdapter mAdapter;

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

    @Bind(R.id.schedule_iv_add_1)
    ImageView mTvAdd1;
    @Bind(R.id.schedule_iv_add_2)
    ImageView mTvAdd2;
    @Bind(R.id.schedule_iv_add_3)
    ImageView mTvAdd3;
    @Bind(R.id.schedule_iv_add_4)
    ImageView mTvAdd4;
    @Bind(R.id.schedule_iv_add_5)
    ImageView mTvAdd5;
    @Bind(R.id.schedule_iv_add_6)
    ImageView mTvAdd6;
    @Bind(R.id.schedule_iv_add_7)
    ImageView mTvAdd7;

    @OnClick(R.id.schedule_bt_last_week)
    public void lastWeek() {
        Date currentDate = todayCal.getTime();
        if (currentDate.before(monDate)) {
            List<Date> dates = DateUtils.lastWeek(monDate, sunDate);
            monDate = dates.get(0);
            sunDate = dates.get(1);
            mTvTime.setText(String.format(timeStr, generateTime(monDate, sunDate)));
            mPresenter.requestVenueWeekList(DateUtils.getFormatTime(monDate), DateUtils.getFormatTime(sunDate));
        }

    }

    @OnClick(R.id.schedule_bt_next_week)
    public void nextWeek() {
        List<Date> dates = DateUtils.nextWeek(monDate, sunDate);
        monDate = dates.get(0);
        sunDate = dates.get(1);
        mTvTime.setText(String.format(timeStr, generateTime(monDate, sunDate)));
        mPresenter.requestVenueWeekList(DateUtils.getFormatTime(monDate), DateUtils.getFormatTime(sunDate));

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
        instance.add(Calendar.DATE, i - 1);
        ReleaseShowFragment.launch(mActivity);
    }

    /**
     * 周几的点击事件
     *
     * @param i 周几
     */
    private void setWeekListData(int i) {
        if (mVenueScheduleModel.getShowModelList(String.valueOf(i)) != null && mVenueScheduleModel.getShowModelList(String.valueOf(i)).size() > 0) {
            mTvListTitle.setText(String.format(listTitleStr, DateUtils.CN_weeks[i - 1]));
            mTvListTitle.setGravity(Gravity.LEFT);
            mAdapter.refresh(mVenueScheduleModel.getShowModelList(String.valueOf(i)));
            mRcyvlerview.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mTvListTitle.setText(String.format(listTitleClearStr, DateUtils.getDayOfWeek(todayCal)));
            mTvListTitle.setGravity(Gravity.CENTER);
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
    private String listTitleClearStr = "YOUR SCHEDULE'S CLEAR %s!";

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
        mImgAddList = new ArrayList<>();
        mImgAddList.add(mTvAdd1);
        mImgAddList.add(mTvAdd2);
        mImgAddList.add(mTvAdd3);
        mImgAddList.add(mTvAdd4);
        mImgAddList.add(mTvAdd5);
        mImgAddList.add(mTvAdd6);
        mImgAddList.add(mTvAdd7);
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
    protected VenueSchedulePresenter createPresenter() {
        return new VenueSchedulePresenter(this);
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
        mPresenter.requestVenueWeekList(DateUtils.getFormatTime(monDate), DateUtils.getFormatTime(sunDate));
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void setFailureView() {

    }

    @Override
    public void setVenueSuccessView(VenueScheduleModel model) {
        mVenueScheduleModel = model;
        //设置每天的数据
        for (int i = 1; i < 8; i++) {
            List<VenueShowModel> showModelList = mVenueScheduleModel.getShowModelList(String.valueOf(i));
            if (showModelList != null) {
                mTextList.get(i - 1).setText(String.valueOf(showModelList.size()));
                mTextList.get(i - 1).setBackgroundResource(R.drawable.shape_blue_circle_solid_bg);
            } else {
                mTextList.get(i - 1).setText("0");
                mTextList.get(i - 1).setBackgroundResource(R.drawable.shape_circle_grey_solid_bg);
            }
        }

        //list
        int day = todayCal.get(Calendar.DAY_OF_WEEK);
        List<VenueShowModel> venueShowModelList;
        if (day != 1) {//不是周日
            venueShowModelList = mVenueScheduleModel.getShowModelList(String.valueOf(day - 1));
        } else {//周日
            venueShowModelList = mVenueScheduleModel.getShowModelList("7");
        }
        Date currentDate = todayCal.getTime();
        if (currentDate.after(monDate) && currentDate.before(sunDate)) {
            if (day != 1) {//不是周日
                for (int i = 0; i < 6; i++) {
                    mImgAddList.get(i).setVisibility(i < day - 2 ? View.GONE : View.VISIBLE);
                }
            } else {//周日
                for (int i = 0; i < 6; i++) {
                    mImgAddList.get(i).setVisibility(View.GONE);
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                mImgAddList.get(i).setVisibility(View.VISIBLE);
            }
        }
        if (venueShowModelList != null && venueShowModelList.size() > 0) {
            mTvListTitle.setText(String.format(listTitleStr, DateUtils.getDayOfWeek(todayCal)));
            mTvListTitle.setGravity(Gravity.LEFT);
            mRcyvlerview.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mTvListTitle.setText(String.format(listTitleClearStr, DateUtils.getDayOfWeek(todayCal)));
            mTvListTitle.setGravity(Gravity.CENTER);
            mRcyvlerview.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new ScheduleVenueAdapter(venueShowModelList);
            mRcyvlerview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            mRcyvlerview.setAdapter(mAdapter);
        } else {
            mAdapter.refresh(venueShowModelList);
        }
        mAdapter.setOnSpotlightListener(this);
    }

    @Override
    public void spotlight(String performID, String userID) {
        mPresenter.spotlightPerform(performID, userID);
    }
}
