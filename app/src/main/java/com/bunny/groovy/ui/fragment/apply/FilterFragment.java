package com.bunny.groovy.ui.fragment.apply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.BasePresenter;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.utils.DateUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.weidget.datepick.DatePickerHelper;
import com.bunny.groovy.weidget.loopview.LoopView;
import com.bunny.groovy.weidget.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/1/2.
 ****************************************/

public class FilterFragment extends BaseFragment {

    @Bind(R.id.filter_seekbar)
    SeekBar mSeekBar;
    @Bind(R.id.filter_tv_distance)
    TextView mTvDistance;
    @Bind(R.id.filter_et_start_time)
    EditText mEtStartTime;

    public static String KEY_DISTANCE = "key_distance";
    private Calendar mSelectDate = Calendar.getInstance();//选择的日期
    private PopupWindow mDatePop;
    private Date today = Calendar.getInstance().getTime();
    private String distance;
    private String value_date;


    public static void launchForResult(Activity from, Bundle bundle, int requestCode) {
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "OPPORTUNITY FILTERS");
        FragmentContainerActivity.launchForResult(from, FilterFragment.class, bundle, requestCode);
    }

    @OnClick(R.id.filter_et_start_time)
    public void selectTime() {
        //选择时间
        showDatePop();
    }

    @OnClick(R.id.filter_tv_submit)
    public void submit() {
        //提交过滤条件
        distance = String.valueOf(mSeekBar.getProgress());
        Intent intent = new Intent();
        intent.putExtra("distance", distance);
        if (!TextUtils.isEmpty(mEtStartTime.getText().toString()))
            intent.putExtra("performStartDate", DateUtils.getFormatTimeHHMM(mSelectDate.getTime()));
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_opp_filter_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mEtStartTime.setFocusable(false);
        mSelectDate = Calendar.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            int distance = args.getInt(KEY_DISTANCE, -1);
            if (distance > 0) {
                mSeekBar.setProgress(distance);
                mTvDistance.setText(distance + "mi");
            }
//            String time = args.getString(KEY_START_TIME);
//            if (!TextUtils.isEmpty(time)) {
//                mEtStartTime.setText(time);
//            }else {
//                mEtStartTime.setText(DateUtils.getFormatTime(new Date()));
//            }
        } else {
            mSeekBar.setProgress(25);
            mTvDistance.setText("25mi");
            mEtStartTime.setText(DateUtils.getFormatTime(new Date()));
        }

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initListener() {
        super.initListener();
        mSeekBar.setMax(50);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = String.valueOf(progress);
                mTvDistance.setText(distance + "mi");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * 显示日期选择器
     */
    private void showDatePop() {
        if (mDatePop == null)
            initDatePop();
        mDatePop.showAtLocation(mEtStartTime, Gravity.CENTER, 0, 0);
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
                    UIUtils.showBaseToast("The selection date is less than today");
                } else {
                    mEtStartTime.setText(DateUtils.getFormatTime(mSelectDate.getTime()));
                    closeDatePop();
                }
            }
        });
    }

}
