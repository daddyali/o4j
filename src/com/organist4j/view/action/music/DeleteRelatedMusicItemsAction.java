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




public class DeleteRelatedMusicItemsAction extends Action implements ISelectionChangedListener {
	Shell s;
	ServiceVO service = null;
	Node n = null;

	public DeleteRelatedMusicItemsAction(ApplicationWindow w) {
		super();
		s = w.getShell();


		setText("&Delete all relations on Music Item");
	}
	public DeleteRelatedMusicItemsAction(TableViewer tbvItemBrowser) {
		super();
		
		s = tbvItemBrowser.getTable().getShell();
		setText("&Delete all relations on Music Item");
	}
	public void run() {

		//Dialog here 
		MusicItemBrowser tv = ServiceLocator.getInstance().getMusicItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
		if (sel.size() <= 0) {
			return;
		}
		MusicItemVO item = (MusicItemVO)sel.getFirstElement();
		String title = "Delete all Relations on Music Item";
		String msg = "Are you sure you want to delete all the relations on the music item " + item.getBookName() + " " + item.getName() + "?";

		if (sel.size() > 1) {
			title = "Delete all Relations on selected Music Items";
			msg = "Are you sure you want to delete all relations on the selected music items?";
		}

		if (!MessageDialog.openConfirm(s, title, msg)) {
			return;
		}

		//Relate all the music items. Do this by taking the first one,
		//then iterating around the next few adding them to the first
		Iterator<MusicItemVO> it = sel.iterator();
		MusicItemVO mi = null;
		while (it.hasNext()) {
			mi = it.next();
			if (mi.getRelatedItems() != null) mi.getRelatedItems().clear();
			
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(mi,true);
				
		}
		
		//Refresh the Music Browser
		ServiceLocator.getInstance().getMusicItemBrowser().refresh(true);
		
		//Refresh any open services


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		if (s.size() <= 0) { //only works for 1 or more
			this.setEnabled(false);
			return;
		} else {
			MusicItemVO item = (MusicItemVO)s.getFirstElement();
			if (item.getRelatedItems() != null && item.getRelatedItems().size() > 0) {
				this.setEnabled(true);
			} else {
				this.setEnabled(false);
			}
			
		}
		if (s.size() > 1) {
			setText("&Delete Relations to Music Items");
		} else {
			setText("&Delete Relations to Music Items");
		}
	}
}


