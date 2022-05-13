//package com.legent.ui.ext.dialogs;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Build;
//import android.os.Environment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.google.common.base.Strings;
//import com.google.common.collect.ComparisonChain;
//import com.google.common.collect.Lists;
//import com.legent.events.PageChangedEvent;
//import com.legent.ui.R;
//import com.legent.ui.UI;
//import com.legent.utils.EventUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.api.ViewUtils;
//
//import java.io.File;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//@SuppressLint("InflateParams")
//public class FileDialog {
//
//	static final public String sParent = "..";
//
//	static public void openFile(Context cx, String title,
//			final OpenFileCallback callback) {
//
//		final Adapter adapter = new Adapter(cx);
//		ListView lv = new ListView(cx);
//
//		Builder builder = new Builder(cx);
//		builder.setTitle(title);
//
//		final AlertDialog dlg = builder.create();
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				FileItem fi = (FileItem) adapter.getItem(position);
//				if (fi instanceof ParentFileItem) {
//					adapter.load(fi.file.getParentFile());
//				} else {
//					if (fi.isDirectory()) {
//						adapter.load(fi.file);
//					} else {
//						if (callback != null) {
//							dlg.dismiss();
//							callback.onOpen(fi.file);
//						}
//					}
//				}
//			}
//		});
//
//		dlg.setView(lv);
//		lv.setAdapter(adapter);
//		adapter.load(Environment.getExternalStorageDirectory());
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
//			dlg.setOnShowListener(new DialogInterface.OnShowListener() {
//				@Override
//				public void onShow(DialogInterface dialog) {
//					EventUtils.postEvent(new PageChangedEvent("dialogshow"));
//				}
//			});
//		}
//		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
//            }
//        });
//		dlg.show();
//	}
//
//	static public void saveFile(Context cx, String title, String defaultName,
//			final SaveFileCallback callback) {
//
//		final Adapter adapter = new Adapter(cx);
//		View view = LayoutInflater.from(cx).inflate(R.layout.dialog_file, null);
//		ListView lv = view.findViewById(R.id.lvFiles);
//		final EditText edtFileName = view
//				.findViewById(R.id.edtFileName);
//
//		edtFileName.setText(defaultName);
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				FileItem fi = (FileItem) adapter.getItem(position);
//				if (fi instanceof ParentFileItem) {
//					adapter.load(fi.file.getParentFile());
//				} else {
//					if (fi.isDirectory()) {
//						adapter.load(fi.file);
//					}
//				}
//			}
//		});
//
//		Builder builder = new Builder(cx);
//		builder.setTitle(title);
////		builder.setNegativeButton(UI.getStr_Cancel(cx), null);
//		builder.setNegativeButton("取消", null);
////		builder.setPositiveButton(UI.getStr_Ok(cx),
//		builder.setPositiveButton("确定",
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						ViewUtils.setDialogShowField(dialog, true);
//						String fileName = edtFileName.getText().toString();
//						if (Strings.isNullOrEmpty(fileName)) {
//							ViewUtils.setDialogShowField(dialog, false);
//							ToastUtils.showShort("file's name is empty");
//						} else {
//							fileName = adapter.getCurrent().getPath() + "/"
//									+ fileName;
//							if (callback != null) {
//								callback.onSave(fileName);
//							}
//						}
//					}
//				});
//
//		AlertDialog dlg = builder.create();
//		dlg.setView(view);
//		lv.setAdapter(adapter);
//		adapter.load(Environment.getExternalStorageDirectory());
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
//			dlg.setOnShowListener(new DialogInterface.OnShowListener() {
//				@Override
//				public void onShow(DialogInterface dialog) {
//					EventUtils.postEvent(new PageChangedEvent("dialogshow"));
//				}
//			});
//		}
//		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
//            }
//        });
//		dlg.show();
//	}
//
//	public interface SaveFileCallback {
//		void onSave(String fileName);
//	}
//
//	public interface OpenFileCallback {
//		void onOpen(File file);
//	}
//
//	static class FileItem {
//		File file;
//
//		public FileItem(File file) {
//			this.file = file;
//		}
//
//		public String getName() {
//			return file.getName();
//		}
//
//		public String getDesc() {
//			return file.getPath();
//		}
//
//		public boolean isDirectory() {
//			return file.isDirectory();
//		}
//
//		@Override
//		public String toString() {
//			return getDesc();
//		}
//	}
//
//	static class ParentFileItem extends FileItem {
//		public ParentFileItem(File file) {
//			super(file);
//		}
//
//		@Override
//		public String getName() {
//			return sParent;
//		}
//
//		@Override
//		public String getDesc() {
//			return "返回父目录";
//		}
//	}
//
//	static class Adapter extends BaseAdapter {
//
//		Context cx;
//		List<FileItem> list = Lists.newArrayList();
//		File file;
//
//		public Adapter(Context cx) {
//			this.cx = cx;
//		}
//
//		public File getCurrent() {
//			return file;
//		}
//
//		public void load(File file) {
//			File[] files = file.listFiles();
//			if (files == null)
//				return;
//
//			this.file = file;
//
//			list.clear();
//			notifyDataSetChanged();
//
//			for (File f : files) {
//				if (f.isDirectory() || f.isFile()) {
//					list.add(new FileItem(f));
//				}
//			}
//
//			Collections.sort(list, new Comparator<FileItem>() {
//
//				@Override
//				public int compare(FileItem lhs, FileItem rhs) {
//					int i = 0;
//					if (lhs.isDirectory() && rhs.isDirectory()) {
//						i = ComparisonChain.start()
//								.compare(lhs.getName(), rhs.getName()).result();
//					} else {
//						if (lhs.isDirectory()) {
//							i = -1;
//						} else {
//							i = 1;
//						}
//					}
//					return i;
//				}
//			});
//
//			if (file.getParentFile() != null) {
//				list.add(0, new ParentFileItem(file));
//			}
//
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHoler vh = null;
//			if (convertView == null) {
//				convertView = LayoutInflater.from(cx).inflate(
//						R.layout.dialog_file_item, parent, false);
//				vh = new ViewHoler(convertView);
//				convertView.setTag(vh);
//			} else {
//				vh = (ViewHoler) convertView.getTag();
//			}
//
//			FileItem fi = list.get(position);
//			vh.showItem(fi);
//
//			return convertView;
//		}
//
//		class ViewHoler {
//
//			ImageView img;
//			TextView txtName;
//			TextView txtDesc;
//
//			public ViewHoler(View view) {
//				img = view.findViewById(R.id.imgItem);
//				txtName = view.findViewById(R.id.txtName);
//				txtDesc = view.findViewById(R.id.txtDesc);
//			}
//
//			public void showItem(FileItem item) {
//				img.setImageResource(item.isDirectory() ? R.mipmap.ic_filedialog_folder
//						: R.mipmap.ic_filedialog_file);
//				txtName.setText(item.getName());
//				txtDesc.setText(item.getDesc());
//			}
//		}
//
//	}
//}
