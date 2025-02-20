package com.cuit.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author sccl
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 今年之前的日期
     *
     * @param date
     * @return
     */
    public static boolean isBeforeThisYear(Date date) {
        Date yearBeginning = getThisYearBeginning();
        return isBefore(date, yearBeginning);
    }

    public static Date getThisYearBeginning() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 得到日期字符串 ，转换格式（yyyy-MM-dd）
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(long dateTime, String pattern) {
        return formatDate(new Date(dateTime), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (date != null) {
//          if (StringUtils.isNotBlank(pattern)) {
//              formatDate = DateFormatUtils.format(date, pattern);
//          } else {
//              formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
//          }
            if (StringUtils.isBlank(pattern)) {
                pattern = "yyyy-MM-dd";
            }
            formatDate = FastDateFormat.getInstance(pattern).format(date);
        }
        return formatDate;
    }


    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String milliTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSSSSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String getFormat(final String format, final Date date) {
        return parseDateToStr(format, date);
    }

    public static final String getNowFormat(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(String format, final String ts) {
        try {
            if (format == null) {
                format = YYYY_MM_DD;
            }
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 指定时间+天数后的时间
     */
    public static String getNextTime(String time, int days, int month, int year) throws ParseException {
        if (StringUtils.isBlank(time)) {
            time = DateUtils.getDate();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date = sdf.parse(time);
        c.setTime(date);
        if (year != 0) {
            c.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            c.add(Calendar.MONTH, month);
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        if (days != 0) {
            c.add(Calendar.DATE, days);// num为增加的天数，可以改变的
        }
        return sdf.format(c.getTime());
    }

    /**
     * 指定时间+天数后的时间
     */
    public static final Date getNextDateTime(Date date, int days, int month, int year) {
        try {
            String ts = DateUtils.getDate();
            if (date != null) {
                ts = DateUtils.dateTime(date);
            }
            ts = DateUtils.getNextTime(ts, days, month, year);
            return new SimpleDateFormat(YYYY_MM_DD).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 两个日期的天数差
     */
    public static int daysBetween(String startLeaveTime, String endLeaveTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        try {
            calst.setTime(sdf.parse(startLeaveTime));
            caled.setTime(sdf.parse(endLeaveTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        if (caled.getTime().getTime() < calst.getTime().getTime()) {
            return -1;
        }
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24 + 1;
        return days;
    }

    /**
     * 指定几个月
     *
     * @return
     */
    public static String getNextMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, month);
        return new SimpleDateFormat("yyyyMM").format(c.getTime());
    }

    /**
     * 判断时间是否在时间段内(包含两边区间)
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @param type:     1 包含两边区间 2：包含头区间 3：包含尾区间
     * @return
     */
    private static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime, String type) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if ((date.after(begin) && date.before(end))
                || (nowTime.equals(beginTime) && ("1".equals(type) || "2".equals(type)))
                || (nowTime.equals(endTime) && ("1".equals(type) || "3".equals(type)))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断时间是否在时间段内(包含两边区间)
     *
     * @param nowTimeStr
     * @param beginTimeStr
     * @param endTimeStr
     * @return
     */
    public static boolean belongCalendarStr(String nowTimeStr, String beginTimeStr, String endTimeStr) {
        return DateUtils.belongCalendar(DateUtils.dateTime(null, nowTimeStr), DateUtils.dateTime(null, beginTimeStr), DateUtils.dateTime(null, endTimeStr), "1");
    }

    /**
     * 判断时间是否在时间段内(包含头区间)
     *
     * @param nowTimeStr
     * @param beginTimeStr
     * @param endTimeStr
     * @return
     */
    public static boolean belongCalendarStrHead(String nowTimeStr, String beginTimeStr, String endTimeStr) {
        return DateUtils.belongCalendar(DateUtils.dateTime(null, nowTimeStr), DateUtils.dateTime(null, beginTimeStr), DateUtils.dateTime(null, endTimeStr), "2");
    }

    /**
     * 判断时间是否在时间段内(包含结尾区间)
     *
     * @param nowTimeStr
     * @param beginTimeStr
     * @param endTime
     * @return
     */
    public static boolean belongCalendarStrTail(Object nowTime, Object beginTime, Object endTime) {
        String nowTimeStr = null;
        String beginTimeStr = null;
        String endTimeStr = null;
        if (nowTime instanceof Date) {
            nowTimeStr = DateUtils.dateTime((Date) nowTime);
        } else {
            nowTimeStr = (String) nowTime;
        }
        if (beginTime instanceof Date) {
            beginTimeStr = DateUtils.dateTime((Date) beginTime);
        } else {
            beginTimeStr = (String) beginTime;
        }
        if (endTime instanceof Date) {
            endTimeStr = DateUtils.dateTime((Date) endTime);
        } else {
            endTimeStr = (String) endTime;
        }
        return DateUtils.belongCalendar(DateUtils.dateTime(null, nowTimeStr), DateUtils.dateTime(null, beginTimeStr), DateUtils.dateTime(null, endTimeStr), "3");
    }

    /**
     * 今天之前的日期
     *
     * @return boolean
     */
    public static boolean isBeforeToday(Date date) {
        Date todayZero = getTodayZeroClock();
        return isBefore(date, todayZero);
    }

    /**
     * 得到当天的0点0分0秒的日期
     *
     * @return Date 日期对象
     */
    public static Date getTodayZeroClock() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 比较两个日期 判断start日期是否在end日期之前
     *
     * @param start 日期
     * @param end   日期
     * @return boolean
     */
    public static boolean isBefore(Date start, Date end) {
        return start.getTime() - end.getTime() < 0 ? true : false;
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.belongCalendarStrTail(new Date(), "2022-09-11", new Date()));
    }

}
