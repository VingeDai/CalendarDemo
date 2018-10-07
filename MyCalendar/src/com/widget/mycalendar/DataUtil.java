package com.widget.mycalendar;

import java.util.Calendar;

/**
 * @author daij
 * @version 1.0 日期处理
 */
public class DataUtil {
	static Calendar c = Calendar.getInstance();

	/**
	 * 获取当前年
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月
	 */
	public static int getCurrentMonth() {
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前天
	 */
	public static int getCurrentDay() {
		return c.get(Calendar.DAY_OF_MONTH);
	}
}
