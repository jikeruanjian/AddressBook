package com.kevin.addressBook.ui;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.kevin.addressBook.R;
import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.tools.CustomTouchListener;
import com.kevin.addressBook.tools.MyFileBrowser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainEditActivity extends BaseActivity{
	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;
	private String toolID="";
	private EditText name;
	private EditText tel;
	private EditText job;
	private EditText unit;
	private EditText address;
	private EditText sell;
	private EditText ask;
	private Button edit;
	private int isclicked=0;
	private String filePaht;
	private static final String picasaPath = Environment.getExternalStorageDirectory()+"/pic";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ��������
		setContentView(R.layout.main_edit);
		
		Button  changePhoto=(Button) this.findViewById(R.id.main_edit_change_photo);
		 name=(EditText) this.findViewById(R.id.main_edit_name);
		 tel=(EditText) this.findViewById(R.id.main_edit_tel);
		 job=(EditText) this.findViewById(R.id.main_edit_job);
		 unit=(EditText) this.findViewById(R.id.main_edit_unit);
		 address=(EditText) this.findViewById(R.id.main_edit_adds);
		 sell=(EditText) this.findViewById(R.id.main_edit_sell);
		 ask=(EditText) this.findViewById(R.id.main_edit_ask);
		final ImageView photo=(ImageView) this.findViewById(R.id.main_edit_photo);
		
		//获取得来的id
		Intent intent = this.getIntent();
		toolID= intent.getStringExtra("toolID");
		AddressInfo addressInfo=new AddressInfo();
		addressInfo=XmlOptionsImp.getInstance().getUserDetails(toolID);
		
		name.setText(addressInfo.getName());
		name.setInputType(InputType.TYPE_NULL);
		tel.setText(addressInfo.getPhoneNum());
		tel.setInputType(InputType.TYPE_NULL);
		job.setText(addressInfo.getPost());
		job.setInputType(InputType.TYPE_NULL);
		unit.setText(addressInfo.getCompany());
		unit.setInputType(InputType.TYPE_NULL);
		address.setText(addressInfo.getAddress());
		address.setInputType(InputType.TYPE_NULL);
		sell.setText(addressInfo.getSaleInfo());
		sell.setInputType(InputType.TYPE_NULL);
		ask.setText(addressInfo.getPurchaseInfo());
		ask.setInputType(InputType.TYPE_NULL);
		filePaht=addressInfo.getImageName();
		Bitmap bitmap = BitmapFactory.decodeFile(filePaht);
		photo.setImageBitmap(bitmap);
		
		edit=(Button) this.findViewById(R.id.main_edit_save);
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isclicked==0){
					//弹出对话框输入密码后验证通过才能改
					final Dialog dialog = new Dialog(MainEditActivity.this,
							R.style.Theme_ShareDialog);
					dialog.setContentView(R.layout.main_edit_dialog);
					dialog.show();
					// 得到资源ID保存信息 或相应的操作
					final EditText write=(EditText) dialog.findViewById(R.id.main_edit_dialog_mima);
					ImageButton save=(ImageButton) dialog.findViewById(R.id.main_edit_dialog_save);
					ImageButton cancel=(ImageButton) dialog.findViewById(R.id.main_edit_dialog_cancel);
					// 确定按钮保存输入资源到数据库 并且跳转到浏览页面
					save.setOnTouchListener(new CustomTouchListener() {
						@Override
						public void eventAction(View arg0) {
							if(write.getText().toString().equals("111111")){
								name.setInputType(InputType.TYPE_CLASS_TEXT);//来开启软键盘。
								tel.setInputType(InputType.TYPE_CLASS_TEXT);
								job.setInputType(InputType.TYPE_CLASS_TEXT);
								unit.setInputType(InputType.TYPE_CLASS_TEXT);
								address.setInputType(InputType.TYPE_CLASS_TEXT);
								sell.setInputType(InputType.TYPE_CLASS_TEXT);
								ask.setInputType(InputType.TYPE_CLASS_TEXT);
								edit.setText("保存");
								isclicked=1;
							}else{
								Toast.makeText(context, "密码错误！", Toast.LENGTH_SHORT).show();
								edit.setText("修改");
								isclicked=0;
							}
							dialog.cancel();
						}
					});

					// 取消按钮不保存此信息 关闭对话框 跳转页面到拍照按钮页面
					cancel.setOnTouchListener(new CustomTouchListener() {
						@Override
						public void eventAction(View arg0) {
							dialog.cancel();
						}
					});
		
				}else{
					AddressInfo ai=new AddressInfo();
					System.out.println("==========="+name.getText().toString());
					ai.setName(name.getText().toString());
					ai.setPhoneNum(tel.getText().toString());
					ai.setPost(job.getText().toString());
					ai.setCompany(unit.getText().toString());
					ai.setAddress(address.getText().toString());
					ai.setSaleInfo(sell.getText().toString());
					ai.setPurchaseInfo(ask.getText().toString());
					ai.setImageName(filePaht);
					//得到图片路径并存入xml文件中 
					try {
						if(XmlOptionsImp.getInstance().editUser(ai)){
							//将此图片复制到存xml的文件夹下
							if(Environment.getExternalStorageState()
									.equals(Environment.MEDIA_MOUNTED)){
								//在sd卡中创建picasa文件夹
								File files = new File(picasaPath);
								if(!files.isDirectory()){
									files.mkdirs();
								}
								String str[]=filePaht.split("/");
								try {
									File saveFile = new File(picasaPath,str[str.length-1]);
									if(!saveFile.exists()){
									FileInputStream fin = new FileInputStream(filePaht);
									FileOutputStream outStream = new FileOutputStream(saveFile);
									copyfile(fin,outStream);//调用自定义拷贝文件方法
									}
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();	
						}
					} catch (IOException e) {
//						Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
					// 保存成功后跳转到主页面
					edit.setText("修改");
					isclicked=0;
				}

			}
		});
		
		changePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final MyFileBrowser fileBrowserView = new MyFileBrowser(
						MainEditActivity.this);
				builder = new AlertDialog.Builder(MainEditActivity.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("选择相片（jpg、png、gif）")
						.setView(fileBrowserView)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// ����¼�.
										List<File> selectedFiles = null;
										selectedFiles = fileBrowserView.getSelectedFiles();

										// ������ʾ��ǰ���� label
										StringBuilder text = new StringBuilder(
												"|");
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
												if (hz.equals("jpg")
														|| hz.equals("png")
														|| hz.equals("gif")) {
													filePaht= file.getPath();
													Bitmap bitmap = BitmapFactory.decodeFile(filePaht);
													photo.setImageBitmap(bitmap);
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
		});
	}
	
	/**自定义拷贝文件*/
	private  void copyfile(FileInputStream fin,FileOutputStream fou) 
			throws IOException{
		byte[] buffer = new byte[1024];
		int nLength;
		fou.flush();
		while((nLength=fin.read(buffer))!=-1){
			fou.write(buffer,0,nLength);
		}
		fou.flush();
		fin.close();
		fou.close();
	}

}
