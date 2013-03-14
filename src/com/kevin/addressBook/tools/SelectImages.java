package com.kevin.addressBook.tools;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.kevin.addressBook.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectImages extends ListView {

	private List fileList = null;
	private Context context = null;
	private Set<Integer> listSet = null;
	private ArrayList<ImageView> listImageViews = new ArrayList<ImageView>();
	private ArrayList<Bitmap> listBitmap = new ArrayList<Bitmap>();

	public SelectImages(Context context) {
		super(context);
		this.context = context;
		File sdCarDir = Environment.getExternalStorageDirectory();
		fileList = new ArrayList<String>();
		listSet = new HashSet<Integer>();
		this.readSdFileName(sdCarDir);
		updateList();
	}

	public void readSdFileName(File pathfile) {
		if (pathfile.exists()) {
			File[] files = pathfile.listFiles();

			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();
					if (file.isDirectory() && fileName.indexOf(".") != 0) {
						this.readSdFileName(file);

					} else {
						String filepath = file.getAbsolutePath();
						if (filepath.endsWith("jpg")
								|| filepath.endsWith("gif")
								|| filepath.endsWith("bmp")
								|| filepath.endsWith("png")) {
							System.out.println("filePath" + filepath);
							fileList.add(filepath);
						}
					}
				}
			}
		} else {
			System.out.println("pathfile不存在");
		}
	}

	public String getSelectedFiles() {
		Iterator<Integer> iterator = listSet.iterator();
		String file = (String) fileList.get(iterator.next());
		return file;
	}

	MyFileListAdapter adapter = new MyFileListAdapter();

	private void updateList() {
		adapter.notifyDataSetChanged();
		setAdapter(adapter);
	}

	class MyFileListAdapter extends BaseAdapter implements
			OnCheckedChangeListener {

		@Override
		public int getCount() {

			return fileList.size();
		}

		@Override
		public Object getItem(int position) {

			return fileList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater layoutInflate = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout linearLayout = (LinearLayout) layoutInflate.inflate(
					R.layout.filelistview, null);
			CheckBox cb = (CheckBox) linearLayout.findViewById(R.id.cb);
			ImageView file_image = (ImageView) linearLayout
					.findViewById(R.id.file_image);
			TextView file_name = (TextView) linearLayout
					.findViewById(R.id.file_name);
			cb.setTag(position);
			cb.setOnCheckedChangeListener(this);
			final int currentpostion = position;
			final String path = (String) fileList.get(currentpostion);
			try {
				getImage(path, file_image);
			} catch (OutOfMemoryError e) {
				// 释放内存资源
				recycleMemory();
				// 将刚才 发生异常没有执行的 代码 再重新执行一次
				getImage(path, file_image);
			}
			// 根据路径构建文件
			File f = new File(path);
			// 用于得到名称的位置
			int indext = path.lastIndexOf("/") + 1;
			file_name.setText(path.substring(indext));
			return linearLayout;
		}

		private void getImage(String path, ImageView image) {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// 获取这个图片的宽和高
			BitmapFactory.decodeFile(path, options); // 此时返回bm为空
			options.inJustDecodeBounds = false;
			// 计算缩放比
			int be = (int) (options.outHeight / (float) 200);
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;
			// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
			try {
				Bitmap bitmap = BitmapFactory.decodeFile(path.toString(),
						options);
				image.setImageBitmap(bitmap);
				listImageViews.add(image);
				listBitmap.add(bitmap);

			} catch (OutOfMemoryError err) {

			}

		}

		private void recycleMemory() {
			// TODO Auto-generated method stub
			// 一屏显示多少行 这里就设置为多少。不设也行 主要是用户体验好 不会将用户看到的图片设为默认图片
			int showCount = 10;
			for (int i = 0; i < listImageViews.size() - showCount; i++) {
				// 从list中去除
				listImageViews.remove(i);
			}

			for (int i = 0; i < listBitmap.size() - showCount; i++) {
				Bitmap bitmap = (Bitmap) listBitmap.get(i);
				// 这里就开始释放bitmap 所占的内存了
				if (bitmap != null) {
					if (!bitmap.isRecycled()) {
						try {
							bitmap.recycle();
						} catch (Exception e) {

						}
					}
				}

				// 从list中去除
				listBitmap.remove(i);
			}
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (isChecked) {
				if (!listSet.contains(buttonView.getTag()))
					listSet.add((Integer) buttonView.getTag());
			} else {
				if (listSet.contains(buttonView.getTag()))
					listSet.remove(buttonView.getTag());
			}

		}

	}
}
