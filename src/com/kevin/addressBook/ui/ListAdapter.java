package com.kevin.addressBook.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kevin.addressBook.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListAdapter extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected List datas = new ArrayList();
	protected Context context;

	public ListAdapter(Context context, List datas) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

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
}
