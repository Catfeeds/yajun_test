package com.wis.basis.common.utils;

import java.math.BigDecimal;

public class NumberUtils {

	/**
	 * 返回满足精度的浮点数
	 * @param value 原始数据
	 * @param scale 精度
	 * @param roundingMode 截取模式
	 * @see    BigDecimal#ROUND_UP
     * @see    BigDecimal#ROUND_DOWN
     * @see    BigDecimal#ROUND_CEILING
     * @see    BigDecimal#ROUND_FLOOR
     * @see    BigDecimal#ROUND_HALF_UP
     * @see    BigDecimal#ROUND_HALF_DOWN
     * @see    BigDecimal#ROUND_HALF_EVEN
     * @see    BigDecimal#ROUND_UNNECESSARY 
	 * @return
	 */
	public static double pricise(double value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		return bd.setScale(scale, roundingMode).doubleValue();
	}
	
	/**
	 * 数字前面自动补0方法 
	 * @param sourceDate 原始数据
	 * @param formatLength 转换长度
	 * @return
	 */
	public static String frontCompWithZore(int sourceDate,int formatLength){
		return String.format("%0"+formatLength+"d", sourceDate);
	 }

}
