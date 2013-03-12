package com.kevin.addressBook.ui;

import com.kevin.addressBook.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity
 * 
 * @author zhudameng
 * 
 */
@SuppressLint("ResourceAsColor")
public class BaseActivity extends Activity {
	protected Context context;
	protected ProgressDialog progressDialog = null;
	protected Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	protected void setListViewHeight(ListView lv) {
		ListAdapter la = lv.getAdapter();
		if (null == la) {
			return;
		}
		int h = 0;
		final int cnt = la.getCount();
		for (int i = 0; i < cnt; i++) {
			View item = la.getView(i, null, lv);
			item.measure(0, 0);
			h += item.getMeasuredHeight();
		}
		ViewGroup.LayoutParams lp = lv.getLayoutParams();
		lp.height = h + (lv.getDividerHeight() * (cnt - 1));
		lv.setLayoutParams(lp);
	}

	/**
	 * 璁剧疆鍦嗚椋庢牸listview鐨勯?涓潯
	 * 
	 * @param position
	 * @param count
	 * @param view
	 */
	public void setIOSListItemBg(int position, int count, View view) {
		if (count > 1) {
			if (position == 0) {
				view.setBackgroundResource(R.drawable.item_bg_selector_head);
			} else if (position == count - 1) {
				view.setBackgroundResource(R.drawable.item_bg_selector_foot);
			} else {
				view.setBackgroundResource(R.drawable.item_bg_selector_middle);
			}
		} else {
			view.setBackgroundResource(R.drawable.item_bg_selector);
		}
	}

	public void showProgressDialog(String text) {

		if (progressDialog == null) {
			try {
				progressDialog = ProgressDialog.show(context, "请稍等...",
						"正在登录...", true);
				progressDialog.setContentView(R.layout.custom_progress);
				// progressDialog.setCancelable(true);
				progressDialog.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface arg0, int arg1,
							KeyEvent arg2) {
						if (arg1 == KeyEvent.KEYCODE_BACK
								&& arg2.getRepeatCount() == 0
								&& arg2.getAction() == KeyEvent.ACTION_UP) {
							new AlertDialog.Builder(BaseActivity.this)
									.setTitle("警告")
									.setMessage("处理尚未完成，现在停止可能影响到程序的正常使用，是否停止？")
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													hideProgressDialog();
												}
											})
									.setNegativeButton(
											"取消",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													return;
												}
											}).show();
						}
						return true;
					}

				});
				View v = progressDialog.getWindow().getDecorView();
				if (text == null) {
					text = "请稍等...";
				}
				setProgressText(v, text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setProgressText(String text) {
		View v = progressDialog.getWindow().getDecorView();
		if (text == null) {
			text = "请稍等...";
		}
		setProgressText(v, text);
	}

	private void setProgressText(View v, String text) {

		if (v instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) v;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = parent.getChildAt(i);
				setProgressText(child, text);
			}
		} else if (v instanceof TextView) {
			LayoutParams params = v.getLayoutParams();
			params.width = 300;
			v.setLayoutParams(params);
			((TextView) v).setWidth(300);
			((TextView) v).setTextSize(18);
			((TextView) v).setText(text);
			((TextView) v).setTextColor(Color.WHITE);
		}
	}

	protected void hideProgressDialog() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace(); // 当父界面已经消失，在来dismiss就会出错，这里直接吞掉就行了
			}
			progressDialog = null;
		}
	}

	// 控制在横竖屏切换的时候出错
	@Override
	protected void onPause() {
		super.onPause();
		if ((progressDialog != null) && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}
