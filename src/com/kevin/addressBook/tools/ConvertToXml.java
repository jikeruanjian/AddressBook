package com.kevin.addressBook.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertToXml {

	public static List<String> ReadFile(String path) {
		List<String> ls = new ArrayList<String>();
		File file = new File(path);
		if (file.exists()) {
			return null;
		}
		BufferedReader reader = null;
		StringBuilder sb = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String s = null;
			// 一次读入一行，直到读入null为文件结束
			while ((s = reader.readLine()) != null) {
				if (s.indexOf("姓名") == 0) {
					if (sb != null && sb.length() > 0) {
						ls.add(sb.toString());
					}
					sb = new StringBuilder();
				}
				sb.append(s);
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

}
