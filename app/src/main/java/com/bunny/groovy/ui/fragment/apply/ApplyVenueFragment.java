package com.bunny.groovy.ui.fragment.apply;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.StyleGridAdapter;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.model.VenueModel;
import com.bunny.groovy.presenter.ApplyVenuePresenter;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.DateUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.IApplyVenueView;
import com.bunny.groovy.weidget.datepick.DatePickerHelper;
import com.bunny.groovy.weidget.loopview.LoopView;
import com.bunny.groovy.weidget.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  申请演出厅表演页面
 *
 * Author: Created by bayin on 2018/1/3.
 ****************************************/

public class ApplyVenueFragment extends BaseFragment<ApplyVenuePresenter> implements IApplyVenueView {

    public static String KEY_VENUE_BEAN = "KEY_VENUE_BEAN";
    private static VenueModel sVenueBean;
    private TextView mTvTimeTitle;
    private List<String> mTimeClockList,mRealTimeList;
    private Calendar mSelectDate = Calendar.getInstance();//选择的日期
    private PopupWindow mDatePop;
    private Date today = Calendar.getInstance().getTime();
    private String startTime = "";//开始时间
    private String endTime = "";//结束时间
    private List<StyleModel> styleList;
    private PopupWindow mTimePop;
    private PopupWindow mPopupWindow;
    private StyleGridAdapter mAdapter;


    public static void launch(Activity from, Bundle bundle) {
        sVenueBean = bundle.getParcelable(KEY_VENUE_BEAN);
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "APPLY");
        FragmentContainerActivity.launch(from, ApplyVenueFragment.class, bundle);
    }

    @Bind(R.id.apply_et_time)
    EditText etTime;
    @Bind(R.id.apply_et_style)
    EditText etStyle;
    @Bind(R.id.apply_et_bio)
    EditText etDesc;


    //弹出选择style窗口
    @OnClick(R.id.apply_et_style)
    public void showStyle() {
        UIUtils.hideSoftInput(etStyle);
        if (styleList == null || styleList.size() == 0)
            mPresenter.requestStyle();
        else showStylePop(styleList);
    }

    //弹出时间选择窗口
    @OnClick(R.id.apply_et_time)
    public void selectTime() {
        UIUtils.hideSoftInput(etStyle);
        showTimeChoosePop();
    }

    /**
     * 弹出选择时间窗口
     */
    private void showTimeChoosePop() {
        if (mTimePop == null)
            initTimePop();
        mTimePop.showAtLocation(etTime, Gravity.CENTER, 0, 0);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        etStyle.setFocusable(false);
        etTime.setFocusable(false);
    }

    /**
     * 初始化选择时间点弹框
     */
    private void initTimePop() {
        mTimePop = new PopupWindow(getActivity());
        View timeView = LayoutInflater.from(getActivity()).inflate(R.layout.weidget_time_choose_layout, null);
        mTimePop.setContentView(timeView);
        mTimePop.setWidth(UIUtils.getScreenWidth() - UIUtils.dip2Px(32));
        mTimePop.setHeight(UIUtils.getScreenHeight() / 2);
        timeView.setFocusable(true);
        timeView.setFocusableInTouchMode(true);
        mTimePop.setFocusable(true);
        mTvTimeTitle = timeView.findViewById(R.id.weidget_tv_title);
        final LoopView loopviewFromTime = timeView.findViewById(R.id.weidget_from_time);
        final LoopView loopviewEndTime = timeView.findViewById(R.id.weidget_end_time);

        //set data
        mTvTimeTitle.setText(Utils.getFormatDate(mSelectDate.getTime()));
        loopviewFromTime.setItems(mTimeClockList);
        loopviewEndTime.setItems(mTimeClockList);
        //set listener
        timeView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    closeTimePop();
                    return true;
                }
                return false;
            }
        });
        timeView.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTimePop();
            }
        });
        timeView.findViewById(R.id.pop_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loopviewFromTime.getSelectedItem() >= loopviewEndTime.getSelectedItem()) {
                    UIUtils.showBaseToast("Start time must not be less than end time.");
                } else {
                    closeTimePop();
                    //设置开始结束时间
                    startTime = mRealTimeList.get(loopviewFromTime.getSelectedItem());
                    endTime = mRealTimeList.get(loopviewEndTime.getSelectedItem());
                    etTime.setText(DateUtils.getFormatTime(mSelectDate.getTime(), startTime) + "-" + endTime);
                }
            }
        });
        mTvTimeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出选择日期的弹窗
                showDatePop();
            }
        });
    }


    /**
     * 关闭时间点选择框
     */
    private void closeTimePop() {
        if (mTimePop != null) mTimePop.dismiss();
    }


    /**
     * 显示日期选择器
     */
    private void showDatePop() {
        if (mDatePop == null)
            initDatePop();
        mDatePop.showAtLocation(etTime, Gravity.CENTER, 0, 0);
    }

    /**
     * 关闭选择日期窗口
     */
    private void closeDatePop() {
        if (mDatePop != null) mDatePop.dismiss();
    }

    /**
     * 初始化选择日期窗口
     */
    private void initDatePop() {
        Calendar minCalendar = Calendar.getInstance();
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.YEAR, 1);
        maxCalendar.add(Calendar.MINUTE, -1);

        mDatePop = new PopupWindow(getActivity());
        View dateView = LayoutInflater.from(getActivity()).inflate(R.layout.weidget_date_choose_layout, null, false);
        mDatePop.setContentView(dateView);
        mDatePop.setWidth(UIUtils.getScreenWidth() - UIUtils.dip2Px(32));
        mDatePop.setHeight(UIUtils.getScreenHeight() / 2);
        LoopView loopMonth = dateView.findViewById(R.id.weidget_month);
        final LoopView loopDay = dateView.findViewById(R.id.weidget_day);
        LoopView loopYear = dateView.findViewById(R.id.weidget_year);
        //set data
        final DatePickerHelper helper = new DatePickerHelper();
        //年
        final List<String> years = new ArrayList<>();
        years.add(String.valueOf(minCalendar.get(Calendar.YEAR)));
        years.add(String.valueOf(maxCalendar.get(Calendar.YEAR)));
        loopYear.setItems(years);
        loopYear.setNotLoop();
        //listener
        loopYear.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
