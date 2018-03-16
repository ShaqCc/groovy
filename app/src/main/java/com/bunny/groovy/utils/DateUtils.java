package com.bunny.groovy.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/12/20.
 ****************************************/

public class DateUtils {
    private static String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static String[] weeks = {"SUN", "MON", "TUE", "WED", "THUR", "FRI", "SAT"};
    public static String[] CN_weeks = {"MON", "TUE", "WED", "THUR", "FRI", "SAT", "SUN"};

    public static String getMonthEn(Date date) {
        return months[date.getMonth()];
    }

    public static String getDayOfWeek(Calendar calendar) {
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return weeks[i - 1];
    }

    //获取小时
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    //获取分钟
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    //获取周
    public static int getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //获取周
    public static int getWeek(int year, int moth, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, moth - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //获取年
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    //获取月
    public static int getMoth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    //获取日
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    public static Date getDate(int year, int moth, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, moth - 1, day, hour, minute);
        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * @param date 日期
     * @param time 时间
     * @return yyyy-MM-dd HH:mm
     */
    public static String getFormatTime(Date date, String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String[] split = time.split(":");
        if (split.length == 2) {
            date.setHours(Integer.parseInt(split[0]));
            date.setMinutes(Integer.parseInt(split[1]));
        }
        return dateFormat.format(date);
    }

    public static String getFormatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getFormatTimeHHMM(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    private static List<Date> list = new ArrayList<>(2);

    /**
     * 获取本周，周一，周日的日期
     *
     * @return
     */
    public static List<Date> getWeekStartEndDate() {
        list.clear();
        Calendar calendar = Calendar.getInstance();
        System.out.println("今天是" + calendar.getTime());

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {//今天周日
            //计算周一
            calendar.add(Calendar.DATE, -6);
            System.out.println("周一是" + calendar.get(Calendar.DATE) + "号");
            list.add(0, calendar.getTime());
            //周日
            calendar.add(Calendar.DATE, 6);
            System.out.println("周日是" + calendar.get(Calendar.DATE) + "号");
            list.add(1, calendar.getTime());
        } else {
            //算出本周第一天日期
            calendar.add(Calendar.DATE, 2 - day);
            System.out.println("周一是" + calendar.get(Calendar.DATE) + "号");
            list.add(0, calendar.getTime());
            //计算周日
            calendar.add(Calendar.DATE, 6);
            System.out.println("周日是" + calendar.get(Calendar.DATE) + "号");
            list.add(1, calendar.getTime());
        }

        return list;
    }

    /**
     * 获取下周一，周日的日期
     *
     * @param from
     * @param end
     * @return
     */
    public static List<Date> nextWeek(Date from, Date end) {
        list.clear();
        Calendar instance = Calendar.getInstance();
        instance.setTime(from);
        instance.add(Calendar.DATE, 7);
        list.add(0, instance.getTime());

        instance.setTime(end);
        instance.add(Calendar.DATE, 7);
        list.add(1, instance.getTime());
        return list;
    }

    /**
     * 上一周时间
     *
     * @param from
     * @param end
     * @return
     */
    public static List<Date> lastWeek(Date from, Date end) {
        list.clear();
        Calendar instance = Calendar.getInstance();
        instance.setTime(from);
        instance.add(Calendar.DATE, -7);
        list.add(0, instance.getTime());

        instance.setTime(end);
        instance.add(Calendar.DATE, -7);
        list.add(1, instance.getTime());
        return list;
    }


    //获取日期的小时时间
    public static String getDateHour(String date) {
        if (!TextUtils.isEmpty(date)) {
            String[] split = date.split(" ");
            if (split.length > 0)
                return split[1];
        }
        return "0:00";
    }

    public static Date getDate(String timeStr) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static void main(String[] args) {
        getWeekStartEndDate();
    }
}
