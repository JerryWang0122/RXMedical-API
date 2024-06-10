package com.rxmedical.api.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    // 測試
    public static void main(String[] args) {
        System.out.println(getStartOfWeek(new Date()));
        System.out.println(getDateWeeksAgo(new Date(), 7));
    }

    /**
     * 傳入當天的Date，取得當週以星期日為起頭的Date
     * @param date
     * @return 當日該週的第一個星期日
     */
    public static Date getStartOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        resetTime(calendar);
        return calendar.getTime();
    }

    /**
     * 取得[weeks]週前對應日期的星期天日期
     * @param date 起始日
     * @param weeks 週數
     * @return
     */
    public static Date getDateWeeksAgo(Date date, int weeks) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, -weeks);
        resetTime(calendar);
        return calendar.getTime();
    }

    public static Date addWeeks(Date date, int weeks) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, weeks);
        resetTime(calendar);
        return calendar.getTime();
    }

    /**
     * 將當日時間設置為 00:00:00
     * @param calendar
     */
    private static void resetTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