//                currentYear = Integer.parseInt(years.get(index));
                mSelectDate.set(Calendar.YEAR, Integer.parseInt(years.get(index)));
            }
        });
        //月份
        String[] monthValues = helper.getEnMonths();
        loopMonth.setItems(Arrays.asList(monthValues));
        loopMonth.setNotLoop();
        //listener
        loopMonth.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
//                currentMonth = index + 1;
                mSelectDate.set(Calendar.MONTH, index);
                loopDay.setItems(Arrays.asList(helper.getDisplayDayAndWeek(mSelectDate.get(Calendar.YEAR),
                        mSelectDate.get(Calendar.MONTH))));
            }
        });
        //日期
        String[] dayValues = helper.getDisplayDayAndWeek(mSelectDate.get(Calendar.YEAR), mSelectDate.get(Calendar.MONTH));
        loopDay.setItems(Arrays.asList(dayValues));
        loopDay.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mSelectDate.set(Calendar.DATE, index + 1);
            }
        });
        //初始日期
        loopYear.setInitPosition(mSelectDate.get(Calendar.YEAR));
        loopMonth.setInitPosition(mSelectDate.get(Calendar.MONTH));
        loopDay.setInitPosition(mSelectDate.get(Calendar.DATE) - 1);
        //点击事件
        dateView.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDatePop();
            }
        });
        dateView.findViewById(R.id.pop_confirm).setOnClickListener(new View.OnClickListener() {
            /**
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (mSelectDate.getTime().before(today)) {
                    UIUtils.showBaseToast("The selection date is less than today.");
                } else {
                    closeDatePop();
                    //设置title
                    mTvTimeTitle.setText(Utils.getFormatDate(mSelectDate.getTime()));
                }
            }
        });
    }


    //申请
    @OnClick(R.id.apply_tv_apply)
    public void apply() {
        UIUtils.hideSoftInput(etStyle);
        //拦截判空
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            UIUtils.showBaseToast("Please input description.");
            return;
        }

        if (TextUtils.isEmpty(etStyle.getText().toString())) {
            UIUtils.showBaseToast("Please choose perform style.");
            return;
        }

        //set params
        //   performType
        //   performStartDate
        //   performEndDate
        //   performDesc
        //   venueName
        //   venueAddress
        //   performerName
        //   venueLongitude
        //   venueLatitude
        //   venueID

        HashMap<String, String> map = new HashMap<>();
        map.put("performStartDate", DateUtils.getFormatTime(mSelectDate.getTime(), startTime));
        map.put("performEndDate", DateUtils.getFormatTime(mSelectDate.getTime(), endTime));
        map.put("performType", etStyle.getText().toString());
        map.put("performDesc", etDesc.getText().toString());
        map.put("performerName", AppCacheData.getPerformerUserModel().getUserName());
        map.put("venueID", sVenueBean.getVenueID());
        map.put("venueName", sVenueBean.getVenueName());
        map.put("venueAddress", sVenueBean.getVenueAddress());
        map.put("venueLongitude", sVenueBean.getLongitude());
        map.put("venueLatitude", sVenueBean.getLatitude());
        map.put("isOpportunity", "0");
        mPresenter.applyVenue(map);
    }

    @Override
    public Activity get() {
        return mActivity;
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
        UIUtils.hideSoftInput(etStyle);
        styleList = modelList;
        if (mPopupWindow == null)
            initStylePop(modelList);
        mPopupWindow.showAtLocation(etStyle, Gravity.CENTER, 0, 0);
    }


    /**
     * 关闭选择style窗口
     */
    private void closePop() {
        if (mPopupWindow != null) mPopupWindow.dismiss();
    }

    private void initStylePop(List<StyleModel> modelList) {
        mPopupWindow = new PopupWindow(getActivity());
        View popview = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_grid_layout, null, false);
        mPopupWindow.setContentView(popview);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setWidth(UIUtils.getScreenWidth() - UIUtils.dip2Px(32));
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerview = popview.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdapter = new StyleGridAdapter(modelList, etStyle.getText().toString().trim());
        recyclerview.setAdapter(mAdapter);
        popview.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
            }
        });
        popview.findViewById(R.id.pop_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
                etStyle.setText(mAdapter.getSelectStyles());
            }
        });
        // 按下android回退物理键 PopipWindow消失解决
        popview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    closePop();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected ApplyVenuePresenter createPresenter() {
        return new ApplyVenuePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_apply_opp_layout;
    }

    @Override
    protected void loadData() {
        //获取可选择的时间
        String[] timeClockArray = mActivity.getResources().getStringArray(R.array.time_clock_array);
        String[] realTimeArr = mActivity.getResources().getStringArray(R.array.time_clock_array_24);
        mTimeClockList = Arrays.asList(timeClockArray);
        mRealTimeList = Arrays.asList(realTimeArr);
    }
}
