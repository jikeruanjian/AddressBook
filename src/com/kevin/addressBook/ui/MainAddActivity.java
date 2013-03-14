package com.kevin.addressBook.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.tools.SelectImages;
import com.kevin.addressBook.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
	private List<String> path;
	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;
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
	private String filePaht="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_edit);

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

		Button save = (Button) this.findViewById(R.id.main_edit_save);
		save.setText("保存");
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
				ai.setImageName(filePaht);
				// 得到图片路径并存入xml文件中
				try {
					if (XmlOptionsImp.getInstance().addUser(ai)) {
						// 将此图片复制到存xml的文件夹下
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							// 在sd卡中创建picasa文件夹
							String dir = XmlOptionsImp.getPath().substring(0,
									XmlOptionsImp.getPath().lastIndexOf("/"))
									+ "/AddressBookPic";
							File files = new File(dir);
							if (!files.isDirectory()) {
								files.mkdirs();
							}
							String str[] = filePaht.split("/");
							try {
								File saveFile = new File(dir,
										str[str.length - 1]);
								if (!saveFile.exists()) {
									FileInputStream fin = new FileInputStream(
											filePaht);
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

						Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT)
								.show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

		Button cancel = (Button) this.findViewById(R.id.main_edit_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		});

		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "SelectPicture"),1);
				
	/*			showProgressDialog("请稍等...");
				final SelectImages fileBrowserView = new SelectImages(
						MainAddActivity.this);
				builder = new AlertDialog.Builder(MainAddActivity.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("选择相片（jpg、png、gif）")
						.setView(fileBrowserView)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										String filePaht = fileBrowserView.getSelectedFiles();
										Bitmap bitmap = BitmapFactory.decodeFile(filePaht);
										photo.setImageBitmap(bitmap);
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
				hideProgressDialog();*/
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(3);
			finish();
		}
		return true;
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		  if (requestCode == 1){  
				Bitmap myBitmap = (Bitmap) map(data);  
				photo.setImageBitmap(myBitmap);
		  }
	}
	public Bitmap map(Intent data) {  
        Uri selectedImage = data.getData();  
        String[] filePathColumn = { MediaStore.Images.Media.DATA };  
        Cursor cursor = context.getContentResolver().query(selectedImage,  
                filePathColumn, null, null, null);  
        cursor.moveToFirst();  
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
        String picturePath = cursor.getString(columnIndex);  
        System.out.println("****************picturePath="+picturePath);
        filePaht=picturePath;
        cursor.close();  
        Log.d("picturePath", picturePath);  
        return BitmapFactory.decodeFile(picturePath);  
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
