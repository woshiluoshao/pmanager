package jp.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static final String TIME_FORMAT_NORMAL_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    public static final String DATE_FORMAT_NORMAL_SECOND_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_NORMAL_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NORMAL_MINUTE = "yyyy-MM-dd HH:mm";

    /**
     * 功能：获取当前时间yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String getCurrentTimeYmdHmsss() {
        return getDateString(new Date(), DATE_FORMAT_NORMAL_SECOND_SSS);
    }

    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTimeYmdHms() {
        return getDateString(new Date(), DATE_FORMAT_NORMAL_SECOND);
    }

    /**
     *获取当前时间yyyy-MM-dd HH:mm
     */
    public static String getCurrentTimeYmdHm() {
        return getDateString(new Date(), DATE_FORMAT_NORMAL_MINUTE);
    }

    /**
     * 将当前时间转换成某个格式
     * @param format
     * @return
     */
    public static String getCurrentTimeByFormat(String format) {

        if(format == null || format == "") {
            format = "yyyy-MM-dd";
        }

        return getDateString(new Date(), format);
    }

    /**
     * 获取当前时间戳yyyyMMddHHmmssSSS
     */
    public static String getTimeStamp() {

        return getDateString(new Date(), TIME_FORMAT_NORMAL_yyyyMMddHHmmssSSS);
    }

    /**
     * 将Date类型转换成String类型
     * @param date
     * @param format
     * @return
     */
    public static String getDateToStr(Date date, String format) {

        if(format == null || format == "") {
            format = "yyyy-MM-dd";
        }

        return getDateString(date, format);
    }

    /**
     * 根据format格式化String，返回Date
     */
    public static Date getStrToDate(String dateStr, String format) {

        if(format==null) format="yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat f1 = new SimpleDateFormat(format);

        Date date = null;

        try {
            date = f1.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("字符串转化为日期出错");
        }

        return date;
    }

    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss.SSS
     * @return
     */
    public static Date getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 根据format格式格式化Date 返回String
     * @param date
     * @param format
     *            DateUtil.DATE_FORMAT_CHINESE_DAY = "yyyy年MM月dd日"; DateUtil.DATE_FORMAT_CHINESE_MINUTE =
     *            "yyyy年MM月dd日 HH:mm"; DateUtil.DATE_FORMAT_CHINESE_SECOND = "yyyy年MM月dd日 HH:mm:ss";
     *            DateUtil.DATE_FORMAT_NORMAL_YEAR = "yyyy"; DateUtil.DATE_FORMAT_NORMAL_MONTH = "yyyy-MM";
     *            DateUtil.DATE_FORMAT_NORMAL_DAY = "yyyy-MM-dd"; DateUtil.DATE_FORMAT_NORMAL_MINUTE =
     *            "yyyy-MM-dd HH:mm"; DateUtil.DATE_FORMAT_NORMAL_SECOND = "yyyy-MM-dd HH:mm:ss";
     *            DateUtil.TIME_FORMAT_NORMAL = "HH:mm"; DateUtil.TIME_FORMAT_NORMAL_HH = "HH";
     *            DateUtil.TIME_FORMAT_NORMAL_MM = "mm";
     */
    public static String getDateString(Date date, String format) {

        String ret = "";

        if (format != null) {
            ret = new SimpleDateFormat(format).format(date);
        } else {
            ret = new SimpleDateFormat(DATE_FORMAT_NORMAL_MINUTE).format(date);
        }

        return ret;
    }

    public static float BJ(long start ,long end){

        float totalTime = (end - start) / 1000f;

        return totalTime;
    }

    /**
     * 获取GMT日期格式
     * @return
     */
    public static String getGMT() {

        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String str = sdf.format(cd.getTime());
        System.out.println(str);

        return str;
    }

    /**
     * 比较两个时间的时间差
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getCompareTimeDiff(String startTime, String endTime) {

        Date start = null;
        Date end = null;
        double second = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try {
            start = sdf.parse(startTime);
            end = sdf.parse(endTime);

            long dd1 = start.getTime();
            long dd2 = end.getTime();
            second = (double)(dd2-dd1)/1000;
        } catch (Exception pe){
            second = -1;
        }

        return Double.toString(second);
    }

    /**
     * 获取某时间的前/后时间
     * @param dateStr
     * @param past
     * @return
     */
    public static String getFutureOrPast(String dateStr, int past){

        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            date = null;
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day + past);

        String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * 获取当前时间的前/后几天
     * @param past
     * @return
     */
    public static String getFutureOrPast(int past) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);

        return result;
    }
}
