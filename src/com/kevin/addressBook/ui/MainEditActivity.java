package com.kevin.addressBook.ui;
import com.kevin.addressBook.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainEditActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ÃÓ≥‰±ÍÃ‚¿∏
		setContentView(R.layout.main_edit);
		
		Button  changePhoto=(Button) this.findViewById(R.id.main_edit_change_photo);
		changePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
