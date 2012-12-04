package com.headbangers.reportmaker.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SpinnerAdapter;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.DrawableManager;
import com.headbangers.reportmaker.service.FilesystemService;

public class GalleryAdapter implements SpinnerAdapter {

	private Activity context;
	// private Battle battle;

	private DrawableManager dwManager;

	private FilesystemService fs = new FilesystemService();

	private List<String> extrasPath = new ArrayList<String>();

	public GalleryAdapter(Activity context, Battle battle) {
		this.context = context;
		extrasPath = fs.findAllExtrasPhotosPath(battle);
		dwManager = new DrawableManager();
	}

	@Override
	public int getCount() {
		return extrasPath.size();
	}

	@Override
	public Object getItem(int arg0) {
		return extrasPath.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View oldView, ViewGroup arg2) {

		ImageView view = (ImageView) oldView;
		if (view == null) {
			view = new ImageView(this.context);
		}

		view.setScaleType(ScaleType.FIT_CENTER);
		view.setLayoutParams(new Gallery.LayoutParams(150, 50));

		dwManager.fetchDrawableOnThread(this.context, extrasPath.get(position),
				view, R.drawable.damier);

		return view;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getDropDownView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
