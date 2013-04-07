package com.kevin.addressBook.bll;

import java.util.List;

import com.kevin.addressBook.dao.AddressInfoDao;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.model.Const;

public class AddressInfoBll {

	public static AddressInfoDao ai = new AddressInfoDao();
	private static int dataCount; // 当前的筛选条件下的数据总条数，只读

	public static int getDataCount() {
		return dataCount;
	}

	private static int pageSize = 20;
	private static int currentPageNum = 1; // 当前的页码
	private static List<AddressInfo> dataList; // 当前的数据集

	public static List<AddressInfo> getDataList() {
		return dataList;
	}

	public static String searchName = ""; // 当前的筛选条件

	public static String getSearchName() {
		return searchName;
	}

	// 更改筛选条件，并加载第一页内容
	public static void setSearchName(String searchName) {
		AddressInfoBll.searchName = searchName;
		// 搜索添加发生改变的时候，所有变量都得重置
		AddressInfoBll.dataCount = ai.getCount(searchName);
		AddressInfoBll.currentPageNum = 1;
		setDataList();
	}

	// 获取当前筛选条件下的下一页的内容
	public static void nextPageData() {
		AddressInfoBll.currentPageNum += 1;
		AddressInfoBll.dataList.addAll(ai.getListByPageNum(currentPageNum,
				pageSize, Const.AddressInfo.NAME + " like '%" + searchName
						+ "%'", null));
	}

	/**
	 * 刷新一下数据
	 */
	public static void refreshData() {
		getTopDataList();
	}

	/**
	 * 获取当前页的数据
	 */
	private static void setDataList() {
		AddressInfoBll.dataList = ai.getListByPageNum(currentPageNum, pageSize,
				Const.AddressInfo.NAME + " like '%" + searchName + "%'", null);
	}

	/**
	 * 获取当前加载了的所有数据
	 */
	private static void getTopDataList() {
		AddressInfoBll.dataList = ai.getTopDataList(currentPageNum, pageSize,
				Const.AddressInfo.NAME + " like '%" + searchName + "%'", null);
	}
}
