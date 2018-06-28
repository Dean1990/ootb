package com.deanlib.ootb.utils;

import java.math.BigDecimal;

/**
 * 数值计算类
 *
 * @author dean
 * @time 2018/6/28 下午3:11
 */
public class MathUtils {

    /**
     * 自减限最小值
     * @param num
     * @param x 限定最小值
     * @return
     */
    public static int decrementMinX(int num,int x){
        return num>x?--num:x;
    }
    public static int decrementMinOne(int num){
        return decrementMinX(num,1);
    }
    public static int decrementMinZero(int num){
        return decrementMinX(num,0);
    }

    /**
     * 金钱四则运算
     * @param x
     * @param y
     * @return
     */
    public static float moneyAdd(float x,float y){
        return BigDecimal.valueOf(x).add(BigDecimal.valueOf(y)).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }
    public static float moneySubtract(float x,float y){
        return BigDecimal.valueOf(x).subtract(BigDecimal.valueOf(y)).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }
    public static float moneyMultiply(float x,float y){
        return BigDecimal.valueOf(x).multiply(BigDecimal.valueOf(y)).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }
    public static float moneyDivide(float x,float y){
        return BigDecimal.valueOf(x).divide(BigDecimal.valueOf(y)).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
