package com.organist4j.view.action.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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




public class RelateMusicItemsAction extends Action implements ISelectionChangedListener {
	Shell s;
	ServiceVO service = null;
	Node n = null;

	public RelateMusicItemsAction(ApplicationWindow w) {
		super();
		s = w.getShell();


		setText("&Relate Music Item");
	}
	public RelateMusicItemsAction(TableViewer tbvItemBrowser) {
		super();
		
		s = tbvItemBrowser.getTable().getShell();
		setText("&Relate Music Item");
	}
	public void run() {

		//Dialog here 
		MusicItemBrowser tv = ServiceLocator.getInstance().getMusicItemBrowser();
		IStructuredSelection sel = (IStructuredSelection) tv.getTbvItemBrowser().getSelection();
		if (sel.size() <= 0) {
			return;
		}
		MusicItemVO item = (MusicItemVO)sel.getFirstElement();
		String title = "Relate Music Item";
		String msg = "Are you sure you want to relate the music item " + item.getBookName() + " " + item.getName() + "?";

		if (sel.size() > 1) {
			title = "Relate Music Items";
			msg = "Are you sure you want to relate all the selected music items?";
		}

		if (!MessageDialog.openConfirm(s, title, msg)) {
			return;
		}

		//Relate all the music items. Do this by taking the first one,
		//then iterating around the next few adding them to the first
		List<MusicItemVO> copySel = new ArrayList<MusicItemVO>();
		Iterator<MusicItemVO> it = sel.iterator();
		while (it.hasNext()) {
			copySel.add(it.next());
		}
		Iterator<MusicItemVO> thisit = sel.iterator();
		MusicItemVO thisItem = null;
		MusicItemVO thatItem = null;
		while (thisit.hasNext()) {
			thisItem = thisit.next();
			Iterator<MusicItemVO> thatit = copySel.iterator();
			while (thatit.hasNext()) {
				thatItem = thatit.next();
				if (!thisItem.equals(thatItem)) {
					thatItem.addRelatedItem(thisItem);
				}
			}
		}
		//Update each item
		thisit = sel.iterator();
		while (thisit.hasNext()) {
			ServiceLocator.getInstance().getMusicDAO().updateMusicItem(thisit.next(),true);
			
		}
		
		//Refresh the Music Browser
		ServiceLocator.getInstance().getMusicItemBrowser().refresh(true);
		
		//Refresh any open services


	}
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		IStructuredSelection s = (IStructuredSelection)arg0.getSelection();
		if (s.size() <= 1) { //only works for multiples
			this.setEnabled(false);
			return;
		} else {
			this.setEnabled(true);
		}
		if (s.size() > 1) {
			setText("&Relate Music Items");
		} else {
			setText("&Relate Music Item");
		}
	}
}

