package com.kevin.addressBook.ui;

import com.kevin.addressBook.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainDetailActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_detail);
		
		Button edit=(Button) this.findViewById(R.id.main_detail_edit);
		Button add=(Button) this.findViewById(R.id.main_detail_add);
		
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 修改用户详细资料
				Intent intent=new Intent(context,MainEditActivity.class);
				context.startActivity(intent);
			}
		});
		
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//增加信息
				Intent intent=new Intent(context,MainAddActivity.class);
				context.startActivity(intent);
			}
		});
		
		
	}
	

}
