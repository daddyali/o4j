package com.organist4j.view.action.music.editor;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.editor.MusicItemEditorRelated;




public class DeleteItemRelatedAction extends Action implements ISelectionChangedListener {
	MusicItemEditorRelated ed;

	public DeleteItemRelatedAction(MusicItemEditorRelated ed) {
		super();
		this.ed = ed;


		setText("&Delete Related Item");
	}

	public void run() {

		//Dialog here 		
		IStructuredSelection sel = (IStructuredSelection) ed.getTv().getSelection();
		if (sel.size() <= 0) {
			return;
		}
		MusicItemVO item = (MusicItemVO)sel.getFirstElement();
		String title = "Delete Related Item";
		String msg = "Are you sure you want to delete the related item " + item.getBookName() + " " + item.getName() + "?";

		if (sel.size() > 1) {
			title = "Delete Related Items";
			msg = "Are you sure you want to delete all the selected related music items?";
		}

		if (!MessageDialog.openConfirm(ed.getTv().getTable().getShell(), title, msg)) {
			return;
		}

		//Delete all the music items
		Iterator<MusicItemVO> it = sel.iterator();
		while (it.hasNext()) {
			item = it.next();
			if (item != ed.getItem()) {
				ed.getItem().removeRelatedItem(item);
				ServiceLocator.getInstance().getMusicDAO().updateMusicItem(item,true);
			}
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(ed.getItem(),true);
		}
		
		//Refresh the Music Browser
		ed.getTv().refresh(true);
		ServiceLocator.getInstance().getMusicItemBrowser().refresh();
		
		


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		if (s.size() <= 0) {
			this.setEnabled(false);
			return;
		} else {
			this.setEnabled(true);
		}
		if (s.size() > 1) {
			setText("&Delete Related Items");
		} else {
			setText("&Delete Related Item");
		}
	}
}

