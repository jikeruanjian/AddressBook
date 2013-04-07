package com.kevin.addressBook.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PicTool {
	public static Bitmap decodeFile(File f) {
		try {
			InputStream is = new FileInputStream(f);
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, o);
			final int REQUIRED_SIZE = 60;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int inSampleSize = 1;
			if (height_tmp > REQUIRED_SIZE || width_tmp > REQUIRED_SIZE) {
				if (width_tmp > height_tmp) {
					inSampleSize = Math.round((float) height_tmp
							/ (float) REQUIRED_SIZE);
				} else {
					inSampleSize = Math.round((float) width_tmp
							/ (float) REQUIRED_SIZE);
				}
			}
			o.inSampleSize = inSampleSize;
			o.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
