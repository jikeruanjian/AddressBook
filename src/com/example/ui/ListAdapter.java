package com.example.ui;

import java.util.HashMap;
import java.util.List;

import com.example.test.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListAdapter extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected List datas;
	protected Context context;
	public HashMap<Integer, View> views = new HashMap<Integer, View>();

	public ListAdapter(Context context, List datas) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
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
