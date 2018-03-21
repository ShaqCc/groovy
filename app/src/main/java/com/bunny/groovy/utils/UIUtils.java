package com.bunny.groovy.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bunny.groovy.R;
import com.bunny.groovy.base.BaseApp;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;


public class UIUtils {

    public static Toast mToast;

    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    public static void showToast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), "", duration);
        }
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * 用于在线程中执行弹土司操作
     */
    public static void showToastSafely(final String msg) {
        UIUtils.getMainThreadHandler().post(new Runnable() {

            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                }
                mToast.setText(msg);
                mToast.show();
            }
        });
    }

    /**
     * 打印吐司
     *
     * @param msg
     */
    private static Toast baseToast;

    public static void showBaseToast(String msg) {
        if (baseToast == null) {
            baseToast = new Toast(getContext());
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.base_toast_layout, null);
            baseToast.setView(inflate);
            //获取屏幕高度
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            baseToast.setGravity(Gravity.TOP, 0, height / 3);
        }
        TextView tvMsg = baseToast.getView().findViewById(R.id.toast_msg);
        if (tvMsg != null)
            tvMsg.setText(msg);
        try {
            Field mTNField = baseToast.getClass().getDeclaredField("mTN");
            mTNField.setAccessible(true);
            Object mTNObject = mTNField.get(baseToast);
            Class tnClass = mTNObject.getClass();
            Field paramsField = tnClass.getDeclaredField("mParams");
            /**由于WindowManager.LayoutParams mParams的权限是private*/
            paramsField.setAccessible(true);
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) paramsField.get(mTNObject);
            layoutParams.windowAnimations = R.anim.toast_in_anim;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        baseToast.show();
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideSoftInput(View view) {
        final InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static Calendar minCal;
    private static Calendar maxCal;
    private static Calendar monthCounter;

    public static void showDateChoosePopWindow(Date minDate, Date maxDate) {
        if (minDate == null || maxDate == null) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-null.  " + dbg(minDate, maxDate));
        }
        if (minDate.after(maxDate)) {
            throw new IllegalArgumentException(
                    "minDate must be before maxDate.  " + dbg(minDate, maxDate));
        }

        minCal = Calendar.getInstance(Locale.getDefault());
        maxCal = Calendar.getInstance(Locale.getDefault());
        monthCounter = Calendar.getInstance(Locale.getDefault());

        minCal.setTime(minDate);
        maxCal.setTime(maxDate);
        setMidnight(minCal);
        setMidnight(maxCal);

        maxCal.add(MINUTE, -1);

        // Now iterate between minCal and maxCal and build up our list of months to show.
        monthCounter.setTime(minCal.getTime());
        final int maxMonth = maxCal.get(MONTH);
        final int maxYear = maxCal.get(YEAR);

        while ((monthCounter.get(MONTH) <= maxMonth // Up to, including the month.
                || monthCounter.get(YEAR) < maxYear) // Up to the year.
                && monthCounter.get(YEAR) < maxYear + 1) { // But not > next yr.
            Date date = monthCounter.getTime();
//            MonthDescriptor month =
//                    new MonthDescriptor(monthCounter.get(MONTH), monthCounter.get(YEAR), date,
//                            Utils.formatDateToMMYYYY(date));
//            cells.add(getMonthCells(month, monthCounter, priceDescriptor));
//            Logr.d("Adding month %s", month);
//            months.add(month);
            monthCounter.add(MONTH, 1);
        }
    }

    private static String dbg(Date minDate, Date maxDate) {
        return "minDate: " + minDate + "\nmaxDate: " + maxDate;
    }

    private static void setMidnight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
    }

    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApp.getContext();
    }

    /**
     * 得到resources对象
     *
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符串
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到string.xml中的字符串，带点位符
     *
     * @return
     */
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 得到string.xml中和字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到主线程Handler
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return BaseApp.getMainHandler();
    }

    /**
     * 得到主线程id
     *
     * @return
     */
    public static long getMainThreadId() {
        return BaseApp.getMainThreadId();
    }

    /**
     * 安全的执行一个任务
     *
     * @param task
     */
    public static void postTaskSafely(Runnable task) {
        int curThreadId = android.os.Process.myTid();
        // 如果当前线程是主线程
        if (curThreadId == getMainThreadId()) {
            task.run();
        } else {
            // 如果当前线程不是主线程
            getMainThreadHandler().post(task);
        }
    }

    /**
     * 延迟执行任务
     *
     * @param task
     * @param delayMillis
     */
    public static void postTaskDelay(Runnable task, int delayMillis) {
        getMainThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * 移除任务
     */
    public static void removeTask(Runnable task) {
        getMainThreadHandler().removeCallbacks(task);
    }

    /**
     * dip-->px
     */
    public static int dip2Px(int dip) {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        float density = getResource().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    /**
     * px-->dip
     */
    public static int px2dip(int px) {

        float density = getResource().getDisplayMetrics().density;
        int dip = (int) (px / density + 0.5f);
        return dip;
    }

    /**
     * sp-->px
     */
    public static int sp2px(int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResource().getDisplayMetrics()) + 0.5f);
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 判断输入框是否为空
     *
     * @param editText
     * @return
     */
    public static boolean isEdittextEmpty(EditText editText) {
        if (editText == null) return true;
        return TextUtils.isEmpty(editText.getText().toString());
    }
}