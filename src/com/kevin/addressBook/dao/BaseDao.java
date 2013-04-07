package com.kevin.addressBook.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.kevin.addressBook.model.Const;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BaseDao {
	private SQLiteDatabase database;

	private String dbName = Const.dataBaseUrl;// 数据库名

	public BaseDao() {
	}

	/**
	 * 断开链接
	 */
	public void closeDatabase() {

		if (database != null) {
			database.close();
		}
	}

	/**
	 * 获取数据链接
	 * 
	 * @param dbName
	 * @return
	 */
	private SQLiteDatabase getDatabase(String dbName) {
		if (database == null || !database.isOpen()) {
			database = SQLiteDatabase.openOrCreateDatabase(dbName, null);
		}
		return database;
	}

	/**
	 * 获取cursor
	 * 
	 * @param sql
	 * @return
	 */
	public Cursor getCursorQuery(String sql, String[] selectStrings) {
		return getDatabase(dbName).rawQuery(sql, selectStrings);
	}

	/**
	 * 插入数据
	 * 
	 * @param model
	 * @return
	 */
	public boolean insert(String tbName, ContentValues values) {
		long result = 0;
		List<String> removeKeys = new ArrayList<String>();
		Set<Entry<String, Object>> set = values.valueSet();
		for (Entry<String, Object> entry : set) {
			if (entry.getValue() == null) {
				// values.remove(entry.getKey());
				removeKeys.add(entry.getKey());
			}
		}
		if (removeKeys.size() > 0) {
			for (String key : removeKeys) {
				values.remove(key);
			}
		}
		result = getDatabase(dbName).insert(tbName, null, values);
		closeDatabase();
		return result != -1;
	}

	/**
	 * 删除数据
	 * 
	 * @param model
	 * @return 受影响的行数
	 */
	public int delete(String tbName, String whereClause, String[] whereArgs) {
		int result = getDatabase(dbName).delete(tbName, whereClause, whereArgs);
		closeDatabase();
		return result;
	}

	/**
	 * 删除所有数据
	 * 
	 * @return
	 */
	public int deleteAll(String tbName) {
		int result = getDatabase(dbName).delete(tbName, null, null);
		closeDatabase();
		return result;
	}

	/**
	 * 修改数据
	 * 
	 * @param model
	 * @return
	 */
	public int update(String tbName, ContentValues values, String whereClause,
			String[] whrereArgs) {
		int result = getDatabase(dbName).update(tbName, values, whereClause,
				whrereArgs);
		closeDatabase();
		return result;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param dbName
	 * @param tbName
	 * @param sql
	 * @return
	 */
	public boolean excuteSQL(String sql) {
		getDatabase(dbName).execSQL(sql);
		closeDatabase();
		return true;
	}

	public boolean excuteSQL(String sql, Object[] bindArgs) {
		this.getDatabase(dbName).execSQL(sql, bindArgs);
		closeDatabase();
		return true;
	}

	/**
	 * 获取标识列的最大值
	 * 
	 * @param tbName
	 * @param columnName
	 * @return
	 */
	public long getMaxRecordID(String tbName, String columnName) {
		String sql = "select max(" + columnName + ") from " + tbName;
		Cursor cursor = getDatabase(dbName).rawQuery(sql, null);
		long result = 0;
		if (cursor != null) {
			cursor.moveToNext();
			result = cursor.getLong(0);
		}
		cursor.close();
		closeDatabase();
		return result;
	}
}
