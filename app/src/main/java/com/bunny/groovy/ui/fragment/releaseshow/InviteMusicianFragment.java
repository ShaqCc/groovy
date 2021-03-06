package com.bunny.groovy.ui.fragment.releaseshow;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseFragment;
import com.bunny.groovy.base.FragmentContainerActivity;
import com.bunny.groovy.model.PerformerUserModel;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.InviteMusicianPresenter;
import com.bunny.groovy.utils.DateUtils;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.utils.Utils;
import com.bunny.groovy.view.ISetFileView;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 邀请表演者
 * Created by Administrator on 2017/12/16.
 */

public class InviteMusicianFragment extends BaseFragment<InviteMusicianPresenter> implements ISetFileView {

    private PopupWindow mTimePop;
    private Calendar mSelectDate = Calendar.getInstance();//选择的日期
    private String startTime = "";//开始时间
    private String endTime = "";//结束时间
    private List<String> mTimeClockList;
    private List<String> mRealTimeClockList;
    private PopupWindow mDatePop;
    private TextView mTvTimeTitle;
    private Date today = Calendar.getInstance().getTime();
    private static PerformerUserModel mPerformerModel;

    @Bind(R.id.iv_musician_head_pic)
    CircleImageView mHeadView;
    @Bind(R.id.musician_tv_name)
    TextView mNameView;
    @Bind(R.id.musician_tv_performerStar)
    TextView mStarView;
    @Bind(R.id.musician_tv_type)
    TextView mStyleView;
    @Bind(R.id.musician_tv_phone)
    TextView mPhoneView;


    public static void launch(Activity from, PerformerUserModel model) {
        mPerformerModel = model;
        Bundle bundle = new Bundle();
        bundle.putString(FragmentContainerActivity.FRAGMENT_TITLE, "INVITE");
        FragmentContainerActivity.launch(from, InviteMusicianFragment.class, bundle);
    }

    @Bind(R.id.release_et_time)
    EditText etTime;

    @Bind(R.id.release_et_bio)
    EditText etBio;

    @OnClick(R.id.tv_invite)
    public void invite() {
        //判断空
        if (UIUtils.isEdittextEmpty(etTime)) {
            UIUtils.showBaseToast("Please Choose Perform Date.");
            return;
        }
        if (UIUtils.isEdittextEmpty(etBio)) {
            UIUtils.showBaseToast("Please input Bio.");
            return;
        }
        mPresenter.inviteMusician(mPerformerModel.getUserID(), DateUtils.getFormatTime(mSelectDate.getTime(), startTime),
                DateUtils.getFormatTime(mSelectDate.getTime(), endTime), etBio.getText().toString().trim());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPerformerModel = null;
    }

    @OnClick(R.id.release_et_time)
    public void selectTime() {
        showTimeChoosePop();
    }

    /**
     * 弹出选择时间窗口
     */
    private void showTimeChoosePop() {
        if (mTimePop == null)
            initTimePop();
        mTimePop.showAtLocation(etBio, Gravity.CENTER, 0, 0);
    }

    /**
     * 关闭时间点选择框
     */
    private void closeTimePop() {
        if (mTimePop != null) mTimePop.dismiss();
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
                    startTime = mRealTimeClockList.get(loopviewFromTime.getSelectedItem());
                    endTime = mRealTimeClockList.get(loopviewEndTime.getSelectedItem());
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
     * 显示日期选择器
     */
    private void showDatePop() {
        if (mDatePop == null)
            initDatePop();
        mDatePop.showAtLocation(etBio, Gravity.CENTER, 0, 0);
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
                    closeDatePop();
                    //设置title
                    mTvTimeTitle.setText(Utils.getFormatDate(mSelectDate.getTime()));
                }
            }
        });
    }


    @Override
    protected InviteMusicianPresenter createPresenter() {
        return new InviteMusicianPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_invite_musician_layout;
    }

    @Override
    protected void loadData() {
        //获取展示的时间
        String[] timeClockArray = mActivity.getResources().getStringArray(R.array.time_clock_array);
        mTimeClockList = Arrays.asList(timeClockArray);
        //获取真正传递的时间
        String[] real = mActivity.getResources().getStringArray(R.array.time_clock_array_24);
        mRealTimeClockList = Arrays.asList(real);
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        etTime.setFocusable(false);
        if (!TextUtils.isEmpty(mPerformerModel.getHeadImg())) {
            Glide.with(getActivity()).load(mPerformerModel.getHeadImg())
                    .placeholder(R.drawable.icon_load_pic)
                    .error(R.drawable.icon_load_pic).dontAnimate()
                    .into(mHeadView);
        } else {
            mHeadView.setImageResource(R.drawable.icon_load_pic);
        }
        mNameView.setText(mPerformerModel.getUserName());
        mStarView.setText(Utils.getStar(mPerformerModel.getStarLevel()));
        mStyleView.setText(mPerformerModel.getPerformTypeName());
        mPhoneView.setText(mPerformerModel.getPhoneNumber());
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public Activity get() {
        return getActivity();
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
    }
}
