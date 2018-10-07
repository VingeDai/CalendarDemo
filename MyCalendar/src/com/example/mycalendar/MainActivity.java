package com.example.mycalendar;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.widget.mycalendar.CalendarGridView;
import com.widget.mycalendar.CalendarGridViewAdapter;
import com.widget.mycalendar.CalendarTool;
import com.widget.mycalendar.DateEntity;
import com.widget.mycalendar.DataUtil;

/**
 * @author daij
 * @version 1.0 日历：考勤记录
 */
public class MainActivity extends Activity {
	private CalendarGridViewAdapter mAdapter;
	private CalendarTool mCalendarTool;
	private CalendarGridView mGridView;
	private List<DateEntity> mDateEntityList;
	private Point mNowCalendarPoint;
	private ImageView mPrevioursIv;
	private ImageView mNextIv;
	private ImageView ivBack;
	private TextView mCalendarTv;
	private TextView tvDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);

		initView();// 初始化View
		setListeners();
		// requestXmas();//请求数据
	}

	/** 初始化view */
	public void initView() {
		mPrevioursIv = (ImageView) findViewById(R.id.calendar_bar_iv_previours);
		mNextIv = (ImageView) findViewById(R.id.calendar_bar_iv_next);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		mCalendarTv = (TextView) findViewById(R.id.calendar_bar_tv_date);
		tvDetail = (TextView) findViewById(R.id.tv_detail);

		mGridView = (CalendarGridView) findViewById(R.id.calendar_gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGridView.setOnItemClickListener(new CalendarItemClickListener());

		mPrevioursIv.setOnClickListener(new ImageViewClickListener());
		mNextIv.setOnClickListener(new ImageViewClickListener());
		mCalendarTool = new CalendarTool(this);
		mNowCalendarPoint = mCalendarTool.getNowCalendar();
		mCalendarTv.setText(mNowCalendarPoint.x + "年" + mNowCalendarPoint.y
				+ "月");
		mDateEntityList = mCalendarTool.getDateEntityList(mNowCalendarPoint.x,
				mNowCalendarPoint.y);
		mAdapter = new CalendarGridViewAdapter(this, getResources());
		mAdapter.setDateList(mDateEntityList);
		mGridView.setAdapter(mAdapter);
	}

	/** 日历监听类 */
	class CalendarItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO 模拟数据
			DateEntity itemDate = (DateEntity) view.getTag();

			if (!((TextUtils.equals(CalendarTool.SATURDAY, itemDate.weekDay)) || TextUtils
					.equals(CalendarTool.SUNDAY, itemDate.weekDay))) {// 非周末
				if (((itemDate.year == DataUtil.getCurrentYear() && itemDate.month <= DataUtil
						.getCurrentMonth()) || (itemDate.year < DataUtil
						.getCurrentYear() && itemDate.month <= 12))
						&& itemDate.isSelfMonthDate) {// 今天以前，且日期在当月
					if (itemDate.day > 0 && itemDate.day <= 20
							|| itemDate.day == 25) {// 正常出勤
						tvDetail.setText("备注：" + "\n" + "        "
								+ itemDate.year + "年" + itemDate.month + "月"
								+ itemDate.day + "日" + "--" + itemDate.weekDay
								+ "\n" + "正常出勤。");
					} else if (itemDate.day == 21 || itemDate.day == 22) {// 请假
						tvDetail.setText("备注：" + "\n" + "        "
								+ itemDate.year + "年" + itemDate.month + "月"
								+ itemDate.day + "日" + "--" + itemDate.weekDay
								+ "\n" + "请假两天，从" + itemDate.month + "月21日到"
								+ itemDate.month + "月22日");
					} else if (itemDate.day == 23 || itemDate.day == 24) {// 迟到、早退
						if (itemDate.day == 23) {

							tvDetail.setText("备注：" + "\n" + "        "
									+ itemDate.year + "年" + itemDate.month
									+ "月" + itemDate.day + "日" + "--"
									+ itemDate.weekDay + "\n"
									+ "上午8：36打卡，下午5：40打卡" + "\n" + "上午迟到6分钟");
						} else if (itemDate.day == 24) {

							tvDetail.setText("备注：" + "\n" + "        "
									+ itemDate.year + "年" + itemDate.month
									+ "月" + itemDate.day + "日" + "--"
									+ itemDate.weekDay + "\n"
									+ "上午8：25打卡，下午5：29打卡" + "\n" + "下午早退1分钟");
						}
					} else {
						if (itemDate.month == 8 && itemDate.day > 25) {
							return;
						}
						tvDetail.setText("备注：" + "\n" + "        "
								+ itemDate.year + "年" + itemDate.month + "月"
								+ itemDate.day + "日" + "--" + itemDate.weekDay
								+ "\n" + "正常出勤。");
					}
				}
			}

			// Toast.makeText(
			// RecordCalendar.this,
			// "选中的是" + itemDate.year + "年" + itemDate.month + "月"
			// + itemDate.day + "日" + "--" + itemDate.weekDay,
			// Toast.LENGTH_SHORT).show();
		}
	}

	/** 按钮 */
	class ImageViewClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.calendar_bar_iv_previours:// 上月
				mDateEntityList.clear();
				mNowCalendarPoint = mCalendarTool.getNowCalendar();
				if (mNowCalendarPoint.x >= 1990 && mNowCalendarPoint.x < 2038) {
					if (mNowCalendarPoint.y - 1 <= 0) {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x - 1, 12);
					} else {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x, mNowCalendarPoint.y - 1);
					}

					mAdapter.setDateList(mDateEntityList);
					mAdapter.notifyDataSetChanged();
					mNowCalendarPoint = mCalendarTool.getNowCalendar();
					mCalendarTv.setText(mNowCalendarPoint.x + "年"
							+ mNowCalendarPoint.y + "月");
				}

				break;
			case R.id.calendar_bar_iv_next:// 下月
				mNowCalendarPoint = mCalendarTool.getNowCalendar();
				mDateEntityList.clear();
				if (mNowCalendarPoint.x >= 1990 && mNowCalendarPoint.x < 2038) {
					if (mNowCalendarPoint.y + 1 > 12) {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x + 1, 1);
					} else {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x, mNowCalendarPoint.y + 1);
					}
					mAdapter.setDateList(mDateEntityList);
					mAdapter.notifyDataSetChanged();
					mNowCalendarPoint = mCalendarTool.getNowCalendar();
					mCalendarTv.setText(mNowCalendarPoint.x + "年"
							+ mNowCalendarPoint.y + "月");
				}
				break;
			}
		}

	}

	private void setListeners() {
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.this.finish();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
