package com.kevin.addressBook.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kevin.addressBook.bll.DBFileImporter;
import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.tools.CustomTouchListener;
import com.kevin.addressBook.tools.PicTool;
import com.kevin.addressBook.tools.SelectFiles;
import com.kevin.addressBook.ui.MainActivity.MainActivityListAdapter.ViewHolder;
import com.kevin.addressBook.R;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.InputType;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private ListView list;
	private static List dataList = new ArrayList();
	private ViewHolder holder;
	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;
	private String key = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏
		setContentView(R.layout.main_list);

		EditText select = (EditText) this.findViewById(R.id.main_list_seacher);
		list = (ListView) this.findViewById(R.id.main_list);
		// Button add = (Button) this.findViewById(R.id.main_add);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: { // 重新加载数据
					list.setAdapter(new MainActivityListAdapter(context,
							DBFileImporter.searchWithKey(key, dataList)));
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
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				key = s.toString();
				showProgressDialog("正在初始化数据...");
				// 初始化界面数据
				new Thread(new Runnable() {

					@Override
					public void run() {
						dataList = XmlOptionsImp.getInstance().getAllUsers();
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

		// add.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(context, MainAddActivity.class);
		// // context.startActivity(intent);
		// startActivityForResult(intent, 3);
		// }
		// });

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
					System.out.println("dir:"
							+ filePath.substring(0, filePath.lastIndexOf("/")));
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
		menu.add(1, 2, 2, "添加记录");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 响应每个菜单项(通过菜单项的ID)
		case 1:
			final SelectFiles fileBrowserView = new SelectFiles(
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

									String filePath = (String) fileBrowserView
											.getSelectedFiles();
									System.out.println("得到的文件路径为：" + filePath);
									// 保存路径
									SharedPreferences preferences = getSharedPreferences(
											"config", Context.MODE_PRIVATE);
									Editor editor = preferences.edit();
									editor.putString("filePath", filePath);
									XmlOptionsImp.setPath(filePath);

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
			break;
		case 2:

			// 弹出对话框输入密码后验证通过才能改
			final Dialog dialog = new Dialog(MainActivity.this,
					R.style.Theme_ShareDialog);
			dialog.setContentView(R.layout.main_edit_dialog);
			dialog.show();
			// 得到资源ID保存信息 或相应的操作
			final EditText write = (EditText) dialog
					.findViewById(R.id.main_edit_dialog_mima);
			ImageButton save = (ImageButton) dialog
					.findViewById(R.id.main_edit_dialog_save);
			ImageButton cancel1 = (ImageButton) dialog
					.findViewById(R.id.main_edit_dialog_cancel);
			// 确定按钮保存输入资源到数据库 并且跳转到浏览页面
			save.setOnTouchListener(new CustomTouchListener() {
				@Override
				public void eventAction(View arg0) {
					if (write.getText().toString().equals("111111")) {
						Intent intent = new Intent(context,MainAddActivity.class);
						// context.startActivity(intent);
						startActivityForResult(intent, 3);
						dialog.cancel();
					}
				}
			});

			// 取消按钮不保存此信息 关闭对话框 跳转页面到拍照按钮页面
			cancel1.setOnTouchListener(new CustomTouchListener() {
				@Override
				public void eventAction(View arg0) {

					dialog.cancel();
				}
			});
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dataList = XmlOptionsImp.getInstance().getAllUsers();
		Message msg = new Message();
		msg.what = 0;
		handler.sendMessage(msg);
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
			holder.name.setText("姓名：" + entity.getName());
			holder.tel.setText("电话：" + entity.getPhoneNum());

			if (!entity.getName().equals("")) {
				String path = XmlOptionsImp.getPath().substring(0,
						XmlOptionsImp.getPath().lastIndexOf("/") + 1)
						+ "AddressBookPic/" + entity.getImageName(); // 得到全路劲
				// BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inJustDecodeBounds = true;
				// // 获取这个图片的宽和高
				// Bitmap bitmap = BitmapFactory.decodeFile(path, options); //
				// 此时返回bm为空
				// options.inJustDecodeBounds = false;
				// // 计算缩放比
				// int be = (int) (options.outHeight / (float) 200);
				// if (be <= 0)
				// be = 1;
				// options.inSampleSize = be;
				// // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
				// bitmap = BitmapFactory.decodeFile(path, options);

				// Bitmap bitmap =
				// BitmapFactory.decodeFile(entity.getImageName());
				holder.photo.setImageBitmap(PicTool.decodeFile(new File(path)));
			} else {
				holder.photo.setImageResource(R.drawable.ic_launcher);
				// holder.photo.setImageResource(R.drawable.dir);
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
