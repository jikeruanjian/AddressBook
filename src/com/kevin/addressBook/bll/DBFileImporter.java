package com.kevin.addressBook.bll;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.ByteArrayBuffer;

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

	public static void importDB(Context context,String filePath, String dbName) {

		AssetManager assetManager = context.getAssets();
		String assetPath = "dbfiles";
		String oldPath = assetPath + "/" + dbName;
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

}
