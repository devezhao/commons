package cn.devezhao.commons;

import java.text.DecimalFormat;

/**
 * 格式化工具
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 * @since 2011-4-10
 */
public class FormatUtils extends DateFormatUtils {

	/**
	 * 千分位格式化不带小数点
	 * 
	 * @param number
	 * @return
	 */
	public static String formatNumber(Number number) {
		return new DecimalFormat(",###").format(number);
	}
	
	/**
	 * 千分位格式化带2位数点
	 * 
	 * @param number
	 * @return
	 */
	public static String formatDecimal(Number number) {
		return formatDecimal(number, 2);
	}
	
	/**
	 * 千分位格式化带指定位数点
	 * 
	 * @param number
	 * @param scale 小数位
	 * @return
	 */
	public static String formatDecimal(Number number, int scale) {
		String format = ",##0";
		if (scale > 0) {
			format += ".";
		}
		for (int i = 0; i < scale; i++) {
			format += "0";
		}
		return new DecimalFormat(format).format(number);
	}
	
	private static final char[] S_NUM = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
	private static final char[] S_UNIT = {'分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万'};
	/**
	 * 转换为大写
	 * 
	 * @param money
	 * @return
	 */
	public static String toChineseCurrency(double money) {
		boolean isNegative = money < 0;
		if (isNegative) {
			money = Math.abs(money);
		}
		
		String str = String.valueOf(Math.round(money * 100 + 0.00001));
		String chs = "";
		
		for (int i = 0; i < str.length(); i++) {
			int n = str.charAt(str.length() - 1 - i) - '0';
			chs = S_NUM[n] + "" + S_UNIT[i] + chs;
		}

		chs = chs.replaceAll("零仟", "零");
		chs = chs.replaceAll("零佰", "零");
		chs = chs.replaceAll("零拾", "零");
		chs = chs.replaceAll("零亿", "亿");
		chs = chs.replaceAll("零万", "万");
		chs = chs.replaceAll("零元", "元");
		chs = chs.replaceAll("零角", "零");
		chs = chs.replaceAll("零分", "零");
		chs = chs.replaceAll("零零", "零");
		chs = chs.replaceAll("零亿", "亿");
		chs = chs.replaceAll("零零", "零");
		chs = chs.replaceAll("零万", "万");
		chs = chs.replaceAll("零零", "零");
		chs = chs.replaceAll("零元", "元");
		chs = chs.replaceAll("亿万", "亿");
		chs = chs.replaceAll("零$", "");
		chs = chs.replaceAll("元$", "元整");

		return isNegative ? "负" + chs : chs;
	}
}
