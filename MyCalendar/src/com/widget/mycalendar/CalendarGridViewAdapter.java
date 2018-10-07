package com.widget.mycalendar;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycalendar.R;

/**
 * @author daij
 * @version 1.0 日历适配器
 */
public class CalendarGridViewAdapter extends BaseAdapter {

	private Resources mRes;
	/** 上下文 */
	private Context mContext;
	/** 日期实体集合 */
	private List<DateEntity> mDataList;
	/** 因为position是从0开始的，所以用当做一个中间者，用来加1.以达到判断除数时，为哪个星期 */
	private int temp;

	public CalendarGridViewAdapter(Context context, Resources res) {
		this.mContext = context;
		this.mRes = res;
	}

	/** 设置日期数据 */
	public void setDateList(List<DateEntity> dataList) {
		this.mDataList = dataList;
	}

	@Override
	public int getCount() {
		if (mDataList == null) {
			return 0;
		}
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 通过传递过来的MenuItem值给每一个item设置数据
		LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.calendar_item_layout, null);
		TextView textView = (TextView) layout
				.findViewById(R.id.calendar_item_tv_day);
		TextView tv_tip = (TextView) layout.findViewById(R.id.calendar_tip);
		if (mDataList != null) {
			textView.setText(mDataList.get(position).day + "");
			if ((TextUtils.equals(CalendarTool.SATURDAY,
					mDataList.get(position).weekDay))
					|| TextUtils.equals(CalendarTool.SUNDAY,
							mDataList.get(position).weekDay)) {
				// 周末背景为白，字体为灰色
				textView.setBackgroundColor(mRes.getColor(R.color.white));
				textView.setTextColor(mRes.getColor(R.color.weekend_day_txt));
			}// TODO 在非周末时候设置颜色
			else {
				if (((mDataList.get(position).year == DataUtil
						.getCurrentYear() && mDataList.get(position).month <= DataUtil
						.getCurrentMonth()) || (mDataList.get(position).year < DataUtil
						.getCurrentYear() && mDataList.get(position).month <= 12))
						&& mDataList.get(position).isSelfMonthDate) {// 今天以前，且日期在当月
					if (mDataList.get(position).day > 0
							&& mDataList.get(position).day <= 20
							|| mDataList.get(position).day == 25) {
						tv_tip.setBackgroundColor(mRes
								.getColor(R.color.tip_normal));
					} else if (mDataList.get(position).day == 21
							|| mDataList.get(position).day == 22) {
						tv_tip.setBackgroundColor(mRes
								.getColor(R.color.tip_leave));
					} else if (mDataList.get(position).day == 23
							|| mDataList.get(position).day == 24) {
						tv_tip.setBackgroundColor(mRes
								.getColor(R.color.tip_late));
					} else {
						tv_tip.setBackgroundColor(mRes
								.getColor(R.color.tip_normal));
						if (mDataList.get(position).month == 8
								&& mDataList.get(position).day >= 25) {
							tv_tip.setBackgroundColor(mRes
									.getColor(R.color.white));
						}
					}
				}
			}
			if (mDataList.get(position).isNowDate
					&& mDataList.get(position).isSelfMonthDate) {
				// 如果为当前号数，则设置为白色背景并，字体为蓝色
				textView.setBackgroundColor(mRes.getColor(R.color.white));
				textView.setTextColor(mRes.getColor(R.color.current_day_txt));
			}
			if (!mDataList.get(position).isSelfMonthDate) {// 是否为本月的号数，不是本月号数显示白色，及不显示
				textView.setTextColor(mRes.getColor(R.color.white));
			}
			layout.setTag(mDataList.get(position));// 把当前日历实体放入GridView 的Item中
		}

		return layout;
	}
}
