package com.kevin.addressBook.ui;

import java.io.File;
import java.util.List;

import com.kevin.addressBook.tools.MyFileBrowser;
import com.kevin.addressBook.tools.SDPictureService;
import com.kevin.addressBook.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainAddActivity extends BaseActivity{
	private List<String> path ;
	private AlertDialog.Builder builder = null;
	private AlertDialog alertDialog = null;
	private View textview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ��������
		setContentView(R.layout.main_edit);
		
		path = SDPictureService.imagePaths;
		
		Button changePhoto = (Button) this.findViewById(R.id.main_edit_change_photo);
		changePhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				final MyFileBrowser fileBrowserView = new MyFileBrowser(
						MainAddActivity.this);
				builder = new AlertDialog.Builder(MainAddActivity.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("ѡ����Ƭ��jpg��png��gif��")
						.setView(fileBrowserView)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
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
													String filePaht = file.getPath();
													Bitmap bitmap = BitmapFactory.decodeFile(filePaht);
//													userPhoto.setImageBitmap(bitmap);//����������ͼƬ
												}
											}
										}
										dialog.cancel();
									}
								})
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// �Ҽ��¼�
										dialog.cancel();
									}
								});
				alertDialog = builder.create();
				alertDialog.show();
			}
		});

	}

	
	
}
