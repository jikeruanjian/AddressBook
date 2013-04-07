package com.kevin.addressBook.bll;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

public class PicData {
	private static AbstractMap<String, Object> mData = new ConcurrentHashMap<String, Object>();
	private static Queue<String> keyQueue = new LinkedList<String>();

	private PicData() {

	}

	public static void putData(String key, Object obj) {
		keyQueue.offer(key);
		mData.put(key, obj);
	}

	public static Object getData(String key) {
		return mData.get(key);
	}

	public static boolean isContainsKey(String key) {
		return mData.containsKey(key);
	}

	public static void clearData() {
		if (mData.size() > 20) {
			String key = keyQueue.poll();
			if (mData.containsKey(key))
				mData.remove(key);
			System.out.println("清空图片集合");
			Log.i("ddd", "清空图片集合");
		}
	}
}
