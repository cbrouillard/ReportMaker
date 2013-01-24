package com.headbangers.reportmaker.listener;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.headbangers.reportmaker.R;
import com.headbangers.reportmaker.pojo.Battle;
import com.headbangers.reportmaker.service.FilesystemService;
import com.headbangers.reportmaker.tools.ImageHelper;

public class PhotoActionListener implements OnLongClickListener {

	private Fragment context;

	private Battle battle;
	private String photoName;
	private int resultCode;

	private FilesystemService fs = FilesystemService.getInstance();

	public PhotoActionListener(Fragment context, Battle battle,
			String photoPath, int returnedRequestCode) {
		this.context = context;
		this.battle = battle;
		this.resultCode = returnedRequestCode;
		this.photoName = photoPath;

	}

	@Override
	public boolean onLongClick(View v) {

		QuickAction qa = new QuickAction(context.getActivity());

		ActionItem replaceAction = new ActionItem();
		replaceAction.setTitle(this.context.getResources().getString(
				R.string.photo_replaceMenu));
		replaceAction.setIcon(this.context.getResources().getDrawable(
				R.drawable.edit));

		ActionItem newTakeAction = new ActionItem();
		newTakeAction.setTitle(this.context.getResources().getString(
				R.string.photo_newCaptureMenu));
		newTakeAction.setIcon(this.context.getResources().getDrawable(
				R.drawable.camera));

		ActionItem deleteAction = new ActionItem();
		deleteAction.setTitle(this.context.getResources().getString(
				R.string.photo_deleteMenu));
		deleteAction.setIcon(this.context.getResources().getDrawable(
				R.drawable.delete));

		qa.addActionItem(replaceAction);
		qa.addActionItem(newTakeAction);
		qa.addActionItem(deleteAction);

		qa.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

			@Override
			public void onItemClick(int pos) {
				switch (pos) {
				case 0:
					// replace
					break;
				case 1:
					// new
					ImageHelper.takePhoto(context, fs, battle,
							fs.determineNextPhotoName(battle, photoName),
							resultCode);
					break;
				case 2:
					// delete
					break;
				}
			}
		});

		qa.show(v);
		return true;
	}
}
