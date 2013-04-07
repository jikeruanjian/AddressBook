package com.kevin.addressBook.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kevin.addressBook.bll.AddressInfoBll;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.model.Const;
import android.content.ContentValues;
import android.database.Cursor;

public class AddressInfoDao extends BaseDao {

	/**
	 * 插入一条数据
	 * 
	 * @param entity
	 * @return
	 */
	public boolean addUser(AddressInfo entity) {
		ContentValues cv = new ContentValues();
		cv.put(Const.AddressInfo.ID, UUID.randomUUID().toString());
		cv.put(Const.AddressInfo.NAME, entity.getName());
		cv.put(Const.AddressInfo.ADDRESS, entity.getAddress());
		cv.put(Const.AddressInfo.COMPANY, entity.getCompany());
		cv.put(Const.AddressInfo.EMAIL, entity.getEmail());
		cv.put(Const.AddressInfo.IMAGENAME, entity.getImageName());
		cv.put(Const.AddressInfo.PHONENUM, entity.getPhoneNum());
		cv.put(Const.AddressInfo.POST, entity.getPost());
		cv.put(Const.AddressInfo.PURCHASEINFO, entity.getPurchaseInfo());
		cv.put(Const.AddressInfo.QQ, entity.getQq());
		cv.put(Const.AddressInfo.SALEINFO, entity.getSaleInfo());
		cv.put(Const.AddressInfo.WEBSITE, entity.getWebSite());

		return insert(Const.AddressInfo.ROWNAME, cv);
	}

	public Boolean deleteUser(String id) {
		return delete(Const.AddressInfo.ROWNAME, Const.AddressInfo.ID + "=?",
				new String[] { id }) == 1;
	}

