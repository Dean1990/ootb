package com.deanlib.ootb.utils;


import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 格式化类
 * 区别于TextUtils更偏向于文字操作和编码相关
 * <p>
 * DATE:时间
 * NUM:数值
 * <p>
 * Created by dean on 16/5/10.
 */
public class FormatUtils {

    /**
     * format:yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * format:yyyy-MM-dd HH:mm
     */
    public static final String DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

    /**
     * format:yyyy-MM-dd
     */
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

    /**
     * format:MM-dd
     */
    public static final String DATE_FORMAT_MD = "MM-dd";


    /**
     * 时间戳转字符串
     *
     * @param timestamp 时间戳
     * @param format    格式
     * @return 指定格式的字符串
     */
    public static String convertDateTimestampToString(long timestamp, String format) {

        Date date = new Date(timestamp);

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);

    }

    /**
     * 时间戳转带描述的字符串
     *
     * @param timestamp 时间戳
     * @return 小于6小时有文字描述，否则 "yyyy-MM-dd HH:mm:ss" 格式的字符串
     */
    public static String convertDateTimestampToDescribeString(long timestamp) {
        return convertDateTimestampToDescribeString(timestamp, DATE_FORMAT_YMDHMS);
    }


    /**
     * 时间戳转带描述的字符串
     *
     * @param timestamp 时间戳
     * @param format    格式
     * @return 小于6小时有文字描述，否则指定格式的字符串
     */
    public static String convertDateTimestampToDescribeString(long timestamp, String format) {

        long l = System.currentTimeMillis() - timestamp;

        if (l < 1000 * 60)
            return "刚刚";
        else if (l < 1000 * 60 * 2)
            return "1分钟前";
        else if (l < 1000 * 60 * 3)
            return "2分钟前";
        else if (l < 1000 * 60 * 4)
            return "3分钟前";
        else if (l < 1000 * 60 * 5)
            return "4分钟前";
        else if (l < 1000 * 60 * 10)
            return "5分钟前";
        else if (l < 1000 * 60 * 20)
            return "10分钟前";
        else if (l < 1000 * 60 * 30)
            return "20分钟前";
        else if (l < 1000 * 60 * 60)
            return "30分钟前";
        else if (l < 1000 * 60 * 60 * 2)
            return "1小时前";
        else if (l < 1000 * 60 * 60 * 3)
            return "2小时前";
        else if (l < 1000 * 60 * 60 * 4)
            return "3小时前";
        else if (l < 1000 * 60 * 60 * 5)
            return "4小时前";
        else if (l < 1000 * 60 * 60 * 6)
            return "5小时前";
        else if (l < 1000 * 60 * 60 * 7)
            return "6小时前";
        else
            return convertDateTimestampToString(timestamp, format);


    }

    /**
     * 字符串转时间戳
     * @param string 字符串
     * @param format 格式
     * @return 时间戳
     */
    public static long convertDateStringToTimestamp(String string, String format){

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            Date date = sdf.parse(string);

            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 格式化数值转带单位的字符串
     *
     * @param num 数值
     * @return 大于1000时带单位
     */
    public static String formatNum(long num) {

        if (num < 1000)
            return num + "";
        else if (num < 10000)
            return (num / 1000) + "千";
        else if (num < 100000000)
            return (num / 10000) + "万";
        else
            return (num / 100000000) + "亿";


    }

    /**
     * 毫秒转时长
     *
     * @param l 毫秒
     * @return h:m:s
     */
    public static String convertNumLongToDuration(long l) {

        l = l/1000;

        String h = addZero((int) (l / 3600));

        String m = addZero((int)(l % 3600 / 60));

        String s = addZero((int) (l % 3600 % 60));

        if ("00".equals(h)) {
            return h + ":" + m + ":" + s;
        }
        return m + ":" + s;
    }

    private static String addZero(int h){

        return h<10?"0"+h:h+"";
    }

    /**
     * 格式化计算机容量单位
     * 可参考
     * @see android.text.format.Formatter#formatFileSize(Context, long)
     *
     * @param size 单位byte
     * @return
     */
    public static String formatFileSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 格式化重量单位
     * @param weight 单位g
     * @return
     */
    public static String formatWeight(float weight){
        //按g换算
        if (weight >= 1000) {
            return (weight/1000)+"kg";
        }else {
            return weight+"g";
        }
    }

    /**
     * 格式化人民币
     * @param money
     * @return
     */
    public static String formatRMB(float money){
        BigDecimal decimal = new BigDecimal(money);
        return formatRMB(decimal.setScale(2,BigDecimal.ROUND_DOWN).toString());
    }

    public static String formatRMB(String money){
        return "¥ " + money;
    }

    /**
     * 隐藏手机号中间部分
     * @param num
     * @return
     */
    public static String hidePhoneNum(String num){

        if (ValidateUtils.isMobileNum(num)) {

            return num.substring(0,3)+"****"+num.substring(num.length()-4);
        }else return num;
    }

    /**
     * 格式 删除线
     * @param str
     * @return
     */
    public static SpannableString formatStrike(String str){
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new StrikethroughSpan(),0,str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 字符串转数值
     * @param str
     * @return
     */
    public static float convertStringToNum(String str){
        if (str!=null) {
            if (Pattern.matches("\\d+\\.?\\d*", str)) {
                return Float.parseFloat(str);
            }
        }
        return 0;
    }

}