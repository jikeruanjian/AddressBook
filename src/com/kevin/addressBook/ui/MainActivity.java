package com.kevin.addressBook.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.kevin.addressBook.bll.AddressInfoBll;
import com.kevin.addressBook.bll.PicData;
import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.model.Const;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnScrollListener {
	private ListView list;
	private ViewHolder holder;
	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;
	private String key = "";
	private HashMap<String, Object> tasks = new HashMap<String, Object>();
	public static boolean isNeedReLoad = false;
	private MainActivityListAdapter mAdapter;
	private int visibleLastIndex = 0; // 最后的可视项索引

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏
		setContentView(R.layout.main_list);

		EditText select = (EditText) this.findViewById(R.id.main_list_seacher);
		list = (ListView) this.findViewById(R.id.main_list);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: { // 重新展现数据
					mAdapter.datas = AddressInfoBll.getDataList();
					mAdapter.notifyDataSetChanged();
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
				showProgressDialog("正在加载数据...");
				// 初始化界面数据
				new Thread(new Runnable() {

					@Override
					public void run() {
						AddressInfoBll.setSearchName(key); // 重新筛选数据
						Message msg = new Message(); // 通知界面重新绘制
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
				System.out.println("itemClick");
				// 点击item进入详细界面 显示详细信息
				AddressInfo entity = (AddressInfo) list
						.getItemAtPosition(position);
				MainEditActivity.addressInfo = entity;
				Intent intent = new Intent(MainActivity.this,
						MainEditActivity.class);
				// Bundle bundle = new Bundle();
				// String toolId = entity.getId();
				// bundle.putString("toolID", toolId);
				// intent.putExtras(bundle);
				MainActivity.this.startActivity(intent);
				System.out.println("itemClick完成");
			}
		});

		showProgressDialog("正在加载数据...");

		mAdapter = new MainActivityListAdapter(context,
				AddressInfoBll.getDataList());
		list.setAdapter(mAdapter);
		list.setOnScrollListener(this);

		Message msg = new Message();
		msg.what = 0;
		handler.sendMessage(msg);
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
					.setTitle("选择文件（AddressBook.xml）")
					.setView(fileBrowserView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									String filePath = fileBrowserView
											.getSelectedFiles();
									System.out.println("得到的文件路径为：" + filePath);
									if (filePath == null
											|| filePath.equals("")
											|| filePath
													.equals(Const.dataBaseUrl)) {// 如果文件没改变就不要在做后面的操作了
										return;
									}
									// 保存路径
									SharedPreferences preferences = getSharedPreferences(
											"config", Context.MODE_PRIVATE);
									Editor editor = preferences.edit();
									editor.putString("filePath", filePath);
									editor.commit();

									showProgressDialog("正在加载文件，请稍后...");
									// XmlOptionsImp.setPath(filePath);

									// dataList = XmlOptionsImp.getInstance()
									// .getAllUsers(); // 重新加载数据
									AddressInfoBll.setSearchName("");

									Message msg = new Message();
									msg.what = 0;
									handler.sendMessage(msg);
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
			if (!XmlOptionsImp.isLogin) {
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
							XmlOptionsImp.isLogin = true;
							Intent intent = new Intent(context,
									MainAddActivity.class);
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
			} else {
				Intent intent = new Intent(context, MainAddActivity.class);
				context.startActivity(intent);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isNeedReLoad) {
			System.out.println("有一次运行了");
			showProgressDialog("正在加载数据...");
			AddressInfoBll.refreshData(); // 可能进行了添加数据操作， 这里要刷新一下
			Message msg = new Message();
			msg.what = 0;
			handler.sendMessage(msg);
		}
	}

	class MainActivityListAdapter extends ListAdapter {

		public MainActivityListAdapter(Context context, List dataList) {
			super(context, dataList);
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

			AddressInfo entity = (AddressInfo) list.getItemAtPosition(position);
			holder.name.setText("姓名：" + entity.getName());
			holder.tel.setText("电话：" + entity.getPhoneNum());

			System.out.println("getView遍历中");
			Object bitmap = PicData.getData(entity.getImageName());
			if (bitmap != null) {
				holder.photo.setImageBitmap((Bitmap) bitmap);
			} else {
				holder.photo.setImageBitmap(null);
			}
			setIOSListItemBg(position, getCount(), convertView);
			PicData.clearData();
			AsyncTask task = (AsyncTask) tasks.get(entity.getImageName());
			if (!entity.getImageName().equals("")) {
				if (task == null || task.isCancelled()) {
					tasks.put(entity.getImageName(), new GetItemImageTask(
							entity.getImageName()).execute(null));// 启动线程异步获取图片
				}
			}
			return convertView;
		}

		/** 存放控件 */
		public final class ViewHolder {
			public TextView name;
			public TextView tel;
			public ImageView photo;
		}

		class GetItemImageTask extends AsyncTask<Void, Void, Void> {// 获取图片仍采用AsyncTask，这里的优化放到下篇再讨论

			String imageName;

			GetItemImageTask(String imageName) {
				this.imageName = imageName;
			}

			@Override
			protected Void doInBackground(Void... params) {
				if (!imageName.equals("") && !PicData.isContainsKey(imageName)) {
					String path = Const.imageDir + imageName; // 得到全路径
					File file = new File(path);
					if (file.exists()) {
						PicData.putData(imageName, PicTool.decodeFile(file));// 将图片放入内存
					}
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				mAdapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		visibleLastIndex = firstVisibleItem + visibleItemCount;
		if (totalItemCount == AddressInfoBll.getDataCount()) {// 数据已经全部加载
			System.out.println("数据加载完了。。。");
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		System.out.println("visibleLastIndex:" + visibleLastIndex);
		System.out.println("mAdapter.getCount()" + mAdapter.getCount());
		System.out.println(scrollState);
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == mAdapter.getCount()
				&& visibleLastIndex < AddressInfoBll.getDataCount()) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			System.out.println("该加载下一页了");
			AddressInfoBll.nextPageData();

			Message msg = new Message();
			msg.what = 0;
			handler.sendMessage(msg);
		}
	}
}
