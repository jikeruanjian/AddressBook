package com.kevin.addressBook.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.kevin.addressBook.R;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class MyFileBrowser extends ListView implements 
	android.widget.AdapterView.OnItemClickListener{

	private Stack<String> pathStack=null;
	private List<File> fileList=null;
	private static final String SDCardPath=Environment.getExternalStorageDirectory().getPath();;
	private Context context=null;
	private OnFileListItemClickListener onFileListItemClickListener;
	private Set<Integer> listSet=null;

	public MyFileBrowser(Context context) {
		super(context);
		this.context=context;
		init();

	}


	
	private void init(){
		pathStack=new Stack<String>();
		fileList=new ArrayList<File>();
		listSet=new HashSet<Integer>();
		pathStack.push(SDCardPath);
		getFiles();
		this.setOnItemClickListener(this);
	}
	
	public List<File> getSelectedFiles(){
		List<File> files = new ArrayList<File>();
		Iterator<Integer> iterator=listSet.iterator();
		while(iterator.hasNext()){
			File file=fileList.get(iterator.next());
			if(file!=null)
			files.add(file);
		}
		return files;
	}
	
	private void getFiles(){
		String path="";
		for(int i=0;i<pathStack.size();i++){
			path+=pathStack.get(i)+"/";
		}
		path=path.substring(0, path.length()-1);
		File f=new File(path);
		File[] files=f.listFiles();
		if(files==null)return;
		fileList.removeAll(fileList);
		
		if(pathStack.size()>1){
			fileList.add(null);
		}
		
		for(File file:files){
			if(file.getName().startsWith("."));
			else
			fileList.add(file);
		}
		updateList();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		File file=fileList.get(position);
		if(file==null){
			pathStack.pop();
			getFiles();
		}else if(file.isDirectory()){
				pathStack.push(file.getName());
				getFiles();
		}else{
			if(onFileListItemClickListener==null)return;
			onFileListItemClickListener.onItemClick(file);
		}
		
	}
	
	MyFileListAdapter adapter = new MyFileListAdapter();
	
	private void updateList(){
		adapter.notifyDataSetChanged();
		setAdapter(adapter);
	}
	
	public void setOnFileClickListener(OnFileListItemClickListener onFileListItemClickListener){
		this.onFileListItemClickListener=onFileListItemClickListener;
	}
	

	class MyFileListAdapter extends BaseAdapter implements OnCheckedChangeListener{
	
		public int getCount() {

			return fileList.size();
		}
	
		public Object getItem(int position) {

			return fileList.get(position);
		}
	
		public long getItemId(int position) {

			return position;
		}
	
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater layoutInflate=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout linearLayout=(LinearLayout)layoutInflate.inflate(R.layout.filelistview, null);
			CheckBox cb=(CheckBox)linearLayout.findViewById(R.id.cb);
			ImageView file_image=(ImageView)linearLayout.findViewById(R.id.file_image);
			TextView file_name=(TextView)linearLayout.findViewById(R.id.file_name);
			cb.setTag(position);
			cb.setOnCheckedChangeListener(this);
			File subFile=fileList.get(position);
			
			int fileIconRes=judgeFileType(subFile);
			String fileName="..";
			if(subFile==null);
			else{
				fileName=subFile.getName();
				if(fileName.equals(""))
					fileName="文件名未知";
			}
			
			file_image.setImageResource(fileIconRes);
			file_name.setText(fileName);
			
			return linearLayout;
		}
		
		private int judgeFileType(File subFile) {

			int fileIconRes=R.drawable.unknow_type;
			if(subFile==null){
				fileIconRes=R.drawable.dir;
			}else if(subFile.isDirectory())fileIconRes=R.drawable.dir;
			else {
				String fileName=subFile.getName();
				if(fileName.endsWith(".jpg")||
						fileName.endsWith(".jpeg")||
						fileName.endsWith(".bmp")||
						fileName.endsWith(".png")||
						fileName.endsWith(".ico")
						){
					fileIconRes=R.drawable.image;
				}else if(fileName.endsWith(".txt")||
						fileName.endsWith(".ini")||
						fileName.endsWith(".xml")){
					fileIconRes=R.drawable.textfile;
				}
			}
			return fileIconRes;
		}

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if(isChecked){
				if(!listSet.contains((Integer)buttonView.getTag()))
					listSet.add((Integer)buttonView.getTag());
			}else{
				if(listSet.contains((Integer)buttonView.getTag()))
					listSet.remove((Integer)buttonView.getTag());
			}
			
		}
		
	}
}
