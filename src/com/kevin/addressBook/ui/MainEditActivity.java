package com.kevin.addressBook.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.kevin.addressBook.R;
import com.kevin.addressBook.bll.AddressInfoBll;
import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.model.Const;
import com.kevin.addressBook.tools.CustomTouchListener;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainEditActivity extends BaseActivity {
	private EditText name;
	private EditText tel;
	private EditText job;
	private EditText unit;
	private EditText address;
	private EditText sell;
	private EditText ask;
	private EditText qqNun;
	private EditText mailBox;
	private EditText url;
	private Button edit;
	private Button cancel;
	private String filePath;
	private ImageView photo;
	public static AddressInfo addressInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_edit);

		MainActivity.isNeedReLoad = false;
		name = (EditText) this.findViewById(R.id.main_edit_name);
		tel = (EditText) this.findViewById(R.id.main_edit_tel);
		job = (EditText) this.findViewById(R.id.main_edit_job);
		unit = (EditText) this.findViewById(R.id.main_edit_unit);
		address = (EditText) this.findViewById(R.id.main_edit_adds);
		sell = (EditText) this.findViewById(R.id.main_edit_sell);
		ask = (EditText) this.findViewById(R.id.main_edit_ask);
		photo = (ImageView) this.findViewById(R.id.main_edit_photo);
		qqNun = (EditText) this.findViewById(R.id.main_edit_qqNum);
		mailBox = (EditText) this.findViewById(R.id.main_edit_mailbox);
		url = (EditText) this.findViewById(R.id.main_edit_url);
		edit = (Button) this.findViewById(R.id.main_edit_save);

		if (addressInfo == null) {
			MainActivity.isNeedReLoad = true;
			finish();
			return;
		}
		name.setText(addressInfo.getName());
		tel.setText(addressInfo.getPhoneNum());
		job.setText(addressInfo.getPost());
		unit.setText(addressInfo.getCompany());
		address.setText(addressInfo.getAddress());
		sell.setText(addressInfo.getSaleInfo());
		ask.setText(addressInfo.getPurchaseInfo());
		qqNun.setText(addressInfo.getQq());
		mailBox.setText(addressInfo.getEmail());
		url.setText(addressInfo.getWebSite());
		if (addressInfo.getImageName().equals("")
				|| addressInfo.getImageName() == null) {
			photo.setImageResource(R.drawable.ic_launcher);
		} else {
			filePath = Const.imageDir + addressInfo.getImageName();
			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			photo.setImageBitmap(bitmap);
		}

		if (!XmlOptionsImp.isLogin) {
			name.setInputType(InputType.TYPE_NULL);
			tel.setInputType(InputType.TYPE_NULL);
			job.setInputType(InputType.TYPE_NULL);
			unit.setInputType(InputType.TYPE_NULL);
			address.setInputType(InputType.TYPE_NULL);
			// sell.setInputType(InputType.TYPE_NULL);
			// ask.setInputType(InputType.TYPE_NULL);
			qqNun.setInputType(InputType.TYPE_NULL);
			mailBox.setInputType(InputType.TYPE_NULL);
			url.setInputType(InputType.TYPE_NULL);
			edit.setText("    修    改    ");
		} else {
			edit.setText("    保    存    ");
		}

		cancel = (Button) this.findViewById(R.id.main_edit_cancel);
		cancel.setText("    返    回    ");
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!XmlOptionsImp.isLogin) {
					showLogin();
				} else {
					addressInfo.setName(name.getText().toString());
					addressInfo.setPhoneNum(tel.getText().toString());
					addressInfo.setPost(job.getText().toString());
					addressInfo.setCompany(unit.getText().toString());
					addressInfo.setAddress(address.getText().toString());
					addressInfo.setSaleInfo(sell.getText().toString());
					addressInfo.setPurchaseInfo(ask.getText().toString());
					addressInfo.setQq(qqNun.getText().toString());
					addressInfo.setEmail(mailBox.getText().toString());
					addressInfo.setWebSite(url.getText().toString());
					if (filePath != null && !filePath.equals(""))
						addressInfo.setImageName(filePath.substring(filePath
								.lastIndexOf("/") + 1));
					try {
						if (AddressInfoBll.ai.editUser(addressInfo)) {
							// 将此图片复制到存设定的文件夹下
							if (filePath != null
									&& !filePath.equals("")
									&& Environment.getExternalStorageState()
											.equals(Environment.MEDIA_MOUNTED)) {
								// 在sd卡中创建picasa文件夹
								String str[] = filePath.split("/");
								try {
									File saveFile = new File(Const.imageDir,
											str[str.length - 1]);
									if (!saveFile.exists()) {
										FileInputStream fin = new FileInputStream(
												filePath);
										FileOutputStream outStream = new FileOutputStream(
												saveFile);
										copyfile(fin, outStream);// 调用自定义拷贝文件方法
									}
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							MainActivity.isNeedReLoad = true;
							Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT)
									.show();
							finish();
						} else {
							Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT)
									.show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 保存成功后跳转到主页面
					cancel.setText("    返    回    ");
				}

			}
		});

		if (XmlOptionsImp.isLogin) {
			photo.setClickable(true);
		}
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!XmlOptionsImp.isLogin) {

					// 弹出对话框输入密码后验证通过才能改
					final Dialog dialog = new Dialog(MainEditActivity.this,
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
								name.setInputType(InputType.TYPE_CLASS_TEXT);// 来开启软键盘。
								tel.setInputType(InputType.TYPE_CLASS_TEXT);
								job.setInputType(InputType.TYPE_CLASS_TEXT);
								unit.setInputType(InputType.TYPE_CLASS_TEXT);
								address.setInputType(InputType.TYPE_CLASS_TEXT);
								sell.setInputType(InputType.TYPE_CLASS_TEXT);
								ask.setInputType(InputType.TYPE_CLASS_TEXT);
								qqNun.setInputType(InputType.TYPE_CLASS_TEXT);
								mailBox.setInputType(InputType.TYPE_CLASS_TEXT);
								url.setInputType(InputType.TYPE_CLASS_TEXT);
								edit.setText("    保    存    ");
								XmlOptionsImp.isLogin = true;
							} else {
								Toast.makeText(context, "密码错误！",
										Toast.LENGTH_SHORT).show();
								edit.setText("    修    改    ");
							}
							dialog.cancel();
						}
					});

					// 取消按钮不保存此信息 关闭对话框 跳转页面到拍照按钮页面
					cancel1.setOnTouchListener(new CustomTouchListener() {
						@Override
						public void eventAction(View arg0) {

							dialog.cancel();
						}
					});
					return;
				}
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
			}
		});
	}

	private void showLogin() {

		// 弹出对话框输入密码后验证通过才能改
		final Dialog dialog = new Dialog(MainEditActivity.this,
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
					name.setInputType(InputType.TYPE_CLASS_TEXT);// 来开启软键盘。
					tel.setInputType(InputType.TYPE_CLASS_TEXT);
					job.setInputType(InputType.TYPE_CLASS_TEXT);
					unit.setInputType(InputType.TYPE_CLASS_TEXT);
					address.setInputType(InputType.TYPE_CLASS_TEXT);
					sell.setInputType(InputType.TYPE_CLASS_TEXT);
					ask.setInputType(InputType.TYPE_CLASS_TEXT);
					qqNun.setInputType(InputType.TYPE_CLASS_TEXT);
					mailBox.setInputType(InputType.TYPE_CLASS_TEXT);
					url.setInputType(InputType.TYPE_CLASS_TEXT);
					photo.setClickable(true);
					edit.setText("    保    存    ");
					XmlOptionsImp.isLogin = true;
				} else {
					Toast.makeText(context, "密码错误！", Toast.LENGTH_SHORT).show();
					edit.setText("    修    改    ");
				}
				dialog.cancel();
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

	/** 自定义拷贝文件 */
	private void copyfile(FileInputStream fin, FileOutputStream fou)
			throws IOException {
		byte[] buffer = new byte[1024];
		int nLength;
		fou.flush();
		while ((nLength = fin.read(buffer)) != -1) {
			fou.write(buffer, 0, nLength);
		}
		fou.flush();
		fin.close();
		fou.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			Bitmap myBitmap = checkImage(data);
			if (myBitmap != null) {
				photo.setImageBitmap(myBitmap);
			} else {
				photo.setImageResource(R.drawable.ic_launcher);
			}
		}
	}

	@SuppressWarnings("finally")
	private Bitmap checkImage(Intent data) {
		Cursor cursor = null;
		try {
			Uri originalUri = data.getData();
			String[] proj = { MediaColumns.DATA };
			cursor = managedQuery(originalUri, proj, null, null, null);
			if (cursor != null) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径
				filePath = cursor.getString(column_index);
				cursor.close();
			}
		} catch (Exception e) {
			Log.e("checkImage", e.getMessage());
		} finally {
			if (cursor != null)
				cursor.close();
			return BitmapFactory.decodeFile(filePath);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		menu.add(1, 1, 1, "删除");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 响应每个菜单项(通过菜单项的ID)
		case 1:
			if (XmlOptionsImp.isLogin) {
				if (AddressInfoBll.ai.deleteUser(addressInfo.getId())) {
					MainActivity.isNeedReLoad = true;
					Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
				}
			} else {
				showLogin();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
