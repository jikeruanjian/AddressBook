package com.kevin.addressBook.tools;

import java.io.File;
import java.util.ArrayList;
import android.util.Log;

public class SDPictureService {
	public static ArrayList<String> imagePaths = new ArrayList<String>();

	public static void getImagesFromSD(File pathfile) {

		if (pathfile.exists()) {
			File[] files = pathfile.listFiles();

			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();
					// System.out.println("........"+fileName.indexOf("."));
					if (file.isDirectory()) {
						getImagesFromSD(file);

					} else {
						String filepath = file.getAbsolutePath();
						if (filepath.endsWith("jpg")
								|| filepath.endsWith("gif")
								|| filepath.endsWith("bmp")
								|| filepath.endsWith("png")) {
							Log.i("TAG", filepath);
							imagePaths.add(filepath);
						}

					}

				}
			}

		}
	}

	private static String[] imageFormatSet = new String[] { "jpg", "png",
			"gif", "bmp" };

	private static boolean isImageFile(String path) {

		for (String format : imageFormatSet) {
			if (path.contains(format)) {
				return true;
			}
		}
		return false;
	}

}
