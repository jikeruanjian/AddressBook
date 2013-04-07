package com.kevin.addressBook.bll;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import com.kevin.addressBook.model.AddressInfo;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * 数据库导入类
 * 
 * @author zhudameng
 * 
 */
public class DBFileImporter {

	public static void importDB(Context context, String filePath, String dbName) {

		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = assetManager.open(dbName);

			BufferedInputStream bis = new BufferedInputStream(inputStream);
			ByteArrayBuffer baf = new ByteArrayBuffer(100);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			byte[] data = baf.toByteArray();

			if (data != null) {
				File file = new File(filePath);// 保存文件
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data);
			}
		} catch (IOException e) {
			Log.d("importDB:err", e.getMessage());
		}

	}

	/**
	 * 筛选,按照名称的
	 * 
	 * @param key
	 * @return
	 */
	public static List<AddressInfo> searchWithKey(String key,
			List<AddressInfo> allInfos) {
		if (key == null || key.equals(""))
			return allInfos;
		List<AddressInfo> result = new ArrayList<AddressInfo>();
		for (AddressInfo addressInfo : allInfos) {
			if (addressInfo.getName().contains(key)) {
				result.add(addressInfo);
			}
		}
		return result;
	}

}
