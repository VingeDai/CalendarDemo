package com.widget.mycalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

/**
 * @author daij
 * @version 1.0 获取日历数据工具类
 */
public class CalendarTool {
	public static final String MONDAY = "周一";
	public static final String TUESDAY = "周二";
	public static final String WEDNESDAY = "周三";
	public static final String THURSDAY = "周四";
	public static final String FRIDAY = "周五";
	public static final String SATURDAY = "周六";
	public static final String SUNDAY = "周日";
	public static final String[] weekDayRow = { SUNDAY, MONDAY, TUESDAY,
			WEDNESDAY, THURSDAY, FRIDAY, SATURDAY };

	private List<DateEntity> mDataList = new ArrayList<DateEntity>();
	private DateEntity mDateEntity;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mDays;

	/** 系统当前年月日 */
	private int mCurrenYear;
	private int mCurrenMonth;
	private int mCurrenDay;
	private Context mContext;
	/** 用于算法的变量 */
	/** 已过去的年份总天数 */
	int mGoneYearDays = 0;
	/** 本年包括今天的过去天数 */
	int thisYearDays = 0;
	/** 是否为闰年 */
	boolean isLeapYear = false;
	/** 平年月天数数组 */
	int commonYearMonthDay[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	/** 闰年月天数数组 */
	int leapYearMonthDay[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public CalendarTool(Context context) {
		this.mContext = context;
		initNowDate();
	}

	/** 获取当前日历的年月 x为年，y为月 */
	public Point getNowCalendar() {
		Point p = new Point(mYear, mMonth);
		return p;
	}

	/** 通过年月获取日期实体集合 集合大小为7*6＝42，星期为7，6行以显示足够数量日期 */
	public List<DateEntity> getDateEntityList(int year, int month) {
		mDataList.clear();
		int dayOfNowCalendar = 42;// 当前日历板的日期总数 7*6个数
		int dayOfWeek = 0;// 得到当前年月的每一天为星期几
		int selfDaysEndWeek = 0;// 本月的最后一天是星期几
		int startDate = 0;// 当前月的上一个月在本日历的开始日期
		int days = 0;// 得到本月的总共天数
		/** 修改部分 */
		int endDate = 0;// 得到上一个月的天数，作为上一个月在本日历的结束日期
		if ((year - 1) == this.mYear || month == 1) {// 说明向前翻了一年，那么上个月的天数就应该是上一年的12月的天数，或者到翻到一月份的时候，那么上一个月的天数也是上一年的12月份的天数
			endDate = this.getDays(year - 1, 12);
		} else {// 得到上一个月的天数，作为上一个月在本日历的结束日期
			endDate = this.getDays(year, month - 1);
		}
		/** 修改部分结束 */
		this.mYear = year;// 当前日历上显示的年
		this.mMonth = month;// 当前日历上显示的月
		int previoursMonthDays = 0;// 上一个月在本月显示的天数
		int nextMonthDays = 0;// 下一个月在本月显示的天数
		days = this.getDays(year, month);
		dayOfWeek = this.getWeekDay(year, month);
		if (dayOfWeek == 0) {
			startDate = endDate - 7 + 1;
		} else {
			startDate = endDate - dayOfWeek + 1;
		}
		previoursMonthDays = endDate - startDate + 1;
		nextMonthDays = dayOfNowCalendar - days - previoursMonthDays;
		/** 先添加前面不属于本月的 */
		for (int i = startDate, j = 0; i <= endDate; i++, j++) {
			mDateEntity = new DateEntity();
			mDateEntity.day = i;
			mDateEntity.isSelfMonthDate = false;
			mDateEntity.year = year;
			mDateEntity.month = month - 1;
			mDateEntity.weekDay = weekDayRow[j];
			mDataList.add(mDateEntity);
		}
		/** 添加本月的 */
		for (int i = 1, j = dayOfWeek; i <= days; i++, j++) {
			mDateEntity = new DateEntity();
			mDateEntity.day = i;
			mDateEntity.isSelfMonthDate = true;
			mDateEntity.year = year;
			mDateEntity.month = month;
			if (j >= 7) {
				j = 0;
			}
			selfDaysEndWeek = j;
			mDateEntity.weekDay = weekDayRow[j];
			if ((year == mCurrenYear) && (month == mCurrenMonth)
					&& i == mCurrenDay) {
				mDateEntity.isNowDate = true;
			}
			mDataList.add(mDateEntity);
		}
		/*** 添加后面下一个月的 */
		for (int i = 1, j = selfDaysEndWeek + 1; i <= nextMonthDays; i++, j++) {
			mDateEntity = new DateEntity();
			mDateEntity.day = i;
			mDateEntity.isSelfMonthDate = false;
			mDateEntity.year = year;
			mDateEntity.month = month + 1;
			if (j >= 7) {
				j = 0;
			}
			mDateEntity.weekDay = weekDayRow[j];
			mDataList.add(mDateEntity);
		}
		return mDataList;
	}

	/** 通过年月，获取这个月一共有多少天 */
	public int getDays(int year, int month) {
		int days = 0;

		if ((year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0)) {
			if (month > 0 && month <= 12) {
				days = leapYearMonthDay[month - 1];
			}
		} else {
			if (month > 0 && month <= 12) {
				days = commonYearMonthDay[month - 1];
			}
		}
		return days;
	}

	/** 获取星期的排列 */
	public String[] getWeekDayRow() {
		return weekDayRow;
	}

	/** 初始化当前系统的日期 */
	public void initNowDate() {
		Date date = new Date();
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-M-d");
		String currentDate = simpleFormat.format(date); // 当期日期
		mCurrenYear = Integer.parseInt(currentDate.split("-")[0]);
		mCurrenMonth = Integer.parseInt(currentDate.split("-")[1]);
		mCurrenDay = Integer.parseInt(currentDate.split("-")[2]);
		this.mYear = mCurrenYear;
		this.mMonth = mCurrenMonth;
	}

	/** 通过年，月获取当前月的第一天1日为星期几 ，返回0是星期天，1是星期一，依次类推 */
	public int getWeekDay(int year, int month) {
		int dayOfWeek;
		int goneYearDays = 0;
		int thisYearDays = 0;
		boolean isLeapYear = false;
		int commonYearMonthDay[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
				30, 31 };
		int leapYearMonthDay[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30,
				31 };
		for (int i = 1900; i < year; i++) {// 从1900年开始算起，1900年1月1日为星期一
			if ((i % 4 == 0 && (i % 100 != 0)) || (i % 400 == 0)) {
				goneYearDays = goneYearDays + 366;
			} else {
				goneYearDays = goneYearDays + 365;
			}
		}
		if ((year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0)) {
			isLeapYear = true;
			for (int i = 0; i < month - 1; i++) {
				thisYearDays = thisYearDays + leapYearMonthDay[i];
			}
		} else {
			isLeapYear = false;
			for (int i = 0; i < month - 1; i++) {
				thisYearDays = thisYearDays + commonYearMonthDay[i];
			}
		}
		dayOfWeek = (goneYearDays + thisYearDays + 1) % 7;
		Log.d(this.getClass().getName(), "从1990到现在有"
				+ (goneYearDays + thisYearDays + 1) + "天");
		Log.d(this.getClass().getName(), year + "年" + month + "月" + 1 + "日是星期"
				+ dayOfWeek);
		return dayOfWeek;
	}
}
