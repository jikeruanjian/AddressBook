package com.kevin.addressBook.tools;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.kevin.addressBook.R;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectFiles extends ListView {

	private List fileList = null;
	private Context context = null;
	private Set<Integer> listSet = null;

	public SelectFiles(Context context) {
		super(context);
		this.context = context;
		File sdCarDir = Environment.getExternalStorageDirectory();
		fileList = new ArrayList<String>();
		listSet = new HashSet<Integer>();
		this.readSdFileName(sdCarDir);
		updateList();
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
			cb.setTag(position);
			cb.setOnCheckedChangeListener(this);
			ImageView file_image = (ImageView) linearLayout
					.findViewById(R.id.file_image);
			TextView file_name = (TextView) linearLayout
					.findViewById(R.id.file_name);
			if (fileList != null) {
				String path = (String) fileList.get(position);
				file_image.setImageResource(R.drawable.folder);
				int i = path.lastIndexOf("/") + 1;
				// 根据路径构建文件
				File f = new File(path);
				// 用于得到名称的位置
				int indext = path.lastIndexOf("/") + 1;
				file_name.setText(path.substring(indext));
				String unite = "";
				// 得到文件的大小
				double size = (double) f.length() / (1024.00 * 1024.00);
				// 根据文件的大小文件的给文件设置单位
				if (size < 1) {
					size = (double) f.length() / 1024.00;

					unite = new DecimalFormat("###,###,###.##").format(size)
							+ "Kb";
				} else {
					size = (double) f.length() / (1024.00 * 1024.00);
					unite = new DecimalFormat("###,###,###.##").format(size)
							+ "MB";
				}
				// 得到文件的日期和大小
				file_name.setText(((String) fileList.get(position))
						.substring(i));
			} else {

				Toast.makeText(context, "没有文件", Toast.LENGTH_LONG).show();
			}
			return linearLayout;

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
						if (filepath.endsWith("log")
								|| filepath.endsWith("docx")
								|| filepath.endsWith("pdf")
								|| filepath.endsWith("doc")
								|| filepath.endsWith("txt")
								|| filepath.endsWith("ppt")
								|| filepath.endsWith("xml")
								|| filepath.endsWith("log")) {
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

}
