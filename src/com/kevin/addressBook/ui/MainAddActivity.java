package com.kevin.addressBook.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.kevin.addressBook.bll.XmlOptionsImp;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.tools.MyFileBrowser;
import com.kevin.addressBook.tools.SDPictureService;
import com.kevin.addressBook.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainAddActivity extends BaseActivity{
	private List<String> path ;
	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;
	private View textview;
	private EditText name;
	private EditText tel;
	private EditText job;
	private EditText unit;
	private EditText address;
	private EditText sell;
	private EditText ask;
	private ImageView photo;
	private String filePaht;
	private static final String picasaPath = Environment.getExternalStorageDirectory()+"/pic";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ��������
		setContentView(R.layout.main_edit);
		
		path = SDPictureService.imagePaths;
		
		 name=(EditText) this.findViewById(R.id.main_edit_name);
		 tel=(EditText) this.findViewById(R.id.main_edit_tel);
		 job=(EditText) this.findViewById(R.id.main_edit_job);
		 unit=(EditText) this.findViewById(R.id.main_edit_unit);
		 address=(EditText) this.findViewById(R.id.main_edit_adds);
		 sell=(EditText) this.findViewById(R.id.main_edit_sell);
		 ask=(EditText) this.findViewById(R.id.main_edit_ask);
		 photo=(ImageView) this.findViewById(R.id.main_edit_photo);
		
		Button save=(Button) this.findViewById(R.id.main_edit_save);
		save.setText("保存");
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
					if(XmlOptionsImp.getInstance().addUser(ai)){
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
						
						Toast.makeText(context, "添加成功！", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show();	
					}
				} catch (IOException e) {
//					Toast.makeText(context, "添加失败！", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				
			}
		});
		
		Button cancel=(Button) this.findViewById(R.id.main_edit_cancel);
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
				photo.setBackgroundResource(R.drawable.ic_launcher);
			}
		});
		
		Button changePhoto = (Button) this.findViewById(R.id.main_edit_change_photo);
		changePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				final MyFileBrowser fileBrowserView = new MyFileBrowser(
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
													filePaht = file.getPath();
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
