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




public class ShowRelatedMusicItemsAction extends Action implements ISelectionChangedListener {
	Shell s;
	ServiceVO service = null;
	Node n = null;

	public ShowRelatedMusicItemsAction(ApplicationWindow w) {
		super();
		s = w.getShell();


		setText("&Show related Music Item(s)");
	}
	public ShowRelatedMusicItemsAction(TableViewer tbvItemBrowser) {
		super();
		
		s = tbvItemBrowser.getTable().getShell();
		setText("&Show related Music Item(s)");
	}
	public void run() {

		//Dialog here 
		MusicItemBrowser tv = ServiceLocator.getInstance().getMusicItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
		if (sel.size() <= 0) {
			return;
		}
		MusicItemVO item = (MusicItemVO)sel.getFirstElement();
//		String title = "Relate Music Item";
//		String msg = "Are you sure you want to relate the music item " + item.getBookName() + " " + item.getName() + "?";
//
//		if (sel.size() > 1) {
//			title = "Relate Music Items";
//			msg = "Are you sure you want to relate all the selected music items?";
//		}
//
//		if (!MessageDialog.openConfirm(s, title, msg)) {
//			return;
//		}

		//Relate all the music items. Do this by taking the first one,
		//then iterating around the next few adding them to the first
		ServiceLocator.getInstance().getMusicItemBrowser().setFilterByMusicItem(item);
		
		//Refresh the Music Browser
		ServiceLocator.getInstance().getMusicItemBrowser().refresh(true);
		
		//Refresh any open services


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		
		if (s.size() != 1) { //only works for singles
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
			setText("&Show Related Music Item(s)");
		} else {
			setText("&Show Related Music Item(s)");
		}
	}
}

