package com.deanlib.ootb.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证相关
 *
 * Created by dean on 2017/4/24.
 */

public class ValidateUtils {

    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 验证手机号码
     *
     * @param mobileNum
     * @return
     */
    public static boolean isMobileNum(String mobileNum) {

        if (mobileNum.length() > 11) {
            return false;
        }

        boolean result = false;

        try {

            Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");

            Matcher m = p.matcher(mobileNum);

            result = m.matches();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;

    }

    /**
     * 验证是否是完整的网址
     *
     * @param url
     * @return
     */
    public static boolean isHttpURL(String url) {

        String str = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(url);

        return m.matches();

    }

    /**
     * 验证是否是整数
     * 包括正整数，负整数和0
     * @param num
     * @return
     */
    public static boolean isInteger(String num){

        return Pattern.matches("[+-]?\\d+",num);

    }

    /**
     * 验证是否是小数
     * @param num
     * @return
     */
    public static boolean isDecimals(String num){

        return Pattern.matches("(([+-]?\\d+)|(\\d*))\\.\\d+",num);
    }

    /**
     * 验证是否是身份证
     * @param num
     * @return
     */
    public static boolean isIDCard(String num){

        return Pattern.matches("\\d{10}((0[1-9])|(1[0-2]))((0[1-9]|([1-2][0-9]))|(3[0-1]))\\d{3}[\\dXx]",num);
    }

    /**
     * 验证是否是金钱金额
     * @param num
     * @return
     */
    public static boolean isMoney(String num){

        return Pattern.matches("\\d+(\\.\\d{1,2})?",num);
    }


}
