package com.kevin.addressBook.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kevin.addressBook.bll.AddressInfoBll;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.model.Const;
import com.kevin.addressBook.R;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainAddActivity extends BaseActivity {
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
	private ImageView photo;
	private String filePaht = "";

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
		photo.setImageResource(R.drawable.ic_launcher);

		Button save = (Button) this.findViewById(R.id.main_edit_save);
		save.setText("    保    存    ");
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
				finish();
			}

		});

		Button cancel = (Button) this.findViewById(R.id.main_edit_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (save()) {
					name.setText("");
					tel.setText("");
					job.setText("");
					unit.setText("");
					address.setText("");
					sell.setText("");
					ask.setText("");
					qqNun.setText("");
					mailBox.setText("");
					url.setText("");
					photo.setBackgroundResource(R.drawable.ic_launcher);
				}
			}
		});

		photo.setClickable(true);
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return true;

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
				filePaht = cursor.getString(column_index);
				cursor.close();
			}
		} catch (Exception e) {
			Log.e("checkImage", e.getMessage());
		} finally {
			if (cursor != null)
				cursor.close();
			return BitmapFactory.decodeFile(filePaht);
		}
	}

	private boolean save() {
		AddressInfo ai = new AddressInfo();
		System.out.println("===========" + name.getText().toString());
		ai.setName(name.getText().toString());
		ai.setPhoneNum(tel.getText().toString());
		ai.setPost(job.getText().toString());
		ai.setCompany(unit.getText().toString());
		ai.setAddress(address.getText().toString());
		ai.setSaleInfo(sell.getText().toString());
		ai.setPurchaseInfo(ask.getText().toString());
		ai.setQq(qqNun.getText().toString());
		ai.setEmail(mailBox.getText().toString());
		ai.setWebSite(url.getText().toString());
		ai.setImageName(filePaht.substring(filePaht.lastIndexOf("/") + 1));
		if (AddressInfoBll.ai.addUser(ai)) {
			MainActivity.isNeedReLoad = true;
			// 将此图片复制到存xml的文件夹下
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String str[] = filePaht.split("/");
				try {
					File saveFile = new File(Const.imageDir,
							str[str.length - 1]);
					if (!saveFile.exists()) {
						FileInputStream fin = new FileInputStream(filePaht);
						FileOutputStream outStream = new FileOutputStream(
								saveFile);
						copyfile(fin, outStream);// 调用自定义拷贝文件方法
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show();
					return false;
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
			return true;
		} else {
			Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show();
		}
		return false;
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

}
