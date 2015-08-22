package com.organist4j.view.action.music;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Shell;

import com.organist4j.model.music.MusicItemVO;
import com.organist4j.model.service.ServiceVO;
import com.organist4j.util.Node;
import com.organist4j.util.ServiceLocator;
import com.organist4j.view.music.item.MusicItemBrowser;
import com.organist4j.view.service.explorer.ServiceExplorer;
import com.organist4j.view.service.plan.ServicePlanWorkbench;




public class DeleteMusicItemsAction extends Action implements ISelectionChangedListener {
	Shell s;
	ServiceVO service = null;
	Node n = null;

	public DeleteMusicItemsAction(ApplicationWindow w) {
		super();
		s = w.getShell();


		setText("&Delete Music Item");
	}
	public DeleteMusicItemsAction(TableViewer tbvItemBrowser) {
		super();
		
		s = tbvItemBrowser.getTable().getShell();
		setText("&Delete Music Item");
	}
	public void run() {

		//Dialog here 
		MusicItemBrowser tv = ServiceLocator.getInstance().getMusicItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
		if (sel.size() <= 0) {
			return;
		}
		MusicItemVO item = (MusicItemVO)sel.getFirstElement();
		String title = "Delete Music Item";
		String msg = "Are you sure you want to delete the music item " + item.getBookName() + " " + item.getName() + "?";

		if (sel.size() > 1) {
			title = "Delete Music Items";
			msg = "Are you sure you want to delete all the selected music items?";
		}

		if (!MessageDialog.openConfirm(s, title, msg)) {
			return;
		}

		//Delete all the music items
		Iterator<MusicItemVO> it = sel.iterator();
		while (it.hasNext()) {
			
			ServiceLocator.getInstance().getMusicDAO().deleteMusicItem(it.next());
		}
		
		//Refresh the Music Browser
		ServiceLocator.getInstance().getMusicItemBrowser().refresh(true);
		
		//Refresh any open services


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
			setText("&Delete Music Items");
		} else {
			setText("&Delete Music Item");
		}
	}
}

