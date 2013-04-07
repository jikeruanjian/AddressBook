package com.kevin.addressBook.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kevin.addressBook.R;
import com.kevin.addressBook.bll.AddressInfoBll;
import com.kevin.addressBook.bll.DBFileImporter;
import com.kevin.addressBook.bll.PicData;
import com.kevin.addressBook.bll.PreReadTask;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.model.Const;
import com.kevin.addressBook.tools.PicTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置标题栏
		setContentView(R.layout.welcome);
		// Date de = new Date();
		// if (de.getYear() > 2013 || de.getMonth() > 3 || de.getDate() > 25) {
		// Toast.makeText(getApplicationContext(), "试用已到期", Toast.LENGTH_LONG)
		// .show();
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// finish();
		// return;
		// }

		// 初始化数据
		String filePath = "/data"
				+ Environment.getDataDirectory().getAbsolutePath() + "/"
				+ "com.kevin.addressBook/" + "AddressBook.db";
		String imageDir = "/data"
				+ Environment.getDataDirectory().getAbsolutePath() + "/"
				+ "com.kevin.addressBook/" + "AddressBookPic/";
		SharedPreferences preferences = getSharedPreferences("config",
				Context.MODE_PRIVATE);
		filePath = preferences.getString("filePath", filePath); // 数据库文件的全路径
		imageDir = preferences.getString("imageDir", imageDir);
		File file = new File(filePath);
		if (!file.exists()) {
			DBFileImporter.importDB(this, filePath, "AddressBook.db");
		}
		File iamgeDirF = new File(imageDir);
		if (!iamgeDirF.exists())
			iamgeDirF.mkdirs();
		// XmlOptionsImp.setPath(filePath); // 现初始化 xmlOptionsImp类
		Const.dataBaseUrl = filePath;
		Const.imageDir = imageDir;

		AddressInfoBll.setSearchName(""); // 开始加载数据了
		for (int i = 0; i < AddressInfoBll.getDataList().size(); i++) { //
			// 先读取
			ReadImgTask task = new ReadImgTask();
			task.execute(AddressInfoBll.getDataList().get(i).getImageName()); //
			// 根据图片名称来执行任务
		}
		Toast.makeText(getApplicationContext(), "PreReading...",
				Toast.LENGTH_SHORT).show();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(WelcomeActivity.this,
						MainActivity.class));//

				finish();
			}
		}, 1000);// 1秒后页面跳转

		// String path = "/data"
		// + Environment.getDataDirectory().getAbsolutePath() + "/"
		// + "com.kevin.addressBook/" + "doc.txt";
		// List<String> ls = readFile(path);
		// int errorCount = 0;
		// for (String string : ls) {
		// // lai.add(converToObject(string));
		// try {
		// if (!AddressInfoBll.ai.addUser(converToObject(string))) {
		// errorCount++;
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// errorCount++;
		// }
		// }
		// System.out.println("错误总数：" + errorCount);
	}

	class ReadImgTask extends PreReadTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... arg0) {
			if (!arg0[0].equals("") && !PicData.isContainsKey(arg0[0])) {
				String path = Const.imageDir + arg0[0]; // 得到全路径
				File file = new File(path);
				if (file.exists()) {
					PicData.putData(arg0[0], PicTool.decodeFile(file));// 将图片放入内存
				}
			}
			return null;
		}
	}

	// 分条
	public List<String> readFile(String path) {
		List<String> ls = new ArrayList<String>();
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}

		BufferedReader reader = null;
		StringBuilder sb = null;
		try {
			FileInputStream fInputStream = new FileInputStream(file);
			// code为上面方法里返回的编码方式
			InputStreamReader inputStreamReader = new InputStreamReader(
					fInputStream, "gb2312");
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(inputStreamReader);
			String s = null;
			// 一次读入一行，直到读入null为文件结束
			while ((s = reader.readLine()) != null) {
				if (s.indexOf("姓名") == 0) {
					if (sb != null && sb.length() > 0) {
						ls.add(sb.toString());
					}
					sb = new StringBuilder();
				}
				if (s.contains("单位：") || s.contains("地址：") || s.contains("电话：")
						|| s.contains("经营项目：") || s.contains("求购：")
						|| s.contains("网址：") || s.contains("QQ：")
						|| s.contains("职务：") || s.contains("邮箱：")) {
					sb.append("&");
				}
				while (s.endsWith(String.valueOf(((char) 57347)))) {
					s = s.substring(0, s.length() - 1);
				}
				sb.append(s.trim() + "\r\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return ls;
	}

	// 转换到object
	public AddressInfo converToObject(String content) {
		AddressInfo ai = null;
		String[] infos = content.split("&");
		if (infos.length > 0) {
			ai = new AddressInfo();
		} else
			return null;
		for (String string : infos) {
			// 去掉最后以为的 \r\n
			string = string.substring(0, string.length() - 2);

			int index = string.indexOf("：") + 1;
			if (string.contains("姓名")) {
				ai.setName(string.substring(index));
			} else if (string.contains("单位")) {
				ai.setCompany(string.substring(index));
			} else if (string.contains("职务")) {
				ai.setPost(string.substring(index));
			} else if (string.contains("地址")) {
				ai.setAddress(string.substring(index));
			} else if (string.contains("电话")) {
				ai.setPhoneNum(string.substring(index));
			} else if (string.contains("经营项目")) {
				ai.setSaleInfo(string.substring(index));
			} else if (string.contains("求购")) {
				ai.setPurchaseInfo(string.substring(index));
			} else if (string.contains("网址")) {
				ai.setWebSite(string.substring(index));
			} else if (string.contains("QQ")) {
				ai.setQq(string.substring(index));
			} else if (string.contains("邮箱")) {
				ai.setEmail(string.substring(index));
			}
		}
		return ai;
	}

}
