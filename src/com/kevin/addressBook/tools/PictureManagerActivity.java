package com.kevin.addressBook.tools;
//package com.example.tools;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.ImageView.ScaleType;
//
//public class PictureManagerActivity extends Activity {
//
//	private Button upPage = null;
//	private Button downPage = null;
//	private Button mark = null;
//	private Button deleteAll = null;
//	private GridView gridView = null;
//	private CheckBox checkBox;
//	private Button back = null;
//	List<String> path ;
//	List<CheckBox> cbob = new ArrayList<CheckBox>();
//	boolean b = true;
//	String name = null;
//	ArrayList<String> arrayList = new ArrayList<String>();
//	int index = 0;
//	int VIEW_COUNT = 12;
//	int i = 0;
//	ImageAdapter imageAdapter;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);// 锟斤拷锟斤拷锟斤拷锟斤拷
//		setContentView(R.layout.main);
//		
//		SDPictureService.getImagesFromSD(Environment.getExternalStorageDirectory());
//		path = SDPictureService.imagePaths;
//		imageAdapter = new ImageAdapter(path, 0);
//
//		upPage = (Button) this.findViewById(R.id.upPage);
//		downPage = (Button) this.findViewById(R.id.downPage);
//		mark = (Button) this.findViewById(R.id.markButton);
//		deleteAll = (Button) this.findViewById(R.id.deleteAll);
//		gridView = (GridView) this.findViewById(R.id.gridView);
//		back = (Button) this.findViewById(R.id.goBack);
//
//		// 锟斤拷锟斤拷锟斤拷匕锟脚ワ拷锟斤拷锟斤拷锟斤拷约锟斤拷锟斤拷锟�		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//
//		// 锟斤拷一�?//		upPage.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				i--;
//				imageAdapter = new ImageAdapter(path, i);
//				gridView.setAdapter(imageAdapter);
//				gridView.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						int postion1 = position + i * VIEW_COUNT;
//						Toast.makeText(getApplicationContext(),
//								path.get(postion1), 1).show();
//						// path.get(position)锟矫碉拷锟斤拷锟酵计拷锟铰凤拷锟斤拷锟斤拷锟斤�?//						String pathname = path.get(postion1);
//						final int curent = postion1;
//						// 锟斤拷示锟斤拷锟斤拷锟斤拷图片锟斤拷锟斤拷锟斤拷薷锟�?					Intent intent = new Intent(PictureManagerActivity.this,
//								EditPicture.class);
//						Bundle bundle = new Bundle();
//						bundle.putString("name", pathname);
//						intent.putExtras(bundle);
//						bundle.putInt("id", curent);
//						PictureManagerActivity.this.startActivity(intent);
//					}
//				});
//				Toast.makeText(PictureManagerActivity.this,
//						"锟斤拷前为锟斤拷" + (i + 1) + "�?, Toast.LENGTH_LONG).show();
//			}
//		});
//		// 锟斤拷一�?//		downPage.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				System.out.println("锟斤拷锟斤拷锟斤拷锟斤拷�?��锟斤拷钮");
//				i++;
//				imageAdapter = new ImageAdapter(path, i);
//				gridView.setAdapter(imageAdapter);
//				gridView.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						int postion1 = position + i * VIEW_COUNT;
//						Toast.makeText(getApplicationContext(),
//								path.get(postion1), 1).show();
//						// path.get(position)锟矫碉拷锟斤拷锟酵计拷锟铰凤拷锟斤拷锟斤拷锟斤�?//						String pathname = path.get(postion1);
//						final int curent = postion1;
//						// 锟斤拷示锟斤拷锟斤拷锟斤拷图片锟斤拷锟斤拷锟斤拷薷锟�?					Intent intent = new Intent(PictureManagerActivity.this,
//								EditPicture.class);
//						Bundle bundle = new Bundle();
//						bundle.putString("name", pathname);
//						intent.putExtras(bundle);
//						bundle.putInt("id", curent);
//						PictureManagerActivity.this.startActivity(intent);
//					}
//				});
//				Toast.makeText(PictureManagerActivity.this,
//						"锟斤拷前为锟斤拷" + (i + 1) + "�?, Toast.LENGTH_LONG).show();
//			}
//		});
//		// 锟斤拷锟�?	mark.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				if (b == true) {
//					System.out.println("锟斤拷锟酵计拷锟斤拷锟轿� + cbob.size());
//					for (int i = 0; i < cbob.size(); i++) {
//						cbob.get(i).setVisibility(View.VISIBLE);
//					}
//					Toast.makeText(PictureManagerActivity.this, "锟斤拷示锟斤拷锟�? 3)
//							.show();
//					b = false;
//				} else if (b == false) {
//					for (int i = 0; i < cbob.size(); i++) {
//						cbob.get(i).setVisibility(View.INVISIBLE);
//					}
//					Toast.makeText(PictureManagerActivity.this, "锟斤拷锟斤拷锟斤拷示", 3)
//							.show();
//					b = true;
//				}
//			}
//		});
//		// 锟斤拷锟斤拷删锟斤拷
//		deleteAll.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				new AlertDialog.Builder(PictureManagerActivity.this)
//						.setIcon(R.drawable.dialog)
//						.setTitle("锟斤拷确锟斤拷锟角凤拷删锟斤拷锟酵计�?
//						.setPositiveButton("删锟斤拷",
//								new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										for (int i = 0; i < arrayList.size(); i++) {
//											File file = new File(arrayList
//													.get(index));
//											file.delete();
//										}
//										Toast.makeText(
//												PictureManagerActivity.this,
//												"锟斤拷锟窖成癸拷删锟斤拷锟斤拷锟角碉拷图�?,
//												Toast.LENGTH_LONG).show();
//										// 为锟斤拷锟矫伙拷锟斤拷锟斤拷删锟斤拷锟斤拷效锟斤拷锟斤拷锟斤拷锟斤拷删锟斤拷锟斤拷俅位氐锟斤拷媒锟斤拷锟�?									Intent intent = new Intent(
//												PictureManagerActivity.this,
//												PictureManagerActivity.class);
//										PictureManagerActivity.this
//												.startActivity(intent);
//									}
//								})
//						.setNegativeButton("取锟斤拷",
//								new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										dialog.cancel();
//									}
//								}).create().show();
//			}
//		});
//
//		// 锟斤拷一锟轿斤拷锟斤拷时锟斤拷示锟斤�?2锟斤拷图片锟斤拷 锟斤拷锟揭伙拷沤锟斤拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟叫诧拷锟斤�?//		if(path != null){
//		imageAdapter = new ImageAdapter(path, i);
//		}else{
//			Toast.makeText(PictureManagerActivity.this, "sd锟斤拷锟斤拷没锟斤拷图片", Toast.LENGTH_LONG).show();
//		}
//		gridView.setAdapter(imageAdapter);
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Toast.makeText(getApplicationContext(), path.get(position), 1)
//						.show();
//				// path.get(position)锟矫碉拷锟斤拷锟酵计拷锟铰凤拷锟斤拷锟斤拷锟斤�?//				String pathname = path.get(position);
//				final int curent = position;
//				// 锟斤拷示锟斤拷锟斤拷锟斤拷图片锟斤拷锟斤拷锟斤拷薷锟�?			Intent intent = new Intent(PictureManagerActivity.this,
//						EditPicture.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("name", pathname);
//				intent.putExtras(bundle);
//				bundle.putInt("id", curent);
//				PictureManagerActivity.this.startActivity(intent);
//			}
//		});
//
//	}
//
//	// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟酵计�	private final class ImageAdapter extends BaseAdapter {
//		private List<String> mImagePath;
//		int i = 0;
//
//		public ImageAdapter(List<String> imagePath, int i) {
//			this.i = i;
//			this.mImagePath = imagePath;
//
//		}
//
//		@Override
//		public int getCount() {
//			// ori锟斤拷示锟斤拷目前为止锟斤拷前锟斤拷页锟斤拷锟杰癸拷锟侥革拷锟斤�?//			int ori = VIEW_COUNT * i;
//			// 值锟斤拷锟杰革拷锟斤�?前锟斤拷页锟侥革拷锟斤拷锟斤拷锟斤拷锟揭灰骋拷锟绞撅拷母锟斤拷锟斤拷锟斤拷锟侥拷系锟街敌★拷锟剿碉拷锟斤拷锟斤拷锟斤拷锟斤拷�?��锟斤拷只锟斤拷锟斤拷示锟斤拷么锟斤拷涂锟斤拷锟斤拷锟�?		if (mImagePath.size() - ori < VIEW_COUNT) {
//				return mImagePath.size() - ori;
//			}
//			// 锟斤拷锟斤拷默锟较碉拷�?锟斤拷要锟斤拷说锟斤拷一页锟斤拷示锟斤拷锟疥，锟斤拷要锟矫伙拷一页锟斤拷示锟斤拷锟斤拷一页锟斤拷默锟较碉拷�?锟斤拷示锟斤拷涂锟斤拷锟斤拷恕锟�?		else {
//				return VIEW_COUNT;
//			}
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return position;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			final int position1 = i * VIEW_COUNT + position;
//			if (position1 < mImagePath.size() && convertView == null) {
//				LayoutInflater inflater = (LayoutInflater) PictureManagerActivity.this
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = inflater.inflate(R.layout.cell, null);
//				ImageView image = (ImageView) convertView
//						.findViewById(R.id.cellImageView);
//				checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
//				// 锟斤拷锟斤拷checkBox
//				cbob.add(checkBox);
//
//				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//						// 锟斤拷锟斤拷玫锟酵计拷锟铰凤拷锟斤拷锟斤拷锟斤拷锟斤拷椋拷锟斤拷锟缴撅拷锟�锟斤拷锟狡诧拷锟斤拷
//						if (isChecked == true) {
//							name = path.get(position1);
//							System.out.println("锟斤拷锟�?锟斤拷锟斤拷锟� + name);
//							arrayList.add(name);
//						} else {
//							System.out.println("锟斤拷锟�?锟狡筹拷锟斤�? + name);
//							arrayList.remove(name);
//						}
//					}
//				});
//				checkBox.setVisibility(View.INVISIBLE);
//				// 锟斤拷锟斤拷锟节达拷锟斤拷锟斤拷锟斤拷锟�锟斤拷图片锟斤拷示锟斤拷gridView锟斤�?//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inJustDecodeBounds = true;
//				// 锟斤拷取锟斤拷锟酵计拷目锟酵革�?//				BitmapFactory.decodeFile(mImagePath.get(position1).toString(),
//						options); // 锟斤拷时锟斤拷锟斤拷bm为锟斤拷
//				options.inJustDecodeBounds = false;
//				// 锟斤拷锟斤拷锟斤拷锟脚憋�?//				int be = (int) (options.outHeight / (float) 200);
//				if (be <= 0)
//					be = 1;
//				options.inSampleSize = be;
//				// 锟斤拷锟铰讹拷锟斤拷图片锟斤拷注锟斤拷锟斤拷锟揭拷锟給ptions.inJustDecodeBounds 锟斤拷为 false�?//				Bitmap bitmap = BitmapFactory.decodeFile(
//						mImagePath.get(position1).toString(), options);
//				image.setScaleType(ScaleType.FIT_XY);
//				image.setImageBitmap(bitmap);
//			}
//			return convertView;
//		}
//	}
//
//}