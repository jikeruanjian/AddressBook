package com.kevin.addressBook.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kevin.addressBook.bll.DBFileImporter;
import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.tools.MyFileBrowser;
import com.kevin.addressBook.ui.MainActivity.MainActivityListAdapter.ViewHolder;
import com.kevin.addressBook.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {
	private ListView list;
	private static List dataList = new ArrayList();
	private ViewHolder holder;
	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏
		setContentView(R.layout.main_list);

		EditText select = (EditText) this.findViewById(R.id.main_list_seacher);
		list = (ListView) this.findViewById(R.id.main_list);
		Button add = (Button) this.findViewById(R.id.main_add);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: { // 重新加载数据
					list.setAdapter(new MainActivityListAdapter(context,
							dataList));
					hideProgressDialog();
					break;
				}
				}
			}
		};

		select.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				final String index = s.toString();
				showProgressDialog("正在初始化数据...");
				// 初始化界面数据
				new Thread(new Runnable() {

					@Override
					public void run() {
						// dataList = XmlOptionsImp.getInstance().;
						// dataList = new ToolPDBll()
						// .getAllCanStockCountSafeTools(index);
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				}).start();
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击item进入详细界面 显示详细信息
				AddressInfo entity = (AddressInfo) dataList.get(position);
				Intent intent = new Intent(context, MainEditActivity.class);
				Bundle bundle = new Bundle();
				String toolId = entity.getId();
				bundle.putString("toolID", toolId);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MainAddActivity.class);
				context.startActivity(intent);
			}
		});

		showProgressDialog("正在初始化数据...");
		// 初始化界面数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (dataList.size() == 0) {
					System.out.println("重新装载了...");
					// 初始化数据
					String filePath = "/data"
							+ Environment.getDataDirectory().getAbsolutePath()
							+ "/" + "com.kevin.addressBook/"
							+ "AddressBook.xml";
					SharedPreferences preferences = getSharedPreferences(
							"config", Context.MODE_PRIVATE);
					filePath = preferences.getString("filePath", filePath);

					File file = new File(filePath);
					if (!file.exists()) {
						DBFileImporter.importDB(context, filePath,
								"AddressBook.xml");
					}
					XmlOptionsImp.setPath(filePath);
					dataList = XmlOptionsImp.getInstance().getAllUsers();
				}
				hideProgressDialog();
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}).start();

		System.gc();
		System.gc();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		menu.add(1, 1, 1, "选择文件");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		// 响应每个菜单项(通过菜单项的ID)
		case 1:
			final MyFileBrowser fileBrowserView = new MyFileBrowser(
					MainActivity.this);
			builder = new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("选择文件（txt、xml）")
					.setView(fileBrowserView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// ����¼�.
									List<File> selectedFiles = null;
									selectedFiles = fileBrowserView
											.getSelectedFiles();

									// ������ʾ��ǰ���� label
									StringBuilder text = new StringBuilder("|");
									if (selectedFiles == null)
										return;
									for (File file : selectedFiles) {
										text.append(file.getName() + "|");
									}
									for (File file : selectedFiles) {
										String fileName = file.getName();
										String[] fs = fileName.split("\\.");
										if (fs.length == 2) {
											String hz = fs[1];
											if (hz.equals("txt")
													|| hz.equals("xml")) {
												String filePath = file
														.getPath();
												System.out.println("得到的文件路径为："
														+ filePath);
												// 保存路径
												SharedPreferences preferences = getSharedPreferences(
														"config",
														Context.MODE_PRIVATE);
												Editor editor = preferences
														.edit();
												editor.putString("filePath",
														filePath);
												XmlOptionsImp.setPath(filePath);
											}
										}
									}
									dialog.cancel();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
			alertDialog = builder.create();
			alertDialog.show();
		}
		return super.onOptionsItemSelected(item);

	}

	class MainActivityListAdapter extends ListAdapter {

		public MainActivityListAdapter(Context context, List dataList) {
			super(context, dataList);
		}

		private void refreshData(List dataList) {
			super.datas = dataList;
			this.notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.main_list_item, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.name = (TextView) convertView
						.findViewById(R.id.main_list_item_name);
				holder.tel = (TextView) convertView
						.findViewById(R.id.main_list_item_tel);
				holder.photo = (ImageView) convertView
						.findViewById(R.id.main_list_item_photo);

				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			AddressInfo entity = (AddressInfo) dataList.get(position);
			holder.name.setText("姓名："+entity.getName());
			holder.tel.setText("电话："+entity.getPhoneNum());
			String path = entity.getImageName();
			if (path != null) {
				Bitmap bitmap = BitmapFactory.decodeFile(entity.getImageName());
				holder.photo.setImageBitmap(bitmap);
			} else {
				holder.photo.setImageResource(R.drawable.ic_launcher);
				// holder.photo.getResources().getDrawable(R.drawable.ic_launcher);
			}

			((MainActivity) context).setIOSListItemBg(position, getCount(),
					convertView);

			return convertView;
		}

		/** 存放控件 */
		public final class ViewHolder {
			public TextView name;
			public TextView tel;
			public ImageView photo;
		}

	}

}
