package com.kevin.addressBook.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kevin.addressBook.bll.DBFileImporter;
import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.bll.XmlUtility;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.ui.MainActivity.MainActivityListAdapter.ViewHolder;
import com.kevin.addressBook.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
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

			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

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
							+ "/" + "com.kevin.addressBook/" + "AddressBook.xml";
					SharedPreferences preferences = getSharedPreferences(
							"config", Context.MODE_PRIVATE);
					filePath = preferences.getString("filePath", filePath);

					File file = new File(filePath);
					if (!file.exists()) {
						DBFileImporter.importDB(context,filePath, "AddressBook.xml");
					}
					XmlOptionsImp.setPath(filePath);
					dataList = XmlOptionsImp.getInstance()
							.getAllUsers();
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			AddressInfo entity = (AddressInfo)dataList.get(position);
			holder.name.setText(entity.getName());
			holder.tel.setText(entity.getId());
			
			XmlOptionsImp.getInstance().addUser(entity);

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