	/**
	 * 更新实体
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Boolean editUser(AddressInfo entity) throws Exception {
		if (entity.getId() == null || entity.getId().equals("")) {
			throw new Exception("未指定要修改信息的ID");
		} else {
			ContentValues cv = new ContentValues();
			cv.put(Const.AddressInfo.NAME, entity.getName());
			cv.put(Const.AddressInfo.ADDRESS, entity.getAddress());
			cv.put(Const.AddressInfo.COMPANY, entity.getCompany());
			cv.put(Const.AddressInfo.EMAIL, entity.getEmail());
			cv.put(Const.AddressInfo.IMAGENAME, entity.getImageName());
			cv.put(Const.AddressInfo.PHONENUM, entity.getPhoneNum());
			cv.put(Const.AddressInfo.POST, entity.getPost());
			cv.put(Const.AddressInfo.PURCHASEINFO, entity.getPurchaseInfo());
			cv.put(Const.AddressInfo.QQ, entity.getQq());
			cv.put(Const.AddressInfo.SALEINFO, entity.getSaleInfo());
			cv.put(Const.AddressInfo.WEBSITE, entity.getWebSite());
			return update(Const.AddressInfo.ROWNAME, cv, Const.AddressInfo.ID
					+ "=?", new String[] { entity.getId() }) > 0;
		}
	}

	/**
	 * 获取详细信息
	 * 
	 * @param id
	 * @return
	 */
	public AddressInfo getUserDetails(String id) {
		List<AddressInfo> result = getList(Const.AddressInfo.ID + "=?",
				new String[] { id });
		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	/**
	 * 按照条件返回相印的 List
	 * 
	 * @param whereClause
	 *            eg: id=?,name=?
	 * @param whereArgs
	 * @return
	 */
	public List<AddressInfo> getList(String whereClause, String[] whereArgs) {
		String sql = "select * from " + Const.AddressInfo.ROWNAME;
		if (!whereClause.equals("")) {
			sql += " where " + whereClause;
		}
		sql += "order by name";
		List<AddressInfo> listAddressInfo = new ArrayList<AddressInfo>();
		Cursor cursor = getCursorQuery(sql, whereArgs);
		AddressInfo dp = null;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				dp = new AddressInfo();
				dp.setId(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.ID)));
				dp.setName(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.NAME)));
				dp.setAddress(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.ADDRESS)));
				dp.setCompany(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.COMPANY)));
				dp.setEmail(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.EMAIL)));
				dp.setImageName(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.IMAGENAME)));
				dp.setPhoneNum(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.PHONENUM)));
				dp.setPost(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.POST)));
				dp.setPurchaseInfo(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.PURCHASEINFO)));
				dp.setQq(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.QQ)));
				dp.setSaleInfo(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.SALEINFO)));
				dp.setWebSite(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.WEBSITE)));
				listAddressInfo.add(dp);
			}
			cursor.close();
			closeDatabase();
		}
		return listAddressInfo;
	}

	/**
	 * 按照条件返回相印的 List
	 * 
	 * @param whereClause
	 *            eg: id=?,name=?
	 * @param whereArgs
	 * @return
	 */
	public List<AddressInfo> getListByPageNum(int pageNum, int pageSize,
			String whereClause, String[] whereArgs) {
		String sql = "select * from " + Const.AddressInfo.ROWNAME;
		if (!whereClause.equals("")) {
			sql += " where " + whereClause;
		}
		sql += " order by name limit " + ((pageNum - 1) * pageSize - 1) + ","
				+ pageSize;
		List<AddressInfo> listAddressInfo = new ArrayList<AddressInfo>();
		Cursor cursor = getCursorQuery(sql, whereArgs);
		AddressInfo dp = null;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				dp = new AddressInfo();
				dp.setId(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.ID)));
				dp.setName(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.NAME)));
				dp.setAddress(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.ADDRESS)));
				dp.setCompany(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.COMPANY)));
				dp.setEmail(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.EMAIL)));
				dp.setImageName(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.IMAGENAME)));
				dp.setPhoneNum(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.PHONENUM)));
				dp.setPost(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.POST)));
				dp.setPurchaseInfo(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.PURCHASEINFO)));
				dp.setQq(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.QQ)));
				dp.setSaleInfo(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.SALEINFO)));
				dp.setWebSite(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.WEBSITE)));
				listAddressInfo.add(dp);
			}
			cursor.close();
			closeDatabase();
		}
		return listAddressInfo;
	}

	/**
	 * 按照条件返回相印的 List
	 * 
	 * @param whereClause
	 *            eg: id=?,name=?
	 * @param whereArgs
	 * @return
	 */
	public List<AddressInfo> getTopDataList(int pageNum, int pageSize,
			String whereClause, String[] whereArgs) {
		String sql = "select * from " + Const.AddressInfo.ROWNAME;
		if (!whereClause.equals("")) {
			sql += " where " + whereClause;
		}
		sql += " order by name limit 0," + pageNum * pageSize;
		List<AddressInfo> listAddressInfo = new ArrayList<AddressInfo>();
		Cursor cursor = getCursorQuery(sql, whereArgs);
		AddressInfo dp = null;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				dp = new AddressInfo();
				dp.setId(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.ID)));
				dp.setName(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.NAME)));
				dp.setAddress(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.ADDRESS)));
				dp.setCompany(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.COMPANY)));
				dp.setEmail(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.EMAIL)));
				dp.setImageName(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.IMAGENAME)));
				dp.setPhoneNum(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.PHONENUM)));
				dp.setPost(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.POST)));
				dp.setPurchaseInfo(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.PURCHASEINFO)));
				dp.setQq(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.QQ)));
				dp.setSaleInfo(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.SALEINFO)));
				dp.setWebSite(cursor.getString(cursor
						.getColumnIndex(Const.AddressInfo.WEBSITE)));
				listAddressInfo.add(dp);
			}
			cursor.close();
			closeDatabase();
		}
		return listAddressInfo;
	}

	/**
	 * 获取条目总数
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int getCount(String key) {
		int count = 0;
		String sql = "select count(*) c from " + Const.AddressInfo.ROWNAME;
		if (key != null && !key.equals("")) {
			sql += " where " + Const.AddressInfo.NAME + " like '%" + key + "%'";
		}
		Cursor cursor = getCursorQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
		}
		return count;
	}
}
